package tests.dashboard.functional.pages;

import methods.Waiters;
import org.openqa.selenium.*;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import tests.paylink.redirect.BasePage;

import java.io.File;
import java.security.Key;
import java.time.Duration;
import java.util.Locale;


public class PaymentsPage extends BasePage {

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/nav/div/div[2]/div[2]/div[1]")
    protected WebElement payments_tab;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/nav/div/div[2]/div[2]/div[2]/a[1]/div[2]/div")
    protected WebElement transactions_tab;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"submenu3\"]/a[2]/div/span")
    protected WebElement installment_tab;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/form/div[1]/div[1]/div/div[1]/div/div[3]/div/input")
    protected WebElement merchant_input;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"__BVID__297\"]/form/div[2]/div[1]/div/div/div/div/label/input")
    protected WebElement tranCode_input;

    @CacheLookup
    @FindBy(xpath = "(//*[@class=\"v-input__control\"])[3]//input")
    protected WebElement orderId_input;

    @CacheLookup
    @FindBy(xpath = "(//label[contains(normalize-space(.), 'RRN')])[1]/following-sibling::input")
    protected WebElement rrn_input;

    @CacheLookup
    @FindBy(xpath = "(//label[contains(normalize-space(.), 'Card number')])[1]/following-sibling::input")
    protected WebElement cardNumber_input;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"__BVID__297\"]/form/div[1]/div[2]/div/div/div/div/label/input")
    protected WebElement operationType_input;

    @CacheLookup
    @FindBy(xpath = "//*[text()=\"Show additional filters\"]/..")
    protected WebElement showAdditionalFilters_button;

    @CacheLookup
    @FindBy(xpath = "(//label[contains(normalize-space(.), 'Approval code')])[1]/following-sibling::input")
    protected WebElement approvalCode_input;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"__BVID__297\"]/form/div[4]/div[2]/div/div/div/label/input")
    protected WebElement ipAdress_input;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"__BVID__297\"]/form/div[5]/div[2]/div/div/div/label/input")
    protected WebElement bankPayment_input;

    @CacheLookup
    //@FindBy(xpath = "//button[@class='btn btn-primary']")
    @FindBy(xpath = "//span[text()=\"Apply\"]//../../button[2]")
    protected WebElement searchBtn;

    @CacheLookup
    @FindBy(className = "collapse-trigger")
    protected WebElement expandTrigger;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div[1]/div/form/div[1]/div[1]/div/div/div/div/ul")
    protected WebElement merchants_list;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/div/div/div[3]")
    protected WebElement resultTable_PaymenyTab;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/div/div[2]/table/tbody")
    protected WebElement resultTable_PaymenyTab2;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/ul")
    protected WebElement orderID_header;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div[2]/div[1]/div/div/div[1]/div/div[2]/div[1]/table/tbody/tr[1]/td[2]")
    protected WebElement orderID_tr;

    @CacheLookup
    @FindBy(xpath = "//table//tr[td[1][normalize-space(text())='Amount']]/td[2]")
    protected WebElement amount_tr;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div/div[2]/form/div/div[3]/div/div[2]")
    protected WebElement amountFR_tr;


    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div[2]/div[1]/div/div/div[1]/div/div[2]/div[1]/table/tbody/tr[5]/td[2]")
    protected WebElement fee_tr;

    @CacheLookup
    @FindBy(xpath = "//button[span[text()='Refund']]")
    protected WebElement reversal_btn;


    @CacheLookup
    @FindBy(css = "div.d-flex.flex-wrap > button.v-btn.bg-primary.v-btn--variant-flat:nth-of-type(4)")
    protected WebElement fastrefund_btn;


    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div/div[2]/form/div/div[3]/div/div[1]/div/div/div[1]/div/div[3]/input")
    protected WebElement reversalAmount_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div/div[2]/form/div/div[3]/div[1]/div/div/div[1]/div/div[3]/input")
    protected WebElement fastrefundAmount_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div/div[2]/form/div/div[3]/div[2]/div/div/div[1]/div/div[3]/textarea")
    protected WebElement fastrefundReason_input;

    @CacheLookup
    @FindBy(css = "div.v-selection-control.v-radio input[type='radio'][aria-label='Enter a new card for refund']")
    protected WebElement newCardRadioButton;

    @CacheLookup
    @FindBy(css = "div.v-field__field > input.v-field__input[type='text'][maxlength='16']")
    protected WebElement fastrefundCardNumber_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div/div[2]/div/div[4]/button[2]")
    protected WebElement refund_btn;

    @CacheLookup
    //@FindBy(xpath = "/html/body/div[2]/div/div[2]/div/div[4]/button/span[3]")
    @FindBy(xpath = "//button[contains(@class, 'v-btn') and .//span[text()='OK']]")
    protected WebElement refundOk_btn;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/div/div[2]/div[2]/div/div/div[1]/div/div/div[2]/div/button/span[3]")
    protected WebElement receiptFR_btn;


    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div/div[2]/form/div/div[4]/button[2]")
    protected WebElement reversalOK_btn;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[4]/div[1]/div/div[1]/button")
    protected WebElement modalX_btn;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/div/div[1]/button")
    protected WebElement expot_btn;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/div/div/div[3]/div/div/div[1]/div[1]/button[2]")
    protected WebElement stopList_btn;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div/div[2]/form/div/div[3]/div/div[1]/div/div/div[1]/div/div[3]/div")
    protected WebElement slModalExp_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div/div[2]/form/div/div[3]/div/div[1]/div/div/div[1]/div/div[3]/div/input")
    protected WebElement slModalExp_ul;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div/div[2]/form/div/div[3]/div/div[2]/div/div/div[1]/div/div[3]/textarea")
    protected WebElement slModalComment_textarea;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[2]/div/div[2]/form/div/div[4]/button[2]")
    protected WebElement slModalOk_btn;

    @CacheLookup
    @FindBy(xpath = "//button[span[text()='Receipt']]")
    protected WebElement receipt_btn;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/div/div[2]/table/tbody/tr[1]/td[12]/a")
    protected WebElement payment_open;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[4]/div[1]/div/div[2]/div/form/div/div[1]/div/div/div/label/input")
    protected WebElement emailModal_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[4]/div[1]/div/div[2]/div/form/div/div[2]/button")
    protected WebElement ReceiptOkModal_btn;

    @CacheLookup
    @FindBy(className = "success")
    protected WebElement success;

    @CacheLookup
    //@FindBy(css = ".mdi-menu.mdi.v-icon.notranslate.v-theme--myCustomLightTheme.v-icon--size-default")
    @FindBy(css = ".mdi-menu.mdi.v-icon.notranslate.v-theme--default-upc.v-icon--size-default")
    protected WebElement icon_menu;


    public static String filePath =  System.getProperty("user.home") + java.io.File.separator + "Downloads";


    public PaymentsPage(WebDriver driver) {
        super(driver);
    }

    public boolean findTranByMerchant(String merchant, String order){
        Waiters.appearElement(getDriver(), icon_menu);
        icon_menu.click();

        Waiters.clickableElement(getDriver(), transactions_tab);
        transactions_tab.click();
        Waiters.sleep(1500);

        merchant_input.click();
        merchant_input.clear();
        merchant_input.sendKeys(merchant);
        Waiters.sleep(1500);

        // Зачекайте, поки з'явиться випадаюче меню і знайдіть відповідний елемент
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        WebElement merchantOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-list-item-title' and text()='" + merchant + "']")));
        merchantOption.click();

        Waiters.clickableElement(getDriver(), searchBtn);
        searchBtn.click();
        Waiters.sleep(1500);
        return CommonFunctions.findRecordInResultTable(order, resultTable_PaymenyTab2);
    }

    public boolean findTranByOrderId(String trackingId){
        Waiters.appearElement(getDriver(), icon_menu);
        icon_menu.click();
        Waiters.appearElement(getDriver(), transactions_tab);
        transactions_tab.click();
        Waiters.appearElement(getDriver(), orderId_input);
        Waiters.sleep(1500);
        orderId_input.click();
        Waiters.sleep(1500);
        orderId_input.sendKeys(trackingId);
        Waiters.sleep(1500);
        Waiters.appearElement(getDriver(), searchBtn);
        Waiters.sleep(1500);
        searchBtn.click();
        Waiters.sleep(1500);
// Натискання на іконку для відкриття транзакції
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        WebElement openIcon = wait.until(ExpectedConditions.elementToBeClickable(
           //     By.cssSelector("i.mdi-folder-open.mdi.v-icon.notranslate.v-theme--myCustomLightTheme.v-icon--size-default.text-primary")));
        By.cssSelector("i.mdi-folder-open.mdi.v-icon.notranslate.v-theme--default-upc.v-icon--size-default.text-primary")));

        openIcon.click();
        return CommonFunctions.findRecordInResultTable(trackingId, resultTable_PaymenyTab);

    }

    public boolean findTranByRRN(String trackingId, String rrn){
        Waiters.appearElement(getDriver(), icon_menu);
        icon_menu.click();
        Waiters.appearElement(getDriver(), transactions_tab);
        transactions_tab.click();
        Waiters.appearElement(getDriver(), showAdditionalFilters_button);
        showAdditionalFilters_button.click();
        Waiters.appearElement(getDriver(), rrn_input);
        Waiters.sleep(1500);
        rrn_input.click();
        Waiters.sleep(1500);
        rrn_input.sendKeys(rrn);
        Waiters.sleep(1500);
        Waiters.appearElement(getDriver(), searchBtn);
        Waiters.sleep(1500);
        searchBtn.click();
        Waiters.sleep(1500);
        return CommonFunctions.findRecordInResultTable(trackingId, resultTable_PaymenyTab2);
    }

    public boolean findTranByCard(String trackingId, String card){
        Waiters.appearElement(getDriver(), icon_menu);
        icon_menu.click();
        Waiters.appearElement(getDriver(), transactions_tab);
        transactions_tab.click();
        Waiters.appearElement(getDriver(), cardNumber_input);
        Waiters.sleep(1500);
        cardNumber_input.click();
        Waiters.sleep(1500);
        cardNumber_input.sendKeys(CommonFunctions.applyMask(card));
        Waiters.sleep(1500);
        Waiters.appearElement(getDriver(), searchBtn);
        Waiters.sleep(1500);
        searchBtn.click();
        Waiters.sleep(1500);
        return CommonFunctions.findRecordInResultTable(trackingId, resultTable_PaymenyTab2);
    }

    public boolean findTranByApprovalCode(String trackingId, String app_code){
        Waiters.appearElement(getDriver(), icon_menu);
        icon_menu.click();
        Waiters.appearElement(getDriver(), transactions_tab);
        transactions_tab.click();
        Waiters.sleep(1500);
        Waiters.appearElement(getDriver(), showAdditionalFilters_button);
        showAdditionalFilters_button.click();
        Waiters.appearElement(getDriver(), approvalCode_input);
        approvalCode_input.click();
        Waiters.sleep(2000);
        approvalCode_input.sendKeys(app_code);
        Waiters.appearElement(getDriver(), searchBtn);
        Waiters.sleep(2000);
        searchBtn.click();
        Waiters.sleep(2000);
        return CommonFunctions.findRecordInResultTable(trackingId, resultTable_PaymenyTab2);
    }

    public boolean findTranByMultFields(String order, String merchant, String rrn, String card, String app_code){
        Waiters.appearElement(getDriver(), icon_menu);
        icon_menu.click();
        Waiters.appearElement(getDriver(), transactions_tab);
        transactions_tab.click();
        Waiters.appearElement(getDriver(), orderId_input);
        Waiters.sleep(1500);
        orderId_input.click();
        Waiters.sleep(1500);
        orderId_input.sendKeys(order);
        Waiters.sleep(1500);
        merchant_input.click();
        Waiters.sleep(1500);
        merchant_input.clear();
        Waiters.sleep(1500);
        merchant_input.sendKeys(merchant);
        Waiters.sleep(1500);

        // Зачекайте, поки з'явиться випадаюче меню і знайдіть відповідний елемент
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        WebElement merchantOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-list-item-title' and text()='" + merchant + "']")));
        merchantOption.click();
        Waiters.appearElement(getDriver(), showAdditionalFilters_button);
        showAdditionalFilters_button.click();
        Waiters.appearElement(getDriver(), rrn_input);
        Waiters.sleep(1500);
        rrn_input.click();
        Waiters.sleep(1500);
        rrn_input.sendKeys(rrn);
        Waiters.sleep(1500);

        Waiters.appearElement(getDriver(), cardNumber_input);
        Waiters.sleep(1500);
        cardNumber_input.click();
        Waiters.sleep(1500);
        cardNumber_input.sendKeys(CommonFunctions.applyMask(card));
        Waiters.sleep(1500);

        Waiters.appearElement(getDriver(), approvalCode_input);
        approvalCode_input.click();
        Waiters.sleep(1500);
        approvalCode_input.sendKeys(app_code);
        Waiters.sleep(1500);

        Waiters.sleep(1500);
        Waiters.appearElement(getDriver(), approvalCode_input);
        approvalCode_input.click();
        Waiters.sleep(1500);
        approvalCode_input.sendKeys(app_code);
        Waiters.sleep(1500);
        Waiters.appearElement(getDriver(), searchBtn);
        Waiters.sleep(1500);
        searchBtn.click();

        return CommonFunctions.findRecordInResultTable(order, resultTable_PaymenyTab2);
    }

    public boolean openTransaction(String trackingId,  String tranID){
        findTranByOrderId(trackingId);
        //boolean is_opened = CommonFunctions.clickOnTran(trackingId, resultTable_PaymenyTab);
        //Waiters.sleep(1500);
        //System.out.println(orderID_header.getText().contains(tranID));
        //if(is_opened) {
            if(orderID_header.getText().contains(tranID)){
                Waiters.sleep(2000);
                return true;
            }
        //}
        return false;
    }

    public void doReversal(String trackingId,  String tranID){
        openTransaction(trackingId, tranID);
        String amount = amount_tr.getText().split(" ")[0];
        amount = String.valueOf(Double.parseDouble(amount));
        System.out.println("Summ from table td: " + amount);
        reversal_btn.click();
        reversalAmount_input.click();
        reversalAmount_input.sendKeys(amount);
        Waiters.sleep(1500);
        reversalOK_btn.click();
    }

    public void doReversalFR(String trackingId, String tranID) {
        openTransaction(trackingId, tranID);
        reversal_btn.click();
        reversalAmount_input.click();
        Waiters.sleep(2500);

        // Получение суммы и обработка текста
        String amountText = amountFR_tr.getText().trim(); // Убираем лишние пробелы

        if (amountText.isEmpty()) {
            throw new IllegalArgumentException("Сумма не найдена в элементе: " + amountFR_tr);
        }

        amountText = amountText.replace("Max:", "").trim(); // Убираем "Max:" и лишние пробелы
        amountText = amountText.replace(",", "."); // Замена запятой на точку, если нужно

        try {
            // Проверяем, является ли строка числом
            double amount = Double.parseDouble(amountText); // Парсим значение
            String formattedAmount = String.format(Locale.US, "%.2f", amount); // Форматируем до 2 знаков после точки
            System.out.println("Summ from table td: " + formattedAmount);

            // Очищаем поле перед вводом
            reversalAmount_input.sendKeys(Keys.CONTROL + "a");
            reversalAmount_input.sendKeys(Keys.DELETE);
            reversalAmount_input.sendKeys(formattedAmount);
        } catch (NumberFormatException e) {
            System.err.println("Некорректное значение суммы: " + amountText);
            throw new IllegalArgumentException("Сумма не является числом: " + amountText, e);
        }

        Waiters.sleep(1500);
        reversalOK_btn.click();
    }

    public void doFastRefund(String trackingId, String tranID) {
        openTransaction(trackingId, tranID);
        String amount = amount_tr.getText().split(" ")[0];
        amount = String.valueOf(Double.parseDouble(amount));
        System.out.println("Summ from table td: " + amount);
        fastrefund_btn.click();
        fastrefundReason_input.click();
        fastrefundReason_input.sendKeys(amount);
        newCardRadioButton.click();
        fastrefundCardNumber_input.click();
        fastrefundCardNumber_input.sendKeys(cardMCardfr);
        Waiters.sleep(3500);
        reversalOK_btn.click();
        Waiters.sleep(3500);
        refund_btn.click();
        Waiters.sleep(3500);
        refundOk_btn.click();
        Waiters.sleep(3500);

        // Пошук елемента з посиланням на транзакцію
        WebElement refundLink = null;
        try {
            //refundLink = getDriver().findElement(By.xpath("//td/div/a[contains(@class, 'link') and contains(text(), 'refunded')]"));
            refundLink = getDriver().findElement(By.xpath("//td/div/a[contains(@class, 'link') and contains(text(), 'returned')]"));

            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", refundLink);
        } catch (NoSuchElementException e) {
            Assert.fail("Refund link not found on the page.");
        }

        receiptFR_btn.click();

        // Додаткова перевірка наявності файлу чеку в папці завантажень
        String downloadPath = System.getProperty("user.home") + java.io.File.separator + "Downloads";
        File downloadDir = new File(downloadPath);
        boolean isFileDownloaded = false;

// Очікування завантаження файлу (максимум 30 секунд)
        int waitTime = 30; // Максимальний час очікування в секундах
        int elapsedTime = 0;

        while (elapsedTime < waitTime) {
            File[] files = downloadDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().startsWith("receipt") && file.getName().endsWith(".pdf")) {
                        isFileDownloaded = true;
                        break;
                    }
                }
            }
            if (isFileDownloaded) {
                break;
            }
            try {
                Thread.sleep(1000); // Затримка 1 секунда
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            elapsedTime++;
        }

        if (!isFileDownloaded) {
            throw new AssertionError("Файл чеку не знайдено в папці завантажень!");
        }

        System.out.println("Файл чеку успішно завантажено.");
    }


    public void doPartialFastRefund(String trackingId, String tranID) {
        openTransaction(trackingId, tranID);
        String amount = amount_tr.getText().split(" ")[0];
        double halfAmount = Double.parseDouble(amount) / 5; // Ділимо суму на 3
        String halfAmountStr = String.format(Locale.US, "%.1f", halfAmount); // Форматуємо з крапкою як роздільником
        System.out.println("Половина суми: " + halfAmountStr);
        fastrefund_btn.click();
        fastrefundAmount_input.click();
        fastrefundAmount_input.sendKeys(Keys.CONTROL + "a"); // Виділення всього тексту
        fastrefundAmount_input.sendKeys(Keys.DELETE); // Видалення виділеного тексту
        Waiters.sleep(1500);
        fastrefundAmount_input.sendKeys(halfAmountStr);
        fastrefundReason_input.click();
        fastrefundReason_input.sendKeys(halfAmountStr);
        newCardRadioButton.click();
        fastrefundCardNumber_input.click();
        fastrefundCardNumber_input.sendKeys(cardVISAfr);
        Waiters.sleep(1500);
        reversalOK_btn.click();
        Waiters.sleep(2500);
        refund_btn.click();
        Waiters.sleep(4500);
        refundOk_btn.click();
        Waiters.sleep(4500);

        // Пошук елемента з посиланням на транзакцію
        WebElement refundLink = null;
        try {
            //refundLink = getDriver().findElement(By.xpath("//td/div/a[contains(@class, 'link') and contains(text(), 'refunded')]"));
            refundLink = getDriver().findElement(By.xpath("//td/div/a[contains(@class, 'link') and contains(text(), 'returned')]"));
            ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", refundLink);
        } catch (NoSuchElementException e) {
            Assert.fail("Refund link not found on the page.");
        }

        receiptFR_btn.click();
        // Перевірка успішності завантаження чеку
        boolean isReceiptLoaded = receiptFR_btn.isDisplayed() && receiptFR_btn.isEnabled();
        Assert.assertTrue(isReceiptLoaded, "Чек успешно загружен");

        // Додаткова перевірка наявності файлу чеку в папці завантажень
        String downloadPath = System.getProperty("user.home") + java.io.File.separator + "Downloads";
        File downloadDir = new File(downloadPath);
        File[] files = downloadDir.listFiles();
        boolean isFileDownloaded = false;

        if (files != null) {
            for (File file : files) {
                if (file.getName().contains("receipt") && file.getName().endsWith(".pdf")) { // Замініть "receipt" на частину назви файлу
                    isFileDownloaded = true;
                    break;
                }
            }
        }

        Assert.assertTrue(isFileDownloaded, "Файл чеку не знайдено в папці завантажень!");
        System.out.println("Файл чеку успішно завантажено.");
    }


    public boolean doExportTransactions(){
        Waiters.appearElement(getDriver(), icon_menu);
        icon_menu.click();
        Waiters.appearElement(getDriver(), transactions_tab);
        transactions_tab.click();
        Waiters.appearElement(getDriver(), expot_btn);
        Waiters.sleep(2000);
        expot_btn.click();
        Waiters.sleep(3000);
        return checkDownloadedFile();
    }

    public boolean checkDownloadedFile(){
        File dir = new File(filePath);
        File[] dirContents = dir.listFiles();
        Waiters.sleep(1500);
        for (File file:dirContents){
            System.out.println(file.getName());
        }
        Waiters.sleep(1500);

        for (int i = 0; i < dirContents.length; i++) {
            if (dirContents[i].getName().contains("transactions")) {
                System.out.println(dirContents.length);
                Waiters.sleep(1500);
                dirContents[i].delete();
                return true;
            }
        }
        return false;
    }



    public boolean putToStopList(String trackingId, String tranID) {
        openTransaction(trackingId, tranID);
        stopList_btn.click();

        slModalComment_textarea.click();
        Waiters.sleep(1500);
        slModalComment_textarea.sendKeys("Stop List Test");
        Waiters.sleep(1500);
        Waiters.clickableElement(getDriver(), slModalExp_input);
        slModalExp_input.click();
        Waiters.sleep(1500);
        // Введення тексту "Day" у поле
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        WebElement dayOption = null;
        try {
            dayOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'v-list-item-title') and text()='Day']")));
        } catch (TimeoutException e) {
            System.out.println("Element with text 'Day' not found within the timeout period.");
            return false;
        }

        dayOption.click();
        Waiters.sleep(2500);

        slModalOk_btn.click();
        return true;
    }

    public boolean sendReceipt(String trackingId, String tranID, String email){
        openTransaction(trackingId, tranID);
        Waiters.clickableElement(getDriver(), receipt_btn);
        receipt_btn.click();
        Waiters.clickableElement(getDriver(), emailModal_input);
        emailModal_input.sendKeys(email);
        Waiters.clickableElement(getDriver(), ReceiptOkModal_btn);
        ReceiptOkModal_btn.click();
        if(success.isEnabled()){
            return true;
        }
        else return false;
    }

    public boolean downloadReceipt(String trackingId, String tranID){
        openTransaction(trackingId, tranID);
        Waiters.clickableElement(getDriver(), receipt_btn);
        receipt_btn.click();
        Waiters.sleep(1500);
        return true;
    }

}
