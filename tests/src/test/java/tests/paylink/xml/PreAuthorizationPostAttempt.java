package tests.paylink.xml;

import static com.ecom.core.config.CardConfig.cardMCa;
import static com.ecom.core.config.EnvData.MERCHANT_ID_AVAL;
import static com.ecom.core.config.EnvData.TERMINAL_ID_AVAL;
import static com.ecom.core.config.EnvData.URL;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.paymentPostAuth;
import static com.ecom.tests.support.PaylinkRequests.paymentPreAuthPares;
import static com.ecom.tests.support.RequestSenderRest.sendRequest;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;
import org.w3c.dom.Document;

public class PreAuthorizationPostAttempt {
  private static Document requestDoc;
  private static Document responseDoc;
  private static int tranId;

  @Test
  public void PreAuthorizationPayment() {
    requestDoc = paymentPreAuthPares(cardMCa, "pre auth a", MERCHANT_ID_AVAL, TERMINAL_ID_AVAL);
    responseDoc = sendRequest(URL, requestDoc);

    System.out.println("--Pre Authorization--\nRequest:\n" + printRequest(requestDoc));
    System.out.println("Response:\n" + printResponse(responseDoc));

    tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));

    //        from Response
    assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000", "TranCode");
    assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M", "CVResult");
    assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
    assertEquals(getElementFromDocument(responseDoc, "Rrn").length(), 12, "Rrn");
    assertEquals(getElementFromDocument(responseDoc, "ApprovalCode").length(), 6, "ApprovalCode");
    //        from DB
    assertEquals(getValueFromTRAN(tranId, "ECI"), "01", "ECI");
    assertEquals(getValueFromTRAN(tranId, "PAResStatus"), "A", "PAResStatus");
    assertEquals(getValueFromTRAN(tranId, "PA_ECI"), "01", "PA_ECI");
    //        assertEquals(getValueFromTRAN(tranId, "FEE"), "400", "FEE");

    System.out.println("Verified:");
    String[] verifiedResponse = {"TranCode", "CVResult", "HostCode", "Rrn", "ApprovalCode"};
    String[] verifiedResponseFromDB = {"ECI", "PAResStatus", "PA_ECI"};
    try {
      verifiedDataFromResponse(responseDoc, verifiedResponse);
      verifiedDataFromDB(tranId, verifiedResponseFromDB);
    } catch (Exception e) {
      System.out.println("TEST FAILED");
    }
  }

  @Test(dependsOnMethods = "PreAuthorizationPayment")
  public void PreAuthorizationPost() {
    Document requestPostAuth = paymentPostAuth(requestDoc, responseDoc, "postAuthorization", 0);
    Document responsePostAuth = sendRequest(URL, requestPostAuth);

    System.out.println("--Post Authorization--\nRequest:\n" + printRequest(requestPostAuth));
    System.out.println("Response:\n" + printResponse(responsePostAuth));

    int tranPostId = getTranIdByOrder(getElementFromDocument(requestPostAuth, "OrderID"));

    assertEquals(getElementFromDocument(responsePostAuth, "TranCode"), "000", "TranCode");
    assertEquals(getValueFromTRAN(tranPostId, "ECI"), getValueFromTRAN(tranId, "ECI"), "ECI");

    System.out.println("Verified:");
    String[] verifiedResponse = {"TranCode"};
    String[] verifiedResponseFromDB = {"ECI"};
    try {
      verifiedDataFromResponse(responsePostAuth, verifiedResponse);
      verifiedDataFromDB(tranPostId, verifiedResponseFromDB);
    } catch (Exception e) {
      System.out.println("TEST FAILED");
    }
  }
}
