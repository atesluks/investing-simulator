package com.atesliuk.investing_simulator.financials;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * POJO class, stores information about a stock
 */
public class StockInfo {

    //Full name of a company
    private String companyName;
    //Symbol of a company's stock
    private String symbol;
    //Current price of a company's stock
    private Double price;
    //Daily change of the stock (in %)
    private String dailyChangePercents;
    //Name of the exchange where the stocks are traded
    private String exchange;
    //Date and time when the information about the stock was updated for the last time
    private LocalDateTime lastUpdated;
    //List that contains time series data on the stock's prices
    private List<StockTimeSeriesData> timeSeries;

    //empty constructor, initializes timeSeries List
    public StockInfo() {
        timeSeries = new ArrayList<>();
    }

    //Constructor with assigned fields
    public StockInfo(String companyName, String symbol, Double price, String dailyChangePercents, String exchange, LocalDateTime lastUpdated) {
        this.companyName = companyName;
        this.symbol = symbol;
        this.price = price;
        this.dailyChangePercents = dailyChangePercents;
        this.exchange = exchange;
        this.lastUpdated = lastUpdated;
        timeSeries = new ArrayList<>();
    }

    //Getters and setters

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDailyChangePercents() {
        return dailyChangePercents;
    }

    public List<StockTimeSeriesData> getTimeSeries() {
        return timeSeries;
    }

    public void setTimeSeries(List<StockTimeSeriesData> timeSeries) {
        this.timeSeries = timeSeries;
    }

    public void setDailyChangePercents(String dailyChangePercents) {
        double change = Double.parseDouble(dailyChangePercents.substring(0, dailyChangePercents.length()-1));
        change = round(change, 1);
        if (change>0)
            this.dailyChangePercents = "+"+change+"%";
        else
            this.dailyChangePercents = change+"%";
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

    // Overriden toString() method for debugging
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

    /**
     * Helper method that round decimal numbers
     * @param value - number that has to be rounded
     * @param places - how many digits after come we want to leave
     * @return - rounded number
     */
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
