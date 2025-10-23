package tests.paylink.xml;

import com.ecom.api.type.FieldsNFile;
import com.ecom.db.JDBCMethods;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.context.TransactionContext;

import java.util.HashMap;
import java.util.Map;

import static com.ecom.api.type.FieldsNFile.*;
import static com.ecom.core.config.CardConfig.cardMC;
import static com.ecom.core.config.CardConfig.cardMC07;
import static com.ecom.core.config.EnvData.URL;
import static com.ecom.core.config.EnvData.id_AVAL;
import static com.ecom.core.config.EnvData.merchant_AVAL;
import static com.ecom.core.config.EnvData.terminal_AVAL;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.paylinkCloseDayForApi;
import static com.ecom.tests.support.PaylinkRequests.payment;
import static com.ecom.tests.support.PaylinkRequests.reversal;
import static com.ecom.tests.support.RequestSenderRest.sendRequest;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;

public class Payment_Refund {
    private Document requestDoc;
    private Document responseDoc;
    private static int tranId;

    @Test
    public void Payment() {
        requestDoc = payment(cardMC, "payment+refund xml", merchant_AVAL, terminal_AVAL);
        responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        tranId = JDBCMethods.getTranIdByOrder(getElementFromDocument (requestDoc,"OrderID"));
        String approvalCode = getElementFromDocument(responseDoc, "ApprovalCode");
        String rrn = getElementFromDocument(responseDoc, "Rrn");

        assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000","TranCode" );
        assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M","CVResult" );
        assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
        assertEquals(approvalCode.length(), 6,"ApprovalCode" );
        assertEquals(rrn.length(), 12, "Rrn");
        assertEquals(getValueFromTRAN(tranId, "ECI"), "07","ECI" );

        System.out.println("Verified:");
        String [] verifiedResponse = {"TranCode", "CVResult", "HostCode", "Rrn", "ApprovalCode"};
        String [] verifiedResponseFromDB = {"ECI"};
        try{
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataFromDB(tranId, verifiedResponseFromDB);}
        catch (Exception e) {
            System.out.println("TEST FAILED");
        }

        addDataToCheckTransactionNFile(requestDoc, tranId, approvalCode, rrn);
    }

    @Test(dependsOnMethods = "Payment")
    public void generateBatch(){
        JDBCMethods.setNewTranTime(tranId);
        System.out.println("--TRAN TIME was Updated--\n");
        paylinkCloseDayForApi (id_AVAL);
        assertFalse(getValueFromTRAN(tranId, "Batch").isEmpty(), "BATCH");
    }

    @Test(dependsOnMethods = "generateBatch")
    public void PaymentRefund() {
        Document requestRevDoc = reversal(requestDoc, responseDoc, 0);
        Document responseRevDoc = sendRequest(URL, requestRevDoc);

        System.out.println("--REFUND--\nRequest:\n" + printRequest(requestRevDoc));
        System.out.println("Response:\n" + printResponse(responseRevDoc));

        int tranRefundId = getTranIdByOrder(getElementFromDocument(requestRevDoc, "OrderID"));

        assertEquals(getElementFromDocument(responseRevDoc, "TranCode"), "000","TranCode" );
        assertEquals(getElementFromDocument(responseRevDoc, "Rrn").length(), 12, "Rrn");
        assertEquals(getValueFromTRAN(tranRefundId, "RevFlag"), "0", "RevFlag");
        assertEquals(getValueFromTRAN(tranRefundId, "ECI"), getValueFromTRAN(tranId, "ECI"), "ECI");
        assertEquals(getValueFromTRAN(tranRefundId, "CVResult"), "M", "CVResult");

        System.out.println("Verified:");
        String [] verifiedResponse = {"TranCode", "Rrn"};
        String [] verifiedResponseFromDB = {"ECI", "RevFlag", "CVResult"};
        try{
            verifiedDataFromResponse(responseRevDoc, verifiedResponse);
            verifiedDataFromDB(tranRefundId, verifiedResponseFromDB);
            verifiedDataFromDB(tranId, new String[]{"Batch"});}
        catch (Exception e) {
            System.out.println("TEST FAILED");
        }

        addDataToCheckTransactionRefundNFile(requestRevDoc, responseRevDoc, tranRefundId);
    }

    public void addDataToCheckTransactionNFile(Document requestDoc, int tranID, String approvalCode,String rrn){
        Map<FieldsNFile, String> checkTransaction = new HashMap<>();
        checkTransaction.put(TestName, "payment");

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

    public void addDataToCheckTransactionRefundNFile(Document requestRevDoc, Document responseRevDoc, int tranID) {
        Map<FieldsNFile, String> checkTransactionRefund = new HashMap<>();
        checkTransactionRefund.put(TestName, "MasterCardPaymentRefund");

        checkTransactionRefund.put(Merchant, merchant_AVAL);
        checkTransactionRefund.put(Card, fillFieldValue(cardMC07[0], 19, ' '));
        checkTransactionRefund.put(Exp_date, cardMC07[4]);
        checkTransactionRefund.put(Tran_type, "06");
        checkTransactionRefund.put(Appr_code, "      ");
        checkTransactionRefund.put(Appr_src, "1");
        checkTransactionRefund.put(Stan, getValueFromTRAN(tranID, "STAN"));
        checkTransactionRefund.put(Ref_number, getElementFromDocument(responseRevDoc, "Rrn"));
        checkTransactionRefund.put(Amount, fillFieldValue(getElementFromDocument(requestRevDoc, "RefundAmount"), 12, '0'));
        checkTransactionRefund.put(Currency, "UAH");
        checkTransactionRefund.put(Terminal, "P");
        checkTransactionRefund.put(Term_nr, terminal_AVAL);
        checkTransactionRefund.put(MerchantCode, fillFieldValue(merchant_AVAL, 15, ' '));
        checkTransactionRefund.put(MOTO_ECI_IND, "7");
        checkTransactionRefund.put(FLD_123_1, "M");
        checkTransactionRefund.put(EPI_42_48_FULL, "210");

        TransactionContext.ECOM_TRAN_PAYMENT_FIELDS.add(checkTransactionRefund);
    }
}

