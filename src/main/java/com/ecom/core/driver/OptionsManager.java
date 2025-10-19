package com.ecom.core.driver;

import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Factory-style helper that produces {@link ChromeOptions} consistently across the test suite.
 */
public final class OptionsManager {

  private static final String WINDOW_SIZE_4K = "--window-size=3840,2160";

  private OptionsManager() {
    // utility class
  }

  public static ChromeOptions chrome(boolean headless) {
    ChromeOptions chromeOptions = new ChromeOptions();
    chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
    chromeOptions.addArguments("--ignore-certificate-errors");
    chromeOptions.addArguments("--allow-insecure-localhost");
    chromeOptions.addArguments("--disable-web-security");
    chromeOptions.addArguments("--no-sandbox");
    chromeOptions.addArguments("--disable-dev-shm-usage");

    if (headless) {
      chromeOptions.addArguments("--headless=new");
      chromeOptions.addArguments(WINDOW_SIZE_4K);
    }

    return chromeOptions;
  }
}
