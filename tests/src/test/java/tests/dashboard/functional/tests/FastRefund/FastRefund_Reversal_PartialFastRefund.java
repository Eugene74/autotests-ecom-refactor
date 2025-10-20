package tests.dashboard.functional.tests.FastRefund;

import com.ecom.tests.support.DashboardRequest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import tests.BaseRedirect;

import static com.ecom.core.config.CardConfig.cardVISAmtRev;
import static com.ecom.core.config.EnvData.URL;
import static com.ecom.core.config.EnvData.id_AVAL;
import static com.ecom.core.config.EnvData.merchant_AVAL;
import static com.ecom.core.config.EnvData.terminal_AVAL;
import static com.ecom.db.JDBCMethods.*;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.PaylinkRequests.payment;
import static com.ecom.tests.support.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;

import static com.ecom.api.type.Attributes.ALLOW_PARTIAL_REVERSAL;

import static com.ecom.tests.support.DocumentTools.getElementFromDocument;
import static com.ecom.tests.support.DocumentTools.verifiedDataFromResponse;
import static com.ecom.api.type.Attributes.ALLOW_PAYMENT_WITHOUT_3DS;
import static com.ecom.api.type.Attributes.ALLOW_FAST_REFUND;

public class FastRefund_Reversal_PartialFastRefund extends BaseRedirect {
    public static String orderId;
    public static String rrn;
    public static String approval_code;
    public static int tranId;

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
        Document requestDoc = payment(cardVISAmtRev, "payment FastRefund", merchant_AVAL, terminal_AVAL);
        Document responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));
        orderId = getElementFromDocument(requestDoc, "OrderID");
        approval_code = getElementFromDocument(responseDoc, "ApprovalCode");
        rrn = getElementFromDocument(responseDoc, "Rrn");

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
        }    }


    @Test(dependsOnMethods = "makePayment")
    public void makePartialFastRefund() {
        DashboardRequest request = new DashboardRequest();
        request.makePartialFastRefund(orderId, String.valueOf(tranId));

        // Зчитування значення MT_TRAN_ID з таблиці
        WebElement refundIdElement = getDriver().findElement(By.xpath("//td[text()='Refund']/following-sibling::td"));
        String refundIdFromTable = refundIdElement.getText();

        // Отримання значення MT_TRAN_ID з бази даних
        int refundIdFromDB = getTranIdByOrderFR(orderId);

        // Перевірка відповідності значень
        Assert.assertEquals(refundIdFromTable, String.valueOf(refundIdFromDB), "MT_TRAN_ID does not match!");

        // Додаткові перевірки
        SoftAssert softAssertion = new SoftAssert();
        softAssertion.assertEquals(getValueFromMTTranFR(refundIdFromDB, "TRAN_TYPE"), "A", "TRAN_TYPE");
        softAssertion.assertEquals(String.valueOf(refundIdFromDB).length(), 6, "MT_TRAN_ID length");
        softAssertion.assertEquals(getValueFromMTTranFR(refundIdFromDB, "RRN").length(), 12, "RRN length");
        softAssertion.assertEquals(getValueFromMTTranFR(refundIdFromDB, "ACTION_CODE"), "000", "ACTION_CODE");
        softAssertion.assertEquals(getValueFromMTTranFR(refundIdFromDB, "APPROVAL_CODE").length(), 6, "APPROVAL_CODE length");
        softAssertion.assertEquals(getValueFromMTTranFR(refundIdFromDB, "CL_TYPE"), "VISA_FAST_REFUND", "CL_TYPE");
        softAssertion.assertAll();

        System.out.println("Verified:");
        String[] verifiedResponseFromDB = {"TRAN_TYPE", "MT_TRAN_ID", "RRN", "ACTION_CODE", "APPROVAL_CODE", "CL_TYPE"};
        try {
            verifiedDataFromDBFR(refundIdFromDB, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }  }

    @Test(dependsOnMethods = "makePartialFastRefund")
    public void makeReversal() {
        DashboardRequest request = new DashboardRequest();
        request.makeReversalForPaymentFR(orderId, String.valueOf(tranId));
        int tranReversId = getTranIdByOrder(orderId);
        SoftAssert softAssertion = new SoftAssert();
        softAssertion.assertEquals(getValueFromTRAN(tranReversId, "Rrn").length(), 12, "Rrn");
        softAssertion.assertEquals(getValueFromTRAN(tranReversId, "TranCode"), "000", "TranCode");
        softAssertion.assertAll();

        System.out.println("Verified:");
        String[] verifiedResponseFromDB = {"ECI", "ApprovalCode", "Rrn", "TranCode", "RevFlag"};
        try {
            verifiedDataFromDB(tranReversId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }   }

    @Test (dependsOnMethods = "makeReversal")
    public void searchByOrderID(){
        DashboardRequest request = new DashboardRequest();
        boolean searchByMerch = request.findPaymentByOrder(orderId);
        Assert.assertEquals(searchByMerch, true, "Search result flag");
    }

    @Test (dependsOnMethods = "makeReversal")
    public void searchByApprovalCode() {
        DashboardRequest request = new DashboardRequest();
        boolean searchByMerch = request.findPaymentByApprovalCode(orderId, approval_code);
        Assert.assertEquals(searchByMerch, true, "Search result flag");
    }

    @AfterClass
    public void setDefaultParam() {
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "false");
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: FALSE");
        setMerchantAtt(id_AVAL, ALLOW_FAST_REFUND, "false");
        System.out.println("ALLOW_FAST_REFUND: FALSE");
        setMerchantAtt(id_AVAL, ALLOW_PARTIAL_REVERSAL, "false");
        System.out.println("ALLOW_PARTIAL_REVERSAL: FALSE");
   } }