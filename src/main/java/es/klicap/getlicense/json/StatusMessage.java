/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: StatusMessage.java 307 2014-11-16 13:13:24Z recena $
 */
package es.klicap.getlicense.json;

import java.util.HashMap;

/**
 * To generate messages type of successful.
 */
public class StatusMessage {

    /**
     * Internal code.
     */
    private String code;

    /**
     * Kee. Uniquely identifies a successful message.
     */
    private String kee;

    /**
     * Description.
     */
    private String description;

    /**
     * HTTP code. If this message reaches Web API layer, this value will be his HTTP CODE.
     */
    private Integer httpCode;

    /**
     * Extra information in key - value format.
     */
    private HashMap<String, Object> extra;

    /**
     * Default constructor.
     */
    public StatusMessage() {
    }

    /**
     * Create a message.
     *
     * @param code
     * @param kee
     * @param description
     * @param httpCode
     */
    public StatusMessage(final String code, final String kee, final String description, final Integer httpCode) {
        this.code = code;
        this.kee = kee;
        this.description = description;
        this.httpCode = httpCode;
        this.extra = new HashMap<String, Object>();
    }

    /**
     * Create a message from a preconfigured message.
     *
     * @param message
     */
    public StatusMessage(final Messages message) {
        this.code = message.getCode();
        this.kee = message.getKee();
        this.description = message.getDescription();
        this.httpCode = message.getHttpCode();
        this.extra = new HashMap<String, Object>();
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * @return the kee
     */
    public String getKee() {
        return kee;
    }

    /**
     * @param kee the kee to set
     */
    public void setKee(final String kee) {
        this.kee = kee;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @return the httpCode
     */
    public Integer getHttpCode() {
        return httpCode;
    }

    /**
     * @param httpCode the httpCode to set
     */
    public void setHttpCode(final Integer httpCode) {
        this.httpCode = httpCode;
    }

    /**
     *
     * @param key
     * @param value
     */
    public void addExtra(final String key, final Object value) {
        extra.put(key, value);
    }

    /**
     * @param extra
     */
    public void setExtra(final HashMap<String, Object> extra) {
        this.extra = extra;
    }

    /**
     * @return the extra
     */
    public HashMap<String, Object> getExtra() {
        return extra;
    }

}
