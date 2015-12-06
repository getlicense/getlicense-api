/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: ApiKey.java 38 2013-12-25 01:01:58Z recena $
 */
package es.klicap.getlicense.model;

import java.io.Serializable;

/**
 *
 */
public class ApiKey implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3745716188798053195L;

    /**
     *
     */
    private Long id;

    /**
     * With UUID format.
     */
    private String apikey;

    /**
     *
     */
    private Long creation;

    /**
     * User related with this ApiKey.
     */
    private User user;

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
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(final User user) {
        this.user = user;
    }

}
