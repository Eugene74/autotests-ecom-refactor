package com.ecom.ui.paylink.redirect.pages;

import com.ecom.ui.util.Waiters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import com.ecom.ui.common.BasePage;

public class LoginAdminPage extends BasePage {
    public LoginAdminPage(WebDriver driver) { super(driver); }

    @CacheLookup
    @FindBy(name = "j_username")
    protected WebElement username;

    @CacheLookup
    @FindBy(name = "j_password")
    protected WebElement pass;

    @CacheLookup
    @FindBy(className = "button")
    protected WebElement btn;

    @CacheLookup
    @FindBy(xpath = "/html/body/div/div/div/form/div/div[3]/input")
    protected WebElement btnRBA;


    @CacheLookup
    @FindBy(xpath = "/html/body/div[3]/div[2]/form/ul/li[3]/div[1]/button")
    protected WebElement btnRBAL;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[3]/div[2]/form/table/tbody/tr[3]/td[2]/button")
    protected WebElement btnRBBG;

    @CacheLookup
    @FindBy(xpath = "/html/body/div/div/div/form/div/div[3]/input")
    protected WebElement btnRBBH;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[3]/div[2]/form/table/tbody/tr[3]/td[2]/button")
    protected WebElement btnRBKO;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[3]/div[2]/form/table/tbody/tr[3]/td[2]/button")
    protected WebElement btnRBRS;

    public void loginIn(String url) {
        getDriver().get(url);
        username.sendKeys(login);
        pass.sendKeys(password);
        btn.click();
        Waiters.sleep(500);
    }

    public void loginInRBA(String url) {
        getDriver().get(url);
        username.sendKeys(login);
        pass.sendKeys(password);
        btnRBA.click();
        Waiters.sleep(500);
    }

    public void loginInRBAL(String url) {
        getDriver().get(url);
        username.sendKeys(login);
        pass.sendKeys(password);
        btnRBAL.click();
        Waiters.sleep(500);
    }

    public void loginInRBBG(String url) {
        getDriver().get(url);
        username.sendKeys(login);
        pass.sendKeys(password);
        btnRBBG.click();
        Waiters.sleep(500);
    }

    public void loginInKBC(String url) {
        getDriver().get(url);
        username.sendKeys(login);
        pass.sendKeys(password);
        btnRBBG.click();
        Waiters.sleep(500);
    }


    public void loginInRBBH(String url) {
        getDriver().get(url);
        username.sendKeys(login);
        pass.sendKeys(password);
        btnRBBH.click();
        Waiters.sleep(500);
    }

    public void loginInRBKO(String url) {
        getDriver().get(url);
        username.sendKeys(login);
        pass.sendKeys(password);
        btnRBKO.click();
        Waiters.sleep(500);
    }

    public void loginInRBRS(String url) {
        getDriver().get(url);
        username.sendKeys(login);
        pass.sendKeys(password);
        btnRBRS.click();
        Waiters.sleep(500);
    }
}
