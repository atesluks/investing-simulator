package com.atesliuk.investing_simulator.domain;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portfolios")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "date_of_creation")
    private LocalDate dateOfCreation;

    @Column(name = "initial_investment")
    private Long initialInvestment;

    @Column(name = "free_cash")
    private Long freeCash;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "portfolio", cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH})
    private List<Deal> tradingHistory;

    @OneToMany(mappedBy = "portfolio",cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH})
    private List<Stock> stocks;

    @OneToMany(mappedBy = "portfolio", cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH})
    private List<Deal> deals;

    public Portfolio() {
    }

    public Portfolio(String name, LocalDate dateOfCreation, Long initialInvestment, Long freeCash, User user, List<Deal> tradingHistory, List<Stock> stocks, List<Deal> deals) {
        this.name = name;
        this.dateOfCreation = dateOfCreation;
        this.initialInvestment = initialInvestment;
        this.freeCash = freeCash;
        this.user = user;
        this.tradingHistory = tradingHistory;
        this.stocks = stocks;
        this.deals = deals;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDate dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Long getInitialInvestment() {
        return initialInvestment;
    }

    public void setInitialInvestment(Long initialInvestment) {
        this.initialInvestment = initialInvestment;
    }

    public Long getFreeCash() {
        return freeCash;
    }

    public void setFreeCash(Long freeCash) {
        this.freeCash = freeCash;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Deal> getTradingHistory() {
        return tradingHistory;
    }

    public void setTradingHistory(List<Deal> tradingHistory) {
        this.tradingHistory = tradingHistory;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    public List<Deal> getDeals() {
        return deals;
    }

    public void setDeals(List<Deal> deals) {
        this.deals = deals;
    }

    public void addStock(Stock theStock){
        if (stocks==null){
            stocks = new ArrayList<>();
        }
        stocks.add(theStock);
    }

    public void addDeal(Deal theDeal){
        if (deals==null){
            deals = new ArrayList<>();
        }
        deals.add(theDeal);
    }
}
