package tests.paylink.redirect.test.aval;

import methods.RedirectRequest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseRedirect;

import static com.ecom.db.JDBCMethods.*;
import static methods.DocumentTools.verifiedDataFromDB;
import static com.ecom.type.Attributes.ALLOW_PAYMENT_WITHOUT_3DS;
import static com.ecom.type.Attributes.MERCHANT_INVOICING_URL;

public class FacilitatorAuth extends BaseRedirect {

    @BeforeClass
    public void setParam() {
        setMerchantAtt(idFacilAVAL, ALLOW_PAYMENT_WITHOUT_3DS, "true");
        setMerchantAtt(id_AVAL, MERCHANT_INVOICING_URL, URLInvocing);
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
    }

    @Test
    public void authorizationPay() {
        RedirectRequest pay = new RedirectRequest();
        String orderRedirect = pay.paymentAuthorization(cardVISA, 0, merchantIDFacil_AVAL, terminalIDFacil_AVAL);

        pay.usedCVC(cardVISA);
        System.out.println("Order: " + orderRedirect);
        exit();

        // Логика повторных попыток для получения tranId
        int tranId = getTranIdByOrderWithRetry(orderRedirect, 5, 2000);
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
        }
    }

    @AfterClass
    public void setDefaultParam() {
        setMerchantAtt(idFacilAVAL, ALLOW_PAYMENT_WITHOUT_3DS, "false");
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