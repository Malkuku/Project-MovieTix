package com.movietix.xiazihao.config;

import com.movietix.xiazihao.constants.ConstantsManager;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DataSourceConfig {
    private static final String JDBC_URL = ConstantsManager.getInstance().getDataBaseURL();
    private static final String USERNAME = ConstantsManager.getInstance().getDataBaseUsername();
    private static final String PASSWORD = ConstantsManager.getInstance().getDataBasePassword();

    public static DataSource createDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(JDBC_URL);
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMaximumPoolSize(10);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        return new HikariDataSource(config);
    }

    //TODO 服务器热交换时，关闭连接池
}
