package com.ecom.tests.steps;

import com.ecom.ui.paylink.redirect.pages.EnterPaymentCardPage;
import com.ecom.ui.paylink.redirect.pages.InvoicingMerchant;
import com.ecom.ui.paylink.redirect.pages.MainMerchantPage;
import org.openqa.selenium.WebDriver;

public class InvoiceSteps {
    private final WebDriver driver;

    public InvoiceSteps(WebDriver driver) {
        this.driver = driver;
    }

    public String createNewInvoice(String urlMerch, int id) {
        MainMerchantPage merchantPage = new MainMerchantPage(driver);
        merchantPage.loginInGo(urlMerch);
        merchantPage.getInvoicingMenu();

        return new InvoicingMerchant(driver).createInvoice(id);
    }

    public String payInvoice(String urlMerch, String[] card, int id) {
        MainMerchantPage merchantPage = new MainMerchantPage(driver);
        merchantPage.loginInGo(urlMerch);
        merchantPage.getInvoicingMenu();

        InvoicingMerchant invoicePage = new InvoicingMerchant(driver);
        String order = invoicePage.createInvoice(id);
        merchantPage.getInvoicingMenu();

        new InvoicingMerchant(driver).searchInvoice(order);
        driver.get(invoicePage.goPay());

        return new EnterPaymentCardPage(driver).enterCardInfoInvoicing(card);
    }
}
