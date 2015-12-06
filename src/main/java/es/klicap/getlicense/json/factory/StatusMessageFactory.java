/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: StatusMessageFactory.java 259 2014-09-14 17:44:25Z recena $
 */
package es.klicap.getlicense.json.factory;

import java.lang.reflect.Type;
import java.util.Map;

import es.klicap.getlicense.json.StatusMessage;
import flexjson.ObjectBinder;
import flexjson.ObjectFactory;

public class StatusMessageFactory implements ObjectFactory {

    @Override
    public Object instantiate(final ObjectBinder context, final Object value, final Type targetType, final Class targetClass) {
        return context.bindIntoObject((Map) value, new StatusMessage(), targetType);
    }

}