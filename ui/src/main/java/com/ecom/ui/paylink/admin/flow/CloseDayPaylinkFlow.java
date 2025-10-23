package com.ecom.ui.paylink.admin.flow;

import com.ecom.core.config.PropertiesManager;
import com.ecom.ui.driver.OptionsManager;
import com.ecom.ui.driver.SelenideDriverManager;
import com.ecom.ui.paylink.admin.pages.BatchPaylink;
import com.ecom.ui.util.Waiters;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Imperative flow that signs into the Paylink extranet and closes the settlement day for a
 * merchant.
 */
public class CloseDayPaylinkFlow {

  private final String extranetUrl;
  private final boolean headless;

  public CloseDayPaylinkFlow() {
    PropertiesManager properties = PropertiesManager.getInstance();
    this.extranetUrl = properties.getEnv("URLtomee") + "/paylink/extranet/index.jsp";
    this.headless = properties.isHeadless();
  }

  public void closeDay(WebDriver driver, String merchantId) {
    driver.get(extranetUrl);
    BatchPaylink batchPage = new BatchPaylink(driver);
    batchPage.login(merchantId);
    batchPage.closed(merchantId);
  }

  public void closeDayWithManagedDriver(String merchantId) {
    SelenideDriverManager.ensureChromeDriverBinary();
    SelenideDriverManager.configure();
    ChromeDriver chromeDriver = new ChromeDriver(OptionsManager.chrome(headless));
    try {
      if (headless) {
        chromeDriver.manage().window().setSize(new Dimension(3840, 2160));
      } else {
        chromeDriver.manage().window().maximize();
      }
      chromeDriver.manage().timeouts().implicitlyWait(Waiters.DEFAULT_TIMEOUT);
      closeDay(chromeDriver, merchantId);
    } finally {
      chromeDriver.quit();
    }
  }
}
