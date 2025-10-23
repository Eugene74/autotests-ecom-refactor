package tests.com.Service01;

import com.ecom.client.Service01Client;
import com.ecom.db.JDBCConnection;
import com.ecom.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.ecom.core.config.EnvData.URL_TOMEE;
import static com.ecom.core.config.EnvData.merchantID_aval1;
import static com.ecom.core.config.EnvData.terminalID_aval1;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
@Slf4j
public class Service01_JSON {

    private String orderID;

    @BeforeClass
    public void setUp() {
        // Отримання значень з бази даних
        orderID = getLatestOrderId();
        // Переконуємося, що OrderID згенеровано і він не є null
        if (orderID == null) {
            throw new IllegalStateException("OrderID не згенеровано");
        }
        log.info("Loaded config: merchant={}, terminal={}, order={}", merchantID_aval1, terminalID_aval1, orderID);
    }

    @Test
    public void shouldSendJsonAndValidateResponse() throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("merchantId", merchantID_aval1);
        payload.put("terminalId", terminalID_aval1);
        payload.put("totalAmount", 5000);
        payload.put("currency", "980");
        payload.put("orderId", orderID);
        payload.put("purchaseTime", "241115202020");

        String jwt = JwtUtils.createJwt(
                "eyJhbGciOiJSUzI1NiJ9",
                payload.toString(),
                "EXDEhK9kMK0lwTEWH4mm1oJvKm5vVFyXnyDnqEDHDc3mYyXEhLv3Ih6_fdmN-apUPxgV5GEpV0YQWTuSyGF3o32dF0n-A4LrZ93z8Dw7gj9ULLd5ffRE42x0tFL6jNNEnVUbj8WB1UeR6mRN4l4aTRaNU123hq6UIqB_jsTxWJU"
        );

        Service01Client client = new Service01Client(URL_TOMEE);
        String response = client.sendJwtRequest(jwt);
        assertNotNull(response, "Response is null");

        String decoded = JwtUtils.decodePayload(response);
        log.info("Decoded response: {}", decoded);

        JSONObject result = new JSONObject(decoded)
                .getJSONArray("results").getJSONObject(0);

        assertEquals(result.getString("tranCode"), "000", "tranCode must be 000");
        log.info("✅ Test passed successfully");
    }

    private String getLatestOrderId() {
        try (Connection conn = JDBCConnection.getDBConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT ORDER_ID FROM TRAN WHERE TRAN_ID = (SELECT MAX(TRAN_ID) FROM TRAN)"
             );
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getString("ORDER_ID");
            throw new IllegalStateException("No ORDER_ID found in TRAN table");
        } catch (Exception e) {
            throw new RuntimeException("DB query failed: " + e.getMessage(), e);
        }
    }
}