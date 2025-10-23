package tests.com.Alias2;

import com.ecom.tests.base.BaseTestAlias2;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import tests.com.Alias1.SharedDataStore;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.ecom.core.config.EnvData.URLAlias2CreatePaymentCredential;
import static com.ecom.core.config.XMLAliasResource.XML_CREATE_PAYMENT_CREDENTIAL_ALIAS2_RESOURCE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CreatePaymentCredentialAlias2 extends BaseTestAlias2 {
    private static final String QUERY = "SELECT ALIAS_ID FROM VA_USER_REQUEST WHERE TRACKING_ID = ? AND TYPE = 'CREATE_ALIAS'";
    @Test
    public void testCreatePaymentCredentialAlias2() throws Exception {
        // Отримання значення aliasId з бази даних
        String aliasId = getAliasIdFromDatabase(SharedDataStore.trackingId, QUERY, "ALIAS_ID");
        if (aliasId == null) {
            throw new IllegalArgumentException("The aliasId is not set.");
        }
        // Зберігаємо aliasId в SharedDataStore для використання між запусками
        SharedDataStore.aliasId = aliasId;
        System.out.println("Fetched aliasId from DB: " + aliasId);
        // Генеруємо GUID
        String guid = generateGUID();
        SharedDataStore.externalPaymentCredentialId = guid; // Зберігаємо GUID для подальшого використання
        System.out.println("Generated GUID: " + guid);
        // Складання XML-запиту
        String xmlContent = getResourceContent(XML_CREATE_PAYMENT_CREDENTIAL_ALIAS2_RESOURCE);
        // Підставляння змінних у XML-запит
        Map<String, String> params = new HashMap<>();
        params.put("Merchant2ID", Merchant2ID);
        params.put("Terminal2ID", Terminal2ID);
        params.put("TrackingId", SharedDataStore.trackingId);
        params.put("aliasId", aliasId);
        params.put("guid", guid);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        // Друк XML-запиту для налагодження
      //  System.out.println("Request XML: " + xmlContent);
        // Відправка POST-запиту
        HttpResponse response = sendPostRequest(URLAlias2CreatePaymentCredential, xmlContent);
        assertNotNull(response);
        // Друкуємо статус код і повну відповідь для налагодження
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Received status code from the server: " + statusCode);
        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
       // System.out.println("Full Response: " + responseContent);
        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");
        // Парсинг та верифікація XML-відповіді
        SoftAssert softAssertion = new SoftAssert();
        verifyCreatePaymentCredentialResponse(responseContent, softAssertion);
        softAssertion.assertAll();
    }

    private void verifyCreatePaymentCredentialResponse(String xml, SoftAssert softAssertion) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        // Перевірка наявності елемента <CreatePaymentCredentialResult>
        NodeList createPaymentCredentialResultNodes = doc.getElementsByTagName("CreatePaymentCredentialResult");
        boolean isCreatePaymentCredentialResultNotEmpty = createPaymentCredentialResultNodes.getLength() > 0 && createPaymentCredentialResultNodes.item(0).getTextContent().trim().length() > 0;
        System.out.println("CreatePaymentCredentialResult is not empty: " + isCreatePaymentCredentialResultNotEmpty);
        softAssertion.assertTrue(isCreatePaymentCredentialResultNotEmpty, "<CreatePaymentCredentialResult> is empty");
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
            NodeList externalIdNodes = doc.getElementsByTagName("externalId");
            NodeList idNodes = doc.getElementsByTagName("id");
            NodeList typeNodes = doc.getElementsByTagName("type");
            if (externalIdNodes.getLength() > 0) {
                String externalIdValue = externalIdNodes.item(0).getTextContent().trim();
                System.out.println("ExternalId: " + externalIdValue);
                SharedDataStore.paymentCredentialExternalId = externalIdValue; // Зберігаємо значення для подальшого використання
            }
            if (idNodes.getLength() > 0) {
                String idValue = idNodes.item(0).getTextContent().trim();
                System.out.println("Id: " + idValue);
                SharedDataStore.paymentCredentialId = idValue; // Зберігаємо значення для подальшого використання
            }
            if (typeNodes.getLength() > 0) {
                String typeValue = typeNodes.item(0).getTextContent().trim();
                System.out.println("Type: " + typeValue);
            }
        }
    }
}
