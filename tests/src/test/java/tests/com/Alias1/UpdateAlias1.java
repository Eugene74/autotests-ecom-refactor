package tests.com.Alias1;

import com.ecom.tests.base.BaseTestAlias1;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import static com.ecom.core.config.EnvData.URLAlias1Update;
import static com.ecom.core.config.XMLAliasResource.XML_UPDATE_ALIAS1_RESOURCE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class UpdateAlias1 extends BaseTestAlias1 {
    private String [] specificTags = {
            "MerchantId",
            "TerminalId",
            "Trackingid",
            "guid",
    };
    @Test(dependsOnMethods = "tests.com.Alias1.CreateAlias1.testCreateAlias1")
    public void testUpdateAlias1() throws Exception {
        String xmlContent = getResourceContent(XML_UPDATE_ALIAS1_RESOURCE);
        // Використовуйте збережені значення
        String requestId = SharedDataStore.requestId;
        String guidFromResponse = SharedDataStore.guidFromResponse;
        // Отримання GUID з бази даних
        String guidFromDb = getGuidFromDb(requestId);
        assertEquals(guidFromDb, guidFromResponse, "GUID from DB doesn't match response GUID");
        Map<String, String> params = new HashMap<>();
        params.put("MerchantID", MerchantID);
        params.put("TerminalID", TerminalID);
        params.put("RequestID", requestId);
        params.put("GUID", guidFromDb);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        HttpResponse response = sendPutRequest(URLAlias1Update, xmlContent);  // Використайте sendPutRequest тут
        assertNotNull(response);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Received status code from the server: " + statusCode);
        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");
        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println("Response (XML):" + printSpecificTags(responseContent, specificTags));
                // Парсинг та верифікація XML відповіді
                SoftAssert softAssertion = new SoftAssert();
        verifyXmlResponse(responseContent, softAssertion, "guid");
        softAssertion.assertAll();
    }

    private String getGuidFromDb(String requestId) throws Exception {
        try (Connection connection = getDBConnection()) {
            String sql = "SELECT GUID FROM VA_USER_REQUEST WHERE TRACKING_ID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, requestId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getString("GUID");
                } else {
                    throw new IllegalArgumentException("No entry found with Tracking ID: " + requestId);
                }
            }
        }
    }
}
