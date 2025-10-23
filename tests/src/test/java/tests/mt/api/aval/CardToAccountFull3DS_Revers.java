package tests.mt.api.aval;

import static com.ecom.core.config.CardConfig.cardMCmt;
import static com.ecom.core.config.EnvData.MERCHANT_ID_AVAL;
import static com.ecom.core.config.EnvData.TERMINAL_ID_AVAL;
import static com.ecom.core.config.EnvData.URL_MT_REV;
import static com.ecom.core.config.EnvData.URL_MT_TRAN;
import static com.ecom.db.JDBCMethods.getValueFromMTTran;
import static com.ecom.db.JDBCMethods.getValueFromMTTranFunding;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.MoneyTransferRequests.reversalOnFunding;
import static com.ecom.tests.support.MoneyTransferRequests.transferCardToAccountPares;
import static com.ecom.tests.support.RequestSenderRest.sendRequest;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;

public class CardToAccountFull3DS_Revers {
  protected Document requestDoc;
  protected Document responseDoc;

  @Test
  public void transferFromCardToAccountFull3DS() {
    requestDoc = transferCardToAccountPares(cardMCmt, MERCHANT_ID_AVAL, TERMINAL_ID_AVAL);
    responseDoc = sendRequest(URL_MT_TRAN, requestDoc);

    System.out.println("--TRANSFER--\nRequest:\n" + printRequestMT(requestDoc));
    System.out.println("Response:\n" + printResponseMT(responseDoc));

    String rrnFunding = getElementFromDocument(responseDoc, "RRN");
    int tranID = Integer.parseInt(getElementFromDocument(responseDoc, "TrackingId"));

    SoftAssert softAssertion = new SoftAssert();
    softAssertion.assertEquals(getElementFromDocument(responseDoc, "Code"), "000", "Code");
    softAssertion.assertEquals(
        getElementFromDocument(responseDoc, "Message"), "Approved", "Message");
    // softAssertion.assertEquals(getElementFromDocument(responseDoc, "CVResult"), "P2",
    // "CVResult");
    softAssertion.assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M", "CVResult");
    softAssertion.assertEquals(
        getElementFromDocument(responseDoc, "ApprovalCode").length(), 6, "ApprovalCode");
    softAssertion.assertEquals(getElementFromDocument(responseDoc, "AuthCode"), "000", "AuthCode");
    softAssertion.assertEquals(getElementFromDocument(responseDoc, "MCC"), "6538", "MCC");
    softAssertion.assertEquals(rrnFunding.length(), 12, "RRN");

    softAssertion.assertEquals(getValueFromMTTran(rrnFunding, "ECI"), "02", "ECI Funding");
    softAssertion.assertEquals(getValueFromMTTran(rrnFunding, "PAResStatus"), "Y", "PAResStatus");
    softAssertion.assertEquals(getValueFromMTTran(rrnFunding, "PA_ECI"), "02", "PA_ECI");
    softAssertion.assertAll();

    System.out.println("Verified:");
    String[] verifiedResponse = {"Code", "Message"};
    String[] verifiedFunding = {"CVResult", "ApprovalCode", "AuthCode", "MCC", "RRN"};
    String[] verifiedResponseFromDBFunding = {"ECI", "PAResStatus", "PA_ECI"};
    try {
      verifiedDataFromResponse(responseDoc, verifiedResponse);
      verifiedDataResponseFunding(responseDoc, verifiedFunding);
      verifiedDataMTFromDBFunding(rrnFunding, verifiedResponseFromDBFunding);
    } catch (Exception e) {
      System.out.printf("TEST FAILED");
    }
  }

  @Test(dependsOnMethods = "transferFromCardToAccountFull3DS")
  public void reversOnFundingCardToAccountFull3DS() {
    Document requestRevDoc =
        reversalOnFunding(MERCHANT_ID_AVAL, TERMINAL_ID_AVAL, requestDoc, responseDoc);
    Document responseRevDoc = sendRequest(URL_MT_REV, requestRevDoc);

    System.out.println(
        "--REVERSAL--\nRequest:\n" + printReversalMT(requestRevDoc, "ReversalRequest"));
    System.out.println("Response:\n" + printReversalMT(responseRevDoc, "ReversalResponse"));

    String rrnReversal = getElementFromDocument(responseRevDoc, "RRN");

    SoftAssert softAssertion = new SoftAssert();
    softAssertion.assertEquals(getElementFromDocument(responseRevDoc, "Code"), "000", "Code");
    softAssertion.assertEquals(
        getElementFromDocument(responseRevDoc, "Message"), "Approved", "Message");
    softAssertion.assertEquals(
        getElementFromDocument(responseRevDoc, "ApprovalCode").length(), 6, "ApprovalCode");
    softAssertion.assertEquals(
        getElementFromDocument(responseRevDoc, "AuthCode"), "000", "AuthCode");
    softAssertion.assertEquals(getElementFromDocument(responseRevDoc, "MCC"), "6538", "MCC");
    softAssertion.assertEquals(
        rrnReversal, getElementFromDocument(responseDoc, "RRN"), "RRN Funding");
    // oftAssertion.assertEquals("1", getValueFromMTTran(rrnReversal, "Reversed"), "Reversed");
    softAssertion.assertEquals(getValueFromMTTranFunding(rrnReversal, "Reversed"), "1", "Reversed");
    softAssertion.assertAll();

    System.out.println("Verified:");
    String[] verifiedResponse = {"Code", "Message"};
    String[] verifiedReversal = {"ApprovalCode", "AuthCode", "MCC", "RRN"};
    String[] verifiedResponseFromDBRevers = {"Reversed"};
    try {
      verifiedDataFromResponse(responseDoc, verifiedResponse);
      verifiedDataResponseRevers(responseDoc, verifiedReversal);
      verifiedDataMTFromDBReversal(rrnReversal, verifiedResponseFromDBRevers);
    } catch (Exception e) {
      System.out.printf("TEST FAILED");
    }
  }
}
