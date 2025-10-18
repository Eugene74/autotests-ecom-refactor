package com.ecom.tests.support;

import tests.BaseTest;
import com.ecom.ui.dashboard.functional.pages.*;
import com.ecom.ui.paylink.redirect.pages.EnterPaymentCardPage;
import com.ecom.ui.paylink.redirect.pages.GoPaymentCardPage;


public class DashboardRequest extends BaseTest {

    public String LogInSuccess(){
        LoginPage page = new LoginPage(getDriver());
        return page.logIn(URLdasboard, login, password);
    }

    public String LogInUnsuccess(){
        LoginPage page = new LoginPage(getDriver());
        return page.logIn(URLdasboard, login, password.substring(0, password.length()-2));
    }

    public boolean searchMerchantByName(){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.doSearchByMerchant();
    }
    public boolean searchMerchantByName(String merchant){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.doSearchByMerchant(merchant);
    }

    public boolean openMerchant(String merchant){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.doSearchByMerchant(merchant);
    }

    public boolean searchMerchantByID(){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.doSearchByMerchantID();
    }

    public boolean searchMerchantByMultFields(){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.doSearchByMultFields();
    }

    public boolean openFindedMerchant(String merchant){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.openMerch(merchant);
    }

    public boolean searchTransaction(){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.doSearchByMultFields();
    }

    public boolean findMTDomTranByID(String trackingId){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.findTransactionByTrackingId(trackingId);
    }

    public boolean findMTDomTranByMID(String merchantId, String trackingId){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.findTransactionByMerchantId(merchantId, trackingId);
    }

    public boolean findMTDomTranByCard(String cardNum, String trackingId){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.findTransactionByCardNumber(cardNum, trackingId);
    }

    public boolean findMTDomTranByMultFields(String merchant, String cardNum, String trackingId){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.findTransactionByMultFields(merchant, cardNum, trackingId);
    }

    public boolean openDomesticTransaction(String trackingId){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.openMTDomesticTransaction(trackingId);
    }

    public boolean makeMTReceipt(String trackingId){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.makeMTReceipt(trackingId);
    }
    public boolean findMTInterTranByID(String trackingId){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.findInterTransactionByTrackingId(trackingId);
    }

    public boolean findMTInterTranByMID(String merchantId, String trackingId){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.findInterTransactionByMerchantId(merchantId, trackingId);
    }

    public boolean findMTInterTranByCard(String cardNum, String trackingId){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.findTransactionInterByCardNumber(cardNum, trackingId);
    }

    public boolean findMTInterTranByMultFields(String merchant, String cardNum, String trackingId){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.findInterTransactionByMultFields(merchant, cardNum, trackingId);
    }

    public boolean findMTInterTranByCardAndStatus(String cardNum, String trackingId){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.findInterTransactionByCardAndStatus(cardNum, trackingId);
    }

    public boolean openInternationalTransaction(String trackingId){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.openMTInterTransaction(trackingId);
    }

    public boolean exportInternationalTransaction(){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.doExportInterTransactions();
    }

    public boolean createExchangeRate(String currencyFrom, String currencyTo, String  rate, String bank){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.toCreateExchangeRate(currencyFrom, currencyTo, rate, bank);
    }

    public boolean deleteExchangeRate(String currencyFrom, String currencyTo, String  rate, String bank){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        MainPage page = new MainPage(getDriver());
        return page.toDeleteExchangeRate(currencyFrom, currencyTo, rate, bank);
    }

    public boolean findPaymentByMerch(String merchant, String order){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        PaymentsPage page = new PaymentsPage(getDriver());
        return page.findTranByMerchant(merchant, order);
    }

    public boolean findPaymentByOrder(String order){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        PaymentsPage page = new PaymentsPage(getDriver());
        return page.findTranByOrderId(order);
    }

    public boolean findPaymentByRRN(String order, String rrn){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        PaymentsPage page = new PaymentsPage(getDriver());
        return page.findTranByRRN(order, rrn);
    }

    public boolean findPaymentByCard(String order, String card){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        PaymentsPage page = new PaymentsPage(getDriver());
        return page.findTranByCard(order, card);
    }

    public boolean findPaymentByApprovalCode(String order, String app_code){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        PaymentsPage page = new PaymentsPage(getDriver());
        return page.findTranByApprovalCode(order, app_code);
    }

    public boolean findPaymentByMultField(String order, String merchant, String rrn, String card, String app_code){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        PaymentsPage page = new PaymentsPage(getDriver());
        return page.findTranByMultFields(order, merchant, rrn, card, app_code);
    }

    /*
    public boolean openPayment(String trackingId, String tranID){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        PaymentsPage page = new PaymentsPage(getDriver());
        return page.openTransaction(trackingId, tranID);
    }*/

    public void makeReversalForPayment(String trackingId, String tranID){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        PaymentsPage page = new PaymentsPage(getDriver());
        page.doReversal(trackingId, tranID);
    }

    public void makeReversalForPaymentFR(String trackingId, String tranID){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        PaymentsPage page = new PaymentsPage(getDriver());
        page.doReversalFR(trackingId, tranID);
    }

    public void makeFastRefundForPayment(String trackingId, String tranID){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        PaymentsPage page = new PaymentsPage(getDriver());
        page.doFastRefund(trackingId, tranID);
    }

    public void makePartialFastRefund(String trackingId, String tranID){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        PaymentsPage page = new PaymentsPage(getDriver());
        page.doPartialFastRefund(trackingId, tranID);
    }

    public String createInvoice(String merchant, String amount, String orderID, String terminal_AVAL){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        ToolsPage page = new ToolsPage(getDriver());
        return page.createInvoice(merchant, amount, orderID, terminal_AVAL);
    }

    public String createMultipayInvoice(String merchant, String amount, String orderID, String terminal_AVAL){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        ToolsPage page = new ToolsPage(getDriver());
        return page.createMultipayInvoice(merchant, amount, orderID, terminal_AVAL);
    }


    public boolean checkStatus(String merchant, String orderID, String status){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        ToolsPage page = new ToolsPage(getDriver());
        return page.checkStatus(merchant, orderID, status);
    }


    public boolean checkMultipayStatus(String merchant, String orderID, String status){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        ToolsPage page = new ToolsPage(getDriver());
        return page.checkMultipayStatus(merchant, orderID, status);
    }

    public String payInvoice(String url, String[] card){
        getDriver().get(url);
        ToolsPage page = new ToolsPage(getDriver());
        String order = page.payInvoice(url, card);
       // EnterPaymentCardPage cvc = new EnterPaymentCardPage(getDriver());
       // cvc.enterCVC(card);
        return order;
    }

    public String payMultipayInvoice(String url, String[] card){
        getDriver().get(url);
        ToolsPage page = new ToolsPage(getDriver());
        String order = page.payMultipayInvoice(url, card);
       // GoPaymentCardPage cvc = new GoPaymentCardPage(getDriver());
        //cvc.enterCVC(card);
        return order;
    }

    public boolean exportPayments(){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        PaymentsPage page = new PaymentsPage(getDriver());
        return page.doExportTransactions();
    }

    public boolean putCardToStopList(String orderID, String tranID){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        PaymentsPage page = new PaymentsPage(getDriver());
        return page.putToStopList(orderID, tranID);
    }

    public boolean deleteCardFromStopList(String card){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        SettingsPage page = new SettingsPage(getDriver());
        return page.deleteFromStopList(card);
    }

    public boolean sendReceipt(String orderID, String tranID, String email){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        PaymentsPage page = new PaymentsPage(getDriver());
        return page.sendReceipt(orderID, tranID, email);
    }

    public boolean downloadReceipt(String orderID, String tranID){
        new LoginPage(getDriver()).logIn(URLdasboard, login, password);
        PaymentsPage page = new PaymentsPage(getDriver());
        return page.downloadReceipt(orderID, tranID);
    }
}