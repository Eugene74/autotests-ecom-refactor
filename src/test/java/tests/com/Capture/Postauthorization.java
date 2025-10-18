package tests.com.Capture;

import jdbc.JDBCConnection;
import methods.WebDriverHolder;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class Postauthorization {
    private WebDriver driver;
    private WebDriverWait wait;
    private Properties properties;

    @BeforeClass
    public void setUp() {
        // Завантажуємо властивості з endpoint.properties
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream("properties/env.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
      //  driver = new ChromeDriver(options);
       // driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      //  wait = new WebDriverWait(driver, Duration.ofSeconds(20)); // Змінено час очікування
       // driver.manage().window().maximize();



        // Ініціюємо WebDriver
        driver = new ChromeDriver(options);
        WebDriverHolder.setDriver(driver);

// Зміна часу очікування з використанням Duration
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

// Ініціюємо WebDriverWait з використанням Duration
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));

// Максимізація вікна
        if (Boolean.parseBoolean(properties.getProperty("is_headless", "false"))) {
            driver.manage().window().setSize(new Dimension(3840, 2160));
        } else {
            driver.manage().window().maximize();
        }






    }

    @AfterClass
    public void tearDown() {
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

            // Вивід значень, отриманих з бази даних
           // System.out.println("Значення, отримані з бази даних:");
            System.out.println("OrderID: " + orderId);
            System.out.println("ApprovalCode: " + approvalCode);
            System.out.println("RRN: " + rrn);

            // Отримуємо значення з файлу властивостей
            String baseUrl = properties.getProperty("URLtomee");
            String merchantId = properties.getProperty("MerchantID_AVAL1");
            String terminalId = properties.getProperty("TerminalID_AVAL1");

            // Отримуємо шлях до HTML шаблону
            Path templatePath = Paths.get("src/main/resources/Html/postauthorization_form.html");
            String htmlContent = new String(Files.readAllBytes(templatePath));

            // Замінюємо змінні у HTML контенті
            htmlContent = htmlContent.replace("${base.url}", baseUrl)
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
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
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
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT RRN, APPROVAL_CODE, ORDER_ID FROM TRAN WHERE TRAN_ID = (SELECT MAX(TRAN_ID) FROM TRAN)"
             )) {
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

   // private void waitForPageLoad() {
     //   new WebDriverWait(driver, Duration.ofSeconds(30)).until(
           //     webDriver -> ((JavascriptExecutor) webDriver)
               //         .executeScript("return document.readyState")
               //         .equals("complete")




        private void waitForPageLoad() {
            new WebDriverWait(driver, Duration.ofSeconds(30)).until(
                    webDriver -> ((JavascriptExecutor) webDriver)
                            .executeScript("return document.readyState")
                            .equals("complete")
        );
    }
}
