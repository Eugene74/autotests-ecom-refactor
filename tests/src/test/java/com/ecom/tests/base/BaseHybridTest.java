package com.ecom.tests.base;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public abstract class BaseHybridTest extends BaseApiTest {

  protected BaseUiTest uiHelper;

  @BeforeClass(alwaysRun = true)
  public void hybridSetUp() {
    uiHelper = new BaseUiTest() {}; // анонимный, чтобы использовать его setUp()
    uiHelper.setUp();
  }

  @AfterClass(alwaysRun = true)
  public void hybridTearDown() {
    uiHelper.tearDown();
  }

  protected WebDriver getDriver() {
    return uiHelper.getDriver();
  }
}
