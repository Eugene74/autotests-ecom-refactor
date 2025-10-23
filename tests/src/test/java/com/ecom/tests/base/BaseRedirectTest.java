package com.ecom.tests.base;

/**
 * Base class for redirect-specific UI tests.
 */
@Deprecated
public abstract class BaseRedirectTest extends BaseTestSupport {

  /*@BeforeMethod
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
  }*/
}
