package com.ecom.core.xml;


/**
 * Central place for static template locations used by legacy tests.
 */
public final class TemplateCatalog {

  private TemplateCatalog() {
    // utility class
  }
  /** Paylink XML templates. */
  public static final String XML_TEMPLATES_PAY_REQ_PATH = "template/xml/paylink/PayRequest.xml";
  public static final String XML_TEMPLATES_TOKEN_REQ_PATH = "template/xml/paylink/TokenRequest.xml";
  public static final String XML_TEMPLATES_REV_REQ_PATH = "template/xml/paylink/ReversalRequest.xml";
  public static final String XML_TEMPLATES_INSTALLMENT_CHOOSE_PATH = "template/xml/paylink/Installment.xml";
  public static final String XML_TEMPLATES_GET_INSTALLMENT_PATH = "template/xml/paylink/GetInstallmentPlans.xml";
  public static final String XML_TEMPLATES_LOCAL_INSTALLMENT_PATH = "template/xml/paylink/LocalInstallment.xml";
  public static final String XML_TEMPLATES_MPI_ENROLL_REQ_PATH = "template/xml/mpi/enrollmentRequest.xml";
  public static final String XML_TEMPLATES_MPI_AUTH_REQ_PATH = "template/xml/mpi/authorizationRequest.xml";

  /** MDES/VTS XML templates. */
  public static final String XML_TEMPLATES_CREATE_TOKEN_MDES_VTS = "/template/xml/api/mdesvts/create_token.xml";
  public static final String XML_TEMPLATES_REFRESH_TOKEN_MDES_VTS = "/template/xml/api/mdesvts/refresh_token.xml";
  public static final String XML_TEMPLATES_CARD_METADATA_MDES_VTS = "/template/xml/api/mdesvts/card_metadata.xml";
  public static final String XML_TEMPLATES_CARD_CONTENT_MDES_VTS = "/template/xml/api/mdesvts/card_content.xml";
  public static final String XML_TEMPLATES_TOKEN_STATUS_MDES_VTS = "/template/xml/api/mdesvts/token_status.xml";
  public static final String XML_TEMPLATES_TOKEN_SUSPEND_MDES_VTS = "/template/xml/api/mdesvts/token_suspend.xml";
  public static final String XML_TEMPLATES_TOKEN_RESUME_MDES_VTS = "/template/xml/api/mdesvts/token_resume.xml";
  public static final String XML_TEMPLATES_TOKEN_DELETE_MDES_VTS = "/template/xml/api/mdesvts/token_delete.xml";

    /** MDES/VTS JSON templates. */
  public static final String JSON_TEMPLATES_CREATE_TOKEN_MDES_VTS = "/template/json/mdesvts/create_token.json";
  public static final String JSON_TEMPLATES_REFRESH_TOKEN_MDES_VTS = "/template/json/mdesvts/refresh_token.json";
  public static final String JSON_TEMPLATES_CARD_METADATA_MDES_VTS = "/template/json/mdesvts/card_metadata.json";
  public static final String JSON_TEMPLATES_CARD_CONTENT_MDES_VTS = "/template/json/mdesvts/card_content.json";
  public static final String JSON_TEMPLATES_TOKEN_STATUS_MDES_VTS = "/template/json/mdesvts/token_status.json";
  public static final String JSON_TEMPLATES_TOKEN_SUSPEND_MDES_VTS = "/template/json/mdesvts/token_suspend.json";
  public static final String JSON_TEMPLATES_TOKEN_RESUME_MDES_VTS = "/template/json/mdesvts/token_resume.json";
  public static final String JSON_TEMPLATES_TOKEN_DELETE_MDES_VTS = "/template/json/mdesvts/token_delete.json";

  public static final String AUTH_PATH = "template/html/Authorization.html";
  public static final String AUTH_PATH_INSTALLMENT = "template/html/AuthorizationInstallment.html";
  public static final String VERIFY_PATH = "template/html/AccountVerification.html";
}
