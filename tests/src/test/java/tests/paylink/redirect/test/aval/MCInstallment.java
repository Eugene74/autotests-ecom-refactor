package tests.paylink.redirect.test.aval;

import methods.RedirectRequest;
import com.ecom.ui.util.Waiters;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseRedirect;
import com.ecom.ui.paylink.redirect.pages.InstallmentChoice;

import static com.ecom.db.JDBCMethods.*;
import static methods.DocumentTools.verifiedDataFromDB;
import static com.ecom.api.type.Attributes.*;
import static com.ecom.api.type.Attributes.ALLOW_INSTALLMENT;

public class MCInstallment extends BaseRedirect {
    @BeforeClass
    public void setParam() {
        setMerchantAtt(id_AVAL1, ALLOW_PAYMENT_WITHOUT_3DS, "true");
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: TRUE");
        setMerchantAtt(id_AVAL1, ALLOW_MASTERCARD_INSTALLMENT, "true");
        System.out.println("ALLOW_MASTERCARD_INSTALLMENT: TRUE");
        setMerchantAtt(id_AVAL1, USE_NEW_FRONT_END, "true");
        System.out.println("USE_NEW_FRONT_END: TRUE");
        setMerchantAtt(id_AVAL1, ALLOW_INSTALLMENT, "true");
        System.out.println("ALLOW_INSTALLMENT: TRUE");
        setMerchantAtt(id_AVAL1, DISABLE_EMAIL_FIELD, "false");
        setMerchantAtt(id_AVAL, MERCHANT_INVOICING_URL, URLInvocing);
        System.out.println("DISABLE_EMAIL_FIELD: FALSE");
    }

    @Test
    public void installmentPay() {
        RedirectRequest pay = new RedirectRequest();
        String orderRedirect = pay.paymentAuthorizationInstallment(cardMCInst, 0, merchantID_aval1, terminalID_aval1);
        pay.usedCVC(cardMCInst);
        System.out.println("Order Redirect: "+ orderRedirect);

        Waiters.sleep(3500); //использовать только на тест среде, задержка для БД
        int tranId = getTranIdByOrder1(orderRedirect);
        System.out.println("Transaction ID: "+ tranId);

        SoftAssert softAssertion = new SoftAssert();
        softAssertion.assertEquals(getValueFromTRAN(tranId, "ECI"), "07", "ECI");
        softAssertion.assertEquals(getValueFromTRAN(tranId, "ApprovalCode").length(), 6, "ApprovalCode");
        softAssertion.assertEquals(getValueFromTRAN(tranId, "Rrn").length(), 12, "Rrn");
        softAssertion.assertEquals(getValueFromTRAN(tranId, "TranCode"), "000", "TranCode");
        softAssertion.assertEquals(getValueFromTRAN(tranId, "CVResult"), "M", "CVResult");
        softAssertion.assertAll();

        System.out.println("Verified:");
        String[] verifiedResponseFromDB = {"ECI", "ApprovalCode", "Rrn", "TranCode", "CVResult"};
        try {
            verifiedDataFromDB(tranId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @AfterClass
    public void setDefaultParam() {
        setMerchantAtt(id_AVAL1, ALLOW_PAYMENT_WITHOUT_3DS, "false");
        System.out.println("ALLOW_PAYMENT_WITHOUT_3DS: FALSE");
        setMerchantAtt(id_AVAL1, ALLOW_MASTERCARD_INSTALLMENT, "false");
        System.out.println("ALLOW_MASTERCARD_INSTALLMENT: FALSE");
        setMerchantAtt(id_AVAL1, USE_NEW_FRONT_END, "false");
        System.out.println("USE_NEW_FRONT_END: FALSE");
        setMerchantAtt(id_AVAL1, ALLOW_INSTALLMENT, "false");
        System.out.println("ALLOW_INSTALLMENT: FALSE");
        setMerchantAtt(id_AVAL1, DISABLE_EMAIL_FIELD, "true");
        System.out.println("DISABLE_EMAIL_FIELD: TRUE");
    }
}
