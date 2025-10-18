package tests.paylink.xml;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;

import static com.ecom.db.JDBCMethods.*;
import static methods.DocumentTools.*;
import static methods.PaylinkRequests.getInstPlansRequest;
import static methods.PaylinkRequests.paymentLocalInstallment;
import static methods.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;
import static com.ecom.api.type.Attributes.ALLOW_INSTALLMENT;
import static com.ecom.api.type.Attributes.ALLOW_PAYMENT_WITHOUT_3DS;

public class LocalInstallment extends BaseTest {
    private static Document requestDoc;
    private static Document responseDoc;
    private static String instPlanParamId;
    private static String numberOfPay;
    private static String feeMonth;
    private static String interestRate;
    private static String checkValue;
    private static String subsequentAmount;


    @BeforeClass
    public void setStatusAttr() {
        setMerchantAtt(id_AVAL, ALLOW_INSTALLMENT, "true");
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "true");
        System.out.println("ALLOW_INSTALLMENT: TRUE");
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
    }

    @Test
    public void chooseInstPlan() {
        requestDoc = getInstPlansRequest(cardMC07, "100000", merchant_AVAL, terminal_AVAL);
        responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        instPlanParamId = getElementFromDocument(responseDoc, "instPlanParamId");
        numberOfPay = getElementFromDocument(responseDoc, "numberOfPay");
        feeMonth = getElementFromDocument(responseDoc, "feeMonth");
        interestRate = getElementFromDocument(responseDoc, "interestRate");
        checkValue = getElementFromDocument(responseDoc, "checkValue");
        subsequentAmount = getElementFromDocument(responseDoc, "subsequentAmount");

        assertNotEquals(instPlanParamId, null, "instPlanParamId");
        assertNotEquals(numberOfPay, null, "numberOfPay");
        assertNotEquals(feeMonth, null, "feeMonth");
        assertNotEquals(interestRate, null, "interestRate");
        assertNotEquals(checkValue, null, "CheckValue");

        System.out.println("Verified:");
        String[] verifiedResponse = {"instPlanParamId", "numberOfPay", "feeMonth", "interestRate", "checkValue"};
        try {
            verifiedDataFromResponse(responseDoc, verifiedResponse);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @Test(dependsOnMethods = "chooseInstPlan")
    public void payLocalInstallmentChoice() {
        Document requestInstChoose = paymentLocalInstallment(cardMCInst, "local installment", merchant_AVAL, terminal_AVAL, subsequentAmount, instPlanParamId, numberOfPay, feeMonth, interestRate, checkValue);
        Document responseInstChoose = sendRequest(URL, requestInstChoose);

        System.out.println("--INSTALLMENT--\nRequest:\n" + printRequest(requestInstChoose));
        System.out.println("Response:\n" + printResponse(responseInstChoose));

        int tranId = getTranIdByOrder(getElementFromDocument(requestInstChoose, "OrderID"));
        String approvalCode = getElementFromDocument(responseInstChoose, "ApprovalCode");
        String rrn = getElementFromDocument(responseInstChoose, "Rrn");

        assertEquals(getElementFromDocument(responseInstChoose, "CVResult"), "M", "CVResult");
        assertEquals(getElementFromDocument(responseInstChoose, "HostCode"), "000", "HostCode");
        assertEquals(approvalCode.length(), 6, "ApprovalCode");
        assertEquals(rrn.length(), 12, "Rrn");
        assertEquals(getValueFromTRAN(tranId, "ECI"), "07", "ECI");

        System.out.println("Verified:");
        String[] verifiedResponse = {"TranCode", "CVResult", "HostCode", "Rrn", "ApprovalCode"};
        String[] verifiedResponseFromDB = {"ECI"};
        try {
            verifiedDataFromResponse(responseInstChoose, verifiedResponse);
            verifiedDataFromDB(tranId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @AfterClass
    public void setDefaultAttr() {
        setMerchantAtt(id_AVAL, ALLOW_INSTALLMENT, "false");
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "false");
        System.out.println("ALLOW_INSTALLMENT: FALSE");
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: FALSE");
    }
}