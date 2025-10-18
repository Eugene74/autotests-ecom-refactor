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


public class PreAuthPostMoreAmountRefund extends BaseTest {
    private Document requestDoc;
    private Document responseDoc;
    private Document requestPostAuth;
    private static int tranId;
    private static int tranPostId;

    @Test
    public void PreAuthorization() {
        requestDoc = paymentPreAuth(cardMC, "pre post more ref xml", merchant_AVAL, terminal_AVAL);
        responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--Pre Authorization--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));

//        from Response
        assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000", "TranCode");
        assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M", "CVResult");
        assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
        assertEquals(getElementFromDocument(responseDoc, "Rrn").length(), 12, "Rrn");
        assertEquals(getElementFromDocument(responseDoc, "ApprovalCode").length(), 6, "ApprovalCode");
//        from DB
        assertEquals(getValueFromTRAN(tranId, "ECI"), "07", "ECI");
//        assertEquals(getValueFromTRAN(tranId, "FEE"), "400", "FEE");

        System.out.println("Verified:");
        String[] verifiedResponse = {"TranCode", "CVResult", "HostCode", "Rrn", "ApprovalCode"};
        String[] verifiedResponseFromDB = {"ECI"};
        try {
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataFromDB(tranId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @Test(dependsOnMethods = "PreAuthorization")
    public void PreAuthorizationPostMoreAmount() {
        requestPostAuth = paymentPostAuth(requestDoc, responseDoc, "post", 0.2);
        Document responsePostAuth = sendRequest(URL, requestPostAuth);

        System.out.println("--Post Authorization--\nRequest:\n" + printRequest(requestPostAuth));
        System.out.println("Response:\n" + printResponse(responsePostAuth));

        tranPostId = getTranIdByOrder(getElementFromDocument(requestPostAuth, "OrderID"));

        assertEquals(getElementFromDocument(responsePostAuth, "TranCode"), "000", "TranCode");
        assertEquals(getValueFromTRAN(tranPostId, "ECI"), getValueFromTRAN(tranId, "ECI"), "ECI");

        System.out.println("Verified:");
        String[] verifiedResponse = {"TranCode"};
        String[] verifiedResponseFromDB = {"ECI"};
        try {
            verifiedDataFromResponse(responsePostAuth, verifiedResponse);
            verifiedDataFromDB(tranPostId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @Test(dependsOnMethods = "PreAuthorizationPostMoreAmount")
    public void generateBatch() {
        JDBCMethods.setNewTranTime(tranPostId);
        System.out.println("--TRAN TIME was Updated--\n");
        paylinkCloseDayForApi(id_AVAL);
        assertFalse(getValueFromTRAN(tranPostId, "Batch").isEmpty(), "BATCH");
    }

    @Test(dependsOnMethods = "generateBatch")
    public void PreAuthorizationPostMoreAmountRefund() {
        Document requestRevDoc = reversal(requestPostAuth, responseDoc, 0);
        Document responseRevDoc = sendRequest(URL, requestRevDoc);

        System.out.println("--REFUND--\nRequest:\n" + printRequest(requestRevDoc));
        System.out.println("Response:\n" + printResponse(responseRevDoc));

        int tranRefundId = getTranIdByOrder(getElementFromDocument(requestRevDoc, "OrderID"));

        assertEquals(getElementFromDocument(responseRevDoc, "TranCode"), "000", "TranCode");
        assertEquals(getElementFromDocument(responseRevDoc, "Rrn").length(), 12, "Rrn");
        assertEquals(getValueFromTRAN(tranRefundId, "RevFlag"), "0", "RevFlag");
        assertEquals(getValueFromTRAN(tranRefundId, "ECI"), getValueFromTRAN(tranId, "ECI"), "ECI");
        assertEquals(getValueFromTRAN(tranRefundId, "CVResult"), "M", "CVResult");

        System.out.println("Verified:");
        String[] verifiedResponse = {"TranCode", "Rrn"};
        String[] verifiedResponseFromDB = {"ECI", "RevFlag", "CVResult"};
        try {
            verifiedDataFromResponse(responseRevDoc, verifiedResponse);
            verifiedDataFromDB(tranRefundId, verifiedResponseFromDB);
            verifiedDataFromDB(tranId, new String[]{"Batch"});
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }
}
