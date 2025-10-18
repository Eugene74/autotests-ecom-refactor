package tests.paylink.redirect.test.aval;

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

public class PreAuthorizationPostLessAmount extends BaseRedirect {
    private static String orderRedirect;
    private static int tranId;

    @BeforeClass
    public static void setParam() {
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "true");
        setMerchantAtt(id_AVAL, MERCHANT_INVOICING_URL, URLInvocing);
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
    }

    @Test
    public void mcPreAuthorization() {
        RedirectRequest pay = new RedirectRequest();
        orderRedirect = pay.paymentAuthorization(cardMC, 1, merchant_AVAL, terminal_AVAL);
        pay.usedCVC(cardMC);

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

    @Test(dependsOnMethods = "mcPreAuthorization")
    public void mcPreAuthorizationPost() {
        RedirectRequest post = new RedirectRequest();
        post.paymentPostAuthorizationDifAmount(orderRedirect, tranId, -0.2);

        orderRedirect = orderRedirect.replace("Order № ", "").trim();
        System.out.println("Order without prefix: " + orderRedirect);

        int tranPostId = getTranIdByOrderWithRetry(orderRedirect, 5, 2000);
        if (tranPostId == 0) {
            System.err.println("Failed to retrieve TranPostId after retries. Test aborted.");
            return;
        }
        System.out.println("TranPostId: " + tranPostId);

        SoftAssert softAssertion = new SoftAssert();
        softAssertion.assertFalse(tranPostId == tranId);
        softAssertion.assertEquals(getValueFromTRAN(tranPostId, "TranCode"), "000", "TranCode");
        softAssertion.assertEquals(getValueFromTRAN(tranPostId, "ECI"), getValueFromTRAN(tranId, "ECI"), "ECI");
        softAssertion.assertAll();

        System.out.println("Verified:");
        String[] verifiedResponseFromDB = {"ECI", "TranCode"};
        try {
            verifiedDataFromDB(tranPostId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void setDefaultParam() {
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "false");
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: FALSE");
    }

    private static int getTranIdByOrderWithRetry(String orderId, int maxRetries, int delayMs) {
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