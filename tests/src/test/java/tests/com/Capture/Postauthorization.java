package tests.com.Capture;

import static com.ecom.core.config.EnvData.MERCHANT_ID_AVAL_1;
import static com.ecom.core.config.EnvData.TERMINAL_ID_AVAL_1;
import static com.ecom.core.config.EnvData.URL_TOMCAT;

import com.ecom.db.JDBCConnection;
import com.ecom.tests.base.BaseUiTest;
import com.ecom.utils.ResourceUtils;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Postauthorization extends BaseUiTest {

  @Test
  public void testTransaction() {
    try {
      // Отримання значень з бази даних
      String[] dbValues = getDatabaseValues();
      String orderId = dbValues[0];
      String approvalCode = dbValues[1];
      String rrn = dbValues[2];

      // Вивід значень, отриманих з бази даних
      // System.out.println("Значення, отримані з бази даних:");
      System.out.println("OrderID: " + orderId);
      System.out.println("ApprovalCode: " + approvalCode);
      System.out.println("RRN: " + rrn);

      // Отримуємо значення з файлу властивостей
      String merchantId = MERCHANT_ID_AVAL_1;
      String terminalId = TERMINAL_ID_AVAL_1;

      // Отримуємо шлях до HTML шаблону
      String htmlContent = ResourceUtils.readAsString("Html/postauthorization_form.html");

      // Замінюємо змінні у HTML контенті
      htmlContent =
          htmlContent
              .replace("${base.url}", URL_TOMCAT)
              .replace("${MerchantID}", merchantId)
              .replace("${TerminalID}", terminalId)
              .replace("${OrderID}", orderId)
              .replace("${ApprovalCode}", approvalCode)
              .replace("${RRN}", rrn);

      // Створюємо тимчасовий файл у тимчасовій директорії операційної системи
      Path tempFilePath = Files.createTempFile("postauthorization_form_fixed", ".html");

      try (BufferedWriter writer = Files.newBufferedWriter(tempFilePath, StandardCharsets.UTF_8)) {
        writer.write(htmlContent);
      }

      // Відкриваємо сторінку з цим HTML файлом
      String finalUrl = "file:///" + tempFilePath.toAbsolutePath().toString().replace("\\", "/");
      driver.get(finalUrl);

      // Натискання на кнопку submit
      WebElement submitButton =
          new WebDriverWait(driver, Duration.ofSeconds(20))
              .until(ExpectedConditions.elementToBeClickable(By.id("submit")));
      submitButton.click();

      // Очікування завантаження сторінки після натискання submit
      waitForPageLoad();
      Thread.sleep(2000); // Примусова пауза

      // Отримання всього тексту з body
      String bodyText = driver.findElement(By.tagName("body")).getText();
      System.out.println("" + bodyText);

      // Перевірки значень
      boolean testPassed = true;

      if (!bodyText.contains("TranCode=000")) {
        System.out.println("TranCode не дорівнює 000, отримано: " + bodyText);
        testPassed = false;
      }

      if (!bodyText.contains("TerminalID=" + terminalId)) {
        System.out.println("TerminalID не співпадає, отримано: " + bodyText);
        testPassed = false;
      }

      if (!bodyText.contains("MerchantID=" + merchantId)) {
        System.out.println("MerchantID не співпадає, отримано: " + bodyText);
        testPassed = false;
      }

      Assert.assertTrue(testPassed, "Тест не пройдено, перевірка значень не вдалася.");

      if (testPassed) {
        System.out.println("Тест пройдено успішно.");
      }

    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail("Тест не пройдено через виняток: " + e.getMessage());
    }
  }

  private String[] getDatabaseValues() {
    String[] values = new String[3];
    try (Connection connection = JDBCConnection.getDBConnection();
        PreparedStatement statement =
            connection.prepareStatement(
                "SELECT RRN, APPROVAL_CODE, ORDER_ID FROM TRAN WHERE TRAN_ID = (SELECT MAX(TRAN_ID) FROM TRAN)")) {
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        values[0] = resultSet.getString("ORDER_ID"); // ORDER_ID
        values[1] = resultSet.getString("APPROVAL_CODE"); // APPROVAL_CODE
        values[2] = resultSet.getString("RRN"); // RRN
      }
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail("Не вдалося виконати запит до бази даних: " + e.getMessage());
    }
    return values;
  }

  private void waitForPageLoad() {
    new WebDriverWait(driver, Duration.ofSeconds(30))
        .until(
            webDriver ->
                ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState")
                    .equals("complete"));
  }
}
