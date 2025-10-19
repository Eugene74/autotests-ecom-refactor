package com.ecom.tests.support;

import org.w3c.dom.Document;
import tests.mt.BaseTestMoneyTransfer;

import static com.ecom.tests.support.DocumentTools.*;

public class MoneyTransferRequests extends BaseTestMoneyTransfer {
    
    public static Document transferCardToCard(String[] cardFunding, String[] cardPayment, String merch, String term){
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTestMoneyTransfer.XML_TEMPLATES_CARD_TO_CARD);
        fillRequestCartToCard(newRequestDoc, cardFunding, cardPayment, merch, term);
        return newRequestDoc;
    }

    public static Document transferCrossBoard(String[] cardFunding, String[] cardPayment, String merch, String term){
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTestMoneyTransfer.XML_CROSS_BORDER);
        fillRequestCartToCard(newRequestDoc, cardFunding, cardPayment, merch, term);
        return newRequestDoc;
    }

    public static Document transferCardToCardPares(String[] cardFunding, String[] cardPayment, String merch, String term){
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTestMoneyTransfer.XML_TEMPLATES_CARD_TO_CARD);
        fillRequestCartToCard(newRequestDoc, cardFunding, cardPayment, merch, term);
        fillPares(newRequestDoc, cardFunding, "Funding");
        return newRequestDoc;
    }

    public static Document transferCardToCardEMV(String[] cardFunding, String[] cardPayment, String merch, String term){
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTestMoneyTransfer.XML_TEMPLATES_CARD_TO_CARD);
        fillRequestCartToCard(newRequestDoc, cardFunding, cardPayment, merch, term);
        fillEMVData(newRequestDoc, cardFunding, "Funding");
        return newRequestDoc;
    }

    public static Document transferAccountToCard(String[] cardPayment, String merch, String term){
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTestMoneyTransfer.XML_TEMPLATES_ACCOUNT_TO_CARD);
        fillRequestAccountToCard(newRequestDoc, cardPayment, merch, term);
        return newRequestDoc;
    }

    public static Document transferCardToAccount(String[] cardFunding, String merch, String term){
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTestMoneyTransfer.XML_TEMPLATES_CARD_TO_ACCOUNT);
        fillRequestCardToAccount(newRequestDoc, cardFunding, merch, term);
        return newRequestDoc;
    }
    
    public static Document transferCardToAccountPares(String[] cardFunding, String merch, String term){
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTestMoneyTransfer.XML_TEMPLATES_CARD_TO_ACCOUNT);
        fillRequestCardToAccount(newRequestDoc, cardFunding, merch, term);
        fillPares(newRequestDoc, cardFunding, "Funding");
        return newRequestDoc;
    }

    public static Document transferCardToAccountEmv(String[] cardFunding, String merch, String term){
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTestMoneyTransfer.XML_TEMPLATES_CARD_TO_ACCOUNT);
        fillRequestCardToAccount(newRequestDoc, cardFunding, merch, term);
        fillEMVData(newRequestDoc, cardFunding, "Funding");
        return newRequestDoc;
    }

    public static Document reversalOnFunding(String merch, String term, Document request, Document response){
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTestMoneyTransfer.XML_TEMPLATES_REVERSAL_MT);
        fillReversalOnFunding(newRequestDoc, merch, term, request, response);
        return newRequestDoc;
    }

    public static Document fastRefundAPI(String merch, String term, Document request, Document response) {
        // Зчитуємо шаблон XML для FastRefund
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTestMoneyTransfer.XML_FAST_REFUND_APPCODE_RNN);

        // Заповнюємо дані в шаблоні
        fillFastRefundAPI(newRequestDoc, merch, term, request, response);

        // Повертаємо заповнений документ
        return newRequestDoc;
    }
    public static Document fastRefundAPIToken(String merch, String term, Document request, Document response) {
        // Зчитуємо шаблон XML для FastRefund
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTestMoneyTransfer.XML_FAST_REFUND_TOKEN);

        // Заповнюємо дані в шаблоні
        fillFastRefundAPI(newRequestDoc, merch, term, request, response);

        // Повертаємо заповнений документ
        return newRequestDoc;
    }

    public static Document fastRefundAPICard(String merch, String term, Document request, Document response) {
        // Зчитуємо шаблон XML для FastRefund
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTestMoneyTransfer.XML_FAST_REFUND_CARD);

        // Заповнюємо дані в шаблоні
        fillFastRefundAPI(newRequestDoc, merch, term, request, response);

        // Повертаємо заповнений документ
        return newRequestDoc;
    }
}
