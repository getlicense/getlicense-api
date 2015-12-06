/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: Product.java 2 2013-11-22 17:24:24Z recena $
 */
package es.klicap.getlicense.json;

/**
 * Preconfigured messages.
 * Internal code + Key + Description + HTTP CODE
 */
public enum Messages {

    ID_NOT_ALLOWED_ON_POST("100", "id.not.allowed", "Specific ID is not allowed for POST", 400),
    ID_REQUIRED("101", "id.required", "Specific ID is required", 400),
    ENTITY_REQUIRED("102", "entity.required", " An entity is required", 400),
    NOT_FOUND("103", "entity.notfound", "The resource does not exists", 404),
    INTERNAL_ERROR("104", "internal.error", "There was a problem processing the request", 500),
    FORBIDDEN("105", "access.denied", "Access denied", 403),
    INVALID_JSON("106", "invalid.json", "Invalid JSON for entity", 400),
    METHOD_NOT_ALLOWED("107", "method.not.allowed", "Method not allowed", 405),
    APIKEY_BADFORMAT("108", "apikey.badformat", "API Key not well formatted", 403),
    UNAUTHORIZED("109", "access.unautorized", "Access requieres authentication", 401),
    VALIDATION("110", "entity.validation", "Entity validation failure", 400),
    INVALID_LICENSE("111", "invalid.license", "The license is not valid", 400),
    BAD_CREDENTIALS("112", "bad.credencials", "Bad credentials", 401),

    OK("200", "ok", "OK", 200),
    CREATED("201", "entity.created", "A new resource has been created", 201),
    REMOVED("202", "entity.removed", "Entity has been removed", 200),
    VALID_LICENSE("203", "valid.license", "License is valid", 200),
    REGISTER_LICENSE("204", "valid.register", "Registration request is valid", 200);

    private String code;
    private String kee;
    private String description;
    private Integer httpCode;

    Messages(final String code, final String kee, final String description, final Integer httpCode) {
        this.code = code;
        this.kee = kee;
        this.description = description;
        this.httpCode = httpCode;
    }

    public String getKee() {
        return kee;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "(HTTP " + httpCode + "): " + code;
    }

    public String getCode() {
        return code;
    }

    public Integer getHttpCode() {
        return httpCode;
    }

}
