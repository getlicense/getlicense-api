/*
 * getlicense.io
 * Copyright (C) 2015 klicap - ingeniería del puzle
 *
 * $Id: LicenseFormatEnum.java 346 2015-03-07 11:41:07Z recena $
 */
package es.klicap.getlicense.formats;

public enum LicenseFormatEnum {

    DEFAULT("default", new JsonFormatter()),

    JSON("json", new JsonFormatter()),

    PROPERTIES("props", new PropertiesFormatter());

    private String id;

    private LicenseFormatter formatter;

    LicenseFormatEnum(final String id, final LicenseFormatter formatter) {
        this.id = id;
        this.formatter = formatter;
    }

    public static LicenseFormatEnum fromString(final String id) {
        if (JSON.id.equals(id)) {
            return JSON;
        } else if (PROPERTIES.id.equals(id)) {
            return PROPERTIES;
        } else {
            return DEFAULT;
        }
    }

    public String getId() {
        return id;
    }

    public LicenseFormatter getFormatter() {
        return formatter;
    }

}
