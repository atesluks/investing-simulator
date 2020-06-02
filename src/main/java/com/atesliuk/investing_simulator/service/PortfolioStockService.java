package com.atesliuk.investing_simulator.service;

import com.atesliuk.investing_simulator.domain.PortfolioStock;

public interface PortfolioStockService {

    Iterable<PortfolioStock> getAllPortfolioStocks();

    PortfolioStock getPortfolioStock(Long id);

    PortfolioStock savePortfolioStock(PortfolioStock thePortfolioStock);

    void deletePortfolioStock(PortfolioStock thePortfolioStock);

}
