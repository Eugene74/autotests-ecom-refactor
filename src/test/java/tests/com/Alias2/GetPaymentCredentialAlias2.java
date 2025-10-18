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

public class GetPaymentCredentialAlias2 extends BaseTestAlias1 {

    @Test
    public void testGetPaymentCredentialAlias2() throws Exception {
        // Переконаємося, що paymentCredentialId збережено в SharedDataStore
        if (SharedDataStore.paymentCredentialId == null) {
            throw new IllegalArgumentException("The paymentCredentialId is not set in SharedDataStore.");
        }

        // Складання XML-запиту
        String xmlContent = getResourceContent(XML_GET_PAYMENT_CREDENTIAL_ALIAS2_RESOURCE);

        // Підставляння змінних у XML-запит
        Map<String, String> params = new HashMap<>();
        params.put("Merchant2ID", Merchant2ID);
        params.put("Terminal2ID", Terminal2ID);
        params.put("TrackingId", SharedDataStore.trackingId);
        params.put("paymentCredentialId", SharedDataStore.paymentCredentialId);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        // Друк XML-запиту для налагодження
        //System.out.println("Request XML: " + xmlContent);

        // Відправка POST-запиту
        HttpResponse response = sendPostRequest(URLAlias2GetPaymentCredential, xmlContent);
        assertNotNull(response);

        // Друкуємо статус код і повну відповідь для налагодження
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Received status code from the server: " + statusCode);
        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        //System.out.println("Full Response: " + responseContent);

        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");

        // Парсинг та верифікація XML-відповіді
        SoftAssert softAssertion = new SoftAssert();
        verifyGetPaymentCredentialResponse(responseContent, softAssertion);
        softAssertion.assertAll();
    }

    private void verifyGetPaymentCredentialResponse(String xml, SoftAssert softAssertion) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        // Перевірка наявності елемента <GetPaymentCredentialResult>
        NodeList getPaymentCredentialResultNodes = doc.getElementsByTagName("GetPaymentCredentialResult");
        boolean isGetPaymentCredentialResultNotEmpty = getPaymentCredentialResultNodes.getLength() > 0 && getPaymentCredentialResultNodes.item(0).getTextContent().trim().length() > 0;
        System.out.println("GetPaymentCredentialResult is not empty: " + isGetPaymentCredentialResultNotEmpty);
        softAssertion.assertTrue(isGetPaymentCredentialResultNotEmpty, "<GetPaymentCredentialResult> is empty");

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
}
