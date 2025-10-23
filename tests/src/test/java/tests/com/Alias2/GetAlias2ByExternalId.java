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

import static com.ecom.core.config.EnvData.URLAlias2GetByExternalId;
import static com.ecom.core.config.XMLAliasResource.XML_GET_ALIAS2_BY_EXTERNALID_RESOURCE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class GetAlias2ByExternalId extends BaseTestAlias2 {
    private static final String QUERY = "SELECT ALIAS_EXTERNAL_ID FROM VA_USER_REQUEST WHERE TRACKING_ID = ? AND TYPE = 'CREATE_ALIAS'";
    @Test
    public void testGetAlias2ByExternalId() throws Exception {
        // Отримання значення externalId з бази даних
        String externalId = getAliasIdFromDatabase(SharedDataStore.trackingId, QUERY, "ALIAS_EXTERNAL_ID");
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
}
