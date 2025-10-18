package tests.mt.p2p.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import tests.paylink.redirect.BasePage;


public class ContentPage extends BasePage {

    public ContentPage(WebDriver driver) {
        super(driver);
    }

    @CacheLookup
    //@FindBy(xpath = "//*[@id=\"successPageButtonNextPayment\"]")
    @FindBy(xpath = "//*[@id=\"button-continue\"]")
    private WebElement content_success;

    public String checkSuccessResponse() {
        return content_success.getText();
    }
}
