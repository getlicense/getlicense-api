/*
 * getlicense.io
 * Copyright (C) 2015 klicap - ingeniería del puzle
 *
 * $Id: PropertiesFormatter.java 346 2015-03-07 11:41:07Z recena $
 */
package es.klicap.getlicense.formats;

import es.klicap.getlicense.filetoolkit.LicenseWriter;
import es.klicap.getlicense.filetoolkit.exception.InvalidKeysException;
import es.klicap.getlicense.filetoolkit.props.PropertiesLicenseWriter;
import es.klicap.getlicense.model.License;
import es.klicap.getlicense.model.Property;

public class PropertiesFormatter implements LicenseFormatter {

    @Override
    public String format(final License license, final String publicKey, final String privateKey) throws InvalidKeysException {
        LicenseWriter writer = new PropertiesLicenseWriter(publicKey, privateKey);
        for (Property property : license.getProperties()) {
            writer.addFeature(property.getName(), property.getValue());
        }
        writer.addFeature("expiration", license.getExpiration().toString());
        writer.addFeature("customer", license.getCustomer().getCompany());
        return writer.write();
    }

}
