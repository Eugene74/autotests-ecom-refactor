package tests.com.Alias2;

import tests.com.Alias1.BaseTestAlias1;
import tests.com.Alias1.SharedDataStore;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
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
import java.util.Objects;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class DeleteAlias2 extends BaseTestAlias1 {

    private static final String XML_DELETE_ALIAS2_RESOURCE = "/XMLAlias2/DeleteAlias2.xml";

    @Test(dependsOnMethods = "tests.com.Alias2.CreateAlias2.testCreateAlias2")
    public void testDeleteAlias2() throws Exception {
        String xmlContent = getResourceContent(XML_DELETE_ALIAS2_RESOURCE);

        // Використання збережених значень з SharedDataStore
        String trackingId = SharedDataStore.requestId;
        String aliasId = getAliasIdFromDatabase(trackingId);

        if (aliasId == null) {
            throw new IllegalArgumentException("The aliasId is not found in the database.");
        }

        Map<String, String> params = new HashMap<>();
        params.put("Merchant2ID", Merchant2ID);
        params.put("Terminal2ID", Terminal2ID);
        params.put("TrackingId", trackingId);
        params.put("aliasId", aliasId);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            xmlContent = xmlContent.replace("${" + entry.getKey() + "}", Objects.requireNonNull(entry.getValue(), "Value for " + entry.getKey() + " is null"));
        }

        // Друк XML-запиту для налагодження
        System.out.println("Request XML:  " + formatXml(xmlContent));

                HttpResponse response = sendDeleteRequest(URLAlias2Delete, xmlContent);
        assertNotNull(response);
        int statusCode = response.getStatusLine().getStatusCode();

        // Друк статус-коду для налагодження
        System.out.println("Received status code from the server: " + statusCode);
        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");

        String responseContent = null;
        if (response.getEntity() != null) {
            responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");

            // Парсинг та верифікація XML відповіді
            SoftAssert softAssertion = new SoftAssert();
            verifyXmlResponse(responseContent, softAssertion);
            softAssertion.assertAll();
        } else {
            System.out.println("No response content received");
        }
    }

    private String getAliasIdFromDatabase(String trackingId) throws Exception {
        String aliasId = null;
        try (Connection connection = getDBConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT ALIAS_ID FROM VA_USER_REQUEST WHERE TRACKING_ID = ? AND TYPE = 'CREATE_ALIAS'")) {

            statement.setString(1, trackingId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    aliasId = resultSet.getString("ALIAS_ID");
                }

            }
        }
        return aliasId;
    }

    private void verifyXmlResponse(String xml, SoftAssert softAssertion) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        // Перевірка наявності елемента <Code>
        NodeList codeNodes = doc.getElementsByTagName("Code");
        boolean isCodePresent = codeNodes.getLength() > 0;
        System.out.println("Code is present: " + isCodePresent);
        softAssertion.assertTrue(isCodePresent, "<Code> element not found");

        // Перевірка значення елемента <Code>
        if (isCodePresent) {
            String codeValue = codeNodes.item(0).getTextContent().trim();
            boolean isCodeCorrect = "000".equals(codeValue);
            System.out.println("Code is 000: " + isCodeCorrect);
            softAssertion.assertTrue(isCodeCorrect, "Code is not 000");
        }
    }

    private String formatXml(String xml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        transformer.transform(new DOMSource(doc), new StreamResult(outputStream));
        return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
    }
}
