package com.atesliuk.investing_simulator.service;

import com.atesliuk.investing_simulator.financials.FinancialApi;
import com.atesliuk.investing_simulator.financials.StockInfo;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FinancialsServiceImpl implements FinancialsService{

    private FinancialApi financialApi;

    public FinancialsServiceImpl() {
        //turned it off while testing other stuff
        financialApi = new FinancialApi();
    }

    @Override
    public Map<String, StockInfo> getAllStockQuotes() {
        return financialApi.getStocks();
    }

    @Override
    public Boolean getKeepUpdating() {
        return financialApi.isKeepUpdating();
    }

    @Override
    public void setKeepUpdating(Boolean keepUpdating) {
        financialApi.setKeepUpdating(keepUpdating);
    }
}
