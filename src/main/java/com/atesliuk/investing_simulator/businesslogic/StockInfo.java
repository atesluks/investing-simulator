package com.atesliuk.investing_simulator.businesslogic;

public class StockInfo {

    private String companyName;
    private String symbol;
    private double price;
    private double dailyChangePercents;
    private String exchange;

    public StockInfo() {
    }

    public StockInfo(String companyName, String symbol, int price, double dailyChangePercents, String exchange) {
        this.companyName = companyName;
        this.symbol = symbol;
        this.price = price;
        this.dailyChangePercents = dailyChangePercents;
        this.exchange = exchange;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDailyChangePercents() {
        return dailyChangePercents;
    }

    public void setDailyChangePercents(double dailyChangePercents) {
        this.dailyChangePercents = dailyChangePercents;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    @Override
    public String toString() {
        return "StockInfo{" +
                "companyName='" + companyName + '\'' +
                ", symbol='" + symbol + '\'' +
                ", price=" + price +
                ", dailyChangePercents=" + dailyChangePercents +
                ", exchange=" + exchange +
                '}';
    }
}
