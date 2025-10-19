package com.ecom.core.driver;

import com.codeborne.selenide.Configuration;
import com.ecom.core.config.PropertiesManager;

/**
 * Configures Selenide based on environment properties.
 */
public final class SelenideDriverManager {

  private SelenideDriverManager() {
    // utility class
  }

  public static void configure() {
    PropertiesManager properties = PropertiesManager.getInstance();
    Configuration.browser = System.getProperty("browser", "chrome");
    Configuration.browserSize = properties.isHeadless() ? "3840x2160" : Configuration.browserSize;
    Configuration.headless = properties.isHeadless();
    Configuration.timeout = Long.parseLong(properties.getEnv("selenide.timeout", "10000"));
    Configuration.pageLoadTimeout = Long.parseLong(properties.getEnv("selenide.pageLoadTimeout", "30000"));
    Configuration.reportsFolder = properties.getEnv("selenide.reportsFolder", "build/reports");
  }

  public static void ensureChromeDriverBinary() {
    boolean driverAvailable = false;
    try {
      Process process = new ProcessBuilder("chromedriver", "--version").start();
      driverAvailable = process.waitFor() == 0;
    } catch (Exception exception) {
      driverAvailable = false;
    }
    if (!driverAvailable) {
      String driverPath = System.getProperty("user.dir") + "/driver/chromedriver.exe";
      System.setProperty("webdriver.chrome.driver", driverPath);
    }
  }
}
