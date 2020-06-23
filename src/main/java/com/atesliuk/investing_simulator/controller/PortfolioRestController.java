package com.atesliuk.investing_simulator.controller;

import com.atesliuk.investing_simulator.controller.exceptions.EntityNotFoundException;
import com.atesliuk.investing_simulator.domain.Portfolio;
import com.atesliuk.investing_simulator.service.PortfolioService;
import com.atesliuk.investing_simulator.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Controller class that handles REST API requests connected with Portfolio objects (Portfolio Entity)
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/")
public class PortfolioRestController {

    //Autowired Portfolio Service object that will be used further
    @Autowired
    private PortfolioService portfolioService;
    //Autowired User Service object that will be used further
    @Autowired
    private UserService userService;

    /**
     * The method handles GET request and returns the list of all portfolios in the database
     * @return - Iterable of Portfolio objects
     */
    @GetMapping("/portfolios")
    public Iterable<Portfolio> getAllPortfolios(){
        return portfolioService.getAllPortfolios();
    }

    /**
     * The method handles GET request and returns a particular portfolio (the one with the provided id as a path variable)
     * @param portfolioId - id of a searched portfolio
     * @return - Portfolio object
     */
    @GetMapping("/portfolios/{portfolioId}")
    public Portfolio getPortfolio(@PathVariable Long portfolioId){
        Portfolio thePortfolio = portfolioService.getPortfolio(portfolioId);

        //If there are no portfolio with such id, an exception will be thrown
        if (thePortfolio==null) throw new EntityNotFoundException("Portfolio id not found - " + portfolioId);
        return thePortfolio;
    }

    /**
     * The method handles POST request that saves a new portfolio
     * @param thePortfolio - a Portfolio object that has to be saved in the MySQL database
     * @return - the saved Portfolio object
     */
    @PostMapping("/portfolios")
    public Portfolio addPortfolio(@RequestBody Portfolio thePortfolio){
        //Getting user's id through the provided variable called user_referenced_id
        Long theUserId = thePortfolio.getUser_referenced_id();
        System.out.println("The passed portfolio for saving: "+thePortfolio);

        //If there are no users with such id, an eception is thrown
        if (theUserId == null)
            throw new NullPointerException("You did not provide ID of a user!");

        //If the provided initial investment number is zero or negative, an exception is thrown
        if (thePortfolio.getInitialInvestment()<=0)
            throw new IllegalArgumentException("Initial investment should be more than 0!");

        //Setting up user, id, date and other variables for the new portfolio
        thePortfolio.setUser(userService.getUser(theUserId));
        thePortfolio.setId(0L);
        thePortfolio.setDeals(new ArrayList<>());
        thePortfolio.setDateOfCreation(LocalDateTime.now());
        thePortfolio.setCash(thePortfolio.getInitialInvestment());
        return portfolioService.savePortfolio(thePortfolio);
    }

    /**
     * The method handles PUT request that updates existing portfolio
     * @param thePortfolio - a Portfolio object in a form it has to be updated
     * @return - an updated Portfolio object
     */
    @PutMapping("/portfolios")
    public Portfolio updatePortfolio(@RequestBody Portfolio thePortfolio){
        return portfolioService.savePortfolio(thePortfolio);
    }

    /**
     * The method handles DELETE request that deletes provided Portfolio object from the database
     * @param portfolioId - id of a portfolio that has to be deleted
     * @return - string with information if the portfolio has been deleted or not
     */
    @DeleteMapping("/portfolios/{portfolioId}")
    public String deletePortfolio(@PathVariable Long portfolioId){
        //Retrieving a Portfolio  object with such id
        Portfolio thePortfolio = portfolioService.getPortfolio(portfolioId);

        //If no portfolios with such id were found, an error message is returned
        if (thePortfolio == null){
            //throw new CustomerNotFoundException("Customer id not found - "+customerId);
            return "Error. User with id "+portfolioId+" was not found.";
        }

        //the portfolio is deleted and success message is returned
        portfolioService.deletePortfolio(thePortfolio);
        return "Deleted user id - " + portfolioId;
    }


}
