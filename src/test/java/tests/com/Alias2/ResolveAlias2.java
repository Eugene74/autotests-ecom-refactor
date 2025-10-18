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

public class ResolveAlias2 extends BaseTestAlias1 {

    @Test
    public void testResolveAlias2() throws Exception {
        // Переконаємося, що aliasValue збережено в SharedDataStore
        if (SharedDataStore.aliasValue == null) {
            throw new IllegalArgumentException("The aliasValue is not set in SharedDataStore.");
        }

        // Складання XML-запиту
        String xmlContent = getResourceContent(XML_RESOLVE_ALIAS2_RESOURCE);

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
        HttpResponse response = sendPostRequest(URLAlias2Resolve, xmlContent);
        assertNotNull(response);

        // Друкуємо статус код і повну відповідь для налагодження
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Received status code from the server: " + statusCode);
        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
       // System.out.println("Full Response: " + responseContent);

        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");

        // Парсинг та верифікація XML-відповіді
        SoftAssert softAssertion = new SoftAssert();
        verifyResolveAliasResponse(responseContent, softAssertion);
        softAssertion.assertAll();
    }

    private void verifyResolveAliasResponse(String xml, SoftAssert softAssertion) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        // Перевірка елемента <ResolveAliasResult> на присутність та правильність
        NodeList resolveAliasResultNodes = doc.getElementsByTagName("ResolveAliasResult");
        boolean isResolveAliasResultNotEmpty = resolveAliasResultNodes.getLength() > 0 && resolveAliasResultNodes.item(0).getTextContent().trim().length() > 0;
        System.out.println("ResolveAliasResult is not empty: " + isResolveAliasResultNotEmpty);
        softAssertion.assertTrue(isResolveAliasResultNotEmpty, "<ResolveAliasResult> is empty");

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
            NodeList profileNodes = doc.getElementsByTagName("profile");
            if (profileNodes.getLength() > 0) {
                NodeList contactInfoNodes = doc.getElementsByTagName("contactInfo");
                NodeList dateOfBirthNodes = doc.getElementsByTagName("dateOfBirth");
                NodeList firstNameNodes = doc.getElementsByTagName("firstName");
                NodeList firstNameLocalNodes = doc.getElementsByTagName("firstNameLocal");
                NodeList lastNameNodes = doc.getElementsByTagName("lastName");
                NodeList lastNameLocalNodes = doc.getElementsByTagName("lastNameLocal");
                NodeList middleNameNodes = doc.getElementsByTagName("middleName");
                NodeList middleNameLocalNodes = doc.getElementsByTagName("middleNameLocal");
                NodeList preferredNameNodes = doc.getElementsByTagName("preferredName");

                // Додано Друк додаткових елементів з відповіді <type>CARD</type> PLD-2707
                NodeList typeNodes = doc.getElementsByTagName("type");
                if (typeNodes.getLength() > 0) {
                    for (int i = 0; i < typeNodes.getLength(); i++) {
                        String typeValue = typeNodes.item(i).getTextContent().trim();
                        System.out.println("type: " + typeValue);}}



                if (contactInfoNodes.getLength() > 0) {
                    System.out.println("contactInfo: " + contactInfoNodes.item(0).getTextContent().trim());
                }
                if (dateOfBirthNodes.getLength() > 0) {
                    System.out.println("dateOfBirth: " + dateOfBirthNodes.item(0).getTextContent().trim());
                }
                if (firstNameNodes.getLength() > 0) {
                    System.out.println("firstName: " + firstNameNodes.item(0).getTextContent().trim());
                }
                if (firstNameLocalNodes.getLength() > 0) {
                    System.out.println("firstNameLocal: " + firstNameLocalNodes.item(0).getTextContent().trim());
                }
                if (lastNameNodes.getLength() > 0) {
                    System.out.println("lastName: " + lastNameNodes.item(0).getTextContent().trim());
                }
                if (lastNameLocalNodes.getLength() > 0) {
                    System.out.println("lastNameLocal: " + lastNameLocalNodes.item(0).getTextContent().trim());
                }
                if (middleNameNodes.getLength() > 0) {
                    System.out.println("middleName: " + middleNameNodes.item(0).getTextContent().trim());
                }
                if (middleNameLocalNodes.getLength() > 0) {
                    System.out.println("middleNameLocal: " + middleNameLocalNodes.item(0).getTextContent().trim());
                }
                if (preferredNameNodes.getLength() > 0) {
                    System.out.println("preferredName: " + preferredNameNodes.item(0).getTextContent().trim());
                }
            }

            NodeList identificationNodes = doc.getElementsByTagName("identification");
            if (identificationNodes.getLength() > 0) {
                NodeList typeNodes = doc.getElementsByTagName("type");
                NodeList valueNodes = doc.getElementsByTagName("value");
                NodeList verificationDetailsNodes = doc.getElementsByTagName("verificationDetails");

                if (typeNodes.getLength() > 0) {
                    System.out.println("type: " + typeNodes.item(0).getTextContent().trim());
                }
                if (valueNodes.getLength() > 0) {
                    System.out.println("value: " + valueNodes.item(0).getTextContent().trim());
                }
                if (verificationDetailsNodes.getLength() > 0) {
                    NodeList authDateTimeNodes = doc.getElementsByTagName("authDateTime");
                    NodeList authMethodReferenceNodes = doc.getElementsByTagName("authMethodReference");
                    NodeList creationDateTimeNodes = doc.getElementsByTagName("creationDateTime");
                    NodeList verifiedEmailNodes = doc.getElementsByTagName("verifiedEmail");
                    NodeList verifiedPhoneNodes = doc.getElementsByTagName("verifiedPhone");

                    if (authDateTimeNodes.getLength() > 0) {
                        System.out.println("authDateTime: " + authDateTimeNodes.item(0).getTextContent().trim());
                    }
                    if (authMethodReferenceNodes.getLength() > 0) {
                        System.out.println("authMethodReference: " + authMethodReferenceNodes.item(0).getTextContent().trim());
                    }
                    if (creationDateTimeNodes.getLength() > 0) {
                        System.out.println("creationDateTime: " + creationDateTimeNodes.item(0).getTextContent().trim());
                    }
                    if (verifiedEmailNodes.getLength() > 0) {
                        System.out.println("verifiedEmail: " + verifiedEmailNodes.item(0).getTextContent().trim());
                    }
                    if (verifiedPhoneNodes.getLength() > 0) {
                        System.out.println("verifiedPhone: " + verifiedPhoneNodes.item(0).getTextContent().trim());
                    }
                }
            }
        }
    }
}
