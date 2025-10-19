package tests.com.Alias1;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
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
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class GetAlias1 extends BaseTestAlias1 {

    @Test(dependsOnMethods = "tests.com.Alias1.UpdateAlias1.testUpdateAlias1")
    public void testGetAlias1() throws Exception {
        String xmlContent = getResourceContent(XML_GET_ALIAS1_RESOURCE);

        String requestId = SharedDataStore.requestId;
        String guidFromResponse = SharedDataStore.guidFromResponse;

        Map<String, String> params = new HashMap<>();
        params.put("MerchantID", MerchantID);
        params.put("TerminalID", TerminalID);
        params.put("RequestID", requestId);
        params.put("GUID", guidFromResponse);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        HttpResponse response = sendPostRequest(URLAlias1Get, xmlContent);
        assertNotNull(response);
        int statusCode = response.getStatusLine().getStatusCode();

        System.out.println("Received status code from the server: " + statusCode);
        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");

        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");

        SoftAssert softAssertion = new SoftAssert();
        String maskedResponse = maskRecipientPAN(responseContent);
        printSpecificTags(maskedResponse);   // Друкуємо тільки теги
        verifyXmlResponse(maskedResponse, softAssertion);
        softAssertion.assertAll();
    }

    private String maskRecipientPAN(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        NodeList recipientPANNodes = doc.getElementsByTagName("recipientPrimaryAccountNumber");
        for (int i = 0; i < recipientPANNodes.getLength(); i++) {
            Node node = recipientPANNodes.item(i);
            String originalValue = node.getTextContent().trim();
            String maskedValue = originalValue.replaceAll(".", "*");
            node.setTextContent(maskedValue);
        }

        // Перетворення документу назад в строку
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        DOMSource source = new DOMSource(doc);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        StreamResult result = new StreamResult(outputStream);
        transformer.transform(source, result);

        return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
    }

    private void verifyXmlResponse(String xml, SoftAssert softAssertion) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        NodeList guidNodes = doc.getElementsByTagName("guid");
        boolean isGuidNotEmpty = guidNodes.getLength() > 0 && guidNodes.item(0).getTextContent().trim().length() > 0;
        System.out.println("guid is not empty: " + isGuidNotEmpty);
        softAssertion.assertTrue(isGuidNotEmpty, "<guid> is empty");

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
    }

    private void printSpecificTags(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        String[] specificTags = {
                "MerchantId",
                "TerminalId",
                "TrackingId",
                "guid"
        };

        for (String tag : specificTags) {
            NodeList nodeList = doc.getElementsByTagName(tag);
            if (nodeList.getLength() > 0) {
                String content = nodeList.item(0).getTextContent().trim();
                System.out.println("<" + tag + ">" + content + "</" + tag + ">");
            }
        }
    }

    private String formatXml(String xml) throws Exception {
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
}
