package tests.paylink.api.onboarding;

import com.ecom.tests.base.BaseTestMerchantOnboarding;
import com.ecom.tests.model.onboarding.GetStatusData;
import com.ecom.tests.support.DataDrivenOnboarding;
import com.ecom.tests.support.DocumentTools;
import com.ecom.tests.support.RequestsOnboarding;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import static com.ecom.tests.support.DocumentTools.convertStringToXmlDocument;
import static com.ecom.tests.support.DocumentTools.printRequestGetStatus;
import static com.ecom.tests.support.DocumentTools.printResponseGetStatus;

public class GetStatus extends BaseTestMerchantOnboarding {

    @Test(dataProvider = "getStatus", dataProviderClass = DataDrivenOnboarding.class)
    public void getStatus(GetStatusData data) {

        // Формируем XML-запрос
        Document requestDoc = RequestsOnboarding.getStatusDocument(
                data.merchantID(),
                data.terminalID(),
                data.requestID()
        );

        // Отправляем GET-запрос
        String response = RestAssured.given()
                .contentType(ContentType.XML)
                .accept(ContentType.XML)
                .body(DocumentTools.toStringApi(requestDoc))
                .relaxedHTTPSValidation()
                .when()
                .get(urlStatusMerchant)
                .asString();

        // Обрабатываем и выводим результат
        response = printResponseGetStatus(convertStringToXmlDocument(response));

        System.out.println("""
                --GET STATUS--
                Request:
                %s
                Response:
                %s
                """.formatted(printRequestGetStatus(requestDoc), response));
    }
}
