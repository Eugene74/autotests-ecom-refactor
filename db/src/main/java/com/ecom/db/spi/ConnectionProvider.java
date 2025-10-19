package com.ecom.db.spi;

import java.sql.Connection;
import java.sql.SQLException;

/** Supplies JDBC {@link Connection} instances to DAO implementations. */
@FunctionalInterface
public interface ConnectionProvider {

  Connection getConnection() throws SQLException;
}
