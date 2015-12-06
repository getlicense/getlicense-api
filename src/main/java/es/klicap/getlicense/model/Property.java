/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: Property.java 269 2014-10-18 10:28:20Z recena $
 */
package es.klicap.getlicense.model;

import java.io.Serializable;

/**
 *
 */
public class Property implements Serializable {

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
    private String name;

    /**
     *
     */
    private String value;

    /**
     *
     */
    private Long licenseIdPk;

    /**
     *
     * @param name
     * @param value
     */
    public Property() {
    }

    /**
     *
     * @param name
     * @param value
     */
    public Property(final String name, final String value) {
        super();
        this.name = name;
        this.value = value;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(final String value) {
        this.value = value;
    }

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
     * @return the licenseIdPK
     */
    public Long getLicenseIdPk() {
        return licenseIdPk;
    }

    /**
     * @param licenseIdPk the licenseIdPk to set
     */
    public void setLicenseIdPk(final Long licenseIdPk) {
        this.licenseIdPk = licenseIdPk;
    }

}
