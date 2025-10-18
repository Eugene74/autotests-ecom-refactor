package com.ecom.ui.dashboard.functional.pages;

import com.ecom.ui.util.Waiters;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.ecom.ui.common.BasePage;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;



public class KafkaEmailsPage extends BasePage {

    public KafkaEmailsPage(WebDriver driver) {
        super(driver);
    }

    @CacheLookup
    @FindBy(xpath = "//span[contains(normalize-space(.), 'Text')]")
    protected WebElement display_mode_select;

    @CacheLookup
    @FindBy(xpath = "//div[contains(normalize-space(.), 'Source') and @class='v-list-item-title']")
    protected WebElement display_mode_source_option;

    public KafkaEmailsPage openEmail(String toEmail, String url, String pipelineName)  {

        // Відкрити URL
        getDriver().get(url);

        // Очікування появи сторінки
        // WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(20));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));

        // Спробувати знайти відповідний елемент
        try {
            WebElement pipelineElement = getDriver().findElement(By.xpath("//div[@class='dashboard__main-menu-item']//span[text()='" + pipelineName + "']/ancestor::div[contains(@class, 'dashboard__main-menu-item')]"));
            WebElement downChevronButton = pipelineElement.findElement(By.xpath(".//i[contains(@class, 'mdi-chevron-down')]"));

            // Натиснути на кнопку з класом mdi-chevron-down
            downChevronButton.click();
            //   System.out.println("Pipeline found and expand/collapse button clicked.");

            // Пауза для завантаження сторінки після натискання
            Thread.sleep(2000);

            LocalDate today = LocalDate.now();
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            List<WebElement> emailItems = getDriver().findElements(By.xpath("//li[@class='submenu__item']"));

            for (WebElement emailItem : emailItems) {
                WebElement toElement = emailItem.findElement(By.xpath(".//div[contains(text(), 'To: ')]"));
                WebElement createdAtElement = emailItem.findElement(By.xpath(".//div[contains(text(), 'Created at: ')]"));
                String toText = toElement.getText().trim();
                String createdAtText = createdAtElement.getText().trim();

                if (toText.contains(toEmail)) {
                    // Витягуємо дату з Created at елемента
                    String createdAtDateString = createdAtText.split("Created at: ")[1].split(" ")[0];
                    LocalDate createdAtDate = LocalDate.parse(createdAtDateString, dateFormatter);

                    if (createdAtDate.equals(today)) {
                        System.out.println(toText + " з датою: " + createdAtText);
                        toElement.click();
                    } else {
                        //  System.out.println("To: viacheslav.yarosh@upc.ua з не поточною датою: " + createdAtText);
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Pipeline not found.");
            Assert.fail("Не знайдено відповідний пейплайн.");
        }
        return this;
    }

    public String get_source_text(){
        Waiters.appearElement(getDriver(), display_mode_select);
        display_mode_select.click();

        Waiters.visibleAndClicableElement(getDriver(), display_mode_source_option);
        display_mode_source_option.click();
        Waiters.sleep(2000);
        getDriver().switchTo().frame(0);
        WebElement body = getDriver().findElement(By.tagName("body"));
        return body.getText();
    }
}
