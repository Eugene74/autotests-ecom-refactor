package tests.paylink.redirect.test.aval;

import methods.RedirectRequest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseRedirect;

import static com.ecom.db.JDBCMethods.*;
import static methods.DocumentTools.verifiedDataFromDB;
import static com.ecom.api.type.Attributes.ALLOW_PAYMENT_WITHOUT_3DS;
import static com.ecom.api.type.Attributes.MERCHANT_INVOICING_URL;

public class AuthorizationRevers extends BaseRedirect {
    private static String orderRedirect;
    private static int tranId;

    @BeforeClass
    public void setParam() {
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "true");
        setMerchantAtt(id_AVAL, MERCHANT_INVOICING_URL, URLInvocing);
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
    }

    @Test
    public void authorizationPay() {
        RedirectRequest pay = new RedirectRequest();
        orderRedirect = pay.paymentAuthorization(cardVISA, 0, merchant_AVAL, terminal_AVAL);
        pay.usedCVC(cardVISA);
        System.out.println("Order: " + orderRedirect);

        // Удаление префикса "Order №" из orderRedirect
        orderRedirect = orderRedirect.replace("Order № ", "").trim();
        System.out.println("Order without prefix: " + orderRedirect);

        // Логика повторных попыток для получения tranId
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
    public void authorizationRevers() {
        RedirectRequest revers = new RedirectRequest();
        revers.doReversal(URL_MERCH, orderRedirect, tranId);

        exit();

        // Удаление префикса "Order №" из orderRedirect
        orderRedirect = orderRedirect.replace("Order № ", "").trim();
        System.out.println("Order without prefix: " + orderRedirect);

        // Логика повторных попыток для получения tranReversId
        int tranReversId = getTranIdByOrderWithRetry(orderRedirect, 5, 2000);
        if (tranReversId == 0) {
            System.err.println("Failed to retrieve TranReversId after retries. Test aborted.");
            return;
        }
        System.out.println("TranReversId: " + tranReversId);

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
            e.printStackTrace();
        }
    }

    @AfterClass
    public void setDefaultParam() {
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "false");
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: FALSE");
    }

    // Метод с логикой повторных попыток
    private int getTranIdByOrderWithRetry(String orderId, int maxRetries, int delayMs) {
        int tranId = 0;
        int attempt = 0;

        while (attempt < maxRetries) {
            try {
                tranId = getTranIdByOrder(orderId);
                if (tranId != 0) {
                    return tranId; // Успешно получили TranId
                }
            } catch (Exception e) {
                System.err.println("Error fetching TranId: " + e.getMessage());
            }

            attempt++;
            System.out.println("Retrying... Attempt " + attempt + " of " + maxRetries);
            try {
                Thread.sleep(delayMs); // Задержка перед повторной попыткой
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted during retry delay", e);
            }
        }

        System.err.println("Failed to fetch TranId after " + maxRetries + " attempts");
        return tranId; // Возвращаем 0, если не удалось получить TranId
    }
}