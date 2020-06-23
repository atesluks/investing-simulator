package com.atesliuk.investing_simulator.service;

import com.atesliuk.investing_simulator.financials.FinancialApi;
import com.atesliuk.investing_simulator.financials.StockInfo;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Implementation of Financials Service. Handles connection
 * between Financials Controller and FinancialApi class that handles
 * ongoing stock data calculation and data retrieving process
 */
@Service
public class FinancialsServiceImpl implements FinancialsService{

    /**
     * Instance of FinancialApi class that is initialized in the constructor
     * and is used furthr in other methods
     */
    private FinancialApi financialApi;

    /**
     * A constructor that creates an instance of FinancialApi class
     */
    public FinancialsServiceImpl() {
        //turned it off while testing other stuff
        financialApi = new FinancialApi();
    }

    /**
     * The method returns HashMap of StockInfo objects each of which
     * stores quote information for all stocks
     * @return a HashMap of stock symbols (keys) and StockInfo objects (values)
     */
    @Override
    public Map<String, StockInfo> getAllStockQuotes() {
        return financialApi.getStocks();
    }

    /**
     * The method tells if the ongoing updating process is running or not
     * @return a boolean (true - is running, false - is paused)
     */
    @Override
    public Boolean getKeepUpdating() {
        return financialApi.isKeepUpdating();
    }

    /**
     * The method starts or stops the ongoing updating process
     * @param keepUpdating - true if the process should run,
     *                     false if the process should be paused
     */
    @Override
    public void setKeepUpdating(Boolean keepUpdating) {
        financialApi.setKeepUpdating(keepUpdating);
    }
}
