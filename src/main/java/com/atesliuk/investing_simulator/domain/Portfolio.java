package com.atesliuk.investing_simulator.domain;

import com.atesliuk.investing_simulator.financials.FinancialApi;
import com.atesliuk.investing_simulator.financials.StockInfo;
import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.time.LocalDateTime;
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
    private LocalDateTime dateOfCreation;

    @Column(name = "initial_investment")
    private Double initialInvestment;

    @Column(name = "cash")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double cash;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(mappedBy = "portfolio", cascade = {CascadeType.ALL})
    private List<Deal> deals;

    //this variable helps when making a POST request for saving a portfolio to reference a user
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long user_referenced_id;

    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double current_portfolio_value;

    public Portfolio() {
    }

    public Portfolio(String name, LocalDateTime dateOfCreation, Double initialInvestment, Double cash, User user, List<Deal> deals, Long user_referenced_id, Double current_portfolio_value) {
        this.name = name;
        this.dateOfCreation = dateOfCreation;
        this.initialInvestment = initialInvestment;
        this.cash = cash;
        this.user = user;
        this.deals = deals;
        this.user_referenced_id = user_referenced_id;
        this.current_portfolio_value = current_portfolio_value;
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

    public LocalDateTime getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(LocalDateTime dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    public Double getInitialInvestment() {
        return initialInvestment;
    }

    public void setInitialInvestment(Double initialInvestment) {
        this.initialInvestment = initialInvestment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public Double getCurrent_portfolio_value() {
        this.current_portfolio_value = calculatePortfolioValue();
        return current_portfolio_value;
    }

    public void setCurrent_portfolio_value(Double current_portfolio_value) {
        this.current_portfolio_value = current_portfolio_value;
    }

    public Double getCash() {
        return cash;
    }

    public void setCash(Double cash) {
        this.cash = cash;
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
                (deals == null ? "" : ", deals=" + deals) +
                ", user_referenced_id=" + user_referenced_id +
                ", current_portfolio_value=" + current_portfolio_value +
                '}';
    }

    private Double calculatePortfolioValue() {
        Double totalValue = this.getCash();

        for (Deal deal : deals) {
            //if the deal is closed, then we won't count its value, because its already in the cash
            if (deal.getClosingDate()!=null)
                continue;
            StockInfo stock = FinancialApi.stocks.get(deal.getStockSymbol());
            if (stock.getPrice() != null) {
                totalValue +=deal.getAmount() * stock.getPrice();
            } else {
                System.err.println("Portfolio value for portfolio " + id + " is incorrect because not all stock quotes were retrieved yet. " +
                        "Try again later.");
            }
        }
        return totalValue;
    }

}
