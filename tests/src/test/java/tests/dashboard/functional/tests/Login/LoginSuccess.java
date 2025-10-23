package tests.dashboard.functional.tests.Login;

import static com.ecom.api.type.Attributes.MERCHANT_INVOICING_URL;
import static com.ecom.core.config.EnvData.*;
import static com.ecom.db.JDBCMethods.setMerchantAtt;
import static com.ecom.db.JDBCMethods.setPropertyURLs;

import com.codeborne.selenide.WebDriverRunner;
import com.ecom.tests.base.BaseUiTest;
import com.ecom.tests.support.DashboardRequest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class LoginSuccess extends BaseUiTest {

  @BeforeClass
  public void setParam() {
    setPropertyURLs("TermUrl01", TERM_URL);
    setPropertyURLs("TermUrl02", TERM_URL);
    setPropertyURLs("TermUrl", TERM_URL);
    setPropertyURLs("ActiveServerURL", ACTIVE_SERVER_URL);
    setPropertyURLs("TermUrl99", TERM_URL);
    setPropertyURLs("3DSCallbackUrl01", CALLBACK_URL);
    setPropertyURLs("3DSCallbackUrl99", CALLBACK_URL);
    setPropertyURLs("3DSCallbackUrl02", CALLBACK_URL);
    setMerchantAtt(ID_AVAL_1, MERCHANT_INVOICING_URL, URL_INVOCING);
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
    String expectedUrlPattern = "(?i)" + URL_TOMCAT + "/dashboard/paylink-transactions\\?.*";
    long timeout = 10_000; // 10 seconds
    long startTime = System.currentTimeMillis();
    // Логирование ожидаемого шаблона
    System.out.println("Expected URL Pattern: " + expectedUrlPattern);
    while (System.currentTimeMillis() - startTime < timeout) {
      if (profile_link.matches(expectedUrlPattern)) {
        break;
      }
      profile_link = WebDriverRunner.url();
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }

    // Проверка, что URL соответствует шаблону
    Assert.assertTrue(
        profile_link.matches(expectedUrlPattern),
        "URL does not match the expected pattern. Actual: " + profile_link);
  }
}
