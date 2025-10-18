package tests.paylink.xml;

import com.ecom.db.JDBCMethods;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;

import static com.ecom.db.JDBCMethods.*;
import static methods.DocumentTools.*;
import static methods.PaylinkRequests.*;
import static methods.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static com.ecom.api.type.Attributes.ALLOW_MASTERPASS;


public class Masterpass_Refund extends BaseTest {
    private Document requestDoc;
    private Document responseDoc;
    private static int tranId;

    @BeforeClass
    public void setStatusAttr() {
        setMerchantAtt(id_AVAL, ALLOW_MASTERPASS, "true");
        System.out.println("ALLOW_MASTERPASS: TRUE");
    }

    @Test
    public void MasterpassPay() {
        requestDoc = paymentMasterpass(cardMC, "masterpass", merchant_AVAL, terminal_AVAL);
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
        assertEquals(getValueFromTRAN(tranId, "ECI"), "07", "ECI");
        assertEquals(getValueFromTRAN(tranId, "CVResult"), "M", "CVResult");
        //assertNull(getValueFromTRAN(tranId, "CVResult"), "CVResult");
        //assertFalse(getValueFromTRAN(tranId, "PSTranID").isEmpty(), "PSTranID");

        System.out.println("Verified:");
        String[] verifiedResponse = {"TranCode", "HostCode", "Rrn", "ApprovalCode"};
        String[] verifiedResponseFromDB = {"ECI", "CVResult", "PSTranID"};
        try {
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataFromDB(tranId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @Test(dependsOnMethods = "MasterpassPay")
    public void generateBatch() {
        JDBCMethods.setNewTranTime(tranId);
        System.out.println("--TRAN TIME was Updated--\n");
        paylinkCloseDayForApi(id_AVAL);
        assertFalse(getValueFromTRAN(tranId, "Batch").isEmpty(), "BATCH");
    }

    @Test(dependsOnMethods = "generateBatch")
    public void MasterpassRefund() {
        Document requestRevDoc = reversal(requestDoc, responseDoc, 0);
        Document responseRevDoc = sendRequest(URL, requestRevDoc);

        System.out.println("--REFUND--\nRequest:\n" + printRequest(requestRevDoc));
        System.out.println("Response:\n" + printResponse(responseRevDoc));

        int tranRefundId = getTranIdByOrder(getElementFromDocument(requestRevDoc, "OrderID"));

        assertEquals(getElementFromDocument(responseRevDoc, "TranCode"), "000", "TranCode");
        assertEquals(getElementFromDocument(responseRevDoc, "Rrn").length(), 12, "Rrn");
        assertEquals(getValueFromTRAN(tranRefundId, "RevFlag"), "0", "RevFlag");
        assertEquals(getValueFromTRAN(tranId, "CVResult"), "M", "CVResult");
        //assertNull(getValueFromTRAN(tranRefundId, "CVResult"), "CVResult");
        assertEquals(getValueFromTRAN(tranRefundId, "ECI"), getValueFromTRAN(tranId, "ECI"), "ECI");

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
        setMerchantAtt(id_AVAL, ALLOW_MASTERPASS, "false");
        System.out.println("SET NEW STATUS - ALLOW_MASTERPASS: FALSE");
    }
}
