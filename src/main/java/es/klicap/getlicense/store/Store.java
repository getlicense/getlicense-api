/*
 * getlicense.io
 * Copyright (C) 2013-2014 klicap - ingeniería del puzle
 *
 * $Id: Store.java 385 2015-04-12 20:54:12Z recena $
 */
package es.klicap.getlicense.store;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.ibatis.builder.xml.XMLConfigBuilder;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.eventbus.EventBus;
import org.vertx.java.platform.Verticle;

import redis.clients.jedis.Jedis;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * Verticle to access data store.
 */
public class Store extends Verticle {

    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Store.class);

    /**
     *
     */
    @Override
    public void start() {
        EventBus eb = vertx.eventBus();

        Jedis jedis = new Jedis("localhost");
        SqlSessionFactory sqlSessionFactory = initMyBatis();

        eb.registerHandler(ProductStore.EVENTBUS_ADDRESS, new ProductStore(sqlSessionFactory));
        eb.registerHandler(LicenseStore.EVENTBUS_ADDRESS, new LicenseStore(sqlSessionFactory));
        eb.registerHandler(LicenseFileStore.EVENTBUS_ADDRESS, new LicenseFileStore(sqlSessionFactory));
        eb.registerHandler(LicenseTypeStore.EVENTBUS_ADDRESS, new LicenseTypeStore(sqlSessionFactory));
        eb.registerHandler(CustomerStore.EVENTBUS_ADDRESS, new CustomerStore(sqlSessionFactory));
        eb.registerHandler(UserStore.EVENTBUS_ADDRESS, new UserStore(sqlSessionFactory));
        eb.registerHandler(SessionStore.EVENTBUS_ADDRESS, new SessionStore(sqlSessionFactory, jedis));
        eb.registerHandler(RegisterStore.EVENTBUS_ADDRESS, new RegisterStore(sqlSessionFactory, jedis));
    }

    /**
     * Configure MyBatis and C3P0 as JDBC connection pool.
     *
     * Related info about C3P0:
     * http://www.mchange.com/projects/c3p0/#locating_configuration_information
     *
     * NOTE: DataSources are usually configured before they are used, either during or immediately
     * following their construction. c3p0 does support property modifications midstream, however.
     */
    private SqlSessionFactory initMyBatis() {
        SqlSessionFactory factory = null;
        try {
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            XMLConfigBuilder configFromFile = new XMLConfigBuilder(inputStream);
            Configuration config = configFromFile.parse();

            String c3p0ConfigFilePath = findC3P0ConfigFile();
            if (c3p0ConfigFilePath != null) {
                System.setProperty("com.mchange.v2.c3p0.cfg.xml", c3p0ConfigFilePath);
            }

            ComboPooledDataSource datasource = new ComboPooledDataSource();
            datasource.setDriverClass("com.mysql.jdbc.Driver");
            datasource.setJdbcUrl(this.container.config().getString("url"));
            datasource.setUser(this.container.config().getString("username"));
            datasource.setPassword(this.container.config().getString("password"));

            Environment development = new Environment("development", new JdbcTransactionFactory(), datasource);
            config.setEnvironment(development);
            factory = new SqlSessionFactoryBuilder().build(config);
            inputStream.close();
        } catch (IOException | PropertyVetoException ex) {
            LOGGER.error(ex.getMessage());
        }
        return factory;
    }

    /**
     *
     */
    private String findC3P0ConfigFile() {
        String envvar = System.getenv("API_HOME");
        String filepath = envvar + "/conf/c3p0-config.xml";
        File file = new File(filepath);
        if (!file.exists() || file.isDirectory()) {
            filepath = null;
            LOGGER.debug("c3p0 configured using default values");
        } else {
            LOGGER.debug("c3p0 configured using a file found: " + filepath);
        }
        return filepath;
    }
}