package com.ecom.core.config;

import java.util.Objects;

/** Immutable holder for database connection properties sourced from {@link EnvConfig}. */
public final class DatabaseSettings {

  private final String driverClassName;
  private final String url;
  private final String username;
  private final String password;

  public DatabaseSettings(String driverClassName, String url, String username, String password) {
    this.driverClassName = Objects.requireNonNull(driverClassName, "driverClassName");
    this.url = Objects.requireNonNull(url, "url");
    this.username = Objects.requireNonNull(username, "username");
    this.password = Objects.requireNonNull(password, "password");
  }

  public String driverClassName() {
    return driverClassName;
  }

  public String url() {
    return url;
  }

  public String username() {
    return username;
  }

  public String password() {
    return password;
  }
}
