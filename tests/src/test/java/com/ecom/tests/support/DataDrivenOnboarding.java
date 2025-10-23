package com.ecom.tests.support;

import com.ecom.core.config.ConfigOnboarding;
import com.ecom.tests.model.onboarding.*;
import org.testng.annotations.DataProvider;

public class DataDrivenOnboarding {

  @DataProvider(name = "createMerchant")
  public Object[][] createMerchantData() {
    MerchantData data =
        new MerchantData(
            ConfigOnboarding.getProperty("test.MerchantID"),
            ConfigOnboarding.getProperty("test.TerminalID"),
            ConfigOnboarding.getProperty("test.AccountLogin"),
            ConfigOnboarding.getProperty("test.request.MerchantID"),
            ConfigOnboarding.getProperty("test.request.Mcc"),
            ConfigOnboarding.getProperty("test.request.TerminalID"),
            ConfigOnboarding.getProperty("test.request.Address.Country"),
            ConfigOnboarding.getProperty("test.request.Address.City"),
            ConfigOnboarding.getProperty("test.request.Address.Street"),
            ConfigOnboarding.getProperty("test.request.Name"),
            ConfigOnboarding.getProperty("test.request.BankId"),
            ConfigOnboarding.getProperty("test.request.Currency"),
            ConfigOnboarding.getProperty("test.request.siteUrl"),
            ConfigOnboarding.getProperty("test.request.additional.phone"),
            ConfigOnboarding.getProperty("test.request.additional.contact"),
            ConfigOnboarding.getProperty("test.request.additional.fax"),
            ConfigOnboarding.getProperty("test.request.additional.email"),
            ConfigOnboarding.getProperty("test.request.additional.zip"),
            ConfigOnboarding.getProperty("test.request.additional.remark"),
            ConfigOnboarding.getProperty("test.request.additional.notify_url"),
            ConfigOnboarding.getProperty("test.request.additional.success_url"),
            ConfigOnboarding.getProperty("test.request.additional.failure_url"),
            ConfigOnboarding.getProperty("test.request.additional.closeday_hour"),
            ConfigOnboarding.getProperty("test.request.additional.ipn"),
            ConfigOnboarding.getProperty("test.request.additional.terminal_type_id"),
            ConfigOnboarding.getProperty("test.request.additional.identification"),
            ConfigOnboarding.getProperty("test.request.additional.timeZone"),
            ConfigOnboarding.getProperty("test.request.additional.sms"),
            ConfigOnboarding.getProperty("test.request.additional.viber"),
            ConfigOnboarding.getProperty("test.request.additional.facebook"));
    return new Object[][] {{data}};
  }

  @DataProvider(name = "createMerchantMcPaymentFacilitator")
  public Object[][] createMcPaymentFacilitatorData() {
    MerchantData base = createMerchantData()[0][0] instanceof MerchantData md ? md : null;
    McFacilitatorData data =
        new McFacilitatorData(
            base,
            ConfigOnboarding.getProperty("test.request.additional.mcPaymentFacilitatorId"),
            ConfigOnboarding.getProperty("test.request.additional.mcIndependentSalesOrgId"),
            ConfigOnboarding.getProperty("test.request.additional.mcSubMerchantId"));
    return new Object[][] {{data}};
  }

  @DataProvider(name = "createMerchantVisaPaymentFacilitatorCliche")
  public Object[][] createVisaPaymentFacilitatorClicheData() {
    MerchantData base = createMerchantData()[0][0] instanceof MerchantData md ? md : null;
    VisaFacilitatorData data =
        new VisaFacilitatorData(
            base,
            ConfigOnboarding.getProperty("test.request.additional.visaPaymentFacilitatorId"),
            ConfigOnboarding.getProperty("test.request.additional.visaIndependentSalesOrgId"),
            ConfigOnboarding.getProperty("test.request.additional.visaSubMerchantId"),
            ConfigOnboarding.getProperty("test.request.additional.visaFundingPayment.merchantCode"),
            ConfigOnboarding.getProperty("test.request.additional.visaFundingPayment.terminalId"),
            ConfigOnboarding.getProperty(
                "test.request.additional.visaFundingPayment.merchantName"));
    return new Object[][] {{data}};
  }

  @DataProvider(name = "deleteMerchant")
  public Object[][] deleteMerchantData() {
    DeleteMerchantData data =
        new DeleteMerchantData(
            ConfigOnboarding.getProperty("test.MerchantID"),
            ConfigOnboarding.getProperty("test.TerminalID"),
            ConfigOnboarding.getProperty("test.AccountLogin"),
            ConfigOnboarding.getProperty("test.request.MerchantID"),
            ConfigOnboarding.getProperty("test.request.TerminalID"));
    return new Object[][] {{data}};
  }

  @DataProvider(name = "getMerchant")
  public Object[][] getMerchantData() {
    GetMerchantData data =
        new GetMerchantData(
            ConfigOnboarding.getProperty("test.MerchantID"),
            ConfigOnboarding.getProperty("test.TerminalID"),
            ConfigOnboarding.getProperty("test.AccountLogin"),
            ConfigOnboarding.getProperty("test.request.MerchantID"),
            ConfigOnboarding.getProperty("test.request.TerminalID"));
    return new Object[][] {{data}};
  }

  @DataProvider(name = "getStatus")
  public Object[][] getStatusData() {
    GetStatusData data =
        new GetStatusData(
            ConfigOnboarding.getProperty("test.MerchantID"),
            ConfigOnboarding.getProperty("test.TerminalID"),
            tests.paylink.api.onboarding.CreateMerchant.requestID);
    return new Object[][] {{data}};
  }

  @DataProvider(name = "updateMerchant")
  public Object[][] updateMerchantData() {
    UpdateMerchantData data =
        new UpdateMerchantData(
            ConfigOnboarding.getProperty("test.MerchantID"),
            ConfigOnboarding.getProperty("test.TerminalID"),
            ConfigOnboarding.getProperty("test.AccountLogin"),
            ConfigOnboarding.getProperty("test.request.MerchantID"),
            ConfigOnboarding.getProperty("test.request.TerminalID"),
            ConfigOnboarding.getProperty("test.request.Name_new"));
    return new Object[][] {{data}};
  }

  @DataProvider(name = "updateMerchantFacilitator")
  public Object[][] updateMerchantFacilitatorData() {
    UpdateMerchantData data =
        new UpdateMerchantData(
            ConfigOnboarding.getProperty("test.MerchantID"),
            ConfigOnboarding.getProperty("test.TerminalID"),
            ConfigOnboarding.getProperty("test.AccountLogin"),
            ConfigOnboarding.getProperty("test.request.MerchantID"),
            ConfigOnboarding.getProperty("test.request.TerminalID"),
            ConfigOnboarding.getProperty("test.request.facilitator.Name_new"));
    return new Object[][] {{data}};
  }
}
