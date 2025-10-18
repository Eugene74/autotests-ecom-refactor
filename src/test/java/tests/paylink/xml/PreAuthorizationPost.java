package tests.paylink.xml;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;

import static jdbc.JDBCMethods.getTranIdByOrder;
import static jdbc.JDBCMethods.getValueFromTRAN;
import static methods.DocumentTools.*;
import static methods.PaylinkRequests.paymentPostAuth;
import static methods.PaylinkRequests.paymentPreAuth;
import static methods.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;

public class PreAuthorizationPost extends BaseTest {
    private static Document requestDoc;
    private static Document responseDoc;
    private static int tranId;

    @Test
    public void PreAuthorization() {
        requestDoc = paymentPreAuth(cardMC, "preAuthorization_post", merchant_AVAL, terminal_AVAL);
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
    public void PreAuthorizationPost() {
        Document requestPostAuth = paymentPostAuth(requestDoc, responseDoc, "postAuthorization", 0);
        Document responsePostAuth = sendRequest(URL, requestPostAuth);

        System.out.println("--Post Authorization--\nRequest:\n" + printRequest(requestPostAuth));
        System.out.println("Response:\n" + printResponse(responsePostAuth));

        int tranPostId = getTranIdByOrder(getElementFromDocument(requestPostAuth, "OrderID"));

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
}
