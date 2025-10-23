package com.ecom.ui.dashboard.functional.pages;

import com.ecom.ui.common.BasePage;
import com.ecom.ui.util.Waiters;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage extends BasePage {

  @CacheLookup
  @FindBy(name = "username")
  protected WebElement username;

  @CacheLookup
  @FindBy(name = "password")
  protected WebElement pass;

  @CacheLookup
  @FindBy(css = "button.v-btn--block.v-btn--elevated.mb-4")
  protected WebElement btn;

  @CacheLookup
  @FindBy(xpath = "/html/body/div/div/div/header/div/button")
  protected WebElement language_Button;

  public LoginPage(WebDriver driver) {
    super(driver);
  }

  public String logIn(String url, String login, String password) {
    getDriver().get(url);

    // Вибір мови
    WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));
    Waiters.appearElement(getDriver(), language_Button);
    language_Button.click();

    // Зачекайте, поки з'явиться випадаюче меню, і виберіть мову "English"
    WebElement englishOption =
        wait.until(
            ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='v-list-item-title' and text()='English']")));
    ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click();", englishOption);
    Waiters.sleep(2000);

    // Введення логіну та пароля
    username.click();
    username.sendKeys(login);
    pass.click();
    pass.sendKeys(password);
    Waiters.sleep(1000);
    btn.click();
    Waiters.sleep(1500);

    return getDriver().getCurrentUrl().toString();
  }
}
