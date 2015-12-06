/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: Product.java 2 2013-11-22 17:24:24Z recena $
 */
package es.klicap.getlicense.json;

import java.util.Map;

import java.util.Map.Entry;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.ObjectFactory;

/**
 * Helper to work with serialize/deserialize processes.
 */
public final class JSON {

    /**
     * Default constructor.
     */
    private JSON() {
    }

    /**
     *
     * @param o
     * @return
     */
    public static String serialize(final Object o) {
        return new JSONSerializer().exclude("*.class").deepSerialize(o);
    }

    /**
     *
     * @param json
     * @param clazz
     * @param factories
     * @return
     */
    public static <T> T deserialize(final String json, final Class<T> clazz, final Map<String, ObjectFactory> factories) {
        JSONDeserializer<T> deserializer = new JSONDeserializer<T>();
        if (factories != null) {
            for (Entry<String, ObjectFactory> e : factories.entrySet()) {
                deserializer.use(e.getKey(), e.getValue());
            }
        }
        return deserializer.deserialize(json, clazz);
    }

    /**
     *
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T deserialize(final String json, final Class<T> clazz) {
        return JSON.deserialize(json, clazz, null);
    }

    /**
     *
     * @param json
     * @return
     */
    public static <T> T deserialize(final String json) {
        JSONDeserializer<T> deserializer = new JSONDeserializer<T>();
        return deserializer.deserialize(json);
    }

}