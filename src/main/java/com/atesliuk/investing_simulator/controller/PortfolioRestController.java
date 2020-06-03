package com.atesliuk.investing_simulator.controller;

import com.atesliuk.investing_simulator.controller.exceptions.EntityNotFoundException;
import com.atesliuk.investing_simulator.domain.Portfolio;
import com.atesliuk.investing_simulator.service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/")
public class PortfolioRestController {

    @Autowired
    private PortfolioService portfolioService;

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
        thePortfolio.setId(0L);
        thePortfolio.setDateOfCreation(LocalDate.now());
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

}
