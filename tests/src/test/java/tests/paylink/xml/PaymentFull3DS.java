package tests.paylink.xml;

import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;

import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.paymentPares;
import static com.ecom.tests.support.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;

public class PaymentFull3DS extends BaseTest {

    @Test
    public void mcPaymentFull3DS() {
        Document requestDoc = paymentPares(cardMC05, "MasterCardPaymentFull3DS", merchant_AVAL, terminal_AVAL);
        Document responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        int tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));

//        from Response
        assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000", "TranCode");
        assertEquals(getElementFromDocument(responseDoc, "CVResult"), "P2", "CVResult");
        assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
        assertEquals(getElementFromDocument(responseDoc, "Rrn").length(), 12, "Rrn");
        assertEquals(getElementFromDocument(responseDoc, "ApprovalCode").length(), 6, "ApprovalCode");
//        from DB
        assertEquals(getValueFromTRAN(tranId, "ECI"), "02", "ECI");
        assertEquals(getValueFromTRAN(tranId, "PAResStatus"), "Y", "PAResStatus");
        assertEquals(getValueFromTRAN(tranId, "PA_ECI"), "02", "PA_ECI");
//        assertEquals(getValueFromTRAN(tranId, "FEE"), "400", "FEE");

        System.out.println("Verified:");
        String[] verifiedResponse = {"TranCode", "CVResult", "HostCode", "Rrn", "ApprovalCode"};
        String[] verifiedResponseFromDB = {"ECI", "PAResStatus", "PA_ECI"};
        try {
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataFromDB(tranId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }
}
