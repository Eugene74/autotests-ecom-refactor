package tests.com.Alias2;

import tests.com.Alias1.BaseTestAlias1;
import tests.com.Alias1.SharedDataStore;
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

public class UpdateAlias2 extends BaseTestAlias1 {

    @Test
    public void testUpdateAlias2() throws Exception {
        // Отримання значення aliasId з бази даних
        String aliasId = getAliasIdFromDatabase(SharedDataStore.trackingId);

        if (aliasId == null) {
            throw new IllegalArgumentException("The aliasId is not set.");
        }

        SharedDataStore.aliasId = aliasId;
        System.out.println("Fetched aliasId from DB: " + aliasId);

        // Складання XML-запиту
        String xmlContent = getResourceContent(XML_UPDATE_ALIAS2_RESOURCE);

        // Підставляння змінних у XML-запит
        Map<String, String> params = new HashMap<>();
        params.put("Merchant2ID", Merchant2ID);
        params.put("Terminal2ID", Terminal2ID);
        params.put("TrackingId", SharedDataStore.trackingId);
        params.put("aliasId", aliasId);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        // Друк XML-запиту для налагодження
      //  System.out.println("Request XML: " + xmlContent);

        // Відправка PUT-запиту
        HttpResponse response = sendPutRequest(URLAlias2Update, xmlContent);
        assertNotNull(response);

        // Друкуємо статус код і повну відповідь для налагодження
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Received status code from the server: " + statusCode);
        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
       // System.out.println("Full Response: " + responseContent);

        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");

        // Парсинг та верифікація XML-відповіді
        SoftAssert softAssertion = new SoftAssert();
        verifyUpdateAliasResponse(responseContent, softAssertion);
        softAssertion.assertAll();
    }

    private void verifyUpdateAliasResponse(String xml, SoftAssert softAssertion) throws Exception {
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

            // Друк додаткових елементів з відповіді
            NodeList updateAliasResultNodes = doc.getElementsByTagName("UpdateAliasResult");
            if (updateAliasResultNodes.getLength() > 0) {
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                DOMSource source = new DOMSource(updateAliasResultNodes.item(0));
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                StreamResult result = new StreamResult(outputStream);
                transformer.transform(source, result);
                String formattedXML = new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
                System.out.println("UpdateAliasResult: " + formattedXML);
            }
        }
    }

    // Метод для отримання aliasId з бази даних
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
}
