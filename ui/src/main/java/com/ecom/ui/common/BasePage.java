package com.ecom.ui.common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

/** Lightweight Selenium page-object base used across legacy UI flows. */
public abstract class BasePage {

  protected final WebDriver driver;

  protected BasePage(WebDriver driver) {
    this.driver = driver;
    PageFactory.initElements(driver, this);
  }

  public WebDriver getDriver() {
    return driver;
  }
}
