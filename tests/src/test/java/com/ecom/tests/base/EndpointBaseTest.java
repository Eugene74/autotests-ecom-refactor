package com.ecom.tests.base;

import static com.ecom.core.config.EnvData.ID_AVAL_1;
import static com.ecom.core.config.EnvData.URL_TOMEE;

import java.io.File;
import org.testng.annotations.BeforeClass;

@Deprecated
public class EndpointBaseTest extends BaseUiTest { // todo needs to be refactored

  protected static Integer merchantId = ID_AVAL_1;
  protected static String downloadFilepath =
      System.getProperty("user.dir") + File.separator + "target";

  @BeforeClass(alwaysRun = true)
  public void openMerchantPage() {
    driver.get(URL_TOMEE);
  }
}
