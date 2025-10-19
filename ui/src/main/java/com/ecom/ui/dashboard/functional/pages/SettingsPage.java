package com.ecom.ui.dashboard.functional.pages;

import com.ecom.ui.util.Waiters;
import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.ecom.ui.common.BasePage;

import java.time.Duration;
import java.util.List;

public class SettingsPage extends BasePage {

    public SettingsPage(WebDriver driver) { super(driver); }

    @CacheLookup
    //@FindBy(xpath = "/html/body/div[1]/div/div/div[1]/aside/nav/div/a[9]")
    @FindBy(xpath = "/html/body/div[1]/div/div/nav/div/div[2]/div[5]/div[1]")
    protected WebElement settings_tab;

    @CacheLookup
    //@FindBy(xpath = "/html/body/div[1]/div/div/div[1]/aside/nav/div/div[9]/a[2]")
    @FindBy(xpath = "/html/body/div[1]/div/div/nav/div/div[2]/div[5]/div[2]/a[3]")
    protected WebElement stoplist_tab;

    @CacheLookup
    //@FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div[2]/div/table/tbody")
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/div/div[1]/table/tbody/tr/td")
    protected WebElement stopList_tbody;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div/div[2]/div/div[3]/button[2]")
    protected WebElement confirmDeleting_btn;

    @CacheLookup
    @FindBy(xpath = "(//*[name()='svg'][@role='presentation'])[1]")
    protected WebElement deleteBtnSvg;

    @CacheLookup
    //@FindBy(css = ".mdi-menu.mdi.v-icon.notranslate.v-theme--myCustomLightTheme.v-icon--size-default")
    @FindBy(css = "button.v-btn.v-app-bar-nav-icon .mdi-menu.mdi.v-icon.notranslate.v-theme--default-upc.v-icon--size-default")
    protected WebElement icon_menu;

   // @CacheLookup
   // @FindBy(xpath = "/html/body/div[1]/div/div/main/div/div/div[1]/table/thead")
   // protected WebElement resultTable_stoplist;

    @CacheLookup
    @FindBy(xpath = "//div[@class='v-table__wrapper']/table/thead")
    protected WebElement resultTable_stoplist;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/div/div[1]/table/tbody/tr/td[6]/i")
    protected WebElement delete_bt_stoplist;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div/div[2]/div/div[3]/button[2]/span[3]")
    protected WebElement confirmDeleteButton;

   // @CacheLookup
   // @FindBy(css = ".v-btn--variant-flat .v-btn__content:contains('OK')")
    //protected WebElement confirmDeleting_btn;


    public boolean deleteFromStopList(String card) {
        try {
            Waiters.appearElement(getDriver(), icon_menu);
            icon_menu.click();
            Waiters.appearElement(getDriver(), settings_tab);
            settings_tab.click();
            Waiters.appearElement(getDriver(), stoplist_tab);
            stoplist_tab.click();
            Waiters.appearElement(getDriver(), delete_bt_stoplist);
            delete_bt_stoplist.click();
            Waiters.sleep(1000);
            Waiters.appearElement(getDriver(), confirmDeleting_btn);
            confirmDeleting_btn.click();
            Waiters.sleep(1000);

            return deleteFromTableByIndex(stopList_tbody, 0);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFromTableByIndex(WebElement tableBody, int index) {
        try {
            List<WebElement> rows = tableBody.findElements(By.tagName("tr"));
            System.out.println("Number of rows found: " + rows.size());
            if (rows.size() > index) {
                WebElement row = rows.get(index);
                // Виконати видалення або інші дії з рядком
                // Наприклад, знайти і натиснути кнопку видалення в цьому рядку
                WebElement deleteButton = row.findElement(By.xpath(".//i[@class='mdi-delete mdi v-icon notranslate v-theme--myCustomLightTheme v-icon--size-default text-error v-icon--clickable']"));
                deleteButton.click();
                // Підтвердження видалення
                WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10)); // Використання старого конструктора
                wait.until(ExpectedConditions.visibilityOf(confirmDeleting_btn));
                confirmDeleting_btn.click();
                return true;
            } else {
                System.out.println("Index out of bounds: " + index);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}