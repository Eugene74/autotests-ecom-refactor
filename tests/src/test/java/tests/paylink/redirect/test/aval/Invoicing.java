package tests.paylink.redirect.test.aval;

import com.ecom.db.JDBCMethods;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseTest;
import methods.RedirectRequest;


import static com.ecom.db.JDBCMethods.getTranIdByOrder;
import static com.ecom.db.JDBCMethods.setMerchantAtt;
import static methods.DocumentTools.verifiedDataFromDB;
import static com.ecom.api.type.Attributes.ALLOW_PAYMENT_WITHOUT_3DS;
import static com.ecom.api.type.Attributes.MERCHANT_INVOICING_URL;


public class Invoicing extends BaseTest {

    @BeforeClass
    public void setParam(){
        JDBCMethods.setMerchantAtt(id_AVAL, ALLOW_PAYMENT_WITHOUT_3DS, "true");
        setMerchantAtt(id_AVAL, MERCHANT_INVOICING_URL, URLInvocing);
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
    }

    @Test
    public void authorizationPay() {
        setup();
        RedirectRequest pay = new RedirectRequest();
        String orderRedirect = pay.paymentInvoicing(URL_MERCH, cardVisaRed, id_AVAL);
        pay.usedCVC(cardVisaRed);
        System.out.println("Order: " + orderRedirect);
        exit();

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
