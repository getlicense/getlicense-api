/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: MailerNotificationHandler.java 310 2014-11-16 19:27:49Z recena $
 */
package es.klicap.getlicense.notification;

import javax.mail.Transport;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;

/**
 * TODO: Add description.
 */
public class MailerNotificationHandler implements Handler<Message<String>> {

    /**
     * Literal to identify event bus address.
     */
    public static final String EVENTBUS_ADDRESS = "mailer.notification";

    /**
     * Database client.
     */
    private Transport transport;

    /**
     * Constructor with params.
     *
     * @param mybatis
     */
    public MailerNotificationHandler(final Transport transport) {
        this.transport = transport;
    }

    @Override
    public void handle(final Message<String> rawMessage) {
        rawMessage.reply();
    }
}
