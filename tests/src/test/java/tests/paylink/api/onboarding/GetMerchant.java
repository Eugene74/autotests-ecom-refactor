package tests.paylink.api.onboarding;

import com.ecom.tests.base.BaseTestMerchantOnboarding;
import com.ecom.tests.model.onboarding.GetMerchantData;
import com.ecom.tests.support.DataDrivenOnboarding;
import com.ecom.tests.support.DocumentTools;
import com.ecom.tests.support.RequestsOnboarding;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import static com.ecom.tests.support.DocumentTools.*;

public class GetMerchant extends BaseTestMerchantOnboarding {

    @Test(dataProvider = "getMerchant", dataProviderClass = DataDrivenOnboarding.class)
    public void getMerchant(GetMerchantData data) {
        // Формируем XML-запрос
        Document requestDoc = RequestsOnboarding.getMerchantDocument(
                data.merchantID(),
                data.terminalID(),
                data.accountLogin(),
                data.reqMerchantID(),
                data.reqTerminalID()
        );
        // Отправляем GET-запрос
        String response = RestAssured.given()
                .contentType(ContentType.XML)
                .accept(ContentType.XML)
                .body(DocumentTools.toStringApi(requestDoc))
                .relaxedHTTPSValidation()
                .when()
                .get(urlGetMerchant)
                .asString();
        // Обрабатываем и выводим результат
        response = printResponseGetMerchant(convertStringToXmlDocument(response));

        System.out.println("""
                --GET MERCHANT--
                Request:
                %s
                Response:
                %s
                """.formatted(printRequestGetMerchant(requestDoc), response));
    }
}
