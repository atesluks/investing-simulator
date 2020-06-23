package com.atesliuk.investing_simulator.service;

import com.atesliuk.investing_simulator.financials.StockInfo;

import java.util.Map;

/**
 * Interface for Financial Service
 */
public interface FinancialsService {

    Map<String, StockInfo> getAllStockQuotes();

    Boolean getKeepUpdating();

    void setKeepUpdating(Boolean keepUpdating);

}
