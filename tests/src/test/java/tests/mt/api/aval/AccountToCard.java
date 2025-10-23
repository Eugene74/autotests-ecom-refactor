package tests.mt.api.aval;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;

import static com.ecom.core.config.CardConfig.cardMCmt;
import static com.ecom.core.config.EnvData.URL_MT_Tran;
import static com.ecom.core.config.EnvData.merchant_AVAL;
import static com.ecom.core.config.EnvData.terminal_AVAL;
import static com.ecom.db.JDBCMethods.getValueFromMTTran;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.MoneyTransferRequests.transferAccountToCard;
import static com.ecom.tests.support.RequestSenderRest.sendRequest;

public class AccountToCard {

    @Test
    public void accountToCard() {
        Document requestDoc = transferAccountToCard(cardMCmt, merchant_AVAL, terminal_AVAL);
        Document responseDoc = sendRequest(URL_MT_Tran, requestDoc);
        
        System.out.println("--TRANSFER--\nRequest:\n" + printRequestMT(requestDoc));
        System.out.println("Response:\n" + printResponseMT(responseDoc));

        String rrnPayment = getElementFromDocument(responseDoc, "RRN");
        
        SoftAssert softAssertion= new SoftAssert();
        softAssertion.assertEquals(getValueFromMTTran(rrnPayment, "ECI"), "07", "ECI");
        softAssertion.assertEquals(getElementFromDocument(responseDoc, "ApprovalCode").length(), 6, "ApprovalCode");
        softAssertion.assertEquals(rrnPayment.length(), 12, "RRN");
        softAssertion.assertEquals(getElementFromDocument(responseDoc, "AuthCode"), "000", "AuthCode");
        softAssertion.assertEquals(getElementFromDocument(responseDoc, "MCC"), "6536", "MCC");
        softAssertion.assertAll();

        System.out.println("Verified:");
        String [] verifiedResponse = {"Code", "Message"};
        String [] verifiedPayment = {"ApprovalCode", "AuthCode", "MCC", "RRN"};
        String [] verifiedResponseFromDBPayment = {"ECI"};
        try{
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataResponsePayment(responseDoc, verifiedPayment, 0);
            verifiedDataMTFromDBPayment(rrnPayment, verifiedResponseFromDBPayment);}
        catch (Exception e) {
            System.out.printf("TEST FAILED");
        }
    }
}
