package com.ecom.ui.paylink.redirect.pages;

import com.ecom.ui.util.Waiters;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.CacheLookup;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import com.ecom.ui.common.BasePage;

public class TransactionPage extends BasePage {
    public TransactionPage(WebDriver driver) { super(driver); }
    private final String path = "//*[contains(text(),'%s')]";

    @CacheLookup
    @FindBy(xpath = "//a[contains(@href,'/go/merchant/do?action=trans')]")
    private WebElement tranMerchantMenu;

    @CacheLookup
    @FindBy(xpath = "/html/body/table[1]/tbody/tr/td[1]/table/tbody/tr[4]/td/a")
    private WebElement tranMerchantMenuRBKO;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"menu\"]/table/tbody/tr[4]/td/a")
    private WebElement tranMerchantMenuRBRS;

    @CacheLookup
    @FindBy(css = "body > table:nth-child(3) > tbody > tr > td:nth-child(1) > table > tbody > tr:nth-child(4) > td > a")
    private WebElement tranMerchantMenuRBAL;

    @CacheLookup
    @FindBy(css = "body > table:nth-child(3) > tbody > tr > td:nth-child(1) > table > tbody > tr:nth-child(4) > td > a")
    private WebElement tranMerchantMenuRBBG;

    @CacheLookup
    @FindBy(css = "body > aside > nav > ul:nth-child(1) > li:nth-child(4) > a")
    private WebElement tranMerchantMenuRBBH;

    @CacheLookup
    @FindBy(name = "orderId")
    private WebElement orderId;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"searchForm\"]/div/fieldset/table/tbody/tr[1]/td[4]/input")
    private WebElement orderIdRBBG;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"searchForm\"]/div/fieldset/table/tbody/tr[1]/td[4]/input")
    private WebElement orderIdRBKO;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"orderId\"]")
    private WebElement orderIdRBBH;

    @CacheLookup
    @FindBy(name = "merchantId")
    private WebElement merchantId;

    @CacheLookup
    @FindBy(xpath = "//input[@class='button'][@type='submit']")
    private WebElement btnSearch;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"searchForm\"]/table/tbody/tr/td/input")
    private WebElement btnSearchRBBG;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"searchForm\"]/table/tbody/tr/td/input")
    private WebElement btnSearchRBKO;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"searchForm\"]/div/div[5]/div[1]/input")
    private WebElement btnSearchRBBH;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"submit\"]")
    private WebElement btnSubmit;

    @CacheLookup
    @FindBy(id = "submit")
    private WebElement btnSubmit2;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"submit\"]")
    private WebElement btnSubmitRBRS;

    @CacheLookup
    @FindBy(xpath = "/html/body/table[2]/tbody/tr/td/div/fieldset[2]/form/table/tbody/tr[2]/td/input[1]")
    private WebElement btnSubmitRBKO;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"amountForm\"]/table/tbody/tr[2]/td/input[1]")
    private WebElement btnSubmitRBI;

    @CacheLookup
    @FindBy(css = ".btn.btn-primary")
    private WebElement btnSearchRBA;

    @CacheLookup
    @FindBy(xpath = "//*[@id='submitReverse']")
    private WebElement btnSubmitReverse;

    @CacheLookup
    @FindBy(css = "#searchForm > table > tbody > tr > td > input")
    private WebElement btnSubmitRBAL;

    @CacheLookup
    @FindBy(xpath = "/html/body/table[2]/tbody/tr/td/div/fieldset[3]/form/table/tbody/tr[2]/td/input[1]")
    private WebElement btnSubmitRBBG;

    @CacheLookup
    @FindBy(xpath = "/html/body/div/div/div/form[2]/div[2]/div/button") // pld-1000
    private WebElement btnSubmitRBBH;

    @CacheLookup
    @FindBy(xpath = "/html/body/table[2]/tbody/tr/td/div/table/tbody/tr[9]/td")
    private WebElement totalAmount;

    @CacheLookup
    @FindBy(xpath = "/html/body/table[2]/tbody/tr/td/div/table/tbody/tr[1]/td[1]/table/tbody/tr[9]/td")
    private WebElement totalAmountGo;

    @CacheLookup
    @FindBy (xpath = "//*[@id=\"content\"]/table/tbody/tr[9]/td")
    private WebElement getTotalAmountRBI;

    @CacheLookup
    @FindBy (xpath = "//*[@id=\"content\"]/table/tbody/tr[9]/td")
    private WebElement getTotalAmountRBAL;

    @CacheLookup
    @FindBy (css = "#content > fieldset:nth-child(4) > table > tbody > tr:nth-child(1) > td:nth-child(1) > table > tbody > tr:nth-child(10) > td")
    private WebElement getTotalAmountRBBG;

    @CacheLookup
    @FindBy (css = "body > div > div > div > div > div:nth-child(2) > div > ul > li:nth-child(1) > span.value")
    private WebElement getTotalAmountRBBH;

    @CacheLookup
    @FindBy (xpath = "/html/body/div/div/div/div/div[2]/div/ul/li[1]/span[2]")
    private WebElement getTotalAmountRBA;

    @CacheLookup
    @FindBy (xpath = "//*[@id=\"content\"]/table/tbody/tr[9]/td")
    private WebElement getTotalAmountRBKO;

    @CacheLookup
    @FindBy (css = "#content > fieldset:nth-child(2) > table > tbody > tr:nth-child(9) > td:nth-child(2)")
    private WebElement getTotalAmountRBRS;

    @CacheLookup
    @FindBy(xpath = "//*[@id=\"amount\"]")
    private WebElement amountRBRS;

    @CacheLookup
    @FindBy(xpath = "//*[@id='amountReverse']")
    private WebElement amountReverse;

    @CacheLookup
    @FindBy(xpath = "//*[@id='amount']")
    private WebElement amount;

    @CacheLookup
    @FindBy(id = "amount")
    private WebElement inputAmount;



    public void getTransactionsMenu() {
        Waiters.appearElement(getDriver(),tranMerchantMenu);
        tranMerchantMenu.click();
        Waiters.waitPageLoaded(getDriver());
    }

    public void getTransactionsMenuRBKO() {
        Waiters.appearElement(getDriver(),tranMerchantMenuRBKO);
        tranMerchantMenuRBKO.click();
        Waiters.waitPageLoaded(getDriver());
    }

    public void getTransactionsMenuRBRS() {
        Waiters.appearElement(getDriver(),tranMerchantMenuRBRS);
        tranMerchantMenuRBRS.click();
        Waiters.waitPageLoaded(getDriver());
    }


    public void getTransactionsMenuRBAL() {
        Waiters.appearElement(getDriver(),tranMerchantMenuRBAL);
        tranMerchantMenuRBAL.click();
        Waiters.waitPageLoaded(getDriver());
    }

    public void getTransactionsMenuRBBG() {
        Waiters.appearElement(getDriver(),tranMerchantMenuRBBG);
        tranMerchantMenuRBBG.click();
        Waiters.waitPageLoaded(getDriver());
    }


    public void getTransactionsMenuRBBH() {
        Waiters.appearElement(getDriver(),tranMerchantMenuRBBH);
        tranMerchantMenuRBBH.click();
        Waiters.waitPageLoaded(getDriver());
    }

    private String getAmount(WebElement webElement) {
        return webElement.getText().substring(0,webElement.getText().length()-3);
    }

    private void checkAmount(WebElement webElement, String amountPost) {
        if(webElement.getAttribute("value").length()>amountPost.length()){
            Waiters.sleep(1000);
            webElement.sendKeys(Keys.DELETE);
        }
    }

    private void checkAmountWithDot(WebElement webElement, String amountPost) {
        if(webElement.getAttribute("value").length()>amountPost.length()+1){
            Waiters.sleep(1000);
            webElement.sendKeys(Keys.ARROW_LEFT);
            webElement.sendKeys(Keys.ARROW_LEFT);
            webElement.sendKeys(Keys.ARROW_LEFT);
            webElement.sendKeys(Keys.DELETE);
        }
    }

    public void findTranByOrder(String order){
     //   Waiters.appearElement(getDriver(),orderId);
        orderId.click();
        orderId.sendKeys(order);
        Waiters.clickableElement(getDriver(), btnSearch);
        btnSearch.click();
        Waiters.waitPageLoaded(getDriver());
    }

    public void findTranByOrderRBI(String order){
        Waiters.appearElement(getDriver(),orderId);
        orderId.click();
        orderId.sendKeys(order);
        Waiters.clickableElement(getDriver(),btnSearch);
        btnSearch.click();
        Waiters.waitPageLoaded(getDriver());
    }

    public void findTranByOrderRBAL(String order){
        Waiters.appearElement(getDriver(),orderId);
        orderId.click();
        orderId.sendKeys(order);
        Waiters.clickableElement(getDriver(),btnSubmitRBAL);
        btnSubmitRBAL.click();
    }

    public void findTranByOrderRBA(String order){
        Waiters.appearElement(getDriver(),orderId);
        orderId.click();
        orderId.sendKeys(order);
        btnSearchRBA.click();
    }

    public void findTranByOrderRBBG(String order){
        Waiters.appearElement(getDriver(),orderIdRBBG);
        orderIdRBBG.click();
        orderIdRBBG.sendKeys(order);
        Waiters.clickableElement(getDriver(), btnSearchRBBG);
        btnSearchRBBG.click();
        Waiters.waitPageLoaded(getDriver());
    }

    public void findTranByOrderRBKO(String order){
        Waiters.appearElement(getDriver(),orderIdRBKO);
        orderIdRBKO.click();
        orderIdRBKO.sendKeys(order);
        Waiters.clickableElement(getDriver(),btnSearchRBKO);
        btnSearchRBKO.click();
    }

    public void findTranByOrderRBBH(String order){
        Waiters.appearElement(getDriver(),orderIdRBBH);
        orderIdRBBH.click();
        orderIdRBBH.sendKeys(order);
        Waiters.clickableElement(getDriver(),btnSearchRBBH);
        btnSearchRBBH.click();
    }

    public void findTranByMerchant(String merch){
        Waiters.appearElement(getDriver(),merchantId);
        Select merchant = new Select(merchantId);
        merchant.selectByVisibleText(merch);
        Waiters.appearElement(getDriver(),btnSearch);
        btnSearch.click();
    }

    public void selectTran (int tran){
        Waiters.sleep(1500);
       Waiters.clickableElement(getDriver(), getDriver().findElement(By.linkText(String.valueOf(tran))));
        getDriver().findElement(By.linkText(String.valueOf(tran))).click();
    }

    public void postAuthorization(){
        Waiters.appearElement(getDriver(),btnSubmit);
        Waiters.sleep(1000);
        btnSubmit.click();
    }

    public void postAuthorizationRBAL (){
        Waiters.appearElement(getDriver(),getTotalAmountRBAL);
        String amountPost = getTotalAmountRBAL.getText();
        amountPost = String.valueOf((Integer.valueOf(amountPost)));
        Waiters.appearElement(getDriver(),inputAmount);
        inputAmount.clear();
        inputAmount.click();
        inputAmount.sendKeys(amountPost);
        checkAmount(inputAmount, amountPost);
        Waiters.appearElement(getDriver(),btnSubmit);
        Waiters.sleep(1000);
        System.out.println("Before post submit");
        btnSubmit.click();
    }

    public void postAuthorizationRBA (){
        Waiters.appearElement(getDriver(),getTotalAmountRBA);
        String amountPost = getAmount(getTotalAmountRBA);
        amountPost = String.valueOf((Integer.valueOf(amountPost)));
        Waiters.appearElement(getDriver(),inputAmount);
        inputAmount.clear();
        inputAmount.click();
        inputAmount.sendKeys(amountPost);
        Waiters.clickableElement(getDriver(),btnSubmit);
        Waiters.sleep(1000);
        btnSubmit.click();
    }

    public void postAuthorizationRBKO(){
        Waiters.appearElement(getDriver(),getTotalAmountRBKO);
        String amountPost = getTotalAmountRBKO.getText();
        Waiters.appearElement(getDriver(),inputAmount);
        inputAmount.clear();
        inputAmount.sendKeys(amountPost);
        checkAmount(inputAmount, amountPost);
        Waiters.appearElement(getDriver(),btnSubmitRBKO);
        Waiters.sleep(1000);
        btnSubmitRBKO.click();
    }

    public void postAuthorizationRBBG(){
        Waiters.appearElement(getDriver(),getTotalAmountRBBG);
        String amountPost = getTotalAmountRBBG.getText();
        amountPost= String.valueOf((Integer.valueOf(amountPost)));
        Waiters.appearElement(getDriver(),inputAmount);
        inputAmount.click();
        inputAmount.sendKeys(amountPost);
        checkAmount(inputAmount, amountPost);
        Waiters.appearElement(getDriver(),btnSubmitRBBG);
        Waiters.sleep(1000);
        btnSubmitRBBG.click();
    }

    public void postAuthorizationRBBH(){
        /*Waiters.appearElement(getDriver(),getTotalAmountRBBH);
        String amountPost = getAmount(getTotalAmountRBBH);
        Waiters.appearElement(getDriver(),inputAmount);
        inputAmount.click();
        inputAmount.sendKeys(amountPost);
        checkAmount(inputAmount, amountPost);*/
        Waiters.appearElement(getDriver(),btnSubmitRBBH);
        Waiters.sleep(1000);
        btnSubmitRBBH.click();
    }

    public void postAuthorizationRBRS(){
        Waiters.appearElement(getDriver(),getTotalAmountRBRS);
        String amountPost = getTotalAmountRBRS.getText();
        amountPost= String.valueOf((Integer.valueOf(amountPost)));
        Waiters.appearElement(getDriver(),inputAmount);
        inputAmount.clear();
        inputAmount.sendKeys(amountPost);
        checkAmount(inputAmount, amountPost);
        Waiters.appearElement(getDriver(),btnSubmitRBRS);
        Waiters.sleep(1000);
        btnSubmitRBRS.click();
    }

    public void postAuthorizationRBI(){
        Waiters.appearElement(getDriver(),getTotalAmountRBI);
        String amountPost = getTotalAmountRBI.getText();
        amountPost= String.valueOf((Integer.valueOf(amountPost)));
        Waiters.appearElement(getDriver(),inputAmount);
        inputAmount.clear();
        inputAmount.sendKeys(amountPost);
        checkAmount(inputAmount, amountPost);
        Waiters.appearElement(getDriver(),btnSubmit);
        Waiters.sleep(1000);
        btnSubmit.click();
    }

    public void postAuthorizationOtherAmount (double delta){
        System.out.println("one");
        Waiters.appearElement(getDriver(),totalAmountGo);
        System.out.println("two");
        String amount = getAmount(totalAmountGo);
        double amountPost = Double.parseDouble(amount);
        amountPost += amountPost * delta;
        System.out.println("Post amount: " + amountPost);
        Waiters.appearElement(getDriver(),inputAmount);
        inputAmount.click();
        inputAmount.clear();
        inputAmount.sendKeys(Double.toString(amountPost));
        checkAmountWithDot(inputAmount, amount);
        Waiters.appearElement(getDriver(),btnSubmit2);
        Waiters.sleep(1000);
        btnSubmit2.click();
    }

    public void postAuthorizationOtherAmountRBI (double delta){
        Waiters.appearElement(getDriver(),getTotalAmountRBI);
        String amount = getAmount(getTotalAmountRBI);
        double amountPost = Double.parseDouble(amount);
        amountPost=amountPost+amountPost*delta;
        System.out.println("Post amount: " + (int) amountPost);
        Waiters.appearElement(getDriver(),inputAmount);
        inputAmount.clear();
        inputAmount.sendKeys(Double.toString(amountPost));
        Waiters.appearElement(getDriver(),btnSubmit);
        btnSubmit.click();
    }

    public void toDoRefund(){
        Waiters.appearElement(getDriver(),totalAmountGo);
        String amountR = totalAmountGo.getText();
        Waiters.appearElement(getDriver(),amountReverse);
        amountReverse.click();
        amountReverse.clear();
        amountReverse.sendKeys(amountR);
        checkAmount(amountReverse, amountR);
        Waiters.appearElement(getDriver(),btnSubmitReverse);
        btnSubmitReverse.click();
    }

    public void toDoRefundRBI(){//For amount format 0.00
        Waiters.appearElement(getDriver(),totalAmount);
        String amountR = totalAmount.getText();
        Waiters.appearElement(getDriver(),amountReverse);
        amountReverse.click();
        amountReverse.clear();
        amountReverse.sendKeys(amountR);
        Waiters.appearElement(getDriver(),btnSubmitReverse);
        btnSubmitReverse.click();
    }

    public void toDoRefundPartial(String reversAmount){
        Waiters.appearElement(getDriver(),amount);
        amount.sendKeys(reversAmount);
        Waiters.appearElement(getDriver(),btnSubmit);
        btnSubmit.click();
    }

    public void toDoReversalRBI(){
        Waiters.appearElement(getDriver(),getTotalAmountRBI);
        String amountR = getTotalAmountRBI.getText();
        amountR = String.valueOf((Integer.valueOf(amountR)));
        Waiters.appearElement(getDriver(),amountReverse);
        amountReverse.click();
        amountReverse.clear();
        amountReverse.sendKeys(amountR);
        checkAmount(amountReverse, amountR);
        Waiters.appearElement(getDriver(),btnSubmitReverse);
        btnSubmitReverse.click();
    }

    public void toDoReversalRBAL(){
        Waiters.appearElement(getDriver(),getTotalAmountRBI);
        String amountR = getTotalAmountRBAL.getText();
        amountR = String.valueOf((Integer.valueOf(amountR)));
        Waiters.appearElement(getDriver(),amountReverse);
        amountReverse.click();
        amountReverse.clear();
        amountReverse.sendKeys(amountR);
        checkAmount(amountReverse, amountR);
        Waiters.appearElement(getDriver(),btnSubmitReverse);
        btnSubmitReverse.click();
    }

    public void toDoReversalRBA(){
        Waiters.appearElement(getDriver(),getTotalAmountRBA);
        String amountR = getTotalAmountRBA.getText();
        Waiters.appearElement(getDriver(),amountReverse);
        amountReverse.click();
        amountReverse.clear();
        amountReverse.sendKeys(amountR);
        checkAmount(amountReverse, amountR);
        Waiters.appearElement(getDriver(),btnSubmitReverse);
        btnSubmitReverse.click();
    }

    public void toDoReversalRBKO(){
        Waiters.appearElement(getDriver(),getTotalAmountRBKO);
        String amountR = getTotalAmountRBKO.getText();
        amountR= String.valueOf((Integer.valueOf(amountR)));
        Waiters.appearElement(getDriver(),amountReverse);
        amountReverse.click();
        amountReverse.sendKeys(amountR);
        checkAmount(amountReverse,amountR);
        Waiters.appearElement(getDriver(),btnSubmitReverse);
        btnSubmitReverse.click();
    }

    public void toDoReversalRBBG(){
        String amountR = getTotalAmountRBBG.getText();
        amountR = String.valueOf((Integer.valueOf(amountR)));
        Waiters.appearElement(getDriver(),amountReverse);
        amountReverse.click();
        amountReverse.clear();
        amountReverse.sendKeys(amountR);
        checkAmount(amountReverse, amountR);
        Waiters.appearElement(getDriver(),btnSubmitReverse);
        btnSubmitReverse.click();
    }

    public void toDoReversalRBBH(){
        Waiters.appearElement(getDriver(),getTotalAmountRBBH);
        String amountR = getTotalAmountRBBH.getText();
        Waiters.appearElement(getDriver(),amountReverse);
        amountReverse.click();
        amountReverse.clear();
        amountReverse.sendKeys(amountR);
        checkAmount(amountReverse, amountR);
        Waiters.appearElement(getDriver(),btnSubmitReverse);
        btnSubmitReverse.click();
    }

    public void toDoReversalRBRS(){
        String amountR = getTotalAmountRBRS.getText();
        amountR = String.valueOf(Integer.valueOf(amountR));
        amountReverse.click();
        amountReverse.clear();
        amountReverse.sendKeys(amountR);
        checkAmount(amountReverse,amountR);
        Waiters.appearElement(getDriver(),btnSubmitReverse);
        btnSubmitReverse.click();
    }
}