package com.atesliuk.investing_simulator.service;

import com.atesliuk.investing_simulator.domain.Portfolio;

/**
 * Interface for Portfolio Service
 */
public interface PortfolioService {

    Iterable<Portfolio> getAllPortfolios();

    Portfolio getPortfolio(Long id);

    Portfolio savePortfolio(Portfolio thePortfolio);

    void deletePortfolio(Portfolio thePortfolio);

}
