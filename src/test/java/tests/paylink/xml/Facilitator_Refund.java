package tests.paylink.xml;

import jdbc.JDBCMethods;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;

import static jdbc.JDBCMethods.getTranIdByOrder;
import static jdbc.JDBCMethods.getValueFromTRAN;
import static methods.DocumentTools.*;
import static methods.PaylinkRequests.*;
import static methods.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;


public class Facilitator_Refund extends BaseTest {
    private static Document requestDoc;
    private static Document responseDoc;
    private static int tranId;

    @Test
    public void FacilitatorPay(){
        requestDoc = payment(cardMC, "ref", merchantIDFacil_AVAL, terminalIDFacil_AVAL);
        responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest\n" + printRequest(requestDoc));
        System.out.println("Response\n" + printResponse(responseDoc));

        tranId = JDBCMethods.getTranIdByOrder(getElementFromDocument (requestDoc,"OrderID"));

        assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000","TranCode" );
        assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M","CVResult" );
        assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
        assertEquals(getElementFromDocument(responseDoc, "Rrn").length(), 12, "Rrn");
        assertEquals(getElementFromDocument(responseDoc, "ApprovalCode").length(), 6,"ApprovalCode" );

        assertEquals(getValueFromTRAN(tranId, "ECI"), "07","ECI" );

        System.out.println("Verified:");
        String [] verifiedResponse = {"TranCode", "CVResult", "HostCode", "Rrn", "ApprovalCode"};
        String [] verifiedResponseFromDB = {"ECI"};
        try{
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataFromDB(tranId, verifiedResponseFromDB);}
        catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @Test(dependsOnMethods = "FacilitatorPay")
    public void generateBatch(){
        JDBCMethods.setNewTranTime(tranId);
        System.out.println("--TRAN TIME was Updated--\n");
        paylinkCloseDayForApi (idFacilAVAL);
        assertFalse(getValueFromTRAN(tranId, "Batch").isEmpty(), "BATCH");
    }

    @Test(dependsOnMethods="generateBatch")
    public void FacilitatorRefund() {
        Document requestRevDoc = reversal(requestDoc, responseDoc, 0);
        Document responseRevDoc = sendRequest(URL, requestRevDoc);

        System.out.println("--REFUND--\nRequest:\n" + printRequest(requestRevDoc));
        System.out.println("Response:\n" + printResponse(responseRevDoc));

        int tranRefundId = getTranIdByOrder(getElementFromDocument(requestRevDoc, "OrderID"));

        assertEquals(getElementFromDocument(responseRevDoc, "TranCode"), "000","TranCode" );
        assertEquals(getElementFromDocument(responseRevDoc, "Rrn").length(), 12, "Rrn");
        assertEquals(getValueFromTRAN(tranRefundId, "RevFlag"), "0", "RevFlag");
        assertEquals(getValueFromTRAN(tranRefundId, "ECI"), getValueFromTRAN(tranId, "ECI"), "ECI");
        assertEquals(getValueFromTRAN(tranRefundId, "CVResult"), "M", "CVResult");

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
}
