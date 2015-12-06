/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: TokenAPI.java 4 2013-11-22 20:05:02Z recena $
 */
package es.klicap.getlicense.json.factory;

import java.lang.reflect.Type;
import java.util.Map;

import es.klicap.getlicense.model.Customer;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;

public class CustomerFactory implements ObjectFactory {

    @Override
    public Object instantiate(final ObjectBinder context, final Object value, final Type targetType, final Class targetClass) {
        return context.bindIntoObject((Map) value, new Customer(), targetType);
    }

}
