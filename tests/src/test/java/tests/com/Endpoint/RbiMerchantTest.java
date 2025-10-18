package tests.com.Endpoint;

import methods.Waiters;
import org.openqa.selenium.*;
        import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class RbiMerchantTest extends BaseTest {

    @BeforeClass
    public void setup() throws IOException {
        super.setup();
        driver.get(baseUrl + "/rbi/merchant");
    }

    @Override
    public void testEndpoints() {
        // Нічого не робити, ми лише хочемо перевірити кінцеву точку /go/merchant
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit(); // Закриття всіх вікон браузера і завершення сеансу WebDriver
        }
    }

    @Test(groups = "rbi-merchant-tests")
    public void verifyPageTitle() {
        String expectedTitle = "Login Page";
        String actualTitle = driver.getTitle();
        assertEquals(actualTitle, expectedTitle); // Перевірка, що заголовок сторінки відповідає очікуваному
    }

    @Test(groups = "rbi-merchant-tests", dependsOnMethods = "verifyPageTitle")
    public void verifyLoginSuccess() {
        WebElement usernameInput = driver.findElement(By.name("j_username"));
        WebElement passwordInput = driver.findElement(By.name("j_password"));
        usernameInput.sendKeys(username);
        passwordInput.sendKeys(password);
        WebElement loginButton = driver.findElement(By.xpath("//*[@id='content']/form/table/tbody/tr[3]/td[2]/input"));
        loginButton.click();

        //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement welcomeMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/table[1]/tbody/tr/td[1]/table/tbody/tr[1]/td/a")));
        assertNotNull(welcomeMessage); // Перевірка, що повідомлення про вітання не є null

        System.out.println("Login successful! Welcome message found.");
    }

    @Test(groups = "rbi-merchant-tests", dependsOnMethods = "verifyLoginSuccess")
    public void verifyTabExistence() {
      //  WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement tab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbi/merchant/index.jsp']")));
        assertNotNull(tab); // Перевірка, що вкладка не є null
    }

    @Test(groups = "rbi-merchant-tests", dependsOnMethods = "verifyTabExistence")
    public void verifyTabNavigation() {
      //  WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/rbi/merchant/index.jsp']")));
        tab.click();

        WebElement activeTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbi/merchant/index.jsp' and contains(@class, 'menuSelected')]")));
        assertNotNull(activeTab); // Перевірка, що активна вкладка існує
    }

    @Test(groups = "rbi-merchant-tests", dependsOnMethods = "verifyTabNavigation")
    public void verifyProfilExistence() {
       // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement profileTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbi/merchant/do?action=profile']")));
        assertNotNull(profileTab); // Перевірка, що вкладка профілю наявна
    }

    @Test(groups = "rbi-merchant-tests", dependsOnMethods = "verifyProfilExistence")
    public void verifyProfilNavigation() {
        // Використання явного очікування для того, щоб елемент завантажився
      //  WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/rbi/merchant/do?action=profile']")));
        tab.click();

        // Використовуємо явне очікування, щоб дочекатися, коли вкладка стане активною
        WebElement activeTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbi/merchant/do?action=profile' and contains(@class, 'menuSelected')]")));
        assertNotNull(activeTab);

        // Використовуємо явне очікування для появи тексту "yarosh"
        WebElement userCell = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[text()='AutoTest']")));
        assertNotNull(userCell);

        // Виведення тексту в друк
        System.out.println("Profile text: " + userCell.getText());
    }

    @Test(groups = "rbi-merchant-tests", dependsOnMethods = "verifyProfilNavigation")
    public void verifyTerminalExistence() {
        // Використання явного очікування для того, щоб елемент завантажився
      //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement terminalTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbi/merchant/do?action=terminals']")));

        // Перевірка, що елемент не є null
        assertNotNull(terminalTab);
    }

    @Test(groups = "rbi-merchant-tests", dependsOnMethods = "verifyTerminalExistence")
    public void verifyTerminalNavigation() {
        // Використання явного очікування для того, щоб елемент завантажився
       // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/rbi/merchant/do?action=terminals']")));
        tab.click();

        // Використовуємо явне очікування, щоб дочекатися, коли вкладка стане активною
        WebElement activeTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbi/merchant/do?action=terminals' and contains(@class, 'menuSelected')]")));
        assertNotNull(activeTab);

        // Клік на вкладку "terminalGet" з використанням значення з propертів
        WebElement terminalGetTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='?action=terminalGet&merchantId=" + merchantId + "']")));
        terminalGetTab.click();

        // Використовуємо явне очікування для пошуку елемента таблиці
        WebElement labelDataRow = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[@class='labeldata']")));
        assertNotNull(labelDataRow);

        // Логування знайденого значення для підтвердження
        WebElement terminalIdLabel = driver.findElement(By.xpath("//tr[@class='labeldata']//td[@class='label' ]"));
        WebElement terminalIdValue = terminalIdLabel.findElement(By.xpath("following-sibling::td"));
        System.out.println("Terminal ID: " + terminalIdValue.getText());
    }

    @Test(groups = "rbi-merchant-tests", dependsOnMethods = "verifyTerminalNavigation")
    public void verifyTransExistence() {
        // Використання явного очікування для того, щоб елемент завантажився
       // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement transTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbi/merchant/do?action=trans']")));

        // Перевірка, що елемент не є null
        assertNotNull(transTab);

        // Клік на вкладку "trans"
        transTab.click();

        // Використовуємо явне очікування для наявності кнопки "Пошук"
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@class='button' and @type='submit' ]")));
        searchButton.click();

        // Використовуємо явне очікування для появи таблиці
        WebElement resultsTable = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr/th[text()='ID']")));
        assertNotNull(resultsTable);

        // Натискання на кнопку "Експорт"
        WebElement exportButton = driver.findElement(By.xpath("//input[@class='button' and @type='submit']"));
        exportButton.click();

        // Використовуємо метод для перевірки існування файлу report.txt
        checkFileDownloaded("report.txt");
    }

    @Test(groups = "rbi-merchant-tests", dependsOnMethods = "verifyTransExistence")
    public void verifyInvoicingExistence() {
        // Використання явного очікування для того, щоб елемент завантажився
       // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement invoicingTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbi/merchant/do?action=invoicing']")));

        // Перевірка, що елемент не є null
        assertNotNull(invoicingTab);
    }

    @Test(groups = "rbi-merchant-tests", dependsOnMethods = "verifyInvoicingExistence")
    public void verifyInvoicingNavigation() {
       // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Натискаємо на вкладку "Invoicing"
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/rbi/merchant/do?action=invoicing']")));
        tab.click();

        // Очікуємо, поки вкладка стане активною
        WebElement activeTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbi/merchant/do?action=invoicing' and contains(@class, 'menuSelected')]")));
        assertNotNull(activeTab);

        // Обираємо опцію "TestYVP2" у селекті "mid"
        WebElement selectMid = driver.findElement(By.xpath("//select[@name='mid' and @id='mid']"));
        WebElement optionMid = selectMid.findElement(By.xpath(".//option[@value='" + merchantId + "']"));
        optionMid.click();


        // Вводимо різні значення в поля
        WebElement oidInput = driver.findElement(By.xpath("//input[@name='oid' and @id='oid']"));
        oidInput.clear();

        // Натискаємо кнопку "Auto generate"
        WebElement generateButton = driver.findElement(By.xpath("//input[@id='generate' and @value='Auto generate']"));
        generateButton.click();

        WebElement amountInput = driver.findElement(By.xpath("//input[@name='amount' and @id='amount']"));
        amountInput.clear();
        amountInput.sendKeys("77.7");

        WebElement descriptionInput = driver.findElement(By.xpath("//input[@name='description' and @id='description']"));
        descriptionInput.clear();
        descriptionInput.sendKeys("RBIInvoicing");

        WebElement expDateSelect = driver.findElement(By.xpath("//select[@name='expDate']"));
        WebElement expDateOption = expDateSelect.findElement(By.xpath(".//option[@value='1']"));
        expDateOption.click();

        // Натискаємо кнопку "Створити"
        WebElement createButton = driver.findElement(By.xpath("//input[@type='submit' ]"));
        createButton.click();

        // Перевірка наявності спливаючого вікна з помилкою
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            System.out.println("Alert found: " + alertText);
            alert.accept();  // Закриваємо спливаюче вікно
            return;  // Завершуємо метод, якщо є помилка
        } catch (NoAlertPresentException e) {
            // Немає спливаючого вікна, продовжуємо тестування
        }

        Waiters.sleep(1000);
        // Знаходимо і запам'ятовуємо значення з <td> елемент, який містить текст "ID", та наступний <td> елемент
        WebElement orderIdTd = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(text(), 'ID')]/following-sibling::td")));
        Waiters.sleep(1000);
        String orderId = orderIdTd.getText();
        System.out.println("Order ID: " + orderId);


        // Повертаємося до вкладки "Invoicing"
        WebElement invoicingTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='menuUnselected' and @href='/rbi/merchant/do?action=invoicing']")));
        invoicingTab.click();

        // Натискаємо на "showForm"
        WebElement showFormSpan = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[@id='showForm' and @data-hidden='true']")));
        showFormSpan.click();

        // Вибираємо опцію "TestYVP2" у селекті "merchantId"
        WebElement merchantIdSelect = driver.findElement(By.xpath("//select[@name='merchantId']"));
        WebElement merchantIdOption = merchantIdSelect.findElement(By.xpath(".//option[@value='" + merchantId + "']"));
        merchantIdOption.click();


        // Вводимо orderId в поле orderId
        WebElement orderIdInput = driver.findElement(By.xpath("//input[@name='orderId']"));
        orderIdInput.clear();
        orderIdInput.sendKeys(orderId);

        // Натискаємо кнопку "Пошук"
        WebElement searchButton = driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td/div/form[2]/fieldset/table/tbody/tr[6]/td/input"));
        searchButton.click();

        // Перевіряємо наявність посилання з текстом orderId
        WebElement resultLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td/a[text()='" + orderId + "']")));
        assertNotNull(resultLink);

        // Виведення результату в консолі
        System.out.println("Invoicing: " + resultLink.getText() + " Ok");
    }

    private void checkFileDownloaded(String expectedFileName) {
        File file = new File(downloadFilepath + File.separator + expectedFileName);
        int attempts = 0;
        while (!file.exists() && attempts < 10) {
            try {
                Thread.sleep(1000);
                attempts++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        assertTrue(file.exists(), "Downloaded file '" + expectedFileName + "' does not exist.");//Перевірка існування завантаженого файлу
        System.out.println("File " + expectedFileName + " successfully downloaded.");
    }

}
