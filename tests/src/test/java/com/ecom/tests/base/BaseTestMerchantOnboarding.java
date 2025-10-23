/**
 * @author semyvolos_h
 * @date 7/29/2022 12:03 PM
 */
package com.ecom.tests.base;

import static com.ecom.core.config.EnvData.URL_TOMEE;

public class BaseTestMerchantOnboarding {

  /** URLs */
  protected static String urlGetMerchant = URL_TOMEE + "/go/merchants/service";

  protected static String urlStatusMerchant = URL_TOMEE + "/go/merchants/status";
  protected static String urlCreateMerchant = URL_TOMEE + "/go/merchants/service";
  protected static String urlUpdateMerchant = URL_TOMEE + "/go/merchants/service";
  protected static String urlDeleteMerchant = URL_TOMEE + "/go/merchants/service";

  /** templates */
  public static final String XML_TEMPLATES_ONBOARDIND_CREATEMERCHANT_PATH =
      "template/xml/api/onboarding/createMerchant.xml";

  public static final String XML_TEMPLATES_ONBOARDIND_CREATEMCPAYMENTFACILITATOR_PATH =
      "template/xml/api/onboarding/createMcPaymentFacilitator.xml";
  public static final String XML_TEMPLATES_ONBOARDIND_CREATEVISAPAYMENTFACILITATORCLICHE_PATH =
      "template/xml/api/onboarding/createVisaPaymentFacilitatorCliche.xml";
  public static final String XML_TEMPLATES_ONBOARDIND_GETMERCHANT_PATH =
      "template/xml/api/onboarding/getMerchant.xml";
  public static final String XML_TEMPLATES_ONBOARDIND_DELETEMERCHANT_PATH =
      "template/xml/api/onboarding/deleteMerchant.xml";
  public static final String XML_TEMPLATES_ONBOARDIND_UPDATEMERCHANT_PATH =
      "template/xml/api/onboarding/updateMerchant.xml";
  public static final String XML_TEMPLATES_ONBOARDIND_GETSTATUS_PATH =
      "template/xml/api/onboarding/getStatus.xml";
}
