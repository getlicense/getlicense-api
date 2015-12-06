/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: UserStore.java 385 2015-04-12 20:54:12Z recena $
 */
package es.klicap.getlicense.store;

import java.util.UUID;

import org.apache.ibatis.session.SqlSessionFactory;

import es.klicap.getlicense.json.BusMessageStore;
import es.klicap.getlicense.json.factory.UserFactory;
import es.klicap.getlicense.model.ApiKey;
import es.klicap.getlicense.model.User;
import flexjson.ObjectFactory;

/**
 * User Store.
 */
public class UserStore extends StoreHandler {

    /**
     * Literal to identify event bus address.
     */
    public static final String EVENTBUS_ADDRESS = "user.store";

    /**
     * Constructor with params.
     *
     * @param mybatis
     */
    public UserStore(final SqlSessionFactory mybatis) {
        super(mybatis);
    }

    @Override
    protected ObjectFactory getObjectFactory() {
        return new UserFactory();
    }

    @Override
    protected void beforeUpdate(final BusMessageStore message) {
        User user = (User) message.getEntity();
        user.setId(new Long(message.getParameter()));
        message.setEntity(user);
    }

    @Override
    protected void afterCreate(final BusMessageStore message) {
        User user = (User) message.getEntity();
        ApiKey apiKey = new ApiKey();
        apiKey.setUser(user);
        apiKey.setApikey(UUID.randomUUID().toString());
        user.setApikey(apiKey.getApikey());
        message.setEntity(user);
        myBatis.insert("ApiKeyMapper.insert", apiKey);
    }
}