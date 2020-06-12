package com.atesliuk.investing_simulator.domain;

import com.atesliuk.investing_simulator.financials.StockInfo;
import com.atesliuk.investing_simulator.service.FinancialsService;
import com.fasterxml.jackson.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portfolios")
public class Portfolio {

    @Autowired
    @Transient
    private FinancialsService financialsService;

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

    @Column(name = "cash")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long cash;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(mappedBy = "portfolio", cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH})
    private List<PortfolioStock> portfolioStocks;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(mappedBy = "portfolio", cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH})
    private List<Deal> deals;

    //this variable helps when making a POST request for saving a portfolio to reference a user
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long user_referenced_id;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long current_portfolio_value;

    public Portfolio() {
    }

    public Portfolio(String name, LocalDate dateOfCreation, Long initialInvestment, User user) {
        this.name = name;
        this.dateOfCreation = dateOfCreation;
        this.initialInvestment = initialInvestment;
        this.user = user;
        this.portfolioStocks = new ArrayList<>();
        this.deals = new ArrayList<>();
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

    @JsonIgnore
    public Long getUser_referenced_id() {
        return user_referenced_id;
    }

    @JsonIgnore
    public void setUser_referenced_id(Long user_id) {
        this.user_referenced_id = user_id;
    }

    public Long getCurrent_portfolio_value() {
        this.current_portfolio_value = calculatePortfolioValue();
        return current_portfolio_value;
    }

    public void setCurrent_portfolio_value(Long current_portfolio_value) {
        this.current_portfolio_value = current_portfolio_value;
    }

    public Long getCash() {
        return cash;
    }

    public void setCash(Long cash) {
        this.cash = cash;
    }

    public void addStock(PortfolioStock thePortfolioStock) {
        if (portfolioStocks == null) {
            portfolioStocks = new ArrayList<>();
        }
        portfolioStocks.add(thePortfolioStock);
    }

    public void addDeal(Deal theDeal) {
        if (deals == null) {
            deals = new ArrayList<>();
        }
        deals.add(theDeal);
    }

    @Override
    public String toString() {
        return "Portfolio{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", dateOfCreation=" + dateOfCreation +
                ", initialInvestment=" + initialInvestment +
                ", cash=" + cash +
                ", user=" + user +
                (portfolioStocks==null ? "" : ", portfolioStocks="+ portfolioStocks)  +
                (deals == null ? "" : ", deals=" + deals) +
                ", user_referenced_id=" + user_referenced_id +
                ", current_portfolio_value=" + current_portfolio_value +
                '}';
    }

    private Long calculatePortfolioValue() {
        Long total_value = this.getCash();
        try{
            List<PortfolioStock> portfolioStocks = this.getPortfolioStocks();
            if (portfolioStocks == null || portfolioStocks.size() == 0) return total_value;
            for (PortfolioStock stock : this.getPortfolioStocks()) {
                String symbol = stock.getTicker();
                StockInfo stockInfo = financialsService.getAllStockQuotes().get(symbol);
                total_value += stock.getAmount() * stockInfo.getPrice();
            }
            return total_value;
        }catch(Exception e){
            System.out.println("Portfolio.calculatePortfolioValie() exception: "+e);
            e.printStackTrace();
        }
        return total_value;
    }

}
