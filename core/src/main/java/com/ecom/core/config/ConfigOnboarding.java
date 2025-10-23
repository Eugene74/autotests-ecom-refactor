package com.ecom.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Loads onboarding-related test data from the classpath. */
public final class ConfigOnboarding {

  private static final String DATA_FILE = "/DataDriven/api.onboarding.properties";

  private static Properties properties;

  private ConfigOnboarding() {
    // utility class
  }

  public static synchronized String getProperty(String propertyName) {
    if (properties == null) {
      properties = loadProperties();
    }
    return properties.getProperty(propertyName);
  }

  private static Properties loadProperties() {
    Properties result = new Properties();
    try (InputStream input = ConfigOnboarding.class.getResourceAsStream(DATA_FILE)) {
      if (input == null) {
        throw new IllegalStateException("Resource not found: " + DATA_FILE);
      }
      result.load(input);
    } catch (IOException exception) {
      throw new IllegalStateException("Unable to read onboarding properties", exception);
    }
    return result;
  }
}
