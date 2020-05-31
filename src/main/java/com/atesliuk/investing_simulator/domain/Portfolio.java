package com.atesliuk.investing_simulator.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "portfolio")
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

    //private List<Deal> tradingHistory;



    public Portfolio() {
    }

    public Portfolio(String name, LocalDate dateOfCreation, Long initialInvestment, Long freeCash) {
        this.name = name;
        this.dateOfCreation = dateOfCreation;
        this.initialInvestment = initialInvestment;
        this.freeCash = freeCash;
    }
}
