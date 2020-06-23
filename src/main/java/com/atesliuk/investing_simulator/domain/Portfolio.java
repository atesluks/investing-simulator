package com.atesliuk.investing_simulator.domain;

import com.atesliuk.investing_simulator.financials.FinancialApi;
import com.atesliuk.investing_simulator.financials.StockInfo;
import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity class of a Portfolio object. The class stores information about Portfolios
 */
@Entity
@Table(name = "portfolios")
public class Portfolio {

    //Id of a portfolio (as stored in the MySQL database)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    //Name of a portfolio
    @Column(name = "name")
    private String name;

    //Date when the portfolio was created
    @Column(name = "date_of_creation")
    private LocalDateTime dateOfCreation;

    //Starting capital for the portfolio
    @Column(name = "initial_investment")
    private Double initialInvestment;

    //Current cash available for the portfolio
    @Column(name = "cash")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double cash;

    //User which owes the portfolio. When retrieving a JSON Object of the portfolio, instead of the
    // full information abut user only user's id is shown
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "user_id")
    private User user;

    //List of all deals within that portfolio. Instead of providing full information about the deals,
    // only deals' ids are provided in JSON Object
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(mappedBy = "portfolio", cascade = {CascadeType.ALL})
    private List<Deal> deals;

    //this variable helps when making a POST request for saving a portfolio to reference a user
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long user_referenced_id;

    //this is a helper variable which is not stored in the MySQL database. The
    //variable shows what is current value of the portfolio (cash + current value of all stocks within it)
    @Transient
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double current_portfolio_value;

    //empty constructor
    public Portfolio() {
    }

    /**
     * Constructor with provided values assigned to some of the variables
     * @param name - name of the portfolio
     * @param dateOfCreation - date when the portfolio was created
     * @param initialInvestment - initial investment of the portfolio
     * @param user - user who's this portfolio is
     */
    public Portfolio(String name, LocalDateTime dateOfCreation, Double initialInvestment, User user) {
        this.name = name;
        this.dateOfCreation = dateOfCreation;
        this.initialInvestment = initialInvestment;
        this.user = user;
    }

    //Getters and setters

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

    //Overriden toString() method for better debugging
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

    /**
     * Helper method that calcualtes current value of the portfolio
     * @return - current value of the portfolio (cash + current value of all stocks in the portfolio)
     */
    private Double calculatePortfolioValue() {
        Double totalValue = this.getCash();

        //Looping over all deals within the portfolio, summing up value of those stocks that are not sold yet
        for (Deal deal : deals) {
            //if the deal is closed, then we won't count its value, because its already in the cash
            if (deal.getClosingDate()!=null)
                continue;

            //If the deal is not closed (the stocks are still in the portfolio) we get information about their
            //current value and add it to the total value of the portfolio
            StockInfo stock = FinancialApi.stocks.get(deal.getStockSymbol());
            if (stock.getPrice() != null) {
                totalValue +=deal.getAmount() * stock.getPrice();
            } else {
                //In case there are no information about some of the stocks
                System.err.println("Portfolio value for portfolio " + id + " is incorrect because not all stock quotes were retrieved yet. " +
                        "Try again later.");
                return -1.0;
            }
        }
        return totalValue;
    }

}
