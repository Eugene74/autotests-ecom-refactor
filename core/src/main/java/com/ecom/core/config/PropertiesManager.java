package com.ecom.core.config;

import java.util.Properties;

/** Legacy adapter that delegates to {@link EnvConfig}. */
public final class PropertiesManager {

  private static final PropertiesManager INSTANCE = new PropertiesManager();

  private final EnvConfig config;

  private PropertiesManager() {
    this.config = EnvConfig.getInstance();
  }

  public static PropertiesManager getInstance() {
    return INSTANCE;
  }

  public String getEnvironment() {
    return config.getEnvironment();
  }

  public boolean isHeadless() {
    return config.getBoolean("is_headless", false);
  }

  public String getEnv(String key) {
    return config.get(key);
  }

  public String getEnv(String key, String defaultValue) {
    return config.get(key, defaultValue);
  }

  public Properties getEnvProperties() {
    return config.getEnvProperties();
  }

  public String getCard(String key) {
    String value = config.getCardValue(key);
    return value != null ? value : config.get(key);
  }

  public String[] getCardArray(String key) {
    String value = getCard(key);
    return value == null ? new String[0] : value.split(";");
  }

  public Properties getCardProperties() {
    return config.getCardProperties();
  }
}
