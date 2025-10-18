/**
 * @author semyvolos_h
 * @date 7/29/2022 12:03 PM
 */
package tests.paylink.api.onboarding;

import tests.BaseTest;

public class BaseTestOnboarding extends BaseTest {

    /** URLs */
    protected static String urlGetMerchant = envProperties.getProperty("URLtomee") + "/go/merchants/service";
    protected static String urlStatusMerchant = envProperties.getProperty("URLtomee") + "/go/merchants/status";
    protected static String urlCreateMerchant = envProperties.getProperty("URLtomee") + "/go/merchants/service";
    protected static String urlUpdateMerchant = envProperties.getProperty("URLtomee") + "/go/merchants/service";
    protected static String urlDeleteMerchant = envProperties.getProperty("URLtomee") + "/go/merchants/service";

    /** templates */
    public static final String XML_TEMPLATES_ONBOARDIND_CREATEMERCHANT_PATH = "template/xml/api/onboarding/createMerchant.xml";
    public static final String XML_TEMPLATES_ONBOARDIND_CREATEMCPAYMENTFACILITATOR_PATH = "template/xml/api/onboarding/createMcPaymentFacilitator.xml";
    public static final String XML_TEMPLATES_ONBOARDIND_CREATEVISAPAYMENTFACILITATORCLICHE_PATH = "template/xml/api/onboarding/createVisaPaymentFacilitatorCliche.xml";
    public static final String XML_TEMPLATES_ONBOARDIND_GETMERCHANT_PATH = "template/xml/api/onboarding/getMerchant.xml";
    public static final String XML_TEMPLATES_ONBOARDIND_DELETEMERCHANT_PATH = "template/xml/api/onboarding/deleteMerchant.xml";
    public static final String XML_TEMPLATES_ONBOARDIND_UPDATEMERCHANT_PATH = "template/xml/api/onboarding/updateMerchant.xml";
    public static final String XML_TEMPLATES_ONBOARDIND_GETSTATUS_PATH = "template/xml/api/onboarding/getStatus.xml";
}