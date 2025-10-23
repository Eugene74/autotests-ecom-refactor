package tests.paylink.xml;

import static com.ecom.core.config.CardConfig.cardMC;
import static com.ecom.core.config.EnvData.MERCHANT_ID_AVAL;
import static com.ecom.core.config.EnvData.TERMINAL_ID_AVAL;
import static com.ecom.core.config.EnvData.URL;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromRecurrent;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.paymentRecurent;
import static com.ecom.tests.support.RequestSenderRest.sendRequest;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertFalse;

import org.testng.annotations.Test;
import org.w3c.dom.Document;

public class RecurrentSubsequent {

  @Test
  public void RecurrentSubsequent() {
    Document requestDoc =
        paymentRecurent(
            cardMC, "recurrent sub", MERCHANT_ID_AVAL, TERMINAL_ID_AVAL, "300176389678909");
    Document responseDoc = sendRequest(URL, requestDoc);

    System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
    System.out.println("Response:\n" + printResponse(responseDoc));

    int tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));

    //        from Response
    assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000", "TranCode");
    assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M", "CVResult");
    assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
    assertEquals(getElementFromDocument(responseDoc, "Rrn").length(), 12, "Rrn");
    assertEquals(getElementFromDocument(responseDoc, "ApprovalCode").length(), 6, "ApprovalCode");
    //        from DB
    assertEquals(getValueFromTRAN(tranId, "ECI"), "07", "ECI");
    assertEquals(getValueFromTRAN(tranId, "RECURRENT"), "1", "RECURRENT");
    assertFalse(getValueFromRecurrent(tranId).isEmpty());
    assertFalse(getValueFromTRAN(tranId, "PSTranID").isEmpty());

    System.out.println("Verified:");
    String[] verifiedResponse = {"TranCode", "CVResult", "HostCode", "Rrn", "ApprovalCode"};
    String[] verifiedResponseFromDB = {"ECI", "RECURRENT", "INCOMING_TID", "PSTranID"};
    try {
      verifiedDataFromResponse(responseDoc, verifiedResponse);
      verifiedDataFromDB(tranId, verifiedResponseFromDB);
    } catch (Exception e) {
      System.out.println("TEST FAILED");
    }
  }
}
