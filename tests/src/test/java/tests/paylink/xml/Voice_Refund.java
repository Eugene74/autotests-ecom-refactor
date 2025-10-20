package tests.paylink.xml;

import com.ecom.db.JDBCMethods;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;

import static com.ecom.core.config.CardConfig.cardMC07;
import static com.ecom.core.config.EnvData.*;
import static com.ecom.db.JDBCMethods.*;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.*;
import static com.ecom.tests.support.RequestSender.sendRequest;
import static org.testng.Assert.*;


public class Voice_Refund extends BaseTest {
    protected static Document requestDoc;
    protected static Document responseDoc;
    private static int tranId;

    @BeforeClass
    public void setStatusFilter(){
        setMerchantFilter(VOICE_FILTR, "1");
        System.out.println("SET NEW STATUS OF FILTER VOICE: TRUE");
    }

    @Test
    public void mcPaymentVoice(){
        requestDoc = payment(cardMC07, "MCVoice+Refund", merchant_AVAL, terminal_AVAL);
        responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        tranId = getTranIdByOrder(getElementFromDocument (requestDoc,"OrderID"));

//        from Response
        assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000","TranCode" );
        assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
        assertEquals(getElementFromDocument(responseDoc, "Rrn").length(), 12, "Rrn");
        assertEquals (getElementFromDocument(responseDoc, "ApprovalCode").length(), 6,"ApprovalCode" );

//        from DB
        assertNull(getValueFromTRAN(tranId, "ECI"), "ECI");
        assertNull(getValueFromTRAN(tranId, "CVResult"), "CVResult");
        assertEquals(JDBCMethods.getValueFromTRAN(tranId, "POS_CODE"), "00","POS_CODE");

        System.out.println("Verified:");
        String [] verifiedResponse = {"TranCode", "HostCode", "Rrn", "ApprovalCode"};
        String [] verifiedResponseFromDB = {"ECI", "CVResult", "POS_CODE"};
        try{
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataFromDB(tranId, verifiedResponseFromDB);}
        catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @Test(dependsOnMethods = "mcPaymentVoice")
    public void generateBatch(){
        paylinkCloseDayForApi (id_AVAL);
        assertFalse(getValueFromTRAN(tranId, "Batch").isEmpty(), "BATCH");
    }

    @Test(dependsOnMethods = "generateBatch")
    public void mcPaymentVoiceRefund() {
        Document requestRevDoc = reversal(requestDoc, responseDoc, 0);
        Document responseRevDoc = sendRequest(URL, requestRevDoc);

        System.out.println("--REFUND--\nRequest:\n" + printRequest(requestRevDoc));
        System.out.println("Response:\n" + printResponse(responseRevDoc));

        int tranRefundId = getTranIdByOrder(getElementFromDocument(requestRevDoc, "OrderID"));

        assertEquals(getElementFromDocument(responseRevDoc, "TranCode"), "000","TranCode" );
        assertEquals(getElementFromDocument(responseRevDoc, "Rrn").length(), 12, "Rrn");
        assertEquals(getValueFromTRAN(tranRefundId, "RevFlag"), "0", "RevFlag");
        assertEquals(getValueFromTRAN(tranRefundId, "ECI"), JDBCMethods.getValueFromTRAN(tranId, "ECI"), "ECI");
        assertNull(getValueFromTRAN(tranRefundId, "CVResult"), "CVResult");

        System.out.println("Verified:");
        String [] verifiedResponse = {"TranCode", "Rrn"};
        String [] verifiedResponseFromDB = {"ECI", "RevFlag", "CVResult"};
        try{
            verifiedDataFromResponse(responseRevDoc, verifiedResponse);
            verifiedDataFromDB(tranRefundId, verifiedResponseFromDB);
            verifiedDataFromDB(tranId, new String[]{"Batch"});}
        catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @AfterClass
    public void setDefaultAttr(){
        setMerchantFilter(VOICE_FILTR, "0");
        System.out.println("SET NEW STATUS OF FILTER VOICE: FALSE");
    }
}
