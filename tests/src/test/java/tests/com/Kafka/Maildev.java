package tests.com.Kafka;

import tests.com.Endpoint.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;
import java.time.Duration;


import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Maildev extends BaseTest {

    @Test
    public void emailSendTo() {
        // Отримати значення base.url з properties
        String baseUrl = properties.getProperty("URLtomee");
        if (baseUrl == null) {
            Assert.fail("base.url not found in properties");
        }

        // Визначаємо частину URL до знаку -tomee.
        String pipelineName = baseUrl.split("-tomee")[0].replace("https://", "").toLowerCase();
        System.out.println("Expected Pipeline Name: " + pipelineName);

        // Відкрити URL
        driver.get(
                String.format(
                    properties.getProperty("base.urlmaildev"),
                    System.getProperty("env", properties.getProperty("default_env"))
                )
        );

        // Очікування появи сторінки
       // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));

        // Спробувати знайти відповідний елемент
        try {
            WebElement pipelineElement = driver.findElement(By.xpath("//div[@class='dashboard__main-menu-item']//span[text()='" + pipelineName + "']/ancestor::div[contains(@class, 'dashboard__main-menu-item')]"));
            WebElement downChevronButton = pipelineElement.findElement(By.xpath(".//i[contains(@class, 'mdi-chevron-down')]"));

            // Натиснути на кнопку з класом mdi-chevron-down
            downChevronButton.click();
         //   System.out.println("Pipeline found and expand/collapse button clicked.");

            // Пауза для завантаження сторінки після натискання
            Thread.sleep(2000);

            // Знайти всі елементи li з класом submenu__item
            List<WebElement> emailItems = driver.findElements(By.xpath("//li[@class='submenu__item']"));

            int count = 0;
            LocalDate today = LocalDate.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (WebElement emailItem : emailItems) {
                WebElement toElement = emailItem.findElement(By.xpath(".//div[contains(text(), 'To: ')]"));
                WebElement createdAtElement = emailItem.findElement(By.xpath(".//div[contains(text(), 'Created at: ')]"));
                String toText = toElement.getText().trim();
                String createdAtText = createdAtElement.getText().trim();

                if (toText.contains("viacheslav.yarosh@upc.ua")) {
                    // Витягуємо дату з Created at елемента
                    String createdAtDateString = createdAtText.split("Created at: ")[1].split(" ")[0];
                    LocalDate createdAtDate = LocalDate.parse(createdAtDateString, dateFormatter);

                    if (createdAtDate.equals(today)) {
                        System.out.println(toText + " з датою: " + createdAtText);
                        count++;
                    } else {
                      //  System.out.println("To: viacheslav.yarosh@upc.ua з не поточною датою: " + createdAtText);
                    }
                }
            }

            System.out.println("Загальна кількість адресатів 'To: viacheslav.yarosh@upc.ua' з поточною датою: " + count);

            if (count == 0) {
                System.out.println("Вказаного адресата з поточною датою не знайдено");
                Assert.fail("Вказаного адресата з поточною датою не знайдено");
            } else {
                System.out.println("Тест пройдено, адресата з поточною датою знайдено.");
            }

        } catch (Exception e) {
            System.out.println("Pipeline not found.");
            Assert.fail("Не знайдено відповідний пейплайн.");
        }
    }
    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}