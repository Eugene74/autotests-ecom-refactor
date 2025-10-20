package tests.paylink.xml;

import com.ecom.db.JDBCMethods;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;
import com.ecom.api.type.FieldsNFile;

import java.util.HashMap;
import java.util.Map;

import static com.ecom.core.config.CardConfig.cardMC;
import static com.ecom.core.config.CardConfig.cardMC07;
import static com.ecom.core.config.EnvData.URL;
import static com.ecom.core.config.EnvData.id_AVAL;
import static com.ecom.core.config.EnvData.merchant_AVAL;
import static com.ecom.core.config.EnvData.terminal_AVAL;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.*;
import static com.ecom.tests.support.RequestSender.sendRequest;
import static org.testng.Assert.*;
import static com.ecom.api.type.FieldsNFile.*;

public class PaymentRefundPartDiffDay extends BaseTest {
    private static Document requestDoc;
    private static Document responseDoc;
    private static int tranPayId;
    private static int tranRefundFirstId;

    @Test
    public void Payment() {
        requestDoc = payment(cardMC, "payment+refund_part_diff_days", merchant_AVAL, terminal_AVAL);
        responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        tranPayId = getTranIdByOrder(getElementFromDocument (requestDoc,"OrderID"));
        String approvalCode = getElementFromDocument(responseDoc, "ApprovalCode");
        String rrn = getElementFromDocument(responseDoc, "Rrn");

        assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000","TranCode" );
        assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M","CVResult" );
        assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
        assertEquals(approvalCode.length(), 6,"ApprovalCode" );
        assertEquals(rrn.length(), 12, "Rrn");
        assertEquals(getValueFromTRAN(tranPayId, "ECI"), "07","ECI" );
//        assertEquals(getValueFromTRAN(tranPayId, "FEE"), "400", "FEE");

        System.out.println("Verified:");
        String [] verifiedResponse = {"TranCode", "CVResult", "HostCode", "Rrn", "ApprovalCode"};
        String [] verifiedResponseFromDB = {"ECI"};
        try{
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataFromDB(tranPayId, verifiedResponseFromDB);}
        catch (Exception e) {
            System.out.println("TEST FAILED");
        }

        addDataToCheckTransactionNFile(requestDoc, tranPayId, approvalCode, rrn);
    }

    @Test(dependsOnMethods = "Payment")
    public void generateBatch(){
        JDBCMethods.setNewTranTime(tranPayId);
        System.out.println("--TRAN TIME was Updated--\n");
        paylinkCloseDayForApi (id_AVAL);
        assertFalse(getValueFromTRAN(tranPayId, "Batch").isEmpty(), "BATCH");
    }

    @Test(dependsOnMethods = "generateBatch")
    public void PaymentRefundPartFirstDay() {
        Document requestRevDoc = reversal(requestDoc, responseDoc, -0.5);
        Document responseRevDoc = sendRequest(URL, requestRevDoc);

        System.out.println("--REFUND--\nRequest:\n" + printRequest(requestRevDoc));
        System.out.println("Response:\n" + printResponse(responseRevDoc));

        tranRefundFirstId = getTranIdByOrder(getElementFromDocument(requestRevDoc, "OrderID"));

        assertEquals(getElementFromDocument(responseRevDoc, "TranCode"), "000","TranCode" );
        assertEquals(getElementFromDocument(responseRevDoc, "Rrn").length(), 12, "Rrn");
        assertEquals(getValueFromTRAN(tranRefundFirstId, "RevFlag"), "0", "RevFlag");
        assertEquals(getValueFromTRAN(tranRefundFirstId, "ECI"), getValueFromTRAN(tranPayId, "ECI"), "ECI");
        assertEquals(getValueFromTRAN(tranRefundFirstId, "CVResult"), "M", "CVResult");

        System.out.println("Verified:");
        String [] verifiedResponse = {"TranCode", "Rrn"};
        String [] verifiedResponseFromDB = {"ECI", "RevFlag", "CVResult"};
        try{
            verifiedDataFromResponse(responseRevDoc, verifiedResponse);
            verifiedDataFromDB(tranRefundFirstId, verifiedResponseFromDB);
            verifiedDataFromDB(tranPayId, new String[]{"Batch"});}
        catch (Exception e) {
            System.out.println("TEST FAILED");
        }

        addDataToCheckTransactionRefundNFile(requestRevDoc, responseRevDoc, tranPayId);
    }

    @Test(dependsOnMethods = "PaymentRefundPartFirstDay")
    public void generateBatchSecond(){
        String batchFirst = getValueFromTRAN(tranPayId, "Batch");

        paylinkCloseDayForApi(id_AVAL);
        assertFalse(getValueFromTRAN(tranRefundFirstId, "Batch").isEmpty(), "BATCH");

        String batchSecond = getValueFromTRAN(tranRefundFirstId, "Batch");
        assertNotEquals(batchSecond, batchFirst);
    }

    @Test(dependsOnMethods = "generateBatchSecond")
    public void PaymentRefundPartOtherDay() {
        Document requestRevDoc = reversal(requestDoc, responseDoc, -0.5);
        Document responseRevDoc = sendRequest(URL, requestRevDoc);

        System.out.println("--REFUND--\nRequest:\n" + printRequest(requestRevDoc));
        System.out.println("Response:\n" + printResponse(responseRevDoc));

        int tranRefundSecondId = getTranIdByOrder(getElementFromDocument(requestRevDoc, "OrderID"));

        assertEquals(getElementFromDocument(responseRevDoc, "TranCode"), "000","TranCode" );
        assertEquals(getElementFromDocument(responseRevDoc, "Rrn").length(), 12, "Rrn");
        assertEquals(getValueFromTRAN(tranRefundSecondId, "RevFlag"), "0", "RevFlag");
        assertEquals(getValueFromTRAN(tranRefundSecondId, "ECI"), getValueFromTRAN(tranPayId, "ECI"), "ECI");
        assertEquals(getValueFromTRAN(tranRefundSecondId, "CVResult"), "M", "CVResult");

        System.out.println("Verified:");
        String [] verifiedResponse = {"TranCode", "Rrn"};
        String [] verifiedResponseFromDB = {"ECI", "RevFlag", "CVResult"};
        try{
            verifiedDataFromResponse(responseRevDoc, verifiedResponse);
            verifiedDataFromDB(tranRefundSecondId, verifiedResponseFromDB);
            verifiedDataFromDB(tranRefundFirstId, new String[]{"Batch"});}
        catch (Exception e) {
            System.out.println("TEST FAILED");
        }

        addDataToCheckTransactionRefundNFile(requestRevDoc, responseRevDoc, tranRefundSecondId);
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

        tranPaymentFields.add(checkTransaction);
    }

    public void addDataToCheckTransactionRefundNFile(Document requestRevDoc, Document responseRevDoc, int tranID) {
        Map<FieldsNFile, String> checkTransactionRefund = new HashMap<>();
        checkTransactionRefund.put(TestName, "MasterCardPaymentRefundPart");

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

        tranPaymentFields.add(checkTransactionRefund);
    }
}

