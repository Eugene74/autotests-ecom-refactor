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

    public static final String URL_TOMCAT = PROPERTIES.getEnv("URLtomcat");
    public static final String URL_MT_Tran = PROPERTIES.getEnv("URLtomcat") + "/mt/tran";
    public static final String URLp2p = PROPERTIES.getEnv("URLtomcat") + "/mt/p2p/init/";
    public static final String URLdasboard = PROPERTIES.getEnv("URLtomcat") + "/dashboard/login";
    public static final String URLfr = PROPERTIES.getEnv("URLtomcat") + "/mt/public/fast-refund/doFastRefund";

    public static final String URL_TOMEE = PROPERTIES.getEnv("URLtomee");
    public static final String URLInvocing = PROPERTIES.getEnv("URLtomee") + "/go";
    public static final String TermURL = PROPERTIES.getEnv("URLtomee") + "/go/enter";
    public static final String ActiveServerURL = PROPERTIES.getEnv("URLtomee") + "/paylink/astest";
    public static final String CallbackURl = PROPERTIES.getEnv("URLtomee") + "/go/3ds";
    public static final String URLredirect = PROPERTIES.getEnv("URLtomee") + "/go/pay";
    public static final String URLredirectPay = PROPERTIES.getEnv("URLtomee") + "/go/pay";
    public static final String URLredirectCapture = PROPERTIES.getEnv("URLtomee") + "/go/capture";
    public static final String URLMdesVtsCreateToken = PROPERTIES.getEnv("URLtomee") + "/go/token/create";
    public static final String URLMdesVtsRefreshToken = PROPERTIES.getEnv("URLtomee") + "/go/token/refresh";
    public static final String URLMdesVtsCardMetadata = PROPERTIES.getEnv("URLtomee") + "/go/card/metadata";
    public static final String URLMdesVtsCardContent = PROPERTIES.getEnv("URLtomee") + "/go/card/content";
    public static final String URLMdesVtsTokenStatus = PROPERTIES.getEnv("URLtomee") + "/go/token/status";
    public static final String URLMdesVtsTokenSuspend = PROPERTIES.getEnv("URLtomee") + "/go/token/suspend";
    public static final String URLMdesVtsTokenResume = PROPERTIES.getEnv("URLtomee") + "/go/token/resume";
    public static final String URLMdesVtsTokenDelete = PROPERTIES.getEnv("URLtomee") + "/go/token/delete";
    public static final String URL_ECG = PROPERTIES.getEnv("URLtomee") + "/paylink/extranet/index.jsp";
    public static final String URL_MERCH = PROPERTIES.getEnv("URLtomee") + "/go/merchant/do?action=trans";
    public static final String URL = PROPERTIES.getEnv("URLtomee") + "/go/service/02";
    public static final String URLjsonToken = PROPERTIES.getEnv("URLtomee") + "/go/payByToken";
    public static final String URL_INST = PROPERTIES.getEnv("URLtomee") + "/go/service/installments";

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
