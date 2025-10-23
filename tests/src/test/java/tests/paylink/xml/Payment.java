package tests.paylink.xml;

import static com.ecom.api.type.FieldsNFile.*;
import static com.ecom.core.config.CardConfig.cardMC07;
import static com.ecom.core.config.CardConfig.cardVISA;
import static com.ecom.core.config.EnvData.MERCHANT_ID_AVAL;
import static com.ecom.core.config.EnvData.TERMINAL_ID_AVAL;
import static com.ecom.core.config.EnvData.URL;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.payment;
import static com.ecom.tests.support.RequestSenderRest.sendRequest;
import static org.testng.Assert.assertEquals;

import com.ecom.api.type.FieldsNFile;
import java.util.HashMap;
import java.util.Map;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.context.TransactionContext;

public class Payment {

  @Test
  public void Payment() {
    Document requestDoc = payment(cardVISA, "payment xml", MERCHANT_ID_AVAL, TERMINAL_ID_AVAL);
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

    addDataToCheckTransactionNFile(requestDoc, tranId, approvalCode, rrn);
  }

  public void addDataToCheckTransactionNFile(
      Document requestDoc, int tranID, String approvalCode, String rrn) {
    Map<FieldsNFile, String> checkTransaction = new HashMap<>();
    checkTransaction.put(TestName, "payment xml");

    checkTransaction.put(Merchant, MERCHANT_ID_AVAL);
    checkTransaction.put(Card, fillFieldValue(cardVISA[0], 19, ' '));
    checkTransaction.put(Exp_date, cardMC07[4]);
    checkTransaction.put(Tran_type, "05");
    checkTransaction.put(Appr_code, approvalCode);
    checkTransaction.put(Appr_src, "1");
    checkTransaction.put(Stan, getValueFromTRAN(tranID, "STAN"));
    checkTransaction.put(Ref_number, rrn);
    checkTransaction.put(
        Amount, fillFieldValue(getElementFromDocument(requestDoc, "TotalAmount"), 12, '0'));
    checkTransaction.put(Currency, "UAH");
    checkTransaction.put(Terminal, "P");
    checkTransaction.put(Term_nr, TERMINAL_ID_AVAL);
    checkTransaction.put(MerchantCode, fillFieldValue(MERCHANT_ID_AVAL, 15, ' '));
    checkTransaction.put(MOTO_ECI_IND, "7");
    checkTransaction.put(FLD_123_1, "M");
    checkTransaction.put(EPI_42_48_FULL, "210");

    TransactionContext.ECOM_TRAN_PAYMENT_FIELDS.add(checkTransaction);
  }
}
