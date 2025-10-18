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

public class UpdateAlias2status extends BaseTestAlias1 {

    @Test
    public void testUpdateAlias2status() throws Exception {
        updateAliasStatus("DISABLED");
        updateAliasStatus("ACTIVE");
    }

    private void updateAliasStatus(String status) throws Exception {
        // Отримання значення aliasId з бази даних
        String aliasId = getAliasIdFromDatabase(SharedDataStore.trackingId);

        if (aliasId == null) {
            throw new IllegalArgumentException("The aliasId is not set.");
        }

        // Зберігаємо aliasId в SharedDataStore для використання між запусками
        SharedDataStore.aliasId = aliasId;

        System.out.println("Fetched aliasId from DB: " + aliasId);

        // Складання XML-запиту
        String xmlContent = getResourceContent(XML_UPDATE_ALIAS2_STATUS_RESOURCE);

        // Підставляння змінних у XML-запит
        Map<String, String> params = new HashMap<>();
        params.put("Merchant2ID", Merchant2ID);
        params.put("Terminal2ID", Terminal2ID);
        params.put("TrackingId", SharedDataStore.trackingId);
        params.put("aliasId", aliasId);
        params.put("status", status);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        // Друк XML-запиту для налагодження
       System.out.println("Request XML: " + extractUpdateAliasStatusTag(xmlContent));

        // Відправка PUT-запиту
        HttpResponse response = sendPutRequest(URLAlias2UpdateStatus, xmlContent);
        assertNotNull(response);

        // Друкуємо статус код і повну відповідь для налагодження
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Received status code from the server: " + statusCode);
        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
       // System.out.println("Full Response: " + responseContent);

        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");

        // Парсинг та верифікація XML-відповіді
        SoftAssert softAssertion = new SoftAssert();
        verifyUpdateAliasStatusResponse(responseContent, softAssertion);
        softAssertion.assertAll();
    }

    private void verifyUpdateAliasStatusResponse(String xml, SoftAssert softAssertion) throws Exception {
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
            NodeList statusResultNodes = doc.getElementsByTagName("Status");
            if (statusResultNodes.getLength() > 0) {
                System.out.println("StatusResult is not empty: true");
                for (int i = 0; i < statusResultNodes.getLength(); i++) {
                    NodeList idNodes = statusResultNodes.item(i).getChildNodes();
                    for (int j = 0; j < idNodes.getLength(); j++) {
                        System.out.println(idNodes.item(j).getNodeName() + ": " + idNodes.item(j).getTextContent().trim());
                    }
                }
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

    // Метод для витягання тега <updateAliasStatus> з XML-запиту
    private String extractUpdateAliasStatusTag(String xmlContent) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xmlContent.getBytes(StandardCharsets.UTF_8)));

        NodeList updateAliasStatusNodes = doc.getElementsByTagName("updateAliasStatus");
        if (updateAliasStatusNodes.getLength() > 0) {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(updateAliasStatusNodes.item(0));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            StreamResult result = new StreamResult(outputStream);
            transformer.transform(source, result);
            return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
        }
        return "";
    }
}
