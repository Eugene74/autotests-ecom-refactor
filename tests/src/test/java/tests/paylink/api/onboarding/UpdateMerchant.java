package tests.paylink.api.onboarding;

import static com.ecom.tests.support.DocumentTools.*;

import com.ecom.tests.base.BaseTestMerchantOnboarding;
import com.ecom.tests.model.onboarding.UpdateMerchantData;
import com.ecom.tests.support.DataDrivenOnboarding;
import com.ecom.tests.support.DocumentTools;
import com.ecom.tests.support.RequestsOnboarding;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

/**
 * @author semyvolos_h
 * @date refactored by Eugene (10/22/2025)
 */
public class UpdateMerchant extends BaseTestMerchantOnboarding {

  @Test(dataProvider = "updateMerchant", dataProviderClass = DataDrivenOnboarding.class)
  public void updateMerchant(UpdateMerchantData data) {

    // Формируем XML-запрос
    Document requestDoc =
        RequestsOnboarding.updateMerchantDocument(
            data.merchantID(),
            data.terminalID(),
            data.accountLogin(),
            data.reqMerchantID(),
            data.reqTerminalID(),
            data.updatedField());

    // Отправляем PUT-запрос
    String response =
        RestAssured.given()
            .contentType(ContentType.XML)
            .accept(ContentType.XML)
            .body(DocumentTools.toStringApi(requestDoc))
            .relaxedHTTPSValidation()
            .when()
            .put(urlUpdateMerchant)
            .asString();

    // Обрабатываем и выводим результат
    response = printResponseUpdateMerchant(convertStringToXmlDocument(response));

    System.out.println(
        """
                --UPDATE MERCHANT--
                Request:
                %s
                Response:
                %s
                """
            .formatted(printRequestUpdateMerchant(requestDoc), response));
  }

  @Test(dataProvider = "updateMerchantFacilitator", dataProviderClass = DataDrivenOnboarding.class)
  public void updateMerchantFacilitator(UpdateMerchantData data) {

    // Формируем XML-запрос
    Document requestDoc =
        RequestsOnboarding.updateMerchantDocument(
            data.merchantID(),
            data.terminalID(),
            data.accountLogin(),
            data.reqMerchantID(),
            data.reqTerminalID(),
            data.updatedField());

    // Отправляем PUT-запрос
    String response =
        RestAssured.given()
            .contentType(ContentType.XML)
            .accept(ContentType.XML)
            .body(DocumentTools.toStringApi(requestDoc))
            .relaxedHTTPSValidation()
            .when()
            .put(urlUpdateMerchant)
            .asString();

    // Обрабатываем и выводим результат
    response = printResponseUpdateMerchant(convertStringToXmlDocument(response));

    System.out.println(
        """
                --UPDATE MERCHANT (FACILITATOR)--
                Request:
                %s
                Response:
                %s
                """
            .formatted(printRequestUpdateMerchant(requestDoc), response));
  }
}
