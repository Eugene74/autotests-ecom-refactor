package com.ecom.tests.steps;

import com.ecom.ui.paylink.redirect.pages.LoginAdminPage;
import com.ecom.ui.paylink.redirect.pages.MainMerchantPage;
import com.ecom.ui.paylink.redirect.pages.TransactionPage;
import org.openqa.selenium.WebDriver;

public class RefundSteps {
  private final WebDriver driver;

  public RefundSteps(WebDriver driver) {
    this.driver = driver;
  }

  public void postAuthorization(String urlMerch, int tranId, String order) {
    MainMerchantPage merchantPage = new MainMerchantPage(driver);
    merchantPage.loginInGo(urlMerch);
    merchantPage.getTransactionsMenu();

    TransactionPage tranPage = new TransactionPage(driver);
    tranPage.findTranByOrder(order);
    tranPage.selectTran(tranId);
    tranPage.postAuthorization();
  }

  public void postAuthorizationWithDiffAmount(
      String urlMerch, String order, int tranId, double delta) {
    MainMerchantPage merchantPage = new MainMerchantPage(driver);
    merchantPage.loginInGo(urlMerch);
    merchantPage.getTransactionsMenu();

    TransactionPage tranPage = new TransactionPage(driver);
    tranPage.findTranByOrder(order);
    tranPage.selectTran(tranId);
    tranPage.postAuthorizationOtherAmount(delta);
  }

  public void doRefund(String urlAdmin, String order, int tranId) {
    LoginAdminPage loginPage = new LoginAdminPage(driver);
    loginPage.loginIn(urlAdmin);

    TransactionPage tranPage = new TransactionPage(driver);
    tranPage.getTransactionsMenu();
    tranPage.findTranByOrder(order);
    tranPage.selectTran(tranId);
    tranPage.toDoRefund();
  }
}
