package com.atesliuk.investing_simulator.domain;

import javax.persistence.*;

@Entity
@Table(name = "stocks")
public class Stock {

    @Column(name = "amount")
    private Long amount;

    @Column(name = "ticker")
    private String ticker;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    public Stock() {
    }

    public Stock(Long amount, String ticker, Portfolio portfolio) {
        this.amount = amount;
        this.ticker = ticker;
        this.portfolio = portfolio;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
}
