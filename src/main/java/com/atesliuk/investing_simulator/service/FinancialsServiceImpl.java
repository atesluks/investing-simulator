package com.atesliuk.investing_simulator.service;

import com.atesliuk.investing_simulator.financials.FinancialApi;
import com.atesliuk.investing_simulator.financials.StockInfo;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinancialsServiceImpl implements FinancialsService{

    private FinancialApi financialApi;

    public FinancialsServiceImpl() {
        financialApi = new FinancialApi();
    }

    @Override
    public Map<String, StockInfo> getStocksQuotes(List<String> symbols) {
        Map<String, StockInfo> stocks = new HashMap<>();

        for (String s : symbols){
            if (!financialApi.getStocks().containsKey(s))
                throw new IllegalArgumentException("There are no stock with a symbol "+s+"!");
            stocks.put(s, financialApi.getStocks().get(s));
        }

        return stocks;
    }

    @Override
    public Map<String, StockInfo> getAllStockQuotes() {
        return financialApi.getStocks();
    }


}
