package com.ecom.ui.paylink.redirect.pages;


import com.ecom.ui.util.Waiters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import com.ecom.ui.common.BasePage;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class InvoicingMerchant extends BasePage {
    public InvoicingMerchant(WebDriver driver) {
        super(driver);
    }
    @CacheLookup
    @FindBy(id = "mid")
    private WebElement mid;

    @CacheLookup
    @FindBy(name = "merchantId")
    private WebElement merchantId;

    @CacheLookup
    @FindBy(id = "generate")
    protected WebElement genOrder;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"content\"]/div/fieldset/table/tbody/tr[1]/td[2]")
    protected WebElement orderId;

    @CacheLookup
    @FindBy(id = "amount")
    protected WebElement amount;

    @CacheLookup
    @FindBy(name="expDate")
    protected WebElement expDate;

    @CacheLookup
    @FindBy(id = "description")
    protected WebElement description;

    @CacheLookup
    @FindBy(xpath = "//input[@type=\"submit\"][@class=\"button\"]")
    protected WebElement createBtn;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"showForm\"]")//*[@id="showForm"]
    protected WebElement show;

    @CacheLookup
    @FindBy(name = "orderId")
    protected WebElement searchOrder;

    @CacheLookup
      @FindBy(xpath = "/html/body/table[2]/tbody/tr/td/div/form[2]/fieldset/table/tbody/tr[6]/td/input")
    protected WebElement searchBtn;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"content\"]/table[1]/tbody/tr[2]/td[1]/a")
    protected WebElement openInvoice;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"content\"]/div/fieldset/table/tbody/tr[7]/td[2]")
    protected WebElement urlInvoice;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[3]/div[3]/table/tbody/tr[2]/td[1]/a")
    protected WebElement tran;

    @CacheLookup
    @FindBy(xpath = "//a[contains(@href,'/go/images/copy.png')]")
    private WebElement linkInvoice;

    protected  String orderRed = "";

    public String createInvoice(int id){
        Select merchant = new Select(mid);
        merchant.selectByValue(String.valueOf(id));
        genOrder.click();
        amount.click();
        amount.sendKeys("777");
        description.sendKeys("invoicing pay");
        expDate.click();
        Select drpExpDate=new Select(expDate);
        drpExpDate.selectByValue("3");
        createBtn.click();
        return orderId.getText();
    }

    public String searchInvoice(String order){
        Waiters.appearElement(getDriver(),show);
        Waiters.sleep(500);
        show.click();
        Waiters.sleep(500);
        searchOrder.sendKeys(order);
        Waiters.sleep(500);
        searchBtn.click();
        Waiters.sleep(500);
        new WebDriverWait(getDriver(), Duration.ofSeconds(30))
                .until(ExpectedConditions.elementToBeClickable(openInvoice));
        openInvoice.click();
        return orderRed;
    }

    public String searchInvoiceRevers(int id, String order){
        Select merchant = new Select(merchantId);
        merchant.selectByValue(String.valueOf(id));
        searchOrder.sendKeys(order);
        searchBtn.click();

        openInvoice.click();
        return orderRed;
    }

      public String goPay(){
        String urlForPay = urlInvoice.getText();
        System.out.println(urlForPay);
        return urlForPay;
    }

    public void openTransaction(){
        tran.click();
    }
}
