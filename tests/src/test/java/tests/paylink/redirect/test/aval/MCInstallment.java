package tests.paylink.redirect.test.aval;

import static com.ecom.api.type.Attributes.*;
import static com.ecom.core.config.CardConfig.cardMCInst;
import static com.ecom.core.config.EnvData.*;
import static com.ecom.db.JDBCMethods.getTranIdByOrder1;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.db.JDBCMethods.setMerchantAtt;
import static com.ecom.tests.support.DocumentTools.verifiedDataFromDB;

import com.ecom.tests.base.BaseUiTest;
import com.ecom.tests.steps.PaymentSteps;
import com.ecom.ui.util.Waiters;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class MCInstallment extends BaseUiTest {
  @BeforeClass
  public void setParam() {
    setMerchantAtt(ID_AVAL_1, ALLOW_PAYMENT_WITHOUT_3DS, "true");
    System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
    setMerchantAtt(ID_AVAL_1, ALLOW_MASTERCARD_INSTALLMENT, "true");
    System.out.println("ALLOW_MASTERCARD_INSTALLMENT: TRUE");
    setMerchantAtt(ID_AVAL_1, USE_NEW_FRONT_END, "true");
    System.out.println("USE_NEW_FRONT_END: TRUE");
    setMerchantAtt(ID_AVAL_1, ALLOW_INSTALLMENT, "true");
    System.out.println("ALLOW_INSTALLMENT: TRUE");
    setMerchantAtt(ID_AVAL_1, DISABLE_EMAIL_FIELD, "false");
    setMerchantAtt(ID_AVAL, MERCHANT_INVOICING_URL, URL_INVOCING);
    System.out.println("DISABLE_EMAIL_FIELD: FALSE");
  }

  @Test
  public void installmentPay() {
    String orderRedirect =
        new PaymentSteps(driver)
            .authorizePaymentInstallment(
                cardMCInst, 0, MERCHANT_ID_AVAL_1, TERMINAL_ID_AVAL_1, URL_REDIRECT);
    new PaymentSteps(driver).usedCVC(cardMCInst);
    System.out.println("Order Redirect: " + orderRedirect);

    Waiters.sleep(3500); // использовать только на тест среде, задержка для БД
    int tranId = getTranIdByOrder1(orderRedirect);
    System.out.println("Transaction ID: " + tranId);

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

  @AfterClass
  public void setDefaultParam() {
    setMerchantAtt(ID_AVAL_1, ALLOW_PAYMENT_WITHOUT_3DS, "false");
    System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: FALSE");
    setMerchantAtt(ID_AVAL_1, ALLOW_MASTERCARD_INSTALLMENT, "false");
    System.out.println("ALLOW_MASTERCARD_INSTALLMENT: FALSE");
    setMerchantAtt(ID_AVAL_1, USE_NEW_FRONT_END, "false");
    System.out.println("USE_NEW_FRONT_END: FALSE");
    setMerchantAtt(ID_AVAL_1, ALLOW_INSTALLMENT, "false");
    System.out.println("ALLOW_INSTALLMENT: FALSE");
    setMerchantAtt(ID_AVAL_1, DISABLE_EMAIL_FIELD, "true");
    System.out.println("DISABLE_EMAIL_FIELD: TRUE");
  }
}
