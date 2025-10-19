package com.ecom.db.service;

import com.ecom.core.config.DatabaseSettings;
import com.ecom.db.spi.ConnectionProvider;
import java.sql.DriverManager;

final class OracleDatabaseService extends BaseDatabaseService {

  OracleDatabaseService(DatabaseSettings settings) {
    super(createProvider(settings));
    loadDriver(settings);
  }

  private static ConnectionProvider createProvider(DatabaseSettings settings) {
    return () -> DriverManager.getConnection(settings.url(), settings.username(), settings.password());
  }

  private static void loadDriver(DatabaseSettings settings) {
    try {
      Class.forName(settings.driverClassName());
    } catch (ClassNotFoundException exception) {
      throw new IllegalStateException("Oracle JDBC driver not found", exception);
    }
  }
}
