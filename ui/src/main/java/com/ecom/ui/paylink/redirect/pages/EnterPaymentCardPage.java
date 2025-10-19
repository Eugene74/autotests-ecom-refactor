package com.ecom.ui.paylink.redirect.pages;

import com.ecom.ui.util.Waiters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.ecom.ui.common.BasePage;

import java.time.Duration;

public class EnterPaymentCardPage extends BasePage {

    public EnterPaymentCardPage(WebDriver driver) {
        super(driver);
    }

    @CacheLookup
    @FindBy(id = "paymentTypeCard")
    private WebElement paymentType;

   // @CacheLookup
   // @FindBy(xpath = "//*[@id=\"CardNumber\"]")
  //  private WebElement cardNumber;
    @CacheLookup
    @FindBy(id = "input-card-number")
    private WebElement cardNumber;

    @CacheLookup
    @FindBy(name = "CardNumberInput")
    private WebElement cardNumberKBC;

    @CacheLookup
    @FindBy(id = "card_number_input")
    private WebElement card_number_input;

    @CacheLookup
    @FindBy(name = "CardNumberInput")
    private WebElement cardNumberInput;

    //@CacheLookup
   // @FindBy(id = "Email")
   // private WebElement email;
    @CacheLookup
    @FindBy(id = "input-email")
    private WebElement email;

    @CacheLookup
    @FindBy(id = "ExpMonth")
    private WebElement expMonth;

    @CacheLookup
    @FindBy(id = "ExpYear")
    private WebElement expYear;

    @CacheLookup
    @FindBy(id = "expiration-date")
    private WebElement expDate;

    @CacheLookup
    @FindBy(id = "card_month_input")
    private WebElement expMonth_input;


    @CacheLookup
    @FindBy(id = "card_year_input")
    private WebElement expYear_input;


    //@CacheLookup
   // @FindBy(id = "cardDataSubmit")
    //private WebElement cardDataSubmit;
    @CacheLookup
    @FindBy(xpath = "//*[@id=\"button-pay\"]")
    private WebElement cardDataSubmit;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"bSubmitEn\"]")
    private WebElement cardDataSubmitKBC;

    @CacheLookup
    @FindBy(className = "btn-submit")
    private WebElement btnSubmit;

    @CacheLookup
    @FindBy(id = "btContinue")
    private WebElement btContinue;

    @CacheLookup
    @FindBy(xpath = "/html/body/table/tbody/tr[1]/td/table/tbody/tr[2]/td/div/table/tbody/tr[1]/td/b")
    private WebElement tranCode;

    @CacheLookup
    @FindBy(xpath = "/html/body/table/tbody/tr[1]/td/table/tbody/tr[2]/td/div/table/tbody/tr[2]/td")
    private WebElement approvalCode;

    //@CacheLookup
   // @FindBy(xpath = "//*[@id=\"cardForm\"]/table/tbody/tr[4]/td[2]")
   // private WebElement order;
    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div/main/span")
    private WebElement order;


    @CacheLookup
    @FindBy(xpath = "//*[@id=\"cardForm\"]/table/tbody/tr[4]/td[2]")
    private WebElement orderAval;

   // @CacheLookup
    //@FindBy(xpath = "//*[@id=\"Cvc\"]")
   // private WebElement cvc;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"input-cvc\"]")
    private WebElement cvc;

    @CacheLookup
    @FindBy(id = "CVCSubmit")
    private WebElement cvcSubmit;

    @CacheLookup
    @FindBy(className = "languages")
    private WebElement languages;

    @CacheLookup
    @FindBy(name = "cardForm")
    private WebElement cardForm;

     public String enterCardInfo(String[] card) {
        String orderID = setCardData(card);
        Waiters.appearElement(getDriver(), email);
        email.sendKeys("viacheslav.varosh@upc.ua");
        cardDataSubmit.click();
        return orderID;
    }

    public String enterCardInfoInvoicing(String[] card)  {
        String orderID = setCardDataInvoicing(card);
        Waiters.appearElement(getDriver(), email);
        email.sendKeys("viacheslav.varosh@upc.ua");
        cardDataSubmit.click();
        return orderID;
    }

    private String setCardDataInvoicing(String[] card) {
        String orderID;
        Waiters.sleep(1500);
        orderID = order.getText();
        System.out.println(orderID);
        cardNumber.sendKeys(card[0]);
        Waiters.sleep(1500);
        // Введіть дату закінчення терміну дії у форматі MM/YY
        String expiryDate = card[2] + "/" + card[1].substring(2); // card[2] - місяць, card[1] - рік, беремо останні 2 цифри року
        WebElement expiryInput = getDriver().findElement(By.id("input-expiry"));
        expiryInput.sendKeys(expiryDate);
        enterCVC(card);
        return orderID;
    }

    private String setCardData(String[] card) {
        Waiters.sleep(2000);
        getDriver().findElement(By.xpath("//*[@id=\"input-card-number\"]"));
        String orderID = order.getText();
        Waiters.sleep(1500);
        cardNumber.sendKeys(card[0]);
        Waiters.sleep(1500);
        // Введіть дату закінчення терміну дії у форматі MM/YY
        String expiryDate = card[2] + "/" + card[1].substring(2); // card[2] - місяць, card[1] - рік, беремо останні 2 цифри року
        WebElement expiryInput = getDriver().findElement(By.id("input-expiry"));
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
        }}
}