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
import java.util.HashMap;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class GetAlias2IdByValue extends BaseTestAlias1 {

    @Test
    public void testGetAliasIdByValue() throws Exception {
        // Переконаємося, що aliasValue збережено в SharedDataStore
        if (SharedDataStore.aliasValue == null) {
            throw new IllegalArgumentException("The aliasValue is not set in SharedDataStore.");
        }

        // Складання XML-запиту
        String xmlContent = getResourceContent(XML_GET_ALIAS2ID_BY_VALUE_RESOURCE);

        // Підставляння змінних у XML-запит
        Map<String, String> params = new HashMap<>();
        params.put("Merchant2ID", Merchant2ID);
        params.put("Terminal2ID", Terminal2ID);
        params.put("TrackingId", SharedDataStore.trackingId);
        params.put("aliasValue", SharedDataStore.aliasValue);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        // Друк XML-запиту для налагодження
       // System.out.println("Request XML: " + xmlContent);

        // Відправка POST-запиту
        HttpResponse response = sendPostRequest(URLAlias2GetAliasId, xmlContent);
        assertNotNull(response);

        // Друкуємо статус код і повну відповідь для налагодження
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Received status code from the server: " + statusCode);
        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        //System.out.println("Full Response: " + responseContent);

        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");

        // Парсинг та верифікація XML-відповіді
        SoftAssert softAssertion = new SoftAssert();
        verifyGetAliasIdResponse(responseContent, softAssertion);
        softAssertion.assertAll();
    }

    private void verifyGetAliasIdResponse(String xml, SoftAssert softAssertion) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        // Перевірка елемента <GetAliasIdFromValueResult> на присутність та правильність
        NodeList getAliasIdResultNodes = doc.getElementsByTagName("GetAliasIdFromValueResult");
        boolean isGetAliasIdResultNotEmpty = getAliasIdResultNodes.getLength() > 0 && getAliasIdResultNodes.item(0).getTextContent().trim().length() > 0;
        System.out.println("GetAliasIdFromValueResult is not empty: " + isGetAliasIdResultNotEmpty);
        softAssertion.assertTrue(isGetAliasIdResultNotEmpty, "<GetAliasIdFromValueResult> is empty");

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
            if (idNodes.getLength() > 0) {
                String idValue = idNodes.item(0).getTextContent().trim();
                System.out.println("id: " + idValue);
            }
        }
    }
}
