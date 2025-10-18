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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class GetAllPaymentCredentialsAlias2 extends BaseTestAlias1 {

    @Test
    public void testGetAllPaymentCredentialsAlias2() throws Exception {
        // Отримання значення aliasId з бази даних
        String aliasId = getAliasIdFromDatabase(SharedDataStore.trackingId);

        if (aliasId == null) {
            throw new IllegalArgumentException("The aliasId is not set.");
        }

        // Зберігаємо aliasId в SharedDataStore для використання між запусками
        SharedDataStore.aliasId = aliasId;

        System.out.println("Fetched aliasId from DB: " + aliasId);

        // Складання XML-запиту
        String xmlContent = getResourceContent(XML_GET_ALL_PAYMENT_CREDENTIALS_ALIAS2_RESOURCE);

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
       // System.out.println("Request XML: " + xmlContent);

        // Відправка POST-запиту
        HttpResponse response = sendPostRequest(URLAlias2GetAllPaymentCredentials, xmlContent);
        assertNotNull(response);

        // Друкуємо статус код і повну відповідь для налагодження
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Received status code from the server: " + statusCode);
        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        //System.out.println("Full Response: " + responseContent);

        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");

        // Парсинг та верифікація XML-відповіді
        SoftAssert softAssertion = new SoftAssert();
        verifyGetAllPaymentCredentialsResponse(responseContent, softAssertion);
        softAssertion.assertAll();
    }

    private void verifyGetAllPaymentCredentialsResponse(String xml, SoftAssert softAssertion) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        // Перевірка наявності елемента <GetAllPaymentCredentialsResult>
        NodeList getAllPaymentCredentialsResultNodes = doc.getElementsByTagName("GetAllPaymentCredentialsResult");
        boolean isGetAllPaymentCredentialsResultNotEmpty = getAllPaymentCredentialsResultNodes.getLength() > 0 && getAllPaymentCredentialsResultNodes.item(0).getTextContent().trim().length() > 0;
        System.out.println("GetAllPaymentCredentialsResult is not empty: " + isGetAllPaymentCredentialsResultNotEmpty);
        softAssertion.assertTrue(isGetAllPaymentCredentialsResultNotEmpty, "<GetAllPaymentCredentialsResult> is empty");

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
            NodeList cardTypeNodes = doc.getElementsByTagName("cardType");
            NodeList createdOnNodes = doc.getElementsByTagName("createdOn");
            NodeList expirationDateNodes = doc.getElementsByTagName("expirationDate");
            NodeList externalIdNodes = doc.getElementsByTagName("externalId");
            NodeList idNodes = doc.getElementsByTagName("id");
            NodeList issuerNameNodes = doc.getElementsByTagName("issuerName");
            NodeList lastFourDigitsNodes = doc.getElementsByTagName("lastFourDigits");
            NodeList lastUpdatedOnNodes = doc.getElementsByTagName("lastUpdatedOn");
            NodeList nameOnCardNodes = doc.getElementsByTagName("nameOnCard");
            NodeList preferredForNodes = doc.getElementsByTagName("preferredFor");
            NodeList statusNodes = doc.getElementsByTagName("status");

            if (cardTypeNodes.getLength() > 0) {
                System.out.println("CardType: " + cardTypeNodes.item(0).getTextContent().trim());
            }
            if (createdOnNodes.getLength() > 0) {
                System.out.println("CreatedOn: " + createdOnNodes.item(0).getTextContent().trim());
            }
            if (expirationDateNodes.getLength() > 0) {
                System.out.println("ExpirationDate: " + expirationDateNodes.item(0).getTextContent().trim());
            }
            if (externalIdNodes.getLength() > 0) {
                System.out.println("ExternalId: " + externalIdNodes.item(0).getTextContent().trim());
            }
            if (idNodes.getLength() > 0) {
                System.out.println("Id: " + idNodes.item(0).getTextContent().trim());
            }
            if (issuerNameNodes.getLength() > 0) {
                System.out.println("IssuerName: " + issuerNameNodes.item(0).getTextContent().trim());
            }
            if (lastFourDigitsNodes.getLength() > 0) {
                System.out.println("LastFourDigits: " + lastFourDigitsNodes.item(0).getTextContent().trim());
            }
            if (lastUpdatedOnNodes.getLength() > 0) {
                System.out.println("LastUpdatedOn: " + lastUpdatedOnNodes.item(0).getTextContent().trim());
            }
            if (nameOnCardNodes.getLength() > 0) {
                System.out.println("NameOnCard: " + nameOnCardNodes.item(0).getTextContent().trim());
            }
            if (preferredForNodes.getLength() > 0) {
                System.out.println("PreferredFor: " + preferredForNodes.item(0).getTextContent().trim());
            }
            if (statusNodes.getLength() > 0) {
                System.out.println("Status: " + statusNodes.item(0).getTextContent().trim());
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
