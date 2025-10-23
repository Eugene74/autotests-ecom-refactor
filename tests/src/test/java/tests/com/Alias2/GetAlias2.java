// Додати змінну aliasValue до SharedDataStore
package tests.com.Alias2;

import static com.ecom.core.config.EnvData.URL_ALIAS_2_GET;
import static com.ecom.core.config.XMLAliasResource.XML_GET_ALIAS2_RESOURCE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import com.ecom.tests.base.BaseTestAlias2;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import tests.com.Alias1.SharedDataStore;

public class GetAlias2 extends BaseTestAlias2 {
  private static final String QUERY =
      "SELECT ALIAS_ID FROM VA_USER_REQUEST WHERE TRACKING_ID = ? AND TYPE = 'CREATE_ALIAS'";

  @Test
  public void testGetAlias2() throws Exception {
    // Переконаємося, що trackingId і aliasId встановлено
    if (SharedDataStore.trackingId == null) {
      throw new IllegalArgumentException("The trackingId is not set.");
    }
    // Отримання значення aliasId з бази даних
    String aliasId = getAliasIdFromDatabase(SharedDataStore.trackingId, QUERY, "ALIAS_ID");
    if (aliasId == null) {
      throw new IllegalArgumentException("The aliasId is not set.");
    }
    SharedDataStore.aliasId = aliasId;
    System.out.println("Fetched aliasId from DB: " + aliasId);
    // Складання XML-запиту
    String xmlContent = getResourceContent(XML_GET_ALIAS2_RESOURCE);
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
    HttpResponse response = sendPostRequest(URL_ALIAS_2_GET, xmlContent);
    assertNotNull(response);
    // Друкуємо статус код і повну відповідь для налагодження
    int statusCode = response.getStatusLine().getStatusCode();
    System.out.println("Received status code from the server: " + statusCode);
    String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
    // System.out.println("Full Response: " + responseContent);
    assertEquals(
        statusCode,
        200,
        "Received status code " + statusCode + " from the server, but expected 200");
    // Парсинг та верифікація XML-відповіді
    SoftAssert softAssertion = new SoftAssert();
    verifyGetAliasResponse(responseContent, softAssertion);
    softAssertion.assertAll();
  }

  private void verifyGetAliasResponse(String xml, SoftAssert softAssertion) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

    // Перевірка елемента <GetAliasResult> на присутність та правильність
    NodeList getAliasResultNodes = doc.getElementsByTagName("GetAliasResult");
    boolean isGetAliasResultNotEmpty =
        getAliasResultNodes.getLength() > 0
            && getAliasResultNodes.item(0).getTextContent().trim().length() > 0;
    System.out.println("GetAliasResult is not empty: " + isGetAliasResultNotEmpty);
    softAssertion.assertTrue(isGetAliasResultNotEmpty, "<GetAliasResult> is empty");

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

      // Збереження aliasValue у SharedDataStore
      NodeList aliasValueNodes = doc.getElementsByTagName("aliasValue");
      if (aliasValueNodes.getLength() > 0) {
        SharedDataStore.aliasValue = aliasValueNodes.item(0).getTextContent().trim();
        System.out.println("Stored aliasValue: " + SharedDataStore.aliasValue);
      }
    }
  }
}
