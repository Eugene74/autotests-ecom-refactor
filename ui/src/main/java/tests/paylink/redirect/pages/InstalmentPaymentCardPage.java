package tests.paylink.redirect.pages;

import methods.Waiters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.paylink.redirect.BasePage;

import java.time.Duration;

public class InstalmentPaymentCardPage extends BasePage {

    public InstalmentPaymentCardPage(WebDriver driver) {
        super(driver);
    }

    @CacheLookup
    @FindBy(xpath = "/html/body/div/div/main/section/div/form/div/div[1]/div[2]/div/div/input")
    private WebElement cardNumber;

    @CacheLookup
    @FindBy(id = "email-input")
    private WebElement email;

    @CacheLookup
    @FindBy(id = "card_month_input")
    private WebElement expMonth_input;

    @CacheLookup
    @FindBy(id = "card_year_input")
    private WebElement expYear_input;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"app\"]/div/main/section/div/form/button")
    private WebElement cardDataSubmit;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"app\"]/div/main/section/div/form/div/div[3]/label/div")
    private WebElement installment_checkbox;

    @CacheLookup
    @FindBy(xpath = "/html/body/div/div/main/section/div/div[1]/div[1]/div[1]")
    private WebElement order;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"cvc-input\"]")
    private WebElement cvc;

    public String enterCardInfo(String[] card) {
        String orderID = setCardData(card);
        Waiters.appearElement(getDriver(), email);
        email.sendKeys("viacheslav.varosh@upc.ua");
        installment_checkbox.click();
        cardDataSubmit.click();
        return orderID;
    }

    private String setCardData(String[] card) {
        Waiters.sleep(2000);
        getDriver().findElement(By.xpath("/html/body/div/div/main/section/div/form/div/div[1]/div[2]/div/div/input"));
        String orderID = order.getText();
        System.out.println("Order ID captured: " + orderID); // Додаткове логування для перевірки значення orderID
        Waiters.sleep(1500);
        cardNumber.sendKeys(card[0]);
        Waiters.sleep(1500);
        // Введіть дату закінчення терміну дії у форматі MM/YY
        String expiryDate = card[2] + "/" + card[1].substring(2); // card[2] - місяць, card[1] - рік, беремо останні 2 цифри року
        WebElement expiryInput = getDriver().findElement(By.id("expiry-date-input"));
        expiryInput.sendKeys(expiryDate);
        enterCVC(card);
        return orderID;
    }

    public void enterCVC(String[] card) {
        if (cvc != null) {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(2)); // 2 секунди
            wait.until(ExpectedConditions.elementToBeClickable(cvc));
            cvc.click();
            cvc.sendKeys("111");
        }
    }
}
