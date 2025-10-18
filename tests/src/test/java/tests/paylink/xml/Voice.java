package tests.paylink.xml;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;

import static com.ecom.db.JDBCMethods.*;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.payment;
import static com.ecom.tests.support.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

public class Voice extends BaseTest {

    @BeforeClass
    public void setStatusFilter() {
        setMerchantFilter(voiceFiltr, "1");
        System.out.println("SET NEW STATUS OF FILTER VOICE: TRUE");
    }

    @Test
    public void mcPaymentVoice() {
        Document requestDoc = payment(cardMC, "voice", merchant_AVAL, terminal_AVAL);
        Document responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        int tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));

//        from Response
        assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000", "TranCode");
        assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
        assertEquals(getElementFromDocument(responseDoc, "Rrn").length(), 12, "Rrn");
        assertEquals(getElementFromDocument(responseDoc, "ApprovalCode").length(), 6, "ApprovalCode");

//        from DB
        assertNull(getValueFromTRAN(tranId, "ECI"), "ECI");
        assertNull(getValueFromTRAN(tranId, "CVResult"), "CVResult");
        assertEquals(getValueFromTRAN(tranId, "POS_CODE"), "00", "POS_CODE");

        System.out.println("Verified:");
        String[] verifiedResponse = {"TranCode", "HostCode", "Rrn", "ApprovalCode"};
        String[] verifiedResponseFromDB = {"ECI", "CVResult", "POS_CODE"};
        try {
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataFromDB(tranId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @AfterClass
    public void setDefaultAttr() {
        setMerchantFilter(voiceFiltr, "0");
        System.out.println("SET NEW STATUS OF FILTER VOICE: FALSE");
    }
}
