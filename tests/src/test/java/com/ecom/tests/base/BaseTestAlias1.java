package com.ecom.tests.base;

import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import static com.ecom.core.config.EnvData.*;


public class BaseTestAlias1 extends BaseApiTest { //todo может переделать?

    protected String baseUrl = URL_TOMCAT;
    protected String TerminalID = terminalID_aval1;
    protected String MerchantID = merchantID_aval1;
    protected String Terminal2ID = terminalID_aval4;
    protected String Merchant2ID = merchantID_aval4;
    protected static String alias1Id = Long.toString(System.currentTimeMillis());
    protected static String alias2Id = Long.toString(System.currentTimeMillis()+1).toString();
    //protected String RequestID = properties.getProperty("RequestID");

    protected String verifyXmlResponse(String xml, SoftAssert softAssertion, String tagName) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        // Перевірка, що елемент tagName не порожній
        NodeList guidNodes = doc.getElementsByTagName(tagName);
        boolean isGuidNotEmpty = guidNodes.getLength() > 0 && !guidNodes.item(0).getTextContent().trim().isEmpty();
        System.out.println(tagName +" is not empty: " + isGuidNotEmpty);
        softAssertion.assertTrue(isGuidNotEmpty, "<" + tagName + "> is empty");
        String guid = guidNodes.item(0).getTextContent().trim();
        // Перевірка значення <Code>
        NodeList codeNodes = doc.getElementsByTagName("Code");
        boolean isCodePresent = codeNodes.getLength() > 0;
        System.out.println("Code is present: " + isCodePresent);
        softAssertion.assertTrue(isCodePresent, "<Code> element not found");
        if (isCodePresent) {
            String codeValue = codeNodes.item(0).getTextContent().trim();
            boolean isCodeCorrect = "000".equals(codeValue);
            System.out.println("Code is 000: " + isCodeCorrect);
            softAssertion.assertTrue(isCodeCorrect, "Code is not 000");
        }
        return guid;
    }

    protected String printSpecificTags(String xml, String [] specificTags) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        StringBuilder result = new StringBuilder();
        for (String tag : specificTags) {
            NodeList nodeList = doc.getElementsByTagName(tag);
            if (nodeList.getLength() > 0) {
                String content = nodeList.item(0).getTextContent().trim();
                result.append("<").append(tag).append(">").append(content).append("</").append(tag).append(">");
            }
        }
        return result.toString().trim();
    }

    protected String formatXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        DOMSource source = new DOMSource(doc);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(outputStream);
        transformer.transform(source, result);

        return new String(outputStream.toByteArray(), StandardCharsets.UTF_8).trim();
    }

    protected String generateUniqueRequestID() {
        return "REQ" + System.currentTimeMillis();
    }
}