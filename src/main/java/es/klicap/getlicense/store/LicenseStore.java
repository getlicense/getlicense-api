/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: LicenseStore.java 385 2015-04-12 20:54:12Z recena $
 */
package es.klicap.getlicense.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.ibatis.session.SqlSessionFactory;

import es.klicap.getlicense.json.BusMessageStore;
import es.klicap.getlicense.json.factory.LicenseFactory;
import es.klicap.getlicense.model.License;
import es.klicap.getlicense.model.Property;
import flexjson.ObjectFactory;

/**
 * License Store.
 */
public class LicenseStore extends StoreHandler {

    /**
     * Literal to identify event bus address.
     */
    public static final String EVENTBUS_ADDRESS = "license.store";

    /**
     * Constructor with params.
     *
     * @param myBatis
     */
    public LicenseStore(final SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    protected ObjectFactory getObjectFactory() {
        return new LicenseFactory();
    }

    @Override
    protected void beforeCreate(final BusMessageStore message) {
        License l = (License) message.getEntity();
        l.setOwner(message.getApiKey().getUser());
        l.setUuid(UUID.randomUUID().toString());
        message.setEntity(l);
    }

    @Override
    protected void afterCreate(final BusMessageStore message) {
        License l = (License) message.getEntity();
        for (Property prop : l.getProperties()) {
            prop.setLicenseIdPk(l.getId());
            myBatis.insert("PropertyMapper.insert", prop);
        }
        l.setUrl(message.getBaseURI() + "/license/file/" + l.getUuid());
        message.setEntity(l);
    }

    @Override
    protected void afterRead(final BusMessageStore message, final List<Object> entities) {
        for (Object entity : entities) {
            License license = (License) entity;
            license.setUrl(message.getBaseURI() + "/license/file/" + license.getUuid());

            Map<String, Object> params = new HashMap<>();
            params.put("id", license.getProduct().getId());
            params.put("owner", message.getApiKey().getUser());
            Integer count = myBatis.selectOne("LicenseTypeMapper.countByProduct", params);
            license.getProduct().setLicenseTypes(count);
        }
    }

    @Override
    protected void beforeDelete(final BusMessageStore message) {
        myBatis.delete("PropertyMapper.deleteByLicense", message.getParameter());
    }
}
