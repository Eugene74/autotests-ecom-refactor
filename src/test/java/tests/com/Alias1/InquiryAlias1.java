package tests.com.Alias1;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;
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
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class InquiryAlias1 extends BaseTestAlias1 {

    private static final String XML_INQUIRY_ALIAS1_RESOURCE = "/XMLAlias1/InquiryAlias1.xml";

    @Test(dependsOnMethods = "tests.com.Alias1.CreateAlias1.testCreateAlias1")
    public void testAliasInquiry() throws Exception {
        String xmlContent = getResourceContent(XML_INQUIRY_ALIAS1_RESOURCE);

        // Використовуйте збережені значення з SharedDataStore
        String requestId = SharedDataStore.requestId;
        String guidFromResponse = SharedDataStore.guidFromResponse;

        if (MerchantID == null || TerminalID == null || requestId == null) {
            throw new IllegalArgumentException("One of the required properties is null. MerchantID: " + MerchantID +
                    ", TerminalID: " + TerminalID + ", RequestID: " + requestId);
        }

        Map<String, String> params = new HashMap<>();
        params.put("MerchantID", MerchantID);
        params.put("TerminalID", TerminalID);
        params.put("RequestID", requestId);
        params.put("GUID", guidFromResponse);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        HttpResponse response = sendPostRequest(URLAlias1Inguiry, xmlContent);
        assertNotNull(response);
        int statusCode = response.getStatusLine().getStatusCode();

        System.out.println("Received status code from the server: " + statusCode);
        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");

        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println("Response (XML):" + printSpecificTags(responseContent));

                // Парсинг та верифікація XML відповіді
                SoftAssert softAssertion = new SoftAssert();
        verifyXmlResponse(responseContent, softAssertion);
        softAssertion.assertAll();
    }

    private void verifyXmlResponse(String xml, SoftAssert softAssertion) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        // Перевірка, що елемент <InquiryAliasResult> не порожній
        NodeList inquiryResultNodes = doc.getElementsByTagName("InquiryAliasResult");
        boolean isInquiryResultNotEmpty = inquiryResultNodes.getLength() > 0 && inquiryResultNodes.item(0).getTextContent().trim().length() > 0;
        System.out.println("InquiryAliasResult is not empty: " + isInquiryResultNotEmpty);
        softAssertion.assertTrue(isInquiryResultNotEmpty, "<InquiryAliasResult> is empty");

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
    }

    private String printSpecificTags(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        String[] specificTags = {
                "MerchantId",
                "TerminalId",
                "TrackingId",
                "alias",
                "paymentInstrOwner"
        };

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

    private String generateUniqueRequestID() {
        return "REQ" + System.currentTimeMillis();
    }
}
