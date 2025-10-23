package tests.paylink.xml;

import static com.ecom.api.type.Attributes.ALLOW_AIRLINE_ADDENDUM_DATA;
import static com.ecom.api.type.Attributes.ALLOW_REF3;
import static com.ecom.core.config.CardConfig.cardMC05;
import static com.ecom.core.config.EnvData.ID_AVAL;
import static com.ecom.core.config.EnvData.MERCHANT_ID_AVAL;
import static com.ecom.core.config.EnvData.TERMINAL_ID_AVAL;
import static com.ecom.core.config.EnvData.URL;
import static com.ecom.db.JDBCMethods.getAddendumData;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.db.JDBCMethods.setMerchantAtt;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.paylinkCloseDayForApi;
import static com.ecom.tests.support.PaylinkRequests.paymentAddendum;
import static com.ecom.tests.support.PaylinkRequests.reversal;
import static com.ecom.tests.support.RequestSenderRest.sendRequest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

public class PaymentFull3DSAddendumRef3_Refund {
  protected static Document requestDoc;
  protected static Document responseDoc;
  private static int tranId;

  @BeforeClass
  public void setStatusAttr() {
    setMerchantAtt(ID_AVAL, ALLOW_REF3, "true");
    setMerchantAtt(ID_AVAL, ALLOW_AIRLINE_ADDENDUM_DATA, "true");
    System.out.println("SET NEW STATUS - ALLOW_REF3 AND ALLOW_AIRLINE_ADDENDUM_DATA: TRUE");
  }

  @Test
  public void mcPaymentFull3DSAddendum() {
    requestDoc =
        paymentAddendum(
            cardMC05,
            "MCPaymentFull3DS_AddendumDataRef3+Refund",
            MERCHANT_ID_AVAL,
            TERMINAL_ID_AVAL,
            "VISA");
    responseDoc = sendRequest(URL, requestDoc);

    System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
    System.out.println("Response:\n" + printResponse(responseDoc));

    tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));

    //        from Response
    assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000", "TranCode");
    assertEquals(getElementFromDocument(responseDoc, "CVResult"), "P2", "CVResult");
    assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
    assertEquals(getElementFromDocument(responseDoc, "Rrn").length(), 12, "Rrn");
    assertEquals(getElementFromDocument(responseDoc, "ApprovalCode").length(), 6, "ApprovalCode");
    //        from DB
    assertEquals(getValueFromTRAN(tranId, "ECI"), "02", "ECI");
    assertEquals(getValueFromTRAN(tranId, "PAResStatus"), "Y", "PAResStatus");
    assertEquals(getValueFromTRAN(tranId, "PA_ECI"), "02", "PA_ECI");
    assertEquals(getValueFromTRAN(tranId, "AddendumData"), "1", "AddendumData");
    assertTrue(getAddendumData(tranId, "Ref3"));
    assertTrue(getAddendumData(tranId, "AirlineAddendumData"));
    //        assertEquals(getValueFromTRAN(tranId, "FEE"), "400", "FEE");

    System.out.println("Verified:");
    String[] verifiedResponse = {"TranCode", "CVResult", "HostCode", "Rrn", "ApprovalCode"};
    String[] verifiedResponseFromDB = {"ECI", "PAResStatus", "PA_ECI", "AddendumData"};
    try {
      verifiedDataFromResponse(responseDoc, verifiedResponse);
      verifiedDataFromDB(tranId, verifiedResponseFromDB);
    } catch (Exception e) {
      System.out.println("TEST FAILED");
    }
  }

  @Test(dependsOnMethods = "mcPaymentFull3DSAddendum")
  public void generateBatch() {
    paylinkCloseDayForApi(ID_AVAL);
    assertFalse(getValueFromTRAN(tranId, "Batch").isEmpty(), "BATCH");
  }

  @Test(dependsOnMethods = "generateBatch")
  public void mcPaymentFull3DSAddendumRefund() {
    Document requestRevDoc = reversal(requestDoc, responseDoc, 0);
    Document responseRevDoc = sendRequest(URL, requestRevDoc);

    System.out.println("--REFUND--\nRequest:\n" + printRequest(requestRevDoc));
    System.out.println("Response:\n" + printResponse(responseRevDoc));

    int tranRefundId = getTranIdByOrder(getElementFromDocument(requestRevDoc, "OrderID"));

    assertEquals(getElementFromDocument(responseRevDoc, "TranCode"), "000", "TranCode");
    assertEquals(getElementFromDocument(responseRevDoc, "Rrn").length(), 12, "Rrn");

    assertEquals(getValueFromTRAN(tranRefundId, "RevFlag"), "0", "RevFlag");
    assertEquals(getValueFromTRAN(tranRefundId, "ECI"), getValueFromTRAN(tranId, "ECI"), "ECI");
    assertEquals(getValueFromTRAN(tranRefundId, "PAResStatus"), "Y", "PAResStatus");
    assertEquals(getValueFromTRAN(tranRefundId, "PA_ECI"), "02", "PA_ECI");
    assertEquals(getValueFromTRAN(tranRefundId, "CVResult"), "P2", "CVResult");

    System.out.println("Verified:");
    String[] verifiedResponse = {"TranCode", "Rrn"};
    String[] verifiedResponseFromDB = {"ECI", "RevFlag", "CVResult", "PAResStatus", "PA_ECI"};
    try {
      verifiedDataFromResponse(responseRevDoc, verifiedResponse);
      verifiedDataFromDB(tranRefundId, verifiedResponseFromDB);
      verifiedDataFromDB(tranId, new String[] {"Batch"});
    } catch (Exception e) {
      System.out.println("TEST FAILED");
    }
  }

  @AfterClass
  public void setDefaultAttr() {
    setMerchantAtt(ID_AVAL, ALLOW_REF3, "false");
    setMerchantAtt(ID_AVAL, ALLOW_AIRLINE_ADDENDUM_DATA, "false");
    System.out.println("SET NEW STATUS - ALLOW_REF3 AND ALLOW_AIRLINE_ADDENDUM_DATA: FALSE");
  }
}
