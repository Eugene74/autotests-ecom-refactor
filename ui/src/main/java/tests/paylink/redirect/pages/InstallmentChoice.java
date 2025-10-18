package tests.paylink.redirect.pages;

import methods.Waiters;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import tests.paylink.redirect.BasePage;

public class InstallmentChoice extends BasePage {

   // @CacheLookup
   // @FindBy(xpath = "//*[@id=\"installment\"]/ul/li/select/option[1]")
   // private WebElement instNumber;

    @CacheLookup
    @FindBy(xpath = "/html/body/div/div/main/section/div/form/div/div[3]/div/div[2]/button[2]/svg/path")
    private WebElement instNumber;



    @CacheLookup
    @FindBy(id = "installmentConfirm")
    private WebElement installmentConfirm;

    public InstallmentChoice(WebDriver driver) { super(driver); }

    public void chooseIstall(){
        System.out.println("Open window for choice");
        Waiters.sleep(1000);
//        instNumber.click();
 //       installmentConfirm.click();
    }

}
