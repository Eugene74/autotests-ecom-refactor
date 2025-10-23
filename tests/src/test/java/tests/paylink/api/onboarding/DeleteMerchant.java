package tests.paylink.api.onboarding;

import com.ecom.tests.base.BaseTestMerchantOnboarding;
import com.ecom.tests.model.onboarding.DeleteMerchantData;
import com.ecom.tests.support.DataDrivenOnboarding;
import com.ecom.tests.support.DocumentTools;
import com.ecom.tests.support.RequestsOnboarding;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import static com.ecom.tests.support.DocumentTools.*;
@Slf4j
public class DeleteMerchant extends BaseTestMerchantOnboarding {

    @Test(dataProvider = "deleteMerchant", dataProviderClass = DataDrivenOnboarding.class)
    public void deleteMerchant(DeleteMerchantData data) {
        Document requestDoc = RequestsOnboarding.deleteMerchantDocument(
                data.merchantID(),
                data.terminalID(),
                data.accountLogin(),
                data.reqMerchantID(),
                data.reqTerminalID()
        );

        String response = RestAssured.given()
                .contentType(ContentType.XML)
                .accept(ContentType.XML)
                .body(DocumentTools.toStringApi(requestDoc))
                .relaxedHTTPSValidation()
                .when()
                .delete(urlDeleteMerchant)
                .asString();

        response = printResponseDeleteMerchant(convertStringToXmlDocument(response));

        log.info("--DELETE MERCHANT--");
        log.info("Request:\n{}", printRequestDeleteMerchant(requestDoc));
        log.info("Response:\n{}", response);
    }
}
