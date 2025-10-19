package com.ecom.core.config;

import com.ecom.core.util.ResourceUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import org.yaml.snakeyaml.Yaml;

/**
 * Centralised environment configuration loader that resolves values from classpath resources.
 */
public final class EnvConfig {

  private static final String APPLICATION_DEFAULT = "config/application-default.properties";
  private static final String APPLICATION_ENV_PROPERTIES = "config/application-%s.properties";
  private static final String APPLICATION_ENV_YAML = "config/application-%s.yaml";
  private static final String APPLICATION_ENV_YML = "config/application-%s.yml";

  private static final String ENV_PROPERTIES = "config/env.properties";
  private static final String CARDS_PROPERTIES = "config/cards.properties";
  private static final String DB_PROPERTIES = "config/db.properties";

  private static final EnvConfig INSTANCE = new EnvConfig();

  private final Properties properties = new Properties();
  private final Properties envProperties = new Properties();
  private final Properties cardProperties = new Properties();
  private final Properties dbProperties = new Properties();
  private final String environment;

  private EnvConfig() {
    loadOptional(APPLICATION_DEFAULT, null);
    loadOptional(ENV_PROPERTIES, envProperties);
    this.environment =
        System.getProperty("env", properties.getProperty("default_env", "default"));
    loadOptional(String.format(APPLICATION_ENV_PROPERTIES, environment), null);
    loadOptional(String.format(APPLICATION_ENV_YAML, environment), null);
    loadOptional(String.format(APPLICATION_ENV_YML, environment), null);
    loadOptional(DB_PROPERTIES, dbProperties);
    loadOptional(CARDS_PROPERTIES, cardProperties);
  }

  public static EnvConfig getInstance() {
    return INSTANCE;
  }

  public String getEnvironment() {
    return environment;
  }

  public String get(String key) {
    return resolve(key, null);
  }

  public String get(String key, String defaultValue) {
    return resolve(key, defaultValue);
  }

  public boolean getBoolean(String key) {
    return Boolean.parseBoolean(resolve(key, "false"));
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    String value = resolve(key, null);
    return value == null ? defaultValue : Boolean.parseBoolean(value);
  }

  public int getInt(String key, int defaultValue) {
    String value = resolve(key, null);
    return value == null ? defaultValue : Integer.parseInt(value);
  }

  public long getLong(String key, long defaultValue) {
    String value = resolve(key, null);
    return value == null ? defaultValue : Long.parseLong(value);
  }

  public Properties asProperties() {
    Properties copy = new Properties();
    copy.putAll(properties);
    return copy;
  }

  public Properties getEnvProperties() {
    Properties copy = new Properties();
    copy.putAll(envProperties);
    return copy;
  }

  public String getEnvValue(String key) {
    return envProperties.getProperty(key);
  }

  public Properties getCardProperties() {
    Properties copy = new Properties();
    copy.putAll(cardProperties);
    return copy;
  }

  public String getCardValue(String key) {
    return cardProperties.getProperty(key);
  }

  public Properties getDatabaseProperties() {
    Properties copy = new Properties();
    copy.putAll(dbProperties);
    return copy;
  }

  public DatabaseSettings getDatabaseSettings(String prefix) {
    Objects.requireNonNull(prefix, "prefix");
    String driver = resolve(prefix + ".driver", null);
    String url = resolve(prefix + ".url", null);
    String username = resolve(prefix + ".username", null);
    String password = resolve(prefix + ".password", null);
    if (driver == null || url == null || username == null || password == null) {
      return null;
    }
    return new DatabaseSettings(driver, url, username, password);
  }

  public String getForEnvironment(String key) {
    return resolve(key, null);
  }

  private String resolve(String key, String defaultValue) {
    Objects.requireNonNull(key, "key");
    String envAwareKey = key + '.' + environment;
    if (properties.containsKey(envAwareKey)) {
      return properties.getProperty(envAwareKey);
    }
    return Optional.ofNullable(properties.getProperty(key)).orElse(defaultValue);
  }

  private void loadOptional(String path, Properties target) {
    if (path == null || path.isBlank()) {
      return;
    }
    try (InputStream inputStream = ResourceUtils.stream(path)) {
      if (path.endsWith(".yaml") || path.endsWith(".yml")) {
        loadYaml(inputStream, target);
      } else {
        Properties loaded = new Properties();
        loaded.load(inputStream);
        properties.putAll(loaded);
        if (target != null) {
          target.putAll(loaded);
        }
      }
    } catch (IllegalStateException ignored) {
      // resource is optional
    } catch (IOException exception) {
      throw new IllegalStateException("Unable to load configuration from " + path, exception);
    }
  }

  private void loadYaml(InputStream inputStream, Properties target) {
    Object loaded = new Yaml().load(inputStream);
    if (loaded instanceof Map<?, ?> map) {
      flattenYaml("", map, target);
    }
  }

  @SuppressWarnings("unchecked")
  private void flattenYaml(String prefix, Map<?, ?> map, Properties target) {
    for (Map.Entry<?, ?> entry : map.entrySet()) {
      if (!(entry.getKey() instanceof String key)) {
        continue;
      }
      String fullKey = prefix.isBlank() ? key : prefix + '.' + key;
      Object value = entry.getValue();
      if (value instanceof Map<?, ?> child) {
        flattenYaml(fullKey, child, target);
      } else {
        String text = Objects.toString(value, "");
        properties.put(fullKey, text);
        if (target != null) {
          target.put(fullKey, text);
        }
      }
    }
  }
}
