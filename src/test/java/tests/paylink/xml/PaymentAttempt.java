package tests.paylink.xml;

import model.*;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;
import type.FieldsNFile;

import java.util.*;

import static jdbc.JDBCMethods.getTranIdByOrder;
import static jdbc.JDBCMethods.getValueFromTRAN;
import static methods.DocumentTools.*;
import static methods.PaylinkRequests.paymentPares;
import static methods.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;
import static type.FieldsNFile.*;
import static type.Mtid.Mtid16;
import static type.Tags.*;

public class PaymentAttempt extends BaseTest {

    @Test
    public void mcPaymentAttempt() {
        Document requestDoc = paymentPares(cardMC06, "MasterCardPaymentAttempt", merchant_AVAL, terminal_AVAL);
        Document responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        int tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));
        String approvalCode = getElementFromDocument(responseDoc, "ApprovalCode");
        String rrn = getElementFromDocument(responseDoc, "Rrn");

        assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000", "TranCode");
        assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M", "CVResult");
        assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
        assertEquals(approvalCode.length(), 6, "ApprovalCode");
        assertEquals(rrn.length(), 12, "Rrn");
        assertEquals(getValueFromTRAN(tranId, "ECI"), "01", "ECI");
        assertEquals(getValueFromTRAN(tranId, "PAResStatus"), "A", "PAResStatus");
        assertEquals(getValueFromTRAN(tranId, "PA_ECI"), "01", "PA_ECI");
//        assertEquals(getValueFromTRAN(tranId, "FEE"), "400", "FEE");

        System.out.println("Verified:");
        String[] verifiedResponse = {"TranCode", "CVResult", "HostCode", "Rrn", "ApprovalCode"};
        String[] verifiedResponseFromDB = {"ECI", "PAResStatus", "PA_ECI"};
        try {
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataFromDB(tranId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }

//        addDataToCheckTransactionNFile(requestDoc, tranID, approvalCode, rrn);
        addTransactionRecordsToCheckNfile(requestDoc, tranId, approvalCode, rrn, cardMC06);
    }

    public void addDataToCheckTransactionNFile(Document requestDoc, int tranID, String approvalCode, String rrn) {
        Map<FieldsNFile, String> checkTransaction = new HashMap<>();
        checkTransaction.put(TestName, "MasterCardPaymentAttempt");

        checkTransaction.put(Merchant, merchant_AVAL);
        checkTransaction.put(Card, fillFieldValue(cardMC06[0], 19, ' '));
        checkTransaction.put(Exp_date, cardMC06[4]);
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
        checkTransaction.put(MOTO_ECI_IND, "1");
        checkTransaction.put(FLD_123_1, "A");
        checkTransaction.put(EPI_42_48_FULL, "212");
        checkTransaction.put(Mtid, "16");

        tranPaymentFields.add(checkTransaction);
    }

    public void addTransactionRecordsToCheckNfile(Document requestDoc, int tranID, String approvalCode, String rrn, String[] card) {
        Set<FieldValue> fieldValues = new HashSet<>();
        Set<TagValue> tagValues = new HashSet<>();

        fieldValues.add(new FieldValue(TestName, "MasterCardPaymentAttempt"));
        fieldValues.add(new FieldValue(Merchant, merchant_AVAL));
        fieldValues.add(new FieldValue(Card, fillFieldValue(card[0], 19, ' ')));
        fieldValues.add(new FieldValue(Exp_date, card[4]));
        fieldValues.add(new FieldValue(Tran_type, "05"));
        fieldValues.add(new FieldValue(Appr_code, approvalCode));
        fieldValues.add(new FieldValue(Appr_src, "1"));
        fieldValues.add(new FieldValue(Stan, getValueFromTRAN(tranID, "STAN")));
        fieldValues.add(new FieldValue(Ref_number, rrn));
        fieldValues.add(new FieldValue(Amount, fillFieldValue(getElementFromDocument(requestDoc, "TotalAmount"), 12, '0')));
        fieldValues.add(new FieldValue(Currency, "UAH"));
        fieldValues.add(new FieldValue(Terminal, "P"));
        fieldValues.add(new FieldValue(Term_nr, terminal_AVAL));
        fieldValues.add(new FieldValue(MerchantCode, fillFieldValue(merchant_AVAL, 15, ' ')));
        fieldValues.add(new FieldValue(MOTO_ECI_IND, "1"));
        fieldValues.add(new FieldValue(FLD_123_1, "A"));
        fieldValues.add(new FieldValue(EPI_42_48_FULL, "212"));

        tagValues.add(new TagValue(Mtid16_3DSV, "0011"));
        tagValues.add(new TagValue(Mtid16_SAAV, fillFieldValue(String.valueOf(card[5].length()), 3, '0') + card[5]));
        tagValues.add(new TagValue(Mtid16_CAVV, fillFieldValue(String.valueOf(card[5].length()), 3, '0') + card[5]));

        ECommTransaction eCommTransaction = new ECommTransaction(
                new TransactionRecord(fieldValues),
                Collections.singletonList(new AcquirerData(Mtid16, tagValues)));

        transactions.add(eCommTransaction);
    }

}
