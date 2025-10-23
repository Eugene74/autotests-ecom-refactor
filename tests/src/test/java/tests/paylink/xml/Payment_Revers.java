package tests.paylink.xml;

import com.ecom.api.type.FieldsNFile;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.context.TransactionContext;

import java.util.HashMap;
import java.util.Map;

import static com.ecom.api.type.FieldsNFile.*;
import static com.ecom.core.config.CardConfig.cardMC07;
import static com.ecom.core.config.CardConfig.cardVISA;
import static com.ecom.core.config.EnvData.URL;
import static com.ecom.core.config.EnvData.merchant_AVAL;
import static com.ecom.core.config.EnvData.terminal_AVAL;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.payment;
import static com.ecom.tests.support.PaylinkRequests.reversal;
import static com.ecom.tests.support.RequestSenderRest.sendRequest;
import static org.testng.Assert.assertEquals;

public class Payment_Revers {
    private static Document requestDoc;
    private static Document responseDoc;
    private static int tranId;

    @Test
    public void Payment() {
        requestDoc = payment(cardVISA, "payment+reversal", merchant_AVAL, terminal_AVAL);
        responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));
        String approvalCode = getElementFromDocument(responseDoc, "ApprovalCode");
        String rrn = getElementFromDocument(responseDoc, "Rrn");

        assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000", "TranCode");
        assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M", "CVResult");
        assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
//        assertEquals(getElementFromDocument(responseDoc, "Fee"), "400", "Fee");
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
            System.out.print("TEST FAILED");
        }

        addDataToCheckTransactionNFile(requestDoc, tranId, approvalCode, rrn);
    }

    @Test(dependsOnMethods = "Payment")
    public void PaymentRevers() {
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
            System.out.print("TEST FAILED");
        }

        addDataToCheckTransactionReversalNFile(requestRevDoc, responseRevDoc, tranReversId);
    }

    public void addDataToCheckTransactionNFile(Document requestDoc, int tranID, String approvalCode, String rrn) {
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
        checkTransaction.put(Amount, fillFieldValue(getElementFromDocument(requestDoc, "TotalAmount"), 12, '0'));
        checkTransaction.put(Currency, "UAH");
        checkTransaction.put(Terminal, "P");
        checkTransaction.put(Term_nr, terminal_AVAL);
        checkTransaction.put(MerchantCode, fillFieldValue(merchant_AVAL, 15, ' '));
        checkTransaction.put(MOTO_ECI_IND, "7");
        checkTransaction.put(FLD_123_1, "M");
        checkTransaction.put(EPI_42_48_FULL, "210");

        TransactionContext.ECOM_TRAN_PAYMENT_FIELDS.add(checkTransaction);
    }

    public void addDataToCheckTransactionReversalNFile(Document requestRevDoc, Document responseRevDoc, int tranID) {
        Map<FieldsNFile, String> checkTransactionRefund = new HashMap<>();
        checkTransactionRefund.put(TestName, "MasterCardPaymentReversal");

        checkTransactionRefund.put(Merchant, merchant_AVAL);
        checkTransactionRefund.put(Card, fillFieldValue(cardMC07[0], 19, ' '));
        checkTransactionRefund.put(Exp_date, cardMC07[4]);
        checkTransactionRefund.put(Tran_type, "25");
        checkTransactionRefund.put(Appr_code, getElementFromDocument(responseDoc, "ApprovalCode"));
        checkTransactionRefund.put(Appr_src, "1");
        checkTransactionRefund.put(Stan, getValueFromTRAN(tranID, "STAN"));
        checkTransactionRefund.put(Ref_number, getElementFromDocument(responseRevDoc, "Rrn"));
        checkTransactionRefund.put(Amount, fillFieldValue(getElementFromDocument(requestRevDoc, "RefundAmount"), 12, '0'));
        checkTransactionRefund.put(Currency, "UAH");
        checkTransactionRefund.put(Terminal, "P");
        checkTransactionRefund.put(Term_nr, terminal_AVAL);
        checkTransactionRefund.put(MerchantCode, fillFieldValue(merchant_AVAL, 15, ' '));
        checkTransactionRefund.put(MOTO_ECI_IND, "7");
        checkTransactionRefund.put(FLD_123_1, " ");
        checkTransactionRefund.put(EPI_42_48_FULL, "210");

        TransactionContext.ECOM_TRAN_PAYMENT_FIELDS.add(checkTransactionRefund);
    }
}

