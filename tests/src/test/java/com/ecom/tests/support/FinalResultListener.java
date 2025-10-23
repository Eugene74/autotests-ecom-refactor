package com.ecom.tests.support;

import com.ecom.ui.driver.WebDriverHolder;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class FinalResultListener implements ITestListener {
  @Override
  public void onTestFailure(ITestResult result) {
    result.getTestContext().getPassedTests().removeResult(result.getMethod());
    result.getTestContext().getSkippedTests().removeResult(result.getMethod());
    WebDriver driver = WebDriverHolder.getDriver();
    try {
      File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      String testName = result.getName().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
      String testClass =
          result.getTestClass().getName().replaceAll("[^a-zA-Z0-9\\.\\-]", "_").replace('.', '_');
      Path dest = Path.of("target/screenshots", testClass + "-" + testName + ".png");
      Files.createDirectories(dest.getParent());
      Files.copy(screenshot.toPath(), dest, StandardCopyOption.REPLACE_EXISTING);
      System.out.println("Screenshot saved: " + dest.toAbsolutePath());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onTestSuccess(ITestResult result) {
    result.getTestContext().getFailedTests().removeResult(result.getMethod());
    result.getTestContext().getSkippedTests().removeResult(result.getMethod());
  }

  @Override
  public void onTestSkipped(ITestResult result) {
    result.getTestContext().getPassedTests().removeResult(result.getMethod());
    result.getTestContext().getFailedTests().removeResult(result.getMethod());
  }

  @Override
  public void onTestStart(ITestResult result) {}

  @Override
  public void onTestFailedButWithinSuccessPercentage(ITestResult result) {}

  @Override
  public void onStart(ITestContext context) {}

  @Override
  public void onFinish(ITestContext context) {}
}
