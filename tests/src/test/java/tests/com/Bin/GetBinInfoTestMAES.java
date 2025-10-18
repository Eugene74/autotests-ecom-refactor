package tests.com.Bin;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class GetBinInfoTestMAES extends BaseTestBinInfo {

    @Test() //TODO: Remove enabled = false annotation when ready to run this test
    public void testGetBinInfoForMAES() throws Exception {
        String xmlContent = getResourceContent(XML_GET_INFO_RESOURCE_MAES);
        String requestId = generateUniqueRequestID();

        if (MerchantID == null || TerminalID == null || requestId == null) {
            throw new IllegalArgumentException("One of the required properties is null. MerchantID: " + MerchantID +
                    ", TerminalID: " + TerminalID + ", RequestID: " + requestId);
        }

        Map<String, String> params = new HashMap<>();
        params.put("MerchantID", MerchantID);
        params.put("TerminalID", TerminalID);
        params.put("RequestID", requestId);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        HttpResponse response = sendPostRequest(URLBinInfo, xmlContent);
        assertNotNull(response);
        int statusCode = response.getStatusLine().getStatusCode();

        System.out.println("Received status code from the server: " + statusCode);

        // Перевірка статус-коду HTTP
        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");

        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        //System.out.println("Response (XML): " + responseContent);

        // Парсинг та верифікація XML відповіді
        SoftAssert softAssertion = new SoftAssert();
        verifyXmlResponse(responseContent, softAssertion);
        softAssertion.assertAll();
    }

    private void verifyXmlResponse(String xml, SoftAssert softAssertion) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        // Перевірка, що елемент <ListBinInfo> не порожній
        NodeList listBinInfoNodes = doc.getElementsByTagName("ListBinInfo");
        boolean isListBinInfoNotEmpty = listBinInfoNodes.getLength() > 0 && listBinInfoNodes.item(0).getTextContent().trim().length() > 0;
        System.out.println("ListBinInfo is not empty: " + isListBinInfoNotEmpty);
        assertTrue(isListBinInfoNotEmpty, "<ListBinInfo> is empty");

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

    private String maskValue(String value, int visibleChars) {
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

    private String generateUniqueRequestID() {
        return "REQ" + System.currentTimeMillis();
    }
}
