package tests.paylink.xml;

import jdbc.JDBCMethods;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;

import static jdbc.JDBCMethods.*;
import static methods.DocumentTools.*;
import static methods.PaylinkRequests.*;
import static methods.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class Moto_Refund extends BaseTest {
    private Document requestDoc;
    private Document responseDoc;
    private static int tranId;

    @BeforeClass
    public void setStatusFilter() {
        setMerchantFilter(motoFiltr, "1");
        System.out.println("SET NEW STATUS OF FILTER MOTO: TRUE");
    }

    @Test
    public void PaymentMoto() {
        requestDoc = payment(cardMC07, "MOTO+refund", merchant_AVAL, terminal_AVAL);
        responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));

//        from Response
        assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000", "TranCode");
        assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M", "CVResult");
        assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
        assertEquals(getElementFromDocument(responseDoc, "Rrn").length(), 12, "Rrn");
        assertEquals(getElementFromDocument(responseDoc, "ApprovalCode").length(), 6, "ApprovalCode");
//        from DB
        //assertEquals(getValueFromTRAN(tranId, "ECI"), "01", "ECI");
        assertEquals(getValueFromTRAN(tranId, "ECI"), "01", "ECI");
        //assertEquals(getValueFromTRAN(tranId, "POS_CODE"), "08", "POS_CODE");
        assertEquals(getValueFromTRAN(tranId, "POS_CODE"), "08", "POS_CODE");

        System.out.println("Verified:");
        String[] verifiedResponse = {"TranCode", "CVResult", "HostCode", "Rrn", "ApprovalCode"};
        String[] verifiedResponseFromDB = {"ECI", "POS_CODE"};
        try {
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataFromDB(tranId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @Test(dependsOnMethods = "PaymentMoto")
    public void generateBatch() {
        JDBCMethods.setNewTranTime(tranId);
        System.out.println("--TRAN TIME was Updated--\n");
        paylinkCloseDayForApi(id_AVAL);
        assertFalse(getValueFromTRAN(tranId, "Batch").isEmpty(), "BATCH");
    }

    @Test(dependsOnMethods = "generateBatch")
    public void PaymentMotoRefundPart() {
        Document requestRevDoc = reversal(requestDoc, responseDoc, 0);
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

    @AfterClass
    public void setDefaultAttr() {
        setMerchantFilter(motoFiltr, "0");
        System.out.println("SET NEW STATUS OF FILTER MOTO: FALSE");
    }
}
