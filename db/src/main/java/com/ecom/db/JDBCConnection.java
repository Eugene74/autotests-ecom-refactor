package com.ecom.db;

import com.ecom.db.exception.DatabaseAccessException;
import com.ecom.db.service.DatabaseService;
import com.ecom.db.service.DatabaseServiceFactory;
import java.sql.Connection;
import java.sql.SQLException;

public final class JDBCConnection {

    private static final DatabaseService DATABASE_SERVICE = DatabaseServiceFactory.getDatabaseService();

    private JDBCConnection() {
    }

    public static String forLike(String input) {
        return "'" + input + "%'";
    }

    public static String forLikeExact(String input) {
        return "'" + input + "'";
    }

    public static Connection getDBConnection() {
        try {
            return DATABASE_SERVICE.getConnection();
        } catch (SQLException exception) {
            throw new DatabaseAccessException("Unable to obtain database connection", exception);
        }
    }
}
