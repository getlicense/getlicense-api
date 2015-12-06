/*
 * getlicense.io
 * Copyright (C) 2013-2015 klicap - ingeniería del puzle
 *
 * $Id: LicenseFileStore.java 385 2015-04-12 20:54:12Z recena $
 */
package es.klicap.getlicense.store;

import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.klicap.getlicense.filetoolkit.exception.InvalidKeysException;
import es.klicap.getlicense.formats.LicenseFormatEnum;
import es.klicap.getlicense.formats.LicenseFormatter;
import es.klicap.getlicense.json.BusMessageReply;
import es.klicap.getlicense.json.BusMessageStore;
import es.klicap.getlicense.json.Messages;
import es.klicap.getlicense.json.StatusMessage;
import es.klicap.getlicense.model.License;
import es.klicap.getlicense.model.Product;
import flexjson.ObjectFactory;
import flexjson.factories.StringObjectFactory;

/**
 * TODO: Add description.
 */
public class LicenseFileStore extends StoreHandler {

    /**
     * Literal to identify event bus address.
     */
    public static final String EVENTBUS_ADDRESS = "license.file.store";

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(LicenseFileStore.class);

    /**
     * Constructor with params.
     *
     * @param mybatis
     */
    public LicenseFileStore(final SqlSessionFactory mybatis) {
        super(mybatis);
    }

    @Override
    protected ObjectFactory getObjectFactory() {
        return new StringObjectFactory();
    }

    /**
     * Handle formatted license request.
     *
     * The bus message comes with:
     *  - Action: one of "generate_file" or "validate_file"
     *  - Entity: the format key (must be a value defined in LicenseFormatEnum)
     */
    @Override
    protected BusMessageReply handleAdditional(final BusMessageStore message) {
        LOGGER.debug("License file requested. Format: " + message.getEntity());
        LicenseFormatter formatter = getFormatterInstance((String) message.getEntity());
        BusMessageReply reply = new BusMessageReply();
        if ("generate_file".equals(message.getAction())) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("uuid", message.getParameter());
            params.put("owner", message.getApiKey().getUser());
            License license = myBatis.selectOne(message.getCommand(), params);
            if (license != null) {
                Map<String, Object> productMapperParams = new HashMap<String, Object>();
                productMapperParams.put("id", license.getProduct().getId());
                productMapperParams.put("owner",  message.getApiKey().getUser());
                Product product = myBatis.selectOne("ProductMapper.byId", productMapperParams);
                try {
                    reply.setEntity(formatter.format(license, product.getPublicKey(), product.getPrivateKey()));
                } catch (InvalidKeysException e) {
                    reply.setMessage(new StatusMessage(Messages.INTERNAL_ERROR));
                }
            } else {
                reply.setMessage(new StatusMessage(Messages.NOT_FOUND));
            }
        } else if ("validate_file".equals(message.getAction())) {
            String licenseB64 = message.getParameter();
            //return validateLicense(licenseB64);
        } else {
            StatusMessage failure = new StatusMessage(Messages.INTERNAL_ERROR);
            failure.addExtra(message.getAction(), "Action is not supported");
            reply.setMessage(failure);
        }
        return reply;
    }

    private LicenseFormatter getFormatterInstance(final String id) {
        return (LicenseFormatter) LicenseFormatEnum.fromString(id).getFormatter();
    }
}
