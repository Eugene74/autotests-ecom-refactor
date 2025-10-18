package tests.paylink.redirect.test.aval;

import methods.RedirectRequest;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseRedirect;

import static jdbc.JDBCMethods.getTranIdByOrder;
import static jdbc.JDBCMethods.getValueFromTRAN;
import static jdbc.JDBCMethods.setMerchantAtt;
import static type.Attributes.ALLOW_PAYMENT_WITHOUT_3DS;
import static type.Attributes.MERCHANT_INVOICING_URL;

public class AccountVerification extends BaseRedirect {

    @BeforeClass
    public void setParam() {
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "true");
        setMerchantAtt(id_AVAL, MERCHANT_INVOICING_URL, URLInvocing);
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
    }

    @Test
    public void lookUp() {
        RedirectRequest pay = new RedirectRequest();
        String orderRedirect = pay.accountVerify(cardVISA);
        System.out.println("Order Redirect: " + orderRedirect);

        // Логика повторных попыток для получения tranId
        int tranId = getTranIdByOrderWithRetry(orderRedirect, 5, 2000);
        if (tranId == 0) {
            System.err.println("Failed to retrieve Transaction ID after retries. Test aborted.");
            return;
        }
        System.out.println("Transaction ID: " + tranId);

        SoftAssert softAssertion = new SoftAssert();

        String eci = getValueFromTRAN(tranId, "ECI");
        System.out.println("ECI: " + eci);
        softAssertion.assertEquals(eci, "07", "ECI");

        String approvalCode = getValueFromTRAN(tranId, "ApprovalCode");
        System.out.println("Approval Code: " + approvalCode);
        softAssertion.assertEquals(approvalCode.length(), 6, "ApprovalCode");

        String rrn = getValueFromTRAN(tranId, "Rrn");
        System.out.println("RRN: " + rrn);
        softAssertion.assertEquals(rrn.length(), 12, "Rrn");

        String tranCode = getValueFromTRAN(tranId, "TranCode");
        System.out.println("Transaction Code: " + tranCode);
        softAssertion.assertEquals(tranCode, "000", "TranCode");

        softAssertion.assertAll();
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
                    return tranId; // Успешно получили Transaction ID
                }
            } catch (Exception e) {
                System.err.println("Error fetching Transaction ID: " + e.getMessage());
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

        System.err.println("Failed to fetch Transaction ID after " + maxRetries + " attempts");
        return tranId; // Возвращаем 0, если не удалось получить Transaction ID
    }
}