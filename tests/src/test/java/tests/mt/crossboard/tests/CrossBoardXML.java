package tests.mt.crossboard.tests;

import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import tests.mt.BaseTestMoneyTransfer;

import static com.ecom.core.config.CardConfig.cardMCmta;
import static com.ecom.core.config.CardConfig.cardVISAmtRev;
import static com.ecom.core.config.EnvData.merchant_AVAL;
import static com.ecom.core.config.EnvData.terminal_AVAL;
import static com.ecom.db.JDBCMethods.getValueFromMTTran;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.MoneyTransferRequests.transferCrossBoard;
import static com.ecom.tests.support.RequestSender.sendRequest;

public class CrossBoardXML extends BaseTestMoneyTransfer {

    @Test
    public void crossBoardXML() {
        Document requestDoc = transferCrossBoard(cardMCmta, cardVISAmtRev, merchant_AVAL, terminal_AVAL);
        Document responseDoc = sendRequest(URLmt, requestDoc);

        System.out.println("--TRANSFER--\nRequest:\n" + printRequestMT(requestDoc));
        System.out.println("Response:\n" + printResponseMT(responseDoc));

        String rrnPayment = getElementFromDocument(responseDoc, "RRN");
        SoftAssert softAssertion = new SoftAssert();

        // Проверка RRN
        softAssertion.assertNotNull(rrnPayment, "RRN is null");
        if (rrnPayment != null) {
            softAssertion.assertEquals(rrnPayment.length(), 12, "RRN");
        }

        // Проверка ApprovalCode
        String approvalCode = getElementFromDocument(responseDoc, "ApprovalCode");
        softAssertion.assertNotNull(approvalCode, "ApprovalCode is null");
        if (approvalCode != null) {
            softAssertion.assertEquals(approvalCode.length(), 6, "ApprovalCode");
        }

        // Проверка других элементов
        softAssertion.assertEquals(getValueFromMTTran(rrnPayment, "ECI"), "07", "ECI");
        softAssertion.assertEquals(getElementFromDocument(responseDoc, "AuthCode"), "000", "AuthCode");
        softAssertion.assertEquals(getElementFromDocument(responseDoc, "MCC"), "6012", "MCC");
        softAssertion.assertAll();

        System.out.println("Verified:");
        String[] verifiedResponse = {"Code", "Message"};
        String[] verifiedPayment = {"ApprovalCode", "AuthCode", "MCC", "RRN"};
        String[] verifiedResponseFromDBPayment = {"ECI"};
        try {
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataResponsePayment(responseDoc, verifiedPayment, 0);
            verifiedDataMTFromDBPayment(rrnPayment, verifiedResponseFromDBPayment);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
            e.printStackTrace();
        }
    }}