package tests.mt;

import tests.BaseTest;

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

    protected static String URLmt = envProperties.getProperty("URLtomcat")+ "/mt/tran";
    protected static String URLmtRev = envProperties.getProperty("URLtomcat")+ "/mt/reversal";
    protected static String URLmtZone = envProperties.getProperty("URLtomcat")+ "/mt/card_zone";
    protected static String URLmtStatus = envProperties.getProperty("URLtomcat")+ "/mt/status";
    protected static String URLmtMersh = envProperties.getProperty("URLtomcat")+ "/mt/merchant/login";

    protected static String URLfr= envProperties.getProperty("URLtomcat")+ "/mt/public/fast-refund/doFastRefund";


}