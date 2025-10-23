package tests.com.Service01;

import static com.ecom.core.config.EnvData.MERCHANT_ID_AVAL_1;
import static com.ecom.core.config.EnvData.TERMINAL_ID_AVAL;
import static com.ecom.core.config.EnvData.URL_TOMEE;

import com.ecom.tests.base.BaseTestService01;
import com.ecom.utils.ResourceUtils;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Service01_HTML extends BaseTestService01 {

  @Test
  public void HTML() {
    try {
      // Отримання значень з бази даних
      String[] dbValues = getDatabaseValues();
      String orderId = dbValues[0];
      String approvalCode = dbValues[1];
      String rrn = dbValues[2];

      // Вивід значень, отриманих з бази даних
      // System.out.println("OrderID: " + orderId);
      // System.out.println("ApprovalCode: " + approvalCode);
      // System.out.println("RRN: " + rrn);
      // Отримуємо шлях до HTML шаблону
      Path templatePath = ResourceUtils.toTempFile("Html/servace01.html");
      String htmlContent = new String(Files.readAllBytes(templatePath), StandardCharsets.UTF_8);

      // Замінюємо змінні у HTML контенті
      htmlContent =
          htmlContent
              .replace("${base.url}", URL_TOMEE)
              .replace("${MerchantID}", MERCHANT_ID_AVAL_1)
              .replace("${TerminalID}", TERMINAL_ID_AVAL)
              .replace("${OrderID}", orderId);

      // Створюємо тимчасовий файл у тимчасовій директорії операційної системи
      Path tempFilePath = Files.createTempFile("servace01_form_fixed", ".html");

      try (BufferedWriter writer = Files.newBufferedWriter(tempFilePath, StandardCharsets.UTF_8)) {
        writer.write(htmlContent);
      }

      // Відкриваємо сторінку з цим HTML файлом
      String finalUrl = "file:///" + tempFilePath.toAbsolutePath().toString().replace("\\", "/");
      getDriver().get(finalUrl);

      // Натискання на кнопку submit
      WebElement submitButton =
          new WebDriverWait(getDriver(), Duration.ofSeconds(20))
              .until(ExpectedConditions.elementToBeClickable(By.id("submit")));
      submitButton.click();

      // Очікування завантаження сторінки після натискання submit
      waitForPageLoad();
      Thread.sleep(2000); // Примусова пауза

      // Отримання всього тексту з body
      String bodyText = getDriver().findElement(By.tagName("body")).getText();
      System.out.println("" + bodyText);

      // Перевірки значень
      boolean testPassed = true;

      if (!bodyText.contains("TranCode=000")) {
        System.out.println("TranCode не дорівнює 000, отримано: " + bodyText);
        testPassed = false;
      }

      if (!bodyText.contains("Currency=980")) {
        System.out.println("Currency не співпадає, отримано: " + bodyText);
        testPassed = false;
      }
      //            if (!bodyText.contains("TerminalID=E1000027")) {
      //                System.out.println("TerminalID не співпадає, отримано: " + bodyText);
      //                testPassed = false;
      //            }
      //
      //            if (!bodyText.contains("MerchantID=1000027")) {
      //                System.out.println("MerchantID не співпадає, отримано: " + bodyText);
      //                testPassed = false;
      //            }
      if (!bodyText.contains("OrderID=" + orderId)) {
        System.out.println("OrderID не співпадає, отримано: " + bodyText);
        testPassed = false;
      }
      if (!bodyText.contains("ApprovalCode=" + approvalCode)) {
        System.out.println("ApprovalCode не співпадає, отримано: " + bodyText);
        testPassed = false;
      }
      if (!bodyText.contains("RRN=" + rrn)) {
        System.out.println("RRN не співпадає, отримано: " + bodyText);
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

  private void waitForPageLoad() {
    new WebDriverWait(getDriver(), Duration.ofSeconds(30))
        .until(
            webDriver ->
                ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState")
                    .equals("complete"));
  }
}
