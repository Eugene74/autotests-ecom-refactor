package tests.paylink.xml;

import static com.ecom.api.type.Attributes.ALLOW_MASTERCARD_INSTALLMENT;
import static com.ecom.core.config.CardConfig.cardMCInst;
import static com.ecom.core.config.EnvData.ID_AVAL;
import static com.ecom.core.config.EnvData.MERCHANT_ID_AVAL;
import static com.ecom.core.config.EnvData.TERMINAL_ID_AVAL;
import static com.ecom.core.config.EnvData.URL;
import static com.ecom.db.JDBCMethods.*;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.paymentAmount;
import static com.ecom.tests.support.PaylinkRequests.paymentInstallChoice;
import static com.ecom.tests.support.RequestSenderRest.sendRequest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertFalse;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

public class MasterInstallment {
  private static Document requestDoc;
  private static Document responseDoc;
  private static int tranId;

  @BeforeClass
  public void setStatusAttr() {
    setMerchantAtt(ID_AVAL, ALLOW_MASTERCARD_INSTALLMENT, "true");
    System.out.println("ALLOW_MASTERCARD_INSTALLMENT: TRUE");
  }

  @Test
  public void payInstallment() {
    requestDoc =
        paymentAmount(
            cardMCInst, "100000", "mc installment xml", MERCHANT_ID_AVAL, TERMINAL_ID_AVAL);
    responseDoc = sendRequest(URL, requestDoc);

    System.out.println("--PAYMENT--\nRequest\n" + printRequest(requestDoc));
    System.out.println("Response\n" + printResponse(responseDoc));

    tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));

    assertEquals(getElementFromDocument(responseDoc, "TranCode"), "602", "TranCode");
    assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
    assertEquals(getElementFromDocument(responseDoc, "Rrn").length(), 12, "Rrn");
    assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M", "CVResult");
    assertEquals(getElementFromDocument(responseDoc, "ApprovalCode").length(), 6, "ApprovalCode");

    assertEquals(getValueFromTRAN(tranId, "ECI"), "07", "ECI");
    assertFalse("PSTranID", getValueFromTRAN(tranId, "PSTranID").isEmpty());

    System.out.println("Verified:");
    String[] verifiedResponse = {"TranCode", "HostCode", "Rrn", "ApprovalCode", "CVResult"};
    String[] verifiedResponseFromDB = {"ECI", "PSTranID"};
    try {
      verifiedDataFromResponse(responseDoc, verifiedResponse);
      verifiedDataFromDB(tranId, verifiedResponseFromDB);
    } catch (Exception e) {
      System.out.println("TEST FAILED");
    }
  }

  @Test(dependsOnMethods = "payInstallment")
  public void payInstallmentChoice() {
    Document requestInstChoose = paymentInstallChoice(requestDoc, responseDoc);
    Document responseInstChoose = sendRequest(URL, requestInstChoose);

    System.out.println("--INSTALLMENT--\nRequest\n" + printRequest(requestInstChoose));
    System.out.println("Response\n" + printResponse(responseInstChoose));

    assertEquals(getElementFromDocument(responseInstChoose, "TranCode"), "000", "TranCode");
    assertEquals(getValueFromTRAN(tranId, "AddendumData"), "1", "AddendumData");
    assertEquals(getValueFromTRAN(tranId, "Installment"), "1", "Installment");
    assertTrue(getAddendumData(tranId, "CONVERSION_RATE_DATE"));
    assertTrue(getInstallmentData(tranId));

    System.out.println("Verified:");
    try {
      verifiedDataFromResponse(responseDoc, new String[] {"TranCode"});
      verifiedDataFromDB(tranId, new String[] {"ECI", "AddendumData", "Installment"});
    } catch (Exception e) {
      System.out.println("TEST FAILED");
    }
  }

  @AfterClass
  public void setDefaultAttr() {
    setMerchantAtt(ID_AVAL, ALLOW_MASTERCARD_INSTALLMENT, "false");
    System.out.println("ALLOW_MASTERCARD_INSTALLMENT: FALSE");
  }
}
