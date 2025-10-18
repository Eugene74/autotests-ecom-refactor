package tests.paylink.xml;


import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;

import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static methods.DocumentTools.*;
import static methods.PaylinkRequests.*;
import static methods.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;


public class PaymentAttempt_Refund_Part extends BaseTest {
    private static Document requestDoc;
    private static Document responseDoc;
    private static int tranId;

    @Test
    public void mcPaymentAttempt(){
        requestDoc = paymentPares(cardMC06, "MasterCardPaymentAttempt+RefundPart", merchant_AVAL, terminal_AVAL);
        responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        tranId = getTranIdByOrder(getElementFromDocument (requestDoc,"OrderID"));

//        from Response
        assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000","TranCode" );
        assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M","CVResult" );
        assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
        assertEquals(getElementFromDocument(responseDoc, "Rrn").length(), 12, "Rrn");
        assertEquals(getElementFromDocument(responseDoc, "ApprovalCode").length(), 6,"ApprovalCode" );
//        from DB
        assertEquals(getValueFromTRAN(tranId, "ECI"), "01","ECI" );
        assertEquals(getValueFromTRAN(tranId, "PAResStatus"), "A","PAResStatus" );
        assertEquals(getValueFromTRAN(tranId, "PA_ECI"), "01","PA_ECI" );
//        assertEquals(getValueFromTRAN(tranId, "FEE"), "400", "FEE");

        System.out.println("Verified:");
        String [] verifiedResponse = {"TranCode", "CVResult", "HostCode", "Rrn", "ApprovalCode"};
        String [] verifiedResponseFromDB = {"ECI", "PAResStatus", "PA_ECI"};
        try{
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataFromDB(tranId, verifiedResponseFromDB);}
        catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @Test(dependsOnMethods = "mcPaymentAttempt")
    public void generateBatch(){
        paylinkCloseDayForApi (id_AVAL);
        assertFalse(getValueFromTRAN(tranId, "Batch").isEmpty(), "BATCH");
    }

    @Test (dependsOnMethods = "generateBatch")
    public void mcPaymentAttemptRefundPart() {
        Document requestRevDoc = reversal(requestDoc, responseDoc, -0.5);
        Document responseRevDoc = sendRequest(URL, requestRevDoc);

        System.out.println("--REFUND--\nRequest:\n" + printRequest(requestRevDoc));
        System.out.println("Response:\n" + printResponse(responseRevDoc));

        int tranRefundId = getTranIdByOrder(getElementFromDocument(requestRevDoc, "OrderID"));

        assertEquals(getElementFromDocument(responseRevDoc, "TranCode"), "000","TranCode" );
        assertEquals(getElementFromDocument(responseRevDoc, "Rrn").length(), 12, "Rrn");

        assertEquals(getValueFromTRAN(tranRefundId, "RevFlag"), "0", "RevFlag");
        assertEquals(getValueFromTRAN(tranRefundId, "ECI"), getValueFromTRAN(tranId, "ECI"), "ECI");
        assertEquals(getValueFromTRAN(tranRefundId, "PAResStatus"), "A", "PAResStatus");
        assertEquals(getValueFromTRAN(tranRefundId, "PA_ECI"), "01", "PA_ECI");
        assertEquals(getValueFromTRAN(tranRefundId, "CVResult"), "M", "CVResult");

        System.out.println("Verified:");
        String [] verifiedResponse = {"TranCode", "Rrn"};
        String [] verifiedResponseFromDB = {"ECI", "RevFlag", "CVResult","PAResStatus", "PA_ECI"};
        try{
            verifiedDataFromResponse(responseRevDoc, verifiedResponse);
            verifiedDataFromDB(tranRefundId, verifiedResponseFromDB);
            verifiedDataFromDB(tranId, new String[]{"Batch"});}
        catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

}
