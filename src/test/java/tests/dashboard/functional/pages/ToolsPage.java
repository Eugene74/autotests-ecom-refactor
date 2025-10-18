package tests.dashboard.functional.pages;

import methods.Waiters;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import tests.paylink.redirect.BasePage;
import tests.paylink.redirect.pages.EnterPaymentCardPage;
import tests.paylink.redirect.pages.GoPaymentCardPage;


import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.List;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;



public class ToolsPage extends BasePage {

    @CacheLookup
    @FindBy(xpath = "//div[@class='v-list-item-title' and (text()='Tools' or text()='Інструменти')]")
    protected WebElement tools_tab;

    @CacheLookup
    @FindBy(xpath = "//div[@class='v-list-item-title' and (text()='Invoicing')]")
    protected WebElement invoicing_tab;

    @CacheLookup
    @FindBy(xpath = "//div[@class='v-list-item-title' and (text()='Multipay invoices')]")
    protected WebElement multipayinvoicing_tab;

    @CacheLookup
    //@FindBy(xpath = "//*[@id=\"__BVID__150\"]/form/div[1]/div[1]/div/div/div/label/input")
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/form/div/div/div[1]/div[1]/div/div[1]/div/div[3]/div/input")
    protected WebElement merchant_input;



    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div/div[2]/div[2]/form/div[1]/div[1]/div/div/div/ul")
    protected WebElement merchants_list;

    @CacheLookup
    //@FindBy(xpath = "//button[@type='button' and .//span[text()='Створити' or text()='Add']]")
    @FindBy(xpath = "//button[@type='button' and .//span[text()='Створити' or text()='Add' or text()='Create']]")
    protected WebElement options_btn;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div[2]/div[2]/form/div/div[3]/div/div[1]/div[1]/div/div[1]/div/div[3]/div/input")
    protected WebElement merchantModal_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div[1]/div[2]/form/div/div[3]/div/div[1]/div/div/div[1]/div/div[3]/div/input")
    protected WebElement merchantModalmultipayinvoicing_input;


    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div[2]/div[2]/form/div/div[3]/div/div[1]/div[2]/div/div[1]/div/div[3]/div/input")
    protected WebElement terminal_AVAL_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div[1]/div[2]/form/div/div[3]/div/div[2]/div[1]/div/div[1]/div/div[3]/div/input")
    protected WebElement terminal_AVAL_multipayinvoicing_input;


    @CacheLookup
    @FindBy(xpath = "//div[@role='combobox' and @aria-haspopup='menu']")
    protected WebElement merchantsModal_list;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div[2]/div[2]/form/div/div[3]/div/div[3]/div[1]/div/div[1]/div/div[3]/input")
    protected WebElement amountModal_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div[1]/div[2]/form/div/div[3]/div/div[3]/div[1]/div/div[1]/div/div[3]/input")
    protected WebElement amountModal_multipayinvoicing_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div[2]/div[2]/form/div/div[3]/div/div[3]/div[2]/div/div[1]/div/div[3]/div/input")
    protected WebElement currencyModal_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div[1]/div[2]/form/div/div[3]/div/div[2]/div[2]/div/div[1]/div/div[3]/div/input")
    protected WebElement currencyModal_multipayinvoicing_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[4]/div[1]/div/div[2]/div/div/form/div[3]/div[2]/div/div/div/div/ul")
    protected WebElement currencyModal_list;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div[2]/div[2]/form/div/div[3]/div/div[4]/div/div/div[1]/div/div[3]/textarea")
    protected WebElement descriptionModal_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div[1]/div[2]/form/div/div[3]/div/div[4]/div/div/div[1]/div/div[3]/textarea")
    protected WebElement descriptionModal_multipayinvoicing_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div[2]/div[2]/form/div/div[4]/button[2]/span[3]")
    protected WebElement saveModal_btn;

    @CacheLookup
    @FindBy(xpath = "//span[contains(normalize-space(.), 'Options')]/ancestor::button")
    protected WebElement options_tab_link;

    @CacheLookup
    @FindBy(xpath = "//label[contains(normalize-space(.), 'Send SMS')]")
    protected WebElement send_sms_checkbox;

    @CacheLookup
    @FindBy(xpath = "//input[@type='tel']")
    protected WebElement telephone_number_input;

    @CacheLookup
    @FindBy(xpath = "//span[contains(normalize-space(.), 'Send')]/ancestor::button")
    protected WebElement send_button;

    @CacheLookup
    //@FindBy(xpath = "//div[contains(normalize-space(.), 'Success') and @role='status']")
    @FindBy(xpath = "//button[@type='button' and .//span[text()='Close']]")
    protected WebElement success_message;


    @CacheLookup
    @FindBy(xpath = " (//label[contains(normalize-space(.), 'URL')])[1]/following-sibling::input")
    protected WebElement invoice_url_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div[1]/div[2]/form/div/div[4]/button[2]/span[3]")
    protected WebElement saveModal_multipayinvoicing_btn;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div[2]/div[2]/form/div/div[3]/div/div[2]/div/div[6]/div/div/div[1]/div/div[4]/button/span[3]/i")
    protected WebElement copyModal_link;


    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div/div[2]/form/div/div[3]/div/div[4]/div[1]/div[1]/div/div/div[1]/div/div[4]/button/span[3]/i")
    protected WebElement copyModal_multipayinvoicing_link;


    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div[2]/div[2]/form/div/div[3]/div/div[2]/div[1]/div/div[1]/div/div[3]/input")
    protected WebElement orderModal_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div[1]/div[2]/form/div/div[3]/div/div[3]/div[2]/div/div[1]/div/div[3]/input")
    protected WebElement orderModal_multipayinvoicing_input;

    @CacheLookup
    //@FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div/div[2]/div[2]/form/div[2]/div/button")
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/form/div/div/div[4]/div[4]/button[2]")
    protected WebElement searchBtn;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/form/div/div/div[3]/div[4]/button[2]")
    protected WebElement search_multipayinvoicing_Btn;


    @CacheLookup
    @FindBy(xpath = "//*[@id=\"app\"]/div[2]/div/div/div/div[2]/a")
    protected WebElement expandTrigger;

    @CacheLookup
    //@FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div/div[2]/div[2]/form/div[1]/div[2]/div/div/label/input")
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/form/div/div/div[2]/div[2]/div/div[1]/div/div[3]/input")
    protected WebElement orderID_input;

    @CacheLookup
    //@FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div/div[2]/div[2]/form/div[1]/div[5]/div/div/div/label/input")
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/form/div/div/div[1]/div[3]/div/div[1]/div/div[3]/div/input")
    protected WebElement status_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div/div[2]/div[2]/form/div[1]/div[5]/div/div/div/ul")
    protected WebElement status_ul;

    @CacheLookup
    //@FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div/div[3]/div[1]/div/table/tbody")
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/div/div[2]")
    protected WebElement invoices_table;

    @CacheLookup
   // @FindBy(css = ".mdi-menu.mdi.v-icon.notranslate.v-theme--myCustomLightTheme.v-icon--size-default")
    @FindBy(css = ".mdi-menu.mdi.v-icon.notranslate.v-theme--default-upc.v-icon--size-default")
    protected WebElement icon_menu;



    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/form/div/div/div[2]/div[1]/div/div[1]/div/div[3]/input")
    protected WebElement invoice_id;




    public ToolsPage(WebDriver driver) {
        super(driver);
    }

    public String createInvoice(String merchant, String amount, String orderID, String terminal_AVAL) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20)); // Збільшено час очікування

        try {
            // Знайдіть і клацніть на іконку меню
            Waiters.appearElement(getDriver(), icon_menu);
            icon_menu.click();

            // Клацніть на вкладку "Tools"
            Waiters.appearElement(getDriver(), tools_tab);
            tools_tab.click();

            // Клацніть на вкладку "Рахунки до оплати" або "Invoices"
            Waiters.appearElement(getDriver(), invoicing_tab);
            invoicing_tab.click();
            Waiters.sleep(1500);

            // Клацніть на кнопку "Створити" або "Add"
            Waiters.appearElement(getDriver(), options_btn);
            options_btn.click();

            // Введіть мерчанта
            Waiters.appearElement(getDriver(), merchantModal_input);
            merchantModal_input.click();
            Waiters.sleep(1000);
            merchantModal_input.sendKeys(merchant);
            Waiters.sleep(1000);
            Actions actions = new Actions(getDriver());
            actions.sendKeys(Keys.DOWN).perform();
            actions.sendKeys(Keys.ENTER).perform();

            // Зачекайте, поки з'явиться випадаюче меню і знайдіть відповідний елемент
//            WebElement merchantOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-list-item-title' and text()='" + merchant + "']")));
//            merchantOption.click();

            // Введіть значення в поле, щоб викликати випадаюче меню
            Waiters.appearElement(getDriver(), terminal_AVAL_input);
            terminal_AVAL_input.click();
            terminal_AVAL_input.sendKeys(terminal_AVAL);
            actions = new Actions(getDriver());
            actions.sendKeys(Keys.DOWN).perform();
            actions.sendKeys(Keys.ENTER).perform();

            // Введіть orderID
            Waiters.appearElement(getDriver(), orderModal_input);
            orderModal_input.click();
            orderModal_input.sendKeys(orderID);

            // Введіть amount
            Waiters.appearElement(getDriver(), amountModal_input);
            amountModal_input.click();
            amountModal_input.sendKeys(amount);

            // Виберіть валюту
            Waiters.appearElement(getDriver(), currencyModal_input);
            currencyModal_input.click();

            // Зачекайте, поки з'явиться випадаюче меню і знайдіть відповідний елемент
            WebElement currencyOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-list-item-title' and text()='UAH']")));
            currencyOption.click();

            // Введіть опис
            Waiters.appearElement(getDriver(), descriptionModal_input);
            descriptionModal_input.click();
            descriptionModal_input.sendKeys("Invoice from dashboard");

            // Збережіть інвойс
            Waiters.appearElement(getDriver(), saveModal_btn);
            saveModal_btn.click();

            // Скопіюйте посилання
            Waiters.sleep(2500);
            Waiters.appearElement(getDriver(), invoice_url_input);
            String url = invoice_url_input.getAttribute("value");
            try{
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            Path dest = Path.of("target/screenshots", "invoice_url.png");
            Files.createDirectories(dest.getParent());
            Files.copy(screenshot.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Screenshot saved: " + dest.toAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            throw  e;
        }
    }




        public String createMultipayInvoice(String merchant, String amount, String orderID, String terminal_AVAL) {
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20)); // Збільшено час очікування

            try {
                // Знайдіть і клацніть на іконку меню
                Waiters.appearElement(getDriver(), icon_menu);
                icon_menu.click();

                // Клацніть на вкладку "Tools"
                Waiters.appearElement(getDriver(), tools_tab);
                tools_tab.click();

                // Клацніть на вкладку "Рахунки до оплати" або "Invoices"
                Waiters.appearElement(getDriver(), multipayinvoicing_tab);
                multipayinvoicing_tab.click();
                Waiters.sleep(1500);

                // Клацніть на кнопку "Створити" або "Add"
                Waiters.appearElement(getDriver(), options_btn);
                options_btn.click();

                // Введіть мерчанта
                Waiters.appearElement(getDriver(), merchantModalmultipayinvoicing_input);
                merchantModalmultipayinvoicing_input.click();
                merchantModalmultipayinvoicing_input.sendKeys(merchant);
                Actions actions = new Actions(getDriver());
                actions.sendKeys(Keys.DOWN).perform();
                actions.sendKeys(Keys.ENTER).perform();

                // Зачекайте, поки з'явиться випадаюче меню і знайдіть відповідний елемент
//                WebElement merchantOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-list-item-title' and text()='" + merchant + "']")));
//                merchantOption.click();

                // Введіть значення в поле, щоб викликати випадаюче меню
                Waiters.appearElement(getDriver(), terminal_AVAL_multipayinvoicing_input);
                terminal_AVAL_multipayinvoicing_input.click();
                terminal_AVAL_multipayinvoicing_input.sendKeys(terminal_AVAL);
                actions.sendKeys(Keys.DOWN).perform();
                actions.sendKeys(Keys.ENTER).perform();

                // Введіть orderID
                Waiters.appearElement(getDriver(), orderModal_multipayinvoicing_input);
                orderModal_multipayinvoicing_input.click();
                orderModal_multipayinvoicing_input.sendKeys(orderID);

                // Введіть amount
                Waiters.appearElement(getDriver(), amountModal_multipayinvoicing_input);
                amountModal_multipayinvoicing_input.click();
                amountModal_multipayinvoicing_input.sendKeys(amount);

                // Виберіть валюту
                Waiters.appearElement(getDriver(), currencyModal_multipayinvoicing_input);
                currencyModal_multipayinvoicing_input.click();

                // Зачекайте, поки з'явиться випадаюче меню і знайдіть відповідний елемент
                WebElement currencyOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-list-item-title' and text()='UAH']")));
                currencyOption.click();

                // Введіть опис
                Waiters.appearElement(getDriver(), descriptionModal_multipayinvoicing_input);
                descriptionModal_multipayinvoicing_input.click();
                descriptionModal_multipayinvoicing_input.sendKeys("Multipay Invoice from dashboard");

                // Збережіть інвойс
                Waiters.appearElement(getDriver(), saveModal_multipayinvoicing_btn);
                saveModal_multipayinvoicing_btn.click();

                // Скопіюйте посилання
                Waiters.sleep(2500); //додано для корекного копіювання
                Waiters.appearElement(getDriver(), invoice_url_input);
                String url = invoice_url_input.getAttribute("value");
                return url;
            } catch (Exception e) {
                e.printStackTrace();
                throw  e;
            }
    }

    public void send_sms(String number){
        Waiters.clickableElement(getDriver(), options_tab_link);
        options_tab_link.click();
        Waiters.visibleElement(getDriver(), send_sms_checkbox);
        send_sms_checkbox.click();
        Waiters.appearElement(getDriver(), telephone_number_input);
        telephone_number_input.click();
        telephone_number_input.sendKeys(number);
        Waiters.appearElement(getDriver(), send_button);
        send_button.click();
        Waiters.appearElement(getDriver(), success_message);
    }


    public boolean findRecordInResultTable(String checkField, WebElement resultTable){
        List<WebElement> results = resultTable.findElements(By.tagName("tr"));
        boolean result = false;
        if(!results.isEmpty()){
            for(int i = 0; i<results.size(); ++i){
                if(results.get(i).getText().contains(checkField)){
                    Waiters.sleep(1500);
                    result = true;
                    break;
                }
            }
        }
        return result;
    }




    public boolean checkMultipayStatus(String merchant, String orderID, String status) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20)); // Збільшено час очікування

        try {
            // Знайдіть і клацніть на іконку меню
            Waiters.appearElement(getDriver(), icon_menu);
            icon_menu.click();

            // Клацніть на вкладку "Tools"
            Waiters.appearElement(getDriver(), tools_tab);
            tools_tab.click();

            // Клацніть на вкладку "Рахунки до оплати" або "Invoices"
            Waiters.appearElement(getDriver(), multipayinvoicing_tab);
            multipayinvoicing_tab.click();
            Waiters.sleep(2000);

            // Введіть мерчанта
            Waiters.appearElement(getDriver(), merchant_input);
            merchant_input.clear();
            merchant_input.click();
            merchant_input.sendKeys(merchant);
            Waiters.sleep(2000);

            // Зачекайте, поки з'явиться випадаюче меню і знайдіть відповідний елемент
            WebElement merchantOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-list-item-title' and text()='" + merchant + "']")));
            merchantOption.click();

            // Введіть orderID
            Waiters.appearElement(getDriver(), orderID_input);
            orderID_input.click();
            orderID_input.sendKeys(orderID);

                        // Натисніть кнопку пошуку
            Waiters.appearElement(getDriver(), search_multipayinvoicing_Btn);
            search_multipayinvoicing_Btn.click();
            Waiters.sleep(2000);

            return findRecordInResultTable(orderID, invoices_table);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean checkStatus(String merchant, String orderID, String status) {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20)); // Збільшено час очікування

        try {
            // Знайдіть і клацніть на іконку меню
            Waiters.appearElement(getDriver(), icon_menu);
            icon_menu.click();

            // Клацніть на вкладку "Tools"
            Waiters.appearElement(getDriver(), tools_tab);
            tools_tab.click();

            // Клацніть на вкладку "Рахунки до оплати" або "Invoices"
            Waiters.appearElement(getDriver(), invoicing_tab);
            invoicing_tab.click();
            Waiters.sleep(2000);

            // Введіть мерчанта
            Waiters.appearElement(getDriver(), merchant_input);
            merchant_input.clear();
            merchant_input.click();
            merchant_input.sendKeys(merchant);
            Waiters.sleep(2000);

            // Зачекайте, поки з'явиться випадаюче меню і знайдіть відповідний елемент
            WebElement merchantOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-list-item-title' and text()='" + merchant + "']")));
            merchantOption.click();

            // Введіть orderID
            Waiters.appearElement(getDriver(), orderID_input);
            orderID_input.click();
            orderID_input.sendKeys(orderID);

            // Введіть статус
            Waiters.appearElement(getDriver(), status_input);
            status_input.click();
            status_input.sendKeys(Keys.chord(Keys.CONTROL, "a")); // Виділити все
            Waiters.sleep(500); // Додайте невелику затримку
            status_input.sendKeys(Keys.DELETE); // Видалити виділене
            status_input.sendKeys(Keys.chord(Keys.CONTROL, "a")); // Виділити все
            Waiters.sleep(500); // Додайте невелику затримку
            status_input.sendKeys(Keys.DELETE); // Видалити виділене
            Waiters.sleep(500); // Додайте невелику затримку
            status_input.sendKeys(status);
            Waiters.sleep(1000); // Всі ці кроки зроблені, бо не видаляється з першого разу

            // Залежно від введеного значення статусу, виберіть відповідний елемент зі списку
            if (status.equalsIgnoreCase("CREATE")) {
                WebElement createOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'v-list-item-title') and .//span[@class='v-autocomplete__mask' and text()='Create']]")));
                createOption.click();
            } else if (status.equalsIgnoreCase("PURCHASE")) {
                WebElement purchaseOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'v-list-item-title') and .//span[@class='v-autocomplete__mask' and text()='Purchase']]")));
                purchaseOption.click();
            }

            // Натисніть кнопку пошуку
            Waiters.appearElement(getDriver(), searchBtn);
            searchBtn.click();
            Waiters.sleep(2000);

            return findRecordInResultTable(orderID, invoices_table);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public String payInvoice(String url, String[] card) {
        EnterPaymentCardPage enter = new EnterPaymentCardPage(getDriver());
        Waiters.sleep(2000);
        return enter.enterCardInfo(card);
    }

    public String payMultipayInvoice(String url, String[] card) {
        GoPaymentCardPage enter = new GoPaymentCardPage(getDriver());
        Waiters.sleep(2000);
        return enter.enterCardInfo(card);
    }

    public String getStrFromClipboard() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        try {
            String result = (String) clipboard.getData(DataFlavor.stringFlavor);
            return result;
        } catch (UnsupportedFlavorException ex) {
            System.out.println(ex);
        } catch (IOException e) {
            System.out.println(e);
        }
        return "";
    }
}
