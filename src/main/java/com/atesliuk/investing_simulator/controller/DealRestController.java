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

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class DealRestController {

    @Autowired
    private DealService dealService;
    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/deals")
    public Iterable<Deal> getAllDeals() {
        return dealService.getAllDeals();
    }

    @GetMapping("/deals/{dealId}")
    public Deal getDeal(@PathVariable Long dealId) {
        Deal theDeal = dealService.getDeal(dealId);
        if (theDeal == null) throw new EntityNotFoundException("Deal id not found - " + dealId);
        return theDeal;
    }

    @PostMapping("/deals")
    public Deal addDeal(@RequestBody Deal theDeal) {
        Long thePortfolioId = theDeal.getPortfolio_referenced_id();

        if (thePortfolioId == null)
            throw new NullPointerException("You did not provide ID of a portfolio!");
        if (theDeal.getAmount()<=0)
            throw new IllegalArgumentException("Stock amount should be more than 0!");
        String symbol = theDeal.getStockSymbol();
        if (!FinancialApi.stocks.containsKey(symbol))
            throw new NullPointerException("Stock with name'"+symbol+"' was not found");

        Portfolio thePortfolio = portfolioService.getPortfolio(thePortfolioId);

        Double price = FinancialApi.stocks.get(symbol).getPrice();
        if (price * theDeal.getAmount() > thePortfolio.getCash())
            throw new IllegalArgumentException("You do not have enough cash in the portfolio! Free cash: "+thePortfolio.getCash()+
                    ", total price for all stocks: "+price * theDeal.getAmount());

        theDeal.setPortfolio(thePortfolio);
        theDeal.setId(0L);
        theDeal.setOpenDate(LocalDateTime.now());
        theDeal.setOpenPrice(price);

        thePortfolio.setCash(thePortfolio.getCash() - price * theDeal.getAmount());
        return dealService.saveDeal(theDeal);
    }

    @PutMapping("/deals/{dealId}")
    public Deal closeDeal(@PathVariable Long dealId) {
        Deal theDeal = dealService.getDeal(dealId);
        if (theDeal==null)
            throw new NullPointerException("Deal was not found. Deal id: "+dealId);

        Double price = FinancialApi.stocks.get(theDeal.getStockSymbol()).getPrice();
        theDeal.setClosingPrice(price);

        Portfolio thePortfolio = theDeal.getPortfolio();
        thePortfolio.setCash(thePortfolio.getCash() + price * theDeal.getAmount());

        theDeal.setClosingDate(LocalDateTime.now());
        return dealService.saveDeal(theDeal);
    }

}
