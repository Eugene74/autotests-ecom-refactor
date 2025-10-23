package tests.paylink.redirect.test.aval;

import static com.ecom.api.type.Attributes.ALLOW_PAYMENT_WITHOUT_3DS;
import static com.ecom.api.type.Attributes.MERCHANT_INVOICING_URL;
import static com.ecom.core.config.CardConfig.cardVisaRed;
import static com.ecom.core.config.EnvData.*;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.db.JDBCMethods.setMerchantAtt;
import static com.ecom.tests.support.DocumentTools.verifiedDataFromDB;
import static com.ecom.tests.support.PaylinkRequests.paylinkCloseDay;
import static org.testng.Assert.assertFalse;

import com.ecom.db.JDBCMethods;
import com.ecom.tests.base.BaseUiTest;
import com.ecom.tests.steps.PaymentSteps;
import com.ecom.tests.steps.RefundSteps;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class FacilitatorAuthRefund extends BaseUiTest {
  private static String orderRedirect;
  private static int tranId;

  @BeforeClass
  public void setParam() {
    setMerchantAtt(ID_FACIL_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "true");
    setMerchantAtt(ID_AVAL, MERCHANT_INVOICING_URL, URL_INVOCING);
    System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
  }

  @Test
  public void authorizationPay() {
    orderRedirect =
        new PaymentSteps(driver)
            .authorizePayment(
                cardVisaRed, 0, MERCHANT_ID_FACIL_AVAL, TERMINAL_ID_FACIL_AVAL, URL_REDIRECT);

    new PaymentSteps(driver).usedCVC(cardVisaRed);
    System.out.println("Order: " + orderRedirect);

    // Удаление префикса "Order №" из orderRedirect
    orderRedirect = orderRedirect.replace("Order № ", "").trim();
    System.out.println("Order without prefix: " + orderRedirect);

    // Логика повторных попыток для получения tranId
    tranId = getTranIdByOrderWithRetry(orderRedirect, 5, 2000);
    if (tranId == 0) {
      System.err.println("Failed to retrieve TranId after retries. Test aborted.");
      return;
    }
    System.out.println("TranId: " + tranId);

    SoftAssert softAssertion = new SoftAssert();
    softAssertion.assertEquals(getValueFromTRAN(tranId, "ECI"), "07", "ECI");
    softAssertion.assertEquals(
        getValueFromTRAN(tranId, "ApprovalCode").length(), 6, "ApprovalCode");
    softAssertion.assertEquals(getValueFromTRAN(tranId, "Rrn").length(), 12, "Rrn");
    softAssertion.assertEquals(getValueFromTRAN(tranId, "TranCode"), "000", "TranCode");
    softAssertion.assertEquals(getValueFromTRAN(tranId, "CVResult"), "M", "CVResult");
    softAssertion.assertAll();

    System.out.println("Verified:");
    String[] verifiedResponseFromDB = {"ECI", "ApprovalCode", "Rrn", "TranCode", "CVResult"};
    try {
      verifiedDataFromDB(tranId, verifiedResponseFromDB);
    } catch (Exception e) {
      System.out.println("TEST FAILED");
      e.printStackTrace();
    }
  }

  @Test(dependsOnMethods = "authorizationPay")
  public void generateBatch() {
    JDBCMethods.setNewTranTime(tranId);
    System.out.println("--TRAN TIME was Updated--\n");
    paylinkCloseDay(ID_FACIL_AVAL);
    assertFalse(getValueFromTRAN(tranId, "Batch").isEmpty(), "BATCH");
  }

  @Test(dependsOnMethods = "generateBatch")
  public void authorizationRefund() {
    new RefundSteps(driver).doRefund(URL_MERCH, orderRedirect, tranId);

    // Удаление префикса "Order №" из orderRedirect
    orderRedirect = orderRedirect.replace("Order № ", "").trim();
    System.out.println("Order without prefix: " + orderRedirect);

    // Логика повторных попыток для получения tranRefundId
    int tranRefundId = getTranIdByOrderWithRetry(orderRedirect, 5, 2000);
    if (tranRefundId == 0) {
      System.err.println("Failed to retrieve TranRefundId after retries. Test aborted.");
      return;
    }
    System.out.println("TranRefundId: " + tranRefundId);

    SoftAssert softAssertion = new SoftAssert();
    softAssertion.assertFalse(tranRefundId == tranId);
    softAssertion.assertEquals(
        getValueFromTRAN(tranRefundId, "ECI"), getValueFromTRAN(tranId, "ECI"), "ECI");
    softAssertion.assertNull(getValueFromTRAN(tranRefundId, "ApprovalCode"), "ApprovalCode");
    softAssertion.assertEquals(getValueFromTRAN(tranRefundId, "RevFlag"), "0", "RevFlag");
    softAssertion.assertEquals(getValueFromTRAN(tranRefundId, "Rrn").length(), 12, "Rrn");
    softAssertion.assertEquals(getValueFromTRAN(tranRefundId, "TranCode"), "000", "TranCode");
    softAssertion.assertEquals(getValueFromTRAN(tranRefundId, "CVResult"), "M", "CVResult");
    softAssertion.assertFalse(getValueFromTRAN(tranId, "Batch").isEmpty(), "BATCH");
    softAssertion.assertAll();

    System.out.println("Verified:");
    String[] verifiedResponseFromDB = {
      "ECI", "ApprovalCode", "Rrn", "TranCode", "RevFlag", "CVResult"
    };
    try {
      verifiedDataFromDB(tranRefundId, verifiedResponseFromDB);
      verifiedDataFromDB(tranId, new String[] {"Batch"});
    } catch (Exception e) {
      System.out.println("TEST FAILED");
      e.printStackTrace();
    }
  }

  @AfterClass
  public void setDefaultParam() {
    setMerchantAtt(ID_FACIL_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "false");
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
          return tranId; // Успешно получили TranId
        }
      } catch (Exception e) {
        System.err.println("Error fetching TranId: " + e.getMessage());
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

    System.err.println("Failed to fetch TranId after " + maxRetries + " attempts");
    return tranId; // Возвращаем 0, если не удалось получить TranId
  }
}
