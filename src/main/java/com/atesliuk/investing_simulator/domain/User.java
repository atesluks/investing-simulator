package com.atesliuk.investing_simulator.domain;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.List;

/**
 * Entity class of a User object. The class stores information about
 * Users
 */
@Entity
@Table(name = "users")
public class User {

    //User's id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    //User's email
    @Column(name = "email")
    private String email;

    //User's first name
    @Column(name = "first_name")
    private String firstName;

    //User's last name
    @Column(name = "last_name")
    private String lastName;

    //USer's password (can be written only, can't be read (providedin JSON object) for security reasons)
    @Column(name = "password")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    //List of portfolios associated with this user (ony portfolio ids are provided in JSON object)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    @OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.DETACH,
            CascadeType.MERGE, CascadeType.REFRESH})
    private List<Portfolio> portfolios;

    //Empty constructor
    public User(){
    }

    /**
     * Constructor that assigns values to some of the fields
     * @param email - User's email
     * @param firstName - User's first name
     * @param lastName - User's last name
     * @param password - User's password
     */
    public User(String email, String firstName, String lastName, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    //Getters and setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public List<Portfolio> getPortfolios() {
        return portfolios;
    }

    public void setPortfolios(List<Portfolio> portfolios) {
        this.portfolios = portfolios;
    }

    //Overriden toString() method for debugging
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
