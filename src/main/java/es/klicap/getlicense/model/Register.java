/*
 * getlicense.io
 * Copyright (C) 2014 klicap - ingeniería del puzle
 *
 * $Id: Register.java 306 2014-11-16 12:31:04Z recena $
 */
package es.klicap.getlicense.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

/**
 *
 */
public class Register implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3745716188798053195L;

    /**
     * Registration identifier.
     */
    private String id;

    /**
     *
     */
    @NotEmpty
    private String email;

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
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(final String id) {
        this.id = id;
    }
}
