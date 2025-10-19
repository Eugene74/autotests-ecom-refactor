/**
 * @author semyvolos_h
 * @date 8/1/2022 9:33 AM
 */
package tests.paylink.api.onboarding;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import com.ecom.tests.support.DataDrivenOnboarding;
import com.ecom.tests.support.DocumentTools;
import com.ecom.tests.support.RequestsOnboarding;
import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import static com.ecom.tests.support.DocumentTools.*;

public class CreateMerchant extends BaseTestOnboarding {
        public static String requestID;

        @Test(dataProvider = "createMerchant", dataProviderClass = DataDrivenOnboarding.class)
        public void createMerchant(String MerchantID,
                                     String TerminalID,
                                     String AccountLogin,
                                     String ReqMerchantID,
                                     String Mcc,
                                     String ReqTerminalID,
                                     String Country,
                                     String City,
                                     String Street,
                                     String Name,
                                     String BankCode,
                                     String Currency,
                                     String siteUrl,
                                     String phone,
                                     String contact,
                                     String fax,
                                     String email,
                                     String zip,
                                     String remark,
                                     String notify_url,
                                     String success_url,
                                     String failure_url,
                                     String closeday_hour,
                                     String ipn,
                                     String terminal_type_id,
                                     String identification,
                                     String timeZone,
                                     String sms,
                                     String viber,
                                     String facebook) {
        Document requestDoc = RequestsOnboarding.createMerchantDocument(MerchantID, TerminalID, AccountLogin, ReqMerchantID, Mcc, ReqTerminalID, Country, City, Street, Name, BankCode, Currency, siteUrl, phone, contact, fax, email, zip, remark, notify_url, success_url, failure_url, closeday_hour, ipn, terminal_type_id, identification, timeZone, sms, viber, facebook);
        String response = RestAssured.given().contentType(ContentType.XML).accept(ContentType.XML).body(DocumentTools.toStringApi(requestDoc)).relaxedHTTPSValidation().when().post(urlCreateMerchant).asString();
        response = printResponseCreateMerchant(convertStringToXmlDocument(response));
        requestID = StringUtils.substringBetween(response, "<RequestID>", "</RequestID>");
        System.out.println("--CREATE MERCHANT--\nRequest:\n" + printRequestCreateMerchant(requestDoc));
        System.out.println("Response:\n" + response);
    }

        @Test(dataProvider = "createMerchantMcPaymentFacilitator", dataProviderClass = DataDrivenOnboarding.class)
        public void mcPaymentFacilitator(String MerchantID,
                           String TerminalID,
                           String AccountLogin,
                           String ReqMerchantID,
                           String Mcc,
                           String ReqTerminalID,
                           String Country,
                           String City,
                           String Street,
                           String Name,
                           String BankCode,
                           String Currency,
                           String siteUrl,
                           String phone,
                           String contact,
                           String fax,
                           String email,
                           String zip,
                           String remark,
                           String notify_url,
                           String success_url,
                           String failure_url,
                           String closeday_hour,
                           String ipn,
                           String terminal_type_id,
                           String identification,
                           String mcPaymentFacilitatorId,
                           String mcIndependentSalesOrgId,
                           String mcSubMerchantId,
                           String timeZone,
                           String sms,
                           String viber,
                           String facebook
        ) {
        Document requestDoc = RequestsOnboarding.createMcPaymentFacilitatorDocument(MerchantID, TerminalID, AccountLogin, ReqMerchantID, Mcc, ReqTerminalID, Country, City, Street, Name, BankCode, Currency, siteUrl, phone, contact, fax, email, zip, remark, notify_url, success_url, failure_url, closeday_hour, ipn, terminal_type_id, identification, mcPaymentFacilitatorId, mcIndependentSalesOrgId, mcSubMerchantId, timeZone, sms, viber, facebook);
        String response = RestAssured.given().contentType(ContentType.XML).accept(ContentType.XML).body(DocumentTools.toStringApi(requestDoc)).relaxedHTTPSValidation().when().post(urlCreateMerchant).asString();
        response = printResponseCreateMcPaymentFacilitator(convertStringToXmlDocument(response));
        requestID = StringUtils.substringBetween(response, "<RequestID>", "</RequestID>");
        System.out.println("--CREATE MERCHANT--\nRequest:\n" + printRequestCreateMcPaymentFacilitator(requestDoc));
        System.out.println("Response:\n" + response);
        }

        @Test(dataProvider = "createMerchantVisaPaymentFacilitatorCliche", dataProviderClass = DataDrivenOnboarding.class)
        public void visaPaymentFacilitatorCliche(String MerchantID,
                       String TerminalID,
                       String AccountLogin,
                       String ReqMerchantID,
                       String Mcc,
                       String ReqTerminalID,
                       String Country,
                       String City,
                       String Street,
                       String Name,
                       String BankCode,
                       String Currency,
                       String siteUrl,
                       String phone,
                       String contact,
                       String fax,
                       String email,
                       String zip,
                       String remark,
                       String notify_url,
                       String success_url,
                       String failure_url,
                       String closeday_hour,
                       String ipn,
                       String terminal_type_id,
                       String identification,
                       String visaPaymentFacilitatorId,
                       String visaIndependentSalesOrgId,
                       String visaSubMerchantId,
                       String merchantCode,
                       String terminalId,
                       String merchantName,
                       String timeZone,
                       String sms,
                       String viber,
                       String facebook
        ){
        Document requestDoc = RequestsOnboarding.createVisaPaymentFacilitatorClicheDocument(MerchantID, TerminalID, AccountLogin, ReqMerchantID, Mcc, ReqTerminalID, Country, City, Street, Name, BankCode, Currency, siteUrl, phone, contact, fax, email, zip, remark, notify_url, success_url, failure_url, closeday_hour, ipn, terminal_type_id, identification, visaPaymentFacilitatorId, visaIndependentSalesOrgId, visaSubMerchantId, merchantCode, terminalId, merchantName, timeZone, sms, viber, facebook);
        String response = RestAssured.given().contentType(ContentType.XML).accept(ContentType.XML).body(DocumentTools.toStringApi(requestDoc)).relaxedHTTPSValidation().when().post(urlCreateMerchant).asString();
        response = printResponseCreateVisaPaymentFacilitatorCliche(convertStringToXmlDocument(response));
        requestID = StringUtils.substringBetween(response, "<RequestID>", "</RequestID>");
        System.out.println("--CREATE MERCHANT--\nRequest:\n" + printRequestCreateVisaPaymentFacilitatorCliche(requestDoc));
        System.out.println("Response:\n" + response);
    }
}