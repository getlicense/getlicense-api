/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: RegisterStore.java 385 2015-04-12 20:54:12Z recena $
 */
package es.klicap.getlicense.store;

import java.util.HashMap;
import java.util.UUID;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSessionFactory;
import org.vertx.java.core.eventbus.Message;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import es.klicap.getlicense.json.BusAction;
import es.klicap.getlicense.json.BusMessageReply;
import es.klicap.getlicense.json.BusMessageStore;
import es.klicap.getlicense.json.JSON;
import es.klicap.getlicense.json.Messages;
import es.klicap.getlicense.json.StatusMessage;
import es.klicap.getlicense.json.factory.RegisterFactory;
import es.klicap.getlicense.model.Register;
import flexjson.ObjectFactory;

/**
 * Register Store.
 */
public class RegisterStore extends StoreHandler {

    /**
     * Literal to identify event bus address.
     */
    public static final String EVENTBUS_ADDRESS = "register.store";

    /**
     * Redis client.
     */
    private Jedis jedis;

    /**
     * Constructor with params.
     *
     * @param mybatis
     * @param jedis
     */
    public RegisterStore(final SqlSessionFactory mybatis, final Jedis jedis) {
        super(mybatis);
        this.jedis = jedis;
    }

    @Override
    protected ObjectFactory getObjectFactory() {
        return new RegisterFactory();
    }

    @Override
    public void handle(final Message<String> rawMessage) {
        try {
            ObjectFactory of = getObjectFactory();
            HashMap<String, ObjectFactory> factories = new HashMap<String, ObjectFactory>();
            if (of != null) {
                factories.put("entities.values", getObjectFactory());
            }
            BusMessageStore message = JSON.deserialize(rawMessage.body(), BusMessageStore.class, factories);

            BusMessageReply reply = null;
            if (BusAction.READ.toString().equals(message.getAction())) {
                reply = handleRead(message);
            } else if (BusAction.CREATE.toString().equals(message.getAction())) {
                reply = handleCreate(message);
            } else if (BusAction.UPDATE.toString().equals(message.getAction())) {
                reply = handleUpdate(message);
            } else if (BusAction.DELETE.toString().equals(message.getAction())) {
                reply = handleDelete(message);
            } else {
                reply = handleAdditional(message);
            }
            myBatis.commit();
            rawMessage.reply(JSON.serialize(reply));
        } catch (PersistenceException pe) {
            BusMessageReply reply = new BusMessageReply();
            StatusMessage message = new StatusMessage(Messages.INTERNAL_ERROR);
            message.addExtra("database", pe.getMessage());
            reply.setMessage(message);
            rawMessage.reply(JSON.serialize(reply));
        } catch (JedisConnectionException jce) {
            BusMessageReply reply = new BusMessageReply();
            StatusMessage message = new StatusMessage(Messages.INTERNAL_ERROR);
            message.addExtra("database", jce.getMessage());
            reply.setMessage(message);
            rawMessage.reply(JSON.serialize(reply));
        }
    }

    @Override
    protected BusMessageReply handleRead(final BusMessageStore message) {
        BusMessageReply reply = new BusMessageReply();
        String obj = jedis.get(message.getParameter());
        if (obj != null) {
            Register register = JSON.deserialize(obj, Register.class);
            StatusMessage status = new StatusMessage(Messages.REGISTER_LICENSE);
            reply.setEntity(register);
            reply.setMessage(status);
            jedis.del(message.getParameter());
        } else {
            StatusMessage status = new StatusMessage(Messages.NOT_FOUND);
            reply.setMessage(status);
        }
        return reply;
    }

    @Override
    protected BusMessageReply handleCreate(final BusMessageStore message) {
        BusMessageReply reply = new BusMessageReply();
        Register register = (Register) message.getEntity();
        register.setId(UUID.randomUUID().toString());
        jedis.set(register.getId(), JSON.serialize(register));
        StatusMessage created = new StatusMessage(Messages.CREATED);
        reply.setEntity(register);
        reply.setMessage(created);
        afterCreate(message);
        return reply;
    }

    @Override
    protected void afterCreate(final BusMessageStore message) {
        //Register register = (Register) message.getEntity();
        // TODO: Send an email.
    }
}
