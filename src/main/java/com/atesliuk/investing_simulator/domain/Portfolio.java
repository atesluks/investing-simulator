package com.atesliuk.investing_simulator.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(mappedBy = "portfolio",cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH})
    private List<PortfolioStock> portfolioStocks;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(mappedBy = "portfolio", cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH})
    private List<Deal> deals;

    public Portfolio() {
    }

    public Portfolio(String name, LocalDate dateOfCreation, Long initialInvestment, User user, List<PortfolioStock> portfolioStocks, List<Deal> deals) {
        this.name = name;
        this.dateOfCreation = dateOfCreation;
        this.initialInvestment = initialInvestment;
        this.user = user;
        this.portfolioStocks = portfolioStocks;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<PortfolioStock> getPortfolioStocks() {
        return portfolioStocks;
    }

    public void setPortfolioStocks(List<PortfolioStock> portfolioStocks) {
        this.portfolioStocks = portfolioStocks;
    }

    public List<Deal> getDeals() {
        return deals;
    }

    public void setDeals(List<Deal> deals) {
        this.deals = deals;
    }

    public void addStock(PortfolioStock thePortfolioStock){
        if (portfolioStocks ==null){
            portfolioStocks = new ArrayList<>();
        }
        portfolioStocks.add(thePortfolioStock);
    }

    public void addDeal(Deal theDeal){
        if (deals==null){
            deals = new ArrayList<>();
        }
        deals.add(theDeal);
    }
}
