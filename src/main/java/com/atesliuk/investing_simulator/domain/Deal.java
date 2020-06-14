package com.atesliuk.investing_simulator.domain;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "deal")
public class Deal implements Comparable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "stock_symbol")
    private String stockSymbol;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "open_date")
    private LocalDateTime openDate;

    @Column(name = "closing_date")
    private LocalDateTime closingDate;

    @Column(name = "open_price")
    private Double openPrice;

    @Column(name = "closing_price")
    private Double closingPrice;

    //this variable helps when making a POST request for saving a deal to reference a portfolio
    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long portfolio_referenced_id;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH})
    @JoinColumn(name = "portfolio_id")
    private Portfolio portfolio;

    public Deal() {
    }

    public Deal(String stockSymbol, Integer amount, LocalDateTime openDate, LocalDateTime closingDate, Double openPrice, Double closingPrice, Portfolio portfolio) {
        this.stockSymbol = stockSymbol;
        this.amount = amount;
        this.openDate = openDate;
        this.closingDate = closingDate;
        this.openPrice = openPrice;
        this.closingPrice = closingPrice;
        this.portfolio = portfolio;
    }

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

    @JsonIgnore
    public Long getPortfolio_referenced_id() {
        return portfolio_referenced_id;
    }

    @JsonIgnore
    public void setPortfolio_referenced_id(Long portfolio_referenced_id) {
        this.portfolio_referenced_id = portfolio_referenced_id;
    }

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
