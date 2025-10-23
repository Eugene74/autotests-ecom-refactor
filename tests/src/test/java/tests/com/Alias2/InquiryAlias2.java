package tests.com.Alias2;

import com.ecom.tests.base.BaseTestAlias2;
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

import static com.ecom.core.config.EnvData.URLAlias2Inquiry;
import static com.ecom.core.config.XMLAliasResource.XML_INQUIRY_ALIAS2_RESOURCE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class InquiryAlias2 extends BaseTestAlias2 {

    @Test
    public void testInquiryAlias2() throws Exception {
        // Складання XML-запиту
        String xmlContent = getResourceContent(XML_INQUIRY_ALIAS2_RESOURCE);

        // Підставляння змінних у XML-запит
        Map<String, String> params = new HashMap<>();
        params.put("Merchant2ID", Merchant2ID);
        params.put("Terminal2ID", Terminal2ID);
        params.put("TrackingId", SharedDataStore.trackingId);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        // Друк XML-запиту для налагодження
       // System.out.println("Request XML: " + xmlContent);

        // Відправка POST-запиту
        HttpResponse response = sendPostRequest(URLAlias2Inquiry, xmlContent);
        assertNotNull(response);

        // Друкуємо статус код і повну відповідь для налагодження
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Received status code from the server: " + statusCode);
        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
       // System.out.println("Full Response: " + responseContent);

        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");

        // Парсинг та верифікація XML-відповіді
        SoftAssert softAssertion = new SoftAssert();
        verifyInquiryAliasResponse(responseContent, softAssertion);
        softAssertion.assertAll();
    }

    private void verifyInquiryAliasResponse(String xml, SoftAssert softAssertion) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        // Перевірка елемента <InquiryAliasResult> на присутність та правильність
        NodeList inquiryAliasResultNodes = doc.getElementsByTagName("InquiryAliasResult");
        boolean isInquiryAliasResultNotEmpty = inquiryAliasResultNodes.getLength() > 0 && inquiryAliasResultNodes.item(0).getTextContent().trim().length() > 0;
        System.out.println("InquiryAliasResult is not empty: " + isInquiryAliasResultNotEmpty);
        softAssertion.assertTrue(isInquiryAliasResultNotEmpty, "<InquiryAliasResult> is empty");

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
            NodeList summaryNodes = doc.getElementsByTagName("summary");
            if (summaryNodes.getLength() > 0) {
                NodeList aliasesFoundNodes = doc.getElementsByTagName("aliasesFound");
                NodeList aliasesNotFoundNodes = doc.getElementsByTagName("aliasesNotFound");
                NodeList aliasesRepeatedNodes = doc.getElementsByTagName("aliasesRepeated");
                NodeList aliasesTotalNodes = doc.getElementsByTagName("aliasesTotal");

                if (aliasesFoundNodes.getLength() > 0) {
                    System.out.println("aliasesFound: " + aliasesFoundNodes.item(0).getTextContent().trim());
                }
                if (aliasesNotFoundNodes.getLength() > 0) {
                    System.out.println("aliasesNotFound: " + aliasesNotFoundNodes.item(0).getTextContent().trim());
                }
                if (aliasesRepeatedNodes.getLength() > 0) {
                    System.out.println("aliasesRepeated: " + aliasesRepeatedNodes.item(0).getTextContent().trim());
                }
                if (aliasesTotalNodes.getLength() > 0) {
                    System.out.println("aliasesTotal: " + aliasesTotalNodes.item(0).getTextContent().trim());
                }
            }
        }
    }
}
