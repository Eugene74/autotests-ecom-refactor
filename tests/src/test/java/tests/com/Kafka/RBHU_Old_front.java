package tests.com.Kafka;

import com.ecom.core.config.PropertiesManager;
import com.ecom.db.JDBCMethods;
import com.ecom.ui.driver.WebDriverHolder;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;
import com.ecom.api.type.Attributes;
import java.time.Duration;


import com.ecom.utils.ResourceUtils;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class RBHU_Old_front {
    private WebDriver driver;
    private WebDriverWait wait;
    private Properties properties;
    private Properties cardProperties;
    private static Integer id_AVAL;

    @BeforeClass
    public void setUp() {
        // Завантажуємо властивості з endpoint.properties
        PropertiesManager manager = PropertiesManager.getInstance();
        properties = manager.getEnvProperties();
        id_AVAL = Integer.valueOf(properties.getProperty("idAVAL1." +
                System.getProperty("env", properties.getProperty("default_env"))
        ));

        // Встановлюємо значення атрибута перед тестом
        JDBCMethods.setMerchantAtt(id_AVAL, Attributes.USE_NEW_FRONT_END, "false");
        JDBCMethods.setMerchantAtt(id_AVAL, Attributes.DISABLE_EMAIL_FIELD, "false");
        JDBCMethods.setMerchantAtt(id_AVAL, Attributes.USE_CARDHOLDER_NAME, "false");
        JDBCMethods.setMerchantAtt(id_AVAL, Attributes.ALLOW_PAYMENT_WITHOUT_3DS, "true");

        System.out.println("USE_NEW_FRONT_END: false вимкнено");
        System.out.println("DISABLE_EMAIL_FIELD: false включено");
        System.out.println("USE_CARDHOLDER_NAME: false включено");

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


        // Завантажуємо властивості з cards.properties
        cardProperties = manager.getCardProperties();
    }

    @AfterClass
    public void tearDown() {
        // Встановлюємо значення атрибута після тесту
        JDBCMethods.setMerchantAtt(id_AVAL, Attributes.USE_NEW_FRONT_END, "true");
        JDBCMethods.setMerchantAtt(id_AVAL, Attributes.DISABLE_EMAIL_FIELD, "true");
        System.out.println("USE_NEW_FRONT_END: true включено");
        System.out.println("DISABLE_EMAIL_FIELD: true вимкнено");

        if (driver != null) {
            driver.quit();
        }
    }

    @DataProvider(name = "locales")
    public Object[][] createLocales() {
        return new Object[][] {
                {"en"}, {"de"}, {"hu"}
        };
    }

    @Test(dataProvider = "locales")
    public void testTransaction(String locale) {
        try {
            // Отримуємо значення з файлу властивостей
            String baseUrl = properties.getProperty("URLtomee");
            String merchantId = properties.getProperty("MerchantID_AVAL1");
            String terminalId = properties.getProperty("TerminalID_AVAL1");

            // Отримуємо шлях до HTML шаблону
            String htmlContent = ResourceUtils.readAsString("Html/transaction_form.html");

            // Замінюємо змінні у HTML контенті, включаючи locale
            htmlContent = htmlContent.replace("${base.url}", baseUrl)
                    .replace("${MerchantID}", merchantId)
                    .replace("${TerminalID}", terminalId)
                    .replace("value=\"en\"", "value=\"" + locale + "\"");

// Створюємо тимчасовий файл у тимчасовій директорії операційної системи
            Path tempFilePath = Files.createTempFile("transaction_form_fixed", ".html");

            try (BufferedWriter writer = Files.newBufferedWriter(tempFilePath, StandardCharsets.UTF_8)) {
                writer.write(htmlContent);
            }

// Відкриваємо сторінку з цим HTML файлом
            String finalUrl = "file:///" + tempFilePath.toAbsolutePath().toString().replace("\\", "/");
            driver.get(finalUrl);

            //System.out.println("Тимчасовий файл створений: " + tempFilePath.toString());

            // Логуємо поточну мову тесту
            System.out.println("Тест для locale: " + locale);

            // Пауза для завантаження сторінки
            Thread.sleep(2000);

            // Завантажуємо значення з cardProperties
            String cardMC = cardProperties.getProperty("cardVISA");
            String[] cardDetails = cardMC.split(";");

            String cardNumber = cardDetails[0];
            String expYear = cardDetails[1].substring(2); // беремо останні дві цифри року
            String expMonth = cardDetails[2];
            String cvv = cardDetails[3];

            // Перше натискання на кнопку submit
            WebElement submitButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("submit")));
          //  System.out.println("Елемент знайдено! Натискаємо кнопку 'Submit'.");
            submitButton.click();

            // Очікування завантаження після натискання submit
            waitForPageLoad();
            Thread.sleep(3000); // Примусова пауза у 5 секунд

            // Заповнюємо форму з очікуванням активності полів
            WebElement cardNumberInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("CardNumberInput")));
            WebElement monthInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("card_month_input")));
            WebElement yearInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("card_year_input")));
            WebElement cvvInput = wait.until(ExpectedConditions.elementToBeClickable(By.id("card_cvv_input")));
            WebElement emailInput = wait.until(ExpectedConditions.elementToBeClickable(By.name("Email")));

            // Клік на поле введення номеру картки
            cardNumberInput.click();

            cardNumberInput.sendKeys(cardNumber);
            monthInput.sendKeys(expMonth);
            yearInput.sendKeys(expYear);
            cvvInput.sendKeys(cvv);
            emailInput.sendKeys("viacheslav.yarosh@upc.ua");

            // Повторне натискання на кнопку "Pay"
            WebElement continueButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("btContinue")));
            //System.out.println("Елемент знайдено! Натискаємо кнопку 'Pay'.");
            continueButton.click();

            // Очікування завантаження сторінки після натискання "Pay"
            waitForPageLoad();

            // Очікуємо, що з'явиться "Transaktionscode", "Transaction code", або "Válaszkód" зі значенням "000"
            WebElement transaktionscodeElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//div[div[span[text()='Transaktionscode'] or span[text()='Transaction code'] or span[text()='Válaszkód']]]/div[normalize-space(text())='000']")
            ));
            String transaktionscode = transaktionscodeElement.getText();
            if (!"000".equals(transaktionscode)) {
                System.out.println("Transaction code знайдено зі значенням: " + transaktionscode);
                Assert.fail("Тест не пройдено - Transaktionscode не дорівнює '000'");
            }

            System.out.println("Transaction code знайдено зі значенням '000'.");

            // Спробуємо знайти кнопку "Quittung senden", "Send receipt", або "Bizonylat küldés"
            WebElement quittungSendenButton;
            try {
                quittungSendenButton = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("//div[@id='emailsend-btn--send' and (span[text()='Quittung senden'] or span[text()='Send receipt'] or span[text()='Bizonylat küldés'])]")
                ));
               // System.out.println("Елемент знайдено! Натискаємо кнопку 'Quittung senden', 'Send receipt', або 'Bizonylat küldés'.");
                quittungSendenButton.click();
            } catch (Exception e) {
                System.out.println("Елемент не знайдено! Кнопку 'Quittung senden', 'Send receipt', або 'Bizonylat küldés' не натиснуто.");
                Assert.fail("Елемент не знайдено! Кнопку 'Quittung senden', 'Send receipt', або 'Bizonylat küldés' не натиснуто.");
                return;
            }

            // Очікування завантаження сторінки після натискання "Quittung senden"
            waitForPageLoad();

            // Перевіряємо, чи з'явилося повідомлення успіху
            WebElement successMessage;
            try {
                successMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//div[@id='emailsend-success' and contains(@class, 'show') and contains(text(), '✓')]")
                ));
                System.out.println("Лист відправлено.");
            } catch (Exception e) {
                System.out.println("Лист не відправлено.");
                Assert.fail("Лист не відправлено.");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            Assert.fail("Тест не пройдено через виняток: " + e.getMessage());
        }
    }

    private void waitForPageLoad() {
        new WebDriverWait(driver, Duration.ofSeconds(30)).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState")
                        .equals("complete"));
    }
}
