/**
 * @author semyvolos_h
 * @date 7/28/2022 17:17 PM
 */
package methods;

import DataDriven.ConfigOnboarding;
import org.testng.annotations.DataProvider;

import static tests.paylink.api.onboarding.CreateMerchant.requestID;

public class DataDrivenOnboarding{

    @DataProvider(name = "createMerchant")
    public Object[][] createMerchantData(){
        Object[][] data = new Object[1][1];
        data[0] = new Object[]{
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
                ConfigOnboarding.getProperty("test.request.additional.facebook")
        };
        return data;
    }

    @DataProvider(name = "createMerchantMcPaymentFacilitator")
    public Object[][] createMcPaymentFacilitatorData(){
        Object[][] data = new Object[1][1];
        data[0] = new Object[]{
                ConfigOnboarding.getProperty("test.MerchantID"),
                ConfigOnboarding.getProperty("test.TerminalID"),
                ConfigOnboarding.getProperty("test.AccountLogin"),
                ConfigOnboarding.getProperty("test.request.MerchantID"),
                ConfigOnboarding.getProperty("test.request.Mcc"),
                ConfigOnboarding.getProperty("test.request.TerminalID"),
                ConfigOnboarding.getProperty("test.request.Address.Country"),
                ConfigOnboarding.getProperty("test.request.Address.City"),
                ConfigOnboarding.getProperty("test.request.Address.Street"),
                ConfigOnboarding.getProperty("test.request.facilitator.Name"),
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
                ConfigOnboarding.getProperty("test.request.additional.mcPaymentFacilitatorId"),
                ConfigOnboarding.getProperty("test.request.additional.mcIndependentSalesOrgId"),
                ConfigOnboarding.getProperty("test.request.additional.mcSubMerchantId"),
                ConfigOnboarding.getProperty("test.request.additional.timeZone"),
                ConfigOnboarding.getProperty("test.request.additional.sms"),
                ConfigOnboarding.getProperty("test.request.additional.viber"),
                ConfigOnboarding.getProperty("test.request.additional.facebook"),
        };
        return data;
    }

    @DataProvider(name = "createMerchantVisaPaymentFacilitatorCliche")
    public Object[][] createVisaPaymentFacilitatorClicheData(){
        Object[][] data = new Object[1][1];
        data[0] = new Object[]{
                ConfigOnboarding.getProperty("test.MerchantID"),
                ConfigOnboarding.getProperty("test.TerminalID"),
                ConfigOnboarding.getProperty("test.AccountLogin"),
                ConfigOnboarding.getProperty("test.request.MerchantID"),
                ConfigOnboarding.getProperty("test.request.Mcc"),
                ConfigOnboarding.getProperty("test.request.TerminalID"),
                ConfigOnboarding.getProperty("test.request.Address.Country"),
                ConfigOnboarding.getProperty("test.request.Address.City"),
                ConfigOnboarding.getProperty("test.request.Address.Street"),
                ConfigOnboarding.getProperty("test.request.facilitator.Name"),
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
                ConfigOnboarding.getProperty("test.request.additional.visaPaymentFacilitatorId"),
                ConfigOnboarding.getProperty("test.request.additional.visaIndependentSalesOrgId"),
                ConfigOnboarding.getProperty("test.request.additional.visaSubMerchantId"),
                ConfigOnboarding.getProperty("test.request.additional.visaFundingPayment.merchantCode"),
                ConfigOnboarding.getProperty("test.request.additional.visaFundingPayment.terminalId"),
                ConfigOnboarding.getProperty("test.request.additional.visaFundingPayment.merchantName"),
                ConfigOnboarding.getProperty("test.request.additional.timeZone"),
                ConfigOnboarding.getProperty("test.request.additional.sms"),
                ConfigOnboarding.getProperty("test.request.additional.viber"),
                ConfigOnboarding.getProperty("test.request.additional.facebook")
        };
        return data;
    }

    @DataProvider(name = "getMerchantStatus")
    public Object[][] getMerchantStatusData(){
        Object[][] data = new Object[1][1];
        data[0] = new Object[]{ConfigOnboarding.getProperty("test.MerchantID"), ConfigOnboarding.getProperty("test.TerminalID"), ConfigOnboarding.getProperty("test.AccountLogin"), ConfigOnboarding.getProperty("test.request.MerchantID"), ConfigOnboarding.getProperty("test.request.TerminalID")};
        return data;
    }

    @DataProvider(name = "getMerchant")
    public Object[][] getMerchantData(){
        Object[][] data = new Object[1][1];
        data[0] = new Object[]{ConfigOnboarding.getProperty("test.MerchantID"), ConfigOnboarding.getProperty("test.TerminalID"), ConfigOnboarding.getProperty("test.AccountLogin"), ConfigOnboarding.getProperty("test.request.MerchantID"), ConfigOnboarding.getProperty("test.request.TerminalID")};
        return data;
    }

    @DataProvider(name = "updateMerchant")
    public Object[][] updateMerchantData(){
        Object[][] data = new Object[1][1];
        data[0] = new Object[]{ConfigOnboarding.getProperty("test.MerchantID"), ConfigOnboarding.getProperty("test.TerminalID"), ConfigOnboarding.getProperty("test.AccountLogin"), ConfigOnboarding.getProperty("test.request.MerchantID"), ConfigOnboarding.getProperty("test.request.TerminalID"), ConfigOnboarding.getProperty("test.request.Name_new")};
        return data;
    }

    @DataProvider(name = "updateMerchantFacilitator")
    public Object[][] updateMerchantFacilitatorData(){
        Object[][] data = new Object[1][1];
        data[0] = new Object[]{ConfigOnboarding.getProperty("test.MerchantID"), ConfigOnboarding.getProperty("test.TerminalID"), ConfigOnboarding.getProperty("test.AccountLogin"), ConfigOnboarding.getProperty("test.request.MerchantID"), ConfigOnboarding.getProperty("test.request.TerminalID"), ConfigOnboarding.getProperty("test.request.facilitator.Name_new")};
        return data;
    }

    @DataProvider(name = "getStatus")
    public Object[][] getStatusData(){
        Object[][] data = new Object[1][1];
        data[0] = new Object[]{ConfigOnboarding.getProperty("test.MerchantID"), ConfigOnboarding.getProperty("test.TerminalID"), requestID};
        return data;
    }

    @DataProvider(name = "deleteMerchant")
    public Object[][] deleteMerchantData(){
        Object[][] data = new Object[1][1];
        data[0] = new Object[]{ConfigOnboarding.getProperty("test.MerchantID"), ConfigOnboarding.getProperty("test.TerminalID"), ConfigOnboarding.getProperty("test.AccountLogin"), ConfigOnboarding.getProperty("test.request.MerchantID"), ConfigOnboarding.getProperty("test.request.TerminalID")};
        return data;
    }
}