package tests.paylink.redirect.test.aval;

import com.ecom.db.JDBCMethods;
import com.ecom.tests.base.BaseUiTest;
import com.ecom.tests.steps.InvoiceSteps;
import com.ecom.tests.steps.PaymentSteps;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static com.ecom.api.type.Attributes.ALLOW_PAYMENT_WITHOUT_3DS;
import static com.ecom.api.type.Attributes.MERCHANT_INVOICING_URL;
import static com.ecom.core.config.CardConfig.cardVisaRed;
import static com.ecom.core.config.EnvData.URLInvocing;
import static com.ecom.core.config.EnvData.URL_MERCH;
import static com.ecom.core.config.EnvData.id_AVAL;
import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.setMerchantAtt;
import static com.ecom.tests.support.DocumentTools.verifiedDataFromDB;


public class Invoicing extends BaseUiTest {

    @BeforeClass
    public void setParam(){
        JDBCMethods.setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "true");
        setMerchantAtt(id_AVAL, MERCHANT_INVOICING_URL, URLInvocing);
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
    }

    @Test
    public void authorizationPay() {
        String orderRedirect = new InvoiceSteps(driver).payInvoice(URL_MERCH, cardVisaRed, id_AVAL);
        new PaymentSteps(driver).usedCVC(cardVisaRed);
        System.out.println("Order: " + orderRedirect);

        int tranID = getTranIdByOrder(orderRedirect);

        SoftAssert softAssertion= new SoftAssert();
        softAssertion.assertEquals(JDBCMethods.getValueFromTRAN(tranID, "ECI"),"07", "ECI");
        softAssertion.assertEquals(JDBCMethods.getValueFromTRAN(tranID, "ApprovalCode").length(),6, "ApprovalCode");
        softAssertion.assertEquals(JDBCMethods.getValueFromTRAN(tranID, "Rrn").length(),12, "Rrn");
        softAssertion.assertEquals(JDBCMethods.getValueFromTRAN(tranID, "TranCode"),"000", "TranCode");
        softAssertion.assertEquals(JDBCMethods.getValueFromTRAN(tranID, "CVResult"),"M", "CVResult");
        softAssertion.assertAll();

        System.out.println("Verified:");
        String [] verifiedResponseFromDB = {"ECI","ApprovalCode", "Rrn", "TranCode", "CVResult"};
        try{
            verifiedDataFromDB(tranID, verifiedResponseFromDB);}
        catch (Exception e) {
            System.out.println("TEST FAILED");
        }

    }

    @AfterClass
    public void setDefaultParam(){
        JDBCMethods.setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "false");
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: FALSE");
    }
}
