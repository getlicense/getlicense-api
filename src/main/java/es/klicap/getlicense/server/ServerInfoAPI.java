/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: ServerInfoAPI.java 259 2014-09-14 17:44:25Z recena $
 */
package es.klicap.getlicense.server;

import org.vertx.java.core.Vertx;
import org.vertx.java.core.json.JsonObject;

import es.klicap.getlicense.json.Messages;
import es.klicap.getlicense.json.StatusMessage;
import es.klicap.getlicense.model.ServerInfo;

/**
 * ServerInfo API.
 */
public class ServerInfoAPI extends ServerHandler {

    /**
     * Specific verticle configuration.
     */
    private JsonObject config;

    /**
     * Constructor with params.
     *
     * @param vertx
     */
    public ServerInfoAPI(final Vertx vertx) {
        super(vertx);
    }

    /**
     * Constructor with params.
     *
     * @param vertx
     * @param config
     */
    public ServerInfoAPI(final Vertx vertx, final JsonObject config) {
        super(vertx);
        this.config = config;
    }

    @Override
    protected void get() {
        ServerInfo server = new ServerInfo();
        server.setVersion(config.getString("api_version"));
        server.setRevision(config.getString("api_revision"));
        end(serialize(server), Messages.OK.getHttpCode());
    }

    @Override
    protected void getId() {
        end(new StatusMessage(Messages.METHOD_NOT_ALLOWED));
    }

    @Override
    protected void post() {
        end(new StatusMessage(Messages.METHOD_NOT_ALLOWED));
    }

    @Override
    protected void putId() {
        end(new StatusMessage(Messages.METHOD_NOT_ALLOWED));
    }

    @Override
    protected void deleteId() {
        end(new StatusMessage(Messages.METHOD_NOT_ALLOWED));
    }

    @Override
    protected void postId() {
        end(new StatusMessage(Messages.METHOD_NOT_ALLOWED));
    }

    @Override
    protected void delete() {
        end(new StatusMessage(Messages.METHOD_NOT_ALLOWED));
    }

    @Override
    protected void put() {
        end(new StatusMessage(Messages.METHOD_NOT_ALLOWED));
    }
}