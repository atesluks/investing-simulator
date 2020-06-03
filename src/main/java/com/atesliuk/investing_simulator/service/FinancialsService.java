package com.atesliuk.investing_simulator.service;

import com.atesliuk.investing_simulator.financials.StockInfo;

import java.util.List;
import java.util.Map;

public interface FinancialsService {

    Map<String, StockInfo> getStocksQuotes(List<String> symbols);

    Map<String, StockInfo> getAllStockQuotes();

}
