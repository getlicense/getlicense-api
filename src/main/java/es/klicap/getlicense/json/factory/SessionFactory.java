/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: UserFactory.java 33 2013-12-09 21:23:09Z recena $
 */
package es.klicap.getlicense.json.factory;

import java.lang.reflect.Type;
import java.util.Map;

import es.klicap.getlicense.model.Session;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;

public class SessionFactory implements ObjectFactory {

    @Override
    public Object instantiate(final ObjectBinder context, final Object value, final Type targetType, final Class targetClass) {
        return context.bindIntoObject((Map) value, new Session(), targetType);
    }

}