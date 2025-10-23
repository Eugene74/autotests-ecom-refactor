package tests.paylink.xml;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import static com.ecom.api.type.Attributes.ALLOW_MASTERPASS;
import static com.ecom.core.config.CardConfig.cardMC;
import static com.ecom.core.config.EnvData.URL;
import static com.ecom.core.config.EnvData.id_AVAL;
import static com.ecom.core.config.EnvData.merchant_AVAL;
import static com.ecom.core.config.EnvData.terminal_AVAL;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.db.JDBCMethods.setMerchantAtt;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.paymentMasterpass;
import static com.ecom.tests.support.RequestSenderRest.sendRequest;
import static org.testng.Assert.assertEquals;

public class Masterpass {

    @BeforeClass
    public void setStatusAttr() {
        setMerchantAtt(id_AVAL, ALLOW_MASTERPASS, "true");
        System.out.println("ALLOW_MASTERPASS: TRUE");
    }

    @Test
    public void masterpass() {
        Document requestDoc = paymentMasterpass(cardMC, "masterpass", merchant_AVAL, terminal_AVAL);
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
        assertEquals(getValueFromTRAN(tranId, "ECI"), "07", "ECI");
        assertEquals(getValueFromTRAN(tranId, "CVResult"), "M", "CVResult");
        //assertNull(getValueFromTRAN(tranId, "CVResult"), "CVResult");
        //assertFalse("PSTranID", getValueFromTRAN(tranId, "PSTranID").isEmpty());

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

    @AfterClass
    public void setDefaultAttr() {
        setMerchantAtt(id_AVAL, ALLOW_MASTERPASS, "false");
        System.out.println("SET NEW STATUS - ALLOW_MASTERPASS: FALSE");
    }
}
