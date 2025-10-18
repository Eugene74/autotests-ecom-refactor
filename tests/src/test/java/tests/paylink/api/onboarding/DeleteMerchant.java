/**
 * @author semyvolos_h
 * @date 7/29/2022 17:08 PM
 */
package tests.paylink.api.onboarding;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import methods.DataDrivenOnboarding;
import methods.DocumentTools;
import methods.RequestsOnboarding;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import static methods.DocumentTools.*;

public class DeleteMerchant extends BaseTestOnboarding{

    @Test(dataProvider = "deleteMerchant", dataProviderClass = DataDrivenOnboarding.class)
    public void Test01(String MerchantID, String TerminalID, String AccountLogin, String ReqMerchantID, String ReqTerminalID) {
        Document requestDoc = RequestsOnboarding.deleteMerchantDocument(MerchantID, TerminalID, AccountLogin, ReqMerchantID, ReqTerminalID);
        String response = RestAssured.given().contentType(ContentType.XML).accept(ContentType.XML).body(DocumentTools.toStringApi(requestDoc)).relaxedHTTPSValidation().when().delete(urlDeleteMerchant).asString();
        response = printResponseDeleteMerchant(convertStringToXmlDocument(response));
        System.out.println("--DELETE MERCHANT--\nRequest:\n " + printRequestDeleteMerchant(requestDoc));
        System.out.println("Response:\n" + response);
    }
}