package com.ecom.ui.util;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

public class Waiters {

    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(25);

    public static void handleAlert(WebDriver driver) {
        try {
            while (driver.switchTo().alert() != null) {
                Alert alert = driver.switchTo().alert();
                alert.getText();
                alert.accept();
            }
        } catch (Exception e) {
            e.getCause();
        }
    }

    static boolean isPresentForWait(WebElement element) {
        try {
            element.isDisplayed();
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static void appearElement(WebDriver driver, final WebElement element) {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(30)).ignoring(NoSuchElementException.class);
        wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return isPresentForWait(element);
            }

            public String toString() {
                return null;
            }
        });
    }

    public static void appearElement(WebDriver driver, By by) {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(25))
                .ignoring(NoSuchElementException.class);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public static void appearElement(WebDriver driver, By by, int waitTime) {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(waitTime))
                .ignoring(NoSuchElementException.class);
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    public static void appearAndVisibleElementByLocator(WebDriver driver, By by) {
        Wait<WebDriver> wait = new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .ignoring(NoSuchElementException.class)
                .ignoring(ElementNotInteractableException.class);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static void visibleElement(WebDriver driver, WebElement element) {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(25))
                .ignoring(ElementNotInteractableException.class);
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static void visibleElementByLocator(WebDriver driver, By by) {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(25))
                .ignoring(ElementNotInteractableException.class);
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public static void clickableElement(WebDriver driver, WebElement element) {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void clickableElement(WebDriver driver, By by) {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        wait.until(ExpectedConditions.elementToBeClickable(by));
    }

    public static void visibleAndClicableElement(WebDriver driver, WebElement element) {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(25))
                .ignoring(ElementNotInteractableException.class)
                .ignoring(NoSuchElementException.class);
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public static void invisibleElement(WebDriver driver, WebElement element) {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(25));
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    public static void apearAndVisible(WebDriver driver, WebElement element) {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(25))
                .ignoring(ElementNotInteractableException.class)
                .ignoring(NoSuchElementException.class);
        wait.until(ExpectedConditions.and(ExpectedConditions.visibilityOf(element)));
    }

    public static void sleep(int i) {
        try {
            Thread.sleep(i);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void waitText(WebDriver driver, By by, String text) {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(25))
                .ignoring(ElementNotInteractableException.class);
        wait.until(ExpectedConditions.textMatches(by, Pattern.compile(text)));
    }

    public static void waitVisibleList(WebDriver driver, By by) {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(25))
                .ignoring(ElementNotInteractableException.class);
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
    }

    public static void waitVisibleList(WebDriver driver, List<WebElement> elements) {
        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(25))
                .ignoring(ElementNotInteractableException.class);
        wait.until(ExpectedConditions.visibilityOfAllElements(elements));
    }

    public static void waitPageLoaded(WebDriver driver) {
//        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(25))
//                .ignoring(ElementNotInteractableException.class);;
//
//        wait.until(new ExpectedCondition<Boolean>() {
//            public Boolean apply(WebDriver wdriver) {
//                return ((JavascriptExecutor) driver).executeScript(
//                        "return document.readyState"
//                ).equals("complete");
//            }
//        });

        Wait<WebDriver> wait = new WebDriverWait(driver, Duration.ofSeconds(25))
                .ignoring(ElementNotInteractableException.class);
        wait.until(webDriver -> ((JavascriptExecutor) driver).executeScript("return document.readyState").toString().equals("complete"));
    }
}




