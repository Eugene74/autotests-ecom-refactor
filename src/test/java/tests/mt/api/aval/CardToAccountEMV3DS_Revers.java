package tests.mt.api.aval;


import methods.RequestSender;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import tests.mt.BaseTestMoneyTransfer;

import static jdbc.JDBCMethods.*;
import static methods.DocumentTools.*;
import static methods.MoneyTransferRequests.reversalOnFunding;
import static methods.MoneyTransferRequests.transferCardToAccountEmv;


public class CardToAccountEMV3DS_Revers extends BaseTestMoneyTransfer {
    protected Document requestDoc;
    protected Document responseDoc;

    @Test
    public void transferFromCardToAccountEMV3DS() {
        requestDoc = transferCardToAccountEmv(cardMCmt, merchant_AVAL, terminal_AVAL);
        responseDoc = RequestSender.sendRequest(URLmt, requestDoc);
        
        System.out.println("--TRANSFER--\nRequest:\n" + printRequestMT(requestDoc));
        System.out.println("Response:\n" + printResponseMT(responseDoc));

        String rrnFunding = getElementFromDocument(responseDoc, "RRN");
        int tranID = Integer.parseInt(getElementFromDocument(responseDoc, "TrackingId"));
        
        SoftAssert softAssertion= new SoftAssert();
        softAssertion.assertEquals(getElementFromDocument(responseDoc, "Code"), "000", "Code");
        softAssertion.assertEquals(getElementFromDocument(responseDoc, "Message"), "Approved", "Message");
        //softAssertion.assertEquals(getElementFromDocument(responseDoc, "CVResult"), "P2", "CVResult");
        softAssertion.assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M", "CVResult");
        softAssertion.assertEquals(getElementFromDocument(responseDoc, "ApprovalCode").length(), 6, "ApprovalCode");
        softAssertion.assertEquals(getElementFromDocument(responseDoc, "AuthCode"), "000", "AuthCode");
        softAssertion.assertEquals(getElementFromDocument(responseDoc, "MCC"), "6538", "MCC");
        softAssertion.assertEquals(rrnFunding.length(), 12, "RRN");

        softAssertion.assertEquals(getValueFromMTTran(rrnFunding, "ECI"), "02", "ECI Funding");
        softAssertion.assertEquals(getValueFromMTTran(rrnFunding, "PAResStatus"), "Y", "PAResStatus");
        softAssertion.assertEquals(getValueFromMTTran(rrnFunding, "PA_ECI"), "02", "PA_ECI");
        softAssertion.assertEquals(getValueFromMTTran(rrnFunding, "Version3DS"), "2", "Version3DS");
        softAssertion.assertTrue(!getTranMT3dsData(rrnFunding).isEmpty(), "DS_TransID");
        softAssertion.assertAll();

        System.out.println("Verified:");
        String [] verifiedResponse = {"Code", "Message"};
        String [] verifiedFunding = {"CVResult", "ApprovalCode", "AuthCode", "MCC", "RRN"};
        String [] verifiedResponseFromDBFunding = {"ECI", "PAResStatus", "PA_ECI", "Version3DS", "DS_TransID"};
        try {
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataResponseFunding(responseDoc, verifiedFunding);
            verifiedDataMTFromDBFunding(rrnFunding, verifiedResponseFromDBFunding);
        }
        catch (Exception e) {
            System.out.printf("TEST FAILED");
        }
    }

    @Test (dependsOnMethods = "transferFromCardToAccountEMV3DS")
    public void reversOnFundingCardToAccountEMV(){
        Document requestRevDoc = reversalOnFunding(merchant_AVAL, terminal_AVAL, requestDoc, responseDoc);
        Document responseRevDoc = RequestSender.sendRequest(URLmtRev, requestRevDoc);

        System.out.println("--REVERSAL--\nRequest:\n" + printReversalMT(requestRevDoc, "ReversalRequest"));
        System.out.println("Response:\n" + printReversalMT(responseRevDoc, "ReversalResponse"));

        String rrnReversal = getElementFromDocument(responseRevDoc, "RRN");

        SoftAssert softAssertion= new SoftAssert();
        softAssertion.assertEquals(getElementFromDocument(responseRevDoc, "Code"), "000", "Code");
        softAssertion.assertEquals(getElementFromDocument(responseRevDoc, "Message"), "Approved", "Message");
        softAssertion.assertEquals(getElementFromDocument(responseRevDoc, "ApprovalCode").length(), 6, "ApprovalCode");
        softAssertion.assertEquals(getElementFromDocument(responseRevDoc, "AuthCode"), "000", "AuthCode");
        softAssertion.assertEquals(getElementFromDocument(responseRevDoc, "MCC"), "6538", "MCC");
        softAssertion.assertEquals(rrnReversal, getElementFromDocument(responseDoc, "RRN"), "RRN Funding" );
        //softAssertion.assertEquals("1", getValueFromMTTran(rrnReversal, "Reversed"), "Reversed");
        softAssertion.assertEquals(getValueFromMTTranFunding(rrnReversal, "Reversed"), "1", "Reversed");
        softAssertion.assertAll();

        System.out.println("Verified:");
        String [] verifiedResponse = {"Code", "Message"};
        String [] verifiedReversal = {"ApprovalCode", "AuthCode", "MCC", "RRN"};
        String [] verifiedResponseFromDBRevers = {"Reversed"};
        try {
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataResponseRevers(responseDoc, verifiedReversal);
            verifiedDataMTFromDBReversal(rrnReversal, verifiedResponseFromDBRevers);
        }
        catch (Exception e) {
            System.out.printf("TEST FAILED");
        }
    }
}
