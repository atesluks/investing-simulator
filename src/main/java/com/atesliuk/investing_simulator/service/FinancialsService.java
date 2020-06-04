package com.atesliuk.investing_simulator.service;

import com.atesliuk.investing_simulator.financials.StockInfo;

import java.util.Map;

public interface FinancialsService {

    Map<String, StockInfo> getAllStockQuotes();

    Boolean getKeepUpdating();

    void setKeepUpdating(Boolean keepUpdating);

}
