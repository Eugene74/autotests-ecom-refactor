package tests.com.Alias1;

import com.ecom.tests.base.BaseTestAlias1;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;

import static com.ecom.core.config.EnvData.URLAlias1Inguiry;
import static com.ecom.core.config.XMLAliasResource.XML_INQUIRY_ALIAS1_RESOURCE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class InquiryAlias1 extends BaseTestAlias1 {
    private String [] specificTags = {
            "MerchantId",
            "TerminalId",
            "Trackingid",
            "alias",
            "paymentInstrOwner"
    };
    @Test(dependsOnMethods = "tests.com.Alias1.CreateAlias1.testCreateAlias1")
    public void testAliasInquiry() throws Exception {
        String xmlContent = getResourceContent(XML_INQUIRY_ALIAS1_RESOURCE);
        // Використовуйте збережені значення з SharedDataStore
        String requestId = SharedDataStore.requestId;
        String guidFromResponse = SharedDataStore.guidFromResponse;
        if (MerchantID == null || TerminalID == null || requestId == null) {
            throw new IllegalArgumentException("One of the required properties is null. MerchantID: " + MerchantID +
                    ", TerminalID: " + TerminalID + ", RequestID: " + requestId);
        }
        Map<String, String> params = new HashMap<>();
        params.put("MerchantID", MerchantID);
        params.put("TerminalID", TerminalID);
        params.put("RequestID", requestId);
        params.put("GUID", guidFromResponse);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        HttpResponse response = sendPostRequest(URLAlias1Inguiry, xmlContent);
        assertNotNull(response);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Received status code from the server: " + statusCode);
        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");
        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println("Response (XML):" + printSpecificTags(responseContent, specificTags));
                // Парсинг та верифікація XML відповіді
                SoftAssert softAssertion = new SoftAssert();
        verifyXmlResponse(responseContent, softAssertion, "InquiryAliasResult");
        softAssertion.assertAll();
    }
}
