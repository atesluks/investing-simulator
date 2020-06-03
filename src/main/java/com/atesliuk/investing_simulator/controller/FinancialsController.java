package com.atesliuk.investing_simulator.controller;

import com.atesliuk.investing_simulator.financials.StockInfo;
import com.atesliuk.investing_simulator.service.FinancialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class FinancialsController {

    @Autowired
    private FinancialsService financialsService;

    @GetMapping("/financials")
    public Map<String, StockInfo> getAllStocksQuotes(){
        return financialsService.getAllStockQuotes();
    }

    @PostMapping("/financials")
    public Map<String, StockInfo> getStocksQuotes(@RequestBody List<String> symbols){
        return financialsService.getStocksQuotes(symbols);
    }

}
