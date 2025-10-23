package tests.paylink.xml;

import static com.ecom.api.type.Attributes.ALLOW_REF3;
import static com.ecom.api.type.Attributes.ALLOW_SETTLEMENT_REFUND;
import static com.ecom.core.config.CardConfig.cardMC;
import static com.ecom.core.config.EnvData.ID_AVAL;
import static com.ecom.core.config.EnvData.MERCHANT_ID_AVAL;
import static com.ecom.core.config.EnvData.TERMINAL_ID_AVAL;
import static com.ecom.core.config.EnvData.URL;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.db.JDBCMethods.setMerchantAtt;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.settlementRefund;
import static com.ecom.tests.support.RequestSenderRest.sendRequest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

public class SettlementRefund_Aval {

  @BeforeClass
  public void setStatusAttr() {
    setMerchantAtt(ID_AVAL, ALLOW_SETTLEMENT_REFUND, "true");
    setMerchantAtt(ID_AVAL, ALLOW_REF3, "true");
    System.out.println("ALLOW_REF3 AND ALLOW_SETTLEMENT_REFUND: TRUE");
  }

  @Test
  public void SettlementRefund() {
    Document requestDoc =
        settlementRefund(cardMC, "settlement ref", MERCHANT_ID_AVAL, TERMINAL_ID_AVAL);
    Document responseDoc = sendRequest(URL, requestDoc);

    System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
    System.out.println("Response:\n" + printResponse(responseDoc));

    int tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));

    assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000", "TranCode");
    assertEquals(getValueFromTRAN(tranId, "ECI"), "07", "ECI");
    assertNull(getValueFromTRAN(tranId, "CVResult"), "CVResult");
    assertEquals(getValueFromTRAN(tranId, "POS_CODE"), "08", "POS_CODE");

    System.out.println("Verified:");
    String[] verifiedResponse = {"TranCode"};
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
    setMerchantAtt(ID_AVAL, ALLOW_SETTLEMENT_REFUND, "false");
    setMerchantAtt(ID_AVAL, ALLOW_REF3, "false");
    System.out.println("ALLOW_REF3 AND ALLOW_SETTLEMENT_REFUND: FALSE");
  }
}
