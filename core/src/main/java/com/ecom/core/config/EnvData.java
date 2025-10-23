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
            String.format(PROPERTIES.getEnv("base.urlmaildev"), ENVIRONMENT);
    public static final String PIPELINE_NAME =
            PROPERTIES.getEnv("URLtomee").split("-tomee")[0].replace("https://", "").toLowerCase();
    public static boolean isHeadless = PROPERTIES.isHeadless();
    public static final String BROWSER = PROPERTIES.getEnv("browser");

    public static final String URL_TOMCAT = PROPERTIES.getEnv("URLtomcat");
    public static final String URL_MT_Tran = URL_TOMCAT + "/mt/tran";
    public static final String URLp2p = URL_TOMCAT + "/mt/p2p/init/";
    public static final String URLdasboard = URL_TOMCAT + "/dashboard/login";
    public static final String URLfr = URL_TOMCAT + "/mt/public/fast-refund/doFastRefund";
    public static String URLmtRev = URL_TOMCAT+ "/mt/reversal";
    public static String URLmtZone = URL_TOMCAT+ "/mt/card_zone";
    public static String URLmtStatus = URL_TOMCAT+ "/mt/status";
    public static String URLmtMersh = URL_TOMCAT+ "/mt/merchant/login";
    public static String URLAlias1Create = URL_TOMCAT+ "/alias/create";
    public static String URLAlias1Update = URL_TOMCAT + "/alias/update";
    public static String URLAlias1Get = URL_TOMCAT + "/alias/get";
    public static String URLAlias1Resolve = URL_TOMCAT + "/alias/resolve";
    public static String URLAlias1Inguiry = URL_TOMCAT + "/alias/inquiry";
    public static String URLAlias1Delete = URL_TOMCAT + "/alias/delete";
    public static String URLAlias2Create = URL_TOMCAT + "/alias/v2/create";
    public static String URLAlias2Get = URL_TOMCAT +  "/alias/v2/get";
    public static String URLAlias2GetAliasId = URL_TOMCAT + "/alias/v2/getAliasId";
    public static String URLAlias2GetByExternalId = URL_TOMCAT + "/alias/v2/getByExternalId";
    public static String URLAlias2Inquiry = URL_TOMCAT + "/alias/v2/inquiry";
    public static String URLAlias2Resolve = URL_TOMCAT + "/alias/v2/resolve";
    public static String URLAlias2Update = URL_TOMCAT + "/alias/v2/update";
    public static String URLAlias2UpdateStatus = URL_TOMCAT + "/alias/v2/updateStatus";
    public static String URLAlias2CreatePaymentCredential = URL_TOMCAT + "/alias/v2/createPaymentCredential";
    public static String URLAlias2GetPaymentCredential = URL_TOMCAT + "/alias/v2/getPaymentCredential";
    public static String URLAlias2GetAllPaymentCredentials = URL_TOMCAT + "/alias/v2/getAllPaymentCredentials";
    public static String URLAlias2DeletePaymentCredential = URL_TOMCAT + "/alias/v2/deletePaymentCredential";
    public static String URLAlias2Delete = URL_TOMCAT + "/alias/v2/delete"; // Додано шлях для запиту DeleteAlias;
    public static String URLBinInfo = URL_TOMCAT + "/infopoint/api/v1/getBinInfo";

    public static final String URL_TOMEE = PROPERTIES.getEnv("URLtomee");
    public static final String URLInvocing = URL_TOMEE + "/go";
    public static final String TermURL = URL_TOMEE + "/go/enter";
    public static final String ActiveServerURL = URL_TOMEE + "/paylink/astest";
    public static final String CallbackURl = URL_TOMEE + "/go/3ds";
    public static final String URLredirect = URL_TOMEE + "/go/pay";
    public static final String URLredirectPay = URL_TOMEE + "/go/pay";
    public static final String URLredirectCapture = URL_TOMEE + "/go/capture";
    public static final String URLMdesVtsCreateToken = URL_TOMEE + "/go/token/create";
    public static final String URLMdesVtsRefreshToken = URL_TOMEE + "/go/token/refresh";
    public static final String URLMdesVtsCardMetadata = URL_TOMEE + "/go/card/metadata";
    public static final String URLMdesVtsCardContent = URL_TOMEE + "/go/card/content";
    public static final String URLMdesVtsTokenStatus = URL_TOMEE + "/go/token/status";
    public static final String URLMdesVtsTokenSuspend = URL_TOMEE + "/go/token/suspend";
    public static final String URLMdesVtsTokenResume = URL_TOMEE + "/go/token/resume";
    public static final String URLMdesVtsTokenDelete = URL_TOMEE + "/go/token/delete";
    public static final String URL_ECG = URL_TOMEE + "/paylink/extranet/index.jsp";
    public static final String URL_MERCH = URL_TOMEE + "/go/merchant/do?action=trans";
    public static final String URL = URL_TOMEE + "/go/service/02";
    public static final String URLjsonToken = URL_TOMEE + "/go/payByToken";
    public static final String URL_INST = URL_TOMEE + "/go/service/installments";

    public static final Integer id_AVAL = Integer.valueOf(PROPERTIES.getEnv("idAVAL." + ENVIRONMENT));
    public static final String merchant_AVAL = PROPERTIES.getEnv("MerchantID_AVAL");
    public static final String terminal_AVAL = PROPERTIES.getEnv("TerminalID_AVAL");

    public static final Integer id_AVAL1 = Integer.valueOf(PROPERTIES.getEnv("idAVAL1." + ENVIRONMENT));
    public static final String merchantID_aval1 = PROPERTIES.getEnv("MerchantID_AVAL1");
    public static final String terminalID_aval1 = PROPERTIES.getEnv("TerminalID_AVAL1");

    public static final Integer id_AVAL2 = Integer.valueOf(PROPERTIES.getEnv("idAVAL2." + ENVIRONMENT));
    public static final String merchantID_aval2 = PROPERTIES.getEnv("MerchantID_AVAL2");
    public static final String terminalID_aval2 = PROPERTIES.getEnv("TerminalID_AVAL2");

    public static final Integer id_AVAL3 = Integer.valueOf(PROPERTIES.getEnv("idAVAL3." + ENVIRONMENT));
    public static final String merchantID_aval3 = PROPERTIES.getEnv("MerchantID_AVAL3");
    public static final String terminalID_aval3 = PROPERTIES.getEnv("TerminalID_AVAL3");

    public static final Integer id_AVAL4 = Integer.valueOf(PROPERTIES.getEnv("idAVAL4." + ENVIRONMENT));
    public static final String merchantID_aval4 = PROPERTIES.getEnv("MerchantID_AVAL4");
    public static final String terminalID_aval4 = PROPERTIES.getEnv("TerminalID_AVAL4");

    public static final Integer id_AVAL5 = Integer.valueOf(PROPERTIES.getEnv("idAVAL5." + ENVIRONMENT));
    public static final String merchantID_aval5 = PROPERTIES.getEnv("MerchantID_AVAL5");
    public static final String terminalID_aval5 = PROPERTIES.getEnv("TerminalID_AVAL5");

    public static final Integer idFacilAVAL = Integer.valueOf(PROPERTIES.getEnv("idFacilAVAL." + ENVIRONMENT));
    public static final String merchantIDFacil_AVAL = PROPERTIES.getEnv("MerchantIDFacil_AVAL");
    public static final String terminalIDFacil_AVAL = PROPERTIES.getEnv("TerminalIDFacil_AVAL");

    public static final String MERCHANT_ID_LOOK = PROPERTIES.getEnv("MerchantID_AVAL");
    public static final String TERMINAL_ID_LOOK = PROPERTIES.getEnv("TerminalID_AVAL");

    public static final Integer MOTO_FILTR = Integer.valueOf(PROPERTIES.getEnv("MOTO"));
    public static final Integer VOICE_FILTR = Integer.valueOf(PROPERTIES.getEnv("VOICE"));
}
