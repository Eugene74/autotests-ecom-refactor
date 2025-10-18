package tests.com.Kafka;

import methods.WebDriverHolder;
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
import java.time.Duration;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class KafdropEmail {
    private WebDriver driver;
    private WebDriverWait wait;
    private Properties properties;

    @BeforeClass
    public void setUp() throws IOException {
        // Завантаження властивостей з конфігураційного файлу
        properties = new Properties();
        File file = new File("properties/env.properties");
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            properties.load(fileInputStream);
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

        // Налаштування опцій ChromeDriver
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
    public void emailUPC() {
        // Отримання URL з властивостей
        String baseUrl = String.format(
                properties.getProperty("base.urlkafka"),
                System.getProperty("env", properties.getProperty("default_env"))
        );
        if (baseUrl == null) {
            Assert.fail("base.urlkafka not found in properties");
        }

        String pipelineUrl = properties.getProperty("base.urlpipelines");
        if (pipelineUrl == null) {
            Assert.fail("base.urlpipelines not found in properties");
        }

        // Витягання Pipeline ID з URL
        String[] parts = pipelineUrl.split("/");
        String pipelineId = parts[parts.length - 1];

        // Відкриття базового URL
        driver.get(baseUrl);

        try {
            // Пошук і заповнення фільтра
            WebElement filterInput = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("filter"))
            );
            filterInput.click();
            filterInput.sendKeys(pipelineId);
        } catch (Exception e) {
            Assert.fail("Не знайдено поле фільтрації");
        }

        try {
            // Пошук рядка з темою, що містить pipelineId
            WebElement dataRow = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//tr[@class='dataRow']/td/a[contains(@href, '" + pipelineId + "')]"))
            );
            String topicName = dataRow.getText();

            if (topicName.equals("emailUPC-" + pipelineId)) {
                dataRow.click();

                try {
                    // Пошук і натискання кнопки View Messages
                    WebElement viewMessagesButton = wait.until(
                            ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[@id='topic-messages' and contains(@href, 'emailUPC-" + pipelineId + "')]"))
                    );
                    String viewMessagesHref = viewMessagesButton.getAttribute("href");
                    if (viewMessagesHref.contains("emailUPC-" + pipelineId)) {
                        viewMessagesButton.click();
                        System.out.println("topic: emailUPC-" + pipelineId);
                    } else {
                        Assert.fail("emailUPC-" + pipelineId + " не знайдено або не співпадає в View Messages");
                    }
                } catch (Exception e) {
                    Assert.fail("emailUPC-" + pipelineId + " не знайдено або не співпадає в View Messages");
                }

                try {
                    // Натискання кнопки View MessagesBtn
                    WebElement viewMessagesBtn = wait.until(
                            ExpectedConditions.elementToBeClickable(By.id("viewMessagesBtn"))
                    );
                    viewMessagesBtn.click();
                    Thread.sleep(5000);
                } catch (Exception e) {
                    Assert.fail("Не вдалося натиснути кнопку View Messages");
                }

                // Натискання на всі кнопки розгортання повідомлень
                List<WebElement> toggleLinks = driver.findElements(By.cssSelector("a.toggle-msg"));
                for (WebElement toggleLink : toggleLinks) {
                    try {
                        toggleLink.click();
                    } catch (Exception e) {
                        System.out.println("Не вдалося натиснути toggle link: " + e.getMessage());
                    }
                }

                Thread.sleep(5000);

                try {
                    // Пошук повідомлень
                    List<WebElement> messageDetails = wait.until(
                            ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector("div.message-detail"))
                    );

                    int foundCount = 0;
                    LocalDate today = LocalDate.now();
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    for (WebElement messageDetail : messageDetails) {
                        try {
                            String messageText = messageDetail.getText();

                            if (messageText.contains("viacheslav.yarosh@upc.ua") || messageText.contains("Viacheslav.Yarosh@upc.ua")) {
                                // Витягання та перевірка дати повідомлення
                                String timestampText = messageText.split("Timestamp:")[1].split("Headers:")[0].trim();
                                String timestampDateString = timestampText.split(" ")[0];
                                LocalDate timestampDate = LocalDate.parse(timestampDateString, dateFormatter);

                                if (timestampDate.equals(today)) {
                                    System.out.println("To: " + (messageText.contains("viacheslav.yarosh@upc.ua") ? "viacheslav.yarosh@upc.ua" : "Viacheslav.Yarosh@upc.ua") + " з датою: " + timestampText);
                                    foundCount++;
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Помилка при перевірці повідомлення: " + e.getMessage());
                            System.out.println("HTML вміст елемента: " + messageDetail.getAttribute("outerHTML"));
                        }
                    }

                    // Вивід результатів
                    System.out.println("Загальна кількість адресатів 'To: viacheslav.yarosh@upc.ua' з поточною датою: " + foundCount);

                    if (foundCount == 0) {
                        Assert.fail("Вказаного адресата з поточною датою не знайдено");
                    } else {
                        System.out.println("Тест пройдено, адресата з поточною датою знайдено.");
                    }

                } catch (Exception e) {
                    Assert.fail("Не вдалося знайти або перевірити повідомлення");
                }

            } else {
                Assert.fail("emailUPC-" + pipelineId + " не знайдено або не співпадає");
            }
        } catch (Exception e) {
            Assert.fail("emailUPC-" + pipelineId + " не знайдено");
        }
    }}