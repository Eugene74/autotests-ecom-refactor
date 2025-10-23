package tests.mt.api.aval;


import com.ecom.tests.support.MoneyTransferRequests;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;

import static com.ecom.core.config.CardConfig.cardMCmt;
import static com.ecom.core.config.CardConfig.cardVISA;
import static com.ecom.core.config.EnvData.URL_MT_Tran;
import static com.ecom.core.config.EnvData.merchant_AVAL;
import static com.ecom.core.config.EnvData.terminal_AVAL;
import static com.ecom.db.JDBCMethods.getTranMT3dsData;
import static com.ecom.db.JDBCMethods.getValueFromMTTran;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.RequestSenderRest.sendRequest;


public class CardToCardEMV3DSMasterVisa {

    @Test
    public void transferCardToCard() {
        Document requestDoc = MoneyTransferRequests.transferCardToCardEMV(cardMCmt, cardVISA, merchant_AVAL, terminal_AVAL);
        Document responseDoc = sendRequest(URL_MT_Tran, requestDoc);
        
        System.out.println("--TRANSFER--\nRequest:\n" + printRequestMT(requestDoc));
        System.out.println("Response:\n" + printResponseMT(responseDoc));

        String rrnFunding = getElementFromDocument(responseDoc, "RRN");
        String rrnPayment = getSecondElementFromDocument(responseDoc, "RRN");
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
        softAssertion.assertEquals(getValueFromMTTran(rrnPayment, "ECI"), "07", "ECI Payment");
        softAssertion.assertTrue(!getTranMT3dsData(rrnFunding).isEmpty(), "DS_TransID");

        softAssertion.assertEquals(getSecondElementFromDocument(responseDoc, "ApprovalCode").length(), 6, "ApprovalCode");
        softAssertion.assertEquals(rrnPayment.length(), 12, "RRN");
        softAssertion.assertEquals(getSecondElementFromDocument(responseDoc, "AuthCode"), "000", "AuthCode");
        softAssertion.assertEquals(getSecondElementFromDocument(responseDoc, "MCC"), "6538", "MCC");
        softAssertion.assertAll();

        System.out.println("Verified:");
        String [] verifiedResponse = {"Code", "Message"};
        String [] verifiedFunding = {"CVResult", "ApprovalCode", "AuthCode", "MCC", "RRN"};
        String [] verifiedPayment = {"ApprovalCode", "AuthCode", "MCC", "RRN"};
        String [] verifiedResponseFromDBFunding = {"ECI", "PAResStatus", "PA_ECI", "Version3DS", "DS_TransID"};
        String [] verifiedResponseFromDBPayment = {"ECI"};
        try{
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataResponseFunding(responseDoc, verifiedFunding);
            verifiedDataResponsePayment(responseDoc, verifiedPayment, 1);
            verifiedDataMTFromDBFunding(rrnFunding, verifiedResponseFromDBFunding);
            verifiedDataMTFromDBPayment(rrnPayment, verifiedResponseFromDBPayment);}
        catch (Exception e) {
            System.out.print("TEST FAILED");
        }
    }
}
