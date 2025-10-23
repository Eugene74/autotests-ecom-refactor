package tests.dashboard.functional.tests.Tools;

import com.ecom.db.JDBCMethods;
import com.ecom.tests.base.BaseUiTest;
import com.ecom.tests.support.DashboardRequest;
import com.ecom.ui.util.Waiters;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Random;

import static com.ecom.api.type.Attributes.ALLOW_PAYMENT_WITHOUT_3DS;
import static com.ecom.api.type.Attributes.MERCHANT_INVOICING_URL;
import static com.ecom.core.config.CardConfig.cardMCmt;
import static com.ecom.core.config.EnvData.URLInvocing;
import static com.ecom.core.config.EnvData.id_AVAL;
import static com.ecom.core.config.EnvData.id_AVAL4;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.setMerchantAtt;
import static com.ecom.tests.support.DocumentTools.verifiedDataFromDB;

public class Multipay_Invoices extends BaseUiTest {
    public String url; // Змінено на нестатичну змінну
    public String order; // Змінено на нестатичну змінну
    public String invoiceId; // Змінна для збереження invoice_id

    @BeforeClass
    public void setParam() {
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "true");
        setMerchantAtt(id_AVAL, MERCHANT_INVOICING_URL, URLInvocing);
        setMerchantAtt(id_AVAL4, ALLOW_PAYMENT_WITHOUT_3DS, "true");
        setMerchantAtt(id_AVAL4, MERCHANT_INVOICING_URL, URLInvocing);
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
    }

    protected String getSaltString() {
        String SALTCHARS = "abcdefghijklmnopqrstuvwxvz1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    @Test
    public void creatingMultipayInvoice() {
        order = getSaltString();
        System.out.println("Generated order: " + order);
        DashboardRequest request = new DashboardRequest();
        url = request.createMultipayInvoice("Auto_YVP3", "777", order, "E2272727");
        System.out.println("Generated URL: " + url);
    }

    @Test(dependsOnMethods = "creatingMultipayInvoice")
    public void checkStatusMultipayInvoice() {
        DashboardRequest request = new DashboardRequest();
        boolean result = request.checkMultipayStatus("Auto_YVP3", order, "CREATE");
        System.out.println("Check status result: " + result);
        Assert.assertEquals(result, true, "Status checking");
    }

    @Test(dependsOnMethods = "checkStatusMultipayInvoice")
    public void payingMultipayInvoice() {
        DashboardRequest request = new DashboardRequest();
        order = request.payMultipayInvoice(url, cardMCmt);
        System.out.println("Order after payment: " + order);

        // Видалення префікса "Order №" з order
        order = order.replace("Order № ", "").trim();
        System.out.println("Order without prefix: " + order);

        int tranId = getTranIdByOrder(order);
        System.out.println("TranId: " + tranId);

        // Перевірка, чи tranId є валідним
        Assert.assertTrue(tranId > 0, "TranId should be a positive number");

        Waiters.sleep(5000); // Час очікування, щоб дати базі даних час оновитися

        String tranCode = JDBCMethods.getValueFromTRAN(tranId, "TranCode");
        System.out.println("TranCode: " + tranCode);

        // Перевірка TranCode
        Assert.assertEquals(tranCode, "000", "TranCode");

        System.out.println("Verified:");
        String[] verifiedResponseFromDB = {"ECI", "ApprovalCode", "Rrn", "TranCode", "CVResult"};
        try {
            verifiedDataFromDB(tranId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
            e.printStackTrace();
        }
    }

    @AfterClass
    public void setDefaultParam() {
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "false");
        setMerchantAtt(id_AVAL4, ALLOW_PAYMENT_WITHOUT_3DS, "false");
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: FALSE");
    }

    // Додатковий метод для отримання правильного значення invoiceId
    private String getInvoiceIdFromOrder(String order) {
        if (order.startsWith("Order № ")) {
            return order.substring(8).trim(); // Видаляємо префікс "Order №"
        }
        return order;
    }
}