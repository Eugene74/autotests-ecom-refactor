package tests.paylink.redirect.test.aval;

import static com.ecom.api.type.Attributes.ALLOW_PAYMENT_WITHOUT_3DS;
import static com.ecom.api.type.Attributes.MERCHANT_INVOICING_URL;
import static com.ecom.core.config.CardConfig.cardVISA;
import static com.ecom.core.config.EnvData.ID_AVAL;
import static com.ecom.core.config.EnvData.URL_INVOCING;
import static com.ecom.core.config.EnvData.URL_MERCH;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.db.JDBCMethods.setMerchantAtt;
import static com.ecom.tests.support.DocumentTools.verifiedDataFromDB;

import com.ecom.db.JDBCMethods;
import com.ecom.tests.base.BaseUiTest;
import com.ecom.tests.steps.InvoiceSteps;
import com.ecom.tests.steps.PaymentSteps;
import com.ecom.tests.steps.RefundSteps;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class InvoicingRevers extends BaseUiTest {

  private static String orderRedirect;
  private static int tranId;

  @BeforeClass
  public void setParam() {
    JDBCMethods.setMerchantAtt(ID_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "true");
    setMerchantAtt(ID_AVAL, MERCHANT_INVOICING_URL, URL_INVOCING);
    System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
  }

  @Test
  public void invoicePay() {
    orderRedirect = new InvoiceSteps(driver).payInvoice(URL_MERCH, cardVISA, ID_AVAL);
    new PaymentSteps(driver).usedCVC(cardVISA);
    System.out.println("Order: " + orderRedirect);

    tranId = getTranIdByOrder(orderRedirect);

    // Видалення префікса "Order №" з orderRedirect
    orderRedirect = orderRedirect.replace("Order № ", "").trim();
    System.out.println("Order without prefix: " + orderRedirect);

    tranId = getTranIdByOrder(orderRedirect);
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
    }
  }

  @Test(dependsOnMethods = "invoicePay")
  public void invoiceRevers() {
    new RefundSteps(driver).doRefund(URL_MERCH, orderRedirect, tranId);
    int tranReversId = getTranIdByOrder(orderRedirect);
    // Видалення префікса "Order №" з orderRedirect (додаткове видалення на випадок, якщо префікс
    // з'явиться знову)
    orderRedirect = orderRedirect.replace("Order № ", "").trim();
    System.out.println("Order without prefix: " + orderRedirect);
    int tranRefundId = getTranIdByOrder(orderRedirect);
    System.out.println("TranRefundId: " + tranRefundId);
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
    }
  }

  @AfterClass
  public void setDefaultParam() {
    JDBCMethods.setMerchantAtt(ID_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "false");
    System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: FALSE");
  }
}
