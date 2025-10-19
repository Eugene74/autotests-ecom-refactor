package tests.com.Kafka;

import com.ecom.core.config.PropertiesManager;
import com.ecom.db.JDBCConnection;
import com.ecom.db.JDBCMethods;
import com.ecom.ui.driver.WebDriverHolder;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import com.ecom.api.type.Attributes;
import java.time.Duration;


import com.ecom.core.util.ResourceUtils;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class AVAL_New_front {
    private WebDriver driver;
    private WebDriverWait wait;
    private Properties properties;
    private Properties cardProperties;
    private static Integer id_AVAL;
    private String locale;
    private String languageXPath;

    public AVAL_New_front(String locale, String languageXPath) {
        this.locale = locale;
        this.languageXPath = languageXPath;
    }

    @BeforeClass
    public void setUp() {
        // Завантажуємо властивості з endpoint.properties
        PropertiesManager manager = PropertiesManager.getInstance();
        properties = manager.getEnvProperties();
        id_AVAL = Integer.valueOf(properties.getProperty("idAVAL1." +
                System.getProperty("env", properties.getProperty("default_env"))
        ));

        // Встановлюємо значення атрибута перед тестом
        JDBCMethods.setMerchantAtt(id_AVAL, Attributes.USE_NEW_FRONT_END, "true");
        JDBCMethods.setMerchantAtt(id_AVAL, Attributes.DISABLE_EMAIL_FIELD, "false");
        JDBCMethods.setMerchantAtt(id_AVAL, Attributes.USE_CARDHOLDER_NAME, "false");
        JDBCMethods.setMerchantAtt(id_AVAL, Attributes.ALLOW_PAYMENT_WITHOUT_3DS, "true");

        // Check if chromedriver is in PATH
        boolean isDriverInPath = false;
        try {
            Process process = new ProcessBuilder("chromedriver", "--version").start();
            int exitCode = process.waitFor();
            isDriverInPath = (exitCode == 0);
        } catch (Exception e) {
            isDriverInPath = false;
        }

        // Set path only if not found in PATH
        if (!isDriverInPath) {
            System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
        }

        // Створюємо ChromeOptions для налаштування WebDriver
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(properties.getProperty("is_headless", "false"))){
            options.addArguments("--headless");
            options.addArguments("--window-size=3840,2160"); // Maximum 4K resolution
        }
        options.addArguments("--allow-file-access-from-files");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--allow-insecure-localhost");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-file-access");
        options.addArguments("--lang=en");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        // Ініціюємо WebDriver
        driver = new ChromeDriver(options);
        WebDriverHolder.setDriver(driver);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
       // wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Зменшено час очікування
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        if (Boolean.parseBoolean(properties.getProperty("is_headless", "false"))) {
            driver.manage().window().setSize(new Dimension(3840, 2160));
        } else {
            driver.manage().window().maximize();
        }

        // Завантажуємо властивості з cards.properties
        cardProperties = manager.getCardProperties();
    }

    @AfterClass
    public void tearDown() {
        // Встановлюємо значення атрибута після тесту
        JDBCMethods.setMerchantAtt(id_AVAL, Attributes.USE_NEW_FRONT_END, "false");
        JDBCMethods.setMerchantAtt(id_AVAL, Attributes.DISABLE_EMAIL_FIELD, "true");

        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testTransaction() {
        try {
            // Отримання значень з бази даних
            String[] dbValues = getDatabaseValues();
            String orderId = dbValues[0];
            String approvalCode = dbValues[1];
            String rrn = dbValues[2];

            // Отримуємо значення з файлу властивостей
            String baseUrl = properties.getProperty("URLtomee");
            String merchantId = properties.getProperty("MerchantID_AVAL1");
            String terminalId = properties.getProperty("TerminalID_AVAL1");

            // Отримуємо шлях до HTML шаблону
            String htmlContent = ResourceUtils.readAsString("Html/authorization_form.html");

            // Замінюємо змінні у HTML контенті
            htmlContent = htmlContent.replace("${base.url}", baseUrl)
                    .replace("${MerchantID}", merchantId)
                    .replace("${TerminalID}", terminalId)
                    .replace("${OrderID}", orderId)
                    .replace("${ApprovalCode}", approvalCode)
                    .replace("${RRN}", rrn);

            // Створюємо тимчасовий файл у тимчасовій директорії операційної системи
            Path tempFilePath = Files.createTempFile("transaction_form_fixed", ".html");

            try (BufferedWriter writer = Files.newBufferedWriter(tempFilePath, StandardCharsets.UTF_8)) {
                writer.write(htmlContent);
            }

            // Відкриваємо сторінку з цим HTML файлом
            String finalUrl = "file:///" + tempFilePath.toAbsolutePath().toString().replace("\\", "/");
            driver.get(finalUrl);

            // Логуємо поточну мову тесту
            System.out.println("Тест для locale: " + locale);

            // Пауза для завантаження сторінки
            Thread.sleep(2000);

            // Завантажуємо значення з cardProperties
            String cardMC = cardProperties.getProperty("cardVISA");
            String[] cardDetails = cardMC.split(";");

            String cardNumber = cardDetails[0];
            String expMonthYear = cardDetails[2] + "/" + cardDetails[1].substring(2); // MM/YY формат
            String cvv = cardDetails[3];

            // Перше натискання на кнопку submit
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
            submitButton.click();

            // Очікування завантаження після натискання submit
            waitForPageLoad();
            Thread.sleep(2000); // Примусова пауза у 2 секунди

            // Вибір мови
            WebElement languageButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/main/div/button")));
            languageButton.click();

            WebElement languageOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(languageXPath)));
            languageOption.click();

            // Заповнюємо форму з очікуванням активності полів
            WebElement cardNumberInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("card-number-input")));
            WebElement expDateInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("expiry-date-input")));
            WebElement cvvInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("cvc-input")));
            WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("email-input")));

            // Клік на поле введення номеру картки
            cardNumberInput.click();

            cardNumberInput.sendKeys(cardNumber);
            expDateInput.sendKeys(expMonthYear);
            cvvInput.sendKeys(cvv);
            emailInput.sendKeys("viacheslav.yarosh@upc.ua");

            // Повторне натискання на кнопку "Pay"
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.action-button.go-payment-form__action-button")));
            continueButton.click();

            // Очікування завантаження сторінки після натискання "Pay"
            waitForPageLoad();
            Thread.sleep(2000); // для тестового середовища встановити 35000

            // Очікуємо, що з'явиться "Transaction Status" зі значенням "Successful"
            WebElement transactionStatusElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//li[@class='go-tran-info__row'][div[text()='Transaction Status']]/div[@class='go-tran-info__value go-tran-info__value_success' and text()='Successful']"
                            + "| //div[text()='Transaktionsstatus']/following-sibling::div[@class='go-tran-info__value go-tran-info__value_success' and text()='Erfolgreich']"
                            + "| //div[text()='Статус транзакції']/following-sibling::div[@class='go-tran-info__value go-tran-info__value_success' and text()='Успішна']")
            ));
            Assert.assertTrue(transactionStatusElement.isDisplayed(), "Transaction Status не 'Successful', 'Erfolgreich' або 'Успішна'");
            System.out.println("Transaction Status: 'Successful'.");

            // Спробуємо знайти кнопку "Send receipt"
            WebElement sendReceiptButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//button[@class='action-button go-result-view__send-email-button' and (span[text()='Send receipt']"
                            + " or span[text()='Quittung senden'] or span[text()='Надіслати квитанцію'])]")
            ));
            sendReceiptButton.click();

            // Очікування завантаження сторінки після натискання "Send receipt"
            waitForPageLoad();
            WebElement receiptSentElement = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//div[@class='go-result-view__receipt-sent' and (span[text()='Receipt sent successfully']"
                            + " or span[text()='Квитанція успішно надіслана'] or span[text()='Quittung erfolgreich gesendet'])]")
            ));
            Assert.assertTrue(receiptSentElement.isDisplayed(), "Лист не відправлено, кнопка 'Send receipt' залишилася активною. Тест не пройдено.");
            System.out.println("Тест пройдено успішно.");

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Assert.fail("Тест не пройдено через виняток: " + e.getMessage());
        }
    }

    private String[] getDatabaseValues() {
        String[] values = new String[3];
        try (Connection connection = JDBCConnection.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT RRN, APPROVAL_CODE, ORDER_ID FROM TRAN WHERE TRAN_ID = (SELECT MAX(TRAN_ID) FROM TRAN)"
             )) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                values[0] = resultSet.getString("ORDER_ID"); // ORDER_ID
                values[1] = resultSet.getString("APPROVAL_CODE"); // APPROVAL_CODE
                values[2] = resultSet.getString("RRN"); // RRN

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
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete")
        );
    }
}
