package com.atesliuk.investing_simulator.domain;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity class of a Deal object. The class stores information about
 * Deals (stocks bought and sold within a portfolio)
 */
@Entity
@Table(name = "deal")
public class Deal implements Comparable{

    /**
     * Id of a deal as it is stored in the database
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * Symbol of a stock
     */
    @Column(name = "stock_symbol")
    private String stockSymbol;

    /**
     * Number of stocks bought
     */
    @Column(name = "amount")
    private Integer amount;

    /**
     * Date when the stocks were bought
     */
    @Column(name = "open_date")
    private LocalDateTime openDate;

    /**
     * Date when the stocks were sold
     */
    @Column(name = "closing_date")
    private LocalDateTime closingDate;

    /**
     * Price at which the stocks were bought  (per one stock)
     */
    @Column(name = "open_price")
    private Double openPrice;

    /**
     * Price per which the stocks were sold (per stock)
     */
    @Column(name = "closing_price")
    private Double closingPrice;

    //This variable helps when making a POST request for saving a deal to reference a portfolio
    //The variable is not included in the MySQL data table
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long portfolio_referenced_id;

    /**
     * Portfolio in which the deal was made
     */
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    /**
     * Empty constructor
     */
    public Deal() {
    }

    /**
     * Constructor with some of the variables assigned
     * @param stockSymbol - stock symbol
     * @param amount - number of stocks bought
     * @param openDate - date when the stocks were bought (the position was opened)
     * @param closingDate - date when the stocks were closed (the position was closed)
     * @param openPrice - price for which stocks were bought (per stock)
     * @param closingPrice - price for which stocks were sold (per stock)
     * @param portfolio - portfolio in which the deal was made
     */
    public Deal(String stockSymbol, Integer amount, LocalDateTime openDate, LocalDateTime closingDate, Double openPrice, Double closingPrice, Portfolio portfolio) {
        this.stockSymbol = stockSymbol;
        this.amount = amount;
        this.openDate = openDate;
        this.closingDate = closingDate;
        this.openPrice = openPrice;
        this.closingPrice = closingPrice;
        this.portfolio = portfolio;
    }

    //Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public void setStockSymbol(String stockSymbol) {
        this.stockSymbol = stockSymbol;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public LocalDateTime getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDateTime openDate) {
        this.openDate = openDate;
    }

    public LocalDateTime getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(LocalDateTime closingDate) {
        this.closingDate = closingDate;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Double getClosingPrice() {
        return closingPrice;
    }

    public void setClosingPrice(Double closingPrice) {
        this.closingPrice = closingPrice;
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(Portfolio portfolio) {
        this.portfolio = portfolio;
    }

    // portfolio_reference_id variable is not included in the JSON object when
    // this Deal object is retrieved vie HTTP request
    @JsonIgnore
    public Long getPortfolio_referenced_id() {
        return portfolio_referenced_id;
    }

    // value of portfolio_reference_id variable is not assigned when HTTP POST request is done
    @JsonIgnore
    public void setPortfolio_referenced_id(Long portfolio_referenced_id) {
        this.portfolio_referenced_id = portfolio_referenced_id;
    }

    // Overriden compareTo() method when comparing deals. Deals are compared by opening and closing date
    @Override
    public int compareTo(Object o) {
        if (o instanceof Deal){
            //if both are not closed
            if (this.closingDate != null && ((Deal) o).closingDate!=null){
                return this.openDate.compareTo(((Deal) o).openDate);
            //if this deal is closed and compared deal is not closed
            }else if (this.closingDate != null & ((Deal) o).closingDate==null){
                return -1;
            //if this deal is not closed and compared deal is closed
            }else if (this.closingDate == null && ((Deal) o).closingDate!= null){
                return 1;
            }else{
                return this.closingDate.compareTo(((Deal) o).closingDate);
            }
        }
        return 0;
    }

}
