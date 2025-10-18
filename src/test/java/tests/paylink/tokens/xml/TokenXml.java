package tests.paylink.tokens.xml;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;

import static com.ecom.db.JDBCMethods.*;
import static methods.DocumentTools.*;
import static methods.PaylinkRequests.payment;
import static methods.PaylinkRequests.paymentToken;
import static methods.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;
import static com.ecom.type.Attributes.ALLOW_SUPPORT_TOKEN;

public class TokenXml extends BaseTest {
    private String tokenId;
    private int tranId;

    @BeforeClass
    public void setParam() {
        setMerchantAtt(id_AVAL, ALLOW_SUPPORT_TOKEN, "true");
        System.out.println("ALLOW_SUPPORT_TOKEN: TRUE");
    }

    @Test
    public void Payment(){
        Document requestDoc = payment(cardVISA, "payment", merchant_AVAL, terminal_AVAL);
        Document responseDoc = sendRequest(URL, requestDoc);
        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response\n" + printResponse(responseDoc));
        tokenId = getElementFromDocument(responseDoc, "tokenId");

        tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));
        String approvalCode = getElementFromDocument(responseDoc, "ApprovalCode");
        String rrn = getElementFromDocument(responseDoc, "Rrn");

        assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000", "TranCode");
        assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M", "CVResult");
        assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
        assertEquals(approvalCode.length(), 6, "ApprovalCode");
        assertEquals(rrn.length(), 12, "Rrn");
        assertEquals(getValueFromTRAN(tranId, "ECI"), "07", "ECI");

        System.out.println("Verified:");
        String[] verifiedResponse = {"TranCode", "CVResult", "HostCode", "Rrn", "ApprovalCode"};
        String[] verifiedResponseFromDB = {"ECI"};
        try {
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataFromDB(tranId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @Test (dependsOnMethods = "Payment")
    public void TokenPay() {
        String[] token = new String[]{tokenId, cardVISA[3]};

        Document requestDoc = paymentToken(token, "token xml", merchant_AVAL, terminal_AVAL);
        Document responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response\n" + printResponse(responseDoc));

        int tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));
        String approvalCode = getElementFromDocument(responseDoc, "ApprovalCode");
        String rrn = getElementFromDocument(responseDoc, "Rrn");

        assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000", "TranCode");
        assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M", "CVResult");
        assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
        assertEquals(approvalCode.length(), 6, "ApprovalCode");
        assertEquals(rrn.length(), 12, "Rrn");
        assertEquals(getValueFromTRAN(tranId, "ECI"), "07", "ECI");

        System.out.println("Verified:");
        String[] verifiedResponse = {"TranCode", "CVResult", "HostCode", "Rrn", "ApprovalCode"};
        String[] verifiedResponseFromDB = {"ECI"};
        try {
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataFromDB(tranId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @AfterClass
    public void setDefaultParam() {
        setMerchantAtt(id_AVAL, ALLOW_SUPPORT_TOKEN, "false");
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: FALSE");
    }
}