package com.ecom.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Centralised loader for environment-related property files.
 */
public final class PropertiesManager {

  private static final String ENV_PROPERTIES = "properties/env.properties";
  private static final String CARDS_PROPERTIES = "properties/cards.properties";

  private static final PropertiesManager INSTANCE = new PropertiesManager();

  private final Properties envProperties;
  private final Properties cardProperties;

  private PropertiesManager() {
    this.envProperties = load(ENV_PROPERTIES);
    this.cardProperties = load(CARDS_PROPERTIES);
  }

  public static PropertiesManager getInstance() {
    return INSTANCE;
  }

  private Properties load(String path) {
    Properties properties = new Properties();
    Path file = Path.of(path);
    if (!Files.exists(file)) {
      throw new IllegalStateException("Properties file not found: " + path);
    }
    try (InputStream inputStream = Files.newInputStream(file)) {
      properties.load(inputStream);
    } catch (IOException ex) {
      throw new IllegalStateException("Unable to load properties from " + path, ex);
    }
    return properties;
  }

  public String getEnvironment() {
    return System.getProperty("env", getEnv("default_env", "default"));
  }

  public boolean isHeadless() {
    return Boolean.parseBoolean(getEnv("is_headless", "false"));
  }

  public String getEnv(String key) {
    return envProperties.getProperty(key);
  }

  public String getEnv(String key, String defaultValue) {
    return envProperties.getProperty(key, defaultValue);
  }

  public String getCard(String key) {
    return cardProperties.getProperty(key);
  }

  public String[] getCardArray(String key) {
    String value = getCard(key);
    return value == null ? new String[0] : value.split(";");
  }
}
