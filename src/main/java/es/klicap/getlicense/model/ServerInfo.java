/*
 * getlicense.io
 * Copyright (C) 2013 klicap - ingeniería del puzle
 *
 * $Id: ServerInfo.java 110 2014-04-13 10:56:40Z recena $
 */
package es.klicap.getlicense.model;

import java.io.Serializable;

/**
 *
 */
public class ServerInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3745716188798053195L;

    /**
     *
     */
    private String version;

    /**
     * Based on project build number.
     */
    private String revision;

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(final String version) {
        this.version = version;
    }

    /**
     * @return the revision
     */
    public String getRevision() {
        return revision;
    }

    /**
     * @param revision the revision to set
     */
    public void setRevision(final String revision) {
        this.revision = revision;
    }
}