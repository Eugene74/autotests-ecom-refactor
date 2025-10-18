package tests.com.Service01;

import jdbc.JDBCConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class Service01_JSON extends BaseTest {

    private String OrderID;
    private String MerchantID;
    private String TerminalID;
    private String baseUrl;
    private Properties properties;

    @BeforeClass
    public void setUp() {
        // Завантажуємо властивості з endpoint.properties
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream("properties/env.properties")) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Отримання значень з бази даних
        String[] dbValues = getDatabaseValues();
        OrderID = dbValues[0];
        MerchantID = properties.getProperty("MerchantID_AVAL1");
        TerminalID = properties.getProperty("TerminalID_AVAL1");
        baseUrl = properties.getProperty("URLtomee");

        // Переконуємося, що OrderID згенеровано і він не є null
        if (OrderID == null) {
            throw new IllegalStateException("OrderID не згенеровано");
        }

        // Логуємо згенерований OrderID для налагодження
        System.out.println("Згенерований OrderID: " + OrderID);
    }

    @Test
    public void JSON() throws Exception {
        // Заповнення даних з пропертів
        Map<String, String> params = new HashMap<>();
        params.put("merchantId", MerchantID);
        params.put("terminalId", TerminalID);
        params.put("totalAmount", "5000");
        params.put("currency", "980");
        params.put("orderId", OrderID);
        params.put("purchaseTime", "241115202020");

        // Створення JSON payload
        String jsonPayload = String.format(
                "{\"merchantId\":\"%s\",\"terminalId\":\"%s\",\"totalAmount\":%s,\"currency\":\"%s\",\"orderId\":\"%s\",\"purchaseTime\":\"%s\"}",
                params.get("merchantId"), params.get("terminalId"), params.get("totalAmount"), params.get("currency"), params.get("orderId"), params.get("purchaseTime")
        );

        // Кодування payload у Base64
        String encodedPayload = Base64.getEncoder().encodeToString(jsonPayload.getBytes(StandardCharsets.UTF_8));

        // Створення JWT
        String jwtHeader = "eyJhbGciOiJSUzI1NiJ9";
        String jwtSignature = "EXDEhK9kMK0lwTEWH4mm1oJvKm5vVFyXnyDnqEDHDc3mYyXEhLv3Ih6_fdmN-apUPxgV5GEpV0YQWTuSyGF3o32dF0n-A4LrZ93z8Dw7gj9ULLd5ffRE42x0tFL6jNNEnVUbj8WB1UeR6mRN4l4aTRaNU123hq6UIqB_jsTxWJU";
        String jwt = String.format("{\"header\":\"%s\",\"payload\":\"%s\",\"signature\":\"%s\"}", jwtHeader, encodedPayload, jwtSignature);

        // Логуємо запит JWT
        System.out.println("Запит (JWT): " + jwt);

        // Відправка POST запиту
        String url = baseUrl + "/go/service/01";
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(jwt));
        post.setHeader("Content-Type", "application/json");

        CloseableHttpClient client = HttpClients.createDefault();
        HttpResponse response = client.execute(post);
        assertNotNull(response);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(statusCode, 200, "Отримано статус-код " + statusCode + " від сервера, але очікувався 200");

        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println("Відповідь (JWT): " + responseContent);

        // Декодування відповіді
        String decodedResponse = decodeJWT(responseContent);

        // Перевірка значень у розшифрованій відповіді
        verifyDecodedResponse(decodedResponse);
    }

    private String decodeJWT(String jwt) {
        // Обробка JSON-об'єкта, який повертається як відповідь
        JSONObject jwtObject = new JSONObject(jwt);
        String encodedPayload = jwtObject.getString("payload");
        String payload = new String(Base64.getDecoder().decode(encodedPayload), StandardCharsets.UTF_8);
        return payload;
    }

    private void verifyDecodedResponse(String decodedResponse) {
        // Друк декодованої відповіді
        System.out.println("Декодована відповідь: " + decodedResponse);

        // Перевірка значення tranCode
        JSONObject jsonResponse = new JSONObject(decodedResponse);
        String tranCode = jsonResponse.getJSONArray("results").getJSONObject(0).getString("tranCode");

        if ("000".equals(tranCode)) {
            System.out.println("Тест пройдено успішно.");
        } else {
            String approvalCode = jsonResponse.getJSONArray("results").getJSONObject(0).getString("approvalCode");
            System.out.println("Тест не пройдено. TranCode: " + tranCode + ", ApprovalCode: " + approvalCode);
            throw new AssertionError("TranCode не дорівнює 000, отримано: " + tranCode);
        }
    }

    private String[] getDatabaseValues() {
        String[] values = new String[3];
        try (Connection connection = JDBCConnection.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT RRN, APPROVAL_CODE, ORDER_ID FROM TRAN WHERE TRAN_ID = (SELECT MAX(TRAN_ID) FROM TRAN)"
             )) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                values[0] = resultSet.getString("ORDER_ID"); // ORDER_ID
                values[1] = resultSet.getString("APPROVAL_CODE"); // APPROVAL_CODE
                values[2] = resultSet.getString("RRN"); // RRN
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Не вдалося виконати запит до бази даних: " + e.getMessage());
        }
        return values;
    }
}
