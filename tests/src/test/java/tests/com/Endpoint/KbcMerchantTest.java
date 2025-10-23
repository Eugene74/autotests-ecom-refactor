package tests.com.Endpoint;

import static com.ecom.core.config.EnvData.LOGIN;
import static com.ecom.core.config.EnvData.PASSWORD;
import static com.ecom.core.config.EnvData.URL_TOMEE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

import com.ecom.tests.base.EndpointBaseTest;
import com.ecom.ui.util.Waiters;
import java.io.File;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class KbcMerchantTest extends EndpointBaseTest {

  @BeforeClass(alwaysRun = true)
  public void openMerchantPage() {
    driver.get(URL_TOMEE + "/kbc/merchant");
  }

  @Test(groups = "go-merchant-tests")
  public void verifyPageTitle() {
    String expectedTitle = "Login Page";
    String actualTitle = driver.getTitle();
    assertEquals(actualTitle, expectedTitle);
  }

  @Test(groups = "go-merchant-tests", dependsOnMethods = "verifyPageTitle")
  public void verifyLoginSuccess() {
    // Виконати вхід
    WebElement usernameInput = driver.findElement(By.name("j_username"));
    WebElement passwordInput = driver.findElement(By.name("j_password"));
    usernameInput.sendKeys(LOGIN); // Використовуймо ім'я користувача з properties
    passwordInput.sendKeys(PASSWORD); // Використовуймо пароль з properties
    WebElement loginButton =
        driver.findElement(
            By.xpath("/html/body/div[3]/div[2]/form/table/tbody/tr[3]/td[2]/button"));
    loginButton.click();

    // Перевірка успішності входу
    // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    WebElement welcomeMessage =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("/html/body/table[1]/tbody/tr/td[1]/table/tbody/tr[1]/td/a")));
    assertNotNull(welcomeMessage);

    // Вивести результат тесту
    System.out.println("Login successful! Welcome message found.");
  }

  @Test(groups = "go-merchant-tests", dependsOnMethods = "verifyLoginSuccess")
  public void verifyTabExistence() {
    // Використання явного очікування для того, щоб елемент завантажився
    // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement tab =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[@href='/kbc/merchant/index.jsp']")));

    // Перевірка, що елемент не є null
    assertNotNull(tab);
  }

  @Test(groups = "go-merchant-tests", dependsOnMethods = "verifyTabExistence")
  public void verifyTabNavigation() {
    // Click on the tab
    // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement tab =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='/kbc/merchant/index.jsp']")));
    tab.click();

    // Use explicit wait to wait for the tab to become active
    WebElement activeTab =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath(
                    "//a[@href='/kbc/merchant/index.jsp' and contains(@class, 'menuSelected')]")));
    assertNotNull(activeTab);
  }

  @Test(groups = "go-merchant-tests", dependsOnMethods = "verifyTabNavigation")
  public void verifyProfilExistence() {
    // Використання явного очікування для того, щоб елемент завантажився
    // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement profileTab =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[@href='/kbc/merchant/do?action=profile']")));
    assertNotNull(profileTab);
  }

  @Test(groups = "go-merchant-tests", dependsOnMethods = "verifyProfilExistence")
  public void verifyProfilNavigation() {
    // Використання явного очікування для того, щоб елемент завантажився
    // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement tab =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='/kbc/merchant/do?action=profile']")));
    tab.click();

    // Використовуємо явне очікування, щоб дочекатися, коли вкладка стане активною
    WebElement activeTab =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath(
                    "//a[@href='/kbc/merchant/do?action=profile' and contains(@class, 'menuSelected')]")));
    assertNotNull(activeTab);

    // Використовуємо явне очікування для появи тексту "yarosh"
    WebElement userCell =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(By.xpath("//td[text()='AutoTest']")));
    assertNotNull(userCell);

    // Виведення тексту в друк
    System.out.println("Profile text: " + userCell.getText());
  }

  @Test(groups = "go-merchant-tests", dependsOnMethods = "verifyProfilNavigation")
  public void verifyTerminalExistence() {
    // Використання явного очікування для того, щоб елемент завантажився
    // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement terminalTab =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[@href='/kbc/merchant/do?action=terminals']")));
    assertNotNull(terminalTab);
  }

  @Test(groups = "go-merchant-tests", dependsOnMethods = "verifyTerminalExistence")
  public void verifyTerminalNavigation() {
    // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement tab =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='/kbc/merchant/do?action=terminals']")));
    tab.click();
    WebElement activeTab =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath(
                    "//a[@href='/kbc/merchant/do?action=terminals' and contains(@class, 'menuSelected')]")));
    assertNotNull(activeTab);

    // Клік на вкладку "terminalGet"
    WebElement terminalGetTab =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='?action=terminalGet&merchantId=" + merchantId + "']")));
    terminalGetTab.click();

    WebElement labelDataRow =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(By.xpath("//tr[@class='labeldata']")));
    assertNotNull(labelDataRow);

    WebElement terminalIdLabel =
        driver.findElement(By.xpath("//tr[@class='labeldata']//td[@class='label']"));
    WebElement terminalIdValue = terminalIdLabel.findElement(By.xpath("following-sibling::td"));
    System.out.println("Terminal ID: " + terminalIdValue.getText());
  }

  @Test(groups = "go-merchant-tests", dependsOnMethods = "verifyTerminalNavigation")
  public void verifyTransExistence() {
    // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement transTab =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[@href='/kbc/merchant/do?action=trans']")));
    assertNotNull(transTab);
    transTab.click();

    WebElement searchButton =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//input[@class='button' and @type='submit']")));
    searchButton.click();

    // Використання оновленого XPath для пошуку заголовків, які містять "ID", "Identifikacija" або
    // "Номер на транзакция"
    WebElement resultsTable =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath(
                    "//tr/th[contains(text(), 'ID') or contains(text(), 'Identifikacija') or contains(text(), 'Номер на транзакция')]")));
    assertNotNull(resultsTable);

    WebElement exportButton =
        driver.findElement(By.xpath("//input[@class='button' and @type='submit']"));
    exportButton.click();

    checkFileDownloaded("report.txt");
  }

  @Test(groups = "go-merchant-tests", dependsOnMethods = "verifyTransExistence")
  public void verifyInvoicingExistence() {
    // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement invoicingTab =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("//a[@href='/kbc/merchant/do?action=invoicing']")));
    assertNotNull(invoicingTab);
  }

  @Test(groups = "go-merchant-tests", dependsOnMethods = "verifyInvoicingExistence")
  public void verifyInvoicingNavigation() {
    //  WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    WebElement tab =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//a[@href='/kbc/merchant/do?action=invoicing']")));
    tab.click();
    WebElement activeTab =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath(
                    "//a[@href='/kbc/merchant/do?action=invoicing' and contains(@class, 'menuSelected')]")));
    assertNotNull(activeTab);

    WebElement selectMid = driver.findElement(By.xpath("//select[@name='mid' and @id='mid']"));
    WebElement optionMid =
        selectMid.findElement(By.xpath(".//option[@value='" + merchantId + "']"));
    optionMid.click();

    WebElement oidInput = driver.findElement(By.xpath("//input[@name='oid' and @id='oid']"));
    oidInput.clear();

    WebElement generateButton =
        driver.findElement(By.xpath("//input[@id='generate' and @value='Auto generate']"));
    generateButton.click();

    WebElement amountInput =
        driver.findElement(By.xpath("//input[@name='amount' and @id='amount']"));
    amountInput.clear();
    amountInput.sendKeys("777");

    WebElement descriptionInput =
        driver.findElement(By.xpath("//input[@name='description' and @id='description']"));
    descriptionInput.clear();
    descriptionInput.sendKeys("KBCInvoicing");

    WebElement expDateSelect = driver.findElement(By.xpath("//select[@name='expDate']"));
    WebElement expDateOption = expDateSelect.findElement(By.xpath(".//option[@value='1']"));
    expDateOption.click();

    WebElement createButton = driver.findElement(By.xpath("//input[@type='submit']"));
    createButton.click();
    Waiters.sleep(1000);
    // Знайти значення з комірки після натискання кнопки "Створити"
    WebElement orderIdTd =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath(
                    "//td[contains(text(), 'ID') or contains(text(), 'ID naloga') or contains(text(), 'Номер на поръчка') or contains(text(), 'Order ID')]/following-sibling::td")));
    Waiters.sleep(1000);
    String orderId = orderIdTd.getText();
    System.out.println("Order ID: " + orderId);

    // Повертаємося до вкладки "Invoicing"
    WebElement invoicingTab =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath(
                    "//a[@class='menuUnselected' and @href='/kbc/merchant/do?action=invoicing']")));
    invoicingTab.click();

    // Натискаємо на "showForm"
    WebElement showFormSpan =
        wait.until(
            ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@id='showForm' and @data-hidden='true']")));
    showFormSpan.click();

    // Вибираємо опцію "TestYVP2" у селекті "merchantId"
    WebElement merchantIdSelect = driver.findElement(By.xpath("//select[@name='merchantId']"));
    WebElement merchantIdOption =
        merchantIdSelect.findElement(By.xpath(".//option[@value='" + merchantId + "']"));
    merchantIdOption.click();

    // Вводимо orderId в поле orderId
    WebElement orderIdInput = driver.findElement(By.xpath("//input[@name='orderId']"));
    orderIdInput.clear();
    orderIdInput.sendKeys(orderId);

    // Натискаємо кнопку "Пошук"
    WebElement searchButton =
        driver.findElement(
            By.xpath(
                "/html/body/table[2]/tbody/tr/td/div/form[2]/fieldset/table/tbody/tr[6]/td/input"));
    searchButton.click();

    // Перевіряємо наявність посилання з текстом orderId
    WebElement resultLink =
        wait.until(
            ExpectedConditions.presenceOfElementLocated(
                By.xpath("//td/a[text()='" + orderId + "']")));
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

    assertTrue(file.exists(), "Downloaded file '" + expectedFileName + "' does not exist.");
    System.out.println("File " + expectedFileName + " successfully downloaded.");
  }
}
