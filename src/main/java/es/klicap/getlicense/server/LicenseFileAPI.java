/*
 * getlicense.io
 * Copyright (C) 2013-2015 klicap - ingeniería del puzle
 *
 * $Id: LicenseFileAPI.java 352 2015-03-15 12:09:57Z recena $
 */
package es.klicap.getlicense.server;

import org.vertx.java.core.Handler;
import org.vertx.java.core.Vertx;
import org.vertx.java.core.eventbus.Message;

import es.klicap.getlicense.json.BusMessageReply;
import es.klicap.getlicense.json.BusMessageStore;
import es.klicap.getlicense.json.Messages;
import es.klicap.getlicense.json.StatusMessage;
import es.klicap.getlicense.json.factory.StatusMessageFactory;
import flexjson.factories.StringObjectFactory;

/**
 * LicenseFile API.
 */
public class LicenseFileAPI extends ServerHandler {

    /**
     * Literal to identify event bus address.
     */
    public static final String EVENTBUS_ADDRESS = "license.file.store";

    /**
     * File mime-type.
     */
    private static final String FILE_MIMETYPE = "application/octet-stream";

    /**
     * Constructor with params.
     *
     * @param vertx
     */
    public LicenseFileAPI(final Vertx vertx) {
        super(vertx);
        factories.put("entities.values", new StringObjectFactory());
        factories.put("message", new StatusMessageFactory());
    }

    @Override
    protected void get() {
        end(new StatusMessage(Messages.ID_REQUIRED));
    }

    @Override
    protected void getId() {
        BusMessageStore m = new BusMessageStore("generate_file", "LicenseMapper.byUUID", request.params().get("id"));
        m.setEntity(request.params().get("format"));
        sendBusMessage(m, LicenseFileAPI.EVENTBUS_ADDRESS, new Handler<Message<String>>() {
            @Override
            public void handle(final Message<String> message) {
                BusMessageReply reply = deserialize(message.body(), BusMessageReply.class, factories);
                StatusMessage status = reply.getMessage();
                if (status != null) {
                    end(status);
                } else {
                    endFile((String) reply.getEntity(), FILE_MIMETYPE, "license-" + request.params().get("id") + ".lic");
                }
            }
        });
    }

    @Override
    protected void post() {
        BusMessageStore m = new BusMessageStore("validate_file", null);
        sendBusMessageFromRequestBody(m, null, LicenseFileAPI.EVENTBUS_ADDRESS, new Handler<Message<String>>() {
            @Override
            public void handle(final Message<String> message) {
                end(message.body());
            }
        });
    }

    @Override
    protected void postId() {
        end(new StatusMessage(Messages.ID_NOT_ALLOWED_ON_POST));
    }

    @Override
    protected void put() {
        end(new StatusMessage(Messages.METHOD_NOT_ALLOWED));
    }

    @Override
    protected void putId() {
        end(new StatusMessage(Messages.METHOD_NOT_ALLOWED));
    }

    @Override
    protected void delete() {
        end(new StatusMessage(Messages.METHOD_NOT_ALLOWED));
    }

    @Override
    protected void deleteId() {
        end(new StatusMessage(Messages.METHOD_NOT_ALLOWED));
    }

}
