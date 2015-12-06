/*
 * getlicense.io
 * Copyright (C) 2014 klicap - ingeniería del puzle
 *
 * $Id: BusMessage.java 187 2014-07-17 06:28:54Z recena $
 */
package es.klicap.getlicense.json;

import java.util.ArrayList;
import java.util.List;


/**
 * Transient entity to send messages through the event bus.
 */
public abstract class BusMessage {

    /**
     * Can be a single entity or a set of entities.
     */
    protected List<Object> entities;

    /**
     * Default constructor.
     */
    public BusMessage() {
    }

    /**
     *
     * @return
     */
    public List<Object> getEntities() {
        return entities;
    }

    /**
     *
     * @param entity
     */
    public void setEntities(final List<Object> entities) {
        this.entities = entities;
    }

    /**
     *
     * @return
     */
    @flexjson.JSON(include = false)
    public Object getEntity() {
        if (entities.size() > 0) {
            return entities.get(0);
        } else {
            return null;
        }
    }

    /**
     *
     * @param entity
     */
    public void setEntity(final Object entity) {
        if (this.entities != null) {
            entities.add(entity);
        } else {
            entities = new ArrayList<Object>();
            entities.add(entity);
        }
    }
}