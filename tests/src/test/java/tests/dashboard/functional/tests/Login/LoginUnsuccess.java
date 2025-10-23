package tests.dashboard.functional.tests.Login;

import com.ecom.tests.base.BaseUiTest;
import com.ecom.tests.support.DashboardRequest;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginUnsuccess extends BaseUiTest {

  @Test
  public void loginDashboard() {
    DashboardRequest request = new DashboardRequest();
    request.LogInUnsuccess();

    // Отримання WebDriver через BaseRedirect
    WebDriver driver = getDriver();

    WebElement modalMessage =
        new WebDriverWait(driver, Duration.ofSeconds(10))
            .until(
                ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".v-snackbar__content")));
    // Знайдіть елемент модального вікна з повідомленням
    /* WebElement modalMessage = driver.findElement(By.cssSelector(".v-snackbar__content"));*/
    String actualMessage = modalMessage.getText();

    // Перевірте, що повідомлення відповідає очікуваному тексту
    String expectedMessage = "Incorrect username or password";
    Assert.assertEquals(actualMessage, expectedMessage);
  }
}
