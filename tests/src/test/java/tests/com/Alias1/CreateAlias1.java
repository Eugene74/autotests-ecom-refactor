package tests.com.Alias1;

import com.ecom.tests.base.BaseTestAlias1;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.HashMap;
import java.util.Map;

import static com.ecom.core.config.EnvData.URLAlias1Create;
import static com.ecom.core.config.XMLAliasResource.XML_CREATE_ALIAS1_RESOURCE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CreateAlias1 extends BaseTestAlias1 {
    private String [] specificTags = {
            "MerchantId",
            "TerminalId",
            "Trackingid",
            "guid",
            "updateAlias"
    };
    @Test
    public void testCreateAlias1() throws Exception {
        String xmlContent = getResourceContent(XML_CREATE_ALIAS1_RESOURCE);
        String requestId = generateUniqueRequestID();
        SharedDataStore.requestId = requestId;
        if (MerchantID == null || TerminalID == null || requestId == null) {
            throw new IllegalArgumentException("One of the required properties is null. MerchantID: " + MerchantID +
                    ", TerminalID: " + TerminalID + ", RequestID: " + requestId);
        }
        Map<String, String> params = new HashMap<>();
        params.put("MerchantID", MerchantID);
        params.put("TerminalID", TerminalID);
        params.put("RequestID", requestId);
        params.put("alias1Id", alias1Id);
        System.out.println("Aliace number = " + alias2Id);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        HttpResponse response = sendPostRequest(URLAlias1Create, xmlContent);
        assertNotNull(response);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Received status code from the server: " + statusCode);
        assertEquals(statusCode, 200,
                "Received status code " + statusCode + " from the server, but expected 200"
                        + "\nReason: " + response.getStatusLine().getReasonPhrase());
        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");

        System.out.println("Response (XML):" + printSpecificTags(responseContent, specificTags));
        // Парсинг та верифікація XML відповіді
        SoftAssert softAssertion = new SoftAssert();
        SharedDataStore.guidFromResponse = verifyXmlResponse(responseContent, softAssertion, "guid");
        softAssertion.assertAll();
    }
}
