package com.ecom.tests.steps;

import static com.ecom.tests.support.HtmlMethods.getHtmlPath;

import com.ecom.tests.support.HtmlMethods;
import com.ecom.ui.paylink.redirect.pages.EnterPaymentCardPage;
import com.ecom.ui.paylink.redirect.pages.InstalmentPaymentCardPage;
import com.ecom.ui.paylink.redirect.pages.PageRequest;
import org.openqa.selenium.WebDriver;

public class PaymentSteps {
  private final WebDriver driver;

  public PaymentSteps(WebDriver driver) {
    this.driver = driver;
  }

  public String authorizePayment(
      String[] card, int delay, String merch, String term, String redirectUrl) {
    HtmlMethods.fillAuthorizationForm(String.valueOf(delay), redirectUrl, merch, term);
    driver.get(getHtmlPath("Authorization.html"));

    new PageRequest(driver).sendReq();
    return new EnterPaymentCardPage(driver).enterCardInfo(card);
  }

  public String authorizePaymentInstallment(
      String[] card, int delay, String merch, String term, String redirectUrl) {
    HtmlMethods.fillAuthorizationFormInstallment(String.valueOf(delay), redirectUrl, merch, term);
    driver.get(getHtmlPath("AuthorizationInstallment.html"));

    new PageRequest(driver).sendReq();
    return new InstalmentPaymentCardPage(driver).enterCardInfo(card);
  }

  public void usedCVC(String[] card) {
    EnterPaymentCardPage cvc = new EnterPaymentCardPage(driver);
    // cvc.enterCVC(card);
  }
}
