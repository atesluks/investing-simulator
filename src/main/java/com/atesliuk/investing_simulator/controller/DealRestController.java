package com.atesliuk.investing_simulator.controller;

import com.atesliuk.investing_simulator.controller.exceptions.EntityNotFoundException;
import com.atesliuk.investing_simulator.domain.Deal;
import com.atesliuk.investing_simulator.domain.Portfolio;
import com.atesliuk.investing_simulator.financials.FinancialApi;
import com.atesliuk.investing_simulator.service.DealService;
import com.atesliuk.investing_simulator.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * Controller class that handles REST API requests connected with Deal objects (Deal Entity)
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class DealRestController {

    //Autowired Deal Service object that will be used further
    @Autowired
    private DealService dealService;
    //Autowired Portfolio Service object that will be used further
    @Autowired
    private PortfolioService portfolioService;

    /**
     * The method handles GET request and returns the list of all deals in the database
     * @return - Iterable of Deal objects
     */
    @GetMapping("/deals")
    public Iterable<Deal> getAllDeals() {
        return dealService.getAllDeals();
    }

    /**
     * The method handles GET request and returns a particular deal (the one with the provided id as a path variable)
     * @param dealId - id of a searched deal
     * @return - Deal object
     */
    @GetMapping("/deals/{dealId}")
    public Deal getDeal(@PathVariable Long dealId) {
        //Retrieving a Dela object with the provided id from the DEal Service
        Deal theDeal = dealService.getDeal(dealId);

        //If no deals with such is were found, an exception is thrown
        if (theDeal == null) throw new EntityNotFoundException("Deal id not found - " + dealId);
        return theDeal;
    }

    /**
     * The method handles POST request that saves a new deal
     * @param theDeal - a Deal object that has to be saved in the MySQL database
     * @return - the saved Deal object
     */
    @PostMapping("/deals")
    public Deal addDeal(@RequestBody Deal theDeal) {
        //Getting portfolio's id through the provided variable called portfolio_referenced_id
        Long thePortfolioId = theDeal.getPortfolio_referenced_id();

        //If no portfolio id was provided, an exception will be thrown
        if (thePortfolioId == null)
            throw new NullPointerException("You did not provide ID of a portfolio!");

        //If the stock amount is zero or negative, an exception is thrown
        if (theDeal.getAmount()<=0)
            throw new IllegalArgumentException("Stock amount should be more than 0!");

        //If the wrong stock symbol is provided, an exception will be thrown
        String symbol = theDeal.getStockSymbol();
        if (!FinancialApi.stocks.containsKey(symbol))
            throw new NullPointerException("Stock with name'"+symbol+"' was not found");

        //Searching for a portfolio with such id
        Portfolio thePortfolio = portfolioService.getPortfolio(thePortfolioId);

        //REtrieving the stock's current price
        Double price = FinancialApi.stocks.get(symbol).getPrice();

        //If there are no enough cash (free money) in the portfolio for buing those stocks, an exception will be thrown
        if (price * theDeal.getAmount() > thePortfolio.getCash())
            throw new IllegalArgumentException("You do not have enough cash in the portfolio! Free cash: "+thePortfolio.getCash()+
                    ", total price for all stocks: "+price * theDeal.getAmount());

        //Seting up other variables
        theDeal.setPortfolio(thePortfolio);
        theDeal.setId(0L);
        theDeal.setOpenDate(LocalDateTime.now());
        theDeal.setOpenPrice(price);

        //Saving the deal
        thePortfolio.setCash(thePortfolio.getCash() - price * theDeal.getAmount());
        return dealService.saveDeal(theDeal);
    }

    /**
     * The method handles PUT request that closes existing deal (sells stocks of that deal)
     * @param dealId - deal's id passed as a path avriable
     * @return - an updated Deal object
     */
    @PutMapping("/deals/{dealId}")
    public Deal closeDeal(@PathVariable Long dealId) {
        //Retrieving a Deal object from Deal Service
        Deal theDeal = dealService.getDeal(dealId);

        //If no deals with such ids were found, an exceptio is thrown
        if (theDeal==null)
            throw new NullPointerException("Deal was not found. Deal id: "+dealId);

        //Retrieving current prie of the stocks within that deal
        Double price = FinancialApi.stocks.get(theDeal.getStockSymbol()).getPrice();
        theDeal.setClosingPrice(price);

        //Adjusting amount of cash in the portfolio
        Portfolio thePortfolio = theDeal.getPortfolio();
        thePortfolio.setCash(thePortfolio.getCash() + price * theDeal.getAmount());

        //Setting closing time for the portfolio
        theDeal.setClosingDate(LocalDateTime.now());
        return dealService.saveDeal(theDeal);
    }

}
