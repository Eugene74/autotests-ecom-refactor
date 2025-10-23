package tests.paylink.xml;

import static com.ecom.core.config.CardConfig.cardMC;
import static com.ecom.core.config.EnvData.MERCHANT_ID_AVAL;
import static com.ecom.core.config.EnvData.TERMINAL_ID_AVAL;
import static com.ecom.core.config.EnvData.URL;
import static com.ecom.core.config.EnvData.VOICE_FILTR;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.db.JDBCMethods.setMerchantFilter;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.payment;
import static com.ecom.tests.support.RequestSenderRest.sendRequest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

public class Voice {

  @BeforeClass
  public void setStatusFilter() {
    setMerchantFilter(VOICE_FILTR, "1");
    System.out.println("SET NEW STATUS OF FILTER VOICE: TRUE");
  }

  @Test
  public void mcPaymentVoice() {
    Document requestDoc = payment(cardMC, "voice", MERCHANT_ID_AVAL, TERMINAL_ID_AVAL);
    Document responseDoc = sendRequest(URL, requestDoc);

    System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
    System.out.println("Response:\n" + printResponse(responseDoc));

    int tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));

    //        from Response
    assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000", "TranCode");
    assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
    assertEquals(getElementFromDocument(responseDoc, "Rrn").length(), 12, "Rrn");
    assertEquals(getElementFromDocument(responseDoc, "ApprovalCode").length(), 6, "ApprovalCode");

    //        from DB
    assertNull(getValueFromTRAN(tranId, "ECI"), "ECI");
    assertNull(getValueFromTRAN(tranId, "CVResult"), "CVResult");
    assertEquals(getValueFromTRAN(tranId, "POS_CODE"), "00", "POS_CODE");

    System.out.println("Verified:");
    String[] verifiedResponse = {"TranCode", "HostCode", "Rrn", "ApprovalCode"};
    String[] verifiedResponseFromDB = {"ECI", "CVResult", "POS_CODE"};
    try {
      verifiedDataFromResponse(responseDoc, verifiedResponse);
      verifiedDataFromDB(tranId, verifiedResponseFromDB);
    } catch (Exception e) {
      System.out.println("TEST FAILED");
    }
  }

  @AfterClass
  public void setDefaultAttr() {
    setMerchantFilter(VOICE_FILTR, "0");
    System.out.println("SET NEW STATUS OF FILTER VOICE: FALSE");
  }
}
