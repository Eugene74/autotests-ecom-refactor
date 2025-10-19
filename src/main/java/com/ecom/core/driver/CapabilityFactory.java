package com.ecom.core.driver;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * Produces Selenium {@link Capabilities} tailored for the requested browser.
 */
public final class CapabilityFactory {

  private CapabilityFactory() {
    // utility class
  }

  public static Capabilities forBrowser(String browser, boolean headless) {
    if ("chrome".equalsIgnoreCase(browser) || browser == null || browser.isBlank()) {
      ChromeOptions options = OptionsManager.chrome(headless);
      return options;
    }
    throw new IllegalArgumentException("Unsupported browser: " + browser);
  }
}
