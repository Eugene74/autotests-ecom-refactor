package tests.paylink.xml;

import static com.ecom.core.config.CardConfig.cardMC;
import static com.ecom.core.config.EnvData.ID_AVAL;
import static com.ecom.core.config.EnvData.MERCHANT_ID_AVAL;
import static com.ecom.core.config.EnvData.TERMINAL_ID_AVAL;
import static com.ecom.core.config.EnvData.URL;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.paylinkCloseDayForApi;
import static com.ecom.tests.support.PaylinkRequests.paymentRecurent;
import static com.ecom.tests.support.PaylinkRequests.reversal;
import static com.ecom.tests.support.RequestSenderRest.sendRequest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

import com.ecom.db.JDBCMethods;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

public class RecurrentInitial_Refund {
  private Document requestDoc;
  private Document responseDoc;
  private static int tranId;

  @Test
  public void RecurrentInitial() {
    requestDoc =
        paymentRecurent(cardMC, "recurrent ref", MERCHANT_ID_AVAL, TERMINAL_ID_AVAL, "true");
    responseDoc = sendRequest(URL, requestDoc);

    System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
    System.out.println("Response:\n" + printResponse(responseDoc));

    tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));

    //        from Response
    assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000", "TranCode");
    assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M", "CVResult");
    assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
    assertEquals(getElementFromDocument(responseDoc, "Rrn").length(), 12, "Rrn");
    assertEquals(getElementFromDocument(responseDoc, "ApprovalCode").length(), 6, "ApprovalCode");
    //        from DB
    assertEquals(getValueFromTRAN(tranId, "ECI"), "07", "ECI");
    assertEquals(getValueFromTRAN(tranId, "RECURRENT"), "1", "RECURRENT");
    assertFalse(getValueFromTRAN(tranId, "PSTranID").isEmpty(), "PSTranID");

    System.out.println("Verified:");
    String[] verifiedResponse = {"TranCode", "CVResult", "HostCode", "Rrn", "ApprovalCode"};
    String[] verifiedResponseFromDB = {"ECI", "RECURRENT", "PSTranID"};
    try {
      verifiedDataFromResponse(responseDoc, verifiedResponse);
      verifiedDataFromDB(tranId, verifiedResponseFromDB);
    } catch (Exception e) {
      System.out.println("TEST FAILED");
    }
  }

  @Test(dependsOnMethods = "RecurrentInitial")
  public void generateBatch() {
    JDBCMethods.setNewTranTime(tranId);
    System.out.println("--TRAN TIME was Updated--\n");
    paylinkCloseDayForApi(ID_AVAL);
    assertFalse(getValueFromTRAN(tranId, "Batch").isEmpty(), "BATCH");
  }

  @Test(dependsOnMethods = "generateBatch")
  public void RecurrentInitialRefund() {
    Document requestRevDoc = reversal(requestDoc, responseDoc, 0);
    Document responseRevDoc = sendRequest(URL, requestRevDoc);

    System.out.println("--REFUND--\nRequest:\n" + printRequest(requestRevDoc));
    System.out.println("Response:\n" + printResponse(responseRevDoc));

    int tranRefundId = getTranIdByOrder(getElementFromDocument(requestRevDoc, "OrderID"));

    assertEquals(getElementFromDocument(responseRevDoc, "TranCode"), "000", "TranCode");
    assertEquals(getElementFromDocument(responseRevDoc, "Rrn").length(), 12, "Rrn");
    assertEquals(getValueFromTRAN(tranRefundId, "RevFlag"), "0", "RevFlag");
    assertEquals(getValueFromTRAN(tranRefundId, "ECI"), getValueFromTRAN(tranId, "ECI"), "ECI");
    assertEquals(getValueFromTRAN(tranRefundId, "CVResult"), "M", "CVResult");
    assertEquals(getValueFromTRAN(tranRefundId, "RECURRENT"), "1", "RECURRENT");

    System.out.println("Verified:");
    String[] verifiedResponse = {"TranCode", "Rrn"};
    String[] verifiedResponseFromDB = {"ECI", "RevFlag", "CVResult", "RECURRENT"};
    try {
      verifiedDataFromResponse(responseRevDoc, verifiedResponse);
      verifiedDataFromDB(tranRefundId, verifiedResponseFromDB);
      verifiedDataFromDB(tranId, new String[] {"Batch"});
    } catch (Exception e) {
      System.out.println("TEST FAILED");
    }
  }
}
