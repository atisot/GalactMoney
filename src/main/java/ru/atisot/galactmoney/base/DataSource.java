package ru.atisot.galactmoney.base;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.atisot.galactmoney.Config;

import java.sql.Connection;
import java.sql.SQLException;

public class DataSource {

    private static HikariConfig cfg = new HikariConfig();
    private static HikariDataSource ds;
    private static String jdbcUrl;

    static {
        jdbcUrl = "jdbc:mysql://" + Config.getHost() + ":" + Config.getPort() + "/" + Config.getDataBaseName() + "?useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";

        //cfg.setDataSourceClassName("com.mysql.cj.jdbc.Driver");
        cfg.setJdbcUrl(jdbcUrl);
        cfg.setUsername(Config.getUsername());
        cfg.setPassword(Config.getPassword());
        cfg.addDataSourceProperty( "cachePrepStmts" , "true" );
        cfg.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        cfg.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        ds = new HikariDataSource(cfg);
    }

    private DataSource() {}

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
