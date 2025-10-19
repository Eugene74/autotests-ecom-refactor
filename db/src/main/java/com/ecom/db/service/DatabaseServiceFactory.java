package com.ecom.db.service;

import com.ecom.core.config.DatabaseSettings;
import com.ecom.core.config.EnvConfig;
import com.ecom.db.config.DatabaseType;

public final class DatabaseServiceFactory {

  private static volatile DatabaseService databaseService;

  private DatabaseServiceFactory() {}

  public static DatabaseService getDatabaseService() {
    if (databaseService == null) {
      synchronized (DatabaseServiceFactory.class) {
        if (databaseService == null) {
          databaseService = buildService();
        }
      }
    }
    return databaseService;
  }

  private static DatabaseService buildService() {
    EnvConfig config = EnvConfig.getInstance();
    String vendorName = config.get("db.vendor", "oracle").toUpperCase();
    DatabaseType type = DatabaseType.valueOf(vendorName);
    DatabaseSettings settings = config.getDatabaseSettings("db." + vendorName.toLowerCase());
    if (settings == null) {
      throw new IllegalStateException("Database settings not found for vendor " + vendorName);
    }
    return switch (type) {
      case ORACLE -> new OracleDatabaseService(settings);
      case POSTGRES -> new PostgresDatabaseService(settings);
    };
  }
}
