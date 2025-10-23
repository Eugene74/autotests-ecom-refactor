package tests.com.Cross;

import com.ecom.core.config.PropertiesManager;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import com.ecom.tests.base.BaseTestService01;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CrossBorderXML extends BaseTestService01 {

    private Properties properties;

    @BeforeClass
    @Override
    public void setUp() {
        try {
            super.setUp();
            properties = new Properties();
            PropertiesManager manager = PropertiesManager.getInstance();
            properties.putAll(manager.getEnvProperties());
            properties.putAll(manager.getCardProperties());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load properties", e);
        }
    }

    @Test
    public void sendXmlRequest() throws Exception {
        // Генерація динамічного OrderID
        String orderID = generateOrderID();

        // Заповнення XML з пропертів
        String xmlContent = getResourceContent("XMLCrossBorder/CrossBorder.xml");
        Map<String, String> params = new HashMap<>();
        params.put("Merchant3ID", properties.getProperty("MerchantID_AVAL5"));
        params.put("Terminal3ID", properties.getProperty("TerminalID_AVAL5"));
        params.put("OrderID", orderID);
        params.put("CardNum", CardNum);  // Використовуємо CardNum з BaseTest
        params.put("ExpYear", ExpYear);  // Використовуємо ExpYear з BaseTest
        params.put("ExpMonth", ExpMonth);  // Використовуємо ExpMonth з BaseTest

        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value != null) {
                xmlContent = xmlContent.replace("${" + key + "}", value);
            } else {
                throw new IllegalArgumentException("Value for " + key + " is null");
            }
        }

        // Роздруківка XML-запиту для перевірки
        //System.out.println("XML Request:\n" + xmlContent);

        // Відправка POST запиту
        String baseUrl = properties.getProperty("URLtomcat");
        String url = baseUrl + "/mt/tran";
        HttpResponse response = sendPostRequest(url, xmlContent);
        assertNotNull(response);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");

        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");

        // Роздруківка відповіді сервера для перевірки
        //System.out.println("Response from server:\n" + responseContent);

        // Верифікація XML відповіді
        SoftAssert softAssertion = new SoftAssert();
        String trackingId = verifyXmlResponse(responseContent, softAssertion);
        softAssertion.assertAll();

        // Друк необхідних тегів
        //System.out.println("Response:\n" + responseContent);

        // Перевірка даних в базі даних
        verifyDatabase(trackingId);
    }

    private String verifyXmlResponse(String xml, SoftAssert softAssertion) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

        NodeList statusCodeNodes = doc.getElementsByTagName("Code");
        String statusCode = statusCodeNodes.getLength() > 0 ? statusCodeNodes.item(0).getTextContent().trim() : "";
        if (statusCode.isEmpty()) {
            softAssertion.fail("Code element not found in the response");
        } else {
            softAssertion.assertEquals(statusCode, "000", "Code is not 000");
        }

        NodeList approvalCodeNodes = doc.getElementsByTagName("ApprovalCode");
        String approvalCode = approvalCodeNodes.getLength() > 0 ? approvalCodeNodes.item(0).getTextContent().trim() : "";
        if (approvalCode.isEmpty()) {
            softAssertion.fail("ApprovalCode element not found in the response");
        }

        NodeList rrnNodes = doc.getElementsByTagName("RRN");
        String rrn = rrnNodes.getLength() > 0 ? rrnNodes.item(0).getTextContent().trim() : "";
        if (rrn.isEmpty()) {
            softAssertion.fail("RRN element not found in the response");
        }

        NodeList trackingIdNodes = doc.getElementsByTagName("TrackingId");
        String trackingId = trackingIdNodes.getLength() > 0 ? trackingIdNodes.item(0).getTextContent().trim() : "";
        if (trackingId.isEmpty()) {
            softAssertion.fail("TrackingId element not found in the response");
        }

        return trackingId;
    }

    private void verifyDatabase(String trackingId) throws Exception {
        try (Connection connection = getDBConnection()) {
            String sql = "SELECT MT_REQUESTS_ID, MERCHANT_CODE, STATUS_CODE, STATUS, TRACKING_ID " +
                    "FROM MT_REQUESTS " +
                    "WHERE TRACKING_ID = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, trackingId);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    int mtRequestId = resultSet.getInt("MT_REQUESTS_ID");
                    String merchantCode = resultSet.getString("MERCHANT_CODE");
                    String statusCode = resultSet.getString("STATUS_CODE");
                    String status = resultSet.getString("STATUS");

                    System.out.println("MT_REQUESTS_ID: " + mtRequestId);
                    System.out.println("MERCHANT_CODE: " + merchantCode);
                    System.out.println("STATUS_CODE: " + statusCode);
                    System.out.println("STATUS: " + status);
                    System.out.println("TRACKING_ID: " + trackingId);

                    // Перевірка значень
                    Assert.assertEquals(merchantCode, "1000148", "MERCHANT_CODE не відповідає очікуваному значенню");
                    Assert.assertEquals(statusCode, "000", "STATUS_CODE не відповідає очікуваному значенню");
                    Assert.assertEquals(status, "OK", "STATUS не відповідає очікуваному значенню");
                } else {
                    throw new IllegalArgumentException("No entry found with TRACKING_ID: " + trackingId);
                }
            }

            try (PreparedStatement tranStatement = connection.prepareStatement(
                    "SELECT OP_TYPE, PURCHASE_DESC FROM TRAN WHERE ORDER_ID = ?")) {
                tranStatement.setString(1, trackingId);

                try (ResultSet tranResultSet = tranStatement.executeQuery()) {
                    if (tranResultSet.next()) {
                        String opType = tranResultSet.getString("OP_TYPE");
                        String purchaseDesc = tranResultSet.getString("PURCHASE_DESC");

                        System.out.println("OP_TYPE: " + opType);
                        System.out.println("PURCHASE_DESC: " + purchaseDesc);

                        // Перевірка значень
                        Assert.assertEquals(opType, "11", "OP_TYPE не відповідає очікуваному значенню");
                        Assert.assertEquals(purchaseDesc, "P2P Cross-Border Transfer", "PURCHASE_DESC не відповідає очікуваному значенню");
                    }
                }
            }
        } catch (Exception e) {
            // Обробка винятку SQL
            e.printStackTrace();
            Assert.fail("Не вдалося виконати запит до бази даних: " + e.getMessage());
        }
    }
}
