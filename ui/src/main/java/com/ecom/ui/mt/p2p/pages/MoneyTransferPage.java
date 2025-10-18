package com.ecom.ui.mt.p2p.pages;


import com.ecom.ui.util.Waiters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import com.ecom.ui.common.BasePage;

import static com.ecom.ui.util.Waiters.handleAlert;


public class MoneyTransferPage extends BasePage {
    public MoneyTransferPage(WebDriver driver) {
        super(driver);
    }
        @Override
        public String toString() {
            return "000-tatra-slovak";
        }

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"input-sender-card-number\"]")
    private WebElement payerCardInput;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"input-recipient-card-number\"]")
    private WebElement recipientCardNumber;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"input-sender-card-expiry-date\"]")
    private WebElement payerCardExpInput;

    @CacheLookup
    @FindBy(id = "card_exp_year")
    private WebElement payerCardExpYearInput;


    @CacheLookup
    @FindBy(xpath = "//*[@id=\"input-sender-card-cvc\"]")
    private WebElement payerCardCVVInput;
    @CacheLookup
    @FindBy(xpath = "//*[@id=\"process\"]/main/div[3]/div[3]/div/label")
    private WebElement agreementCheckbox;

    @CacheLookup
    @FindBy(id = "btn_submit")
    private WebElement sendMoneyButton;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"input-sender-card-holder-name\"]")
    private WebElement payerNameInput;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"input-recipient-card-holder-name\"]")
    private WebElement recipientName;

    @CacheLookup
    @FindBy(xpath = "//html/body/div[2]/div/footer/div/button/span")
    private WebElement goNext_btn;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"input-amount\"]")
    private WebElement amount_input;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"app\"]/div/main/div[2]/div/div[2]/section/div[2]/label/span")
    private WebElement agree_checkbox;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div/footer/div/button[2]/span")
    private WebElement amountFormButtonNext;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div/footer/div/button[2]/span")
    private WebElement summaryFormButtonSubmit;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"successPageButtonNextPayment\"]")
    private WebElement successPageButtonNextPayment;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"input-sender-date-of-birth\"]")
    private WebElement dateOfBirth;


    public void enterSenderCardNum(String[] card, String name) {

        Waiters.appearElement(getDriver(), payerCardInput);
        payerCardInput.click();
        payerCardInput.sendKeys(card[0]);
        Waiters.appearElement(getDriver(), payerCardExpInput);
        payerCardExpInput.sendKeys(card[4]);
        Waiters.appearElement(getDriver(), payerCardCVVInput);
        payerCardCVVInput.sendKeys(card[3]);

        Waiters.appearElement(getDriver(), dateOfBirth);
        dateOfBirth.click();
        dateOfBirth.sendKeys(String.valueOf(24101982));

        Waiters.appearElement(getDriver(), payerNameInput);
        payerNameInput.click();
        payerNameInput.sendKeys(name);
    }

    public void enterRecipientCardNum(String card, String name) {
        recipientCardNumber.sendKeys(card);
        recipientName.sendKeys(name);
        Waiters.appearElement(getDriver(), goNext_btn);
        goNext_btn.click();
    }

    public void enterAmount(String amount) {
        Waiters.appearElement(getDriver(), amount_input);
        amount_input.click();
        Waiters.sleep(2000);
        amount_input.sendKeys(amount);
        Waiters.sleep(2000);
        agree_checkbox.click();
        Waiters.sleep(2000);
        Waiters.clickableElement(getDriver(), amountFormButtonNext);
        amountFormButtonNext.click();
    }

    public boolean validaty(){
        Waiters.appearElement(getDriver(), successPageButtonNextPayment);
        if(successPageButtonNextPayment.isEnabled()){
            return true;
        }else{
            return false;
        }
    }

    public void confirmTransfer() {
        summaryFormButtonSubmit.click();
        handleAlert(getDriver());
    }
}