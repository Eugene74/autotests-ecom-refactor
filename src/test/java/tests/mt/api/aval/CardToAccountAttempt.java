package tests.mt.api.aval;


import methods.RequestSender;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import tests.mt.BaseTestMoneyTransfer;

import static jdbc.JDBCMethods.getValueFromMTTran;
import static methods.DocumentTools.*;
import static methods.MoneyTransferRequests.transferCardToAccountPares;


public class CardToAccountAttempt extends BaseTestMoneyTransfer {

    @Test
    public void transferFromCardToAccountAttempt() {
        Document requestDoc = transferCardToAccountPares(cardVISA, merchant_AVAL, terminal_AVAL);
        Document responseDoc = RequestSender.sendRequest(URLmt, requestDoc);
        
        System.out.println("--TRANSFER--\nRequest:\n" + printRequestMT(requestDoc));
        System.out.println("Response:\n" + printResponseMT(responseDoc));

        String rrnFunding = getElementFromDocument(responseDoc, "RRN");
        int tranID = Integer.parseInt(getElementFromDocument(responseDoc, "TrackingId"));
        
        SoftAssert softAssertion= new SoftAssert();
        softAssertion.assertEquals(getElementFromDocument(responseDoc, "Code"), "000", "Code");
        softAssertion.assertEquals(getElementFromDocument(responseDoc, "Message"), "Approved", "Message");
        softAssertion.assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M", "CVResult");
        softAssertion.assertEquals(getElementFromDocument(responseDoc, "ApprovalCode").length(), 6, "ApprovalCode");
        softAssertion.assertEquals(getElementFromDocument(responseDoc, "AuthCode"), "000", "AuthCode");
        softAssertion.assertEquals(getElementFromDocument(responseDoc, "MCC"), "6538", "MCC");
        softAssertion.assertEquals(rrnFunding.length(), 12, "RRN");

        softAssertion.assertEquals(getValueFromMTTran(rrnFunding, "ECI"), "06", "ECI Funding");
        softAssertion.assertEquals(getValueFromMTTran(rrnFunding, "PAResStatus"), "A", "PAResStatus");
        softAssertion.assertEquals(getValueFromMTTran(rrnFunding, "PA_ECI"), "06", "PA_ECI");
        softAssertion.assertAll();

        System.out.println("Verified:");
        String [] verifiedResponse = {"Code", "Message"};
        String [] verifiedFunding = {"CVResult", "ApprovalCode", "AuthCode", "MCC", "RRN"};
        String [] verifiedResponseFromDBFunding = {"ECI", "PAResStatus", "PA_ECI"};
        try {
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataResponseFunding(responseDoc, verifiedFunding);
            verifiedDataMTFromDBFunding(rrnFunding, verifiedResponseFromDBFunding);
        }
        catch (Exception e) {
            System.out.printf("TEST FAILED");
        }
    }
}
