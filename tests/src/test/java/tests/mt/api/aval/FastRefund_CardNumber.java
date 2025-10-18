package tests.mt.api.aval;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import tests.mt.BaseTestMoneyTransfer;

import static com.ecom.db.JDBCMethods.*;
import static methods.DocumentTools.*;
import static methods.MoneyTransferRequests.*;
import static methods.PaylinkRequests.payment;
import static methods.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;
import static com.ecom.api.type.Attributes.*;


public class FastRefund_CardNumber extends BaseTestMoneyTransfer {
    protected Document requestDoc;
    protected Document responseDoc;

    public static String orderId;
    public static String rrn;
    public static String approval_code;
    public static int tranId;
    public static String totalAmountFromPayment;
    public static String approvalCodeFromPayment;
    public static String rrnFromPayment;
    public static String valueFromPayment;

    @BeforeClass
    public void setParam() {
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "true");
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
        setMerchantAtt(id_AVAL, ALLOW_FAST_REFUND, "true");
        System.out.println("ALLOW_FAST_REFUND: TRUE");
        setMerchantAtt(id_AVAL, ALLOW_PARTIAL_REVERSAL, "true");
        System.out.println("ALLOW_PARTIAL_REVERSAL: TRUE");
    }

    @Test
    public void makePayment() {
        requestDoc = payment(cardMCmt, "payment FastRefund", merchant_AVAL, terminal_AVAL);
        responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));
        orderId = getElementFromDocument(requestDoc, "OrderID");
        approval_code = getElementFromDocument(responseDoc, "ApprovalCode");
        rrn = getElementFromDocument(responseDoc, "Rrn");

        approvalCodeFromPayment = approval_code;
        rrnFromPayment = rrn;
        valueFromPayment = getElementFromDocument(requestDoc, "Value");
        totalAmountFromPayment = getElementFromDocument(requestDoc, "TotalAmount");

        assertEquals(approval_code.length(), 6, "ApprovalCode");
        assertEquals(rrn.length(), 12, "Rrn");
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

    private String maskCardNumber(String cardNumber) {
        if (cardNumber != null && cardNumber.length() >= 12) {
            return cardNumber.substring(0, 6) + "******" + cardNumber.substring(cardNumber.length() - 4);
        }
        return cardNumber; // Возвращаем как есть, если длина недостаточна
    }

    @Test(dependsOnMethods = "makePayment")
    public void makeFastRefundCard() {
        Document requestRFDoc = fastRefundAPICard(merchant_AVAL, terminal_AVAL, requestDoc, responseDoc);

        if (requestRFDoc.getElementsByTagName("ApprovalCode").getLength() > 0) {
            requestRFDoc.getElementsByTagName("ApprovalCode").item(0).setTextContent(approvalCodeFromPayment);
        }
        if (requestRFDoc.getElementsByTagName("RRN").getLength() > 0) {
            requestRFDoc.getElementsByTagName("RRN").item(0).setTextContent(rrnFromPayment);
        }
        if (requestRFDoc.getElementsByTagName("Value").getLength() > 0) {
            String value = totalAmountFromPayment != null && !totalAmountFromPayment.isEmpty() ? totalAmountFromPayment : "100";
            requestRFDoc.getElementsByTagName("Value").item(0).setTextContent(value);
        }

        // Генерация нового TrackingId в формате FR + 6 цифр
        String trackingId = "FR" + String.format("%06d", (int) (Math.random() * 1000000));
        if (requestRFDoc.getElementsByTagName("TrackingId").getLength() > 0) {
            requestRFDoc.getElementsByTagName("TrackingId").item(0).setTextContent(trackingId);
        }

        // Установлюємо відкритий номер карти в запит
        if (requestRFDoc.getElementsByTagName("RecipientCardNumber").getLength() > 0) {
            requestRFDoc.getElementsByTagName("RecipientCardNumber").item(0).setTextContent(cardMCfr); // Відкритий номер карти
        }

// Клонуємо документ для логування
        Document requestRFDocForLogging = (Document) requestRFDoc.cloneNode(true);
        if (requestRFDocForLogging.getElementsByTagName("RecipientCardNumber").getLength() > 0) {
            String maskedCardNumber = maskCardNumber(cardMCfr); // Маскуємо номер карти
            requestRFDocForLogging.getElementsByTagName("RecipientCardNumber").item(0).setTextContent(maskedCardNumber);
        }

// Логування запиту з маскованим номером карти
        System.out.println("--FAST REFUND REQUEST--\n" + convertXMLDocumentToString(requestRFDocForLogging));

// Відправляємо запит з відкритим номером карти
        Document responseRFDoc = sendRequest(URLfr, requestRFDoc);

// Логування відповіді
        System.out.println("--FAST REFUND RESPONSE--\n" + convertXMLDocumentToString(responseRFDoc));

        int refundIdFromDB = getTranIdByOrderFR(orderId);
        SoftAssert softAssertion = new SoftAssert();

// Проверки
        softAssertion.assertEquals(getElementFromDocument(responseRFDoc, "Code"), "000", "Code");
        softAssertion.assertEquals(getElementFromDocument(responseRFDoc, "Message"), "Approved", "Message");
        softAssertion.assertEquals(getElementFromDocument(responseRFDoc, "ApprovalCode").length(), 6, "ApprovalCode");
        softAssertion.assertEquals(getElementFromDocument(responseRFDoc, "RRN").length(), 12, "RRN");
        softAssertion.assertEquals(getValueFromMTTranFR(refundIdFromDB, "TRAN_TYPE"), "A", "TRAN_TYPE");
        softAssertion.assertEquals(String.valueOf(refundIdFromDB).length(), 6, "MT_TRAN_ID length");
        softAssertion.assertEquals(getValueFromMTTranFR(refundIdFromDB, "CL_TYPE"), "MC_FAST_REFUND", "CL_TYPE");
        softAssertion.assertAll();

// Проверка через verifiedData
     System.out.println("Verified:");
        String[][] verifiedData = {
                {"Code", "Message", "ApprovalCode", "RRN"},
                {"TRAN_TYPE", "MT_TRAN_ID", "CL_TYPE"}
        };

        for (int i = 0; i < verifiedData.length; i++) {
            try {
                if (i == 0) {
                    verifiedDataFromResponse(responseRFDoc, verifiedData[i]);
                } else {
                    verifiedDataFromDBFR(refundIdFromDB, verifiedData[i]);
                }
            } catch (Exception e) {
                System.out.println("TEST FAILED");
            } } }

    @Test(dependsOnMethods = "makeFastRefundCard")
    public void makePaymentPartialFRefundCard() {
        Document requestDoc = payment(cardMC, "payment FastRefund", merchant_AVAL, terminal_AVAL);
        Document responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT WITH HALF VALUE--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));
        orderId = getElementFromDocument(requestDoc, "OrderID");
        approval_code = getElementFromDocument(responseDoc, "ApprovalCode");
        rrn = getElementFromDocument(responseDoc, "Rrn");


        approvalCodeFromPayment = approval_code;
        rrnFromPayment = rrn;
        valueFromPayment = getElementFromDocument(requestDoc, "Value");
        totalAmountFromPayment = getElementFromDocument(requestDoc, "TotalAmount");

        // Рассчитываем половину значения Value
        if (valueFromPayment != null && !valueFromPayment.isEmpty()) {
            int value = Integer.parseInt(valueFromPayment);
            valueFromPayment = String.valueOf(value / 2);
        }

        assertEquals(approval_code.length(), 6, "ApprovalCode");
        assertEquals(rrn.length(), 12, "Rrn");

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

    @Test(dependsOnMethods = "makePaymentPartialFRefundCard")
    public void makePartialFastRefundCard() {
        Document requestRFDoc = fastRefundAPICard(merchant_AVAL, terminal_AVAL, requestDoc, responseDoc);

        if (requestRFDoc.getElementsByTagName("ApprovalCode").getLength() > 0) {
            requestRFDoc.getElementsByTagName("ApprovalCode").item(0).setTextContent(approvalCodeFromPayment);
        }
        if (requestRFDoc.getElementsByTagName("RRN").getLength() > 0) {
            requestRFDoc.getElementsByTagName("RRN").item(0).setTextContent(rrnFromPayment);
        }
        if (requestRFDoc.getElementsByTagName("Value").getLength() > 0) {
            // Ділимо значення Value на два
            String value = totalAmountFromPayment != null && !totalAmountFromPayment.isEmpty()
                    ? String.valueOf(Integer.parseInt(totalAmountFromPayment) / 2)
                    : "50"; // Значення за замовчуванням — половина від 100
            requestRFDoc.getElementsByTagName("Value").item(0).setTextContent(value);
        }

        // Генерация нового TrackingId в формате FR + 6 цифр
        String trackingId = "FR" + String.format("%06d", (int) (Math.random() * 1000000));
        if (requestRFDoc.getElementsByTagName("TrackingId").getLength() > 0) {
            requestRFDoc.getElementsByTagName("TrackingId").item(0).setTextContent(trackingId);
        }

        // Установлюємо відкритий номер карти в запит
        if (requestRFDoc.getElementsByTagName("RecipientCardNumber").getLength() > 0) {
            requestRFDoc.getElementsByTagName("RecipientCardNumber").item(0).setTextContent(cardMCardfr); // Відкритий номер карти
        }

// Клонуємо документ для логування
        Document requestRFDocForLogging = (Document) requestRFDoc.cloneNode(true);
        if (requestRFDocForLogging.getElementsByTagName("RecipientCardNumber").getLength() > 0) {
            String maskedCardNumber = maskCardNumber(cardMCardfr); // Маскуємо номер карти
            requestRFDocForLogging.getElementsByTagName("RecipientCardNumber").item(0).setTextContent(maskedCardNumber);
        }

// Логування запиту з маскованим номером карти
        System.out.println("--FAST REFUND REQUEST--\n" + convertXMLDocumentToString(requestRFDocForLogging));

// Відправляємо запит з відкритим номером карти
        Document responseRFDoc = sendRequest(URLfr, requestRFDoc);

// Логування відповіді
        System.out.println("--FAST REFUND RESPONSE--\n" + convertXMLDocumentToString(responseRFDoc));

        int refundIdFromDB = getTranIdByOrderFR(orderId);
        SoftAssert softAssertion = new SoftAssert();

// Проверки
        softAssertion.assertEquals(getElementFromDocument(responseRFDoc, "Code"), "000", "Code");
        softAssertion.assertEquals(getElementFromDocument(responseRFDoc, "Message"), "Approved", "Message");
        softAssertion.assertEquals(getElementFromDocument(responseRFDoc, "ApprovalCode").length(), 6, "ApprovalCode");
        softAssertion.assertEquals(getElementFromDocument(responseRFDoc, "RRN").length(), 12, "RRN");
        softAssertion.assertEquals(getValueFromMTTranFR(refundIdFromDB, "TRAN_TYPE"), "A", "TRAN_TYPE");
        softAssertion.assertEquals(String.valueOf(refundIdFromDB).length(), 6, "MT_TRAN_ID length");
        softAssertion.assertEquals(getValueFromMTTranFR(refundIdFromDB, "CL_TYPE"), "MC_FAST_REFUND", "CL_TYPE");
        softAssertion.assertAll();

// Проверка через verifiedData
        System.out.println("Verified:");
        String[][] verifiedData = {
                {"Code", "Message", "ApprovalCode", "RRN"},
                {"TRAN_TYPE", "MT_TRAN_ID", "CL_TYPE"}
        };

        for (int i = 0; i < verifiedData.length; i++) {
            try {
                if (i == 0) {
                    verifiedDataFromResponse(responseRFDoc, verifiedData[i]);
                } else {
                    verifiedDataFromDBFR(refundIdFromDB, verifiedData[i]);
                }
            } catch (Exception e) {
                System.out.println("TEST FAILED");
            } } }

    @AfterClass
    public void setDefaultParam() {
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "false");
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: FALSE");
        setMerchantAtt(id_AVAL, ALLOW_FAST_REFUND, "false");
        System.out.println("ALLOW_FAST_REFUND: FALSE");
        setMerchantAtt(id_AVAL, ALLOW_PARTIAL_REVERSAL, "false");
        System.out.println("ALLOW_PARTIAL_REVERSAL: FALSE");
    }
}