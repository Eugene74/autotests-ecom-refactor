package tests.paylink.redirect.test.aval;

import com.ecom.tests.base.BaseUiTest;
import com.ecom.tests.steps.VerificationSteps;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.ecom.api.type.Attributes.ALLOW_PAYMENT_WITHOUT_3DS;
import static com.ecom.api.type.Attributes.MERCHANT_INVOICING_URL;
import static com.ecom.core.config.CardConfig.cardVISA;
import static com.ecom.core.config.EnvData.URLInvocing;
import static com.ecom.core.config.EnvData.URLredirect;
import static com.ecom.core.config.EnvData.id_AVAL;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.getValueFromTRAN;
import static com.ecom.db.JDBCMethods.setMerchantAtt;

public class AccountVerification extends BaseUiTest {

    @BeforeClass
    public void setParam() {
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "true");
        setMerchantAtt(id_AVAL, MERCHANT_INVOICING_URL, URLInvocing);
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
    }

    @Test
    public void lookUp() {
        String orderRedirect = new VerificationSteps(driver).verifyAccount(cardVISA, URLredirect);
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