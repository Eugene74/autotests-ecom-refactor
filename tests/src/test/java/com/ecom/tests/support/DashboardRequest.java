package com.ecom.tests.support;

import static com.ecom.core.config.EnvData.LOGIN;
import static com.ecom.core.config.EnvData.PASSWORD;
import static com.ecom.core.config.EnvData.URL_DASHBOARD;

import com.codeborne.selenide.WebDriverRunner;
import com.ecom.ui.dashboard.functional.pages.*;
import org.openqa.selenium.WebDriver;

public class DashboardRequest {
  private final WebDriver driver;

  public DashboardRequest() {
    // Инициализируем один раз при создании объекта
    this.driver = WebDriverRunner.getWebDriver();
  }

  public String LogInSuccess() {
    LoginPage page = new LoginPage(driver);
    return page.logIn(URL_DASHBOARD, LOGIN, PASSWORD);
  }

  public String LogInUnsuccess() {
    LoginPage page = new LoginPage(driver);
    return page.logIn(URL_DASHBOARD, LOGIN, PASSWORD.substring(0, PASSWORD.length() - 2));
  }

  public boolean searchMerchantByName() {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.doSearchByMerchant();
  }

  public boolean searchMerchantByName(String merchant) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.doSearchByMerchant(merchant);
  }

  public boolean openMerchant(String merchant) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.doSearchByMerchant(merchant);
  }

  public boolean searchMerchantByID() {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.doSearchByMerchantID();
  }

  public boolean searchMerchantByMultFields() {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.doSearchByMultFields();
  }

  public boolean openFindedMerchant(String merchant) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.openMerch(merchant);
  }

  public boolean searchTransaction() {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.doSearchByMultFields();
  }

  public boolean findMTDomTranByID(String trackingId) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.findTransactionByTrackingId(trackingId);
  }

  public boolean findMTDomTranByMID(String merchantId, String trackingId) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.findTransactionByMerchantId(merchantId, trackingId);
  }

  public boolean findMTDomTranByCard(String cardNum, String trackingId) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.findTransactionByCardNumber(cardNum, trackingId);
  }

  public boolean findMTDomTranByMultFields(String merchant, String cardNum, String trackingId) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.findTransactionByMultFields(merchant, cardNum, trackingId);
  }

  public boolean openDomesticTransaction(String trackingId) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.openMTDomesticTransaction(trackingId);
  }

  public boolean makeMTReceipt(String trackingId) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.makeMTReceipt(trackingId);
  }

  public boolean findMTInterTranByID(String trackingId) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.findInterTransactionByTrackingId(trackingId);
  }

  public boolean findMTInterTranByMID(String merchantId, String trackingId) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.findInterTransactionByMerchantId(merchantId, trackingId);
  }

  public boolean findMTInterTranByCard(String cardNum, String trackingId) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.findTransactionInterByCardNumber(cardNum, trackingId);
  }

  public boolean findMTInterTranByMultFields(String merchant, String cardNum, String trackingId) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.findInterTransactionByMultFields(merchant, cardNum, trackingId);
  }

  public boolean findMTInterTranByCardAndStatus(String cardNum, String trackingId) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.findInterTransactionByCardAndStatus(cardNum, trackingId);
  }

  public boolean openInternationalTransaction(String trackingId) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.openMTInterTransaction(trackingId);
  }

  public boolean exportInternationalTransaction() {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.doExportInterTransactions();
  }

  public boolean createExchangeRate(
      String currencyFrom, String currencyTo, String rate, String bank) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.toCreateExchangeRate(currencyFrom, currencyTo, rate, bank);
  }

  public boolean deleteExchangeRate(
      String currencyFrom, String currencyTo, String rate, String bank) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    MainPage page = new MainPage(driver);
    return page.toDeleteExchangeRate(currencyFrom, currencyTo, rate, bank);
  }

  public boolean findPaymentByMerch(String merchant, String order) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    PaymentsPage page = new PaymentsPage(driver);
    return page.findTranByMerchant(merchant, order);
  }

  public boolean findPaymentByOrder(String order) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    PaymentsPage page = new PaymentsPage(driver);
    return page.findTranByOrderId(order);
  }

  public boolean findPaymentByRRN(String order, String rrn) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    PaymentsPage page = new PaymentsPage(driver);
    return page.findTranByRRN(order, rrn);
  }

  public boolean findPaymentByCard(String order, String card) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    PaymentsPage page = new PaymentsPage(driver);
    return page.findTranByCard(order, card);
  }

  public boolean findPaymentByApprovalCode(String order, String app_code) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    PaymentsPage page = new PaymentsPage(driver);
    return page.findTranByApprovalCode(order, app_code);
  }

  public boolean findPaymentByMultField(
      String order, String merchant, String rrn, String card, String app_code) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    PaymentsPage page = new PaymentsPage(driver);
    return page.findTranByMultFields(order, merchant, rrn, card, app_code);
  }

  /*
  public boolean openPayment(String trackingId, String tranID){
      new LoginPage(driver).logIn(URLdasboard, login, password);
      PaymentsPage page = new PaymentsPage(driver);
      return page.openTransaction(trackingId, tranID);
  }*/

  public void makeReversalForPayment(String trackingId, String tranID) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    PaymentsPage page = new PaymentsPage(driver);
    page.doReversal(trackingId, tranID);
  }

  public void makeReversalForPaymentFR(String trackingId, String tranID) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    PaymentsPage page = new PaymentsPage(driver);
    page.doReversalFR(trackingId, tranID);
  }

  public void makeFastRefundForPayment(String trackingId, String tranID) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    PaymentsPage page = new PaymentsPage(driver);
    page.doFastRefund(trackingId, tranID);
  }

  public void makePartialFastRefund(String trackingId, String tranID) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    PaymentsPage page = new PaymentsPage(driver);
    page.doPartialFastRefund(trackingId, tranID);
  }

  public String createInvoice(
      String merchant, String amount, String orderID, String terminal_AVAL) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    ToolsPage page = new ToolsPage(driver);
    return page.createInvoice(merchant, amount, orderID, terminal_AVAL);
  }

  public String createMultipayInvoice(
      String merchant, String amount, String orderID, String terminal_AVAL) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    ToolsPage page = new ToolsPage(driver);
    return page.createMultipayInvoice(merchant, amount, orderID, terminal_AVAL);
  }

  public boolean checkStatus(String merchant, String orderID, String status) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    ToolsPage page = new ToolsPage(driver);
    return page.checkStatus(merchant, orderID, status);
  }

  public boolean checkMultipayStatus(String merchant, String orderID, String status) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    ToolsPage page = new ToolsPage(driver);
    return page.checkMultipayStatus(merchant, orderID, status);
  }

  public String payInvoice(String url, String[] card) {
    driver.get(url);
    ToolsPage page = new ToolsPage(driver);
    String order = page.payInvoice(url, card);
    // EnterPaymentCardPage cvc = new EnterPaymentCardPage(driver);
    // cvc.enterCVC(card);
    return order;
  }

  public String payMultipayInvoice(String url, String[] card) {
    driver.get(url);
    ToolsPage page = new ToolsPage(driver);
    String order = page.payMultipayInvoice(url, card);
    // GoPaymentCardPage cvc = new GoPaymentCardPage(driver);
    // cvc.enterCVC(card);
    return order;
  }

  public boolean exportPayments() {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    PaymentsPage page = new PaymentsPage(driver);
    return page.doExportTransactions();
  }

  public boolean putCardToStopList(String orderID, String tranID) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    PaymentsPage page = new PaymentsPage(driver);
    return page.putToStopList(orderID, tranID);
  }

  public boolean deleteCardFromStopList(String card) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    SettingsPage page = new SettingsPage(driver);
    return page.deleteFromStopList(card);
  }

  public boolean sendReceipt(String orderID, String tranID, String email) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    PaymentsPage page = new PaymentsPage(driver);
    return page.sendReceipt(orderID, tranID, email);
  }

  public boolean downloadReceipt(String orderID, String tranID) {
    new LoginPage(driver).logIn(URL_DASHBOARD, LOGIN, PASSWORD);
    PaymentsPage page = new PaymentsPage(driver);
    return page.downloadReceipt(orderID, tranID);
  }
}
