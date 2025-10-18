package tests.com.Endpoint;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class LoginPageTest extends BaseTest {
    @Test(groups = "login-tests")
    public void verifyLoginPageTitle() {
        // Verify the login page title
        String expectedTitle = "Login Page";
        String actualTitle = driver.getTitle();
        assertEquals(actualTitle, expectedTitle);
    }

    @Test(groups = "login-tests")
    public void verifyLoginFieldsExist() {
        // Verify that the login fields exist
        WebElement usernameField = driver.findElement(By.name("j_username"));
        WebElement passwordField = driver.findElement(By.name("j_password"));
        assertNotNull(usernameField);
        assertNotNull(passwordField);
    }
}