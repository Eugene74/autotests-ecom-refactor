/**
 * @author semyvolos_h
 * @date 7/28/2022 15:31 PM
 */
package com.ecom.tests.support;

import static com.ecom.tests.support.DocumentTools.*;

import com.ecom.tests.base.BaseTestMerchantOnboarding;
import org.w3c.dom.Document;

public class RequestsOnboarding {

  public static Document createMerchantDocument(
      String MerchantID,
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
    Document newRequestDoc =
        DocumentTools.readXMLFile(
            BaseTestMerchantOnboarding.XML_TEMPLATES_ONBOARDIND_CREATEMERCHANT_PATH);
    fillCreateMerchant(
        newRequestDoc,
        MerchantID,
        TerminalID,
        AccountLogin,
        ReqMerchantID,
        Mcc,
        ReqTerminalID,
        Country,
        City,
        Street,
        Name,
        BankCode,
        Currency,
        siteUrl,
        phone,
        contact,
        fax,
        email,
        zip,
        remark,
        notify_url,
        success_url,
        failure_url,
        closeday_hour,
        ipn,
        terminal_type_id,
        identification,
        timeZone,
        sms,
        viber,
        facebook);
    return newRequestDoc;
  }

  public static Document createMcPaymentFacilitatorDocument(
      String MerchantID,
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
      String facebook) {
    Document newRequestDoc =
        DocumentTools.readXMLFile(
            BaseTestMerchantOnboarding.XML_TEMPLATES_ONBOARDIND_CREATEMCPAYMENTFACILITATOR_PATH);
    fillCreateMcPaymentFacilitator(
        newRequestDoc,
        MerchantID,
        TerminalID,
        AccountLogin,
        ReqMerchantID,
        Mcc,
        ReqTerminalID,
        Country,
        City,
        Street,
        Name,
        BankCode,
        Currency,
        siteUrl,
        phone,
        contact,
        fax,
        email,
        zip,
        remark,
        notify_url,
        success_url,
        failure_url,
        closeday_hour,
        ipn,
        terminal_type_id,
        identification,
        mcPaymentFacilitatorId,
        mcIndependentSalesOrgId,
        mcSubMerchantId,
        timeZone,
        sms,
        viber,
        facebook);
    return newRequestDoc;
  }

  public static Document createVisaPaymentFacilitatorClicheDocument(
      String MerchantID,
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
      String facebook) {
    Document newRequestDoc =
        DocumentTools.readXMLFile(
            BaseTestMerchantOnboarding
                .XML_TEMPLATES_ONBOARDIND_CREATEVISAPAYMENTFACILITATORCLICHE_PATH);
    fillcreateVisaPaymentFacilitatorCliche(
        newRequestDoc,
        MerchantID,
        TerminalID,
        AccountLogin,
        ReqMerchantID,
        Mcc,
        ReqTerminalID,
        Country,
        City,
        Street,
        Name,
        BankCode,
        Currency,
        siteUrl,
        phone,
        contact,
        fax,
        email,
        zip,
        remark,
        notify_url,
        success_url,
        failure_url,
        closeday_hour,
        ipn,
        terminal_type_id,
        identification,
        visaPaymentFacilitatorId,
        visaIndependentSalesOrgId,
        visaSubMerchantId,
        merchantCode,
        terminalId,
        merchantName,
        timeZone,
        sms,
        viber,
        facebook);
    return newRequestDoc;
  }

  public static Document getMerchantDocument(
      String MerchantID,
      String TerminalID,
      String AccountLogin,
      String ReqMerchantID,
      String ReqTerminalID) {
    Document newRequestDoc =
        DocumentTools.readXMLFile(
            BaseTestMerchantOnboarding.XML_TEMPLATES_ONBOARDIND_GETMERCHANT_PATH);
    fillGetMerchant(
        newRequestDoc, MerchantID, TerminalID, AccountLogin, ReqMerchantID, ReqTerminalID);
    return newRequestDoc;
  }

  public static Document deleteMerchantDocument(
      String MerchantID,
      String TerminalID,
      String AccountLogin,
      String ReqMerchantID,
      String ReqTerminalID) {
    Document newRequestDoc =
        DocumentTools.readXMLFile(
            BaseTestMerchantOnboarding.XML_TEMPLATES_ONBOARDIND_DELETEMERCHANT_PATH);
    fillDeleteMerchant(
        newRequestDoc, MerchantID, TerminalID, AccountLogin, ReqMerchantID, ReqTerminalID);
    return newRequestDoc;
  }

  public static Document updateMerchantDocument(
      String MerchantID,
      String TerminalID,
      String AccountLogin,
      String ReqMerchantID,
      String ReqTerminalID,
      String updField) {
    Document newRequestDoc =
        DocumentTools.readXMLFile(
            BaseTestMerchantOnboarding.XML_TEMPLATES_ONBOARDIND_UPDATEMERCHANT_PATH);
    fillUpdateMerchant(
        newRequestDoc,
        MerchantID,
        TerminalID,
        AccountLogin,
        ReqMerchantID,
        ReqTerminalID,
        updField);
    return newRequestDoc;
  }

  public static Document getStatusDocument(String MerchantID, String TerminalID, String requestID) {
    Document newRequestDoc =
        DocumentTools.readXMLFile(
            BaseTestMerchantOnboarding.XML_TEMPLATES_ONBOARDIND_GETSTATUS_PATH);
    fillGetStatus(newRequestDoc, MerchantID, TerminalID, requestID);
    return newRequestDoc;
  }
}
