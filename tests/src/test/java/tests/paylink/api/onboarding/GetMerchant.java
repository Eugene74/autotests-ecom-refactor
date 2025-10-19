/**
 * @author semyvolos_h
 * @date 7/29/2022 14:13 PM
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

public class GetMerchant extends BaseTestOnboarding {

    @Test(dataProvider = "getMerchant", dataProviderClass = DataDrivenOnboarding.class)
    public void Test01(String MerchantID, String TerminalID, String AccountLogin, String ReqMerchantID, String ReqTerminalID) {
        Document requestDoc = RequestsOnboarding.getMerchantDocument(MerchantID, TerminalID, AccountLogin, ReqMerchantID, ReqTerminalID);
        String response = RestAssured.given().contentType(ContentType.XML).accept(ContentType.XML).body(DocumentTools.toStringApi(requestDoc)).relaxedHTTPSValidation().when().get(urlGetMerchant).asString();
        response = printResponseGetMerchant(convertStringToXmlDocument(response));
        System.out.println("--GET MERCHANT--\nRequest:\n" + printRequestGetMerchant(requestDoc));
        System.out.println("Response:\n" + response);
    }
}