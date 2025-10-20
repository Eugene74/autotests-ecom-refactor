package com.ecom.tests.support;

import tests.BaseTest;
import com.ecom.ui.paylink.redirect.pages.*;

import java.io.File;

import static com.ecom.core.config.EnvData.URL_MERCH;
import static com.ecom.core.config.EnvData.URLredirect;
import static com.ecom.tests.support.HtmlMethods.*;

public class RedirectRequest extends BaseTest {

    private static final String PATH = "Authorization.html";
    private static final String PATH_INSTALLMENT = "AuthorizationInstallment.html";
    private static final String PATH_LOOKUP = "AccountVerification.html";


    private String getFilePathString(String pathToFile)  {
        File file = null;
        try {
            file = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
                    + File.separator + "resources" + File.separator + "template" + File.separator + "html"
                    + File.separator + pathToFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert file != null;
        return file.toURI().toString();
    }

    public String paymentAuthorization(String[] card, int delay, String merch, String term) {
        fillAuthorizationForm(String.valueOf(delay), URLredirect, merch, term);
        System.out.println("path = " + getFilePathString(PATH));
        getDriver().get(getFilePathString(PATH));
        PageRequest send = new PageRequest(getDriver());
        send.sendReq();
        EnterPaymentCardPage enter = new EnterPaymentCardPage(getDriver());

        return enter.enterCardInfo(card);
    }
//1111111111111111111111
    public String paymentAuthorizationInstallment(String[] card, int delay, String merch, String term) {
        fillAuthorizationFormInstallment(String.valueOf(delay), URLredirect, merch, term);
        getDriver().get(getFilePathString(PATH_INSTALLMENT));
        PageRequest send = new PageRequest(getDriver());
        send.sendReq();
        InstalmentPaymentCardPage enter = new InstalmentPaymentCardPage(getDriver());
        String order = enter.enterCardInfo(card);
        return order;
    }

    public void paymentPostAuthorization(int tranID, String order) {
        MainMerchantPage login = new MainMerchantPage(getDriver());
        login.loginInGo(URL_MERCH);
        login.getTransactionsMenu();
        TransactionPage tran = new TransactionPage(getDriver());
        tran.findTranByOrder(order);
        tran.selectTran(tranID);
        new TransactionPage(getDriver()).postAuthorization();
    }

    public void paymentPostAuthorizationDifAmount(String order, int tranID, double delta){
        MainMerchantPage login = new MainMerchantPage(getDriver());
        login.loginInGo(URL_MERCH);
        login.getTransactionsMenu();
        TransactionPage tran = new TransactionPage(getDriver());
        tran.findTranByOrder(order);
        tran.selectTran(tranID);
        new TransactionPage(getDriver()).postAuthorizationOtherAmount(delta);
    }

    public String createNewInvoice(String urlMerch, int id) {
        MainMerchantPage login = new MainMerchantPage(getDriver());
        login.loginInGo(urlMerch);
        login.getInvoicingMenu();
        InvoicingMerchant invoice = new InvoicingMerchant(getDriver());
        return invoice.createInvoice(id);
    }

    public String paymentInvoicing(String urlMerch, String[]card, int id) {
        MainMerchantPage login = new MainMerchantPage(getDriver());
        login.loginInGo(urlMerch);
        login.getInvoicingMenu();
        InvoicingMerchant invoice = new InvoicingMerchant(getDriver());
        String order = invoice.createInvoice(id);
        new MainMerchantPage(getDriver()).getInvoicingMenu();
        new InvoicingMerchant(getDriver()).searchInvoice(order);
        getDriver().get(invoice.goPay());
        EnterPaymentCardPage enter = new EnterPaymentCardPage(getDriver());
        return enter.enterCardInfoInvoicing(card);
    }


    public void usedCVC(String[] card){
        EnterPaymentCardPage cvc = new EnterPaymentCardPage(getDriver());
       // cvc.enterCVC(card);
    }

    public void doReversal(String url, String order, int tranId) {
        LoginAdminPage loginPage = new LoginAdminPage(getDriver());
        loginPage.loginIn(url);
        TransactionPage transactionPage = new TransactionPage(getDriver());
        transactionPage.getTransactionsMenu();
        transactionPage.findTranByOrder(order);
        transactionPage.selectTran(tranId);
        new TransactionPage(getDriver()).toDoRefund();
    }



    public String accountVerify(String[] card) {
        fillVerificationForm(URLredirect);
        getDriver().get(getFilePathString(PATH_LOOKUP));
        PageRequest send = new PageRequest(getDriver());
        send.sendReq();
        EnterPaymentCardPage enter = new EnterPaymentCardPage(getDriver());
        return enter.enterCardInfo(card);
    }

    public void changeAquirerID(String inputID, String url) {
        LoginAdminPage loginPage = new LoginAdminPage(getDriver());
        loginPage.loginIn(url);
        BanksPage banksPage = new BanksPage(getDriver());
        banksPage.chandeAquirerID(inputID);
    }
}
