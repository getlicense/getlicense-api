/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: LicenseTypeStore.java 385 2015-04-12 20:54:12Z recena $
 */
package es.klicap.getlicense.store;

import org.apache.ibatis.session.SqlSessionFactory;

import es.klicap.getlicense.json.BusMessageStore;
import es.klicap.getlicense.json.factory.LicenseTypeFactory;
import es.klicap.getlicense.model.Attribute;
import es.klicap.getlicense.model.LicenseType;
import flexjson.ObjectFactory;

/**
 * LicenseType Store.
 */
public class LicenseTypeStore extends StoreHandler {

    /**
     * Literal to identify event bus address.
     */
    public static final String EVENTBUS_ADDRESS = "license.type.store";

    /**
     * Constructor with params.
     *
     * @param mybatis
     */
    public LicenseTypeStore(final SqlSessionFactory mybatis) {
        super(mybatis);
    }

    @Override
    protected ObjectFactory getObjectFactory() {
        return new LicenseTypeFactory();
    }

    @Override
    protected void beforeCreate(final BusMessageStore message) {
        LicenseType licenseType = (LicenseType) message.getEntity();
        licenseType.setOwner(message.getApiKey().getUser());
        message.setEntity(licenseType);
    }

    @Override
    protected void afterCreate(final BusMessageStore message) {
        LicenseType licenseType = (LicenseType) message.getEntity();
        for (String kee : licenseType.getAttributes()) {
            Attribute attribute = new Attribute(kee, licenseType.getId());
            myBatis.insert("AttributeMapper.insert", attribute);
        }
    }

    @Override
    protected void beforeUpdate(final BusMessageStore message) {
        LicenseType licenseType = (LicenseType) message.getEntity();
        licenseType.setId(new Long(message.getParameter()));
        licenseType.setOwner(message.getApiKey().getUser());
        message.setEntity(licenseType);
        myBatis.delete("AttributeMapper.deleteByLicenseType", licenseType.getId());
    }

    @Override
    protected void afterUpdate(final BusMessageStore message) {
        afterCreate(message);
    }

    @Override
    protected void beforeDelete(final BusMessageStore message) {
        myBatis.insert("AttributeMapper.deleteByLicenseType", message.getParameter());
    }
}
