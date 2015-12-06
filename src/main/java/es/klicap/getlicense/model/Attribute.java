/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: Attribute.java 270 2014-10-18 11:19:28Z recena $
 */
package es.klicap.getlicense.model;

import java.io.Serializable;

/**
 *
 */
public class Attribute implements Serializable {

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
    private Long licenseTypeIdPk;

    /**
     *
     * @param name
     * @param licenseTypeIdPk
     */
    public Attribute(final String name, final Long licenseTypeIdPk) {
        super();
        this.name = name;
        this.licenseTypeIdPk = licenseTypeIdPk;
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
     * @return the licenseTypeIdPk
     */
    public Long getLicenseTypeIdPk() {
        return licenseTypeIdPk;
    }

    /**
     * @param licenseTypeIdPk the licenseTypeIdPk to set
     */
    public void setLicenseTypeIdPk(final Long licenseTypeIdPk) {
        this.licenseTypeIdPk = licenseTypeIdPk;
    }

}
