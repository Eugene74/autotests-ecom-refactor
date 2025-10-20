package com.ecom.tests.support;

import org.openqa.selenium.WebDriver;
import tests.BaseTest;
import com.ecom.ui.mt.p2p.pages.ContentPage;
import com.ecom.ui.mt.p2p.pages.MoneyTransferPage;


import java.util.ArrayList;
import java.util.List;

import static com.ecom.core.config.EnvData.URLp2p;
import static com.ecom.db.JDBCMethods.getRequestIdMT;
import static com.ecom.ui.util.Waiters.handleAlert;

public class MoneyTransferPageUtils extends BaseTest {
    public static final String senderName = "Yarosh S";
    public static final String recipientName = "Yarik Vuk";
    public static final String senderCity = "Glasgow";
    public static final String senderStreet = "Bag";
    public static final String senderHouse = "1";
    public static final String senderFlat = "101";
    public static final String senderPhoneNum = "101010101010";

    private final List<String> response = new ArrayList<>();

    public String domesticTransfer(int merchantId, String[] senderCard, String recipientCardNum, Class<? extends MoneyTransferPage> pageClass) {
        getDriver().get(URLp2p + Integer.toHexString(merchantId));

        MoneyTransferPage mainPage;
        try {
            // Динамическое создание объекта страницы
            mainPage = pageClass.getDeclaredConstructor(WebDriver.class).newInstance(getDriver());
        } catch (Exception e) {
            throw new RuntimeException("Failed to create page instance: " + pageClass.getName(), e);
        }

        mainPage.enterSenderCardNum(senderCard, senderName);
        mainPage.enterRecipientCardNum(recipientCardNum, recipientName);
        mainPage.enterAmount("777");
        mainPage.confirmTransfer();

        handleAlert(getDriver());
        // Передаем merchantId вместо фиксированного id_AVAL
        String generatedRequestId = getRequestIdMT(merchantId);

        return generatedRequestId;
    }

    public List<String> getContentData(){
        ContentPage contentPage = new ContentPage(getDriver());
        response.add(contentPage.checkSuccessResponse());

        return response;
    }
}