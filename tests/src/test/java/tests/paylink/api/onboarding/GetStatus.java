/**
 * @author semyvolos_h
 * @date 8/1/2022 11:02 AM
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

public class GetStatus extends BaseTestOnboarding{

    @Test(dataProvider = "getStatus", dataProviderClass = DataDrivenOnboarding.class)
    public void Test01(String MerchantID, String TerminalID, String requestID) {
        Document requestDoc = RequestsOnboarding.getStatusDocument(MerchantID, TerminalID, requestID);
        String response = RestAssured.given().contentType(ContentType.XML).accept(ContentType.XML).body(DocumentTools.toStringApi(requestDoc)).relaxedHTTPSValidation().when().get(urlStatusMerchant).asString();
        response = printResponseGetStatus(convertStringToXmlDocument(response));
        System.out.println("--GET STATUS--\nRequest:\n" + printRequestGetStatus(requestDoc));
        System.out.println("Response:\n" + response);
    }
}
