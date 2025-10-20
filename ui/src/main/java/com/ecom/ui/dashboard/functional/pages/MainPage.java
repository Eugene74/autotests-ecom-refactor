package com.ecom.ui.dashboard.functional.pages;

import com.ecom.ui.util.Waiters;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.ecom.ui.common.BasePage;
import static com.ecom.core.config.EnvData.*;
import java.time.Duration;
import java.io.File;
import java.util.List;



public class MainPage extends BasePage {

    @CacheLookup
    //@FindBy(xpath = "//*[@id=\"app\"]/div[1]/aside/nav/div/a[2]/div/span[2]")
    @FindBy(xpath = "//*[@id=\"app\"]/div/div/nav/div/div[2]/a[2]")
    protected WebElement merchantsTab;

    @CacheLookup
    @FindBy(className = "collapse-trigger")// "//*[@id=\"app\"]/div[2]/div/div/div[2]/a")
    protected WebElement expandTrigger;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"__BVID__145\"]/form/div[1]/div[1]/div/div/div/label/span")
    protected WebElement merchantNameExpand;

     @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/form/div[1]/div[1]/div")
    protected WebElement merchantName;


    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div[2]/div/form/div[1]/div[1]/div/div/div/ul")
    protected WebElement merchantList;


    @CacheLookup
    @FindBy(xpath = "//button[@type='submit' and contains(@class, 'bg-primary')]")
    protected WebElement searchBtn;
    @CacheLookup
    //@FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div[3]/div[1]/table/tbody")
    @FindBy(css = "div.v-table__wrapper > table > tbody")
    protected WebElement resultTable_MerchTab;

    @CacheLookup
    @FindBy(css = ".v-data-table__tbody")
    protected WebElement resultTable_MTDomesticTab;


    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div[3]/div[1]/div/table/tbody")
    protected WebElement resultTable_MTInterTab;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div[2]/div/form/div[2]/div[1]/div/div/div/label/input")
    protected WebElement merchantID;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/form/div[2]/div[2]/div/div[1]/div/div[3]/input")
    protected WebElement terminalID;

    @CacheLookup
    //@FindBy(xpath = "/html/body/div[1]/div/div/div[1]/aside/nav/div/a[3]")
    @FindBy(xpath = "/html/body/div[1]/div/div/nav/div/div[2]/div[1]/div[1]/div[2]/div")
    protected WebElement moneyTransferTab;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/nav/div/div[2]/div[1]/div[2]/a[1]/div[2]/div")
    protected WebElement domesticMTTab;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"submenu2\"]/a[2]")
    protected WebElement interMTTab;

    @CacheLookup
    //@FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div[2]/div/form/div[1]/div[1]/div/div[1]/div/div/div/label/input")
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/form/div[1]/div[1]/div/div[1]/div/div[3]/input")
    protected WebElement tranID_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/form/div[1]/div[3]/div/div[1]/div/div[3]/input")
    protected WebElement merchantID_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div[2]/div/form/div[1]/div[1]/div/div[2]/div/div/div/div/ul")
    protected WebElement merchantID_list;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/main/div/form/div[1]/div[3]/div/div[1]/div/div[3]/input")
    protected WebElement cardNumber_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div[2]/div/form/div[1]/div[2]/div/div[2]/div/div/div/div/div/label/input")
    protected WebElement cardNumberInter_input;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"app\"]/div[2]/div/div/div[1]/div/div/h2")
    protected WebElement transactionID_text;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"app\"]/div[2]/div/div/div[3]/div[2]/div/div[2]/div/span/button")
    protected WebElement expot_btn;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[4]/div[1]/div/div[2]/div/form/div/div/button")
    protected WebElement exportOK_btn;


    @CacheLookup
    @FindBy(xpath = "//*[@id=\"submenu2\"]/a[3]/div/span")
    protected WebElement exchangeRate_tab;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div/div[4]/button")
    protected WebElement createRate_btn;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"exachangeForm\"]/div[1]/div/div[2]/div/div/form/div[1]/div[1]/div/div/div/div/label/input")
    protected WebElement currencyFrom_input;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"exachangeForm\"]/div[1]/div/div[2]/div/div/form/div[1]/div[2]/div/div/div/div/label/input")
    protected WebElement currencyTo_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[4]/div[1]/div/div[2]/div/div/form/div[1]/div[3]/div/div/div/label/input")
    protected WebElement rate_input;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"exachangeForm\"]/div[1]/div/div[2]/div/div/form/div[1]/div[4]/div/div/div/div/label/input")
    protected WebElement bank_input;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"exachangeForm\"]/div[1]/div/div[2]/div/div/form/div[2]/button")
    protected WebElement saveRate_btn;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"exachangeForm\"]/div[1]/div/div[2]/div/div/form/div[1]/div[1]/div/div/div/div/ul")
    protected WebElement currencyFrom_list;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"exachangeForm\"]/div[1]/div/div[2]/div/div/form/div[1]/div[2]/div/div/div/div/ul")
    protected WebElement currencyTo_list;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"exachangeForm\"]/div[1]/div/div[2]/div/div/form/div[1]/div[4]/div/div/div/div/ul")
    protected WebElement bank_list;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"app\"]/div[2]/div/div/div/ul/li[2]/a")
    protected WebElement introdused_nav;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div/div[3]/div[1]/table/tbody")
    protected WebElement introdusedTable;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[4]/div[1]/div/div[2]/div/div[2]/button[1]")
    protected WebElement confirmDeletingER_btn;

    @CacheLookup
    @FindBy(xpath = "//div[@class='col-p']//div//*[name()='svg']")
    protected WebElement deletingER_btn;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div[1]/div/div/h2")
    protected WebElement orderID_header;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"app\"]/div[2]/div/div/div/div/div/div/h2/small")
    protected WebElement merchantHeader_header;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div[2]/div/form/div[1]/div[2]/div/div[1]/div/div/div/div/label/input")
    protected WebElement statusCB_input;

    @CacheLookup
    @FindBy(xpath = "/html/body/div[1]/div/div/div[2]/div/div/div[2]/div/form/div[1]/div[2]/div/div[1]/div/div/div/div/ul")
    protected WebElement statusCB_ul;

    @CacheLookup
    @FindBy(xpath = "//*[@data-datepicker-instance]//input")
    protected WebElement calendarOpenButton;

   // @CacheLookup
    //@FindBy(css = "button.material-btn:nth-child(2)")
   // @FindBy(xpath = "//*[@id=\"app\"]/div/div/main/div/div/div[2]/div[2]/div/div/div[1]/div/div/div[2]/div/button[1]/span[3]")
  //  protected WebElement receipt_btn;

    @CacheLookup
    @FindBy(xpath = "//button[contains(@class, 'v-btn') and contains(@class, 'bg-primary') and .//span[text()='Receipt']]")
    protected WebElement receipt_btn;


    @CacheLookup
    //@FindBy(css = ".mdi-menu.mdi.v-icon.notranslate.v-theme--myCustomLightTheme.v-icon--size-default")
    @FindBy(css = ".mdi-menu.mdi.v-icon.notranslate.v-theme--default-upc.v-icon--size-default")
    protected WebElement icon_menu;

    @CacheLookup
    @FindBy(css = ".dp__overlay")
    protected WebElement timePickerOverlay;

    @CacheLookup
    @FindBy(css = "button[aria-label='Open time picker']")
    protected WebElement timePickerButton;

    @CacheLookup
    @FindBy(xpath = "//button[contains(@class, 'v-btn--variant-flat')]//span[text()='OK']")
    protected WebElement confirmButton;

    public class TimePickerPage {
        private WebDriver driver;

        public TimePickerPage(WebDriver driver) {
            this.driver = driver;
            PageFactory.initElements(driver, this);
        }

        @CacheLookup
        @FindBy(css = ".dp__overlay")
        protected WebElement timePickerOverlay;

        @CacheLookup
        @FindBy(css = "button[data-test-id='open-time-picker-btn']")
        protected WebElement timePickerButton;

        @CacheLookup
        @FindBy(xpath = "//button[contains(@class, 'v-btn--variant-flat') and text()='OK']")
        protected WebElement confirmButton;


        // Відкриває таймпікер, натискаючи відповідні кнопки
        public void openTimePicker() {
            try {
                // Знаходить кнопку для відкриття календаря і натискає на неї
                calendarOpenButton.click();
                Waiters.sleep(1000); // Додатковий sleep для надійності

                // Натискає кнопку для відкриття таймпікера
                timePickerButton.click();
                Waiters.sleep(1000); // Додатковий sleep для надійності
            } catch (Exception e) {
                e.printStackTrace(); // Виводить помилку, якщо щось пішло не так
            }
        }

        // Змінює час на дві хвилини вперед
        public void setTimeTwoMinutesAhead() {
            try {
                // Знаходить всі елементи часу у таймпікері
                List<WebElement> timeInputs = timePickerOverlay.findElements(By.cssSelector(".dp__time_input"));

                // Проходить через всі елементи часу
                for (int i = 0; i < timeInputs.size(); i++) {
                    WebElement timeInput = timeInputs.get(i);
                    try {
                        // Чекає, поки кнопка інкременту хвилин стане видимою
                        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
                        WebElement incrementMinutesButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("button[data-test-id='minutes-time-inc-btn-" + i + "']")));

                        // Знаходить відображення хвилин
                        WebElement minutesDisplay = timeInput.findElement(By.cssSelector("button[data-test-id='minutes-toggle-overlay-btn-" + i + "']"));

                        // Змінює значення хвилин на 2 вперед
                        incrementTime(incrementMinutesButton, minutesDisplay, 2, 60);
                    } catch (Exception e) {
                        e.printStackTrace(); // Виводить помилку для конкретного елемента, якщо щось пішло не так
                    }
                }
            } catch (Exception e) {
                e.printStackTrace(); // Виводить загальну помилку, якщо щось пішло не так
            }
        }

        // Інкрементує значення часу
        private void incrementTime(WebElement incrementButton, WebElement timeDisplay, int increment, int max) throws InterruptedException {
            // Отримує поточне значення хвилин
            int currentTime = Integer.parseInt(timeDisplay.getText());
            // Обчислює цільове значення хвилин
            int targetTime = (currentTime + increment) % max;

            // Натискає кнопку інкременту, поки не досягне цільового значення
            while (Integer.parseInt(timeDisplay.getText()) != targetTime) {
                incrementButton.click(); // Натискає кнопку
                Thread.sleep(500); // Додатковий sleep для надійності
            }
        }

        // Підтверджує вибір часу
        public void confirmTimeSelection() {
            try {
                confirmButton.click(); // Натискає кнопку підтвердження
                Waiters.sleep(1000); // Додатковий sleep для надійності
            } catch (Exception e) {
                e.printStackTrace(); // Виводить помилку, якщо щось пішло не так
            }
        }
    }


     public static String filePath = System.getProperty("user.home") + java.io.File.separator + "Downloads";

    public MainPage(WebDriver driver) {
        super(driver);
    }

    public String selectMerchantByIndex(int index) {
        Waiters.clickableElement(getDriver(), merchantName);
        merchantName.click();
        Waiters.clickableElement(getDriver(), merchantList);
        List<WebElement> merchants = merchantList.findElements(By.tagName("li"));
        String name = merchants.get(index).getText();
        merchants.get(index).click();
        Waiters.clickableElement(getDriver(), searchBtn);
        searchBtn.click();
        return name;
    }

    public boolean findRecordInResultTable(String merchantName, String merchantId, String terminalId) {
        List<WebElement> results = resultTable_MerchTab.findElements(By.tagName("td"));
        int count = 0;
        if (!results.isEmpty() && resultTable_MerchTab.findElements(By.tagName("tr")).size() == 1) {
            for (WebElement result : results) {
                if (result.getText().equals(merchantName) || result.getText().equals(merchantId) || result.getText().equals(terminalId)) {
                    count++;
                }
            }
        }
        if (count == 3) {
            return true;
        } else {
            return false;
        }
    }

    public boolean doSearchByMerchant() {
        // Знайдіть і клацніть на іконку меню
        Waiters.appearElement(getDriver(), icon_menu);
        icon_menu.click();

        Waiters.clickableElement(getDriver(), merchantsTab);
        merchantsTab.click();
        if (!merchantName.isDisplayed()) {
            Waiters.clickableElement(getDriver(), expandTrigger);
            expandTrigger.click();
        }
        String merchant = this.selectMerchantByIndex(2);
        Waiters.clickableElement(getDriver(), resultTable_MerchTab);
        return CommonFunctions.findRecordInResultTable(merchant, resultTable_MerchTab);
    }


    public boolean doSearchByMerchant(String merchant) {
        try {
            // Знайдіть і клацніть на іконку меню
            Waiters.appearElement(getDriver(), icon_menu);
            icon_menu.click();

            // Клік на вкладку "Merchants"
            Waiters.clickableElement(getDriver(), merchantsTab);
            merchantsTab.click();

            // Перевіряємо, чи відображається поле для введення імені мерчанта
            if (!merchantName.isDisplayed()) {
                Waiters.appearElement(getDriver(), expandTrigger);
                expandTrigger.click();
            }

            // Взаємодія з полем для введення імені мерчанта
            WebElement merchantInput = merchantName.findElement(By.cssSelector("input"));
            Waiters.appearElement(getDriver(), merchantInput);
            merchantInput.click();
            Waiters.sleep(2000);
            merchantInput.sendKeys(merchant);
            Waiters.sleep(2000);

            // Зачекайте, поки з'явиться випадаюче меню і знайдіть відповідний елемент
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            WebElement merchantOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-list-item-title' and text()='" + merchant + "']")));
            merchantOption.click();

            // Натискання кнопки пошуку
            searchBtn.click();
            Waiters.sleep(2000);

            // Перевірка результатів пошуку
            return CommonFunctions.findRecordInResultTable(merchant, resultTable_MerchTab);
        } catch (Exception e) {
            System.out.println("Виникла помилка: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }




    public boolean openMerch(String merchant) {
        try {
            // Знайдіть і клацніть на іконку меню
            Waiters.appearElement(getDriver(), icon_menu);
            icon_menu.click();

            // Клік на вкладку "Merchants"
            Waiters.clickableElement(getDriver(), merchantsTab);
            merchantsTab.click();

            // Очікування таблиці результатів
            Waiters.appearElement(getDriver(), resultTable_MerchTab);

            // Додаткове очікування, щоб переконатися, що таблиця завантажена
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.invisibilityOfElementWithText(By.cssSelector("tbody.v-data-table__tbody tr td"), "Loading items..."));
            //System.out.println("Таблиця завантажена.");

            // Вибір мерчанта зі списку
            List<WebElement> rows = resultTable_MerchTab.findElements(By.cssSelector("tbody.v-data-table__tbody tr"));
            boolean merchantFound = false;
            for (WebElement row : rows) {
                List<WebElement> cells = row.findElements(By.cssSelector("td"));
                for (int i = 0; i < cells.size(); i++) {
                }
                if (cells.size() > 2 && cells.get(2).getText().equals(merchant)) {
                    WebElement icon = cells.get(cells.size() - 1).findElement(By.cssSelector("i.mdi-card-account-details-outline"));
                    icon.click();
                    merchantFound = true;
                    break;
                }
            }

            if (!merchantFound) {
                System.out.println("Мерчанта не знайдено: " + merchant);
                return false;

            }
            Waiters.sleep(2000);
            // Перевірка URL сторінки
            String expectedUrl = "/dashboard/merchants/";
            wait.until(ExpectedConditions.urlContains(expectedUrl));
            boolean urlCheck = getDriver().getCurrentUrl().contains(expectedUrl);
            System.out.println("Поточний URL: " + getDriver().getCurrentUrl());
            return urlCheck;
        } catch (Exception e) {
            System.out.println("Виникла помилка: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }







    public boolean doSearchByMerchantID() {
        try {
            String merchant = merchant_AVAL;

            // Знайдіть і клацніть на іконку меню
            Waiters.appearElement(getDriver(), icon_menu);
            icon_menu.click();

            // Клік на вкладку "Merchants"
            Waiters.clickableElement(getDriver(), merchantsTab);
            merchantsTab.click();

            // Взаємодія з полем для введення ID мерчанта
            merchantID_input.click();
            Waiters.sleep(2000);
            merchantID_input.sendKeys(merchant);

            // Натискання кнопки пошуку
            searchBtn.click();
            Waiters.sleep(2000);

            // Перевірка результатів пошуку
            return CommonFunctions.findRecordInResultTable(merchant, resultTable_MerchTab);
        } catch (Exception e) {
            System.out.println("Виникла помилка: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }





    public boolean doSearchByMultFields() {
        try {
            // Знайдіть і клацніть на іконку меню
            Waiters.appearElement(getDriver(), icon_menu);
            icon_menu.click();

            String merchant = "YVPauto";
            String merchantId = merchant_AVAL;
            String terminalId = terminal_AVAL;

            // Клік на вкладку "Merchants"
            Waiters.clickableElement(getDriver(), merchantsTab);
            merchantsTab.click();

            // Перевіряємо, чи відображається поле для введення імені мерчанта
            if (!merchantName.isDisplayed()) {
                Waiters.appearElement(getDriver(), expandTrigger);
                expandTrigger.click();
            }

            // Знайдемо внутрішній елемент input
            WebElement merchantInput = merchantName.findElement(By.cssSelector("input"));

            // Взаємодія з полем для введення імені мерчанта
            Waiters.appearElement(getDriver(), merchantInput);
            merchantInput.click();
            Waiters.sleep(2000);
            merchantInput.sendKeys(merchant);
            Waiters.sleep(2000);

            // Зачекайте, поки з'явиться випадаюче меню і знайдіть відповідний елемент
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            WebElement merchantOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-list-item-title' and text()='" + merchant + "']")));
            merchantOption.click();

            // Введення ID мерчанта
            merchantID_input.click();
            Waiters.sleep(2000);
            merchantID_input.sendKeys(merchantId);

            // Введення ID терміналу
            terminalID.click();
            Waiters.sleep(2000);
            terminalID.sendKeys(terminalId);

            // Натискання кнопки пошуку
            searchBtn.click();
            Waiters.sleep(2000);

            // Перевірка результатів пошуку
            return findRecordInResultTable(merchant, merchantId, terminalId);
        } catch (Exception e) {
            System.out.println("Виникла помилка: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    private void enterTextUsingJavaScript(WebElement element, String text) {
        JavascriptExecutor js = (JavascriptExecutor) getDriver();
        js.executeScript("arguments[0].value='" + text + "';", element);
        System.out.println("Текст введено за допомогою JavaScript: " + text);
    }



    public boolean findTransactionByTrackingId(String trackingId) {
        clickElement(icon_menu);
        clickElement(moneyTransferTab);
        clickElement(domesticMTTab);

        Waiters.appearElement(getDriver(), tranID_input);
        tranID_input.click();
        Waiters.sleep(1500);
        tranID_input.sendKeys(trackingId);
        if (!merchantName.isDisplayed()) {
            Waiters.appearElement(getDriver(), expandTrigger);
            expandTrigger.click();
        }
        Waiters.appearElement(getDriver(), searchBtn);
        Waiters.sleep(1500);
        searchBtn.click();
        return CommonFunctions.findRecordInResultTable(trackingId, resultTable_MTDomesticTab);
    }

    public boolean findTransactionByMerchantId(String merchant, String trackingId) {
        try {
            // Знайдіть і клацніть на іконку меню
            Waiters.appearElement(getDriver(), icon_menu);
            icon_menu.click();

            // Клік на вкладку "Money Transfer"
            Waiters.appearElement(getDriver(), moneyTransferTab);
            moneyTransferTab.click();

            // Клік на вкладку "Domestic MT"
            Waiters.appearElement(getDriver(), domesticMTTab);
            domesticMTTab.click();

            // Перевіряємо, чи відображається поле для введення імені мерчанта
            if (!merchantName.isDisplayed()) {
                Waiters.appearElement(getDriver(), expandTrigger);
                expandTrigger.click();
            }
            TimePickerPage timePickerPage = new TimePickerPage(getDriver());
            timePickerPage.openTimePicker();
            timePickerPage.setTimeTwoMinutesAhead();
            timePickerPage.confirmTimeSelection();

            // Взаємодія з полем для введення імені мерчанта
            WebElement merchantInput = merchantName.findElement(By.xpath("/html/body/div[1]/div/div/main/div/form/div[1]/div[2]/div/div[1]/div/div[3]/div/input"));
            Waiters.appearElement(getDriver(), merchantInput);
            merchantInput.click();
            Waiters.sleep(500); // Короткий sleep для надійності
            merchantInput.clear();
            merchantInput.sendKeys(merchant);

            // Зачекайте, поки з'явиться випадаюче меню і знайдіть відповідний елемент
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            WebElement merchantOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-list-item-title' and text()='" + merchant + "']")));
            merchantOption.click();

            // Натискання кнопки пошуку
            Waiters.appearElement(getDriver(), searchBtn);
            searchBtn.click();
            Waiters.sleep(1000); // Короткий sleep для надійності

            // Перевірка результатів пошуку
            return CommonFunctions.findRecordInResultTable(trackingId, resultTable_MTDomesticTab);
        } catch (Exception e) {
            System.out.println("Виникла помилка: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }






    public boolean findTransactionByCardNumber(String cardNumber, String trackingId) {
        try {
            clickElement(icon_menu);
            clickElement(moneyTransferTab);
            clickElement(domesticMTTab);

            TimePickerPage timePickerPage = new TimePickerPage(getDriver());
            timePickerPage.openTimePicker();
            timePickerPage.setTimeTwoMinutesAhead();
            timePickerPage.confirmTimeSelection();

            Waiters.appearElement(getDriver(), cardNumber_input);
            String maskedCardNumber = CommonFunctions.applyMask(cardNumber);
            cardNumber_input.clear();
            cardNumber_input.sendKeys(maskedCardNumber);

            clickElement(searchBtn);
            Waiters.sleep(2500);

            return CommonFunctions.findRecordInResultTable(trackingId, resultTable_MTDomesticTab);
        } catch (Exception e) {
            System.out.println("Виникла помилка: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private void clickElement(WebElement element) {
        Waiters.appearElement(getDriver(), element);
        element.click();
        Waiters.sleep(1000); // Додатковий sleep для надійності
    }

    public boolean findTransactionByMultFields(String merchant, String cardNumber, String trackingId) {
        clickElement(icon_menu);
        clickElement(moneyTransferTab);
        clickElement(domesticMTTab);

        tranID_input.click();
        Waiters.sleep(1500);
        tranID_input.sendKeys(trackingId);
               if (!merchantName.isDisplayed()) {
            Waiters.appearElement(getDriver(), expandTrigger);
            expandTrigger.click();
        }

        // Взаємодія з полем для введення імені мерчанта
        WebElement merchantInput = merchantName.findElement(By.xpath("/html/body/div[1]/div/div/main/div/form/div[1]/div[2]/div/div[1]/div/div[3]/div/input"));
        Waiters.appearElement(getDriver(), merchantInput);
        merchantInput.click();
        Waiters.sleep(500); // Короткий sleep для надійності
        merchantInput.clear();
        merchantInput.sendKeys(merchant);

        // Зачекайте, поки з'явиться випадаюче меню і знайдіть відповідний елемент
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
        WebElement merchantOption = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[@class='v-list-item-title' and text()='" + merchant + "']")));
        merchantOption.click();

        Waiters.appearElement(getDriver(), cardNumber_input);
        Waiters.sleep(1500);
        cardNumber_input.sendKeys(CommonFunctions.applyMask(cardNumber));
        Waiters.appearElement(getDriver(), searchBtn);
        Waiters.sleep(1500);
        searchBtn.click();
        Waiters.sleep(1500);
        return CommonFunctions.findRecordInResultTable(trackingId, resultTable_MTDomesticTab);
    }

    public boolean findInterTransactionByTrackingId(String trackingId) {
        Waiters.appearElement(getDriver(), moneyTransferTab);
        moneyTransferTab.click();
        Waiters.appearElement(getDriver(), interMTTab);
        interMTTab.click();
        if (!tranID_input.isDisplayed()) {
            Waiters.appearElement(getDriver(), expandTrigger);
            expandTrigger.click();
        }
        Waiters.appearElement(getDriver(), tranID_input);
        Waiters.sleep(1500);
        tranID_input.click();
        Waiters.sleep(1500);
        tranID_input.sendKeys(trackingId);
        Waiters.sleep(1500);
        Waiters.appearElement(getDriver(), searchBtn);
        Waiters.sleep(1500);
        searchBtn.click();
        Waiters.sleep(1500);
        return CommonFunctions.findRecordInResultTable(trackingId, resultTable_MTInterTab);
    }

    public boolean findInterTransactionByMerchantId(String merchant, String trackingId) {
        Waiters.appearElement(getDriver(), moneyTransferTab);
        moneyTransferTab.click();
        Waiters.appearElement(getDriver(), domesticMTTab);
        interMTTab.click();
        if (!merchantID_input.isDisplayed()) {
            Waiters.appearElement(getDriver(), expandTrigger);
            expandTrigger.click();
        }
        Waiters.appearElement(getDriver(), merchantID_input);
        merchantID_input.click();
        Waiters.sleep(1500);
        merchantID_input.clear();
        merchantID_input.sendKeys(merchant);
        CommonFunctions.chooseFromList(merchantID_list, merchant);
        Waiters.appearElement(getDriver(), searchBtn);
        Waiters.sleep(1500);
        searchBtn.click();
        Waiters.sleep(1500);
        return CommonFunctions.findRecordInResultTable(trackingId, resultTable_MTInterTab);
    }

    public boolean findTransactionInterByCardNumber(String cardNumber, String trackingId) {
        Waiters.appearElement(getDriver(), moneyTransferTab);
        moneyTransferTab.click();
        Waiters.appearElement(getDriver(), interMTTab);
        interMTTab.click();
        if (!cardNumberInter_input.isDisplayed()) {
            Waiters.appearElement(getDriver(), expandTrigger);
            expandTrigger.click();
        }
        Waiters.appearElement(getDriver(), cardNumberInter_input);
        System.out.println(CommonFunctions.applyMask(cardNumber));
        Waiters.sleep(1500);
        cardNumberInter_input.sendKeys(CommonFunctions.applyMask(cardNumber));
        Waiters.appearElement(getDriver(), searchBtn);
        Waiters.sleep(1500);
        searchBtn.click();
        Waiters.sleep(1500);
        return CommonFunctions.findRecordInResultTable(trackingId, resultTable_MTInterTab);
    }

    public boolean findInterTransactionByCardAndStatus(String cardNumber, String trackingId) {
        Waiters.appearElement(getDriver(), moneyTransferTab);
        moneyTransferTab.click();
        Waiters.appearElement(getDriver(), interMTTab);
        interMTTab.click();
        if (!tranID_input.isDisplayed()) {
            Waiters.appearElement(getDriver(), expandTrigger);
            expandTrigger.click();
        }

        Waiters.appearElement(getDriver(), cardNumberInter_input);
        cardNumberInter_input.sendKeys(CommonFunctions.applyMask(cardNumber));
        statusCB_input.click();
        CommonFunctions.chooseFromList(statusCB_ul, "Approved");
        Waiters.appearElement(getDriver(), searchBtn);
        Waiters.sleep(1500);
        searchBtn.click();
        Waiters.sleep(1500);
        return CommonFunctions.findRecordInResultTable(trackingId, resultTable_MTInterTab);
    }


    public boolean findInterTransactionByMultFields(String merchant, String cardNumber, String trackingId) {
        Waiters.appearElement(getDriver(), moneyTransferTab);
        moneyTransferTab.click();
        Waiters.appearElement(getDriver(), interMTTab);
        interMTTab.click();
        if (!tranID_input.isDisplayed()) {
            Waiters.appearElement(getDriver(), expandTrigger);
            expandTrigger.click();
        }
        Waiters.appearElement(getDriver(), tranID_input);
        tranID_input.click();
        tranID_input.sendKeys(trackingId);
        Waiters.appearElement(getDriver(), merchantID_input);
        merchantID_input.click();
        Waiters.sleep(1500);
        merchantID_input.clear();
        merchantID_input.sendKeys(merchant);
        CommonFunctions.chooseFromList(merchantID_list, merchant);
        Waiters.appearElement(getDriver(), cardNumberInter_input);
        cardNumberInter_input.sendKeys(CommonFunctions.applyMask(cardNumber));
        Waiters.appearElement(getDriver(), searchBtn);
        Waiters.sleep(1500);
        searchBtn.click();
        Waiters.sleep(1500);
        return CommonFunctions.findRecordInResultTable(trackingId, resultTable_MTInterTab);
    }

    public boolean openMTDomesticTransaction(String trackingId) {
        try {
            clickElement(icon_menu);
            clickElement(moneyTransferTab);
            clickElement(domesticMTTab);

            // Відкриття таймпікера та встановлення часу
            TimePickerPage timePickerPage = new TimePickerPage(getDriver());
            timePickerPage.openTimePicker();
            timePickerPage.setTimeTwoMinutesAhead();
            timePickerPage.confirmTimeSelection();

            // Введення Tracking ID
            Waiters.appearElement(getDriver(), tranID_input);
            tranID_input.click();
            Waiters.sleep(1500);
            tranID_input.sendKeys(trackingId);

            // Перевірка видимості поля для введення імені мерчанта
            if (!merchantName.isDisplayed()) {
                Waiters.appearElement(getDriver(), expandTrigger);
                expandTrigger.click();
            }

            // Натискання кнопки пошуку
            Waiters.appearElement(getDriver(), searchBtn);
            Waiters.sleep(1500);
            searchBtn.click();

            // Натискання на іконку для відкриття транзакції
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            WebElement openIcon = wait.until(ExpectedConditions.elementToBeClickable(
                    //By.cssSelector("i.mdi-folder-open.mdi.v-icon.notranslate.v-theme--myCustomLightTheme.v-icon--size-default.text-primary")));
            By.cssSelector("i.mdi-folder-open.mdi.v-icon.notranslate.v-theme--default-upc.v-icon--size-default.text-primary")));
            openIcon.click();

            // Очікування появи заголовка "Details"
            WebElement detailsHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//li[contains(@class, 'v-breadcrumbs-item--disabled') and .//a[text()='Details']]")));

            // Перевірка, чи заголовок містить "Details"
            return detailsHeader.getText().contains("Details");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    public boolean openMTInterTransaction(String trackingId) {
        Waiters.appearElement(getDriver(), moneyTransferTab);
        moneyTransferTab.click();
        Waiters.appearElement(getDriver(), interMTTab);
        interMTTab.click();
        if (!tranID_input.isDisplayed()) {
            Waiters.appearElement(getDriver(), expandTrigger);
            expandTrigger.click();
        }
        Waiters.appearElement(getDriver(), tranID_input);
        tranID_input.click();
        Waiters.sleep(1500);
        tranID_input.sendKeys(trackingId);
        Waiters.appearElement(getDriver(), searchBtn);
        Waiters.sleep(1500);
        searchBtn.click();
        Waiters.sleep(1500);
        boolean clicked = CommonFunctions.clickOnTran(trackingId, resultTable_MTInterTab);
        System.out.println(transactionID_text.getText());
        if (transactionID_text.getText().contains(trackingId)) {
            return true;
        }
        return false;
    }

    public boolean doExportInterTransactions() {
        Waiters.appearElement(getDriver(), moneyTransferTab);
        moneyTransferTab.click();
        Waiters.appearElement(getDriver(), interMTTab);
        interMTTab.click();
        Waiters.appearElement(getDriver(), expot_btn);
        Waiters.sleep(2000);
        expot_btn.click();
        Waiters.appearElement(getDriver(), exportOK_btn);
        exportOK_btn.click();
        Waiters.sleep(3000);
        return checkDownloadedFile("cross-border-pereport");
    }

    public boolean checkDownloadedFile(String file_name) {
        File dir = new File(filePath);
        File[] dirContents = dir.listFiles();

        for (File file : dirContents) {
            System.out.println(file.getName());
        }
        Waiters.sleep(2000);

        for (int i = 0; i < dirContents.length; i++) {
            if (dirContents[i].getName().contains(file_name)) {
                System.out.println(dirContents.length);
                Waiters.sleep(2000);
                dirContents[i].delete();
                return true;
            }
        }
        return false;
    }

    public boolean checkCreatedRate(String currencyFrom, String currencyTo, String rate, String bank) {
        introdused_nav.click();
        String[] data = {currencyFrom, currencyTo, rate, bank};
        return (CommonFunctions.findRecordInResultTable(data, introdusedTable) >= 0) ? true : false;
    }

    public boolean deleteFromTableByIndex() {
        Waiters.appearElement(getDriver(), deletingER_btn);
        deletingER_btn.click();
        return true;
    }

    public boolean toCreateExchangeRate(String currencyFrom, String currencyTo, String rate, String bank) {
        Waiters.appearElement(getDriver(), moneyTransferTab);
        moneyTransferTab.click();
        Waiters.appearElement(getDriver(), exchangeRate_tab);
        exchangeRate_tab.click();
        Waiters.appearElement(getDriver(), createRate_btn);
        createRate_btn.click();
        Waiters.appearElement(getDriver(), currencyFrom_input);
        //currencyFrom_input.click();
        currencyFrom_input.sendKeys(currencyFrom);
        CommonFunctions.chooseFromList(currencyFrom_list, currencyFrom);
        Waiters.sleep(1500);
        Waiters.appearElement(getDriver(), currencyTo_input);
        //currencyTo_input.click();
        currencyTo_input.sendKeys(currencyTo);
        CommonFunctions.chooseFromList(currencyTo_list, currencyTo);
        Waiters.sleep(1500);
        Waiters.appearElement(getDriver(), rate_input);
        rate_input.sendKeys(rate);
        Waiters.sleep(2000);
        Waiters.appearElement(getDriver(), bank_input);
        //bank_input.click();
        bank_input.sendKeys(bank);
        Waiters.sleep(3000);
        CommonFunctions.chooseFromList(bank_list, bank);
        //rate_input.click();
        Waiters.sleep(2000);
        Waiters.appearElement(getDriver(), saveRate_btn);
        saveRate_btn.click();
        return checkCreatedRate(currencyFrom, currencyTo, rate, bank);
    }

    public boolean toDeleteExchangeRate(String currencyFrom, String currencyTo, String rate, String bank) {
        Waiters.appearElement(getDriver(), moneyTransferTab);
        moneyTransferTab.click();
        Waiters.appearElement(getDriver(), exchangeRate_tab);
        exchangeRate_tab.click();
        introdused_nav.click();
        //String[] data = {currencyFrom, currencyTo, rate, bank};
        //int index = CommonFunctions.findRecordInResultTable(data, introdusedTable);

        return deleteFromTableByIndex();
    }

    public boolean makeMTReceipt(String tracking_ID) {
        try {
            // Відкрити транзакцію
            if (!openMTDomesticTransaction(tracking_ID)) {
                return false;
            }

            // Додаткове очікування для надійності
            Waiters.sleep(1500);

            // Очікування, поки URL зміниться на новий
            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(10));
            wait.until(ExpectedConditions.urlContains("/dashboard/mt-domestic/"));

            // Очікування появи кнопки "Receipt"
            Waiters.appearElement(getDriver(), receipt_btn);

            // Переконатися, що кнопка видима і клікабельна
            wait.until(ExpectedConditions.elementToBeClickable(receipt_btn));

            // Натискання на кнопку "Receipt"
            receipt_btn.click();

            // Додаткове очікування для завершення дії
            Waiters.sleep(4000);

            // Перевірка, чи файл завантажений
            return checkDownloadedFile("receipt");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMTReceipt(String tracking_ID) {
        openMTDomesticTransaction(tracking_ID);
        receipt_btn.click();
        Waiters.sleep(1500);
        return checkDownloadedFile("receipt");
    }
}