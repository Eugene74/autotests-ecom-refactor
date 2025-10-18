/**
 * @author semyvolos_h
 * @date 8/1/2022 11:33 AM
 */
package tests.paylink.api.onboarding;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import com.ecom.tests.support.DataDrivenOnboarding;
import com.ecom.tests.support.DocumentTools;
import com.ecom.tests.support.RequestsOnboarding;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import static com.ecom.tests.support.DocumentTools.*;

public class UpdateMerchant extends BaseTestOnboarding{

    @Test(dataProvider = "updateMerchant", dataProviderClass = DataDrivenOnboarding.class)
    public void Test01(String MerchantID, String TerminalID, String AccountLogin, String ReqMerchantID, String ReqTerminalID, String updField) {
        Document requestDoc = RequestsOnboarding.updateMerchantDocument(MerchantID, TerminalID, AccountLogin, ReqMerchantID, ReqTerminalID, updField);
        String response = RestAssured.given().contentType(ContentType.XML).accept(ContentType.XML).body(DocumentTools.toStringApi(requestDoc)).relaxedHTTPSValidation().when().put(urlUpdateMerchant).asString();
        response = printResponseUpdateMerchant(convertStringToXmlDocument(response));
        System.out.println("--UPDATE MERCHANT--\nRequest:\n" + printRequestUpdateMerchant(requestDoc));
        System.out.println("Response:\n" + response);
    }

    @Test(dataProvider = "updateMerchantFacilitator", dataProviderClass = DataDrivenOnboarding.class)
    public void Test02(String MerchantID, String TerminalID, String AccountLogin, String ReqMerchantID, String ReqTerminalID, String updField) {
        Document requestDoc = RequestsOnboarding.updateMerchantDocument(MerchantID, TerminalID, AccountLogin, ReqMerchantID, ReqTerminalID, updField);
        String response = RestAssured.given().contentType(ContentType.XML).accept(ContentType.XML).body(DocumentTools.toStringApi(requestDoc)).relaxedHTTPSValidation().when().put(urlUpdateMerchant).asString();
        response = printResponseUpdateMerchant(convertStringToXmlDocument(response));
        System.out.println("--UPDATE MERCHANT--\nRequest:\n" + printRequestUpdateMerchant(requestDoc));
        System.out.println("Response:\n" + response);
    }
}
