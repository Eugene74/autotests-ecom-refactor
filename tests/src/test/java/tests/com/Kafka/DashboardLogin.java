package tests.com.Kafka;

import com.ecom.core.config.PropertiesManager;
import methods.WebDriverHolder;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.time.Duration;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

public class DashboardLogin {
    private WebDriver driver;
    private WebDriverWait wait;
    private Properties properties;

    @BeforeClass
    public void setUp() {
        properties = PropertiesManager.getInstance().getEnvProperties();

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

        driver = new ChromeDriver(options);
        WebDriverHolder.setDriver(driver);
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5),Duration.ofSeconds(1));
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
    public void forgotPassword() throws InterruptedException {
        String baseUrl = properties.getProperty("URLtomcat");
        if (baseUrl == null) {
            Assert.fail("base.urlmt not found in properties");
        }
        String loginUrl = baseUrl + "/dashboard/login";
        driver.get(loginUrl);

        WebElement languageButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.v-btn--icon.v-btn--variant-text")));
        languageButton.click();

        WebElement englishOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-list-item-title' and text()='English']")));
        englishOption.click();
        Thread.sleep(2000);

        WebElement forgotPasswordLink = wait.until(ExpectedConditions.elementToBeClickable(By.linkText("Forgot your password?")));
        forgotPasswordLink.click();
        Thread.sleep(2000);

        WebElement usernameContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='app']/div/div/main/div[2]/div/form/div/div[1]/div")));
        WebElement usernameField = usernameContainer.findElement(By.xpath(".//input[@type='text']"));
        Actions actions = new Actions(driver);
        actions.moveToElement(usernameField).click().perform();
        usernameField.sendKeys("yarosh");

        WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and contains(@class, 'v-btn')]")));
        okButton.click();
    }
}
