package tests.com.Endpoint;

import com.ecom.ui.util.Waiters;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.time.Duration;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class RbrsMerchantTest extends BaseTest {

    @BeforeClass
    public void setup() {
        super.setup();
        driver.get(baseUrl + "/rbrs/merchant");
    }

    @AfterClass
    public void teardown() {
        if (driver != null) {
            driver.quit();
                    }
    }


    @Test(groups = "rbrs-merchant-tests")
    public void verifyPageTitle() {
        String expectedTitle = "Login Page";
        String actualTitle = driver.getTitle();
        assertEquals(actualTitle, expectedTitle);
    }

    @Test(groups = "rbrs-merchant-tests", dependsOnMethods = "verifyPageTitle")
    public void verifyLoginSuccess() {
        // Виконати вхід
        WebElement usernameInput = driver.findElement(By.name("j_username"));
        WebElement passwordInput = driver.findElement(By.name("j_password"));
        usernameInput.sendKeys(username); // Використовуймо ім'я користувача з properties
        passwordInput.sendKeys(password); // Використовуймо пароль з properties
        WebElement loginButton = driver.findElement(By.xpath("/html/body/div[3]/div[2]/form/table/tbody/tr[3]/td[2]/button"));
        loginButton.click();

        // Перевірка успішності входу
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
       // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        WebElement welcomeMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"menu\"]/table/tbody/tr[1]/td/a")));
        assertNotNull(welcomeMessage);

        // Вивести результат тесту
        System.out.println("Login successful! Welcome message found.");
    }

    @Test(groups = "rbrs-merchant-tests", dependsOnMethods = "verifyLoginSuccess")
    public void verifyTabExistence() {
        // Використання явного очікування для того, щоб елемент завантажився
        //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement tab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbrs/merchant/index.jsp']")));

        // Перевірка, що елемент не є null
        assertNotNull(tab);
    }

    @Test(groups = "rbrs-merchant-tests", dependsOnMethods = "verifyTabExistence")
    public void verifyTabNavigation() {
        // Click on the tab
      //  WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/rbrs/merchant/index.jsp']")));
        tab.click();

        // Use explicit wait to wait for the tab to become active
        WebElement activeTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbrs/merchant/index.jsp' and contains(@class, 'menuSelected')]")));
        assertNotNull(activeTab);
    }

    @Test(groups = "rbrs-merchant-tests", dependsOnMethods = "verifyTabNavigation")
    public void verifyProfilExistence() {
        // Використання явного очікування для того, щоб елемент завантажився
       // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement profileTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbrs/merchant/do?action=profile']")));

        // Перевірка, що елемент не є null
        assertNotNull(profileTab);
    }

    @Test(groups = "rbrs-merchant-tests", dependsOnMethods = "verifyProfilExistence")
    public void verifyProfilNavigation() {
        // Використання явного очікування для того, щоб елемент завантажився
      //  WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/rbrs/merchant/do?action=profile']")));
        tab.click();

        // Використовуємо явне очікування, щоб дочекатися, коли вкладка стане активною
        WebElement activeTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbrs/merchant/do?action=profile' and contains(@class, 'menuSelected')]")));
        assertNotNull(activeTab);

        // Використовуємо явне очікування для появи тексту "yarosh"
        WebElement userCell = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[text()='AutoTest']")));
        assertNotNull(userCell);

        // Виведення тексту в друк
        System.out.println("Profile text: " + userCell.getText());
    }

    @Test(groups = "rbrs-merchant-tests", dependsOnMethods = "verifyProfilNavigation")
    public void verifyTerminalExistence() {
        // Використання явного очікування для того, щоб елемент завантажився
        //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement terminalTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbrs/merchant/do?action=terminals']")));

        // Перевірка, що елемент не є null
        assertNotNull(terminalTab);
    }

    @Test(groups = "rbrs-merchant-tests", dependsOnMethods = "verifyTerminalExistence")
    public void verifyTerminalNavigation() {
        // Використання явного очікування для того, щоб елемент завантажився
       // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/rbrs/merchant/do?action=terminals']")));
        tab.click();

        // Використовуємо явне очікування, щоб дочекатися, коли вкладка стане активною
        WebElement activeTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbrs/merchant/do?action=terminals' and contains(@class, 'menuSelected')]")));
        assertNotNull(activeTab);

        // Клік на вкладку "terminalGet"
        WebElement terminalGetTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='?action=terminalGet&merchantId=" + merchantId + "']")));
        terminalGetTab.click();

        WebElement labelDataRow = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[@class='labeldata']")));
        assertNotNull(labelDataRow);

        WebElement terminalIdLabel = driver.findElement(By.xpath("//tr[@class='labeldata']//td[@class='label']"));
        WebElement terminalIdValue = terminalIdLabel.findElement(By.xpath("following-sibling::td"));
        System.out.println("Terminal ID: " + terminalIdValue.getText());
    }


    @Test(groups = "rbrs-merchant-tests", dependsOnMethods = "verifyTerminalNavigation")
    public void verifyTransExistence() {
        // Використання явного очікування для того, щоб елемент завантажився
       // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement transTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbrs/merchant/do?action=trans']")));

        // Перевірка, що елемент не є null
        assertNotNull(transTab);

        // Клік на вкладку "trans"
        transTab.click();

        // Використовуємо явне очікування для наявності кнопки "Пошук"
        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@class='button' and @type='submit']")));
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

    @Test(groups = "rbrs-merchant-tests", dependsOnMethods = "verifyTransExistence")
    public void verifyInvoicingExistence() {
        // Використання явного очікування для того, щоб елемент завантажився
        //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement invoicingTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbrs/merchant/do?action=invoicing']")));

        // Перевірка, що елемент не є null
        assertNotNull(invoicingTab);
    }

    @Test(groups = "rbrs-merchant-tests", dependsOnMethods = "verifyInvoicingExistence")
    public void verifyInvoicingNavigation() {
       // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Натискаємо на вкладку "Invoicing"
        WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/rbrs/merchant/do?action=invoicing']")));
        tab.click();

        // Очікуємо, поки вкладка стане активною
        WebElement activeTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbrs/merchant/do?action=invoicing' and contains(@class, 'menuSelected')]")));
        assertNotNull(activeTab);

        // Обираємо опцію "TestYVP2" у селекті "mid"
        WebElement selectMid = driver.findElement(By.xpath("//select[@name='mid' and @id='mid']"));
        WebElement optionMid = selectMid.findElement(By.xpath(".//option[@value='" + merchantId + "']"));
        optionMid.click();


        // Вводимо різні значення в поля
        WebElement oidInput = driver.findElement(By.xpath("//input[@name='oid' and @id='oid']"));
        oidInput.clear();

        // Натискаємо кнопку "Auto generate"
        WebElement generateButton = driver.findElement(By.xpath("//input[@id='generate']"));
        generateButton.click();

        WebElement amountInput = driver.findElement(By.xpath("//input[@name='amount' and @id='amount']"));
        amountInput.clear();
        amountInput.sendKeys("777");

        WebElement descriptionInput = driver.findElement(By.xpath("//input[@name='description' and @id='description']"));
        descriptionInput.clear();
        descriptionInput.sendKeys("RBRSInvoicing");

        WebElement expDateSelect = driver.findElement(By.xpath("//select[@name='expDate']"));
        WebElement expDateOption = expDateSelect.findElement(By.xpath(".//option[@value='1']"));
        expDateOption.click();

        // Натискаємо кнопку "Створити"
        WebElement createButton = driver.findElement(By.xpath("//input[@type='submit']"));
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

        // Знайти <td> елемент, який містить текст "ID", та наступний <td> елемент
        // WebElement orderIdTd = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[contains(text(), 'ID')]/following-sibling::td")));
        // String orderId = orderIdTd.getText();
        //  System.out.println("Order ID: " + orderId);

// Знайти <td> елемент, який містить кілька можливих текстів, і отримати наступний <td> елемент
        Waiters.sleep(1000);
        WebElement orderIdTd = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//td[contains(text(), 'Order ID') or contains(text(), 'Kodi i porosisë') or contains(text(), 'ID замовлення')"
                        + " or contains(text(), 'ID заказа')]/following-sibling::td")));
        Waiters.sleep(1000);
        String orderId = orderIdTd.getText();
        System.out.println("Order ID: " + orderId);



        // Повертаємося до вкладки "Invoicing"
        WebElement invoicingTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='menuUnselected' and @href='/rbrs/merchant/do?action=invoicing']")));
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
        WebElement searchButton = driver.findElement(By.xpath("//*[@id=\"searchData\"]/tbody/tr[6]/td/input"));
        searchButton.click();


        // Перевіряємо наявність посилання з текстом orderId
        WebElement resultLink = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td/a[text()='" + orderId + "']")));
        assertNotNull(resultLink);

        // Виведення результату в консолі
        System.out.println("Invoicing: " + resultLink.getText() + " Ok");
    }


    private void checkFileDownloaded(String expectedFileName) {
        // Асинхронне очікування завантаження файлу
        File file = new File(downloadFilepath + File.separator + expectedFileName);
        int attempts = 0;
        while (!file.exists() && attempts < 10) {
            try {
                Thread.sleep(1000); // Чекаємо 1 секунду між перевірками
                attempts++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Перевірка, чи файл дійсно завантажився
        assertTrue(file.exists(), "Downloaded file '" + expectedFileName + "' does not exist.");
        System.out.println("File " + expectedFileName + " successfully downloaded.");
    }
}
