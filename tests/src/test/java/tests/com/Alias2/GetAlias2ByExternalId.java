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
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class GetAlias2ByExternalId extends BaseTestAlias1 {

    @Test
    public void testGetAlias2ByExternalId() throws Exception {
        // Отримання значення externalId з бази даних
        String externalId = getExternalIdFromDatabase(SharedDataStore.trackingId);

        if (externalId == null) {
            throw new IllegalArgumentException("The externalId is not set.");
        }

        SharedDataStore.externalId = externalId;
        System.out.println("Fetched externalId from DB: " + externalId);

        // Складання XML-запиту
        String xmlContent = getResourceContent(XML_GET_ALIAS2_BY_EXTERNALID_RESOURCE);

        // Підставляння змінних у XML-запит
        Map<String, String> params = new HashMap<>();
        params.put("Merchant2ID", Merchant2ID);
        params.put("Terminal2ID", Terminal2ID);
        params.put("TrackingId", SharedDataStore.trackingId);
        params.put("externalId", externalId);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        // Друк XML-запиту для налагодження
        //System.out.println("Request XML: " + xmlContent);

        // Відправка POST-запиту
        HttpResponse response = sendPostRequest(URLAlias2GetByExternalId, xmlContent);
        assertNotNull(response);

        // Друкуємо статус код і повну відповідь для налагодження
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Received status code from the server: " + statusCode);
        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        //System.out.println("Full Response: " + responseContent);

        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");

        // Парсинг та верифікація XML-відповіді
        SoftAssert softAssertion = new SoftAssert();
        verifyGetAliasByExternalIdResponse(responseContent, softAssertion);
        softAssertion.assertAll();
    }

    private void verifyGetAliasByExternalIdResponse(String xml, SoftAssert softAssertion) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        // Перевірка елемента <GetByExternalIdResult> на присутність та правильність
        NodeList getByExternalIdResultNodes = doc.getElementsByTagName("GetByExternalIdResult");
        boolean isGetByExternalIdResultNotEmpty = getByExternalIdResultNodes.getLength() > 0 && getByExternalIdResultNodes.item(0).getTextContent().trim().length() > 0;
        System.out.println("GetByExternalIdResult is not empty: " + isGetByExternalIdResultNotEmpty);
        softAssertion.assertTrue(isGetByExternalIdResultNotEmpty, "<GetByExternalIdResult> is empty");

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

            // Друкуємо додаткові елементи з відповіді
            NodeList idNodes = doc.getElementsByTagName("id");
            NodeList associatedIdsNodes = doc.getElementsByTagName("associatedIds");

            if (idNodes.getLength() > 0) {
                System.out.println("id: " + idNodes.item(0).getTextContent().trim());
            }

            if (associatedIdsNodes.getLength() > 0) {
                for (int i = 0; i < associatedIdsNodes.getLength(); i++) {
                    String associatedId = associatedIdsNodes.item(i).getTextContent().trim();
                    System.out.println("associatedId: " + associatedId);
                }
            }
        }
    }

    // Метод для отримання externalId з бази даних
    private String getExternalIdFromDatabase(String trackingId) throws Exception {
        String externalId = null;
        try (Connection connection = getDBConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT ALIAS_EXTERNAL_ID FROM VA_USER_REQUEST WHERE TRACKING_ID = ? AND TYPE = 'CREATE_ALIAS'")) {

            statement.setString(1, trackingId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    externalId = resultSet.getString("ALIAS_EXTERNAL_ID");
                }
            }
        }
        return externalId;
    }
}
