package tests.paylink.redirect.test.aval;

import static com.ecom.api.type.Attributes.ALLOW_PAYMENT_WITHOUT_3DS;
import static com.ecom.api.type.Attributes.MERCHANT_INVOICING_URL;
import static com.ecom.core.config.CardConfig.cardVISA;
import static com.ecom.core.config.EnvData.*;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.db.JDBCMethods.setMerchantAtt;
import static com.ecom.tests.support.DocumentTools.verifiedDataFromDB;

import com.ecom.tests.base.BaseUiTest;
import com.ecom.tests.steps.PaymentSteps;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class Authorization extends BaseUiTest {

  @BeforeClass
  public void setParam() {
    setMerchantAtt(ID_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "true");
    setMerchantAtt(ID_AVAL, MERCHANT_INVOICING_URL, URL_INVOCING);
    System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
  }

  @Test
  public void authorizationPay() {
    String orderRedirect =
        new PaymentSteps(driver)
            .authorizePayment(cardVISA, 0, MERCHANT_ID_AVAL, TERMINAL_ID_AVAL, URL_REDIRECT);
    new PaymentSteps(driver).usedCVC(cardVISA);
    System.out.println("Order: " + orderRedirect);

    // Логика повторных попыток для получения TranID
    int tranID = getTranIdByOrderWithRetry(orderRedirect, 5, 2000);
    if (tranID == 0) {
      System.err.println("Failed to retrieve TranID after retries. Test aborted.");
      return;
    }
    System.out.println("TranID: " + tranID);

    SoftAssert softAssertion = new SoftAssert();
    softAssertion.assertEquals(getValueFromTRAN(tranID, "ECI"), "07", "ECI");
    softAssertion.assertEquals(
        getValueFromTRAN(tranID, "ApprovalCode").length(), 6, "ApprovalCode");
    softAssertion.assertEquals(getValueFromTRAN(tranID, "Rrn").length(), 12, "Rrn");
    softAssertion.assertEquals(getValueFromTRAN(tranID, "TranCode"), "000", "TranCode");
    softAssertion.assertEquals(getValueFromTRAN(tranID, "CVResult"), "M", "CVResult");
    softAssertion.assertAll();

    System.out.println("Verified:");
    String[] verifiedResponseFromDB = {"ECI", "ApprovalCode", "Rrn", "TranCode", "CVResult"};
    try {
      verifiedDataFromDB(tranID, verifiedResponseFromDB);
    } catch (Exception e) {
      System.out.println("TEST FAILED");
      e.printStackTrace();
    }
  }

  @AfterClass
  public void setDefaultParam() {
    setMerchantAtt(ID_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "false");
    System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: FALSE");
  }

  // Метод с логикой повторных попыток
  private int getTranIdByOrderWithRetry(String orderId, int maxRetries, int delayMs) {
    int tranId = 0;
    int attempt = 0;

    while (attempt < maxRetries) {
      try {
        tranId = getTranIdByOrder(orderId);
        if (tranId != 0) {
          return tranId; // Успешно получили TranID
        }
      } catch (Exception e) {
        System.err.println("Error fetching TranID: " + e.getMessage());
      }

      attempt++;
      System.out.println("Retrying... Attempt " + attempt + " of " + maxRetries);
      try {
        Thread.sleep(delayMs); // Задержка перед повторной попыткой
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException("Thread interrupted during retry delay", e);
      }
    }

    System.err.println("Failed to fetch TranID after " + maxRetries + " attempts");
    return tranId; // Возвращаем 0, если не удалось получить TranID
  }
}
