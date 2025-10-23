package com.ecom.tests.steps;

import com.ecom.tests.support.HtmlMethods;
import com.ecom.ui.paylink.redirect.pages.EnterPaymentCardPage;
import com.ecom.ui.paylink.redirect.pages.PageRequest;
import org.openqa.selenium.WebDriver;

import static com.ecom.tests.support.HtmlMethods.getHtmlPath;

public class VerificationSteps {
    private final WebDriver driver;

    public VerificationSteps(WebDriver driver) {
        this.driver = driver;
    }


    public String verifyAccount(String[] card, String redirectUrl) {
        HtmlMethods.fillVerificationForm(redirectUrl);
        driver.get(getHtmlPath("AccountVerification.html"));

        new PageRequest(driver).sendReq();
        return new EnterPaymentCardPage(driver).enterCardInfo(card);
    }
}
