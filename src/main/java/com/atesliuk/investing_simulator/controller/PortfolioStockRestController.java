package com.atesliuk.investing_simulator.controller;

import com.atesliuk.investing_simulator.controller.exceptions.EntityNotFoundException;
import com.atesliuk.investing_simulator.domain.PortfolioStock;
import com.atesliuk.investing_simulator.service.PortfolioStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PortfolioStockRestController {

    @Autowired
    private PortfolioStockService portfolioStockService;

    @GetMapping("/portfolioStocks")
    public Iterable<PortfolioStock> getAllPortfolioStocks() {
        return portfolioStockService.getAllPortfolioStocks();
    }

    @GetMapping("/portfolioStocks/{portfolioStockId}")
    public PortfolioStock getPortfolioStock(@PathVariable Long portfolioStockId) {
        PortfolioStock thePortfolioStock = portfolioStockService.getPortfolioStock(portfolioStockId);
        if (thePortfolioStock == null) throw new EntityNotFoundException("Deal id not found - " + portfolioStockId);
        return thePortfolioStock;
    }

    @PostMapping("/portfolioStocks")
    public PortfolioStock addPortfolioStock(@RequestBody PortfolioStock thePortfolioStock) {
        thePortfolioStock.setId(0L);
        return portfolioStockService.savePortfolioStock(thePortfolioStock);
    }

    @PutMapping("/portfolioStocks")
    public PortfolioStock updatePortfolioStock(@RequestBody PortfolioStock thePortfolioStock) {
        return portfolioStockService.savePortfolioStock(thePortfolioStock);
    }

    @DeleteMapping("/portfolioStocks/{portfolioStockId}")
    public String deletePortfolioStock(@PathVariable Long portfolioStockId) {
        PortfolioStock thePortfolioStock = portfolioStockService.getPortfolioStock(portfolioStockId);
        if (thePortfolioStock == null) {
            return "Error. User with id " + portfolioStockId + " was not found.";
        }
        portfolioStockService.deletePortfolioStock(thePortfolioStock);
        return "Deleted user id - " + portfolioStockId;
    }

}
