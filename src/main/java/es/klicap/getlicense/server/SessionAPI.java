/*
 * getlicense.io
 * Copyright (C) 2014 klicap - ingeniería del puzle
 *
 * $Id: SessionAPI.java 334 2015-02-28 17:29:04Z amuniz $
 */
package es.klicap.getlicense.server;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;

import es.klicap.getlicense.json.BusAction;
import es.klicap.getlicense.json.BusMessageReply;
import es.klicap.getlicense.json.BusMessageStore;
import es.klicap.getlicense.json.Messages;
import es.klicap.getlicense.json.StatusMessage;
import es.klicap.getlicense.json.factory.SessionFactory;
import es.klicap.getlicense.json.factory.StatusMessageFactory;
import es.klicap.getlicense.model.Session;

/**
 * Session API.
 */
public class SessionAPI extends ServerHandler {

    /**
     * Literal to identify event bus address.
     */
    private static final String EVENTBUS_ADDRESS = "session.store";

    /**
     * Constructor with params.
     *
     * @param vertx
     */
    public SessionAPI(final Vertx vertx) {
        super(vertx);
        factories.put("entities.values", new SessionFactory());
        factories.put("message", new StatusMessageFactory());
    }

    @Override
    protected void get() {
        end(new StatusMessage(Messages.METHOD_NOT_ALLOWED));
    }

    @Override
    protected void getId() {
        end(new StatusMessage(Messages.METHOD_NOT_ALLOWED));
    }

    @Override
    protected void post() {
        BusMessageStore m = new BusMessageStore(BusAction.CREATE.toString());
        sendAnonymousBusMessageFromRequestBody(m, Session.class, SessionAPI.EVENTBUS_ADDRESS, new Handler<Message<String>>() {
            @Override
            public void handle(final Message<String> message) {
                BusMessageReply reply = deserialize(message.body(), BusMessageReply.class, factories);
                end(reply.getMessage());
            }
        });
    }

    @Override
    protected void postId() {
        end(new StatusMessage(Messages.ID_NOT_ALLOWED_ON_POST));
    }

    @Override
    protected void put() {
        end(new StatusMessage(Messages.ID_REQUIRED));
    }

    @Override
    protected void putId() {
        BusMessageStore m = new BusMessageStore(BusAction.UPDATE.toString(), null, request.params().get("id"));
        sendAnonymousBusMessage(m, SessionAPI.EVENTBUS_ADDRESS, new Handler<Message<String>>() {
            @Override
            public void handle(final Message<String> message) {
                StatusMessage update = new StatusMessage(Messages.OK);
                end(update);
            }
        });

    }

    @Override
    protected void delete() {
        end(new StatusMessage(Messages.ID_REQUIRED));
    }

    @Override
    protected void deleteId() {
        BusMessageStore m = new BusMessageStore(BusAction.DELETE.toString(), null, request.params().get("id"));
        sendAnonymousBusMessage(m, SessionAPI.EVENTBUS_ADDRESS, new Handler<Message<String>>() {
            @Override
            public void handle(final Message<String> message) {
                StatusMessage removed = new StatusMessage(Messages.REMOVED);
                end(removed);
            }
        });
    }
}
