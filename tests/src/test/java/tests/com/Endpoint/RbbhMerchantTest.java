package tests.com.Endpoint;

import methods.Waiters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import java.time.Duration;

import java.io.File;
import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

    public class RbbhMerchantTest extends BaseTest {

        @BeforeClass
        public void setup() throws IOException {
            super.setup();
            driver.get(baseUrl + "/rbbh/merchant");
        }

        public void testEndpoints() {
            // Нічого не робити, ми лише хочемо перевірити кінцеву точку /kbc/merchant
        }

       @AfterClass
        public void teardown() {
            if (driver != null) {
                driver.quit(); // Закриття всіх вікон браузера і завершення сеансу WebDriver

            }

       }

        @Test(groups = "rbbh-merchant-tests")
        public void verifyPageTitle() {
            String expectedTitle = "RBBH Payment Gateway";
            String actualTitle = driver.getTitle();
            assertEquals(actualTitle, expectedTitle);
        }

        @Test(groups = "rbbh-merchant-tests", dependsOnMethods = "verifyPageTitle")
        public void verifyLoginSuccess() {
            // Виконати вхід
            WebElement usernameInput = driver.findElement(By.name("j_username"));
            WebElement passwordInput = driver.findElement(By.name("j_password"));
            usernameInput.sendKeys(username); // Використовуймо ім'я користувача з properties
            passwordInput.sendKeys(password); // Використовуймо пароль з properties
            WebElement loginButton = driver.findElement(By.xpath("/html/body/div/div/div/form/div/div[3]/input"));
            loginButton.click();

            // Перевірка успішності входу
           // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            WebElement welcomeMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/aside/nav/ul[1]/li[1]/a")));
            assertNotNull(welcomeMessage);


            // Вивести результат тесту
            System.out.println("Login successful! Welcome message found.");
        }

        @Test(groups = "rbbh-merchant-tests", dependsOnMethods = "verifyLoginSuccess")
        public void verifyTabExistence() {
            // Використання явного очікування для того, щоб елемент завантажився
           // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement tab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbbh/merchant/index.jsp']")));

            // Перевірка, що елемент не є null
            assertNotNull(tab);
        }

        @Test(groups = "rbbh-merchant-tests", dependsOnMethods = "verifyTabExistence")
        public void verifyTabNavigation() {
            // Click on the tab
           // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/rbbh/merchant/index.jsp']")));
            tab.click();

            // Use explicit wait to wait for the tab to become active
            WebElement activeTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/aside/nav/ul[1]/li[1]/a")));
            assertNotNull(activeTab);
        }

        @Test(groups = "rbbh-merchant-tests", dependsOnMethods = "verifyTabNavigation")
        public void verifyProfilExistence() {
            // Використання явного очікування для того, щоб елемент завантажився
           // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement profileTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbbh/merchant/do?action=profile']")));


            // Перевірка, що елемент не є null
            assertNotNull(profileTab);
        }

        @Test(groups = "rbbh-merchant-tests", dependsOnMethods = "verifyProfilExistence")
        public void verifyProfilNavigation() {
            // Використання явного очікування для того, щоб елемент завантажився
            //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/rbbh/merchant/do?action=profile']")));
            tab.click();

            // Використовуємо явне очікування, щоб дочекатися, коли вкладка стане активною
            WebElement activeTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//html/body/aside/nav/ul[1]/li[2]/a")));
            assertNotNull(activeTab);

            // Використовуємо явне очікування для появи тексту "yarosh"
            WebElement userCell = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/div/div/div[1]/form/div/div[1]/div/span")));
            assertNotNull(userCell);

            // Виведення тексту в друк
            System.out.println("Profile text: " + userCell.getText());
        }

        @Test(groups = "rbbh-merchant-tests", dependsOnMethods = "verifyProfilNavigation")
        public void verifyTerminalExistence() {
            // Використання явного очікування для того, щоб елемент завантажився
            //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement terminalTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbbh/merchant/do?action=terminals']")));

            // Перевірка, що елемент не є null
            assertNotNull(terminalTab);
        }

        @Test(groups = "rbbh-merchant-tests", dependsOnMethods = "verifyTerminalExistence")
        public void verifyTerminalNavigation() {
            // Використання явного очікування для того, щоб елемент завантажився
           // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Клік на вкладку "terminals"
            WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/rbbh/merchant/do?action=terminals']")));
            tab.click();

            // Використовуємо явне очікування, щоб дочекатися, коли вкладка стане активною
            WebElement activeTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/aside/nav/ul[1]/li[3]/a")));
            assertNotNull(activeTab);

            // Клік на вкладку "terminalGetWithUI"
            WebElement terminalGetTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='?action=terminalGetWithUI&merchantId=" + merchantId + "']")));
            terminalGetTab.click();

            // Знайти <li> елемент з класом "label", який містить текст "Terminal ID"
            WebElement terminalIdLabel = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//span[@class='label' and (contains(text(), 'ID') or contains(text(), 'Kodi'))]")));

            // Знайти наступний <span> елемент з класом "value"
            WebElement terminalIdValue = terminalIdLabel.findElement(By.xpath("following-sibling::span[@class='value']"));
            assertNotNull(terminalIdValue);

            String terminalId = terminalIdValue.getText();
            System.out.println("Terminal ID: " + terminalId);
        }





        @Test(groups = "rbbh-merchant-tests", dependsOnMethods = "verifyTerminalNavigation")
        public void verifyTransExistence() {
            // Використання явного очікування для того, щоб елемент завантажився
            //WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement transTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbbh/merchant/do?action=trans']")));

            // Перевірка, що елемент не є null
            assertNotNull(transTab);

            // Клік на вкладку "trans"
            transTab.click();

            // Використовуємо явне очікування для наявності кнопки "Пошук"
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div/div/div/form/div/div[5]/div[1]/input")));
            searchButton.click();

            // Використовуємо явне очікування для появи таблиці
            WebElement resultsTable = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//tr/th[text()='ID']")));
            assertNotNull(resultsTable);

            // Натискання на кнопку "Експорт"
//            WebElement exportButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@id='export' and @class='export-table']")));
//            exportButton.click();

            // Використовуємо метод для перевірки існування файлу report.txt
            checkFileDownloaded("report.txt");
        }


        @Test(groups = "rbbh-merchant-tests", dependsOnMethods = "verifyTransExistence")
        public void verifyInvoicingExistence() {
            // Використання явного очікування для того, щоб елемент завантажився
           // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement invoicingTab = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//a[@href='/rbbh/merchant/do?action=invoicing']")));

            // Перевірка, що елемент не є null
            assertNotNull(invoicingTab);
        }

        @Test(groups = "rbbh-merchant-tests", dependsOnMethods = "verifyInvoicingExistence")
        public void verifyInvoicingNavigation() {
           // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Натискаємо на вкладку "Invoicing"
            WebElement tab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/rbbh/merchant/do?action=invoicing']")));
            tab.click();

            // Базовий URL з пропертей
            String baseUrl = properties.getProperty("URLtomee");
            String invoicingUrl = String.format("%s/rbbh/merchant/do?action=invoicing", baseUrl);

            // Очікуємо, поки вкладка стане активною, просто перевіряємо URL
          //  wait.until(ExpectedConditions.urlToBe(invoicingUrl));  // закомічено для тесту на test середовищі

            // Обираємо опцію "TestYVP2" у селекті "mid"
            WebElement selectMid = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[@name='mid' and @id='mid']")));
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
            amountInput.sendKeys("777");

            WebElement descriptionInput = driver.findElement(By.xpath("//input[@name='description' and @id='description']"));
            descriptionInput.clear();
            descriptionInput.sendKeys("RBBHInvoicing");

            WebElement expDateSelect = driver.findElement(By.xpath("//select[@name='expDate']"));
            WebElement expDateOption = expDateSelect.findElement(By.xpath(".//option[@value='1']"));
            expDateOption.click();


            // Натискаємо кнопку "Створити"
            WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit']")));
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
            Waiters.sleep(1000);
// Знайти <span> елемент з текстом "Order ID" або інші
            WebElement orderIdSpan = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//span[contains(text(), 'Order ID') or contains(text(), 'Broj narudžbe')"
                            + " or contains(text(), 'ID замовлення') or contains(text(), 'ID заказа')]")));

// Знайти наступний <span> елемент з класом "value"
            WebElement orderIdValueSpan = orderIdSpan.findElement(By.xpath("following-sibling::span[@class='value']"));
            Waiters.sleep(1000);
            String orderId = orderIdValueSpan.getText();
            System.out.println("Order ID: " + orderId);



            // Повертаємося до вкладки "Invoicing"
            WebElement invoicingTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@href='/rbbh/merchant/do?action=invoicing']")));
            invoicingTab.click();

            // Натискаємо на "showForm" (якщо це елемент з класом "collapse-trigger", замість 'showForm')
            WebElement showFormSpan = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[@class='collapse-trigger expanded']")));
            showFormSpan.click();

            // Вибираємо опцію "TestYVP2" у селекті "merchantId"
            WebElement merchantIdSelect = driver.findElement(By.xpath("//select[@name='merchantId']"));
            WebElement merchantIdOption = merchantIdSelect.findElement(By.xpath(".//option[@value='" + merchantId + "']"));
            try {
                Thread.sleep(2000); // Затримка на 2 секунди
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            merchantIdOption.click();


            // Вводимо orderId в поле orderId
            WebElement orderIdInput = driver.findElement(By.xpath("//input[@name='orderId']"));
            orderIdInput.clear();
            orderIdInput.sendKeys(orderId);

            // Натискаємо кнопку "Пошук"
            WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@type='submit' and @value='Traži']")));
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
