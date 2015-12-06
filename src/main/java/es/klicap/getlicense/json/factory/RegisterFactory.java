/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: RegisterFactory.java 306 2014-11-16 12:31:04Z recena $
 */
package es.klicap.getlicense.json.factory;

import java.lang.reflect.Type;
import java.util.Map;

import es.klicap.getlicense.model.Register;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;

public class RegisterFactory implements ObjectFactory {

    @Override
    public Object instantiate(final ObjectBinder context, final Object value, final Type targetType, final Class targetClass) {
        return context.bindIntoObject((Map) value, new Register(), targetType);
    }

}