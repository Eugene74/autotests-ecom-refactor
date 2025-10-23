package com.ecom.core.config;

public final class EnvData {
  private EnvData() {
    // Utility class: prevent instantiation
  }

  private static final PropertiesManager PROPERTIES = PropertiesManager.getInstance();
  public static final String ENVIRONMENT = PROPERTIES.getEnvironment();
  public static final String LOGIN = PROPERTIES.getEnv("login");
  public static final String PASSWORD = PROPERTIES.getEnv("password");
  public static final String URL_MAIL_DEV =
      java.lang.String.format(PROPERTIES.getEnv("base.urlmaildev"), ENVIRONMENT);
  public static final String URL_PIPELINE =
      java.lang.String.format(PROPERTIES.getEnv("base.urlpipelines"), ENVIRONMENT);
  public static final String URL_KAFKA =
      java.lang.String.format(PROPERTIES.getEnv("base.urlkafka"), ENVIRONMENT);
  public static final String PIPELINE_NAME =
      PROPERTIES.getEnv("URLtomee").split("-tomee")[0].replace("https://", "").toLowerCase();

  public static final String PIPELINE_BASE = PROPERTIES.getEnv("base.urlpipelines");
  public static boolean IS_HEADLESS = PROPERTIES.isHeadless();
  public static final String BROWSER = PROPERTIES.getEnv("browser");

  public static final String URL_TOMCAT = PROPERTIES.getEnv("URLtomcat");
  public static final String URL_MT_TRAN = URL_TOMCAT + "/mt/tran";
  public static final String UR_LP_2_P = URL_TOMCAT + "/mt/p2p/init/";
  public static final String URL_DASHBOARD = URL_TOMCAT + "/dashboard/login";
  public static final String URL_FR = URL_TOMCAT + "/mt/public/fast-refund/doFastRefund";
  public static final String URL_MT_REV = URL_TOMCAT + "/mt/reversal";
  public static final String URL_MT_ZONE = URL_TOMCAT + "/mt/card_zone";
  public static final String URL_MT_STATUS = URL_TOMCAT + "/mt/status";
  public static final String URL_MT_MERSH = URL_TOMCAT + "/mt/merchant/login";
  public static final String URL_ALIAS_1_CREATE = URL_TOMCAT + "/alias/create";
  public static final String URL_ALIAS_1_UPDATE = URL_TOMCAT + "/alias/update";
  public static final String URL_ALIAS_1_GET = URL_TOMCAT + "/alias/get";
  public static final String URL_ALIAS_1_RESOLVE = URL_TOMCAT + "/alias/resolve";
  public static final String URL_ALIAS_1_INGUIRY = URL_TOMCAT + "/alias/inquiry";
  public static final String URL_ALIAS_1_DELETE = URL_TOMCAT + "/alias/delete";
  public static final String URL_ALIAS_2_CREATE = URL_TOMCAT + "/alias/v2/create";
  public static final String URL_ALIAS_2_GET = URL_TOMCAT + "/alias/v2/get";
  public static final String URL_ALIAS_2_GET_ALIAS_ID = URL_TOMCAT + "/alias/v2/getAliasId";
  public static final String URL_ALIAS_2_GET_BY_EXTERNAL_ID =
      URL_TOMCAT + "/alias/v2/getByExternalId";
  public static final String URL_ALIAS_2_INQUIRY = URL_TOMCAT + "/alias/v2/inquiry";
  public static final String URL_ALIAS_2_RESOLVE = URL_TOMCAT + "/alias/v2/resolve";
  public static final String URL_ALIAS_2_UPDATE = URL_TOMCAT + "/alias/v2/update";
  public static final String URL_ALIAS_2_UPDATE_STATUS = URL_TOMCAT + "/alias/v2/updateStatus";
  public static final String URL_ALIAS_2_CREATE_PAYMENT_CRED =
      URL_TOMCAT + "/alias/v2/createPaymentCredential";
  public static final String URL_ALIAS_2_GET_PAYMENT_CRED =
      URL_TOMCAT + "/alias/v2/getPaymentCredential";
  public static final String URL_ALIAS_2_GET_ALL_PAYMENT_CRED =
      URL_TOMCAT + "/alias/v2/getAllPaymentCredentials";
  public static final String URL_ALIAS_2_DELETE_PAYMENT_CRED =
      URL_TOMCAT + "/alias/v2/deletePaymentCredential";
  public static final String URL_ALIAS_2_DELETE =
      URL_TOMCAT + "/alias/v2/delete"; // Додано шлях для запиту DeleteAlias;
  public static final String URL_BIN_INFO = URL_TOMCAT + "/infopoint/api/v1/getBinInfo";

  public static final String URL_TOMEE = PROPERTIES.getEnv("URLtomee");
  public static final String URL_INVOCING = URL_TOMEE + "/go";
  public static final String TERM_URL = URL_TOMEE + "/go/enter";
  public static final String ACTIVE_SERVER_URL = URL_TOMEE + "/paylink/astest";
  public static final String CALLBACK_URL = URL_TOMEE + "/go/3ds";
  public static final String URL_REDIRECT = URL_TOMEE + "/go/pay";
  public static final String URL_REDIRECT_PAY = URL_TOMEE + "/go/pay";
  public static final String URL_REDIRECT_CAPTURE = URL_TOMEE + "/go/capture";
  public static final String URL_MDES_VTS_CREATE_TOKEN = URL_TOMEE + "/go/token/create";
  public static final String URL_MDES_VTS_REFRESH_TOKEN = URL_TOMEE + "/go/token/refresh";
  public static final String URL_MDES_VTS_CARD_METADATA = URL_TOMEE + "/go/card/metadata";
  public static final String URL_MDES_VTS_CARD_CONTENT = URL_TOMEE + "/go/card/content";
  public static final String URL_MDES_VTS_TOKEN_STATUS = URL_TOMEE + "/go/token/status";
  public static final String URL_MDES_VTS_TOKEN_SUSPEND = URL_TOMEE + "/go/token/suspend";
  public static final String URL_MDES_VTS_TOKEN_RESUME = URL_TOMEE + "/go/token/resume";
  public static final String URL_MDES_VTS_TOKEN_DELETE = URL_TOMEE + "/go/token/delete";
  public static final String URL_ECG = URL_TOMEE + "/paylink/extranet/index.jsp";
  public static final String URL_MERCH = URL_TOMEE + "/go/merchant/do?action=trans";
  public static final String URL = URL_TOMEE + "/go/service/02";
  public static final String URL_JSON_TOKEN = URL_TOMEE + "/go/payByToken";
  public static final String URL_INST = URL_TOMEE + "/go/service/installments";

  public static final Integer ID_AVAL = Integer.valueOf(PROPERTIES.getEnv("idAVAL." + ENVIRONMENT));
  public static final String MERCHANT_ID_AVAL = PROPERTIES.getEnv("MerchantID_AVAL");
  public static final String TERMINAL_ID_AVAL = PROPERTIES.getEnv("TerminalID_AVAL");

  public static final Integer ID_AVAL_1 =
      Integer.valueOf(PROPERTIES.getEnv("idAVAL1." + ENVIRONMENT));
  public static final String MERCHANT_ID_AVAL_1 = PROPERTIES.getEnv("MerchantID_AVAL1");
  public static final String TERMINAL_ID_AVAL_1 = PROPERTIES.getEnv("TerminalID_AVAL1");

  public static final Integer ID_AVAL_2 =
      Integer.valueOf(PROPERTIES.getEnv("idAVAL2." + ENVIRONMENT));
  public static final String MERCHANT_ID_AVAL_2 = PROPERTIES.getEnv("MerchantID_AVAL2");
  public static final String TERMINAL_ID_AVAL_2 = PROPERTIES.getEnv("TerminalID_AVAL2");

  public static final Integer ID_AVAL_3 =
      Integer.valueOf(PROPERTIES.getEnv("idAVAL3." + ENVIRONMENT));
  public static final String MERCHANT_ID_AVAL_3 = PROPERTIES.getEnv("MerchantID_AVAL3");
  public static final String TERMINAL_ID_AVAL_3 = PROPERTIES.getEnv("TerminalID_AVAL3");

  public static final Integer ID_AVAL_4 =
      Integer.valueOf(PROPERTIES.getEnv("idAVAL4." + ENVIRONMENT));
  public static final String MERCHANT_ID_AVAL_4 = PROPERTIES.getEnv("MerchantID_AVAL4");
  public static final String TERMINAL_ID_AVAL_4 = PROPERTIES.getEnv("TerminalID_AVAL4");

  public static final Integer ID_AVAL_5 =
      Integer.valueOf(PROPERTIES.getEnv("idAVAL5." + ENVIRONMENT));
  public static final String MERCHANT_ID_AVAL_5 = PROPERTIES.getEnv("MerchantID_AVAL5");
  public static final String TERMINAL_ID_AVAL_5 = PROPERTIES.getEnv("TerminalID_AVAL5");

  public static final Integer ID_FACIL_AVAL =
      Integer.valueOf(PROPERTIES.getEnv("idFacilAVAL." + ENVIRONMENT));
  public static final String MERCHANT_ID_FACIL_AVAL = PROPERTIES.getEnv("MerchantIDFacil_AVAL");
  public static final String TERMINAL_ID_FACIL_AVAL = PROPERTIES.getEnv("TerminalIDFacil_AVAL");

  public static final String MERCHANT_ID_LOOK = PROPERTIES.getEnv("MerchantID_AVAL");
  public static final String TERMINAL_ID_LOOK = PROPERTIES.getEnv("TerminalID_AVAL");

  public static final Integer MOTO_FILTR = Integer.valueOf(PROPERTIES.getEnv("MOTO"));
  public static final Integer VOICE_FILTR = Integer.valueOf(PROPERTIES.getEnv("VOICE"));
}
