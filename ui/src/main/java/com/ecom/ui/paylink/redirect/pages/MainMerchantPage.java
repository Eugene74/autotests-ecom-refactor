package com.ecom.ui.paylink.redirect.pages;


import com.ecom.ui.util.Waiters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import com.ecom.ui.common.BasePage;

import static com.ecom.core.config.EnvData.LOGIN;
import static com.ecom.core.config.EnvData.PASSWORD;
import static java.lang.Thread.sleep;

public class MainMerchantPage extends BasePage {

    public MainMerchantPage(WebDriver driver) {
        super(driver);
    }

    @CacheLookup
    @FindBy(name = "j_username")
    protected WebElement username;

    @CacheLookup
    @FindBy(name = "j_password")
    protected WebElement pass;

    @CacheLookup
    @FindBy(css = "body > table:nth-child(3) > tbody > tr > td:nth-child(1) > table > tbody > tr:nth-child(7) > td > a")
    private WebElement invoiceMerchantMenu;

    @CacheLookup
    @FindBy(css = "body > table:nth-child(3) > tbody > tr > td:nth-child(1) > table > tbody > tr:nth-child(5) > td > a")
    private WebElement invoiceMerchantMenuRBI;


    @CacheLookup
    @FindBy(css = "body > aside > nav > ul:nth-child(1) > li:nth-child(5) > a")
    private WebElement invoiceMerchantMenuRBA;

    @CacheLookup
    @FindBy(css = "body > table:nth-child(3) > tbody > tr > td:nth-child(1) > table > tbody > tr:nth-child(5) > td")
    private WebElement invoiceMerchantMenuRBAL;

    @CacheLookup
    @FindBy(css = "body > table:nth-child(3) > tbody > tr > td:nth-child(1) > table > tbody > tr:nth-child(6) > td > a")
    private WebElement invoiceMerchantMenuRBBG;

    @CacheLookup
    @FindBy(css = "body > aside > nav > ul:nth-child(1) > li:nth-child(5) > a")
    private WebElement invoiceMerchantMenuRBBH;

    @CacheLookup
    @FindBy(css = "body > table:nth-child(3) > tbody > tr > td:nth-child(1) > table > tbody > tr:nth-child(5) > td > a")
    private WebElement invoiceMerchantMenuRBKO;

    @CacheLookup
    @FindBy(css = "#menu > table > tbody > tr:nth-child(5) > td > a")
    private WebElement invoiceMerchantMenuRBRS;

    @CacheLookup
    @FindBy(xpath = "//a[contains(@href,'/go/merchant/do?action=trans')]")
    private WebElement tranMerchantMenu;

    @CacheLookup
    @FindBy(css = "body > aside > nav > ul:nth-child(1) > li:nth-child(4) > a")
    private WebElement tranMerchantMenuRBA;

    @CacheLookup
    @FindBy(css = "body > table:nth-child(3) > tbody > tr > td:nth-child(1) > table > tbody > tr:nth-child(4) > td > a")
    private WebElement tranMerchantMenuRBKO;

    @CacheLookup
    @FindBy(css = "#menu > table > tbody > tr:nth-child(4) > td > a")
    private WebElement tranMerchantMenuRBRS;

    @CacheLookup
    @FindBy(css = ".btn.btn-primary.btn-block")
    private WebElement btnBlock;

    @CacheLookup
    @FindBy(className = "button")
    protected WebElement btn;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[3]/div[2]/form/ul/li[3]/div[1]/button")
    private WebElement btnSubmit;


    @CacheLookup
    @FindBy(xpath = "/html/body/div[3]/div[2]/form/table/tbody/tr[3]/td[2]/button")
    private WebElement btnSubmitRBKO;

    @CacheLookup
    @FindBy(xpath = "/html/body/div/div/div/form/div/div[3]/input")
    private WebElement btnSubmitRBBH;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"header\"]/table/tbody/tr[2]/td[3]/font/img")
    private WebElement lang;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"header\"]/table/tbody/tr[2]/td[3]/a[1]/img")
    private WebElement langRBI;


    @CacheLookup
    @FindBy(css = "body > div.content > div.languages > ul > li:nth-child(1) > a")
    private WebElement langRBAL;


    @CacheLookup
    @FindBy(css = "#header > div.header-info > div > ul > li:nth-child(1) > a")
    private WebElement langRBRS;

    @CacheLookup
    @FindBy(className = "locale-en")
    private WebElement english;

    @CacheLookup
    @FindBy(id = "lang")
    private WebElement langRBA;

    private final String path = "//*[contains(text(),'%s')]";

    public void selectMenu(String menu) {
        By by = By.xpath(String.format(path, menu));
        getDriver().findElement(by).click();
    }

    public void selectInvoicing() {
        selectMenu("Invoicing_08");
    }

    public void getInvoicingMenu() {
        invoiceMerchantMenu.click();
    }

    public void getInvoicingMenuRBI() {
        Waiters.sleep(1000);
        invoiceMerchantMenuRBI.click();
    }

    public void getInvoicingMenuRBA() {
        Waiters.sleep(1000);
        invoiceMerchantMenuRBA.click();
    }

    public void getInvoicingMenuRBAL() {
        Waiters.sleep(1000);
        invoiceMerchantMenuRBAL.click();
    }

    public void getInvoicingMenuRBBG() {
        Waiters.sleep(1000);
        invoiceMerchantMenuRBBG.click();
    }

    public void getInvoicingMenuRBBH() {
        Waiters.sleep(1000);
        invoiceMerchantMenuRBBH.click();
    }

    public void getInvoicingMenuRBKO() {
        Waiters.sleep(1000);
        invoiceMerchantMenuRBKO.click();
    }

    public void getInvoicingMenuRBRS() {
        Waiters.clickableElement(getDriver(), invoiceMerchantMenuRBRS);
        invoiceMerchantMenuRBRS.click();
    }

    public void getTransactionsMenu() { tranMerchantMenu.click(); }

    public void getTransactionsMenuRBA() { tranMerchantMenuRBA.click(); }

    public void getTransactionsMenuRBKO() { tranMerchantMenuRBKO.click(); }

    public void getTransactionsMenuRBRS() { tranMerchantMenuRBRS.click(); }

    public void getTransactionsMenuRBI() {
        Waiters.sleep(1000);
        tranMerchantMenu.click();
    }

    public void loginInGo(String url) {
        getDriver().get(url);
        username.sendKeys(LOGIN);
        pass.sendKeys(PASSWORD);
        btn.click();
    }

    public void loginInRBI(String url) throws InterruptedException {
        getDriver().get(url);
        langRBI.click();
        sleep(1000);
        username.sendKeys(LOGIN);
        pass.sendKeys(PASSWORD);
        btn.click();
        Waiters.sleep(500);
    }

    public void loginInRBA(String url) throws InterruptedException {
        getDriver().get(url);
        Select language = new Select(langRBA);
        language.selectByVisibleText("English");
        Waiters.appearElement(getDriver(), username);
        username.sendKeys(LOGIN);
        pass.sendKeys(PASSWORD);
        btnBlock.click();
        Waiters.sleep(500);
    }

    public void loginInRBAL(String url) throws InterruptedException {
        getDriver().get(url);
        langRBAL.click();
        english.click();
        sleep(1000);
        username.sendKeys(LOGIN);
        pass.sendKeys(PASSWORD);
        btnSubmit.click();
        Waiters.sleep(500);
    }

    public void loginInRBKO(String urlMerch) throws InterruptedException {
        getDriver().get(urlMerch);
        langRBAL.click();
        english.click();
        sleep(1000);
        username.sendKeys(LOGIN);
        pass.sendKeys(PASSWORD);
        btnSubmitRBKO.click();
        Waiters.sleep(500);
    }

    public void loginInRBBG(String urlMerch) throws InterruptedException {
        getDriver().get(urlMerch);
        langRBAL.click();
//        english.click();
        sleep(1000);
        username.sendKeys(LOGIN);
        pass.sendKeys(PASSWORD);
        btnSubmitRBKO.click();
        Waiters.sleep(500);
    }

    public void loginInRBBH(String urlMerch) throws InterruptedException {
        getDriver().get(urlMerch);
      //  langRBAL.click();
//        english.click();
        sleep(1000);
        username.sendKeys(LOGIN);
        pass.sendKeys(PASSWORD);
        btnSubmitRBBH.click();
        Waiters.sleep(500);
    }


    public void loginInRBRS(String urlMerch) throws InterruptedException {
        getDriver().get(urlMerch);

        langRBAL.click();
        english.click();
        sleep(1000);
        username.sendKeys(LOGIN);
        pass.sendKeys(PASSWORD);
        btnSubmitRBKO.click();
        Waiters.sleep(500);
    }
}
