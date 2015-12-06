/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: ServerHandler.java 346 2015-03-07 11:41:07Z recena $
 */
package es.klicap.getlicense.server;

import io.netty.handler.codec.http.Cookie;
import io.netty.handler.codec.http.CookieDecoder;

import java.net.URI;
import java.util.HashMap;
import java.util.Set;

import org.vertx.java.core.Handler;
import org.vertx.java.core.MultiMap;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.http.HttpHeaders;
import org.vertx.java.core.http.HttpServerRequest;

import redis.clients.jedis.Jedis;
import es.klicap.getlicense.json.BusMessageStore;
import es.klicap.getlicense.json.JSON;
import es.klicap.getlicense.json.Messages;
import es.klicap.getlicense.json.StatusMessage;
import es.klicap.getlicense.model.ApiKey;
import es.klicap.getlicense.model.Session;
import flexjson.ObjectFactory;

/**
 *
 */
public abstract class ServerHandler implements Handler<HttpServerRequest> {

    /**
     * HTTP Header > Access-Control-Allow-Origin.
     */
    private static final String ALLOW_ORIGIN_VALUE = "*";

    /**
     * HTTP Header > Access-Control-Allow-Headers.
     */
    private static final String ALLOW_METHODS_VALUE = "GET,PUT,POST,DELETE,OPTIONS";

    /**
     * HTTP Header > Access-Control-Allow-Headers.
     */
    private static final String ALLOW_HEADERS_VALUE = "Content-Type, Authorization,Content-Length,X-Requested-With,GL_API_KEY,GL_SESSION_ID";

    /**
     * HTTP request.
     */
    protected HttpServerRequest request;

    /**
     * Vertx instance.
     */
    protected Vertx vertx;

    /**
     *
     */
    protected HashMap<String, ObjectFactory> factories;

    /**
     * Redis client.
     */
    private Jedis jedis;

    /**
     * Constructor with params.
     *
     * @param vertx
     */
    public ServerHandler(final Vertx vertx) {
        this.vertx = vertx;
        jedis = new Jedis("localhost");
        factories = new HashMap<String, ObjectFactory>();
    }

    @Override
    public void handle(final HttpServerRequest req) {
        request = req;
        MultiMap params = request.params();
        Boolean withId = params.contains("id");

        if (request.method().equals("GET")) {
            if (withId) {
                getId();
            } else {
                get();
            }
        } else if (request.method().equals("POST")) {
            if (withId) {
                postId();
            } else {
                post();
            }
        } else if (request.method().equals("PUT")) {
            if (withId) {
                putId();
            } else {
                put();
            }
        } else if (request.method().equals("DELETE")) {
            if (withId) {
                deleteId();
            } else {
                delete();
            }
        } else if (request.method().equals("OPTIONS")) {
            // To implements CORS when a client use "Preflighted Requests" (p.e ExtJS)
            // Ref. https://developer.mozilla.org/en-US/docs/Web/HTTP/Access_control_CORS#Preflighted_requests
            request.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ServerHandler.ALLOW_ORIGIN_VALUE);
            request.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ServerHandler.ALLOW_METHODS_VALUE);
            request.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, ServerHandler.ALLOW_HEADERS_VALUE);
            request.response().setStatusCode(200).end();
        }
    }

    /**
     * Attempts to retrieve the API Key in the following ways.
     * - param in GET or POST
     * - param in HEADER
     * - using GL_SESSION_ID for get it
     *
     * @return null if it is not an authenticated request.
     */
    protected ApiKey getApiKey() {
        ApiKey apiKey = null;
        String apiKeyParam = null;
        // From request using a GET param
        apiKeyParam = request.params().get("GL_API_KEY");
        if (apiKeyParam == null) {
            // From request using a HEADER param
            apiKeyParam = request.headers().get("GL_API_KEY");
        }
        if (apiKeyParam == null) {
            // From request using GL_SESSION_ID (GET param)
            String sessionId = request.params().get("GL_SESSION_ID");
            if (sessionId != null) {
                String tmp  = jedis.get(sessionId);
                Session session = deserialize(tmp, Session.class);
                apiKeyParam = session.getApikey();
            }
        }
        if (apiKeyParam == null) {
            // From request using GL_SESSION_ID (COOKIE param)
            String tmp = request.headers().get(HttpHeaders.COOKIE);
            if (tmp != null) {
                Set<Cookie> cookies = CookieDecoder.decode(tmp);
                for (final Cookie cookie : cookies) {
                    if (cookie.getName().equals("GL_SESSION_ID")) {
                        String sessionId = cookie.getValue();
                        if (sessionId != null) {
                            String value  = jedis.get(sessionId);
                            Session session = deserialize(value, Session.class);
                            apiKeyParam = session.getApikey();
                        }
                    }
                }
            }
        }
        if (apiKeyParam == null) {
            // From request using GL_SESSION_ID (HEADER param)
            String sessionId = request.headers().get("GL_SESSION_ID");
            if (sessionId != null) {
                String value  = jedis.get(sessionId);
                if (value != null) {
                    Session session = deserialize(value, Session.class);
                    apiKeyParam = session.getApikey();
                }
            }
        }
        try {
            apiKey = new ApiKey();
            apiKey.setApikey(apiKeyParam);
        } catch (IllegalArgumentException iae) {
            end(new StatusMessage(Messages.APIKEY_BADFORMAT));
        } catch (NullPointerException npe) {
            end(new StatusMessage(Messages.FORBIDDEN));
        }
        return apiKey;
    }

    /**
     * Examples.
     *
     * https://api.getlicense.io/user/34 > https://api.getlicense.io
     * http://api.getlicense.io:8081/user/34 > http://api.getlicense.io:8081
     *
     * @return
     */
    protected String getBaseURI() {
        URI uri = request.absoluteURI();
        return uri.getScheme() + "://" + uri.getAuthority();
    }

    /**
     * Returns event bus.
     *
     * @return
     */
    protected EventBus eventBus() {
        return vertx.eventBus();
    }

    /**
     * Returns the serialized java object.
     *
     * @param obj Java object.
     * @return
     */
    protected String serialize(final Object obj) {
        return JSON.serialize(obj);
    }

    /**
     * Deserialize basic objects.
     *
     * @param json
     * @param clazz
     * @return
     */
    protected <T> T deserialize(final String json, final Class<T> clazz) {
        T deserialized = null;
        try {
            deserialized = JSON.deserialize(json, clazz);
        } catch (RuntimeException rte) {
            end(new StatusMessage(Messages.INVALID_JSON));
        }
        return deserialized;
    }

    /**
     * Deserialize complex objects that require specific factories.
     *
     * @param json
     * @param clazz
     * @param factories
     * @return
     */
    protected <T> T deserialize(final String json, final Class<T> clazz, final HashMap<String, ObjectFactory> factories) {
        T deserialized = null;
        try {
            deserialized = JSON.deserialize(json, clazz, factories);
        } catch (RuntimeException rte) {
            StatusMessage message = new StatusMessage(Messages.INTERNAL_ERROR);
            message.addExtra("exception", rte.toString());
            end(message);
        }
        return deserialized;
    }

    protected void end(final String response) {
        request.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ServerHandler.ALLOW_ORIGIN_VALUE);
        request.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ServerHandler.ALLOW_METHODS_VALUE);
        request.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, ServerHandler.ALLOW_HEADERS_VALUE);
        request.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json").end(response);
    }

    protected void end(final String response, final int httpCode) {
        request.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ServerHandler.ALLOW_ORIGIN_VALUE);
        request.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ServerHandler.ALLOW_METHODS_VALUE);
        request.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, ServerHandler.ALLOW_HEADERS_VALUE);
        request.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json").setStatusCode(httpCode).end(response);
    }

    protected void end(final String response, final int httpCode, final String contentType) {
        request.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ServerHandler.ALLOW_ORIGIN_VALUE);
        request.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ServerHandler.ALLOW_METHODS_VALUE);
        request.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, ServerHandler.ALLOW_HEADERS_VALUE);
        request.response().putHeader(HttpHeaders.CONTENT_TYPE, contentType).setStatusCode(httpCode).end(response);
    }

    protected void end(final StatusMessage message) {
        request.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ServerHandler.ALLOW_ORIGIN_VALUE);
        request.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ServerHandler.ALLOW_METHODS_VALUE);
        request.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, ServerHandler.ALLOW_HEADERS_VALUE);
        request.response().putHeader(HttpHeaders.CONTENT_TYPE, "application/json").setStatusCode(message.getHttpCode()).end(serialize(message));
    }

    protected void endFile(final String response, final String contentType, final String filename) {
        request.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, ServerHandler.ALLOW_ORIGIN_VALUE);
        request.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, ServerHandler.ALLOW_METHODS_VALUE);
        request.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, ServerHandler.ALLOW_HEADERS_VALUE);
        request.response().putHeader("Content-Disposition", "attachment; filename=" + filename);
        request.response().putHeader(HttpHeaders.CONTENT_TYPE, contentType);
        request.response().setStatusCode(Messages.OK.getHttpCode()).end(response);
    }

    protected <T> void sendBusMessageFromRequestBody(final BusMessageStore message, final Class<T> clazz, final String address,
            final Handler<Message<String>> callback) {
        ApiKey apiKey = getApiKey();
        if (apiKey != null) {
            message.setBaseURI(getBaseURI());
            message.setApiKey(apiKey);
            request.bodyHandler(new ActionBodyHandler<T>(message, clazz, address, callback));
        }
    }

    protected <T> void sendAnonymousBusMessageFromRequestBody(final BusMessageStore message, final Class<T> clazz, final String address,
            final Handler<Message<String>> callback) {
        message.setBaseURI(getBaseURI());
        request.bodyHandler(new ActionBodyHandler<T>(message, clazz, address, callback));
    }

    protected void sendBusMessage(final BusMessageStore message, final String address, final Handler<Message<String>> callback) {
        ApiKey apiKey = getApiKey();
        if (apiKey.getApikey() != null) {
            message.setBaseURI(getBaseURI());
            message.setApiKey(apiKey);
            eventBus().send(address, serialize(message), callback);
        } else {
            end(new StatusMessage(Messages.FORBIDDEN));
        }
    }

    protected void sendAnonymousBusMessage(final BusMessageStore message, final String address, final Handler<Message<String>> callback) {
        message.setBaseURI(getBaseURI());
        eventBus().send(address, serialize(message), callback);
    }

    /**
     * Internal private class modeling a request body handler.
     * After mapping the request body, a message is sent to the bus,
     * and the callback (response from bus) is handled in the given callback.
     *
     * @param <T> Target type on which to map the body request.
     */
    private class ActionBodyHandler<T> implements Handler<Buffer> {

        private Class<T> clazz;
        private String address;
        private Handler<Message<String>> callback;
        private BusMessageStore m;

        public ActionBodyHandler(final BusMessageStore m, final Class<T> clazz, final String address, final Handler<Message<String>> callback) {
            this.clazz = clazz;
            this.address = address;
            this.callback = callback;
            this.m = m;
        }

        @Override
        public void handle(final Buffer body) {
            if (clazz != null) {
                T p = deserialize(body.toString(), clazz);
                m.setEntity(p);
            } else {
                m.setParameter(body.toString());
            }
            eventBus().send(address, serialize(m), callback);
        }
    }

    protected abstract void get();

    protected abstract void getId();

    protected abstract void put();

    protected abstract void putId();

    protected abstract void post();

    protected abstract void postId();

    protected abstract void delete();

    protected abstract void deleteId();

}