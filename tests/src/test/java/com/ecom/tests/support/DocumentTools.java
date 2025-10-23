package com.ecom.tests.support;

import com.ecom.api.model.xml.InstallmentPlan;
import com.ecom.core.xml.XmlFacade;
import com.ecom.core.xml.XmlUtils;
import com.ecom.db.JDBCMethods;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

public class DocumentTools {

    static long date = new Date().getTime();
    private static final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
    public static ThreadLocalRandom rnd = ThreadLocalRandom.current();
    private static final String trackingId = String.valueOf(rnd.nextInt(100000, 999999));
    private static final XmlFacade XML = XmlFacade.create();

    public static Document readXMLFile(String filePath) {
        return XML.load(filePath);
    }

    public static String convertXMLDocumentToString(Document doc) {
        return XML.asString(doc);
    }

    public static Document convertStringToXmlDocument(String XMLString) {
        return XML.fromString(XMLString);
    }

    /**
     * Methods for GET/SET value of TAG from XML Document
     */

    public static String getElementFromDocument(Document doc, String tag) {
        try {
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName(tag);
            return nList.item(0).getTextContent();
        } catch (Exception e) {
           // System.out.println(">>> tag: " + tag + " is missing in response");
        }
        return "";
    }

    public static String getNestedElementFromDocument(Document doc, String xpathExpression) {
        try {
            XPath xPath = XPathFactory.newInstance().newXPath();
            Node node = (Node) xPath.evaluate(xpathExpression, doc, XPathConstants.NODE);
            return node != null ? node.getTextContent() : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getSecondElementFromDocument(Document doc, String tag) {
        try {
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName(tag);
            return nList.item(1).getTextContent();
        } catch (Exception e) {
            System.out.println(">>> Error: tag is missing in response");
        }
        return "";
    }

    public static void setAttributeToDocument(Document doc, String tag, String value) {
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName(tag);
        nList.item(0).setTextContent(value);
    }

    public static Document addTags(Document template, String parent, String tag, String value) {
        Node root = template.getElementsByTagName(parent).item(0);
        Node recurrent = template.createElement(tag);
        recurrent.setTextContent(value);
        root.appendChild(recurrent);
        return template;
    }

    private static String maskString(String strText, int start, int end) {
        int maskLength = end - start;
        StringBuilder sbMaskString = new StringBuilder(maskLength);
        for (int i = 0; i < maskLength; i++) {
            sbMaskString.append("*");
        }
        return strText.substring(0, start)
                + sbMaskString.toString()
                + strText.substring(start + maskLength);
    }

    private static ArrayList<InstallmentPlan> getPlans(){
        ArrayList<InstallmentPlan> installmentPlans = new ArrayList<>();

        return installmentPlans;
    }

    /**
     * Output tran data
     */

    private static String nodeListToString(NodeList nodes) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < nodes.getLength(); ++i) {
            Node node = nodes.item(i);
            builder.append(XmlUtils.toString(node));
        }
        return builder.toString();
    }

    public static String printRequest(Document doc) {
        try {
            String cardNum = doc.getElementsByTagName("CardNum").item(0).getTextContent();
            String cvv = doc.getElementsByTagName("CVNum").item(0).getTextContent();
            doc.getElementsByTagName("CardNum").item(0).setTextContent(maskString(cardNum, 6, 12));
            doc.getElementsByTagName("CVNum").item(0).setTextContent(maskString(cvv, 0, 3));
        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        doc = convertStringToXmlDocument(convertXMLDocumentToString(doc).replaceAll("(?:>)(\\s*)<", "><"));

        System.out.println("===============================================");
        return nodeListToString(doc.getElementsByTagName("RequestData"));
    }

    public static String printResponse(Document doc) {
        System.out.println("-----------------------------------------------");
        return nodeListToString(doc.getElementsByTagName("ResponseData"));
    }

    /**
     * Onboarding API
     * @param doc
     * @return
     */
    public static String printResponseAPI(Document doc) {
        System.out.println("-----------------------------------------------");
        try
        {
            DOMSource domSource = new DOMSource(doc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            return writer.toString();
        }
        catch(TransformerException ex)
        {
            ex.printStackTrace();
            return null;
        }
    }

    public static void verifiedDataFromResponse(Document response, String[] tags) {
        System.out.println("===============================================");
        for (String tag : tags) {
            if (tag.equals("ExtData")) {
                System.out.print("RS_" + tag + ": " + response.getElementsByTagName("ExtData").item(0).getAttributes().item(0).getTextContent() + "\n");
                System.out.print("RS_" + tag + ": " + response.getElementsByTagName("ExtData").item(0).getAttributes().item(1).getTextContent() + "\n");
            } else {
                System.out.print("RS_" + tag + ": " + getElementFromDocument(response, tag) + "\n");
            }
        }
        System.out.println("-----------------------------------------------");
    }

    public static void verifiedDataResponseFunding(Document response, String[] tags) {
        for (String tag : tags) {
            System.out.print("RS_Funding_" + tag + ": " + getElementFromDocument(response, tag) + "\n");
        }
        System.out.println("-----------------------------------------------");
    }

    public static void verifiedDataResponsePayment(Document response, String[] tags, int index) {
        for (String tag : tags) {
            if (index == 1) {
                System.out.print("RS_Payment_" + tag + ": " + getSecondElementFromDocument(response, tag) + "\n");
            } else {
                System.out.print("RS_Payment_" + tag + ": " + getElementFromDocument(response, tag) + "\n");
            }
        }
        System.out.println("-----------------------------------------------");
    }

    public static void verifiedDataResponseRevers(Document response, String[] tags) {
        for (String tag : tags) {
            System.out.print("RS_Revers_" + tag + ": " + getElementFromDocument(response, tag) + "\n");
        }
        System.out.println("-----------------------------------------------");
    }


    public static void verifiedDataFromDB(int tranId, String[] tags) {
        System.out.println("DB_TranID: " + tranId);
        for (String tag : tags) {
            switch (tag) {
                case "Call_Id":
                    System.out.print("DB_" + tag + ": " + JDBCMethods.getCheckoutData(tranId) + "\n");
                    break;
                case "DS_TransID":
                    System.out.print("DB_" + tag + ": " + JDBCMethods.getTran3dsData(tranId) + "\n");
                    break;
                case "INCOMING_TID":
                    System.out.print("DB_" + tag + ": " + JDBCMethods.getValueFromRecurrent(tranId) + "\n");
                    break;
                default:
                    System.out.print("DB_" + tag + ": " + JDBCMethods.getValueFromTRAN(tranId, tag) + "\n");
                    break;
            }
        }
        System.out.println("-----------------------------------------------");
    }

    public static void verifiedDataFromDBFR(int tranId, String[] tags) {
        System.out.println("DB_TranID: " + tranId);
        for (String tag : tags) {
            String value = JDBCMethods.getValueFromMTTranFR(tranId, tag);
            if (value == null || value.isEmpty()) {
                System.out.println("DB_" + tag + ": [EMPTY or NULL]");
            } else {
                System.out.println("DB_" + tag + ": " + value);
            }
        }
        System.out.println("-----------------------------------------------");
    }

    public static void verifiedDataMTFromDBFunding(String rrn, String[] tags) {
        for (String tag : tags) {
            switch (tag) {
                case "RRN":
                    System.out.print("DB_Funding_RRN: " + rrn + "\n");
                    break;
                case "DS_TransID":
                    System.out.print("DB_Funding_" + tag + ": " + JDBCMethods.getTranMT3dsData(rrn) + "\n");
                    break;
                default:
                    System.out.print("DB_Funding_" + tag + ": " + JDBCMethods.getValueFromMTTran(rrn, tag) + "\n");
                    break;
            }
        }
        System.out.println("-----------------------------------------------");
    }

    public static void verifiedDataMTFromDBPayment(String rrn, String[] tags) {
        for (String tag : tags) {
            if (tag.equals("RRN"))
                System.out.print("DB_Payment_RRN: " + rrn + "\n");
            else
                System.out.print("DB_Payment_" + tag + ": " + JDBCMethods.getValueFromMTTran(rrn, tag) + "\n");
        }
        System.out.println("-----------------------------------------------");
    }

    public static void verifiedDataMTFromDBReversal(String rrn, String[] tags) {
        for (String tag : tags) {
            System.out.print("DB_Revers_" + tag + ": " + JDBCMethods.getValueFromMTTran(rrn, tag) + "\n");
            break;
        }
        System.out.println("-----------------------------------------------");
    }


    /**
     * Methods for fill XML Request according to template
     */

    public static Document fillMerchant(Document doc, String merch, String term) {
        doc.getElementsByTagName("MerchantID").item(0).setTextContent(merch);
        doc.getElementsByTagName("TerminalID").item(0).setTextContent(term);
        return doc;
    }

    public static Document fillCardData(Document doc, String[] card) {
        doc.getElementsByTagName("CardNum").item(0).setTextContent(card[0]);
        doc.getElementsByTagName("ExpYear").item(0).setTextContent(card[1]);
        doc.getElementsByTagName("ExpMonth").item(0).setTextContent(card[2]);
        doc.getElementsByTagName("CVNum").item(0).setTextContent(card[3]);
        return doc;
    }

    public static Document fillGetInstPlanRequest(Document doc, String[] card, String amount, String merch, String term) {
        fillMerchant(doc, merch, term);
        doc.getElementsByTagName("CardNum").item(0).setTextContent(card[0]);
        doc.getElementsByTagName("TotalAmount").item(0).setTextContent(amount);
        return doc;
    }

    public static Document fillRequestData(Document request, String[] card, String merch, String term, String desc) {
        fillMerchant(request, merch, term);
        fillCardData(request, card);
        request.getElementsByTagName("Message").item(0).getAttributes().item(0).setTextContent(sdf.format(date));
        request.getElementsByTagName("OrderID").item(0).setTextContent(String.valueOf(rnd.nextInt(100000, 999999)));
        request.getElementsByTagName("Date").item(0).setTextContent(sdf.format(date));
        request.getElementsByTagName("TotalAmount").item(0).setTextContent(String.valueOf(rnd.nextInt(1001, 9999)));
//        TODO for Borica
//        request.getElementsByTagName("TotalAmount").item(0).setTextContent("2211");
        request.getElementsByTagName("Description").item(0).setTextContent(desc);
        return request;
    }

    public static Document fillTokenRequestData(Document request, String[] token, String merch, String term, String desc) {
        fillMerchant(request, merch, term);
        request.getElementsByTagName("tokenId").item(0).setTextContent(token[0]);
        request.getElementsByTagName("CVNum").item(0).setTextContent(token[1]);
        request.getElementsByTagName("Message").item(0).getAttributes().item(0).setTextContent(sdf.format(date));
        request.getElementsByTagName("OrderID").item(0).setTextContent(String.valueOf(rnd.nextInt(100000, 999999)));
        request.getElementsByTagName("Date").item(0).setTextContent(sdf.format(date));
        request.getElementsByTagName("TotalAmount").item(0).setTextContent(String.valueOf(rnd.nextInt(1001, 9999)));
        request.getElementsByTagName("Description").item(0).setTextContent(desc);
        return request;
    }

    public static Document fillRequestDataBorica(Document request, String[] card, String merch, String term, String desc, String amount) {
        fillMerchant(request, merch, term);
        fillCardData(request, card);
        request.getElementsByTagName("Message").item(0).getAttributes().item(0).setTextContent(sdf.format(date));
        request.getElementsByTagName("OrderID").item(0).setTextContent(String.valueOf(rnd.nextInt(100000, 999999)));
        request.getElementsByTagName("Date").item(0).setTextContent(sdf.format(date));
        request.getElementsByTagName("TotalAmount").item(0).setTextContent(amount);
        request.getElementsByTagName("Description").item(0).setTextContent(desc);
        return request;
    }

    public static Document fillRequestDataAmount(Document request, String[] card, String merch, String term, String desc, String amount) {
        fillMerchant(request, merch, term);
        fillCardData(request, card);
        request.getElementsByTagName("Message").item(0).getAttributes().item(0).setTextContent(sdf.format(date));
        request.getElementsByTagName("OrderID").item(0).setTextContent(String.valueOf(rnd.nextInt(100000, 999999)));
        request.getElementsByTagName("Date").item(0).setTextContent(sdf.format(date));
        request.getElementsByTagName("TotalAmount").item(0).setTextContent(amount);
        request.getElementsByTagName("Description").item(0).setTextContent(desc);
        return request;
    }

    public static Document fillCheckoutData(Document request) {
        addTags(request, "PayData", "Wallet", "");
        addTags(request, "Wallet", "VISACheckout", "");
        addTags(request, "VISACheckout", "callId", "73269061663880701");
        return request;
    }

    public static Document fillParesData(Document request, String[] card, String rootTag) {
        fillPares(request, card, rootTag);
        addTags(request, "PARes", "CavvAlgorithm", card[7]);
        return request;
    }

    public static Document fillEMVData(Document request, String[] card, String rootTag) {
        fillPares(request, card, rootTag);
        addTags(request, "PARes", "EMV3ds", "");
        //TODO for Borica
//        addTags(request, "EMV3ds", "DServerTransID", "29a7f3f3-4395-4671-a4a2-07c8c5522925");
        addTags(request, rootTag, "SCAExemption", "01");
        addTags(request, "EMV3ds", "DServerTransID", "45670c37-6acb-4946-a2cc-bc82fb26ea51");
        addTags(request, "EMV3ds", "VisaMerchantIdentifier", "12345678");
        return request;
    }

    public static Document fillPares(Document request, String[] card, String rootTag) {
        addTags(request, rootTag, "PARes", "");
        addTags(request, "PARes", "Status", card[6]);
        addTags(request, "PARes", "CAVV", card[5]);
        addTags(request, "PARes", "ECI", card[8]);
        return request;
    }

    public static Document fillSettlement(Document request) {
        request.renameNode(request.getElementsByTagName("Authorization").item(0), "", "Settlement");
        request.renameNode(request.getElementsByTagName("PayData").item(0), "", "SettlementRefundData");
        addTags(request, "SettlementRefundData", "ECI", "01");
        addTags(request, "SettlementRefundData", "PosConditionCode", "08");
        addTags(request, "SettlementRefundData", "Ref3", "asdasdasd333ASD");
        return request;
    }

    public static Document fillLocalInstChoice(Document request, String instPlanParamId, String numberOfPay, String feeMonth, String interestRate, String checkValue) {
//        request.renameNode(request.getElementsByTagName("PayData").item(0), "", "InternalInstallmentChoice");
//        addTags(request, "PayData", "InternalInstallmentChoice", "");
        request.getElementsByTagName("instPlanParamId").item(0).setTextContent(instPlanParamId);
        request.getElementsByTagName("numberOfPay").item(0).setTextContent(numberOfPay);
        request.getElementsByTagName("feeMonth").item(0).setTextContent(feeMonth);
        request.getElementsByTagName("interestRate").item(0).setTextContent(interestRate);
        request.getElementsByTagName("CheckValue").item(0).setTextContent(checkValue);
//
//        addTags(request, "InternalInstallmentChoice", "instPlanParamId", instPlanParamId);
//        addTags(request, "InternalInstallmentChoice", "numberOfPay", numberOfPay);
//        addTags(request, "InternalInstallmentChoice", "feeMonth", feeMonth);
//        addTags(request, "InternalInstallmentChoice", "interestRate", interestRate);
//        addTags(request, "InternalInstallmentChoice", "CheckValue", checkValue);
        return request;
    }

    public static Document fillAddendumVisa(Document request) {
        addTags(request, "AirlineAddendumData", "VISA", "");
        addTags(request, "VISA", "ItineraryData", "");
        addTags(request, "ItineraryData", "passengerName", "RomondaEleonoraElizabethPanta");
        addTags(request, "ItineraryData", "ticketNumber", "AAA123BBB456CCC");
        addTags(request, "ItineraryData", "restrictedTicketIndicator", "0");
        addTags(request, "ItineraryData", "VTripLegs", "");
        addTags(request, "VTripLegs", "VTripLeg", "");
        addTags(request, "VTripLeg", "carrierCode", "S2");
        addTags(request, "VTripLeg", "departureAirport", "RIX22");
        addTags(request, "VTripLeg", "departureDate", "20200914");
        addTags(request, "VTripLeg", "destinationAirport", "S2");
        addTags(request, "VTripLeg", "serviceClass", "A1");
        addTags(request, "VTripLeg", "legNr", "01");
        return request;
    }

    public static Document fillAddendumMC(Document request) {
        addTags(request, "AirlineAddendumData", "MasterCard", "");
        addTags(request, "MasterCard", "customerRef", "S2");
        addTags(request, "MasterCard", "carrierCode", "S2");
        addTags(request, "MasterCard", "passengerName", "RomondaEleonoraElizabethPanta");
        addTags(request, "MasterCard", "ticketNumber", "AAA123BBB456CCC");
        addTags(request, "MasterCard", "restrictedTicketIndicator", "0");
        addTags(request, "MasterCard", "MCTripLegs", "");
        addTags(request, "MCTripLegs", "MCTripLeg", "");
        addTags(request, "MCTripLeg", "carrierCode", "S2");
        addTags(request, "MCTripLeg", "departureAirport", "RIX22");
        addTags(request, "MCTripLeg", "destinationAirport", "S2");
        addTags(request, "MCTripLeg", "serviceClass", "A1");
        addTags(request, "VTripLeg", "legNr", "01");
        return request;
    }

    public static Document fillAddendumData(Document request, String paysys) {
        addTags(request, "PayData", "AirlineAddendumData", "");
        if (paysys.equals("VISA")) {
            fillAddendumVisa(request);
        } else {
            fillAddendumMC(request);
        }
        addTags(request, "PayData", "Ref3", "Congrats");
        return request;
    }

    /**
     * Methods for fill Reversal/Refund Requests
     */
    public static String getTagAmount(Document requestPay) {
        String tag;
        if (requestPay.getElementsByTagName("PostauthorizationAmount").getLength() > 0) {
            tag = "PostauthorizationAmount";
        } else {
            tag = "TotalAmount";
        }
        return tag;
    }

    public static Document fillInvoice(Document requestRevers, Document requestPay) {

        requestRevers.getElementsByTagName("OrderID").item(0).setTextContent(requestPay.getElementsByTagName("OrderID").item(0).getTextContent());
        requestRevers.getElementsByTagName("Date").item(0).setTextContent(requestPay.getElementsByTagName("Date").item(0).getTextContent());
        requestRevers.getElementsByTagName("TotalAmount").item(0).setTextContent(requestPay.getElementsByTagName(getTagAmount(requestPay)).item(0).getTextContent());
        try {
            requestRevers.getElementsByTagName("Transaction").item(0).getAttributes().item(0)
                    .setTextContent(requestPay.getElementsByTagName("Transaction").item(0).getAttributes().item(0).getTextContent());
        } catch (Exception ex) {
            ex.getLocalizedMessage();
        }
        return requestRevers;
    }

    public static Document fillAmountToRefund(Document requestRevers,
                                              Document requestPay,
                                              String tag,
                                              double n) {
        String amountText = requestPay.getElementsByTagName(getTagAmount(requestPay)).item(0).getTextContent();
        double amount = Double.parseDouble(amountText);
        amount = amount + (amount * n);
        requestRevers.getElementsByTagName(tag).item(0).setTextContent(String.valueOf((int) amount));

        return requestRevers;
    }

    public static Document fillFromResponseData(Document request, Document responsePay) {
        request.getElementsByTagName("Message").item(0).getAttributes().item(0).setTextContent(sdf.format(date));
        setAttributeToDocument(request, "MerchantID", getElementFromDocument(responsePay, "MerchantID"));
        setAttributeToDocument(request, "TerminalID", getElementFromDocument(responsePay, "TerminalID"));
        setAttributeToDocument(request, "Rrn", getElementFromDocument(responsePay, "Rrn"));
        setAttributeToDocument(request, "ApprovalCode", getElementFromDocument(responsePay, "ApprovalCode"));
        try {
            request.getElementsByTagName("checkData").item(0).setTextContent(responsePay.getElementsByTagName("checkData").item(1).getTextContent());
        } catch (Exception ex) {
            ex.getLocalizedMessage();
        }
        return request;
    }

    public static Document fillRequestRefund(Document requestRevers, Document requestPay, Document responsePay, double delta) {
        fillInvoice(requestRevers, requestPay);
        fillAmountToRefund(requestRevers, requestPay, "RefundAmount", delta);
        fillFromResponseData(requestRevers, responsePay);
        return requestRevers;
    }

    public static Document fillPostRequest(Document requestPostAuth, Document requestPreAuth, Document responsePreAuth, String description, double delta) {
        fillRequestRefund(requestPostAuth, requestPreAuth, responsePreAuth, delta);
        setAttributeToDocument(requestPostAuth, "Description", description);
        requestPostAuth.renameNode(requestPostAuth
                .getElementsByTagName("Refund").item(0), "", "Postauthorization");
        requestPostAuth.renameNode(requestPostAuth
                .getElementsByTagName("RefundData").item(0), "", "PostauthorizationData");
        requestPostAuth.renameNode(requestPostAuth
                .getElementsByTagName("RefundAmount").item(0), "", "PostauthorizationAmount");
        requestPostAuth.renameNode(requestPostAuth
                .getElementsByTagName("AuthorizationRef").item(0), "", "PreauthorizationRef");
        return requestPostAuth;
    }

    public static String getValueByField(String message, String field) {
        String value = "";
        int start = message.indexOf("<field id=\"" + field + "\"");
        return value;
    }

    public static String fillFieldValue(String fieldValue, int fieldLength, char ch) {
        if (ch == '0') {
            return StringUtils.repeat(ch, (fieldLength - fieldValue.length())) + fieldValue;
        } else if (ch == ' ') {
            return fieldValue + StringUtils.repeat(ch, (fieldLength - fieldValue.length()));
        }
        return fieldValue;
    }

    /**
     * For Money Transfer
     */

    public static Document fillMerchantMT(Document doc, String merch, String term) {
        doc.getElementsByTagName("MerchantId").item(0).setTextContent(merch);
        doc.getElementsByTagName("TerminalId").item(0).setTextContent(term);
        return doc;
    }

    public static Document fillRequestAccountToCard(Document request, String[] cardP, String merch, String term) {
        fillRequestDataMT(request, merch, term);
        request.getElementsByTagName("RecipientCardNumber").item(0).setTextContent(cardP[0]);
        return request;
    }

    public static Document fillRequestCardToAccount(Document request, String[] cardF, String merch, String term) {
        fillRequestDataMT(request, merch, term);
        updateMTCardDataFunding(request, cardF);
        return request;
    }

    public static Document fillRequestDataMT(Document request, String merch, String term) {
        fillMerchantMT(request, merch, term);
        request.getElementsByTagName("TrackingId").item(0).setTextContent(String.valueOf(rnd.nextInt(100000, 999999)));
        request.getElementsByTagName("Value").item(0).setTextContent(String.valueOf(rnd.nextInt(100, 10000)));
        return request;
    }

    public static Document fillRequestCartToCard(Document request, String[] cardF, String[] cardP, String merch, String term) {
        fillRequestDataMT(request, merch, term);
        updateMTCardDataFunding(request, cardF);
        request.getElementsByTagName("RecipientCardNumber").item(0).setTextContent(cardP[0]);
        return request;
    }

    public static Document fillReversalOnFunding(Document requestRev, String merch, String term, Document request, Document response) {
        fillRequestDataMT(requestRev, merch, term);
        fillValueByTags(request, requestRev, "TrackingId");
        fillValueByTags(request, requestRev, "Value");
        fillValueByTags(response, requestRev, "ApprovalCode");
        fillValueByTags(response, requestRev, "RRN");
        return request;
    }

    public static void fillFastRefundAPI(Document newRequestDoc, String merch, String term, Document request, Document response) {
        // Заполняем MerchantId и TerminalId
        newRequestDoc.getElementsByTagName("MerchantId").item(0).setTextContent(merch);
        newRequestDoc.getElementsByTagName("TerminalId").item(0).setTextContent(term);

        // Заполняем данные из response
        fillValueByTagsFR(response, newRequestDoc, "ApprovalCode");
        fillValueByTagsFR(response, newRequestDoc, "RRN");

        // Заполняем данные из request
        fillValueByTagsFR(request, newRequestDoc, "Value");

        // Устанавливаем произвольное значение для TrackingId
        newRequestDoc.getElementsByTagName("TrackingId").item(0).setTextContent("123456");
    }



    /* Stoplist API
     * fill xml*/

    public static Document fillCreateItemPan(Document doc, String merchantId, String terminalId, String trackingId, String type, String pan, String expDate, String createRemark, String createDate){
        doc.getElementsByTagName("MerchantID").item(0).setTextContent(merchantId);
        doc.getElementsByTagName("TerminalID").item(0).setTextContent(terminalId);
        doc.getElementsByTagName("trackingId").item(0).setTextContent(trackingId);
        doc.getElementsByTagName("type").item(0).setTextContent(type);
        doc.getElementsByTagName("value").item(0).setTextContent(pan);
        doc.getElementsByTagName("expDate").item(0).setTextContent(expDate);
        doc.getElementsByTagName("remark").item(0).setTextContent(createRemark);
        doc.getElementsByTagName("created").item(0).setTextContent(createDate);
        return doc;
    }

    public static Document fillGetItemPan(Document doc, String merchantId, String terminalId, String trackingId, String stopListId){
        doc.getElementsByTagName("MerchantID").item(0).setTextContent(merchantId);
        doc.getElementsByTagName("TerminalID").item(0).setTextContent(terminalId);
        doc.getElementsByTagName("trackingId").item(0).setTextContent(trackingId);
        doc.getElementsByTagName("stopListId").item(0).setTextContent(stopListId);
        return doc;
    }

    public static Document fillUpdateItemPan(Document doc, String merchantId, String terminalId, String trackingId, String stopListId, String expDate, String updateRemark){
        doc.getElementsByTagName("MerchantID").item(0).setTextContent(merchantId);
        doc.getElementsByTagName("TerminalID").item(0).setTextContent(terminalId);
        doc.getElementsByTagName("trackingId").item(0).setTextContent(trackingId);
        doc.getElementsByTagName("stopListId").item(0).setTextContent(stopListId);
        doc.getElementsByTagName("expDate").item(0).setTextContent(expDate);
        doc.getElementsByTagName("remark").item(0).setTextContent(updateRemark);
        return doc;
    }

    public static Document fillFindItemPan(Document doc, String merchantId, String terminalId, String trackingId, String type, String pan){
        doc.getElementsByTagName("MerchantID").item(0).setTextContent(merchantId);
        doc.getElementsByTagName("TerminalID").item(0).setTextContent(terminalId);
        doc.getElementsByTagName("trackingId").item(0).setTextContent(trackingId);
        doc.getElementsByTagName("type").item(0).setTextContent(type);
        doc.getElementsByTagName("value").item(0).setTextContent(pan);
        return doc;
    }

    public static Document fillDeleteItemPan(Document doc, String merchantId, String terminalId, String trackingId, String stopListId){
        doc.getElementsByTagName("MerchantID").item(0).setTextContent(merchantId);
        doc.getElementsByTagName("TerminalID").item(0).setTextContent(terminalId);
        doc.getElementsByTagName("trackingId").item(0).setTextContent(trackingId);
        doc.getElementsByTagName("stopListId").item(0).setTextContent(stopListId);
        return doc;
    }

    /* Onboarding API
    * fill xml*/

    public static Document fillCreateMerchant(
            Document doc,
            String MerchantID,
            String TerminalID,
            String AccountLogin,
            String ReqMerchantID,
            String Mcc,
            String ReqTerminalID,
            String Country,
            String City,
            String Street,
            String Name,
            String BankCode,
            String Currency,
            String siteUrl,
            String phone,
            String contact,
            String fax,
            String email,
            String zip,
            String remark,
            String notify_url,
            String success_url,
            String failure_url,
            String closeday_hour,
            String ipn,
            String terminal_type_id,
            String identification,
            String timeZone,
            String sms,
            String viber,
            String facebook){
        doc.getElementsByTagName("MerchantID").item(0).setTextContent(MerchantID);
        doc.getElementsByTagName("TerminalID").item(0).setTextContent(TerminalID);
        doc.getElementsByTagName("AccountLogin").item(0).setTextContent(AccountLogin);
        NodeList cells = doc.getElementsByTagName("CreateMerchantRequest");
        for(int i = 0; i < cells.getLength(); i++){
            Element item = (Element)cells.item(i);
            item.getElementsByTagName("MerchantID").item(0).setTextContent(ReqMerchantID);
            item.getElementsByTagName("Mcc").item(0).setTextContent(Mcc);
            item.getElementsByTagName("TerminalID").item(0).setTextContent(ReqTerminalID);
            item.getElementsByTagName("Country").item(0).setTextContent(Country);
            item.getElementsByTagName("City").item(0).setTextContent(City);
            item.getElementsByTagName("Street").item(0).setTextContent(Street);
            item.getElementsByTagName("Name").item(0).setTextContent(Name);
            item.getElementsByTagName("BankCode").item(0).setTextContent(BankCode);
            item.getElementsByTagName("Currency").item(0).setTextContent(Currency);
            item.getElementsByTagName("siteUrl").item(0).setTextContent(siteUrl);
            item.getElementsByTagName("phone").item(0).setTextContent(phone);
            item.getElementsByTagName("contact").item(0).setTextContent(contact);
            item.getElementsByTagName("fax").item(0).setTextContent(fax);
            item.getElementsByTagName("email").item(0).setTextContent(email);
            item.getElementsByTagName("zip").item(0).setTextContent(zip);
            item.getElementsByTagName("remark").item(0).setTextContent(remark);
            item.getElementsByTagName("notify_url").item(0).setTextContent(notify_url);
            item.getElementsByTagName("success_url").item(0).setTextContent(success_url);
            item.getElementsByTagName("failure_url").item(0).setTextContent(failure_url);
            item.getElementsByTagName("closeday_hour").item(0).setTextContent(closeday_hour);
            item.getElementsByTagName("ipn").item(0).setTextContent(ipn);
            item.getElementsByTagName("terminal_type_id").item(0).setTextContent(terminal_type_id);
            item.getElementsByTagName("identification").item(0).setTextContent(identification);
            item.getElementsByTagName("timeZone").item(0).setTextContent(timeZone);
            item.getElementsByTagName("sms").item(0).setTextContent(sms);
            item.getElementsByTagName("viber").item(0).setTextContent(viber);
            item.getElementsByTagName("facebook").item(0).setTextContent(facebook);
        }

        return doc;
    }

    public static Document fillCreateMcPaymentFacilitator(
            Document doc,
            String MerchantID,
            String TerminalID,
            String AccountLogin,
            String ReqMerchantID,
            String Mcc,
            String ReqTerminalID,
            String Country,
            String City,
            String Street,
            String Name,
            String BankCode,
            String Currency,
            String siteUrl,
            String phone,
            String contact,
            String fax,
            String email,
            String zip,
            String remark,
            String notify_url,
            String success_url,
            String failure_url,
            String closeday_hour,
            String ipn,
            String terminal_type_id,
            String identification,
            String mcPaymentFacilitatorId,
            String mcIndependentSalesOrgId,
            String mcSubMerchantId,
            String timeZone,
            String sms,
            String viber,
            String facebook){
        doc.getElementsByTagName("MerchantID").item(0).setTextContent(MerchantID);
        doc.getElementsByTagName("TerminalID").item(0).setTextContent(TerminalID);
        doc.getElementsByTagName("AccountLogin").item(0).setTextContent(AccountLogin);
        NodeList cells = doc.getElementsByTagName("CreateMerchantRequest");
        for(int i = 0; i < cells.getLength(); i++){
            Element item = (Element)cells.item(i);
            item.getElementsByTagName("MerchantID").item(0).setTextContent(ReqMerchantID);
            item.getElementsByTagName("Mcc").item(0).setTextContent(Mcc);
            item.getElementsByTagName("TerminalID").item(0).setTextContent(ReqTerminalID);
            item.getElementsByTagName("Country").item(0).setTextContent(Country);
            item.getElementsByTagName("City").item(0).setTextContent(City);
            item.getElementsByTagName("Street").item(0).setTextContent(Street);
            item.getElementsByTagName("Name").item(0).setTextContent(Name);
            item.getElementsByTagName("BankCode").item(0).setTextContent(BankCode);
            item.getElementsByTagName("Currency").item(0).setTextContent(Currency);
            item.getElementsByTagName("siteUrl").item(0).setTextContent(siteUrl);
            item.getElementsByTagName("phone").item(0).setTextContent(phone);
            item.getElementsByTagName("contact").item(0).setTextContent(contact);
            item.getElementsByTagName("fax").item(0).setTextContent(fax);
            item.getElementsByTagName("email").item(0).setTextContent(email);
            item.getElementsByTagName("zip").item(0).setTextContent(zip);
            item.getElementsByTagName("remark").item(0).setTextContent(remark);
            item.getElementsByTagName("notify_url").item(0).setTextContent(notify_url);
            item.getElementsByTagName("success_url").item(0).setTextContent(success_url);
            item.getElementsByTagName("failure_url").item(0).setTextContent(failure_url);
            item.getElementsByTagName("closeday_hour").item(0).setTextContent(closeday_hour);
            item.getElementsByTagName("ipn").item(0).setTextContent(ipn);
            item.getElementsByTagName("terminal_type_id").item(0).setTextContent(terminal_type_id);
            item.getElementsByTagName("identification").item(0).setTextContent(identification);
            item.getElementsByTagName("mcPaymentFacilitatorId").item(0).setTextContent(mcPaymentFacilitatorId);
            item.getElementsByTagName("mcIndependentSalesOrgId").item(0).setTextContent(mcIndependentSalesOrgId);
            item.getElementsByTagName("mcSubMerchantId").item(0).setTextContent(mcSubMerchantId);
            item.getElementsByTagName("timeZone").item(0).setTextContent(timeZone);
            item.getElementsByTagName("sms").item(0).setTextContent(sms);
            item.getElementsByTagName("viber").item(0).setTextContent(viber);
            item.getElementsByTagName("facebook").item(0).setTextContent(facebook);
        }

        return doc;
    }

    public static Document fillcreateVisaPaymentFacilitatorCliche(
            Document doc,
            String MerchantID,
            String TerminalID,
            String AccountLogin,
            String ReqMerchantID,
            String Mcc,
            String ReqTerminalID,
            String Country,
            String City,
            String Street,
            String Name,
            String BankCode,
            String Currency,
            String siteUrl,
            String phone,
            String contact,
            String fax,
            String email,
            String zip,
            String remark,
            String notify_url,
            String success_url,
            String failure_url,
            String closeday_hour,
            String ipn,
            String terminal_type_id,
            String identification,
            String visaPaymentFacilitatorId,
            String visaIndependentSalesOrgId,
            String visaSubMerchantId,
            String merchantCode,
            String terminalId,
            String merchantName,
            String timeZone,
            String sms,
            String viber,
            String facebook
    ){
        doc.getElementsByTagName("MerchantID").item(0).setTextContent(MerchantID);
        doc.getElementsByTagName("TerminalID").item(0).setTextContent(TerminalID);
        doc.getElementsByTagName("AccountLogin").item(0).setTextContent(AccountLogin);
        NodeList cells = doc.getElementsByTagName("CreateMerchantRequest");
        for(int i = 0; i < cells.getLength(); i++){
            Element item = (Element)cells.item(i);
            item.getElementsByTagName("MerchantID").item(0).setTextContent(ReqMerchantID);
            item.getElementsByTagName("Mcc").item(0).setTextContent(Mcc);
            item.getElementsByTagName("TerminalID").item(0).setTextContent(ReqTerminalID);
            item.getElementsByTagName("Country").item(0).setTextContent(Country);
            item.getElementsByTagName("City").item(0).setTextContent(City);
            item.getElementsByTagName("Street").item(0).setTextContent(Street);
            item.getElementsByTagName("Name").item(0).setTextContent(Name);
            item.getElementsByTagName("BankCode").item(0).setTextContent(BankCode);
            item.getElementsByTagName("Currency").item(0).setTextContent(Currency);
            item.getElementsByTagName("siteUrl").item(0).setTextContent(siteUrl);
            item.getElementsByTagName("phone").item(0).setTextContent(phone);
            item.getElementsByTagName("contact").item(0).setTextContent(contact);
            item.getElementsByTagName("fax").item(0).setTextContent(fax);
            item.getElementsByTagName("email").item(0).setTextContent(email);
            item.getElementsByTagName("zip").item(0).setTextContent(zip);
            item.getElementsByTagName("remark").item(0).setTextContent(remark);
            item.getElementsByTagName("notify_url").item(0).setTextContent(notify_url);
            item.getElementsByTagName("success_url").item(0).setTextContent(success_url);
            item.getElementsByTagName("failure_url").item(0).setTextContent(failure_url);
            item.getElementsByTagName("closeday_hour").item(0).setTextContent(closeday_hour);
            item.getElementsByTagName("ipn").item(0).setTextContent(ipn);
            item.getElementsByTagName("terminal_type_id").item(0).setTextContent(terminal_type_id);
            item.getElementsByTagName("identification").item(0).setTextContent(identification);
            item.getElementsByTagName("visaPaymentFacilitatorId").item(0).setTextContent(visaPaymentFacilitatorId);
            item.getElementsByTagName("visaIndependentSalesOrgId").item(0).setTextContent(visaIndependentSalesOrgId);
            item.getElementsByTagName("visaSubMerchantId").item(0).setTextContent(visaSubMerchantId);
            item.getElementsByTagName("merchantCode").item(0).setTextContent(merchantCode);
            item.getElementsByTagName("terminalId").item(0).setTextContent(terminalId);
            item.getElementsByTagName("merchantName").item(0).setTextContent(merchantName);
            item.getElementsByTagName("timeZone").item(0).setTextContent(timeZone);
            item.getElementsByTagName("sms").item(0).setTextContent(sms);
            item.getElementsByTagName("viber").item(0).setTextContent(viber);
            item.getElementsByTagName("facebook").item(0).setTextContent(facebook);
        }

        return doc;
    }

    public static Document fillGetMerchant(Document doc, String MerchantID, String TerminalID, String AccountLogin, String ReqMerchantID, String ReqTerminalID){
        doc.getElementsByTagName("MerchantID").item(0).setTextContent(MerchantID);
        doc.getElementsByTagName("TerminalID").item(0).setTextContent(TerminalID);
        doc.getElementsByTagName("AccountLogin").item(0).setTextContent(AccountLogin);
        NodeList cells = doc.getElementsByTagName("GetMerchantRequest");
        for(int i = 0; i < cells.getLength(); i++){
            Element item = (Element)cells.item(i);
            item.getElementsByTagName("MerchantID").item(0).setTextContent(ReqMerchantID);
            item.getElementsByTagName("TerminalID").item(0).setTextContent(ReqTerminalID);
        }
        return doc;
    }

    public static Document fillDeleteMerchant(Document doc, String MerchantID, String TerminalID, String AccountLogin, String ReqMerchantID, String ReqTerminalID){
        doc.getElementsByTagName("MerchantID").item(0).setTextContent(MerchantID);
        doc.getElementsByTagName("TerminalID").item(0).setTextContent(TerminalID);
        doc.getElementsByTagName("AccountLogin").item(0).setTextContent(AccountLogin);
        NodeList cells = doc.getElementsByTagName("DeleteMerchantRequest");
        for(int i = 0; i < cells.getLength(); i++){
            Element item = (Element)cells.item(i);
            item.getElementsByTagName("MerchantID").item(0).setTextContent(ReqMerchantID);
            item.getElementsByTagName("TerminalID").item(0).setTextContent(ReqTerminalID);
        }
        return doc;
    }

    public static Document fillUpdateMerchant(Document doc, String MerchantID, String TerminalID, String AccountLogin, String ReqMerchantID, String ReqTerminalID, String updField){
        doc.getElementsByTagName("MerchantID").item(0).setTextContent(MerchantID);
        doc.getElementsByTagName("TerminalID").item(0).setTextContent(TerminalID);
        doc.getElementsByTagName("AccountLogin").item(0).setTextContent(AccountLogin);
        NodeList cells = doc.getElementsByTagName("UpdateMerchantRequest");
        for(int i = 0; i < cells.getLength(); i++){
            Element item = (Element)cells.item(i);
            item.getElementsByTagName("MerchantID").item(0).setTextContent(ReqMerchantID);
            item.getElementsByTagName("TerminalID").item(0).setTextContent(ReqTerminalID);
            item.getElementsByTagName("Name").item(0).setTextContent(updField);
        }
        return doc;
    }

    public static Document fillGetStatus(Document doc, String MerchantID, String TerminalID, String RequestID){
        doc.getElementsByTagName("MerchantID").item(0).setTextContent(MerchantID);
        doc.getElementsByTagName("TerminalID").item(0).setTextContent(TerminalID);
        doc.getElementsByTagName("RequestID").item(0).setTextContent(RequestID);
        return doc;
    }

    /* print request/response Merchant Onboarding API*/

    public static String printRequestCreateMerchant(Document doc) {
        try {
            String merchantID = doc.getElementsByTagName("MerchantID").item(0).getTextContent();
            String terminalID = doc.getElementsByTagName("TerminalID").item(0).getTextContent();
            String accountLogin = doc.getElementsByTagName("AccountLogin").item(0).getTextContent();
            NodeList cells = doc.getElementsByTagName("CreateMerchantRequest");

            for(int i = 0; i < cells.getLength(); i++){
                Element item = (Element)cells.item(i);
                String reqMerchantID = item.getElementsByTagName("MerchantID").item(0).getTextContent();
                String reqTerminalID = item.getElementsByTagName("TerminalID").item(0).getTextContent();
            }
        } catch (Exception e) {
            System.out.println(">>> Tag is missing in response");
        }

        doc = convertStringToXmlDocument(toStringApi(doc));
        System.out.println("===============================================");
        return nodeListToString(doc.getElementsByTagName("XMLCreateMerchantRequest"));
    }


    public static String printResponseCreateMerchant(Document doc) {
        doc = convertStringToXmlDocument(toStringApi(doc));
        System.out.println("-----------------------------------------------");
        return nodeListToString(doc.getElementsByTagName("XMLCreateMerchantResponse"));
    }

    public static String printRequestCreateMcPaymentFacilitator(Document doc) {
        try {
            String merchantID = doc.getElementsByTagName("MerchantID").item(0).getTextContent();
            String terminalID = doc.getElementsByTagName("TerminalID").item(0).getTextContent();
            String accountLogin = doc.getElementsByTagName("AccountLogin").item(0).getTextContent();
            NodeList cells = doc.getElementsByTagName("CreateMerchantRequest");

            for(int i = 0; i < cells.getLength(); i++){
                Element item = (Element)cells.item(i);
                String reqMerchantID = item.getElementsByTagName("MerchantID").item(0).getTextContent();
                String reqTerminalID = item.getElementsByTagName("TerminalID").item(0).getTextContent();
            }
        } catch (Exception e) {
            System.out.println(">>> Tag is missing in response");
        }

        doc = convertStringToXmlDocument(toStringApi(doc));
        System.out.println("===============================================");
        return nodeListToString(doc.getElementsByTagName("XMLCreateMerchantRequest"));
    }


    public static String printResponseCreateMcPaymentFacilitator(Document doc) {
        doc = convertStringToXmlDocument(toStringApi(doc));
        System.out.println("-----------------------------------------------");
        return nodeListToString(doc.getElementsByTagName("XMLCreateMerchantResponse"));
    }

    public static String printRequestCreateVisaPaymentFacilitatorCliche(Document doc) {
        try {
            String merchantID = doc.getElementsByTagName("MerchantID").item(0).getTextContent();
            String terminalID = doc.getElementsByTagName("TerminalID").item(0).getTextContent();
            String accountLogin = doc.getElementsByTagName("AccountLogin").item(0).getTextContent();
            NodeList cells = doc.getElementsByTagName("CreateMerchantRequest");

            for(int i = 0; i < cells.getLength(); i++){
                Element item = (Element)cells.item(i);
                String reqMerchantID = item.getElementsByTagName("MerchantID").item(0).getTextContent();
                String reqTerminalID = item.getElementsByTagName("TerminalID").item(0).getTextContent();
            }
        } catch (Exception e) {
            System.out.println(">>> Tag is missing in response");
        }

        doc = convertStringToXmlDocument(toStringApi(doc));
        System.out.println("===============================================");
        return nodeListToString(doc.getElementsByTagName("XMLCreateMerchantRequest"));
    }


    public static String printResponseCreateVisaPaymentFacilitatorCliche(Document doc) {
        doc = convertStringToXmlDocument(toStringApi(doc));
        System.out.println("-----------------------------------------------");
        return nodeListToString(doc.getElementsByTagName("XMLCreateMerchantResponse"));
    }

    public static String printRequestGetMerchant(Document doc) {
        try {
            String merchantID = doc.getElementsByTagName("MerchantID").item(0).getTextContent();
            String terminalID = doc.getElementsByTagName("TerminalID").item(0).getTextContent();
            String accountLogin = doc.getElementsByTagName("AccountLogin").item(0).getTextContent();
            NodeList cells = doc.getElementsByTagName("GetMerchantRequest");

            for(int i = 0; i < cells.getLength(); i++){
                Element item = (Element)cells.item(i);
                String reqMerchantID = item.getElementsByTagName("MerchantID").item(0).getTextContent();
                String reqTerminalID = item.getElementsByTagName("TerminalID").item(0).getTextContent();
            }
        } catch (Exception e) {
            System.out.println(">>> Tag is missing in response");
        }

        doc = convertStringToXmlDocument(toStringApi(doc));

        System.out.println("===============================================");
        return nodeListToString(doc.getElementsByTagName("XMLGetMerchantRequest"));
    }


    public static String printResponseGetMerchant(Document doc) {
        doc = convertStringToXmlDocument(toStringApi(doc));
        System.out.println("-----------------------------------------------");
        return nodeListToString(doc.getElementsByTagName("XMLGetMerchantResponse"));
    }

    public static String printRequestDeleteMerchant(Document doc) {
        try {
            String merchantID = doc.getElementsByTagName("MerchantID").item(0).getTextContent();
            String terminalID = doc.getElementsByTagName("TerminalID").item(0).getTextContent();
            String accountLogin = doc.getElementsByTagName("AccountLogin").item(0).getTextContent();
            NodeList cells = doc.getElementsByTagName("DeleteMerchantRequest");

            for(int i = 0; i < cells.getLength(); i++){
                Element item = (Element)cells.item(i);
                String reqMerchantID = item.getElementsByTagName("MerchantID").item(0).getTextContent();
                String reqTerminalID = item.getElementsByTagName("TerminalID").item(0).getTextContent();
            }
        } catch (Exception e) {
            System.out.println(">>> Tag is missing in response");
        }

        doc = convertStringToXmlDocument(toStringApi(doc));

        System.out.println("===============================================");
        return nodeListToString(doc.getElementsByTagName("XMLDeleteMerchantRequest"));
    }

    public static String printResponseDeleteMerchant(Document doc) {
        doc = convertStringToXmlDocument(toStringApi(doc));
        System.out.println("-----------------------------------------------");
        return nodeListToString(doc.getElementsByTagName("XMLDeleteMerchantResponse"));
    }

    public static String printRequestUpdateMerchant(Document doc) {
        try {
            String merchantID = doc.getElementsByTagName("MerchantID").item(0).getTextContent();
            String terminalID = doc.getElementsByTagName("TerminalID").item(0).getTextContent();
            String accountLogin = doc.getElementsByTagName("AccountLogin").item(0).getTextContent();
            NodeList cells = doc.getElementsByTagName("UpdateMerchantRequest");

            for(int i = 0; i < cells.getLength(); i++){
                Element item = (Element)cells.item(i);
                String reqMerchantID = item.getElementsByTagName("MerchantID").item(0).getTextContent();
                String reqTerminalID = item.getElementsByTagName("TerminalID").item(0).getTextContent();
                String Name_new = item.getElementsByTagName("Name").item(0).getTextContent();
            }
        } catch (Exception e) {
            System.out.println(">>> Tag is missing in response");
        }

        doc = convertStringToXmlDocument(toStringApi(doc));

        System.out.println("===============================================");
        return nodeListToString(doc.getElementsByTagName("UpdateMerchantRequest"));
    }


    public static String printResponseUpdateMerchant(Document doc) {
        doc = convertStringToXmlDocument(toStringApi(doc));
        System.out.println("-----------------------------------------------");
        return nodeListToString(doc.getElementsByTagName("XMLUpdateMerchantResponse"));
    }

    public static String printRequestGetStatus(Document doc) {
        try {
            String merchantID = doc.getElementsByTagName("MerchantID").item(0).getTextContent();
            String terminalID = doc.getElementsByTagName("TerminalID").item(0).getTextContent();
            String requestID = doc.getElementsByTagName("RequestID").item(0).getTextContent();
          } catch (Exception e) {
            System.out.println(">>> Tag is missing in response");
        }

        doc = convertStringToXmlDocument(toStringApi(doc));

        System.out.println("===============================================");
        return nodeListToString(doc.getElementsByTagName("XMLGetStatusRequest"));
    }


    public static String printResponseGetStatus(Document doc) {
        doc = convertStringToXmlDocument(toStringApi(doc));
        System.out.println("-----------------------------------------------");
        return nodeListToString(doc.getElementsByTagName("XMLGetStatusResponse"));
    }

    public static String toStringApi(Document doc) {
        try {
            StringWriter sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(doc), new StreamResult(sw));

            String str = sw.toString().replaceAll("(?:>)(\\s*)<", "><");
            str = str.replaceAll("(?:>)(\\s*)", ">");

            return str;
        } catch (Exception ex) {
            throw new RuntimeException("Error converting to String", ex);
        }
    }

    /* Onboarding API */

    public static Document updateMTCardDataFunding(Document request, String[] card) {
        request.getElementsByTagName("CardNumber").item(0).setTextContent(card[0]);
        request.getElementsByTagName("ExpYear").item(0).setTextContent(card[1]);
        request.getElementsByTagName("ExpMonth").item(0).setTextContent(card[2]);
        request.getElementsByTagName("CVV2").item(0).setTextContent(card[3]);
        return request;
    }

    public static String printRequestMT(Document doc) {
        try {
            String cardNumberFunding = doc.getElementsByTagName("CardNumber").item(0).getTextContent();
            String cvv = doc.getElementsByTagName("CVV2").item(0).getTextContent();
            doc.getElementsByTagName("CardNumber").item(0).setTextContent(maskString(cardNumberFunding, 6, 12));
            doc.getElementsByTagName("CVV2").item(0).setTextContent(maskString(cvv, 0, 3));
        } catch (Exception e) {
            System.out.println(">>> Tag is missing in response");
        }
        try {
            String cardNumberPayment = doc.getElementsByTagName("RecipientCardNumber").item(0).getTextContent();
            doc.getElementsByTagName("RecipientCardNumber").item(0).setTextContent(maskString(cardNumberPayment, 6, 12));
        } catch (Exception e) {
            System.out.println(">>> Tag RecipientCardNumber is missing in response");
        }
        doc = convertStringToXmlDocument(convertXMLDocumentToString(doc).replaceAll("(?:>)(\\s*)<", "><"));

        System.out.println("===============================================");
        return nodeListToString(doc.getElementsByTagName("TransactionRequest"));
    }

    public static String printResponseMT(Document doc) {
        System.out.println("-----------------------------------------------");
        NodeList nodeList = doc.getElementsByTagName("TransactionResponse");
        nodeList.item(0).removeChild(nodeList.item(0).getLastChild());
        return nodeListToString(nodeList);
    }

    public static String printReversalMT(Document doc, String tag) {
        System.out.println("-----------------------------------------------");
        NodeList nodeList = doc.getElementsByTagName(tag);
        if (tag.equals("ReversalResponse")) {
            nodeList.item(0).removeChild(nodeList.item(0).getLastChild());
        }
        return nodeListToString(nodeList);
    }

    public static Document fillValueByTags(Document dataFrom, Document dataTo, String tag) {
        String fromValue = dataFrom.getElementsByTagName(tag).item(0).getTextContent();
        dataTo.getElementsByTagName(tag).item(0).setTextContent(fromValue);
        return dataTo;
    }

    public static Document fillValueByTagsFR(Document dataFrom, Document dataTo, String tag) {
        // Проверяем, существует ли элемент в dataFrom
        if (dataFrom != null && dataFrom.getElementsByTagName(tag).getLength() > 0 && dataFrom.getElementsByTagName(tag).item(0) != null) {
            String fromValue = dataFrom.getElementsByTagName(tag).item(0).getTextContent();
            if (dataTo != null && dataTo.getElementsByTagName(tag).getLength() > 0 && dataTo.getElementsByTagName(tag).item(0) != null) {
                dataTo.getElementsByTagName(tag).item(0).setTextContent(fromValue);
            } else {
         //       System.out.println("Tag '" + tag + "' not found in dataTo document.");
            }
        } else {
        //    System.out.println("Tag '" + tag + "' not found in dataFrom document.");
        }
        return dataTo;
    }

    /**
     * for Active Server
     */

    public static String printASRequest(Document doc) {
        try {
            String cardNum = doc.getElementsByTagName("CardNum").item(0).getTextContent();
            doc.getElementsByTagName("CardNum").item(0).setTextContent(maskString(cardNum, 6, 12));
        } catch (Exception e) {
            e.printStackTrace();
        }
        doc = convertStringToXmlDocument(convertXMLDocumentToString(doc).replaceAll("(?:>)(\\s*)<", "><"));

        System.out.println("===============================================");
        return nodeListToString(doc.getElementsByTagName("EMV3DSRequest"));
    }

    public static String printASResponse(Document doc) {
        System.out.println("-----------------------------------------------");
        return nodeListToString(doc.getElementsByTagName("EMV3DSResponse"));
    }

    /**
     * for MPI
     */
    public static Document fillRequestMPIData(Document request, String[] card, String merch, String term, String desc) {
        fillMerchant(request, merch, term);
        request.getElementsByTagName("CardNum").item(0).setTextContent(card[0]);
        request.getElementsByTagName("ExpYear").item(0).setTextContent(card[1]);
        request.getElementsByTagName("ExpMonth").item(0).setTextContent(card[2]);
        request.getElementsByTagName("Message").item(0).getAttributes().item(0).setTextContent(sdf.format(date));
        request.getElementsByTagName("Description").item(0).setTextContent(desc);
        return request;
    }

    public static String printRequestMpiEnroll(Document doc) {
        try {
            String cardNum = doc.getElementsByTagName("CardNum").item(0).getTextContent();
            doc.getElementsByTagName("CardNum").item(0).setTextContent(maskString(cardNum, 6, 12));
        } catch (NullPointerException n) {
            System.out.println("Tag is missing");
        }
        doc = convertStringToXmlDocument(convertXMLDocumentToString(doc).replaceAll("(?:>)(\\s*)<", "><"));
        System.out.println("===============================================");
        return nodeListToString(doc.getElementsByTagName("XMLMPIRequest"));
    }

    public static String printRequestMpiAuth(Document doc) {
        try {
            String pares = doc.getElementsByTagName("PaRes").item(0).getTextContent();
            doc.getElementsByTagName("PaRes").item(0).setTextContent(pares.substring(0, 10) + " ... " + pares.substring(pares.length() - 10));
        } catch (NullPointerException n) {
            System.out.println("Tag is missing");
        }
        doc = convertStringToXmlDocument(convertXMLDocumentToString(doc).replaceAll("(?:>)(\\s*)<", "><"));
        System.out.println("===============================================");
        return nodeListToString(doc.getElementsByTagName("XMLMPIRequest"));
    }

    public static String printResponseMPI(Document doc) {
        System.out.println("-----------------------------------------------");
        return nodeListToString(doc.getElementsByTagName("XMLMPIResponse"));
    }
}
