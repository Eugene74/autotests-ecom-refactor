package com.ecom.ui.driver;

import org.openqa.selenium.WebDriver;

/**
 * Thread-bound holder for the active {@link WebDriver} instance used by legacy flows that still
 * depend on Selenium APIs directly.
 */
public final class WebDriverHolder {

  private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

  private WebDriverHolder() {}

  public static void setDriver(WebDriver webDriver) {
    DRIVER.set(webDriver);
  }

  public static WebDriver getDriver() {
    return DRIVER.get();
  }

  public static void removeDriver() {
    DRIVER.remove();
  }
}
