/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: BusMessageStore.java 187 2014-07-17 06:28:54Z recena $
 */
package es.klicap.getlicense.json;

import java.util.List;

import es.klicap.getlicense.model.ApiKey;

/**
 * Transient entity to send messages through the event bus.
 */
public class BusMessageStore extends BusMessage {

    /**
     * Action. Default actions (they can be extendend)
     */
    private String action;

    /**
     * API Key object.
     */
    private ApiKey apiKey;

    /**
     * Parameter to send to the workers.
     * Used in READ or DELETE actions.
     */
    private String parameter;

    /**
     * Command to execute for the given action.
     * It's open to any use, but usually we use this to send the MyBatis query to execute.
     */
    private String command;

    /**
     * Base URI of the application.
     * It's only required by workers that have to generate absolute URLs.
     */
    private String baseURI;

    /**
     * Default constructor.
     */
    public BusMessageStore() {
    }

    public BusMessageStore(final String action, final String command, final String parameter) {
        this.action = action;
        this.command = command;
        this.parameter = parameter;

    }

    public BusMessageStore(final String action, final String command, final Object entity) {
        this.action = action;
        this.command = command;
        this.setEntity(entity);
    }

    public BusMessageStore(final String action, final String command, final List<Object> entities) {
        this.action = action;
        this.command = command;
        this.setEntity(entities);
    }

    public BusMessageStore(final String action, final String command) {
        this.action = action;
        this.command = command;
    }

    public BusMessageStore(final String action) {
        this.action = action;
    }

    public ApiKey getApiKey() {
        return apiKey;
    }

    public void setApiKey(final ApiKey apiKey) {
        this.apiKey = apiKey;
    }

    public String getAction() {
        return action;
    }

    public void setAction(final String action) {
        this.action = action;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(final String parameter) {
        this.parameter = parameter;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(final String command) {
        this.command = command;
    }

    public String getBaseURI() {
        return baseURI;
    }

    public void setBaseURI(final String basePath) {
        this.baseURI = basePath;
    }

}
