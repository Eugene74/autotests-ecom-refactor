package tests.com.Capture;

import static com.ecom.core.config.CardConfig.cardVISA;
import static com.ecom.core.config.EnvData.ID_AVAL;
import static com.ecom.core.config.EnvData.MERCHANT_ID_AVAL_1;
import static com.ecom.core.config.EnvData.TERMINAL_ID_AVAL_1;
import static com.ecom.core.config.EnvData.URL_TOMEE;

import com.ecom.api.type.Attributes;
import com.ecom.db.JDBCConnection;
import com.ecom.db.JDBCMethods;
import com.ecom.tests.base.BaseUiTest;
import com.ecom.utils.ResourceUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class Preauthorization extends BaseUiTest {

  @BeforeClass
  public void setUp() {
    System.out.println("Setting up the test environment...");

    System.out.println("id_AVAL: " + ID_AVAL);
    // Встановлюємо значення атрибута перед тестом
    JDBCMethods.setMerchantAtt(ID_AVAL, Attributes.USE_NEW_FRONT_END, "true");
    JDBCMethods.setMerchantAtt(ID_AVAL, Attributes.DISABLE_EMAIL_FIELD, "false");
    JDBCMethods.setMerchantAtt(ID_AVAL, Attributes.USE_CARDHOLDER_NAME, "false");
    JDBCMethods.setMerchantAtt(ID_AVAL, Attributes.ALLOW_PAYMENT_WITHOUT_3DS, "true");
  }

  @AfterClass
  public void tearDown() {
    System.out.println("Tearing down the test environment...");
    // Встановлюємо значення атрибута після тесту
    JDBCMethods.setMerchantAtt(ID_AVAL, Attributes.USE_NEW_FRONT_END, "false");
    JDBCMethods.setMerchantAtt(ID_AVAL, Attributes.DISABLE_EMAIL_FIELD, "true");
  }

  @Test
  public void testTransaction() {
    try {
      System.out.println("Starting testTransaction...");
      // Отримання значень з бази даних
      String[] dbValues = getDatabaseValues();
      String orderId = dbValues[0];
      String approvalCode = dbValues[1];
      String rrn = dbValues[2];

      // Логування значень з бази даних
      // System.out.println("OrderID: " + orderId);
      // System.out.println("ApprovalCode: " + approvalCode);
      // System.out.println("RRN: " + rrn);

      // Отримуємо значення з файлу властивостей
      String baseUrl = URL_TOMEE;
      String merchantId = MERCHANT_ID_AVAL_1;
      String terminalId = TERMINAL_ID_AVAL_1;

      // Логування значень з файлу властивостей
      //  System.out.println("baseUrl: " + baseUrl);
      System.out.println("merchantId: " + merchantId);
      System.out.println("terminalId: " + terminalId);

      // Отримуємо шлях до HTML шаблону
      String htmlContent = ResourceUtils.readAsString("Html/preauthorization_form.html");

      // Замінюємо змінні у HTML-контенті
      htmlContent =
          htmlContent
              .replace("${base.url}", baseUrl)
              .replace("${MerchantID}", merchantId)
              .replace("${TerminalID}", terminalId)
              .replace("${OrderID}", orderId)
              .replace("${ApprovalCode}", approvalCode != null ? approvalCode : "")
              .replace("${RRN}", rrn);

      // Створюємо тимчасовий файл
      Path tempFilePath = Files.createTempFile("preauthorization_form_", ".html");
      Files.write(tempFilePath, htmlContent.getBytes(StandardCharsets.UTF_8));

      // Відкриваємо тимчасовий файл у браузері
      String finalUrl = "file:///" + tempFilePath.toAbsolutePath().toString().replace("\\", "/");
      driver.get(finalUrl);
      String[] cardDetails = cardVISA;
      String cardNumber = cardDetails[0];
      String expMonthYear = cardDetails[2] + "/" + cardDetails[1].substring(2); // MM/YY формат
      String cvv = cardDetails[3];
      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
      // Перше натискання на кнопку submit
      WebElement submitButton =
          wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
      submitButton.click();

      // Очікування завантаження після натискання submit
      waitForPageLoad();
      Thread.sleep(2000); // Примусова пауза у 5 секунд

      // Заповнюємо форму з очікуванням активності полів
      WebElement cardNumberInput =
          wait.until(ExpectedConditions.elementToBeClickable(By.id("card-number-input")));
      WebElement expDateInput =
          wait.until(ExpectedConditions.elementToBeClickable(By.id("expiry-date-input")));
      WebElement cvvInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("cvc-input")));
      WebElement emailInput =
          wait.until(ExpectedConditions.elementToBeClickable(By.id("email-input")));

      // Клік на поле введення номеру картки
      cardNumberInput.click();

      cardNumberInput.sendKeys(cardNumber);
      expDateInput.sendKeys(expMonthYear);
      cvvInput.sendKeys(cvv);
      emailInput.sendKeys("viacheslav.yarosh@upc.ua");

      // Повторне натискання на кнопку "Pay"
      WebElement continueButton =
          wait.until(
              ExpectedConditions.elementToBeClickable(
                  By.cssSelector("button.action-button.go-payment-form__action-button")));
      continueButton.click();

      // Очікування завантаження сторінки після натискання "Pay"
      waitForPageLoad();
      Thread.sleep(2000); // для тестового середовища встановити 35000

      // Очікуємо, що з'явиться "Transaction Status" зі значенням "Successful"
      WebElement transactionStatusElement =
          wait.until(
              ExpectedConditions.visibilityOfElementLocated(
                  By.xpath(
                      "//li[@class='go-tran-info__row'][div[text()='Transaction Status']]/div[@class='go-tran-info__value go-tran-info__value_success' and text()='Successful']")));
      String transactionStatus = transactionStatusElement.getText();
      Assert.assertEquals(transactionStatus, "Successful", "Transaction Status не 'Successful'");
      System.out.println("Transaction Status: 'Successful'.");

      // Спробуємо знайти кнопку "Send receipt"
      WebElement quittungSendenButton;
      try {
        quittungSendenButton =
            wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.xpath(
                        "//button[@class='action-button go-result-view__send-email-button' and span[text()='Send receipt']]")));
        //  System.out.println("Елемент знайдено! Натискаємо кнопку 'Send receipt'");

        // Перевіряємо, чи поле email активне перед натисканням
        WebElement emailInputField = driver.findElement(By.id("email-input"));
        Assert.assertTrue(emailInputField.isEnabled(), "Поле email не активне.");

        // Натискаємо на кнопку "Send receipt"
        quittungSendenButton.click();
      } catch (Exception e) {
        System.out.println("Елемент не знайдено! Кнопку 'Send receipt' не натиснуто.");
        Assert.fail("Елемент не знайдено! Кнопку 'Send receipt' не натиснуто.");
        return;
      }

      // Очікування завантаження сторінки після натискання "Send receipt"
      waitForPageLoad();
      try {
        // Шукаємо елемент з текстом "Receipt sent successfully"
        WebElement receiptSentElement =
            wait.until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.xpath(
                        "//div[@class='go-result-view__receipt-sent' and span[text()='Receipt sent successfully']]")));
        System.out.println("Тест пройдено успішно.");
      } catch (TimeoutException e) {
        // Перевірка, чи кнопка "Send receipt" залишилася активною після натискання
        if (quittungSendenButton.isEnabled()) {
          System.out.println(
              "Лист не відправлено, кнопка 'Send receipt' залишилася активною. Тест не пройдено.");
          Assert.fail(
              "Тест не пройдено - кнопка 'Send receipt' залишилася активною після натискання.");
        } else {
          System.out.println(
              "Лист не відправлено, кнопка 'Send receipt' не активна. Тест не пройдено.");
          Assert.fail("Тест не пройдено - лист не відправлено.");
        }
      }

    } catch (IOException | InterruptedException e) {
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

        // Виводимо значення в консоль
        System.out.println("Значення, отримані з бази даних:");
        System.out.println("OrderID: " + values[0]);
        System.out.println("ApprovalCode: " + values[1]);
        System.out.println("RRN: " + values[2]);
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
