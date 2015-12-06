/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: CustomerStore.java 385 2015-04-12 20:54:12Z recena $
 */
package es.klicap.getlicense.store;

import org.apache.ibatis.session.SqlSessionFactory;

import es.klicap.getlicense.json.BusMessageStore;
import es.klicap.getlicense.json.factory.CustomerFactory;
import es.klicap.getlicense.model.Customer;
import flexjson.ObjectFactory;

/**
 * Customer Store.
 */
public class CustomerStore extends StoreHandler {

    /**
     * Literal to identify event bus address.
     */
    public static final String EVENTBUS_ADDRESS = "customer.store";

    /**
     * Constructor with params.
     *
     * @param mybatis
     */
    public CustomerStore(final SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    protected ObjectFactory getObjectFactory() {
        return new CustomerFactory();
    }

    @Override
    protected void beforeUpdate(final BusMessageStore message) {
        Customer c = (Customer) message.getEntity();
        c.setId(new Long(message.getParameter()));
        c.setOwner(message.getApiKey().getUser());
        message.setEntity(c);
    }

    @Override
    protected void beforeCreate(final BusMessageStore message) {
        Customer customer = (Customer) message.getEntity();
        customer.setOwner(message.getApiKey().getUser());
        message.setEntity(customer);
    }
}
