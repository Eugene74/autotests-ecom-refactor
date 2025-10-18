package tests.paylink.redirect;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;
import tests.BaseRedirect;

import java.util.concurrent.TimeUnit;

public class BasePage extends BaseRedirect {

    protected static WebDriver driver;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
        getDriver().manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    public WebDriver getDriver() {
        return driver;
    }
}