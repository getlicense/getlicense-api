/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: Product.java 375 2015-04-03 11:03:14Z recena $
 */
package es.klicap.getlicense.model;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotEmpty;

import flexjson.JSON;

/**
 *
 */
public class Product implements Serializable {

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
    @NotEmpty
    private String name;

    /**
     * Owner.
     */
    private User owner;

    /**
     * Logical remove.
     */
    private Long deletion;

    /**
     * Number of license types.
     */
    private Integer licenseTypes;

    /**
     * Public key for this product.
     */
    private String publicKey;

    /**
     * Private key for this product.
     */
    private String privateKey;

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
     *
     * @return
     */
    public Integer getLicenseTypes() {
        return licenseTypes;
    }

    /**
     *
     * @param licenseTypes
     */
    public void setLicenseTypes(final Integer licenseTypes) {
        this.licenseTypes = licenseTypes;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(final String publicKey) {
        this.publicKey = publicKey;
    }

    @JSON(include = false)
    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(final String privateKey) {
        this.privateKey = privateKey;
    }
}
