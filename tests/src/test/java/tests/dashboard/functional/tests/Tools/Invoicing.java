package tests.dashboard.functional.tests.Tools;

import com.ecom.db.JDBCMethods;
import com.ecom.tests.support.DashboardRequest;
import com.ecom.ui.util.Waiters;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tests.BaseRedirect;
import com.ecom.ui.dashboard.functional.pages.KafkaEmailsPage;
import com.ecom.ui.dashboard.functional.pages.ToolsPage;

import java.util.Random;
import java.util.regex.Pattern;

import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.setMerchantAtt;
import static com.ecom.tests.support.DocumentTools.verifiedDataFromDB;
import static com.ecom.api.type.Attributes.ALLOW_PAYMENT_WITHOUT_3DS;
import static com.ecom.api.type.Attributes.MERCHANT_INVOICING_URL;

public class Invoicing extends BaseRedirect {
    public String url; // Змінено на нестатичну змінну
    public String order;

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
    public void creatingInvoice() {
        order = getSaltString();
        System.out.println("Generated order: " + order);
        DashboardRequest request = new DashboardRequest();
        url = request.createInvoice("Auto_YVP3", "777", order, "E2272727");
        System.out.println("Generated URL: " + url);
    }

    @Test(dependsOnMethods = "creatingInvoice")
    public void checkStatus() {
        DashboardRequest request = new DashboardRequest();
        boolean result = request.checkStatus("Auto_YVP3", order, "CREATE");
        System.out.println("Check status result: " + result);
        Assert.assertEquals(result, true, "Status checking");
    }

    @Test(dependsOnMethods = "checkStatus")
    public void payingInvoice() {
        DashboardRequest request = new DashboardRequest();
        order = request.payInvoice(url, cardMCmt);
        System.out.println("Order after payment: " + order);

        // Видалення префікса "Order №" з order
        order = order.replace("Order № ", "").trim();
        System.out.println("Order without prefix: " + order);

        int tranId = getTranIdByOrder(order);
        System.out.println("TranId: " + tranId);

        Waiters.sleep(2500);

        Assert.assertEquals(JDBCMethods.getValueFromTRAN(tranId, "TranCode"), "000", "TranCode");

        System.out.println("Verified:");
        String[] verifiedResponseFromDB = {"ECI", "ApprovalCode", "Rrn", "TranCode", "CVResult"};
        try {
            verifiedDataFromDB(tranId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
            e.printStackTrace();
        }
    }

    @Test(dependsOnMethods = "payingInvoice")
    public void checkStatus_paying() {
        DashboardRequest request = new DashboardRequest();
        boolean result = request.checkStatus("Auto_YVP3", order, "PURCHASE");
        System.out.println("Check status result: " + result);
        Assert.assertEquals(result, true, "Status checking");
    }

    @Test
    public void invoice_sms(){
        order = getSaltString();
        System.out.println("Generated order: " + order);
        DashboardRequest request = new DashboardRequest();
        request.createInvoice("Auto_YVP3", "777", order, "E2272727");
        ToolsPage invoicePopUp = new ToolsPage(driver);
        String phone_number = "380" + (int)(Math.random() * 1000000000);
        invoicePopUp.send_sms(phone_number);
        KafkaEmailsPage emailsPage = new KafkaEmailsPage(driver);
        String source_text = emailsPage.openEmail(phone_number + "@sms.101.sms.upc.smpp", urlmaildev, pipelineName)
                .get_source_text();
        String expepectedHeader1 = "Content-Type: text/plain; charset=UTF-8";
        String expepectedHeader2 = "Content-Transfer-Encoding: 7bit";
        Assert.assertTrue(
                source_text.contains(expepectedHeader1),
                "Expected header '" + "expepectedHeader1"  + "' not found in actual source: '" + source_text + "'"
        );

        Assert.assertTrue(
                source_text.contains(expepectedHeader2),
                "Expected header '" + "expepectedHeader1"  + "' not found in actual source: '" + source_text + "'"
        );

        int contentTypeCount = source_text.split(Pattern.quote("Content-Type"), -1).length - 1;
        Assert.assertEquals(
                contentTypeCount,
                1,
                "Header '" + "Content-Type" + "' should appear exactly once in source: '" + source_text + "'"
        );

        int contentTransferCount = source_text.split(Pattern.quote("Content-Type"), -1).length - 1;
        Assert.assertEquals(
                contentTransferCount,
                1,
                "Header '" + "Content-Type" + "' should appear exactly once in source: '" + source_text + "'"
        );
    }

    @AfterClass
    public void setDefaultParam() {
        setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "false");
        setMerchantAtt(id_AVAL4, ALLOW_PAYMENT_WITHOUT_3DS, "false");
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: FALSE");
    }
}