/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: User.java 384 2015-04-05 11:38:18Z recena $
 */
package es.klicap.getlicense.model;

import java.io.Serializable;

import flexjson.JSON;

/**
 *
 */
public class User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3745716188798053195L;

    /**
     *
     */
    private Long id;

    /**
     *
     */
    private String username;

    /**
     *
     */
    private String email;

    /**
     *
     */
    private Long creation;

    /**
     * Logical remove.
     */
    private Long deletion;

    /**
     * ApiKey.
     */
    private String apikey;

    /**
     * Hashed password.
     */
    private String password;

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
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(final String username) {
        this.username = username;
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
     * @return the creation
     */
    public Long getCreation() {
        return creation;
    }

    /**
     * @param creation the creation to set
     */
    public void setCreation(final Long creation) {
        this.creation = creation;
    }

    /**
     * @return the apikey
     */
    public String getApikey() {
        return apikey;
    }

    /**
     * @param apikey the apikey to set
     */
    public void setApikey(final String apikey) {
        this.apikey = apikey;
    }

    /**
     * @return the password
     */
    @JSON(include = false)
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(final String password) {
        this.password = password;
    }

}
