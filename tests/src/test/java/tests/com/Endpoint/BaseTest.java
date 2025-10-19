package tests.com.Endpoint;

import com.ecom.core.config.PropertiesManager;
import java.io.File;
import java.util.HashMap;
import java.util.Properties;

import com.ecom.core.driver.WebDriverHolder;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.BeforeClass;



public class BaseTest extends BasePropertiesTest {
    protected WebDriver driver;
    protected Properties properties;
    protected String[] endpointArray;
    protected String baseUrl;
    protected String username;
    protected String password;
    protected String merchantId;  // Додамо поле для збереження merchantId
    static String downloadFilepath = System.getProperty("user.dir") + File.separator + "target";


    @BeforeClass
    public void setup() {
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

        ChromeOptions capability = new ChromeOptions();
        capability.setAcceptInsecureCerts(true);
        capability.addArguments("--lang=en");
        capability.addArguments("--no-sandbox");
        capability.addArguments("--disable-dev-shm-usage");
        capability.addArguments("--ignore-certificate-errors");
        capability.addArguments("--allow-insecure-localhost");
        capability.addArguments("--disable-web-security");

        if (Boolean.parseBoolean(properties.getProperty("is_headless", "false"))){
            capability.addArguments("--headless");
        }
        HashMap<String, Object> chromePrefs = new HashMap<>();
        chromePrefs.put("download.default_directory", downloadFilepath);
        capability.setExperimentalOption("prefs", chromePrefs);
        // Initialize the WebDriver instance
        driver = new ChromeDriver(capability);
        WebDriverHolder.setDriver(driver);


        // Maximize the window
        if (Boolean.parseBoolean(properties.getProperty("is_headless", "false"))) {
            driver.manage().window().setSize(new Dimension(3840, 2160));
        } else {
            driver.manage().window().maximize();
        }


        // Get the base URL
        baseUrl = properties.getProperty("URLtomee");

        // Get endpoints
        String endpoints = properties.getProperty("endpoints");
        if (endpoints != null) {
            endpointArray = endpoints.split(",");
        } else {
            endpointArray = new String[]{};
        }

        // Отримуємо ім'я користувача і пароль
        username = properties.getProperty("login");
        password = properties.getProperty("password");

        // Отримуємо merchantId
        merchantId = properties.getProperty("idAVAL1."+
                System.getProperty("env", properties.getProperty("default_env"))
        );
    }

    public void testEndpoints() {

        // Реалізація методу

    }

}
