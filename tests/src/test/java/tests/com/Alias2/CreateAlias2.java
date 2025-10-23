package tests.com.Alias2;

import static com.ecom.core.config.EnvData.URL_ALIAS_2_CREATE;
import static com.ecom.core.config.XMLAliasResource.XML_CREATE_ALIAS2_RESOURCE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import com.ecom.tests.base.BaseTestAlias2;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import tests.com.Alias1.SharedDataStore;

public class CreateAlias2 extends BaseTestAlias2 {

  @Test
  public void testCreateAlias2() throws Exception {
    String xmlContent = getResourceContent(XML_CREATE_ALIAS2_RESOURCE);
    String requestId =
        generateUniqueRequestID(); // Генеруємо requestId і будемо використовувати його як
    // TrackingId
    String guid = generateGUID(); // Генеруємо GUID
    SharedDataStore.requestId = requestId;
    // Перевірка ініціалізації змінних Terminal2ID та Merchant2ID
    if (Merchant2ID == null || Terminal2ID == null || requestId == null) {
      throw new IllegalArgumentException(
          "One of the required properties is null. Merchant2ID: "
              + Merchant2ID
              + ", Terminal2ID: "
              + Terminal2ID
              + ", requestId: "
              + requestId);
    }
    // Підставляння змінних у XML-запит
    Map<String, String> params = new HashMap<>();
    params.put("Merchant2ID", Merchant2ID);
    params.put("Terminal2ID", Terminal2ID);
    params.put("TrackingId", requestId);
    params.put("guid", guid);
    params.put("alias2Id", alias2Id);
    System.out.println("Aliace number = " + alias2Id);
    for (Map.Entry<String, String> entry : params.entrySet()) {
      xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
    }
    // Відправка POST-запиту
    HttpResponse response = sendPostRequest(URL_ALIAS_2_CREATE, xmlContent);
    assertNotNull(response);
    // Друкуємо статус код і повну відповідь для налагодження
    int statusCode = response.getStatusLine().getStatusCode();
    System.out.println("Received status code from the server: " + statusCode);
    String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
    // System.out.println("Full Response: " + responseContent);
    assertEquals(
        statusCode,
        200,
        "Received status code "
            + statusCode
            + " from the server, but expected 200"
            + "\nReason: "
            + response.getStatusLine().getReasonPhrase());
    // Парсинг та верифікація XML-відповіді
    SoftAssert softAssertion = new SoftAssert();
    verifyXmlResponse(responseContent, softAssertion);
    softAssertion.assertAll();
    // Зберігання значень з відповіді для подальшого використання
    String createAliasResult = extractCreateAliasResult(responseContent);
    System.out.println("CreateAliasResult: " + createAliasResult);
    // Виведення та зберігання paymentCredentials
    String paymentCredentials = extractPaymentCredentials(responseContent);
    System.out.println("PaymentCredentials: " + paymentCredentials);
    // Зберігаємо значення в SharedDataStore
    SharedDataStore.createAliasResult = createAliasResult;
    SharedDataStore.paymentCredentials = paymentCredentials;
    SharedDataStore.trackingId = requestId; // Зберігання trackingId в SharedDataStore
    System.out.println("Stored trackingId: " + SharedDataStore.trackingId);
  }

  private void verifyXmlResponse(String xml, SoftAssert softAssertion) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

    // Перевірка елемента <CreateAliasResult> на присутність та правильність
    NodeList createAliasResultNodes = doc.getElementsByTagName("CreateAliasResult");
    boolean isCreateAliasResultNotEmpty =
        createAliasResultNodes.getLength() > 0
            && createAliasResultNodes.item(0).getTextContent().trim().length() > 0;
    System.out.println("CreateAliasResult is not empty: " + isCreateAliasResultNotEmpty);
    softAssertion.assertTrue(isCreateAliasResultNotEmpty, "<CreateAliasResult> is empty");

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

  private String extractCreateAliasResult(String xml) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

    NodeList createAliasResultNodes = doc.getElementsByTagName("CreateAliasResult");
    if (createAliasResultNodes.getLength() > 0) {
      return createAliasResultNodes.item(0).getTextContent().trim();
    }
    return null;
  }

  private String extractPaymentCredentials(String xml) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

    NodeList paymentCredentialsNodes = doc.getElementsByTagName("paymentCredentials");
    if (paymentCredentialsNodes.getLength() > 0) {
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      TransformerFactory transFactory = TransformerFactory.newInstance();
      Transformer transformer = transFactory.newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.transform(
          new DOMSource(paymentCredentialsNodes.item(0)), new StreamResult(outputStream));
      return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
    }
    return null;
  }
}
