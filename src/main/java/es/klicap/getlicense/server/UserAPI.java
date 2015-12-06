/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: UserAPI.java 334 2015-02-28 17:29:04Z amuniz $
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
import es.klicap.getlicense.json.factory.StatusMessageFactory;
import es.klicap.getlicense.json.factory.UserFactory;
import es.klicap.getlicense.model.User;

/**
 * User API.
 */
public class UserAPI extends ServerHandler {

    /**
     * Literal to identify event bus address.
     */
    public static final String EVENTBUS_ADDRESS = "user.store";

    /**
     * Constructor with params.
     *
     * @param vertx
     */
    public UserAPI(final Vertx vertx) {
        super(vertx);
        factories.put("entities.values", new UserFactory());
        factories.put("message", new StatusMessageFactory());
    }

    @Override
    protected void get() {
        BusMessageStore m = new BusMessageStore(BusAction.READ.toString(), "UserMapper.all");
        sendBusMessage(m, EVENTBUS_ADDRESS, new Handler<Message<String>>() {
            @Override
            public void handle(final Message<String> message) {
                BusMessageReply reply = deserialize(message.body(), BusMessageReply.class, factories);
                StatusMessage status = reply.getMessage();
                if (status != null) {
                    end(status);
                } else {
                    end(serialize(reply.getEntities()), Messages.OK.getHttpCode());
                }
            }
        });
    }

    @Override
    protected void getId() {
        BusMessageStore m = new BusMessageStore(BusAction.READ.toString(), "UserMapper.byId", request.params().get("id"));
        sendBusMessage(m, EVENTBUS_ADDRESS, new Handler<Message<String>>() {
            @Override
            public void handle(final Message<String> message) {
                BusMessageReply reply = deserialize(message.body(), BusMessageReply.class, factories);
                StatusMessage status = reply.getMessage();
                if (status != null) {
                    end(reply.getMessage());
                } else {
                    end(serialize(reply.getEntity()), Messages.OK.getHttpCode());
                }
            }
        });
    }

    @Override
    protected void put() {
        end(new StatusMessage(Messages.ID_REQUIRED));
    }

    @Override
    protected void putId() {
        BusMessageStore m = new BusMessageStore(BusAction.UPDATE.toString(), "UserMapper.update", request.params().get("id"));
        sendBusMessageFromRequestBody(m, User.class, EVENTBUS_ADDRESS, new Handler<Message<String>>() {
            @Override
            public void handle(final Message<String> message) {
                BusMessageReply reply = deserialize(message.body(), BusMessageReply.class, factories);
                end(reply.getMessage());
            }
        });
    }

    @Override
    protected void post() {
        BusMessageStore m = new BusMessageStore(BusAction.CREATE.toString(), "UserMapper.insert");
        sendBusMessageFromRequestBody(m, User.class, EVENTBUS_ADDRESS, new Handler<Message<String>>() {
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
    protected void delete() {
        end(new StatusMessage(Messages.ID_REQUIRED));
    }

    @Override
    protected void deleteId() {
        BusMessageStore m = new BusMessageStore(BusAction.DELETE.toString(), "UserMapper.delete", request.params().get("id"));
        sendBusMessage(m, EVENTBUS_ADDRESS, new Handler<Message<String>>() {
            @Override
            public void handle(final Message<String> message) {
                BusMessageReply reply = deserialize(message.body(), BusMessageReply.class, factories);
                end(reply.getMessage());
            }
        });
    }
}
