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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class UpdateAlias1 extends BaseTestAlias1 {

    @Test(dependsOnMethods = "tests.com.Alias1.CreateAlias1.testCreateAlias1")
    public void testUpdateAlias1() throws Exception {
        String xmlContent = getResourceContent(XML_UPDATE_ALIAS1_RESOURCE);

        // Використовуйте збережені значення
        String requestId = SharedDataStore.requestId;
        String guidFromResponse = SharedDataStore.guidFromResponse;

        // Отримання GUID з бази даних
        String guidFromDb = getGuidFromDb(requestId);
        assertEquals(guidFromDb, guidFromResponse, "GUID from DB doesn't match response GUID");

        Map<String, String> params = new HashMap<>();
        params.put("MerchantID", MerchantID);
        params.put("TerminalID", TerminalID);
        params.put("RequestID", requestId);
        params.put("GUID", guidFromDb);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        HttpResponse response = sendPutRequest(URLAlias1Update, xmlContent);  // Використайте sendPutRequest тут
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

    private String getGuidFromDb(String requestId) throws Exception {
        try (Connection connection = getDBConnection()) {
            String sql = "SELECT GUID FROM VA_USER_REQUEST WHERE TRACKING_ID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, requestId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getString("GUID");
                } else {
                    throw new IllegalArgumentException("No entry found with Tracking ID: " + requestId);
                }
            }
        }
    }

    private void verifyXmlResponse(String xml, SoftAssert softAssertion) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        // Перевірка, що елемент <guid> не порожній
        NodeList guidNodes = doc.getElementsByTagName("guid");
        boolean isGuidNotEmpty = guidNodes.getLength() > 0 && guidNodes.item(0).getTextContent().trim().length() > 0;
        System.out.println("guid is not empty: " + isGuidNotEmpty);
        softAssertion.assertTrue(isGuidNotEmpty, "<guid> is empty");

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
                "guid"
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
