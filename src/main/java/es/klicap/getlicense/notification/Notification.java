/*
 * getlicense.io
 * Copyright (C) 2014 klicap - ingeniería del puzle
 *
 * $Id: Notification.java 346 2015-03-07 11:41:07Z recena $
 */
package es.klicap.getlicense.notification;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;

import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.platform.Verticle;

/**
 * Verticle to send notification.
 */
public class Notification extends Verticle {

    /**
     *
     */
    private Session session;

    /**
     *
     */
    private Transport transport;

    /**
     *
     */
    private String username;

    /**
     *
     */
    private String password;

    /**
     *
     */
    private String host;

    /**
     *
     */
    private Integer port;

    /**
     *
     */
    @Override
    public void start() {
        EventBus eb = vertx.eventBus();
        host = this.container.config().getString("host");
        port = this.container.config().getInteger("port");
        username = this.container.config().getString("username");
        password = this.container.config().getString("password");

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.socketFactory.port", port);
        props.put("mail.smtp.socketFactory.fallback", Boolean.toString(false));
        props.put("mail.smtp.auth", Boolean.toString(true));

        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            transport = session.getTransport();
            transport.connect();
        } catch (MessagingException e) {
            this.container.logger().error("Failed to setup mail transport", e);
        }

        eb.registerHandler(MailerNotificationHandler.EVENTBUS_ADDRESS, new MailerNotificationHandler(transport));
    }
}