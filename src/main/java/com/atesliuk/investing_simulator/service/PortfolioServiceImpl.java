package com.atesliuk.investing_simulator.service;

import com.atesliuk.investing_simulator.domain.Portfolio;
import com.atesliuk.investing_simulator.repository.PortfolioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of Portfolio Service. Handles connection
 * between Service Controller and Service DAO
 */
@Service
public class PortfolioServiceImpl implements PortfolioService {

    /**
     * An autowired PortfolioDAO object that wil be used further
     * for communication with the database
     */
    @Autowired
    private PortfolioDAO portfolioDAO;

    /**
     * The method returns the list of all existing Portfolios
     * in the database
     * @return an Iteratable of Portfolios
     */
    @Override
    public Iterable<Portfolio> getAllPortfolios() {
        return portfolioDAO.findAll();
    }

    /**
     * The method returns a particular Portfolio object
     * @param id - an ID of a searched portfolio
     * @return a Portfolio object that matches the id
     */
    @Override
    public Portfolio getPortfolio(Long id) {
        Optional<Portfolio> result = portfolioDAO.findById(id);
        if (result.isPresent()) return result.get();
        else return null;
    }

    /**
     * The method saves new or updates an existing portfolio
     * @param thePortfolio - a Portfolio object that has to be saved or updated
     * @return - a saved or updated Portfolio object
     */
    @Override
    public Portfolio savePortfolio(Portfolio thePortfolio) {
        return portfolioDAO.save(thePortfolio);
    }

    /**
     * The method deletes a portfolio from database
     * @param thePortfolio - a Portfolio object that has to be deleted
     */
    @Override
    public void deletePortfolio(Portfolio thePortfolio) {
        portfolioDAO.delete(thePortfolio);
    }
}
