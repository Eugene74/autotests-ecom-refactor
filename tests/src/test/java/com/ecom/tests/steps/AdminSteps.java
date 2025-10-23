package com.ecom.tests.steps;

import com.ecom.ui.paylink.redirect.pages.BanksPage;
import com.ecom.ui.paylink.redirect.pages.LoginAdminPage;
import org.openqa.selenium.WebDriver;

public class AdminSteps {

  private final WebDriver driver;

  public AdminSteps(WebDriver driver) {
    this.driver = driver;
  }

  public void changeAquirerID(String inputID, String url) {
    LoginAdminPage loginPage = new LoginAdminPage(driver);
    loginPage.loginIn(url);
    BanksPage banksPage = new BanksPage(driver);
    banksPage.chandeAquirerID(inputID);
  }
}
