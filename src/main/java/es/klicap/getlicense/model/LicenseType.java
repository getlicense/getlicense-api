/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: LicenseType.java 262 2014-10-11 10:46:41Z recena $
 */
package es.klicap.getlicense.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import flexjson.JSON;

/**
 *
 */
public class LicenseType implements Serializable {

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
    private String name;

    /**
     * Default expiration time. In days.
     */
    private Integer expiration;

    /**
     *
     */
    private List<String> attributes = new ArrayList<String>();

    /**
     *
     */
    private Product product;

    /**
     * Owner.
     */
    private User owner;

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
     * @return the attributes
     */
    public List<String> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(final List<String> attributes) {
        this.attributes = attributes;
    }

    /**
     * @return the product
     */
    public Product getProduct() {
        return product;
    }

    /**
     * @param product the product to set
     */
    public void setProduct(final Product product) {
        this.product = product;
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
     * @return the expiration
     */
    public Integer getExpiration() {
        return expiration;
    }

    /**
     * @param expiration the expiration to set
     */
    public void setExpiration(final Integer expiration) {
        this.expiration = expiration;
    }
}
