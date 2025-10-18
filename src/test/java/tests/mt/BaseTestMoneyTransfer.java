package tests.mt;

import tests.BaseTest;

public class BaseTestMoneyTransfer extends BaseTest {
    protected static final String XML_TEMPLATES_CARD_TO_CARD = "src/main/resources/template/xml/mt/cardToCard.xml";
    protected static final String XML_TEMPLATES_CARD_TO_ACCOUNT = "src/main/resources/template/xml/mt/cardToAccount.xml";
    protected static final String XML_TEMPLATES_ACCOUNT_TO_CARD = "src/main/resources/template/xml/mt/accountToCard.xml";
    protected static final String XML_TEMPLATES_REVERSAL_MT = "src/main/resources/template/xml/mt/reversalRequestMT.xml";
    protected static final String XML_TEMPLATES_CARD_ZONE = "src/main/resources/mt/cardZone.xml";
    protected static final String XML_TEMPLATES_STATUS = "src/main/resources/mt/statusRequest.xml";
    protected static final String XML_CROSS_BORDER = "src/main/resources/template/xml/mt/CrossBorder.xml";

    protected static final String XML_FAST_REFUND_APPCODE_RNN = "src/main/resources/template/xml/mt/fastRefundApprovalCodeRRN.xml";
    protected static final String XML_FAST_REFUND_TOKEN = "src/main/resources/template/xml/mt/fastRefundToken.xml";
    protected static final String XML_FAST_REFUND_CARD = "src/main/resources/template/xml/mt/fastRefundCard.xml";

    protected static String URLmt = envProperties.getProperty("URLtomcat")+ "/mt/tran";
    protected static String URLmtRev = envProperties.getProperty("URLtomcat")+ "/mt/reversal";
    protected static String URLmtZone = envProperties.getProperty("URLtomcat")+ "/mt/card_zone";
    protected static String URLmtStatus = envProperties.getProperty("URLtomcat")+ "/mt/status";
    protected static String URLmtMersh = envProperties.getProperty("URLtomcat")+ "/mt/merchant/login";

    protected static String URLfr= envProperties.getProperty("URLtomcat")+ "/mt/public/fast-refund/doFastRefund";


}