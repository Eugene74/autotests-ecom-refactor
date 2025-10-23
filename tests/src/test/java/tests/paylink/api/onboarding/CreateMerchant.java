package tests.paylink.api.onboarding;

import static com.ecom.tests.support.DocumentTools.*;

import com.ecom.tests.base.BaseTestMerchantOnboarding;
import com.ecom.tests.model.onboarding.*;
import com.ecom.tests.support.*;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

@Slf4j
public class CreateMerchant extends BaseTestMerchantOnboarding {

  public static String requestID;

  @Test(dataProvider = "createMerchant", dataProviderClass = DataDrivenOnboarding.class)
  public void createMerchant(MerchantData data) {
    Document requestDoc =
        RequestsOnboarding.createMerchantDocument(
            data.merchantID(),
            data.terminalID(),
            data.accountLogin(),
            data.reqMerchantID(),
            data.mcc(),
            data.reqTerminalID(),
            data.country(),
            data.city(),
            data.street(),
            data.name(),
            data.bankCode(),
            data.currency(),
            data.siteUrl(),
            data.phone(),
            data.contact(),
            data.fax(),
            data.email(),
            data.zip(),
            data.remark(),
            data.notifyUrl(),
            data.successUrl(),
            data.failureUrl(),
            data.closedayHour(),
            data.ipn(),
            data.terminalTypeId(),
            data.identification(),
            data.timeZone(),
            data.sms(),
            data.viber(),
            data.facebook());

    String response =
        RestAssured.given()
            .contentType(ContentType.XML)
            .accept(ContentType.XML)
            .body(DocumentTools.toStringApi(requestDoc))
            .relaxedHTTPSValidation()
            .when()
            .post(urlCreateMerchant)
            .asString();

    response = printResponseCreateMerchant(convertStringToXmlDocument(response));
    requestID = StringUtils.substringBetween(response, "<RequestID>", "</RequestID>");
    log.info("--CREATE MERCHANT--\nRequest:\n{}", printRequestCreateMerchant(requestDoc));
    log.info("Response:\n{}", response);
  }

  @Test(
      dataProvider = "createMerchantMcPaymentFacilitator",
      dataProviderClass = DataDrivenOnboarding.class)
  public void mcPaymentFacilitator(McFacilitatorData data) {
    MerchantData d = data.base();
    Document requestDoc =
        RequestsOnboarding.createMcPaymentFacilitatorDocument(
            d.merchantID(),
            d.terminalID(),
            d.accountLogin(),
            d.reqMerchantID(),
            d.mcc(),
            d.reqTerminalID(),
            d.country(),
            d.city(),
            d.street(),
            d.name(),
            d.bankCode(),
            d.currency(),
            d.siteUrl(),
            d.phone(),
            d.contact(),
            d.fax(),
            d.email(),
            d.zip(),
            d.remark(),
            d.notifyUrl(),
            d.successUrl(),
            d.failureUrl(),
            d.closedayHour(),
            d.ipn(),
            d.terminalTypeId(),
            d.identification(),
            data.mcPaymentFacilitatorId(),
            data.mcIndependentSalesOrgId(),
            data.mcSubMerchantId(),
            d.timeZone(),
            d.sms(),
            d.viber(),
            d.facebook());

    String response =
        RestAssured.given()
            .contentType(ContentType.XML)
            .accept(ContentType.XML)
            .body(DocumentTools.toStringApi(requestDoc))
            .relaxedHTTPSValidation()
            .when()
            .post(urlCreateMerchant)
            .asString();

    response = printResponseCreateMcPaymentFacilitator(convertStringToXmlDocument(response));
    requestID = StringUtils.substringBetween(response, "<RequestID>", "</RequestID>");
    log.info(
        "--CREATE MERCHANT MC--\nRequest:\n{}", printRequestCreateMcPaymentFacilitator(requestDoc));
    log.info("Response:\n{}", response);
  }

  @Test(
      dataProvider = "createMerchantVisaPaymentFacilitatorCliche",
      dataProviderClass = DataDrivenOnboarding.class)
  public void visaPaymentFacilitatorCliche(VisaFacilitatorData data) {
    MerchantData d = data.base();
    Document requestDoc =
        RequestsOnboarding.createVisaPaymentFacilitatorClicheDocument(
            d.merchantID(),
            d.terminalID(),
            d.accountLogin(),
            d.reqMerchantID(),
            d.mcc(),
            d.reqTerminalID(),
            d.country(),
            d.city(),
            d.street(),
            d.name(),
            d.bankCode(),
            d.currency(),
            d.siteUrl(),
            d.phone(),
            d.contact(),
            d.fax(),
            d.email(),
            d.zip(),
            d.remark(),
            d.notifyUrl(),
            d.successUrl(),
            d.failureUrl(),
            d.closedayHour(),
            d.ipn(),
            d.terminalTypeId(),
            d.identification(),
            data.visaPaymentFacilitatorId(),
            data.visaIndependentSalesOrgId(),
            data.visaSubMerchantId(),
            data.merchantCode(),
            data.terminalId(),
            data.merchantName(),
            d.timeZone(),
            d.sms(),
            d.viber(),
            d.facebook());

    String response =
        RestAssured.given()
            .contentType(ContentType.XML)
            .accept(ContentType.XML)
            .body(DocumentTools.toStringApi(requestDoc))
            .relaxedHTTPSValidation()
            .when()
            .post(urlCreateMerchant)
            .asString();

    response =
        printResponseCreateVisaPaymentFacilitatorCliche(convertStringToXmlDocument(response));
    requestID = StringUtils.substringBetween(response, "<RequestID>", "</RequestID>");
    log.info(
        "--CREATE MERCHANT VISA--\nRequest:\n{}",
        printRequestCreateVisaPaymentFacilitatorCliche(requestDoc));
    log.info("Response:\n{}", response);
  }
}
