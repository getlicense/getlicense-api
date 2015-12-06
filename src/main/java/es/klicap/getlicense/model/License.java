/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: License.java 269 2014-10-18 10:28:20Z recena $
 */
package es.klicap.getlicense.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class License implements Serializable {

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
    private String uuid;

    /**
     *
     */
    private Long creation;

    /**
     *
     */
    private Long expiration;

    /**
     *
     */
    private Product product;

    /**
     *
     */
    private List<Property> properties;

    /**
     *
     */
    private Customer customer;

    /**
     * Owner.
     */
    private User owner;

    /**
     * URL to download the license.
     */
    private String url;

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
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(final Customer customer) {
        this.customer = customer;
    }

    /**
     * @return the properties
     */
    public List<Property> getProperties() {
        if (properties == null) {
            properties = new ArrayList<Property>();
        }
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(final List<Property> properties) {
        this.properties = properties;
    }

    /**
     * @return the uuid
     */
    public String getUuid() {
        return uuid;
    }

    /**
     * @param uuid the uuid to set
     */
    public void setUuid(final String uuid) {
        this.uuid = uuid;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public void setUrl(final String url) {
        this.url = url;
    }

    /**
     *
     * @param key
     * @return
     */
    public String getPropertyValue(final String key) {
        for (Property kv : this.properties) {
            if (kv.getName().equals(key)) {
                return kv.getValue();
            }
        }
        return null;
    }

    /**
     * @return the expiration
     */
    public Long getExpiration() {
        return expiration;
    }

    /**
     * @param expiration the expiration to set
     */
    public void setExpiration(final Long expiration) {
        this.expiration = expiration;
    }

    /**
     * @return the owner
     */
    public User getOwner() {
        return owner;
    }

    /**
     * @param owner the owner to set
     */
    public void setOwner(final User owner) {
        this.owner = owner;
    }

}
