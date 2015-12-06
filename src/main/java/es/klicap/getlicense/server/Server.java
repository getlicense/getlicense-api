/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: Server.java 327 2015-02-27 19:13:53Z amuniz $
 */
package es.klicap.getlicense.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.HttpServer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.RouteMatcher;
import org.vertx.java.platform.Verticle;

import es.klicap.getlicense.json.JSON;
import es.klicap.getlicense.json.Messages;
import es.klicap.getlicense.json.StatusMessage;

/**
 * TODO: Add description.
 */
public class Server extends Verticle {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);

    /**
     * TODO: Add description.
     */
    @Override
    public void start() {

        HttpServer server = vertx.createHttpServer();
        RouteMatcher routeMatcher = new RouteMatcher();

        // Product API
        routeMatcher.all("/product", new ProductAPI(vertx));
        routeMatcher.all("/product/:id", new ProductAPI(vertx));

        // LicenseType API
        routeMatcher.all("/license/type", new LicenseTypeAPI(vertx));
        routeMatcher.all("/license/type/:id", new LicenseTypeAPI(vertx));

        // License file (download and check API)
        routeMatcher.all("/license/file", new LicenseFileAPI(vertx));
        routeMatcher.all("/license/file/:id", new LicenseFileAPI(vertx));

        // License API
        routeMatcher.all("/license", new LicenseAPI(vertx));
        routeMatcher.all("/license/:id", new LicenseAPI(vertx));

        // Customer API
        routeMatcher.all("/customer", new CustomerAPI(vertx));
        routeMatcher.all("/customer/:id", new CustomerAPI(vertx));

        // User API
        routeMatcher.all("/user", new UserAPI(vertx));
        routeMatcher.all("/user/:id", new UserAPI(vertx));

        // Session API
        routeMatcher.all("/session", new SessionAPI(vertx));
        routeMatcher.all("/session/:id", new SessionAPI(vertx));

        // Register API
        routeMatcher.all("/register", new RegisterAPI(vertx));
        routeMatcher.all("/register/:id", new RegisterAPI(vertx));

        // ServerInfo API
        routeMatcher.all("/server", new ServerInfoAPI(vertx, this.container.config()));

        // 404
        routeMatcher.noMatch(new Handler<HttpServerRequest>() {
            public void handle(final HttpServerRequest request) {
                request.response().putHeader("Content-Type", "application/json").setStatusCode(Messages.NOT_FOUND.getHttpCode())
                        .end(JSON.serialize(new StatusMessage(Messages.NOT_FOUND)));
            }
        });

        String host = this.container.config().getString("host");
        Integer port = this.container.config().getInteger("port");
        String version = this.container.config().getString("api_version");
        String url = "http://" + host + ":" + port;

        server.requestHandler(routeMatcher).listen(port, host);
        LOGGER.info("API Server is launched");
        LOGGER.info("API version " + version + ", API URL " + url);
    }
}
