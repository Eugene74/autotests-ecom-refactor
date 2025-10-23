package tests.paylink.xml;

import static com.ecom.api.type.Attributes.ALLOW_MASTERPASS;
import static com.ecom.core.config.CardConfig.cardMC;
import static com.ecom.core.config.EnvData.ID_AVAL;
import static com.ecom.core.config.EnvData.MERCHANT_ID_AVAL;
import static com.ecom.core.config.EnvData.TERMINAL_ID_AVAL;
import static com.ecom.core.config.EnvData.URL;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.db.JDBCMethods.setMerchantAtt;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.paymentMasterpass;
import static com.ecom.tests.support.PaylinkRequests.reversal;
import static com.ecom.tests.support.RequestSenderRest.sendRequest;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

public class Masterpass_Revers {
  private static Document requestDoc;
  private static Document responseDoc;
  private static int tranId;

  @BeforeClass
  public void setStatusAttr() {
    setMerchantAtt(ID_AVAL, ALLOW_MASTERPASS, "true");
    System.out.println("ALLOW_MASTERPASS: TRUE");
  }

  @Test
  public void masterpass() {
    requestDoc = paymentMasterpass(cardMC, "Masterpass", MERCHANT_ID_AVAL, TERMINAL_ID_AVAL);
    responseDoc = sendRequest(URL, requestDoc);

    System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
    System.out.println("Response:\n" + printResponse(responseDoc));

    tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));
    //        from Response
    assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000", "TranCode");
    assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
    assertEquals(getElementFromDocument(responseDoc, "Rrn").length(), 12, "Rrn");
    assertEquals(getElementFromDocument(responseDoc, "ApprovalCode").length(), 6, "ApprovalCode");
    //        from DB
    assertEquals(getValueFromTRAN(tranId, "ECI"), "07", "ECI");
    assertEquals(getValueFromTRAN(tranId, "CVResult"), "M", "CVResult");
    // assertNull(getValueFromTRAN(tranId, "CVResult"), "CVResult");
    // assertFalse(getValueFromTRAN(tranId, "PSTranID").isEmpty(), "PSTranID");

    System.out.println("Verified:");
    String[] verifiedResponse = {"TranCode", "HostCode", "Rrn", "ApprovalCode"};
    String[] verifiedResponseFromDB = {"ECI", "CVResult", "PSTranID"};
    try {
      verifiedDataFromResponse(responseDoc, verifiedResponse);
      verifiedDataFromDB(tranId, verifiedResponseFromDB);
    } catch (Exception e) {
      System.out.println("TEST FAILED");
    }
  }

  @Test(dependsOnMethods = "masterpass")
  public void masterpassRevers() {
    Document requestRevDoc = reversal(requestDoc, responseDoc, 0);
    Document responseRevDoc = sendRequest(URL, requestRevDoc);

    System.out.println("--REVERSAL--\nRequest:\n" + printRequest(requestRevDoc));
    System.out.println("Response:\n" + printResponse(responseRevDoc));

    int tranReversId = getTranIdByOrder(getElementFromDocument(requestRevDoc, "OrderID"));

    assertEquals(getElementFromDocument(responseRevDoc, "TranCode"), "000", "TranCode");
    assertEquals(getElementFromDocument(responseRevDoc, "Rrn").length(), 12, "Rrn");
    assertEquals(getValueFromTRAN(tranReversId, "RevFlag"), "1", "RevFlag");
    assertEquals(getValueFromTRAN(tranReversId, "ECI"), getValueFromTRAN(tranId, "ECI"), "ECI");

    System.out.println("Verified:");
    String[] verifiedResponse = {"TranCode", "Rrn"};
    String[] verifiedResponseFromDB = {"ECI", "RevFlag"};
    try {
      verifiedDataFromResponse(responseRevDoc, verifiedResponse);
      verifiedDataFromDB(tranReversId, verifiedResponseFromDB);
    } catch (Exception e) {
      System.out.println("TEST FAILED");
    }
  }

  @AfterClass
  public void setDefaultAttr() {
    setMerchantAtt(ID_AVAL, ALLOW_MASTERPASS, "false");
    System.out.println("ALLOW_MASTERPASS: FALSE");
  }
}
