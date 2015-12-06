/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: LicenseTypeAPI.java 334 2015-02-28 17:29:04Z amuniz $
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
import es.klicap.getlicense.json.factory.LicenseTypeFactory;
import es.klicap.getlicense.json.factory.StatusMessageFactory;
import es.klicap.getlicense.model.LicenseType;

/**
 * License type API.
 */
public class LicenseTypeAPI extends ServerHandler {

    /**
     * Literal to identify event bus address.
     */
    private static final String EVENTBUS_ADDRESS = "license.type.store";

    /**
     * Constructor with params.
     *
     * @param vertx
     */
    public LicenseTypeAPI(final Vertx vertx) {
        super(vertx);
        factories.put("entities.values", new LicenseTypeFactory());
        factories.put("message", new StatusMessageFactory());
    }

    @Override
    protected void get() {
        BusMessageStore m = new BusMessageStore(BusAction.READ.toString(), "LicenseTypeMapper.all");
        sendBusMessage(m, LicenseTypeAPI.EVENTBUS_ADDRESS, new Handler<Message<String>>() {
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
        BusMessageStore m = new BusMessageStore(BusAction.READ.toString(), "LicenseTypeMapper.byId", request.params().get("id"));
        sendBusMessage(m, LicenseTypeAPI.EVENTBUS_ADDRESS, new Handler<Message<String>>() {
            @Override
            public void handle(final Message<String> message) {
                BusMessageReply reply = deserialize(message.body(), BusMessageReply.class, factories);
                StatusMessage status = reply.getMessage();
                if (status != null) {
                    end(status);
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
        BusMessageStore m = new BusMessageStore(BusAction.UPDATE.toString(), "LicenseTypeMapper.update", request.params().get("id"));
        sendBusMessageFromRequestBody(m, LicenseType.class, LicenseTypeAPI.EVENTBUS_ADDRESS, new Handler<Message<String>>() {
            @Override
            public void handle(final Message<String> message) {
                BusMessageReply reply = deserialize(message.body(), BusMessageReply.class, factories);
                end(reply.getMessage());
            }
        });
    }

    @Override
    protected void post() {
        BusMessageStore m = new BusMessageStore(BusAction.CREATE.toString(), "LicenseTypeMapper.insert");
        sendBusMessageFromRequestBody(m, LicenseType.class, LicenseTypeAPI.EVENTBUS_ADDRESS, new Handler<Message<String>>() {
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
        BusMessageStore m = new BusMessageStore(BusAction.DELETE.toString(), "LicenseTypeMapper.delete", request.params().get("id"));
        sendBusMessage(m, LicenseTypeAPI.EVENTBUS_ADDRESS, new Handler<Message<String>>() {
            @Override
            public void handle(final Message<String> message) {
                BusMessageReply reply = deserialize(message.body(), BusMessageReply.class, factories);
                end(reply.getMessage());
            }
        });
    }

}
