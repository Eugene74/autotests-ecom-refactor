package tests.dashboard.functional.tests.Login;

import methods.DashboardRequest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import tests.BaseRedirect;

import static com.ecom.db.JDBCMethods.setMerchantAtt;
import static com.ecom.db.JDBCMethods.setPropertyURLs;
import static com.ecom.api.type.Attributes.MERCHANT_INVOICING_URL;

public class LoginSuccess extends BaseRedirect {


    @BeforeClass
    public void setParam() {
        setPropertyURLs("TermUrl01", TermURL);
        setPropertyURLs("TermUrl02", TermURL);
        setPropertyURLs("TermUrl", TermURL);
        setPropertyURLs("ActiveServerURL", ActiveServerURL);
        setPropertyURLs("TermUrl99", TermURL);
        setPropertyURLs("3DSCallbackUrl01", CallbackURl);
        setPropertyURLs("3DSCallbackUrl99", CallbackURl);
        setPropertyURLs("3DSCallbackUrl02", CallbackURl);
        setMerchantAtt(id_AVAL1, MERCHANT_INVOICING_URL, URLInvocing);
    }

    @Test
    public static void loginDashboard() {
        DashboardRequest request = new DashboardRequest();
        String profile_link = request.LogInSuccess();

        // Логирование для отладки
        System.out.println("Received Profile Link: " + profile_link);

        // Проверка, что profile_link не является null или пустым
        Assert.assertNotNull(profile_link, "Profile link is null");
        Assert.assertFalse(profile_link.isEmpty(), "Profile link is empty");

        // Ожидаемый шаблон URL (нечувствительный к регистру)
        String expectedUrlPattern = "(?i)" + URLmt + "/dashboard/paylink-transactions\\?.*";
        long timeout = 10_000; // 10 seconds
        long startTime = System.currentTimeMillis();
        // Логирование ожидаемого шаблона
        System.out.println("Expected URL Pattern: " + expectedUrlPattern);
        while (System.currentTimeMillis() - startTime < timeout) {
            if (profile_link.matches(expectedUrlPattern)) {
                break;
            }
            profile_link = request.getDriver().getCurrentUrl().toString();
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }


        // Проверка, что URL соответствует шаблону
        Assert.assertTrue(profile_link.matches(expectedUrlPattern),
                "URL does not match the expected pattern. Actual: " + profile_link);
    }
}