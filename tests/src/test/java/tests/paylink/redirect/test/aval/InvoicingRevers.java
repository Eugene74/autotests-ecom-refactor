package tests.paylink.redirect.test.aval;

import com.ecom.db.JDBCMethods;
import com.ecom.tests.support.RedirectRequest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseRedirect;

import static com.ecom.db.JDBCMethods.*;
import static com.ecom.tests.support.DocumentTools.verifiedDataFromDB;
import static com.ecom.api.type.Attributes.ALLOW_PAYMENT_WITHOUT_3DS;
import static com.ecom.api.type.Attributes.MERCHANT_INVOICING_URL;

public class InvoicingRevers extends BaseRedirect {

    private static String orderRedirect;
    private static int tranId;

    @BeforeClass
    public void setParam(){
        JDBCMethods.setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "true");
        setMerchantAtt(id_AVAL, MERCHANT_INVOICING_URL, URLInvocing);
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
    }

    @Test
    public void invoicePay() {
        RedirectRequest pay = new RedirectRequest();
        orderRedirect = pay.paymentInvoicing(URL_MERCH, cardVISA, id_AVAL);
        pay.usedCVC(cardVISA);
        System.out.println("Order: " + orderRedirect);

        tranId = getTranIdByOrder(orderRedirect);

        // Видалення префікса "Order №" з orderRedirect
        orderRedirect = orderRedirect.replace("Order № ", "").trim();
        System.out.println("Order without prefix: " + orderRedirect);

        tranId = getTranIdByOrder(orderRedirect);
        System.out.println("TranId: " + tranId);

        SoftAssert softAssertion= new SoftAssert();
        softAssertion.assertEquals(getValueFromTRAN(tranId, "ECI"),"07", "ECI");
        softAssertion.assertEquals(getValueFromTRAN(tranId, "ApprovalCode").length(),6, "ApprovalCode");
        softAssertion.assertEquals(getValueFromTRAN(tranId, "Rrn").length(),12, "Rrn");
        softAssertion.assertEquals(getValueFromTRAN(tranId, "TranCode"),"000", "TranCode");
        softAssertion.assertEquals(getValueFromTRAN(tranId, "CVResult"),"M", "CVResult");
        softAssertion.assertAll();

        System.out.println("Verified:");
        String [] verifiedResponseFromDB = {"ECI","ApprovalCode", "Rrn", "TranCode", "CVResult"};
        try{
            verifiedDataFromDB(tranId, verifiedResponseFromDB);}
        catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @Test(dependsOnMethods = "invoicePay")
    public void invoiceRevers() {
        RedirectRequest revers = new RedirectRequest();
        revers.doReversal(URL_MERCH, orderRedirect, tranId);

        int tranReversId = getTranIdByOrder(orderRedirect);

// Видалення префікса "Order №" з orderRedirect (додаткове видалення на випадок, якщо префікс з'явиться знову)
        orderRedirect = orderRedirect.replace("Order № ", "").trim();
        System.out.println("Order without prefix: " + orderRedirect);

        int tranRefundId = getTranIdByOrder(orderRedirect);
        System.out.println("TranRefundId: " + tranRefundId);

        SoftAssert softAssertion = new SoftAssert();
        softAssertion.assertFalse(tranReversId == tranId);
        softAssertion.assertEquals(getValueFromTRAN(tranReversId, "ECI"), getValueFromTRAN(tranId, "ECI"), "ECI");
        softAssertion.assertEquals(getValueFromTRAN(tranReversId, "ApprovalCode").length(), 6, "ApprovalCode");
        softAssertion.assertEquals(getValueFromTRAN(tranReversId, "RevFlag"), "1", "RevFlag");
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

    @AfterClass
    public void setDefaultParam(){
        JDBCMethods.setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "false");
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: FALSE");
    }
}
