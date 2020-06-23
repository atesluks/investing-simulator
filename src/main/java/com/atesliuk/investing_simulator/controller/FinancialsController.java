package com.atesliuk.investing_simulator.controller;

import com.atesliuk.investing_simulator.financials.StockInfo;
import com.atesliuk.investing_simulator.service.FinancialsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller that Handles API requests for the financial data of the stocks
 */
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api")
public class FinancialsController {

    //Autowired Financial Service object that will be sued further
    @Autowired
    private FinancialsService financialsService;

    /**
     * The method handles GET request that returns a HashMap of financial data for all stocks
     * @return - a HashMap where keys are stock symbols (String) and Values are StockInfo objects
     */
    @GetMapping("/financials")
    public Map<String, StockInfo> getAllStocksQuotes(){
        return financialsService.getAllStockQuotes();
    }

    /**
     * The method handles GET request and returns whether stock financial data is updating or not
     * @return - true if the stock updating process is ongoing, false if the process is stoped
     */
    @GetMapping("/financials/isUpdating")
    public Boolean getIsUpdating(){
        return financialsService.getKeepUpdating();
    }

    /**
     * The method launches or stops the process of updating stocks' financial information
     * @param isUpdatingValue - if the updating process should run or not
     * @return - true if the updating process is running, false if it is paused
     */
    @PostMapping("/financials/isUpdating/{isUpdatingValue}")
    public String setIsUpdaating(@PathVariable Boolean isUpdatingValue){
        financialsService.setKeepUpdating(isUpdatingValue);
        String result = isUpdatingValue? "Stocks are updating" : "Stocks stopped updating" ;
        return result;
    }

}
