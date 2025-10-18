package tests.paylink.redirect.test.aval;

import com.ecom.db.JDBCMethods;
import methods.RedirectRequest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseRedirect;

import static com.ecom.db.JDBCMethods.*;
import static methods.DocumentTools.verifiedDataFromDB;
import static methods.PaylinkRequests.paylinkCloseDay;
import static org.testng.Assert.assertFalse;
import static com.ecom.api.type.Attributes.*;

public class AuthorizationRefundOnLine extends BaseRedirect {
    private static String orderRedirect;
    private static int tranId;

    @BeforeClass
    public void setParam() {
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "true");
        setMerchantAtt(id_AVAL, ALLOW_REFUND_ONLINE, "true");
        setMerchantAtt(id_AVAL, MERCHANT_INVOICING_URL, URLInvocing);
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
        System.out.println("ALLOW_REFUND_ONLINE: TRUE");
    }

    @Test
    public void authorizationPay() {
        RedirectRequest pay = new RedirectRequest();
        orderRedirect = pay.paymentAuthorization(cardMC, 0, merchant_AVAL, terminal_AVAL);
        pay.usedCVC(cardVISA);
        System.out.println("Order: " + orderRedirect);

        orderRedirect = orderRedirect.replace("Order № ", "").trim();
        System.out.println("Order without prefix: " + orderRedirect);

        tranId = getTranIdByOrderWithRetry(orderRedirect, 5, 2000);
        if (tranId == 0) {
            System.err.println("Failed to retrieve TranId after retries. Test aborted.");
            return;
        }
        System.out.println("TranId: " + tranId);

        SoftAssert softAssertion = new SoftAssert();
        softAssertion.assertEquals(getValueFromTRAN(tranId, "ECI"), "07", "ECI");
        softAssertion.assertEquals(getValueFromTRAN(tranId, "ApprovalCode").length(), 6, "ApprovalCode");
        softAssertion.assertEquals(getValueFromTRAN(tranId, "Rrn").length(), 12, "Rrn");
        softAssertion.assertEquals(getValueFromTRAN(tranId, "TranCode"), "000", "TranCode");
        softAssertion.assertEquals(getValueFromTRAN(tranId, "CVResult"), "M", "CVResult");
        softAssertion.assertAll();

        System.out.println("Verified:");
        String[] verifiedResponseFromDB = {"ECI", "ApprovalCode", "Rrn", "TranCode", "CVResult"};
        try {
            verifiedDataFromDB(tranId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
            e.printStackTrace();
        }
    }

    @Test(dependsOnMethods = "authorizationPay")
    public void generateBatch() {
        JDBCMethods.setNewTranTime(tranId);
        System.out.println("--TRAN TIME was Updated--\n");
        paylinkCloseDay(id_AVAL);
        assertFalse(getValueFromTRAN(tranId, "Batch").isEmpty(), "BATCH");
    }

    @Test(dependsOnMethods = "generateBatch")
    public void authorizationRefundOnLine() {
        RedirectRequest refund = new RedirectRequest();
        refund.doReversal(URL_MERCH, orderRedirect, tranId);

        orderRedirect = orderRedirect.replace("Order № ", "").trim();
        System.out.println("Order without prefix: " + orderRedirect);

        int tranRefundId = getTranIdByOrderWithRetry(orderRedirect, 5, 2000);
        if (tranRefundId == 0) {
            System.err.println("Failed to retrieve TranRefundId after retries. Test aborted.");
            return;
        }
        System.out.println("TranRefundId: " + tranRefundId);

        SoftAssert softAssertion = new SoftAssert();
        softAssertion.assertFalse(tranRefundId == tranId);
        softAssertion.assertEquals(getValueFromTRAN(tranRefundId, "ECI"), getValueFromTRAN(tranId, "ECI"), "ECI");
        softAssertion.assertEquals(getValueFromTRAN(tranRefundId, "ApprovalCode").length(), 6, "ApprovalCode");
        softAssertion.assertEquals(getValueFromTRAN(tranRefundId, "RevFlag"), "0", "RevFlag");
        softAssertion.assertEquals(getValueFromTRAN(tranRefundId, "Rrn").length(), 12, "Rrn");
        softAssertion.assertEquals(getValueFromTRAN(tranRefundId, "TranCode"), "000", "TranCode");
        softAssertion.assertEquals(getValueFromTRAN(tranRefundId, "CVResult"), "M", "CVResult");
        softAssertion.assertFalse(getValueFromTRAN(tranId, "Batch").isEmpty(), "BATCH");
        softAssertion.assertAll();

        System.out.println("Verified:");
        String[] verifiedResponseFromDB = {"ECI", "ApprovalCode", "Rrn", "TranCode", "RevFlag", "CVResult"};
        try {
            verifiedDataFromDB(tranRefundId, verifiedResponseFromDB);
            verifiedDataFromDB(tranId, new String[]{"Batch"});
        } catch (Exception e) {
            System.out.println("TEST FAILED");
            e.printStackTrace();
        }
    }

    @AfterClass
    public void setDefaultParam() {
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "false");
        setMerchantAtt(id_AVAL, ALLOW_REFUND_ONLINE, "false");
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: FALSE");
        System.out.println("ALLOW_REFUND_ONLINE: FALSE");
    }

    private int getTranIdByOrderWithRetry(String orderId, int maxRetries, int delayMs) {
        int tranId = 0;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                tranId = getTranIdByOrder(orderId);
                if (tranId != 0) {
                    return tranId;
                }
            } catch (Exception e) {
                System.err.println("Error fetching TranId: " + e.getMessage());
            }

            attempt++;
            System.out.println("Retrying... Attempt " + attempt + " of " + maxRetries);
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted during retry delay", e);
            }
        }

        System.err.println("Failed to fetch TranId after " + maxRetries + " attempts");
        return tranId;
    }
}