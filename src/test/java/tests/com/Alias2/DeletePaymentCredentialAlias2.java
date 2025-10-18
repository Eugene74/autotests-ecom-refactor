package tests.com.Alias2;

import tests.com.Alias1.BaseTestAlias1;
import tests.com.Alias1.SharedDataStore;
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
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class DeletePaymentCredentialAlias2 extends BaseTestAlias1 {

    private static final String XML_DELETE_PAYMENT_CREDENTIAL_ALIAS2_RESOURCE = "/XMLAlias2/DeletePaymentCredentialAlias2.xml";
    private static final int TIMEOUT_MS = 60000; // 60 секунд

    @Test(dependsOnMethods = "tests.com.Alias2.CreatePaymentCredentialAlias2.testCreatePaymentCredentialAlias2")
    public void testDeletePaymentCredentialAlias2() throws Exception {
        String xmlContent = getResourceContent(XML_DELETE_PAYMENT_CREDENTIAL_ALIAS2_RESOURCE);

        // Використовуйте збережені значення з SharedDataStore
        String requestId = SharedDataStore.requestId;
        String paymentCredentialId = SharedDataStore.paymentCredentialId;

        Map<String, String> params = new HashMap<>();
        params.put("Merchant2ID", Merchant2ID);
        params.put("Terminal2ID", Terminal2ID);
        params.put("TrackingId", requestId);
        params.put("paymentCredentialId", paymentCredentialId);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            xmlContent = xmlContent.replace("${" + entry.getKey() + "}", Objects.requireNonNull(entry.getValue(), "Value for " + entry.getKey() + " is null"));
        }

        // Друк XML-запиту для налагодження
       System.out.println("Request XML: " + formatXml(xmlContent));

        ResponseData response = sendCustomDeleteRequest(URLAlias2DeletePaymentCredential, xmlContent);
        assertNotNull(response);
        int statusCode = response.code();
        System.out.println("Received status code from the server: " + statusCode);
        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");

        String responseContent = response.body();
        //System.out.println("Response Content: " + responseContent);

        // Парсинг та верифікація XML відповіді
        SoftAssert softAssertion = new SoftAssert();
        verifyXmlResponse(responseContent, softAssertion);
        softAssertion.assertAll();
    }

    private ResponseData sendCustomDeleteRequest(String url, String xmlContent) throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(TIMEOUT_MS))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofMillis(TIMEOUT_MS))
                .header("Content-Type", "application/xml")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(xmlContent, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return new ResponseData(response.statusCode(), response.body());
    }
    /**
     * Аналог okhttp3.Response — простой контейнер
     */
    private record ResponseData(int code, String body) {}


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
