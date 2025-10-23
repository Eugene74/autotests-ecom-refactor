package com.ecom.tests.base;

import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

@Listeners({com.ecom.tests.support.FinalResultListener.class})
public abstract class BaseTest {

  @BeforeSuite(alwaysRun = true)
  public void loadEnv() {
    System.out.println("Environment initialized: " + System.getProperty("env", "default"));
  }
}
