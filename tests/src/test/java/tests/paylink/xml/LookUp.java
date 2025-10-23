package tests.paylink.xml;

import static com.ecom.core.config.CardConfig.cardVISA;
import static com.ecom.core.config.EnvData.MERCHANT_ID_LOOK;
import static com.ecom.core.config.EnvData.TERMINAL_ID_LOOK;
import static com.ecom.core.config.EnvData.URL;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.paymentAmount;
import static com.ecom.tests.support.RequestSenderRest.sendRequest;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;
import org.w3c.dom.Document;

public class LookUp {

  @Test
  public void visaLookUp() {

    Document requestDoc =
        paymentAmount(cardVISA, "0", "mc lookup", MERCHANT_ID_LOOK, TERMINAL_ID_LOOK);
    Document responseDoc = sendRequest(URL, requestDoc);

    System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
    System.out.println("Response:\n" + printResponse(responseDoc));

    int tranID = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));

    assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000", "TranCode");
    assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M", "CVResult");
    assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
    assertEquals(getElementFromDocument(responseDoc, "Rrn").length(), 12, "Rrn");
    assertEquals(getElementFromDocument(responseDoc, "ApprovalCode").length(), 6, "ApprovalCode");
    //        from DB
    assertEquals(getValueFromTRAN(tranID, "ECI"), "07", "ECI");
    System.out.println("Verified:");
    String[] verifiedResponse = {"TranCode", "CVResult", "HostCode", "Rrn", "ApprovalCode"};
    String[] verifiedResponseFromDB = {"ECI"};
    try {
      verifiedDataFromResponse(responseDoc, verifiedResponse);
      verifiedDataFromDB(tranID, verifiedResponseFromDB);
    } catch (Exception e) {
      System.out.println("TEST FAILED");
    }
  }
}
