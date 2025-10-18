package tests.dashboard.functional.tests.Settings;

import methods.DashboardRequest;
import methods.MoneyTransferRequests;
import methods.RequestSender;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseRedirect;
import org.testng.Assert;


import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.setMerchantAtt;
import static methods.DocumentTools.*;
import static methods.DocumentTools.verifiedDataFromDB;
import static methods.PaylinkRequests.payment;
import static methods.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;
import static com.ecom.api.type.Attributes.ALLOW_PAYMENT_WITHOUT_3DS;

public class StopList extends BaseRedirect {
    public static String orderId;
    public static String rrn;
    public static String approval_code;
    public static int tranId;
    protected static String URLmt = envProperties.getProperty("URLtomcat")+ "/mt/tran";

    @BeforeClass
    public void setParam() {
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "true");
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
    }

    @Test
    public void makePayment(){
        Document requestDoc = payment(cardMCmt, "payment", merchant_AVAL, terminal_AVAL);
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

    @Test(dependsOnMethods = "makePayment")
    public void putCardToStopList()  {
        DashboardRequest request = new DashboardRequest();
        boolean searchByCard = request.putCardToStopList(orderId, String.valueOf(tranId));
        System.out.println(searchByCard);
        Assert.assertEquals(searchByCard, true, "Search result flag");
    }

    @Test(dependsOnMethods = "putCardToStopList")
    public void makePaymentAfterPuttingToStopList(){
        Document requestDoc = payment(cardMCmt, "MC payment", merchant_AVAL, terminal_AVAL);
        Document responseDoc = sendRequest(URL, requestDoc);

        System.out.println("--PAYMENT--\nRequest:\n" + printRequest(requestDoc));
        System.out.println("Response:\n" + printResponse(responseDoc));

        Assert.assertEquals(getElementFromDocument(responseDoc, "TranCode"), "432", "TranCode");

        System.out.println("Verified:");
        String[] verifiedResponse = {"TranCode"};
        String[] verifiedResponseFromDB = {"ECI"};
        try {
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataFromDB(tranId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("Error. TEST FAILED");
        }
    }

    @Test(dependsOnMethods = "putCardToStopList")
    public void moneyTransfeAfterPuttingToStopList(){
        Document requestDoc = MoneyTransferRequests.transferCardToCard(cardMCmt, cardVISAmtRev, merchant_AVAL, terminal_AVAL);
        Document responseDoc = RequestSender.sendRequest(URLmt, requestDoc);

        //System.out.println("--TRANSFER--\nRequest:\n" + printRequestMT(requestDoc));
        //System.out.println("Response:\n" + printResponseMT(responseDoc));

        String rrnPayment = getElementFromDocument(responseDoc, "RRN");
        Assert.assertEquals(rrnPayment.length(), 12, "RRN");
    }

    @Test(dependsOnMethods = "makePaymentAfterPuttingToStopList")
    public void deleteCardFromStopList()  {
        DashboardRequest request = new DashboardRequest();
        boolean searchByCard = request.deleteCardFromStopList(cardMCmt[0]);
        System.out.println(searchByCard);
        Assert.assertEquals(searchByCard, false, "Search result flag");
    }

    @AfterClass
    public void setDefaultParam() {
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "false");
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: FALSE");
    }
}