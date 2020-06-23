package com.atesliuk.investing_simulator.financials;

/**
 * POJO class that holds information about a stock's price in a particular time
 */
public class StockTimeSeriesData implements Comparable<StockTimeSeriesData>{

    //A refered point in time (date)
    private String date;
    //Open price of the stock at that date
    private Double open;
    //High price of the stock at that date
    private Double high;
    //Low price of the stock at that date
    private Double low;
    //Closing price of the stock at that date
    private Double close;
    //Traded volume of the stock at that date
    private Double volume;

    //Empty cosntructor
    public StockTimeSeriesData() {
    }

    //Constructor with assigned fields
    public StockTimeSeriesData(String date, Double open, Double high, Double low, Double close, Double volume) {
        this.date = date;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    //Getters and setters

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getOpen() {
        return open;
    }

    public void setOpen(Double open) {
        this.open = open;
    }

    public Double getHigh() {
        return high;
    }

    public void setHigh(Double high) {
        this.high = high;
    }

    public Double getLow() {
        return low;
    }

    public void setLow(Double low) {
        this.low = low;
    }

    public Double getClose() {
        return close;
    }

    public void setClose(Double close) {
        this.close = close;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    //Overriden toString() method
    @Override
    public String toString() {
        return "StockTimeSeriesData{" +
                "date='" + date + '\'' +
                ", open=" + open +
                ", high=" + high +
                ", low=" + low +
                ", close=" + close +
                ", volume=" + volume +
                '}';
    }

    //Overriden compereTo method. Compares two objects by dates.
    //Is used further in the app to sort the array
    @Override
    public int compareTo(StockTimeSeriesData o) {
        return this.getDate().compareTo(o.getDate());
    }
}
