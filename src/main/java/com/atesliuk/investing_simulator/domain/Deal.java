package com.atesliuk.investing_simulator.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "deal")
public class Deal implements Comparable{

    public final String ACTION_BUY="BUY";
    public final String ACTION_SELL="SELL";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "action")
    private String action;

    @Column(name = "ticker")
    private String ticker;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "date")
    private LocalDate date;

    @Column(name = "profit")
    private Long profit;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    public Deal() {
    }

    public Deal(String action, String ticker, Integer amount, LocalDate date, Long profit, Portfolio portfolio) {
        this.action = action;
        this.ticker = ticker;
        this.amount = amount;
        this.date = date;
        this.profit = profit;
        this.portfolio = portfolio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Long getProfit() {
        return profit;
    }

    public void setProfit(Long profit) {
        this.profit = profit;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Deal){
            Deal d = (Deal) o;
            return this.date.compareTo(d.date);
        }
        return 0;
    }

}
