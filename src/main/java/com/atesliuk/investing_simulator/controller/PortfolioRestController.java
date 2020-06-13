package com.atesliuk.investing_simulator.controller;

import com.atesliuk.investing_simulator.controller.exceptions.EntityNotFoundException;
import com.atesliuk.investing_simulator.domain.Deal;
import com.atesliuk.investing_simulator.domain.Portfolio;
import com.atesliuk.investing_simulator.financials.FinancialApi;
import com.atesliuk.investing_simulator.service.DealService;
import com.atesliuk.investing_simulator.service.PortfolioService;
import com.atesliuk.investing_simulator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/")
public class PortfolioRestController {

    @Autowired
    private PortfolioService portfolioService;
    @Autowired
    private UserService userService;
    @Autowired
    private DealService dealService;

    @GetMapping("/portfolios")
    public Iterable<Portfolio> getAllPortfolios(){
        return portfolioService.getAllPortfolios();
    }

    @GetMapping("/portfolios/{portfolioId}")
    public Portfolio getPortfolio(@PathVariable Long portfolioId){
        Portfolio thePortfolio = portfolioService.getPortfolio(portfolioId);
        if (thePortfolio==null) throw new EntityNotFoundException("Portfolio id not found - " + portfolioId);
        return thePortfolio;
    }

    @PostMapping("/portfolios")
    public Portfolio addPortfolio(@RequestBody Portfolio thePortfolio){
        Long theUserId = thePortfolio.getUser_referenced_id();
        System.out.println("The passed portfolio for saving: "+thePortfolio);
        if (theUserId == null)
            throw new NullPointerException("You did not provide ID of a user!");
        if (thePortfolio.getInitialInvestment()<=0)
            throw new IllegalArgumentException("Initial investment should be more than 0!");

        thePortfolio.setUser(userService.getUser(theUserId));
        thePortfolio.setId(0L);
        thePortfolio.setPortfolioStocks(new ArrayList<>());
        thePortfolio.setDeals(new ArrayList<>());
        thePortfolio.setDateOfCreation(LocalDate.now());
        thePortfolio.setCash(thePortfolio.getInitialInvestment());
        return portfolioService.savePortfolio(thePortfolio);
    }

    @PutMapping("/portfolios")
    public Portfolio updatePortfolio(@RequestBody Portfolio thePortfolio){
        return portfolioService.savePortfolio(thePortfolio);
    }

    @DeleteMapping("/portfolios/{portfolioId}")
    public String deletePortfolio(@PathVariable Long portfolioId){
        Portfolio thePortfolio = portfolioService.getPortfolio(portfolioId);
        if (thePortfolio == null){
            //throw new CustomerNotFoundException("Customer id not found - "+customerId);
            return "Error. User with id "+portfolioId+" was not found.";
        }
        portfolioService.deletePortfolio(thePortfolio);
        return "Deleted user id - " + portfolioId;
    }

    //For getting and adding Deals (Buying and Selling stocks)

    @GetMapping("/portfolios/{portfolioId}/getAllDeals")
    public Iterable<Deal> getAllPortfolioDeals(@PathVariable Long portfolioId){
        return null;
    }

    @PostMapping("/portfolios/{portfolioId}/saveDeal")
    public Deal addDeal(@PathVariable Long portfolioId, @RequestBody Deal theDeal){
        Portfolio thePortfolio = portfolioService.getPortfolio(portfolioId);
        if (thePortfolio == null)
            throw new NullPointerException("Portfolio was not found in addDeal(). Portfolio id: "+portfolioId);

        String action = theDeal.getAction().toUpperCase();
        if (!(action.equals("BUY") || action.equals("SELL")))
            throw new IllegalArgumentException("Property 'action' in Deal object can be either 'BUY' or 'SELL'!");
        theDeal.setAction(action);

        if (action.equals("BUY"))
            theDeal.setProfit(0L);

        if (!FinancialApi.allStockSymbols.contains(theDeal.getTicker()))
            throw new IllegalArgumentException("There are no stock with name "+theDeal.getTicker());

        if (theDeal.getAmount()<=0)
            throw new IllegalArgumentException("Number of bought or sold stocks should be larger than 0!");

        theDeal.setId(0L);
        theDeal.setDate(LocalDate.now());

        theDeal.setPortfolio(thePortfolio);

        return dealService.saveDeal(theDeal);
    }



}
