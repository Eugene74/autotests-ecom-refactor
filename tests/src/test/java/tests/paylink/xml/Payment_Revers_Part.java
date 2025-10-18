package tests.paylink.xml;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;
import com.ecom.api.type.FieldsNFile;

import java.util.HashMap;
import java.util.Map;

import static com.ecom.db.JDBCMethods.*;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.payment;
import static com.ecom.tests.support.PaylinkRequests.reversal;
import static com.ecom.tests.support.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;
import static com.ecom.api.type.Attributes.ALLOW_PARTIAL_REVERSAL;
import static com.ecom.api.type.FieldsNFile.*;

public class Payment_Revers_Part extends BaseTest {
    private static Document requestDoc;
    private static Document responseDoc;
    private static String approvalCode;
    private static String rrn;
    private static int tranId;

    @BeforeClass
    public void setStatusFilter() {
        setMerchantAtt(id_AVAL, ALLOW_PARTIAL_REVERSAL, "true");
        System.out.println("SET NEW STATUS - ALLOW_PARTIAL_REVERSAL: TRUE");
    }

    @Test
    public void mcPayment() {
        requestDoc = payment(cardMC07, "MasterCardPayment+ReversalPart", merchant_AVAL, terminal_AVAL);
        responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));
        approvalCode = getElementFromDocument(responseDoc, "ApprovalCode");
        rrn = getElementFromDocument(responseDoc, "Rrn");

        assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000", "TranCode");
        assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M", "CVResult");
        assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
        assertEquals(approvalCode.length(), 6, "ApprovalCode");
        assertEquals(rrn.length(), 12, "Rrn");
        assertEquals(getValueFromTRAN(tranId, "ECI"), "07", "ECI");
//        assertEquals(getValueFromTRAN(tranId, "FEE"), "400", "FEE");

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

    @Test(dependsOnMethods = "mcPayment")
    public void mcPaymentReversPart() {
        Document requestRevDoc = reversal(requestDoc, responseDoc, -0.5);
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

        int amount = Integer.parseInt(getElementFromDocument(requestDoc, "TotalAmount"));
        amount -= Integer.parseInt(getElementFromDocument(requestRevDoc, "RefundAmount"));

        addDataToCheckTransactionNFile(amount, tranId, approvalCode, rrn);
    }

    @AfterClass
    public void setDefaultAttr() {
        setMerchantAtt(id_AVAL, ALLOW_PARTIAL_REVERSAL, "false");
        System.out.println("SET NEW STATUS - ALLOW_PARTIAL_REVERSAL: FALSE");
    }

    public void addDataToCheckTransactionNFile(Integer amount, int tranID, String approvalCode, String rrn) {
        Map<FieldsNFile, String> checkTransaction = new HashMap<>();
        checkTransaction.put(TestName, "MasterCardPayment");

        checkTransaction.put(Merchant, merchant_AVAL);
        checkTransaction.put(Card, fillFieldValue(cardMC07[0], 19, ' '));
        checkTransaction.put(Exp_date, cardMC07[4]);
        checkTransaction.put(Tran_type, "05");
        checkTransaction.put(Appr_code, approvalCode);
        checkTransaction.put(Appr_src, "1");
        checkTransaction.put(Stan, getValueFromTRAN(tranID, "STAN"));
        checkTransaction.put(Ref_number, rrn);
        checkTransaction.put(Amount, fillFieldValue(amount.toString(), 12, '0'));
        checkTransaction.put(Currency, "UAH");
        checkTransaction.put(Terminal, "P");
        checkTransaction.put(Term_nr, terminal_AVAL);
        checkTransaction.put(MerchantCode, fillFieldValue(merchant_AVAL, 15, ' '));
        checkTransaction.put(MOTO_ECI_IND, "7");
        checkTransaction.put(FLD_123_1, "M");
        checkTransaction.put(EPI_42_48_FULL, "210");

        tranPaymentFields.add(checkTransaction);
    }
}

