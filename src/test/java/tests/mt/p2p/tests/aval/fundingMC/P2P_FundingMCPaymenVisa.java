package tests.mt.p2p.tests.aval.fundingMC;

import methods.MoneyTransferPageUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseRedirect;
import tests.mt.p2p.pages.*;
import tests.paylink.redirect.BasePage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import java.util.List;

import static jdbc.JDBCMethods.*;
import static methods.DocumentTools.verifiedDataMTFromDBFunding;
import static methods.DocumentTools.verifiedDataMTFromDBPayment;
import static type.Attributes.ALLOW_CROSS_BORDER;
import static type.Attributes.ALLOW_PAYMENT_WITHOUT_3DS;

public class P2P_FundingMCPaymenVisa extends BaseRedirect {

    private String requestId;
    private String idAVAL1;
    private String idAVAL2;
    private String idAVAL3;
    private String idAVAL4;


    @BeforeClass
    public void setStatusAttr()  throws IOException{
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("properties/env.properties")) {
            properties.load(input);
        }
        idAVAL1 = properties.getProperty("idAVAL1." + environment);
        idAVAL2 = properties.getProperty("idAVAL2." + environment);
        idAVAL3 = properties.getProperty("idAVAL3." + environment);
        idAVAL4 = properties.getProperty("idAVAL4." + environment);

        setMerchantAtt(Integer.parseInt(idAVAL3), ALLOW_CROSS_BORDER, "false");
        setMerchantAtt(Integer.parseInt(idAVAL4), ALLOW_CROSS_BORDER, "false");
        setMerchantAtt(Integer.parseInt(idAVAL1), ALLOW_CROSS_BORDER, "false");
        setMerchantAtt(Integer.parseInt(idAVAL2), ALLOW_CROSS_BORDER, "false");
        System.out.println("ALLOW_CROSS_BORDER: false");
    }


    @DataProvider(name = "merchantData")
    public Object[][] merchantData() throws IOException {
        return new Object[][]{
                {idAVAL3, MoneyTransferPage3.class},
                {idAVAL4, MoneyTransferPage4.class},
                {idAVAL1, MoneyTransferPage.class},
                {idAVAL2, MoneyTransferPage2.class}


        };
    }

    @Test(dataProvider = "merchantData")
    public void transferFromMCFullToVISA(String merchantId, Class<? extends BasePage> pageClass) throws Exception {
        int merchantIdInt = Integer.parseInt(merchantId);
        BasePage page = pageClass.getDeclaredConstructor(WebDriver.class).newInstance(getDriver());
        System.out.println("Testing with page: " + page.toString());

        MoneyTransferPageUtils moneyTransfer = new MoneyTransferPageUtils();
        requestId = moneyTransfer.domesticTransfer(merchantIdInt, cardMCmt, cardVISArec, (Class<? extends MoneyTransferPage>) pageClass);
        String code = getValueFromMTTran(getRrnFromMTTran(requestId, "F"), "Code");


        // Додатковий код для перевірки транзакції
        List<String> checkTransfer = new MoneyTransferPageUtils().getContentData();
       System.out.println("requestId: " + requestId);

        String requestId = getLastRequestId();
        String rrn_funding = getRrnFromMTTran(requestId, "F");
        String rrn_payment = getRrnFromMTTran(requestId, "P");


        SoftAssert softAssertion = new SoftAssert();
        softAssertion.assertEquals(getValueFromMTTran(rrn_funding, "ECI"), "05", "F_ECI");
        softAssertion.assertEquals(getValueFromMTTran(rrn_funding, "ApprovalCode").length(), 6, "F_ApprovalCode");
        softAssertion.assertEquals(rrn_funding.length(), 12, "RRN_Funding");
        softAssertion.assertEquals(getValueFromMTTran(rrn_funding, "Code"), "000", "F_AuthCode");
        softAssertion.assertEquals(getValueFromMTTran(rrn_funding, "MCC"), "6538", "F_MCC");
        softAssertion.assertEquals(getValueFromMTTran(rrn_funding, "CVResult"), "M", "CVResult");
        softAssertion.assertEquals(getValueFromMTTran(rrn_payment, "ECI"), "07", "F_ECI");
        softAssertion.assertEquals(getValueFromMTTran(rrn_payment, "ApprovalCode").length(), 6, "P_ApprovalCode");
        softAssertion.assertEquals(rrn_payment.length(), 12, "RRN_Payment");
        softAssertion.assertEquals(getValueFromMTTran(rrn_payment, "Code"), "000", "P_AuthCode");
        softAssertion.assertEquals(getValueFromMTTran(rrn_payment, "MCC"), "6538", "P_MCC");
        softAssertion.assertAll();

        System.out.println("Verified:");
        String[] verifiedResponseFromDBFunding = {"ECI", "ApprovalCode", "MCC", "Code", "CVResult", "RRN"};
        String[] verifiedResponseFromDBPayment = {"ECI", "ApprovalCode", "MCC", "Code", "RRN"};
        try {
            verifiedDataMTFromDBFunding(rrn_funding, verifiedResponseFromDBFunding);
            verifiedDataMTFromDBPayment(rrn_payment, verifiedResponseFromDBPayment);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }
}