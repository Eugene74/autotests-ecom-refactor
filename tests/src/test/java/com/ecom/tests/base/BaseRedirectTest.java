package com.ecom.tests.base;

import com.ecom.ui.driver.OptionsManager;
import com.ecom.ui.driver.SelenideDriverManager;
import com.ecom.ui.driver.WebDriverHolder;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

/**
 * Base class for redirect-specific UI tests.
 */
public abstract class BaseRedirectTest extends BaseTestSupport {

  @BeforeMethod
  public void setUpRedirectDriver() {
    SelenideDriverManager.ensureChromeDriverBinary();
    SelenideDriverManager.configure();
    ChromeDriver chromeDriver = new ChromeDriver(OptionsManager.chrome(isHeadless));
    if (isHeadless) {
      chromeDriver.manage().window().setSize(new Dimension(3840, 2160));
    } else {
      chromeDriver.manage().window().maximize();
    }
    WebDriverHolder.setDriver(chromeDriver);
    driver = chromeDriver;
  }

  @AfterMethod
  public void tearDownRedirectDriver() {
    if (driver != null) {
      driver.quit();
    }
    WebDriverHolder.removeDriver();
  }
}
