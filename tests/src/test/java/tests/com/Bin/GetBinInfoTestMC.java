package tests.com.Bin;

import static com.ecom.core.config.EnvData.URL_BIN_INFO;
import static com.ecom.core.config.XMLBinInfoResource.XML_GET_INFO_RESOURCE_MC;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import com.ecom.tests.base.BaseTestBinInfo;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class GetBinInfoTestMC extends BaseTestBinInfo {

  @Test() // TODO: Remove enabled = false annotation when ready to run this test
  public void testGetBinInfoForMC() throws Exception {
    String xmlContent = getResourceContent(XML_GET_INFO_RESOURCE_MC);
    String requestId = generateUniqueRequestID();
    if (MerchantID == null || TerminalID == null || requestId == null) {
      throw new IllegalArgumentException(
          "One of the required properties is null. MerchantID: "
              + MerchantID
              + ", TerminalID: "
              + TerminalID
              + ", RequestID: "
              + requestId);
    }
    Map<String, String> params = new HashMap<>();
    params.put("MerchantID", MerchantID);
    params.put("TerminalID", TerminalID);
    params.put("RequestID", requestId);
    for (Map.Entry<String, String> entry : params.entrySet()) {
      xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
    }
    HttpResponse response = sendPostRequest(URL_BIN_INFO, xmlContent);
    assertNotNull(response);
    int statusCode = response.getStatusLine().getStatusCode();
    System.out.println("Received status code from the server: " + statusCode);
    // Перевірка статус-коду HTTP
    assertEquals(
        statusCode,
        200,
        "Received status code " + statusCode + " from the server, but expected 200");
    String responseContent = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
    // System.out.println("Response (XML): " + responseContent);
    // Парсинг та верифікація XML відповіді
    SoftAssert softAssertion = new SoftAssert();
    verifyXmlResponse(responseContent, softAssertion, "ListBinInfo");
    softAssertion.assertAll();
  }
}
