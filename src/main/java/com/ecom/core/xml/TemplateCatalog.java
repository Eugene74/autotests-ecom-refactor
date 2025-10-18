package com.ecom.core.xml;


/**
 * Central place for static template locations used by legacy tests.
 */
public final class TemplateCatalog {

  private TemplateCatalog() {
    // utility class
  }

  public static final String PAY_REQUEST = "./src/main/resources/template/xml/paylink/PayRequest.xml";
  public static final String TOKEN_REQUEST = "./src/main/resources/template/xml/paylink/TokenRequest.xml";
  public static final String REVERSAL_REQUEST = "./src/main/resources/template/xml/paylink/ReversalRequest.xml";
  public static final String INSTALLMENT_CHOOSE = "./src/main/resources/template/xml/paylink/Installment.xml";
  public static final String GET_INSTALLMENT_PLANS =
      "./src/main/resources/template/xml/paylink/GetInstallmentPlans.xml";
  public static final String LOCAL_INSTALLMENT =
      "./src/main/resources/template/xml/paylink/LocalInstallment.xml";
  public static final String MPI_ENROLLMENT =
      "./src/main/resources/template/xml/mpi/enrollmentRequest.xml";
  public static final String MPI_AUTHORIZATION =
      "./src/main/resources/template/xml/mpi/authorizationRequest.xml";
  public static final String MDES_VTS_CREATE_TOKEN = "/template/xml/api/mdesvts/create_token.xml";
  public static final String MDES_VTS_REFRESH_TOKEN = "/template/xml/api/mdesvts/refresh_token.xml";
  public static final String MDES_VTS_CARD_METADATA = "/template/xml/api/mdesvts/card_metadata.xml";
  public static final String MDES_VTS_CARD_CONTENT = "/template/xml/api/mdesvts/card_content.xml";
  public static final String MDES_VTS_TOKEN_STATUS = "/template/xml/api/mdesvts/token_status.xml";
  public static final String MDES_VTS_TOKEN_SUSPEND = "/template/xml/api/mdesvts/token_suspend.xml";
  public static final String MDES_VTS_TOKEN_RESUME = "/template/xml/api/mdesvts/token_resume.xml";
  public static final String MDES_VTS_TOKEN_DELETE = "/template/xml/api/mdesvts/token_delete.xml";

  public static final String HTML_AUTHORIZATION = "./src/main/resources/template/html/Authorization.html";
  public static final String HTML_AUTHORIZATION_INSTALLMENT =
      "./src/main/resources/template/html/AuthorizationInstallment.html";
  public static final String HTML_ACCOUNT_VERIFICATION =
      "./src/main/resources/template/html/AccountVerification.html";

  public static final String JSON_MDES_VTS_CREATE_TOKEN = "/template/json/mdesvts/create_token.json";
  public static final String JSON_MDES_VTS_REFRESH_TOKEN = "/template/json/mdesvts/refresh_token.json";
  public static final String JSON_MDES_VTS_CARD_METADATA = "/template/json/mdesvts/card_metadata.json";
  public static final String JSON_MDES_VTS_CARD_CONTENT = "/template/json/mdesvts/card_content.json";
  public static final String JSON_MDES_VTS_TOKEN_STATUS = "/template/json/mdesvts/token_status.json";
  public static final String JSON_MDES_VTS_TOKEN_SUSPEND = "/template/json/mdesvts/token_suspend.json";
  public static final String JSON_MDES_VTS_TOKEN_RESUME = "/template/json/mdesvts/token_resume.json";
  public static final String JSON_MDES_VTS_TOKEN_DELETE = "/template/json/mdesvts/token_delete.json";
}
