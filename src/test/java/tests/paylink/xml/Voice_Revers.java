package tests.paylink.xml;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;

import static jdbc.JDBCMethods.*;
import static methods.DocumentTools.*;
import static methods.PaylinkRequests.payment;
import static methods.PaylinkRequests.reversal;
import static methods.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;


public class Voice_Revers extends BaseTest {
    private static Document requestDoc;
    private static Document responseDoc;
    private static int tranId;

    @BeforeClass
    public void setStatusFilter() {
        setMerchantFilter(voiceFiltr, "1");
        System.out.println("SET NEW STATUS OF FILTER VOICE: TRUE");
    }

    @Test
    public void mcPaymentVoice() {
        requestDoc = payment(cardMC07, "MCVoice+Reversal", merchant_AVAL, terminal_AVAL);
        responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));

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

    @Test(dependsOnMethods = "mcPaymentVoice")
    public void mcPaymentVoiceRevers() {
        Document requestRevDoc = reversal(requestDoc, responseDoc, 0);
        Document responseRevDoc = sendRequest(URL, requestRevDoc);

        System.out.println("--REVERSAL--\nRequest:\n" + printRequest(requestRevDoc));
        System.out.println("Response:\n" + printResponse(responseRevDoc));

        int tranReversId = getTranIdByOrder(getElementFromDocument(requestRevDoc, "OrderID"));

        assertEquals(getElementFromDocument(responseRevDoc, "TranCode"), "000", "TranCode");
        assertEquals(getElementFromDocument(responseRevDoc, "Rrn").length(), 12, "Rrn");
        assertEquals(getValueFromTRAN(tranReversId, "RevFlag"), "1", "RevFlag");
        assertEquals(getValueFromTRAN(tranReversId, "ECI"), getValueFromTRAN(tranId, "ECI"), "ECI");

        System.out.println("Verified:");
        String[] verifiedResponse = {"TranCode", "Rrn"};
        String[] verifiedResponseFromDB = {"ECI", "RevFlag"};
        try {
            verifiedDataFromResponse(responseRevDoc, verifiedResponse);
            verifiedDataFromDB(tranReversId, verifiedResponseFromDB);
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
