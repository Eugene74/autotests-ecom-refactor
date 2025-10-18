package jdbc;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JDBCConnection {

    private static final String DB_DRIVER = "oracle.jdbc.driver.OracleDriver";

    private static Properties configProperties = new Properties();
    private static Properties envProperties = new Properties();
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
        try (FileInputStream input = new FileInputStream("properties/db.properties")) {
            FileInputStream fisEnv = new FileInputStream("properties/env.properties");
            envProperties.load(fisEnv);
            configProperties.load(input);
            DB_CONNECTION_TP2ST = String.format(
                    configProperties.getProperty(  "DB_CONNECTION_TP2ST"),
                    System.getProperty("env", envProperties.getProperty("default_env"))
            );
            DB_USER_OWNER = configProperties.getProperty("DB_USER_OWNER");
            DB_PASSWORD = configProperties.getProperty("DB_PASSWORD");
        }
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
