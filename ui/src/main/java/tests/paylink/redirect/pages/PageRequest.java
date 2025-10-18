package tests.paylink.redirect.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import tests.paylink.redirect.BasePage;

public class PageRequest extends BasePage {

    @CacheLookup
    @FindBy(name="submit")
    protected WebElement submit;

    public PageRequest(WebDriver driver) { super(driver); }
    public void sendReq()  { submit.click(); }
}

