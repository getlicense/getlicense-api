/*
 * getlicense.io
 * Copyright (C) 2015 klicap - ingeniería del puzle
 *
 * $Id: LicenseFormatter.java 334 2015-02-28 17:29:04Z amuniz $
 */
package es.klicap.getlicense.formats;

import es.klicap.getlicense.filetoolkit.exception.InvalidKeysException;
import es.klicap.getlicense.model.License;

public interface LicenseFormatter {

    String format(License license, String publicKey, String privateKey) throws InvalidKeysException;

}
