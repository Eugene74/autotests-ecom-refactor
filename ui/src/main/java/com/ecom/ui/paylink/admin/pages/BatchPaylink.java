package com.ecom.ui.paylink.admin.pages;

import com.ecom.ui.util.Waiters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import com.ecom.ui.common.BasePage;

import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.By.xpath;

public class BatchPaylink extends BasePage {
    public BatchPaylink(WebDriver driver) {
        super(driver);
    }

    private static final int LENTH = 6;
    private static final String BATCH_NAME = "Batch id ";

    @CacheLookup
    @FindBy(name = "j_username")
    private WebElement username;


    @CacheLookup
    @FindBy(name = "j_password")
    private WebElement pass;

    @CacheLookup
    @FindBy(className = "button")
    private WebElement button;
    @CacheLookup
    @FindBy(xpath = "//*[@id=\"content\"]/form/table[2]/tbody/tr/td/input")
    private WebElement buttonGen;
    @CacheLookup
    @FindBy(xpath = "//a[text()='Расчеты']")
    private WebElement settl;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"menu\"]/table/tbody/tr[2]/td/a")
    private WebElement settlMenu;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"content\"]/table/tbody/tr[2]/td[2]")
    private WebElement message;

    @CacheLookup
    @FindBy(xpath = "//a[text()='Пачки']")
    private WebElement batch;

    @CacheLookup
    @FindBy(name = "sendFile")
    private WebElement sendFile;

    public void login(String iD) {
        username.sendKeys(login);
        pass.sendKeys(password);
        button.click();
    }

    public void closed(String iD) {
        getDriver().manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        Waiters.appearElement(getDriver(), settl);
//      Waiters.sleep(1000);
        settl.click();
        Waiters.appearElement(getDriver(), settlMenu);
        Waiters.sleep(1000);
        settlMenu.click();
        Waiters.waitPageLoaded(getDriver());
        Waiters.sleep(1000);
        Waiters.appearElement(getDriver(), xpath("//input[@name='merchantId'][@value=" + iD + "]"));
        getDriver().findElement(xpath("//input[@name='merchantId'][@value=" + iD + "]")).click();
        Waiters.appearElement(getDriver(), buttonGen);
        Waiters.sleep(1000);
        buttonGen.click();
        String mess = message.getText();
        Waiters.sleep(1000);
        int start = mess.lastIndexOf(BATCH_NAME) + BATCH_NAME.length();
        Waiters.sleep(1000);
        String batchId = mess.substring(start, start + LENTH);
        Waiters.appearElement(getDriver(), batch);
        Waiters.sleep(1000);
        batch.click();
        Waiters.waitPageLoaded(getDriver());
        Waiters.sleep(1000);
        getDriver().findElement(xpath("//input[@name='batchId'][@value=" + batchId + "]")).click();
        Waiters.appearElement(getDriver(), sendFile);
        Waiters.sleep(1000);
        sendFile.click();
    }
}
