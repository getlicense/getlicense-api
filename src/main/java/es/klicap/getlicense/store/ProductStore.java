/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: ProductStore.java 385 2015-04-12 20:54:12Z recena $
 */
package es.klicap.getlicense.store;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;

import es.klicap.getlicense.filetoolkit.key.KeyUtil;
import es.klicap.getlicense.filetoolkit.key.StringKeyPair;
import es.klicap.getlicense.json.BusMessageStore;
import es.klicap.getlicense.json.factory.ProductFactory;
import es.klicap.getlicense.model.LicenseType;
import es.klicap.getlicense.model.Product;
import flexjson.ObjectFactory;

/**
 * Product Store.
 */
public class ProductStore extends StoreHandler {

    /**
     * Literal to identify event bus address.
     */
    public static final String EVENTBUS_ADDRESS = "product.store";

    /**
     * Constructor with params.
     *
     * @param mybatis
     */
    public ProductStore(final SqlSessionFactory mybatis) {
        super(mybatis);
    }

    @Override
    protected ObjectFactory getObjectFactory() {
        return new ProductFactory();
    }

    @Override
    protected void afterRead(final BusMessageStore message, final List<Object> entities) {
        for (Object entity : entities) {
            Product product = (Product) entity;
            Map<String, Object> params = new HashMap<>();
            params.put("id", product.getId());
            params.put("owner", message.getApiKey().getUser());
            Integer count = myBatis.selectOne("LicenseTypeMapper.countByProduct", params);
            product.setLicenseTypes(count);
        }
    }

    @Override
    protected void beforeUpdate(final BusMessageStore message) {
        Product product = (Product) message.getEntity();
        product.setId(new Long(message.getParameter()));
        product.setOwner(message.getApiKey().getUser());
        message.setEntity(product);
    }

    @Override
    protected void beforeCreate(final BusMessageStore message) {
        Product product = (Product) message.getEntity();
        product.setOwner(message.getApiKey().getUser());
        StringKeyPair pair = KeyUtil.generateKeyPair();
        product.setPublicKey(pair.getPublicKey());
        product.setPrivateKey(pair.getPrivateKey());
        message.setEntity(product);
    }

    @Override
    protected void beforeDelete(final BusMessageStore message) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", message.getParameter());
        params.put("owner", message.getApiKey().getUser());
        List<LicenseType> licenseTypes = myBatis.selectList("LicenseTypeMapper.byProduct", params);
        for (LicenseType licenseType : licenseTypes) {
            myBatis.delete("AttributeMapper.deleteByLicenseType", licenseType.getId());
        }
        myBatis.delete("LicenseTypeMapper.deleteByProduct", params);
    }
}
