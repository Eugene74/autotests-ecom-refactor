package tests.com.Onboarding;

import com.ecom.tests.base.BaseTestOnboarding;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.ecom.core.config.EnvData.merchantID_aval1;
import static com.ecom.core.config.EnvData.terminalID_aval1;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class Onboarding extends BaseTestOnboarding {

    private static String savedRequestId;

    @Test
    public void testOnboardMerchant() throws Exception {
        String xmlContent = getResourceContent(XML_ONBOARD_MERCHANT_RESOURCE);
        String randomDigits = generateRandomDigits(4);
        String name = "test" + randomDigits;
        String siteUrl = "https://" + randomDigits + "usa.com";
        String email = "USA@upc" + randomDigits + ".ua";
        String taxNumber = "2034" + randomDigits;

        Map<String, String> params = new HashMap<>();
        params.put("MerchantID",merchantID_aval1);
        params.put("Name", name);
        params.put("siteUrl", siteUrl);
        params.put("Email", email);
        params.put("TaxNumber", taxNumber);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        // Логування XML, що відправляється
       // System.out.println("Sending XML: " + xmlContent);
        // Додаємо паузу перед відправкою запиту
        Thread.sleep(5000); //todo remove later
        HttpResponse response = sendPostRequest(baseUrl + "/go/merchants/service/external", xmlContent);
        assertNotNull(response);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Received status code from the server: " + statusCode);
        // Перевірка статус-коду HTTP
        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");
        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        // Логування отриманої відповіді
        //System.out.println("Received Response: " + responseContent);
        // Парсинг та верифікація XML відповіді
        SoftAssert softAssertion = new SoftAssert();
        verifyXmlResponse(responseContent, softAssertion);
        softAssertion.assertAll();
    }

    private void verifyXmlResponse(String xml, SoftAssert softAssertion) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        // Логування всього документа
        //System.out.println("Parsed XML Document: " + xml);

        NodeList responseNodes = doc.getElementsByTagName("XMLOnboardMerchantResponse");
        assertTrue(responseNodes.getLength() > 0, "<XMLOnboardMerchantResponse> element not found");

        if (responseNodes.getLength() > 0) {
            NodeList childNodes = responseNodes.item(0).getChildNodes();
            String requestId = null;
            String code = null;
            String message = null;
            String merchantId = null;

            for (int i = 0; i < childNodes.getLength(); i++) {
                if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String nodeName = childNodes.item(i).getNodeName();
                    String nodeValue = childNodes.item(i).getTextContent();

                    switch (nodeName) {
                        case "RequestID":
                            requestId = nodeValue;
                            break;
                        case "status":
                            NodeList statusNodes = childNodes.item(i).getChildNodes();
                            for (int j = 0; j < statusNodes.getLength(); j++) {
                                if (statusNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                    String statusNodeName = statusNodes.item(j).getNodeName();
                                    String statusNodeValue = statusNodes.item(j).getTextContent();
                                    if ("Code".equals(statusNodeName)) {
                                        code = statusNodeValue;
                                    } else if ("Message".equals(statusNodeName)) {
                                        message = statusNodeValue;
                                    }
                                }
                            }
                            break;
                        case "MerchantID":
                            merchantId = nodeValue;
                            break;
                    }
                }
            }

            System.out.println("RequestID: " + requestId);
            System.out.println("Code: " + code);
            System.out.println("Message: " + message);
            System.out.println("MerchantID: " + merchantId);

            softAssertion.assertNotNull(requestId, "RequestID is null");
            softAssertion.assertEquals(code, "000", "Code is not 000");
            softAssertion.assertNotNull(message, "Message is null");
            softAssertion.assertNotNull(merchantId, "MerchantID is null");

            // Зберігаємо значення requestId для подальшого використання
            savedRequestId = requestId;
            System.out.println("Saved RequestID: " + savedRequestId);
        }
    }

    @Test(dependsOnMethods = "testOnboardMerchant")
    public void testGetStatusOnboarding() throws Exception {
        String xmlContent = getResourceContent(XML_GET_STATUS_ONBOARD);

        // Перевірка на null
        assertNotNull(savedRequestId, "Saved RequestID is null");

        Map<String, String> params = new HashMap<>();
        params.put("MerchantID", merchantID_aval1);
        params.put("TerminalID", terminalID_aval1);
        params.put("RequestID", savedRequestId);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }

        // Логування XML після заміни змінних
        System.out.println("Sending XML after replacement: " + xmlContent);

        // Додаємо паузу між запитами
        Thread.sleep(5000);

        HttpResponse response = sendGetRequestWithEntity(baseUrl + "/go/merchants/status/external", xmlContent);
        assertNotNull(response);
        int statusCode = response.getStatusLine().getStatusCode();

        System.out.println("Received status code from the server: " + statusCode);

        // Перевірка статус-коду HTTP
        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");

        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");

        // Логування отриманої відповіді
        //System.out.println("Received Response: " + responseContent);

        // Парсинг та верифікація XML відповіді
        SoftAssert softAssertion = new SoftAssert();
        verifyGetStatusXmlResponse(responseContent, softAssertion);
        softAssertion.assertAll();
    }

    private void verifyGetStatusXmlResponse(String xml, SoftAssert softAssertion) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        // Логування всього документа
        //System.out.println("Parsed XML Document: " + xml);

        NodeList responseNodes = doc.getElementsByTagName("XMLOnboardStatusResponse");
        assertTrue(responseNodes.getLength() > 0, "<XMLOnboardStatusResponse> element not found");

        if (responseNodes.getLength() > 0) {
            NodeList childNodes = responseNodes.item(0).getChildNodes();
            String code = null;
            String message = null;
            String active = null;
            String merchantId = null;
            String mdesStatus = null;
            String mdesName = null;
            String mdesDebitCreditIndicator = null;
            String vtsStatus = null;
            String vtsTrid = null;
            String vtsTaxNumber = null;

            for (int i = 0; i < childNodes.getLength(); i++) {
                if (childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) {
                    String nodeName = childNodes.item(i).getNodeName();
                    String nodeValue = childNodes.item(i).getTextContent();

                    switch (nodeName) {
                        case "status":
                            NodeList statusNodes = childNodes.item(i).getChildNodes();
                            for (int j = 0; j < statusNodes.getLength(); j++) {
                                if (statusNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                    String statusNodeName = statusNodes.item(j).getNodeName();
                                    String statusNodeValue = statusNodes.item(j).getTextContent();
                                    if ("Code".equals(statusNodeName)) {
                                        code = statusNodeValue;
                                    } else if ("Message".equals(statusNodeName)) {
                                        message = statusNodeValue;
                                    }
                                }
                            }
                            break;
                        case "Active":
                            active = nodeValue;
                            break;
                        case "MerchantID":
                            merchantId = nodeValue;
                            break;
                        case "MDES":
                            NodeList mdesNodes = childNodes.item(i).getChildNodes();
                            for (int j = 0; j < mdesNodes.getLength(); j++) {
                                if (mdesNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                    String mdesNodeName = mdesNodes.item(j).getNodeName();
                                    String mdesNodeValue = mdesNodes.item(j).getTextContent();
                                    if ("Status".equals(mdesNodeName)) {
                                        mdesStatus = mdesNodeValue;
                                    } else if ("Name".equals(mdesNodeName)) {
                                        mdesName = mdesNodeValue;
                                    } else if ("DebitCreditIndicator".equals(mdesNodeName)) {
                                        mdesDebitCreditIndicator = mdesNodeValue;
                                    }
                                }
                            }
                            break;
                        case "VTS":
                            NodeList vtsNodes = childNodes.item(i).getChildNodes();
                            for (int j = 0; j < vtsNodes.getLength(); j++) {
                                if (vtsNodes.item(j).getNodeType() == Node.ELEMENT_NODE) {
                                    String vtsNodeName = vtsNodes.item(j).getNodeName();
                                    String vtsNodeValue = vtsNodes.item(j).getTextContent();
                                    if ("Status".equals(vtsNodeName)) {
                                        vtsStatus = vtsNodeValue;
                                    } else if ("TRID".equals(vtsNodeName)) {
                                        vtsTrid = vtsNodeValue;
                                    } else if ("TaxNumber".equals(vtsNodeName)) {
                                        vtsTaxNumber = vtsNodeValue;
                                    }
                                }
                            }
                            break;
                    }
                }
            }

            System.out.println("Code: " + code);
            System.out.println("Message: " + message);
            System.out.println("Active: " + active);
            System.out.println("MerchantID: " + merchantId);
            System.out.println("MDES Status: " + mdesStatus);
            System.out.println("MDES Name: " + mdesName);
            System.out.println("MDES DebitCreditIndicator: " + mdesDebitCreditIndicator);
            System.out.println("VTS Status: " + vtsStatus);
            System.out.println("VTS TRID: " + vtsTrid);
            System.out.println("VTS TaxNumber: " + vtsTaxNumber);

            softAssertion.assertNotNull(code, "Code is null");
            softAssertion.assertEquals(code, "000", "Code is not 000");
            softAssertion.assertNotNull(message, "Message is null");
            softAssertion.assertNotNull(active, "Active is null");
            softAssertion.assertNotNull(merchantId, "MerchantID is null");
            softAssertion.assertNotNull(mdesStatus, "MDES Status is null");
            softAssertion.assertNotNull(mdesName, "MDES Name is null");
            softAssertion.assertNotNull(mdesDebitCreditIndicator, "MDES DebitCreditIndicator is null");
            softAssertion.assertNotNull(vtsStatus, "VTS Status is null");
            softAssertion.assertNotNull(vtsTrid, "VTS TRID is null");
            softAssertion.assertNotNull(vtsTaxNumber, "VTS TaxNumber is null");

            // Перевірка статусу VTS
            if (!"ONBOARDED".equals(vtsStatus)) {
                softAssertion.fail("VTS Status is not ONBOARDED: " + vtsStatus);
            }

            // Перевірка статусу MDES
            if (!"REQUEST_SENT".equals(mdesStatus) && !"ONBOARDED".equals(mdesStatus)) {
                softAssertion.fail("MDES Status is not REQUEST_SENT or ONBOARDED: " + mdesStatus);
            }
        }
    }
}
