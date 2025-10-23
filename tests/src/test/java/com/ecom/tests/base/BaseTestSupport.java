package com.ecom.tests.base;

/**
 * Central base implementation that provides configuration and driver lifecycle helpers.
 */
@Deprecated
public abstract class BaseTestSupport {

 /* public static final List<String> ECOM_TRAN_PAYMENT_LIST = new ArrayList<>();
  public static final List<Map<FieldsNFile, String>> ECOM_TRAN_PAYMENT_FIELDS = new ArrayList<>();
  public static final List<Map<FieldsNFile, String>> ECOM_TRAN_REVERS_FIELDS = new ArrayList<>();
  public static final List<ECommTransaction> ECOMM_TRANSACTION_LIST = new ArrayList<>();*/

  //protected WebDriver driver;

 /* protected void setup() {
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
*/
  /*protected void exit() {
    if (driver != null) {
      driver.quit();
      driver = null;
      WebDriverRunner.closeWebDriver();
    }
  }*/

 /* public WebDriver getDriver() {
    return driver == null ? WebDriverRunner.getWebDriver() : driver;
  }*/

}
