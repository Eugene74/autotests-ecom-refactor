package tests.paylink.xml;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;

import static com.ecom.db.JDBCMethods.*;
import static methods.DocumentTools.*;
import static methods.PaylinkRequests.settlementRefund;
import static methods.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static com.ecom.type.Attributes.ALLOW_REF3;
import static com.ecom.type.Attributes.ALLOW_SETTLEMENT_REFUND;

public class SettlementRefund_Aval extends BaseTest {

    @BeforeClass
    public void setStatusAttr() {
        setMerchantAtt(id_AVAL, ALLOW_SETTLEMENT_REFUND, "true");
        setMerchantAtt(id_AVAL, ALLOW_REF3, "true");
        System.out.println("ALLOW_REF3 AND ALLOW_SETTLEMENT_REFUND: TRUE");
    }

    @Test
    public void SettlementRefund() {
        Document requestDoc = settlementRefund(cardMC, "settlement ref", merchant_AVAL, terminal_AVAL);
        Document responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        int tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));

        assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000", "TranCode");
        assertEquals(getValueFromTRAN(tranId, "ECI"), "07", "ECI");
        assertNull(getValueFromTRAN(tranId, "CVResult"), "CVResult");
        assertEquals(getValueFromTRAN(tranId, "POS_CODE"), "08", "POS_CODE");

        System.out.println("Verified:");
        String[] verifiedResponse = {"TranCode"};
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
        setMerchantAtt(id_AVAL, ALLOW_SETTLEMENT_REFUND, "false");
        setMerchantAtt(id_AVAL, ALLOW_REF3, "false");
        System.out.println("ALLOW_REF3 AND ALLOW_SETTLEMENT_REFUND: FALSE");
    }
}
