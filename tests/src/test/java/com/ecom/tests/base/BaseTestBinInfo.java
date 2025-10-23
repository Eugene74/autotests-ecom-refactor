package com.ecom.tests.base;

import static com.ecom.core.config.EnvData.*;
import static org.testng.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BaseTestBinInfo
    extends BaseApiTest { // todo потом переделать(или убрать) это промежуточный BaseTestBinInfo?
  protected String baseUrl = URL_TOMCAT;
  protected String TerminalID = TERMINAL_ID_AVAL_1;
  protected String MerchantID = MERCHANT_ID_AVAL_1;
  protected String Terminal2ID = TERMINAL_ID_AVAL_4;
  protected String Merchant2ID = MERCHANT_ID_AVAL_4;

  // todo  create a utility class and move it there
  protected String maskValue(String value, int visibleChars) {
    if (value.length() <= visibleChars) {
      return value;
    }
    StringBuilder maskedValue = new StringBuilder();
    maskedValue.append(value.substring(0, visibleChars));
    for (int i = 0; i < value.length() - visibleChars; i++) {
      maskedValue.append('*');
    }
    return maskedValue.toString();
  }

  protected void verifyXmlResponse(String xml, SoftAssert softAssertion, String tagName)
      throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

    // Перевірка, що елемент <ListBinInfo> не порожній
    NodeList listBinInfoNodes = doc.getElementsByTagName(tagName);
    boolean isListBinInfoNotEmpty =
        listBinInfoNodes.getLength() > 0
            && !listBinInfoNodes.item(0).getTextContent().trim().isEmpty();
    System.out.println(tagName + " is not empty: " + isListBinInfoNotEmpty);
    assertTrue(isListBinInfoNotEmpty, "<" + tagName + "> is empty");

    // Перевірка значення <Code>
    NodeList codeNodes = doc.getElementsByTagName("Code");
    boolean isCodePresent = codeNodes.getLength() > 0;
    assertTrue(isCodePresent, "<Code> element not found");
    if (isCodePresent) {
      String codeValue = codeNodes.item(0).getTextContent().trim();
      boolean isCodeCorrect = "000".equals(codeValue);
      System.out.println("Code is 000: " + isCodeCorrect);
      assertTrue(isCodeCorrect, "Code is not 000");
    }

    // Вивід тегів з <BinInfo> з маскуванням тільки для MinAccountRange і MaxAccountRange
    NodeList binInfoNodes = doc.getElementsByTagName("BinInfo");
    if (binInfoNodes.getLength() > 0) {
      NodeList childNodes = binInfoNodes.item(0).getChildNodes();
      for (int i = 0; i < childNodes.getLength(); i++) {
        if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
          String nodeName = childNodes.item(i).getNodeName();
          String originalValue = childNodes.item(i).getTextContent();
          String valueToPrint = originalValue;
          if ("MinAccountRange".equals(nodeName) || "MaxAccountRange".equals(nodeName)) {
            valueToPrint = maskValue(originalValue, 6);
          }
          System.out.println(nodeName + ": " + valueToPrint);
        }
      }
    }
  }

  protected String generateUniqueRequestID() {
    return "REQ" + System.currentTimeMillis();
  }
}
