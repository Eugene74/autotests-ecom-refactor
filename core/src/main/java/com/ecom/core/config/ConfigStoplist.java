package com.ecom.core.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Loads stoplist test data from the classpath. */
public final class ConfigStoplist {

  private static final String DATA_FILE = "/DataDriven/api.stoplist.properties";

  private static Properties properties;

  private ConfigStoplist() {
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
    try (InputStream input = ConfigStoplist.class.getResourceAsStream(DATA_FILE)) {
      if (input == null) {
        throw new IllegalStateException("Resource not found: " + DATA_FILE);
      }
      result.load(input);
    } catch (IOException exception) {
      throw new IllegalStateException("Unable to read stoplist properties", exception);
    }
    return result;
  }
}
