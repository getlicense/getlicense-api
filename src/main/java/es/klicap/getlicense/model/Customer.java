/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: Customer.java 305 2014-11-15 12:49:10Z recena $
 */
package es.klicap.getlicense.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

import flexjson.JSON;

/**
 *
 */
public class Customer implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -4572779905418053886L;

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private String company;

    /**
     * First name.
     */
    private String fname;

    /**
     * Last name.
     */
    private String lname;

    /**
     * Street (postal address).
     */
    private String street;

    /**
     * Postal code (postal address).
     */
    private String postalCode;

    /**
     * City (postal address).
     */
    private String city;

    /**
     * State (postal address).
     */
    private String state;

    /**
     * Country code (postal address), using ISO 3166-1.
     */
    @NotEmpty
    private String country;

    /**
     * Email (contact).
     */
    private String email;

    /**
     * Website (contact).
     */
    private String web;

    /**
     * Tax ID, p.e. ES91858241
     */
    @NotEmpty
    private String taxid;

    /**
     * Owner.
     */
    private User owner;

    /**
     * Logical remove.
     */
    private Long deletion;


    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(final Long id) {
        this.id = id;
    }

    /**
     * @return the owner
     */
    @JSON(include = false)
    public User getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(final User owner) {
        this.owner = owner;
    }

    /**
     * @return the deletion
     */
    @JSON(include = false)
    public Long getDeletion() {
        return deletion;
    }

    /**
     * @param deletion the deletion to set
     */
    public void setDeletion(final Long deletion) {
        this.deletion = deletion;
    }

    /**
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company the company to set
     */
    public void setCompany(final String company) {
        this.company = company;
    }

    /**
     * @return the fname
     */
    public String getFname() {
        return fname;
    }

    /**
     * @param fname the fname to set
     */
    public void setFname(final String fname) {
        this.fname = fname;
    }

    /**
     * @return the lname
     */
    public String getLname() {
        return lname;
    }

    /**
     * @param lname the lname to set
     */
    public void setLname(final String lname) {
        this.lname = lname;
    }

    /**
     * @return the street
     */
    public String getStreet() {
        return street;
    }

    /**
     * @param street the street to set
     */
    public void setStreet(final String street) {
        this.street = street;
    }

    /**
     * @return the postalCode
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @param postalCode the postalCode to set
     */
    public void setPostalCode(final String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(final String city) {
        this.city = city;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(final String state) {
        this.state = state;
    }

    /**
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(final String country) {
        this.country = country;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(final String email) {
        this.email = email;
    }

    /**
     * @return the web
     */
    public String getWeb() {
        return web;
    }

    /**
     * @param web the web to set
     */
    public void setWeb(final String web) {
        this.web = web;
    }

    /**
     * @return the taxid
     */
    public String getTaxid() {
        return taxid;
    }

    /**
     * @param taxid the taxid to set
     */
    public void setTaxid(final String taxid) {
        this.taxid = taxid;
    }
}
