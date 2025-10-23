package tests.com.Alias1;

import static com.ecom.core.config.EnvData.URL_ALIAS_1_DELETE;
import static com.ecom.core.config.XMLAliasResource.XML_DELETE_ALIAS1_RESOURCE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import com.ecom.tests.base.BaseTestAlias1;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class DeleteAlias1 extends BaseTestAlias1 {
  private String[] specificTags = {
    "MerchantId", "TerminalId", "Trackingid", "DeleteAliasResult",
  };

  @Test(
      dependsOnMethods = {
        "tests.com.Alias1.CreateAlias1.testCreateAlias1",
        "tests.com.Alias1.UpdateAlias1.testUpdateAlias1",
        "tests.com.Alias1.InquiryAlias1.testAliasInquiry"
      })
  public void testDeleteAlias1() throws Exception {
    String xmlContent = getResourceContent(XML_DELETE_ALIAS1_RESOURCE);

    // Використовуйте збережені значення з SharedDataStore
    String requestId = SharedDataStore.requestId;
    String guidFromResponse = SharedDataStore.guidFromResponse;
    Map<String, String> params = new HashMap<>();
    params.put("MerchantID", MerchantID);
    params.put("TerminalID", TerminalID);
    params.put("RequestID", requestId);
    params.put("GUID", guidFromResponse);
    params.put("alias1Id", alias1Id);
    for (Map.Entry<String, String> entry : params.entrySet()) {
      xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
    }
    HttpResponse response = sendDeleteRequest(URL_ALIAS_1_DELETE, xmlContent);
    assertNotNull(response);
    int statusCode = response.getStatusLine().getStatusCode();
    System.out.println("Received status code from the server: " + statusCode);
    assertEquals(
        statusCode,
        200,
        "Received status code " + statusCode + " from the server, but expected 200");
    String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
    System.out.println("Response (XML):" + printSpecificTags(responseContent, specificTags));
    // Парсинг та верифікація XML відповіді
    SoftAssert softAssertion = new SoftAssert();
    verifyXmlResponse(responseContent, softAssertion, "DeleteAliasResult");
    softAssertion.assertAll();
  }
}
