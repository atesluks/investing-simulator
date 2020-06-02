package com.atesliuk.investing_simulator.service;

import com.atesliuk.investing_simulator.domain.PortfolioStock;
import com.atesliuk.investing_simulator.repository.PortfolioStockDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PortfolioStockServiceImpl implements PortfolioStockService {

    @Autowired
    private PortfolioStockDAO portfolioStockDAO;

    @Override
    public Iterable<PortfolioStock> getAllPortfolioStocks() {
        return portfolioStockDAO.findAll();
    }

    @Override
    public PortfolioStock getPortfolioStock(Long id) {
        Optional<PortfolioStock> result = portfolioStockDAO.findById(id);
        if (result.isPresent()) return result.get();
        else return null;
    }

    @Override
    public PortfolioStock savePortfolioStock(PortfolioStock thePortfolioStock) {
        Long amount = thePortfolioStock.getAmount();
        if (amount<=0L) throw new IllegalArgumentException("PortfolioStock amount should be more than 0!");
        return portfolioStockDAO.save(thePortfolioStock);
    }

    @Override
    public void deletePortfolioStock(PortfolioStock thePortfolioStock) {
        portfolioStockDAO.delete(thePortfolioStock);
    }
}