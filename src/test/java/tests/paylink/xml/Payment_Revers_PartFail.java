package tests.paylink.xml;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;

import static jdbc.JDBCMethods.getTranIdByOrder;
import static jdbc.JDBCMethods.getValueFromTRAN;
import static methods.DocumentTools.*;
import static methods.PaylinkRequests.payment;
import static methods.PaylinkRequests.reversal;
import static methods.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;

public class Payment_Revers_PartFail extends BaseTest {
    private static Document requestDoc;
    private static Document responseDoc;

    @Test
    public void mcPayment() {
        requestDoc = payment(cardMC07, "MasterCardPayment+ReversalPartFail", merchant_AVAL, terminal_AVAL);
        responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        int tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));

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

    @Test(dependsOnMethods = "mcPayment")
    public void mcPaymentReversPartFail() {
        Document requestRevDoc = reversal(requestDoc, responseDoc, -0.5);
        Document responseRevDoc = sendRequest(URL, requestRevDoc);

        System.out.println("--REVERSAL--\nRequest:\n" + printRequest(requestRevDoc));
        System.out.println("Response:\n" + printResponse(responseRevDoc));

        assertEquals(getElementFromDocument(responseRevDoc, "TranCode"), "430", "TranCode");
        System.out.println("Verified:");
        String[] verifiedResponse = {"TranCode"};
        verifiedDataFromResponse(responseRevDoc, verifiedResponse);
    }
}

