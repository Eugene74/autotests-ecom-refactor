package tests.paylink.xml;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;

import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.*;
import static com.ecom.tests.support.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class PaymentFull3DS_Refund_Part extends BaseTest {
    private static Document requestDoc;
    private static Document responseDoc;
    private static int tranId;

    @Test
    public void mcPaymentFull3DS(){
        requestDoc = paymentPares(cardMC05, "MasterCardPaymentFull3DS+RefundPart", merchant_AVAL, terminal_AVAL);
        responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        tranId = getTranIdByOrder(getElementFromDocument (requestDoc,"OrderID"));

//        from Response
        assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000","TranCode" );
        assertEquals(getElementFromDocument(responseDoc, "CVResult"), "P2","CVResult" );
        assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
        assertEquals(getElementFromDocument(responseDoc, "Rrn").length(), 12, "Rrn");
        assertEquals(getElementFromDocument(responseDoc, "ApprovalCode").length(), 6,"ApprovalCode" );
//        from DB
        assertEquals(getValueFromTRAN(tranId, "ECI"), "02","ECI" );
        assertEquals(getValueFromTRAN(tranId, "PAResStatus"), "Y","PAResStatus" );
        assertEquals(getValueFromTRAN(tranId, "PA_ECI"), "02","PA_ECI" );
//        assertEquals(getValueFromTRAN(tranId, "FEE"), "400", "FEE");

        System.out.println("Verified:");
        String [] verifiedResponse = {"TranCode", "CVResult", "HostCode", "Rrn", "ApprovalCode"};
        String [] verifiedResponseFromDB = {"ECI", "PAResStatus", "PA_ECI" };
        try{
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataFromDB(tranId, verifiedResponseFromDB);}
        catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @Test(dependsOnMethods = "mcPaymentFull3DS")
    public void generateBatch(){
        paylinkCloseDayForApi (id_AVAL);
        assertFalse(getValueFromTRAN(tranId, "Batch").isEmpty(), "BATCH");
    }

    @Test (dependsOnMethods = "generateBatch")
    public void mcPaymentFull3DSRefundPart() {
        Document requestRevDoc = reversal(requestDoc, responseDoc, -0.5);
        Document responseRevDoc = sendRequest(URL, requestRevDoc);

        System.out.println("--REFUND--\nRequest:\n" + printRequest(requestRevDoc));
        System.out.println("Response:\n" + printResponse(responseRevDoc));

        int tranRefundId = getTranIdByOrder(getElementFromDocument(requestRevDoc, "OrderID"));

        assertEquals(getElementFromDocument(responseRevDoc, "TranCode"), "000","TranCode" );
        assertEquals(getElementFromDocument(responseRevDoc, "Rrn").length(), 12, "Rrn");

        assertEquals(getValueFromTRAN(tranRefundId, "RevFlag"), "0", "RevFlag");
        assertEquals(getValueFromTRAN(tranRefundId, "ECI"), getValueFromTRAN(tranId, "ECI"), "ECI");
        assertEquals(getValueFromTRAN(tranRefundId, "PAResStatus"), "Y", "PAResStatus");
        assertEquals(getValueFromTRAN(tranRefundId, "PA_ECI"), "02", "PA_ECI");
        assertEquals(getValueFromTRAN(tranRefundId, "CVResult"), "P2", "CVResult");

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
