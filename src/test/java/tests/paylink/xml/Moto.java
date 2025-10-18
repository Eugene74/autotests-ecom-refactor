package tests.paylink.xml;

import methods.RequestSender;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;

import static jdbc.JDBCMethods.*;
import static methods.DocumentTools.*;
import static methods.PaylinkRequests.payment;
import static org.testng.Assert.assertEquals;

public class Moto extends BaseTest {

    @BeforeClass
    public void setStatusFilter() {
        setMerchantFilter(motoFiltr, "1");
        System.out.println("SET NEW STATUS OF FILTER MOTO: TRUE");
    }

    @Test
    public void PaymentMoto() {
        Document requestDoc = payment(cardMC, "MOTO", merchant_AVAL, terminal_AVAL);
        Document responseDoc = RequestSender.sendRequest(URL, requestDoc);

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
        assertEquals(getValueFromTRAN(tranId, "ECI"), "01", "ECI");
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

    @AfterClass
    public void setDefaultFilter() {
        setMerchantFilter(motoFiltr, "0");
        System.out.println("SET NEW STATUS OF FILTER MOTO: FALSE");
    }
}
