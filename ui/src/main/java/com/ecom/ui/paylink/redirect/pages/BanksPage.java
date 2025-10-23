package com.ecom.ui.paylink.redirect.pages;

import com.ecom.ui.common.BasePage;
import com.ecom.ui.util.Waiters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;

public class BanksPage extends BasePage {

  public BanksPage(WebDriver driver) {
    super(driver);
  }

  @CacheLookup
  @FindBy(xpath = "//*[@id=\"content\"]/table/tbody/tr/td/form/table[1]/tbody/tr[3]/td/input")
  private WebElement aquirerID_input;

  @CacheLookup
  @FindBy(xpath = "//*[@id=\"content\"]/table/tbody/tr/td/form/table[2]/tbody/tr/td[1]/input")
  private WebElement sentChanges_btn;

  public void chandeAquirerID(String inputID) {
    Waiters.sleep(2000);
    aquirerID_input.click();
    Waiters.sleep(2000);
    aquirerID_input.clear();
    Waiters.sleep(2000);
    aquirerID_input.sendKeys(inputID);
    Waiters.sleep(2000);
    sentChanges_btn.click();
    Waiters.sleep(2000);
  }
}
