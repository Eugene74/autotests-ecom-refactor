package tests.paylink.xml;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;

import static com.ecom.db.JDBCMethods.*;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.paymentAddendum;
import static com.ecom.tests.support.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static com.ecom.api.type.Attributes.ALLOW_AIRLINE_ADDENDUM_DATA;
import static com.ecom.api.type.Attributes.ALLOW_REF3;

public class PaymentFull3DSAddendumRef3 extends BaseTest {

    @BeforeClass
    public void setStatusAttr() {
        setMerchantAtt(id_AVAL, ALLOW_REF3, "true");
        setMerchantAtt(id_AVAL, ALLOW_AIRLINE_ADDENDUM_DATA, "true");
        System.out.println("SET NEW STATUS - ALLOW_REF3 AND ALLOW_AIRLINE_ADDENDUM_DATA: TRUE");
    }

    @Test
    public void mcPaymentFull3DSAddendum() {
        Document requestDoc = paymentAddendum(cardMC05, "MCFull3DS_AddendumDataRef3", merchant_AVAL, terminal_AVAL, "VISA");
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
        assertEquals(getValueFromTRAN(tranId, "AddendumData"), "1", "AddendumData");
        assertTrue(getAddendumData(tranId, "Ref3"));
        assertTrue(getAddendumData(tranId, "AirlineAddendumData"));
//        assertEquals(getValueFromTRAN(tranId, "FEE"), "400", "FEE");

        System.out.println("Verified:");
        String[] verifiedResponse = {"TranCode", "CVResult", "HostCode", "Rrn", "ApprovalCode"};
        String[] verifiedResponseFromDB = {"ECI", "PAResStatus", "PA_ECI", "AddendumData"};
        try {
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataFromDB(tranId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @AfterClass
    public void setDefaultAttr() {
        setMerchantAtt(id_AVAL, ALLOW_REF3, "false");
        setMerchantAtt(id_AVAL, ALLOW_AIRLINE_ADDENDUM_DATA, "false");
        System.out.println("SET NEW STATUS - ALLOW_REF3 AND ALLOW_AIRLINE_ADDENDUM_DATA: FALSE");
    }
}
