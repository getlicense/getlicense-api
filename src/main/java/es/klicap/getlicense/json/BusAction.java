/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: Attribute.java 11 2013-11-24 10:17:34Z recena $
 */
package es.klicap.getlicense.json;

/**
 * TODO: Add description.
 */
public enum BusAction {

    /**
     * Create entity.
     */
    CREATE("create"),

    /**
     * Read an entity or several.
     */
    READ("read"),

    /**
     * Update an entity or several.
     */
    UPDATE("update"),

    /**
     * Delete an entity or several.
     */
    DELETE("delete");

    /**
     * Action name.
     */
    private String action;

    /**
     * Constructor with params.
     *
     * @param action
     */
    private BusAction(final String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return action;
    }
}
