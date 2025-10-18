package tests.com.Cross;

import com.ecom.core.config.PropertiesManager;
import com.ecom.db.JDBCConnection;
import com.ecom.core.driver.WebDriverHolder;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CrossBorder {
    private WebDriver driver;
    private WebDriverWait wait;
    private Properties properties;

    @BeforeClass
    public void setUp() {
        // Завантаження властивостей зі конфігураційних файлів
        PropertiesManager manager = PropertiesManager.getInstance();
        properties = manager.getEnvProperties();
        properties.putAll(manager.getCardProperties());

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

        // Налаштування опцій ChromeDriver
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(properties.getProperty("is_headless", "false"))){
            options.addArguments("--headless");
            options.addArguments("--window-size=3840,2160"); // Maximum 4K resolution
        }
        options.addArguments("--allow-file-access-from-files");
        options.addArguments("--disable-web-security");
        options.addArguments("--allow-file-access");
        options.addArguments("--ignore-ssl-errors=yes");
        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--allow-insecure-localhost");


        // Ініціалізація WebDriver і налаштування основних параметрів
      //  driver = new ChromeDriver(options);
       // driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      //  wait = new WebDriverWait(driver, Duration.ofSeconds(20));
       // driver.manage().window().maximize();

        // Ініціалізація WebDriver і налаштування основних параметрів
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
        // Закриття браузера після завершення тестів
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void validateCrossBorder() throws Exception {
        String senderName = "Sender name Yarik";
        String recipientName = "Recipient name Vuk";

        // Отримання властивостей base.urlmt і merchantId
        String baseUrl = properties.getProperty("URLtomcat");
        if (baseUrl == null) {
            Assert.fail("base.urlmt not found in properties");
        }

        String merchantIdDec = properties.getProperty("idAVAL5." +
                System.getProperty("env", properties.getProperty("default_env")
                )
        );
        if (merchantIdDec == null) {
            Assert.fail("merchantId not found in properties");
        }

        // Перетворення десяткового числа в HEX
        String merchantIdHex = Integer.toHexString(Integer.parseInt(merchantIdDec));

        // Формування URL
        String crossBorderUrl = baseUrl + "/mt/p2p/init/" + merchantIdHex;
        System.out.println("URL for CrossBorder test: " + crossBorderUrl);

        // Відкриття сформованого URL
        driver.get(crossBorderUrl);

        // Очікування завантаження сторінки
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("sender-card-number-input")));
           System.out.println("Сторінка успішно завантажена.");

            // Вводимо дані з card.properties
            // Дані картки відправника
            String[] senderCardData = properties.getProperty("cardMCmta").split(";");
            WebElement senderCardNumberInput = driver.findElement(By.id("sender-card-number-input"));
            senderCardNumberInput.sendKeys(senderCardData[0]);

            WebElement senderCardExpiryDateInput = driver.findElement(By.id("sender-card-expiry-date-input"));
            String expiryDate = senderCardData[2] + "/" + senderCardData[1].substring(2); // Формат MM/YY
            senderCardExpiryDateInput.sendKeys(expiryDate);

            WebElement senderCardCVCInput = driver.findElement(By.id("sender-card-cvc-input"));
            Random random = new Random();
            int randomCVC = 100 + random.nextInt(900); // Генерація 3-значного числа
            senderCardCVCInput.sendKeys(String.valueOf(randomCVC));

            WebElement sendercardholdernameinput = driver.findElement(By.id("sender-card-holder-name-input"));
            sendercardholdernameinput.sendKeys(senderName);

            WebElement recipientcardholdernameinput = driver.findElement(By.id("recipient-card-holder-name-input"));
            recipientcardholdernameinput.sendKeys(recipientName);

            // Дані картки отримувача
            WebElement recipientCardNumberInput = driver.findElement(By.id("recipient-card-number-input"));
            recipientCardNumberInput.sendKeys(properties.getProperty("cardVISArec").split(";")[0]);

            // Натискаємо кнопку "Continue"
            WebElement continueButton = driver.findElement(By.id("button-continue"));
            continueButton.click();

            // Очікуємо, поки поле amount-input стане активним
            WebElement amountInput = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.id("amount-input"))
            );
           // System.out.println("Поле 'amount-input' знайдено.");

            // Вводимо значення в поле amount-input
            amountInput.sendKeys("555");
            Thread.sleep(3000); // Якщо необхідна пауза

            // Знаходимо та обираємо чекбокс
            WebElement checkbox = driver.findElement(By.cssSelector(".checkbox__chevron"));
            checkbox.click();

            // Натискаємо кнопку "Continue" знову
            WebElement finalContinueButton = driver.findElement(By.xpath("//button[@type='button' and contains(@class, 'app-button_primary')]/span[contains(text(), 'Continue')]"));
            finalContinueButton.click();

            // Перевірка наявності введених даних на наступній сторінці
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='summary-form__fake-input-value' and text()='" + senderName + "']")));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='summary-form__fake-input-value' and text()='" + recipientName + "']")));

            // Натискаємо кнопку "Transfer"
            WebElement transferButton = driver.findElement(By.xpath("//button[@type='button' and contains(@class, 'app-button_primary')]/span[contains(text(), 'Transfer')]"));
            transferButton.click();

            // Чекаємо 8 секунд
            Thread.sleep(8000);

            // Перевірка наявності елементів, що сигналізують про успішний трансфер
            WebElement nextTransferButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='button-continue' and contains(@class, 'app-button_primary')]")));
            WebElement successfulTransferMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//p[@class='status-page__description' and text()='Successful transfer']")));

            if (nextTransferButton.isDisplayed() && successfulTransferMessage.isDisplayed()) {
                System.out.println("Переказ виконано успішно.");

                // Отримання даних з бази даних
                getMtRequestData();
            } else {
                Assert.fail("Платіж не успішно.");
            }

        } catch (Exception e) {
            Assert.fail("Сторінку не вдалося завантажити або знайти необхідні елементи: " + e.getMessage());
        }
    }

    private void getMtRequestData() throws Exception {
        try (Connection connection = jdbc.JDBCConnection.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT MT_REQUESTS_ID, MERCHANT_CODE, STATUS_CODE, STATUS, TRACKING_ID " +
                             "FROM (SELECT * FROM MT_REQUESTS ORDER BY CREATED DESC) WHERE ROWNUM = 1")) {

            String trackingId = null;

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int mtRequestId = resultSet.getInt("MT_REQUESTS_ID");
                    String merchantCode = resultSet.getString("MERCHANT_CODE");
                    String statusCode = resultSet.getString("STATUS_CODE");
                    String status = resultSet.getString("STATUS");
                    trackingId = resultSet.getString("TRACKING_ID");

                    System.out.println("MT_REQUESTS_ID: " + mtRequestId);
                    System.out.println("MERCHANT_CODE: " + merchantCode);
                    System.out.println("STATUS_CODE: " + statusCode);
                    System.out.println("STATUS: " + status);
                    System.out.println("TRACKING_ID: " + trackingId);

                    // Перевірка значень
                    Assert.assertEquals(merchantCode, "1000148", "MERCHANT_CODE не відповідає очікуваному значенню");
                    Assert.assertEquals(statusCode, "000", "STATUS_CODE не відповідає очікуваному значенню");
                    Assert.assertEquals(status, "OK", "STATUS не відповідає очікуваному значенню");
                }
            }

            if (trackingId != null) {
                try (PreparedStatement tranStatement = connection.prepareStatement(
                        "SELECT OP_TYPE, PURCHASE_DESC FROM TRAN WHERE ORDER_ID = ?")) {
                    tranStatement.setString(1, trackingId);

                    try (ResultSet tranResultSet = tranStatement.executeQuery()) {
                        if (tranResultSet.next()) {
                            String opType = tranResultSet.getString("OP_TYPE");
                            String purchaseDesc = tranResultSet.getString("PURCHASE_DESC");

                            System.out.println("OP_TYPE: " + opType);
                            System.out.println("PURCHASE_DESC: " + purchaseDesc);

                            // Перевірка значень
                            Assert.assertEquals(opType, "11", "OP_TYPE не відповідає очікуваному значенню");
                            Assert.assertEquals(purchaseDesc, "P2P Cross-Border Transfer", "PURCHASE_DESC не відповідає очікуваному значенню");
                        }
                    }
                }
            } else {
                Assert.fail("Не вдалося знайти TRACKING_ID.");
            }
        } catch (Exception e) {
            // Обробка винятку SQL
            e.printStackTrace();
            Assert.fail("Не вдалося виконати запит до бази даних: " + e.getMessage());
        }
    }
}
