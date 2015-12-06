/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id$
 */
package es.klicap.getlicense.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

import es.klicap.getlicense.migration.DBMigrator;

/**
 * Read for more information.
 * http://vertx.io/core_manual_java.html#using-a-verticle-to-co-ordinate-loading-of-an-application
 */
public class Launcher extends Verticle {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

    /**
     * TODO: Add description.
     */
    @Override
    public void start() {
        LOGGER.info("Starting Getlicense...");
        JsonObject config = container.config();

        DBMigrator migrator = new DBMigrator(config.getObject("db").getString("url"),
                config.getObject("db").getString("username"), config.getObject("db").getString("password"));
        migrator.run();

        container.deployVerticle("es.klicap.getlicense.server.Server", config.getObject("server"));
        container.deployVerticle("es.klicap.getlicense.store.Store", config.getObject("db"));
        container.deployVerticle("es.klicap.getlicense.notification.Notification", config.getObject("smtp"));
    }
}
