/*
 * getlicense.io
 * Copyright (C) 2015 klicap - ingeniería del puzle
 *
 * $Id: DBMigrator.java 346 2015-03-07 11:41:07Z recena $
 */
package es.klicap.getlicense.migration;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;

public class DBMigrator {

    private Flyway flyway;

    public DBMigrator(final String dbUrl, final String username, final String password) {
        flyway = new Flyway();
        flyway.setDataSource(dbUrl, username, password);
        flyway.setTable("GL_DBMETADATA");
    }

    public void run() {
        if (!isInitialized()) {
            flyway.setBaselineOnMigrate(true);
            flyway.migrate();
        } else {
            if (existsPendingUpdates()) {
                flyway.migrate();
            }
        }
    }

    /**
     * True if the flyway management table exists. False otherwise.
     */
    private Boolean isInitialized() {
        MigrationInfo mi = flyway.info().current();
        return (mi != null) ? Boolean.TRUE : Boolean.FALSE;
    }

    /**
     * True if flyway detects pending updates to apply. False otherwise.
     */
    private Boolean existsPendingUpdates() {
        MigrationInfo[] mi = flyway.info().pending();
        return (mi.length > 0) ? Boolean.TRUE : Boolean.FALSE;
    }

}
