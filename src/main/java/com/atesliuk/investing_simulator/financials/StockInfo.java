package com.atesliuk.investing_simulator.financials;

import java.time.LocalDateTime;

public class StockInfo {

    private String companyName;
    private String symbol;
    private String price;
    private String dailyChangePercents;
    private String exchange;
    private LocalDateTime lastUpdated;

    public StockInfo() {
    }

    public StockInfo(String companyName, String symbol, String price, String dailyChangePercents, String exchange, LocalDateTime lastUpdated) {
        this.companyName = companyName;
        this.symbol = symbol;
        this.price = price;
        this.dailyChangePercents = dailyChangePercents;
        this.exchange = exchange;
        this.lastUpdated = lastUpdated;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDailyChangePercents() {
        return dailyChangePercents;
    }

    public void setDailyChangePercents(String dailyChangePercents) {
        this.dailyChangePercents = dailyChangePercents;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "StockInfo{" +
                "companyName='" + companyName + '\'' +
                ", symbol='" + symbol + '\'' +
                ", price=" + price +
                ", dailyChangePercents=" + dailyChangePercents +
                ", exchange=" + exchange +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}
