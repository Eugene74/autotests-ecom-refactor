/**
 * @author semyvolos_h
 * @date 1/15/2024 3:16 PM
 */
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
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class Capture extends BaseUiTest {

  private static String orderRedirect;
  private static int tranId;

  @BeforeClass
  public static void setParam() {
    setMerchantAtt(ID_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "true");
    setMerchantAtt(ID_AVAL, MERCHANT_INVOICING_URL, URL_INVOCING);
    System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
  }

  @Test
  public void preAuthorization() {
    orderRedirect =
        new PaymentSteps(driver)
            .authorizePayment(cardMC, 1, MERCHANT_ID_AVAL, TERMINAL_ID_AVAL, URL_REDIRECT);
    new PaymentSteps(driver).usedCVC(cardMC);

    System.out.println("Order: " + orderRedirect);

    tranId = getTranIdByOrder(orderRedirect);

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
    }
  }

  @Test(dependsOnMethods = "preAuthorization")
  public void Capture(String tranId) {}
}
