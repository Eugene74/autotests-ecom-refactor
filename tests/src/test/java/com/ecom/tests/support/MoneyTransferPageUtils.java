package com.ecom.tests.support;

import static com.ecom.core.config.EnvData.UR_LP_2_P;
import static com.ecom.db.JDBCMethods.getRequestIdMT;
import static com.ecom.ui.util.Waiters.handleAlert;

import com.ecom.core.config.P2PConfig;
import com.ecom.ui.mt.p2p.pages.ContentPage;
import com.ecom.ui.mt.p2p.pages.MoneyTransferPage;
import java.util.ArrayList;
import java.util.List;
import org.openqa.selenium.WebDriver;

public class MoneyTransferPageUtils {

  private final WebDriver driver;

  private final List<String> response = new ArrayList<>();

  public MoneyTransferPageUtils(WebDriver driver) {
    this.driver = driver;
  }

  public String domesticTransfer(
      int merchantId,
      String[] senderCard,
      String recipientCardNum,
      Class<? extends MoneyTransferPage> pageClass) {
    driver.get(UR_LP_2_P + Integer.toHexString(merchantId));
    MoneyTransferPage mainPage;
    try {
      // Динамическое создание объекта страницы
      mainPage = pageClass.getDeclaredConstructor(WebDriver.class).newInstance(driver);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create page instance: " + pageClass.getName(), e);
    }
    mainPage.enterSenderCardNum(senderCard, P2PConfig.senderName());
    mainPage.enterRecipientCardNum(recipientCardNum, P2PConfig.recipientName());
    mainPage.enterAmount(P2PConfig.amountDefault());
    mainPage.confirmTransfer();
    handleAlert(driver);
    // Передаем merchantId вместо фиксированного id_AVAL
    String generatedRequestId = getRequestIdMT(merchantId);
    return generatedRequestId;
  }

  public List<String> getContentData() {
    ContentPage contentPage = new ContentPage(driver);
    response.add(contentPage.checkSuccessResponse());
    return List.copyOf(response);
  }
}
