package com.ecom.ui.mt.crossboard.pages;

import com.ecom.ui.util.Waiters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import com.ecom.ui.common.BasePage;

public class PayPage extends BasePage {
    public PayPage(WebDriver driver) { super(driver); }

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"recipientCardNumber\"]")
    private WebElement recipientCard_input;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"recipientName\"]")
    private WebElement recipientName_input;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"cardDataFormButtonContinue\"]")
    private WebElement nextStepRec_btn;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"senderCardNumber\"]")
    private WebElement senderCardNumber_input;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"senderCardValidity\"]")
    private WebElement senderCardExpDate_input;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"senderCardCvc\"]")
    private WebElement senderCardCVV_input;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"senderName\"]")
    private WebElement senderName_input;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"cardDataFormButtonContinue\"]")
    private WebElement goNext_btn;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"amountFormAmountInput\"]")
    private WebElement amount_input;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"amountFormTermOfUseCheckbox\"]")
    private WebElement agree_checkbox;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"amountFormButtonNext\"]")
    private WebElement amountFormButtonNext;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"summaryFormButtonSubmit\"]")
    private WebElement summaryFormButtonSubmit;

    ////////////////////////////
    @CacheLookup
    @FindBy(name = "senderCity")
    private WebElement senderCity_input;

    @CacheLookup
    @FindBy(name = "senderStreet")
    private WebElement senderStreet_input;

    @CacheLookup
    @FindBy(name = "senderHouse")
    private WebElement senderHouse_input;

    @CacheLookup
    @FindBy(name = "senderFlat")
    private WebElement senderFlat_input;

    @CacheLookup
    @FindBy(name = "phoneNumber")
    private WebElement senderPhoneNumber_input;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"cross-border-form\"]/fieldset[2]/div/div[2]/div/div[2]/button")
    private WebElement nextStepSend_btn;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"successPageButtonNextPayment\"]")
    private WebElement successPageButtonNextPayment;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"cross-border-form\"]/fieldset[3]/div/div[2]/div/div[2]/button")
    private WebElement send_btn;


    @CacheLookup
    @FindBy(id = "transferID")
    private WebElement transferID;

    @CacheLookup
    @FindBy(id = "authorizationCode")
    private WebElement authorizationCode;

    public void enterRecipientCardData(String card, String name) {
        Waiters.appearElement(getDriver(), recipientCard_input);
        recipientCard_input.click();
        recipientCard_input.sendKeys(card);
        Waiters.appearElement(getDriver(), recipientName_input);
        recipientName_input.click();
        recipientName_input.sendKeys(name);
        Waiters.appearElement(getDriver(), nextStepRec_btn);
        nextStepRec_btn.click();
    }

    public void enterSenderCardData(String[] card, String name) {
        Waiters.appearElement(getDriver(), senderCardNumber_input);
        senderCardNumber_input.click();
        senderCardNumber_input.sendKeys(card[0]);
        Waiters.appearElement(getDriver(), senderCardExpDate_input);
        senderCardExpDate_input.sendKeys(card[4]);
        Waiters.appearElement(getDriver(), senderCardCVV_input);
        senderCardCVV_input.sendKeys(card[3]);
        Waiters.appearElement(getDriver(), senderName_input);
        senderName_input.click();
        senderName_input.sendKeys(name);
        Waiters.appearElement(getDriver(), goNext_btn);
        goNext_btn.click();
    }

    public void enterTransferAmount(String amount) {
        Waiters.appearElement(getDriver(), amount_input);
        amount_input.click();
        amount_input.sendKeys(amount);
        Waiters.sleep(1000);
        agree_checkbox.click();
        Waiters.sleep(1000);
        Waiters.clickableElement(getDriver(), amountFormButtonNext);
        amountFormButtonNext.click();
    }

    public void summaryPay(){
        summaryFormButtonSubmit.click();
    }

    public boolean validaty(){
        Waiters.appearElement(getDriver(), successPageButtonNextPayment);
        if(successPageButtonNextPayment.isEnabled()){
            return true;
        }else{
            return false;
        }
    }
}