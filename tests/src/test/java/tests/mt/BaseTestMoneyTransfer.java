package tests.mt;

import tests.BaseTest;

import static com.ecom.core.config.EnvData.URL_TOMCAT;

public class BaseTestMoneyTransfer extends BaseTest {
    protected static final String XML_TEMPLATES_CARD_TO_CARD = "template/xml/mt/cardToCard.xml";
    protected static final String XML_TEMPLATES_CARD_TO_ACCOUNT = "template/xml/mt/cardToAccount.xml";
    protected static final String XML_TEMPLATES_ACCOUNT_TO_CARD = "template/xml/mt/accountToCard.xml";
    protected static final String XML_TEMPLATES_REVERSAL_MT = "template/xml/mt/reversalRequestMT.xml";
    protected static final String XML_TEMPLATES_CARD_ZONE = "mt/cardZone.xml";
    protected static final String XML_TEMPLATES_STATUS = "mt/statusRequest.xml";
    protected static final String XML_CROSS_BORDER = "template/xml/mt/CrossBorder.xml";
    protected static final String XML_FAST_REFUND_APPCODE_RNN = "template/xml/mt/fastRefundApprovalCodeRRN.xml";
    protected static final String XML_FAST_REFUND_TOKEN = "template/xml/mt/fastRefundToken.xml";
    protected static final String XML_FAST_REFUND_CARD = "template/xml/mt/fastRefundCard.xml";
    protected static String URLmt = URL_TOMCAT+ "/mt/tran";
    protected static String URLmtRev = URL_TOMCAT+ "/mt/reversal";
    protected static String URLmtZone = URL_TOMCAT+ "/mt/card_zone";
    protected static String URLmtStatus = URL_TOMCAT+ "/mt/status";
    protected static String URLmtMersh = URL_TOMCAT+ "/mt/merchant/login";
    protected static String URLfr= URL_TOMCAT+ "/mt/public/fast-refund/doFastRefund";
}