package com.ecom.tests.base;

import static com.codeborne.selenide.Selenide.closeWebDriver;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.ecom.core.config.EnvData;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Базовый класс для UI-тестов (Selenide). Использует конфигурацию из EnvData и обеспечивает единые
 * настройки WebDriver'а. Управляет настройками браузера, логированием, headless-режимом и
 * WebDriver'ом.
 */
@Slf4j
public abstract class BaseUiTest extends BaseTest {

  protected WebDriver driver;

  @BeforeMethod(alwaysRun = true)
  public void setUp() {
    String browser = EnvData.BROWSER != null ? EnvData.BROWSER.toLowerCase() : "chrome";
    boolean isHeadless = EnvData.IS_HEADLESS;

    log.info("=== UI Test Initialization ===");
    log.info("Environment: {}", EnvData.ENVIRONMENT);
    log.info("Browser: {}", browser);
    log.info("Headless: {}", isHeadless);

    // Настройки Selenide
    Configuration.browser = browser;
    Configuration.headless = isHeadless;
    Configuration.timeout = 10000;
    Configuration.pageLoadTimeout = 30000;
    Configuration.browserSize = isHeadless ? "1920x1080" : "maximize";
    Configuration.reportsFolder = "target/screenshots";
    Configuration.screenshots = true;
    Configuration.savePageSource = false;

    // WebDriver будет создан лениво при первом open()
    driver = WebDriverRunner.getAndCheckWebDriver();
  }

  @AfterMethod(alwaysRun = true)
  public void tearDown() {
    try {
      closeWebDriver();
      log.info("Driver closed successfully.");
    } catch (Exception e) {
      log.warn("Error during WebDriver cleanup: {}", e.getMessage());
    } finally {
      log.info("=== UI Test Finished ===");
    }
  }

  protected WebDriver getDriver() {
    return WebDriverRunner.getWebDriver();
  }
}
