package com.atesliuk.investing_simulator.domain;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

@Entity
public class Deal implements Comparable{

    public final String ACTION_BUY="BUY";
    public final String ACTION_SELL="SELL";

    private Long id;
    private String action;
    private String stock;
    private Integer amount;
    private LocalDate date;
    private Long profit;

    @ManyToOne
    @JoinColumn(name = "")
    private Portfolio portfolio;


    @Override
    public int compareTo(Object o) {
        if (o instanceof Deal){
            Deal d = (Deal) o;
            return this.date.compareTo(d.date);
        }
        return 0;
    }
}
