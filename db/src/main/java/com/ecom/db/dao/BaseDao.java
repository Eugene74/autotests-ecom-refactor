package com.ecom.db.dao;

import com.ecom.db.exception.DatabaseAccessException;
import com.ecom.db.spi.ConnectionProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Base class for DAOs providing convenience helpers for query execution. */
public abstract class BaseDao {

  private final ConnectionProvider connectionProvider;

  protected BaseDao(ConnectionProvider connectionProvider) {
    this.connectionProvider = connectionProvider;
  }

  protected Connection getConnection() throws SQLException {
    return connectionProvider.getConnection();
  }

  protected <T> T query(
      String sql, SqlConsumer<PreparedStatement> binder, ResultSetExtractor<T> extractor) {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      if (binder != null) {
        binder.accept(statement);
      }
      try (ResultSet resultSet = statement.executeQuery()) {
        return extractor.extract(resultSet);
      }
    } catch (SQLException exception) {
      throw new DatabaseAccessException("Failed to execute query: " + sql, exception);
    }
  }

  protected int update(String sql, SqlConsumer<PreparedStatement> binder) {
    try (Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement(sql)) {
      if (binder != null) {
        binder.accept(statement);
      }
      return statement.executeUpdate();
    } catch (SQLException exception) {
      throw new DatabaseAccessException("Failed to execute update: " + sql, exception);
    }
  }

  @FunctionalInterface
  protected interface SqlConsumer<T> {
    void accept(T value) throws SQLException;
  }

  @FunctionalInterface
  protected interface ResultSetExtractor<T> {
    T extract(ResultSet resultSet) throws SQLException;
  }
}
