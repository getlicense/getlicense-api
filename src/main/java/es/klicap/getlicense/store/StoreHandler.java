/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: StoreHandler.java 385 2015-04-12 20:54:12Z recena $
 */
package es.klicap.getlicense.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;

import es.klicap.getlicense.json.BusAction;
import es.klicap.getlicense.json.BusMessageReply;
import es.klicap.getlicense.json.BusMessageStore;
import es.klicap.getlicense.json.JSON;
import es.klicap.getlicense.json.Messages;
import es.klicap.getlicense.json.StatusMessage;
import es.klicap.getlicense.model.ApiKey;
import flexjson.ObjectFactory;

/**
 * TODO: Add description.
 */
public abstract class StoreHandler implements Handler<Message<String>> {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreHandler.class);

    /**
     * Database client.
     */
    protected SqlSession myBatis;

    /**
     * Database session factory.
     */
    protected SqlSessionFactory sqlSessionFactory;

    /**
     * Constructor with params.
     *
     * @param myBatis
     */
    public StoreHandler(final SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
        this.myBatis = null;
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
            if (isAuthenticated(message)) {
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
            }
            myBatis.commit();
            myBatis.close();
            rawMessage.reply(JSON.serialize(reply));
        } catch (PersistenceException pe) {
            LOGGER.error(pe.getMessage(), pe);
            BusMessageReply reply = new BusMessageReply();
            StatusMessage message = new StatusMessage(Messages.INTERNAL_ERROR);
            message.addExtra("database", pe.getMessage());
            reply.setMessage(message);
            rawMessage.reply(JSON.serialize(reply));
        }
    }

    /**
     *
     * @param message
     * @return
     */
    protected BusMessageReply handleRead(final BusMessageStore message) {
        BusMessageReply reply = new BusMessageReply();
        List<Object> entities = null;
        beforeRead(message);
        Map<String, Object> params = new HashMap<>();
        if (message.getEntities() != null) {
            params.put("entity", message.getEntity());
            params.put("owner", message.getApiKey().getUser());
            entities = myBatis.selectList(message.getCommand(), params);
            if (entities.size() == 0) {
                StatusMessage status = new StatusMessage(Messages.NOT_FOUND);
                reply.setMessage(status);
            }
        } else if (message.getParameter() != null) {
            params.put("id", message.getParameter());
            params.put("owner", message.getApiKey().getUser());
            entities = myBatis.selectList(message.getCommand(), params);
            if (entities.size() == 0) {
                StatusMessage status = new StatusMessage(Messages.NOT_FOUND);
                reply.setMessage(status);
            }
        } else {
            params.put("owner", message.getApiKey().getUser());
            entities = myBatis.selectList(message.getCommand(), params);
        }
        afterRead(message, entities);
        reply.setEntities(entities);
        return reply;
    }

    /**
     *
     * @param message
     * @return
     */
    protected BusMessageReply handleDelete(final BusMessageStore message) {
        BusMessageReply reply = new BusMessageReply();
        if (message.getParameter() != null) {
            beforeDelete(message);
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("id", message.getParameter());
            params.put("owner", message.getApiKey().getUser());
            myBatis.delete(message.getCommand(), params);
            afterDelete(message);
            reply.setMessage(new StatusMessage(Messages.REMOVED));
        } else {
            reply.setMessage(new StatusMessage(Messages.ID_REQUIRED));
        }
        return reply;
    }

    /**
     * This operation requires data validation.
     *
     * @param message
     * @return
     */
    protected BusMessageReply handleUpdate(final BusMessageStore message) {
        BusMessageReply reply = new BusMessageReply();
        if (message.getEntity() != null) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Object>> violations = validator.validate(message.getEntity());
            if (violations.size() == 0) {
                beforeUpdate(message);
                myBatis.update(message.getCommand(), message.getEntity());
                afterUpdate(message);
                reply.setMessage(new StatusMessage(Messages.OK));
            } else {
                StatusMessage failure = new StatusMessage(Messages.VALIDATION);
                for (ConstraintViolation<Object> violation : violations) {
                    failure.addExtra(violation.getPropertyPath().toString(), violation.getMessage());
                }
                reply.setMessage(failure);
            }
        } else {
            reply.setMessage(new StatusMessage(Messages.ENTITY_REQUIRED));
        }
        return reply;
    }

    /**
     * This operation requires data validation.
     *
     * @param message
     * @return
     */
    protected BusMessageReply handleCreate(final BusMessageStore message) {
        BusMessageReply reply = new BusMessageReply();
        if (message.getEntity() != null) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<Object>> violations = validator.validate(message.getEntity());
            if (violations.size() == 0) {
                beforeCreate(message);
                myBatis.insert(message.getCommand(), message.getEntity());
                afterCreate(message);
                reply.setEntity(message.getEntity());
                reply.setMessage(new StatusMessage(Messages.CREATED));
            } else {
                StatusMessage failure = new StatusMessage(Messages.VALIDATION);
                for (ConstraintViolation<Object> violation : violations) {
                    failure.addExtra(violation.getPropertyPath().toString(), violation.getMessage());
                }
                reply.setMessage(failure);
            }
        } else {
            reply.setMessage(new StatusMessage(Messages.ENTITY_REQUIRED));
        }
        return reply;
    }

    /**
     * Checks if a message has a valid API Key.
     *
     * @param message
     * @return
     */
    protected Boolean isAuthenticated(final BusMessageStore message) {
        if (message.getApiKey().getApikey() == null) {
            LOGGER.debug("API key (or session ID) not found in request");
            return Boolean.FALSE;
        }
        ApiKey apiKey = null;
        apiKey = myBatis.selectOne("ApiKeyMapper.byApiKey", message.getApiKey().getApikey());
        if (apiKey == null) {
            LOGGER.debug("API key not found in database! [" + message.getApiKey().getApikey() + "]");
            return Boolean.FALSE;
        }
        message.setApiKey(apiKey);
        if (message.getApiKey().getUser() != null) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    /**
     * This method can be overriden by childs to add addtional handling logic.
     * It will be called only if the default bus actions (BusAction) did not apply.
     *
     * @param message The bus message
     * @return the response as String
     */
    protected BusMessageReply handleAdditional(final BusMessageStore message) {
        BusMessageReply reply = new BusMessageReply();
        StatusMessage failure = new StatusMessage(Messages.INTERNAL_ERROR);
        failure.addExtra(message.getAction(), "Action is not supported");
        reply.setMessage(failure);
        return reply;
    }

    /**
     * TODO: Add description.
     *
     * @return
     */
    protected abstract ObjectFactory getObjectFactory();

    /**
     * Called after read an entity (a call to mybatis.select).
     *
     * @param message the bus message
     * @param entities entities read from the store
     */
    protected void afterRead(final BusMessageStore message, final List<Object> entities) {
    }

    /**
     *
     * @param message
     */
    protected void beforeRead(final BusMessageStore message) {
    }

    /**
     *
     * @param message
     */
    protected void afterDelete(final BusMessageStore message) {
    }

    /**
     *
     * @param message
     */
    protected void beforeDelete(final BusMessageStore message) {
    }

    /**
     *
     * @param message
     */
    protected void afterUpdate(final BusMessageStore message) {
    }

    /**
     *
     * @param message
     */
    protected void beforeUpdate(final BusMessageStore message) {
    }

    /**
     *
     * @param message
     */
    protected void afterCreate(final BusMessageStore message) {
    }

    /**
     *
     * @param message
     */
    protected void beforeCreate(final BusMessageStore message) {
    }

}
