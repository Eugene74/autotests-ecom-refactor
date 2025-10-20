package com.ecom.tests.base;

import com.codeborne.selenide.WebDriverRunner;
import com.ecom.api.model.ECommTransaction;
import com.ecom.api.type.FieldsNFile;
import com.ecom.ui.driver.OptionsManager;
import com.ecom.ui.driver.SelenideDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.ecom.core.config.EnvData.isHeadless;

/**
 * Central base implementation that provides configuration and driver lifecycle helpers.
 */
public abstract class BaseTestSupport {

  public static final List<String> tranPayment = new ArrayList<>();
  public static final List<Map<FieldsNFile, String>> tranPaymentFields = new ArrayList<>();
  public static final List<Map<FieldsNFile, String>> tranReversFields = new ArrayList<>();
  public static final List<ECommTransaction> transactions = new ArrayList<>();

  protected WebDriver driver;

  protected void setup() {
    SelenideDriverManager.ensureChromeDriverBinary();
    SelenideDriverManager.configure();
    ChromeDriver chromeDriver = new ChromeDriver(OptionsManager.chrome(isHeadless));
    if (isHeadless) {
      chromeDriver.manage().window().setSize(new Dimension(3840, 2160));
    } else {
      chromeDriver.manage().window().maximize();
    }
    chromeDriver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
    WebDriverRunner.setWebDriver(chromeDriver);
    driver = chromeDriver;
  }

  protected void exit() {
    if (driver != null) {
      driver.quit();
      driver = null;
      WebDriverRunner.closeWebDriver();
    }
  }

  public WebDriver getDriver() {
    return driver == null ? WebDriverRunner.getWebDriver() : driver;
  }

}
