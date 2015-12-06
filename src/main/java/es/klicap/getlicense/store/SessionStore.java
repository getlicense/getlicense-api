/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: SessionStore.java 385 2015-04-12 20:54:12Z recena $
 */
package es.klicap.getlicense.store;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.eventbus.Message;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;
import es.klicap.getlicense.json.BusAction;
import es.klicap.getlicense.json.BusMessageReply;
import es.klicap.getlicense.json.BusMessageStore;
import es.klicap.getlicense.json.JSON;
import es.klicap.getlicense.json.Messages;
import es.klicap.getlicense.json.StatusMessage;
import es.klicap.getlicense.json.factory.SessionFactory;
import es.klicap.getlicense.model.Session;
import es.klicap.getlicense.model.User;
import flexjson.ObjectFactory;

/**
 * Session Store.
 */
public class SessionStore extends StoreHandler {

    /**
     * Literal to identify event bus address.
     */
    public static final String EVENTBUS_ADDRESS = "session.store";

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionStore.class);

    /**
     * Redis client.
     */
    private Jedis jedis;

    /**
     * Constructor with params.
     *
     * @param myBatis
     * @param jedis
     */
    public SessionStore(final SqlSessionFactory sqlSessionFactory, final Jedis jedis) {
        super(sqlSessionFactory);
        this.jedis = jedis;
    }

    @Override
    protected ObjectFactory getObjectFactory() {
        return new SessionFactory();
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
            myBatis = sqlSessionFactory.openSession();
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
            myBatis.close();
            rawMessage.reply(JSON.serialize(reply));
        } catch (PersistenceException | JedisConnectionException ex) {
            LOGGER.error(ex.getMessage(), ex);
            BusMessageReply reply = new BusMessageReply();
            StatusMessage message = new StatusMessage(Messages.INTERNAL_ERROR);
            message.addExtra("database", ex.getMessage());
            reply.setMessage(message);
            rawMessage.reply(JSON.serialize(reply));
        }
    }

    @Override
    protected BusMessageReply handleCreate(final BusMessageStore message) {
        BusMessageReply reply = new BusMessageReply();
        beforeCreate(message);
        Session session = (Session) message.getEntity();
        if (session.getApikey() != null) {
            session.setCreation(System.currentTimeMillis() / 1000L);
            session.setId(UUID.randomUUID().toString());
            jedis.set(session.getId(), JSON.serialize(session));
            StatusMessage created = new StatusMessage(Messages.CREATED);
            created.addExtra("GL_SESSION_ID", session.getId());
            reply.setEntity(session);
            reply.setMessage(created);
        } else {
            reply.setMessage(new StatusMessage(Messages.BAD_CREDENTIALS));
        }
        afterCreate(message);
        return reply;
    }

    @Override
    protected void beforeCreate(final BusMessageStore message) {
        Session session = (Session) message.getEntity();
        Map<String, Object> params = new HashMap<>();
        params.put("username", session.getUsername());
        params.put("password", session.getPassword());
        User user = myBatis.selectOne("UserMapper.byCredentials", params);
        if (user != null) {
            session.setApikey(user.getApikey());
        }
        message.setEntity(session);
    }

    @Override
    protected BusMessageReply handleUpdate(final BusMessageStore message) {
        BusMessageReply reply = new BusMessageReply();
        beforeUpdate(message);
        Session session = JSON.deserialize(jedis.get(message.getParameter()), Session.class);
        if (session != null) {
            // Refresh the session lifetime.
            reply.setMessage(new StatusMessage(Messages.OK));
        } else {
            reply.setMessage(new StatusMessage(Messages.NOT_FOUND));
        }
        afterCreate(message);
        return reply;
    }

    @Override
    protected BusMessageReply handleDelete(final BusMessageStore message) {
        BusMessageReply reply = null;
        beforeDelete(message);
        jedis.del(message.getParameter());
        afterDelete(message);
        return reply;
    }
}
