CREATE TABLE `GL_APIKEY` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `APIKEY` varchar(255) COLLATE utf8_bin NOT NULL,
  `CREATION` bigint(20) NOT NULL,
  `USER_ID_PK` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `GL_ATTRIBUTE` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `LICENSETYPE_ID_PK` bigint(20) NOT NULL,
  `NAME` varchar(256) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `GL_CUSTOMER` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `COMPANY` varchar(2000) COLLATE utf8_bin NOT NULL,
  `USER_ID_PK` bigint(20) NOT NULL,
  `DELETION` bigint(20) DEFAULT NULL,
  `FIRST_NAME` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `LAST_NAME` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `STREET` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `POSTAL_CODE` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `CITY` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `STATE` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `COUNTRY` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `EMAIL` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `WEB` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  `TAXID` varchar(200) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `GL_LICENSE` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CREATION` bigint(20) NOT NULL,
  `PRODUCT_ID_PK` bigint(20) NOT NULL,
  `CUSTOMER_ID_PK` bigint(20) NOT NULL,
  `UUID` varchar(200) COLLATE utf8_bin NOT NULL,
  `EXPIRATION` bigint(20) DEFAULT NULL,
  `USER_ID_PK` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `GL_LICENSETYPE` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(2000) COLLATE utf8_bin NOT NULL,
  `DEFAULT_EXPIRATION` int(11) NOT NULL,
  `PRODUCT_ID_PK` bigint(20) NOT NULL,
  `USER_ID_PK` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `GL_PRODUCT` (
  `NAME` varchar(2000) COLLATE utf8_bin NOT NULL,
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USER_ID_PK` bigint(20) NOT NULL,
  `DELETION` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `GL_PROPERTY` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `VALUE` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `LICENSE_ID_PK` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `GL_USER` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(256) COLLATE utf8_bin NOT NULL,
  `EMAIL` varchar(256) COLLATE utf8_bin NOT NULL,
  `CREATION` bigint(20) NOT NULL,
  `DELETION` bigint(20) DEFAULT NULL,
  `HASHED_PASSWD` varchar(256) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `ID_UNIQUE` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

INSERT INTO GL_USER (CREATION, USERNAME, EMAIL, HASHED_PASSWD) VALUES (UNIX_TIMESTAMP(), "recena", "recena@klicap.es", SHA1('getlicense'));
INSERT INTO GL_APIKEY (CREATION, APIKEY, USER_ID_PK) VALUES (UNIX_TIMESTAMP(), "08893a38-de79-11e3-ba79-6a29915d1c76", 1);