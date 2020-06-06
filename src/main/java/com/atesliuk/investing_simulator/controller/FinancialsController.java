package com.atesliuk.investing_simulator.controller;

import com.atesliuk.investing_simulator.financials.StockInfo;
import com.atesliuk.investing_simulator.service.FinancialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class FinancialsController {

    @Autowired
    private FinancialsService financialsService;

    @GetMapping("/financials")
    public Map<String, StockInfo> getAllStocksQuotes(){
        return financialsService.getAllStockQuotes();
    }

    @GetMapping("/financials/isUpdating")
    public Boolean getIsUpdating(){
        return financialsService.getKeepUpdating();
    }

    @PostMapping("/financials/isUpdating/{isUpdatingValue}")
    public String setIsUpdaating(@PathVariable Boolean isUpdatingValue){
        financialsService.setKeepUpdating(isUpdatingValue);
        String result = isUpdatingValue? "Stocks are updating" : "Stocks stopped updating" ;
        return result;
    }

}
