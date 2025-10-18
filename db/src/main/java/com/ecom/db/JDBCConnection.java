package com.ecom.db;

import com.ecom.core.config.PropertiesManager;
import com.ecom.core.util.ResourceUtils;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCConnection {

    private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";

    private static final PropertiesManager PROPERTIES_MANAGER = PropertiesManager.getInstance();
    private static String DB_CONNECTION_TP2ST;
    private static String DB_USER_OWNER;
    private static String DB_PASSWORD;

    static {
        try {
            readConfig();
            Class.forName(DB_DRIVER);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void readConfig() throws IOException {
        Properties dbProperties = new Properties();
        try (InputStream input = ResourceUtils.stream("config/db.properties")) {
            dbProperties.load(input);
        }

        String environment = System.getProperty("env", PROPERTIES_MANAGER.getEnv("default_env"));
        DB_CONNECTION_TP2ST = String.format(dbProperties.getProperty("DB_CONNECTION_TP2ST"), environment);
        DB_USER_OWNER = dbProperties.getProperty("DB_USER_OWNER");
        DB_PASSWORD = dbProperties.getProperty("DB_PASSWORD");
    }

    public static String forLike(String input) {
        return "'" + input + "%'";
    }

    public static String forLikeExact(String input) {
        return "'" + input + "'";
    }

    protected static void close(Statement statement, Connection dbConnection) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Connection getDBConnection() {
        try {
            return DriverManager.getConnection(DB_CONNECTION_TP2ST, DB_USER_OWNER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
