package tests.dashboard.functional.tests.Payments;

import com.ecom.tests.support.DashboardRequest;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import tests.BaseRedirect;

import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.db.JDBCMethods.setMerchantAtt;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.DocumentTools.getElementFromDocument;
import static com.ecom.tests.support.DocumentTools.verifiedDataFromResponse;
import static com.ecom.tests.support.PaylinkRequests.payment;
import static com.ecom.tests.support.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;
import static com.ecom.api.type.Attributes.ALLOW_PAYMENT_WITHOUT_3DS;

public class Transaction extends BaseRedirect {
    public static String orderId;
    public static String rrn;
    public static String approval_code;
    public static int tranId;

    @BeforeClass
    public void setParam() {
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "true");
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
    }

    @Test
    public void makePayment() {
        Document requestDoc = payment(cardVISAmtRev, "payment dashboard", merchant_AVAL, terminal_AVAL);
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
        }

    }

    @Test (dependsOnMethods = "makePayment")
    public void searchByOrderID(){
        DashboardRequest request = new DashboardRequest();
        boolean searchByMerch = request.findPaymentByOrder(orderId);
        Assert.assertEquals(searchByMerch, true, "Search result flag");
    }

    @Test (dependsOnMethods = "makePayment")
    public void searchByMerchant() {
        DashboardRequest request = new DashboardRequest();
        boolean searchByMerch = request.findPaymentByMerch("YVPauto", orderId);
        Assert.assertEquals(searchByMerch, true, "Search result flag");
    }

    @Test (dependsOnMethods = "makePayment")
    public void searchByRRN() {
        DashboardRequest request = new DashboardRequest();
        boolean searchByMerch = request.findPaymentByRRN(orderId, rrn);
        Assert.assertEquals(searchByMerch, true, "Search result flag");
    }


    @Test (dependsOnMethods = "makePayment")
    public void searchByCard(){
        DashboardRequest request = new DashboardRequest();
        boolean searchByMerch = request.findPaymentByCard(orderId, cardVISAmtRev[0]);
        Assert.assertEquals(searchByMerch, true, "Search result flag");
    }

    @Test (dependsOnMethods = "makePayment")
    public void searchByApprovalCode() {
        DashboardRequest request = new DashboardRequest();
        boolean searchByMerch = request.findPaymentByApprovalCode(orderId, approval_code);
        Assert.assertEquals(searchByMerch, true, "Search result flag");
    }

    @Test (dependsOnMethods = "makePayment")
    public void searchByMultFields() {
        DashboardRequest request = new DashboardRequest();
        boolean searchByMerch = request.findPaymentByMultField(orderId, "YVPauto" +
                "", rrn,  cardVISAmtRev[0], approval_code);
        Assert.assertEquals(searchByMerch, true, "Search result flag");
    }

    @Test(dependsOnMethods = "makePayment")
    public void makeReversal() {
        DashboardRequest request = new DashboardRequest();
        request.makeReversalForPayment(orderId, String.valueOf(tranId));
        int tranReversId = getTranIdByOrder(orderId);
        SoftAssert softAssertion = new SoftAssert();
        //softAssertion.assertFalse(tranReversId == tranId);
        softAssertion.assertEquals(getValueFromTRAN(tranReversId, "Rrn").length(), 12, "Rrn");
        softAssertion.assertEquals(getValueFromTRAN(tranReversId, "TranCode"), "000", "TranCode");
        softAssertion.assertAll();

        System.out.println("Verified:");
        String[] verifiedResponseFromDB = {"ECI", "ApprovalCode", "Rrn", "TranCode", "RevFlag"};
        try {
            verifiedDataFromDB(tranReversId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @Test
    public static void exportTransactionsToExcel(){
        DashboardRequest request = new DashboardRequest();
        boolean expFlag = request.exportPayments();
        System.out.println(expFlag);

        Assert.assertEquals(expFlag, true, "Search result flag");
    }

    @Test (dependsOnMethods = "makePayment")
    public static void downloadReceipt(){
        DashboardRequest request = new DashboardRequest();
        boolean result = request.downloadReceipt(orderId, String.valueOf(tranId));
        System.out.println(result);

        Assert.assertEquals(result, true, "Success result flag");
    }

    @AfterClass
    public void setDefaultParam() {
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "false");
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: FALSE");
    }

}