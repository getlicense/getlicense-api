/*
 * getlicense.io
 * Copyright (C) 2014 klicap - ingeniería del puzle
 *
 * $Id: LicenseFileFactory.java 281 2014-10-26 11:23:22Z recena $
 */
package es.klicap.getlicense.json.factory;

import java.lang.reflect.Type;
import java.util.Map;

import es.klicap.getlicense.filetoolkit.impl.DefaultLicense;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;

public class LicenseFileFactory implements ObjectFactory {

    @Override
    public Object instantiate(final ObjectBinder context, final Object value, final Type targetType, final Class targetClass) {
        return context.bindIntoObject((Map) value, new DefaultLicense(), targetType);
    }

}
