package tests.paylink.xml;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;

import static com.ecom.db.JDBCMethods.*;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.*;
import static com.ecom.tests.support.RequestSender.sendRequest;
import static org.testng.Assert.*;
import static com.ecom.api.type.Attributes.ALLOW_MASTERCARD_INSTALLMENT;

public class MasterInstallment_Revers extends BaseTest {
    private static Document requestDoc;
    private static Document responseDoc;
    private static int tranId;

    @BeforeClass
    public void setStatusAttr() {
        setMerchantAtt(id_AVAL, ALLOW_MASTERCARD_INSTALLMENT, "true");
        System.out.println("ALLOW_MASTERCARD_INSTALLMENT: TRUE");
    }

    @Test
    public void payInstallment() {
        requestDoc = paymentAmount(cardMCInst, "100000", "MasterCardInstallment+Reversal", merchant_AVAL, terminal_AVAL);
        responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));
//        from Response
        assertEquals(getElementFromDocument(responseDoc, "TranCode"), "602", "TranCode");
        assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
        assertEquals(getElementFromDocument(responseDoc, "Rrn").length(), 12, "Rrn");
        assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M", "CVResult");
        assertEquals(getElementFromDocument(responseDoc, "ApprovalCode").length(), 6, "ApprovalCode");
//        from DB
        assertEquals(getValueFromTRAN(tranId, "ECI"), "07", "ECI");
        assertFalse(getValueFromTRAN(tranId, "PSTranID").isEmpty(), "PSTranID");

        System.out.println("Verified:");
        String[] verifiedResponse = {"TranCode", "HostCode", "Rrn", "ApprovalCode", "CVResult"};
        String[] verifiedResponseFromDB = {"ECI", "PSTranID"};
        try {
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataFromDB(tranId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @Test(dependsOnMethods = "payInstallment")
    public void payInstallmentChoice() {
        Document requestInstChoose = paymentInstallChoice(requestDoc, responseDoc);
        Document responseInstChoose = sendRequest(URL, requestInstChoose);

        System.out.println("--INSTALLMENT--\nRequest:\n" + printRequest(requestInstChoose));
        System.out.println("Response:\n" + printResponse(responseInstChoose));

        assertEquals(getElementFromDocument(responseInstChoose, "TranCode"), "000", "TranCode");
        assertEquals(getValueFromTRAN(tranId, "AddendumData"), "1", "AddendumData");
        assertEquals(getValueFromTRAN(tranId, "Installment"), "1", "Installment");
        assertTrue(getAddendumData(tranId, "CONVERSION_RATE_DATE"));
        assertTrue(getInstallmentData(tranId));

        System.out.println("Verified:");
        try {
            verifiedDataFromResponse(responseDoc, new String[]{"TranCode"});
            verifiedDataFromDB(tranId, new String[]{"ECI", "AddendumData", "Installment"});
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @Test(dependsOnMethods = "payInstallmentChoice")
    public void payInstallmentRevers() {
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
        setMerchantAtt(id_AVAL, ALLOW_MASTERCARD_INSTALLMENT, "false");
        System.out.println("SET NEW STATUS - ALLOW_MASTERCARD_INSTALLMENT: FALSE");
    }
}

