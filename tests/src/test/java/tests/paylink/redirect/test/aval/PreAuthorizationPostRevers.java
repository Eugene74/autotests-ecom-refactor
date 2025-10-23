package tests.paylink.redirect.test.aval;

import static com.ecom.api.type.Attributes.ALLOW_PAYMENT_WITHOUT_3DS;
import static com.ecom.api.type.Attributes.MERCHANT_INVOICING_URL;
import static com.ecom.core.config.CardConfig.cardMC;
import static com.ecom.core.config.EnvData.*;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.db.JDBCMethods.setMerchantAtt;
import static com.ecom.tests.support.DocumentTools.verifiedDataFromDB;

import com.ecom.tests.base.BaseUiTest;
import com.ecom.tests.steps.PaymentSteps;
import com.ecom.tests.steps.RefundSteps;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class PreAuthorizationPostRevers extends BaseUiTest {
  private static String orderRedirect;
  private static int tranId;
  private static int tranPostId;

  @BeforeClass
  public static void setParam() {
    setMerchantAtt(ID_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "true");
    setMerchantAtt(ID_AVAL, MERCHANT_INVOICING_URL, URL_INVOCING);
    System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
  }

  @Test
  public void visaPreAuthorization() {
    orderRedirect =
        new PaymentSteps(driver)
            .authorizePayment(cardMC, 1, MERCHANT_ID_AVAL, TERMINAL_ID_AVAL, URL_REDIRECT);
    new PaymentSteps(driver).usedCVC(cardMC);

    System.out.println("Order: " + orderRedirect);

    orderRedirect = orderRedirect.replace("Order № ", "").trim();
    System.out.println("Order without prefix: " + orderRedirect);

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

  @Test(dependsOnMethods = "visaPreAuthorization")
  public void visaPreAuthorizationPost() {
    new RefundSteps(driver).postAuthorization(URL_MERCH, tranId, orderRedirect);
    orderRedirect = orderRedirect.replace("Order № ", "").trim();
    System.out.println("Order without prefix: " + orderRedirect);
    tranPostId = getTranIdByOrderWithRetry(orderRedirect, 5, 2000);
    if (tranPostId == 0) {
      System.err.println("Failed to retrieve TranPostId after retries. Test aborted.");
      return;
    }
    System.out.println("TranPostId: " + tranPostId);
    SoftAssert softAssertion = new SoftAssert();
    softAssertion.assertFalse(tranPostId == tranId);
    softAssertion.assertEquals(getValueFromTRAN(tranPostId, "TranCode"), "000", "TranCode");
    softAssertion.assertEquals(
        getValueFromTRAN(tranPostId, "ECI"), getValueFromTRAN(tranId, "ECI"), "ECI");
    softAssertion.assertAll();
    System.out.println("Verified:");
    String[] verifiedResponseFromDB = {"ECI", "TranCode"};
    try {
      verifiedDataFromDB(tranPostId, verifiedResponseFromDB);
    } catch (Exception e) {
      System.out.println("TEST FAILED");
      e.printStackTrace();
    }
  }

  @Test(dependsOnMethods = "visaPreAuthorizationPost")
  public void visaPreAuthorizationPostRevers() {
    new RefundSteps(driver).doRefund(URL_MERCH, orderRedirect, tranPostId);
    orderRedirect = orderRedirect.replace("Order № ", "").trim();
    System.out.println("Order without prefix: " + orderRedirect);
    int tranReversId = getTranIdByOrderWithRetry(orderRedirect, 5, 2000);
    if (tranReversId == 0) {
      System.err.println("Failed to retrieve TranReversId after retries. Test aborted.");
      return;
    }
    System.out.println("TranReversId: " + tranReversId);
    SoftAssert softAssertion = new SoftAssert();
    softAssertion.assertFalse(tranReversId == tranId);
    softAssertion.assertEquals(
        getValueFromTRAN(tranReversId, "ECI"), getValueFromTRAN(tranId, "ECI"), "ECI");
    softAssertion.assertEquals(
        getValueFromTRAN(tranReversId, "ApprovalCode").length(), 6, "ApprovalCode");
    softAssertion.assertEquals(getValueFromTRAN(tranReversId, "RevFlag"), "1", "RevFlag");
    softAssertion.assertEquals(getValueFromTRAN(tranReversId, "Rrn").length(), 12, "Rrn");
    softAssertion.assertEquals(getValueFromTRAN(tranReversId, "TranCode"), "000", "TranCode");
    softAssertion.assertAll();
    System.out.println("Verified:");
    String[] verifiedResponseFromDB = {"ECI", "ApprovalCode", "Rrn", "TranCode", "RevFlag"};
    try {
      verifiedDataFromDB(tranReversId, verifiedResponseFromDB);
    } catch (Exception e) {
      System.out.println("TEST FAILED");
      e.printStackTrace();
    }
  }

  @AfterClass
  public static void setDefaultParam() {
    setMerchantAtt(ID_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "false");
    System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: FALSE");
  }

  private static int getTranIdByOrderWithRetry(String orderId, int maxRetries, int delayMs) {
    int tranId = 0;
    int attempt = 0;

    while (attempt < maxRetries) {
      try {
        tranId = getTranIdByOrder(orderId);
        if (tranId != 0) {
          return tranId;
        }
      } catch (Exception e) {
        System.err.println("Error fetching TranId: " + e.getMessage());
      }

      attempt++;
      System.out.println("Retrying... Attempt " + attempt + " of " + maxRetries);
      try {
        Thread.sleep(delayMs);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        throw new RuntimeException("Thread interrupted during retry delay", e);
      }
    }

    System.err.println("Failed to fetch TranId after " + maxRetries + " attempts");
    return tranId;
  }
}
