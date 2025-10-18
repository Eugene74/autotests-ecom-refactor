package tests.dashboard.functional.tests.Login;

import methods.DashboardRequest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.BaseRedirect;

import java.time.Duration;

public class LoginUnsuccess extends BaseRedirect {

    @Test
    public void loginDashboard() {
        DashboardRequest request = new DashboardRequest();
        request.LogInUnsuccess();

        // Отримання WebDriver через BaseRedirect
        WebDriver driver = getDriver();

        WebElement modalMessage = new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".v-snackbar__content")));
        // Знайдіть елемент модального вікна з повідомленням
       /* WebElement modalMessage = driver.findElement(By.cssSelector(".v-snackbar__content"));*/
        String actualMessage = modalMessage.getText();

        // Перевірте, що повідомлення відповідає очікуваному тексту
        String expectedMessage = "Incorrect username or password";
        Assert.assertEquals(actualMessage, expectedMessage);
    }
}
