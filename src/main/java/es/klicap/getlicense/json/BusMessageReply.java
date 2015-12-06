/*
 * getlicense.io
 * Copyright (C) 2014 klicap - ingeniería del puzle
 *
 * $Id: BusMessageReply.java 259 2014-09-14 17:44:25Z recena $
 */
package es.klicap.getlicense.json;

/**
 * Transient entity to send messages through the event bus.
 *
 */
public class BusMessageReply extends BusMessage {

    /**
     * Information about a successful reply message.
     */
    private StatusMessage message;

    /**
     * Default constructor.
     */
    public BusMessageReply() {
    }

    /**
     * @return the message
     */
    public StatusMessage getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(final StatusMessage message) {
        this.message = message;
    }
}