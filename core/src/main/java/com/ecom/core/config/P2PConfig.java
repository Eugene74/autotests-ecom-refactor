package com.ecom.core.config;

/**
 * Загрузчик конфигурации P2P (Money Transfer). Данные берутся из
 * resources/config/p2p-data.properties.
 */
public class P2PConfig {

  private static final PropertiesManager PROPERTIES = PropertiesManager.getInstance();

  public static String senderName() {
    return PROPERTIES.getEnv("p2p.sender.name");
  }

  public static String senderCity() {
    return PROPERTIES.getEnv("p2p.sender.city");
  }

  public static String senderStreet() {
    return PROPERTIES.getEnv("p2p.sender.street");
  }

  public static String senderHouse() {
    return PROPERTIES.getEnv("p2p.sender.house");
  }

  public static String senderFlat() {
    return PROPERTIES.getEnv("p2p.sender.flat");
  }

  public static String senderPhone() {
    return PROPERTIES.getEnv("p2p.sender.phone");
  }

  public static String recipientName() {
    return PROPERTIES.getEnv("p2p.recipient.name");
  }

  public static String amountDefault() {
    return PROPERTIES.getEnv("p2p.amount.default");
  }

  public static String currency() {
    return PROPERTIES.getEnv("p2p.currency");
  }

  public static String description() {
    return PROPERTIES.getEnv("p2p.description");
  }
}
