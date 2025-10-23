package com.ecom.ui.driver;

import com.codeborne.selenide.Configuration;
import com.ecom.core.config.EnvConfig;

/** Configures Selenide based on environment properties. */
public final class SelenideDriverManager {

  private SelenideDriverManager() {
    // utility class
  }

  public static void configure() {
    EnvConfig config = EnvConfig.getInstance();
    Configuration.browser = System.getProperty("browser", "chrome");
    boolean headless =
        config.getBoolean("selenide.headless", config.getBoolean("is_headless", false));
    Configuration.browserSize = headless ? "3840x2160" : Configuration.browserSize;
    Configuration.headless = headless;
    Configuration.timeout = config.getLong("selenide.timeout", 10_000L);
    Configuration.pageLoadTimeout = config.getLong("selenide.pageLoadTimeout", 30_000L);
    Configuration.reportsFolder = config.get("selenide.reportsFolder", "build/reports");
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
