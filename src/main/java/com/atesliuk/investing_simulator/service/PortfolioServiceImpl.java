package com.atesliuk.investing_simulator.service;

import com.atesliuk.investing_simulator.domain.Portfolio;
import com.atesliuk.investing_simulator.repository.PortfolioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PortfolioServiceImpl implements PortfolioService {

    @Autowired
    private PortfolioDAO portfolioDAO;

    @Override
    public Iterable<Portfolio> getAllPortfolios() {
        return portfolioDAO.findAll();
    }

    @Override
    public Portfolio getPortfolio(Long id) {
        Optional<Portfolio> result = portfolioDAO.findById(id);
        if (result.isPresent()) return result.get();
        else return null;
    }

    @Override
    public Portfolio savePortfolio(Portfolio thePortfolio) {
        return portfolioDAO.save(thePortfolio);
    }

    @Override
    public void deletePortfolio(Portfolio thePortfolio) {
        portfolioDAO.delete(thePortfolio);
    }
}
