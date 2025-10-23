package tests.com.Service01;

import static com.ecom.core.config.CardConfig.cardVISA;
import static com.ecom.core.config.EnvData.MERCHANT_ID_AVAL_1;
import static com.ecom.core.config.EnvData.TERMINAL_ID_AVAL_1;
import static com.ecom.core.config.EnvData.URL_TOMEE;
import static com.ecom.db.JDBCMethods.verifyOrders;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import com.ecom.tests.base.BaseApiTest;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class Purchase extends BaseApiTest {

  private String approvalCode;
  private String rrn;
  private String orderID;

  @Test
  public void authorization() throws Exception {
    // Генерація динамічного OrderID
    orderID = generateOrderID();
    // Завантаження даних картки з окремого файлу
    String[] cardData = cardVISA; // cardDetails.split(";");
    // Заповнення XML з пропертів
    String xmlContent = getResourceContent("XMLPurchase/Purchase.xml");
    Map<String, String> params = new HashMap<>();
    params.put("MerchantID", MERCHANT_ID_AVAL_1);
    params.put("TerminalID", TERMINAL_ID_AVAL_1);
    params.put("OrderID", orderID);
    params.put("CardNum", cardData[0].trim());
    params.put("ExpYear", cardData[1].trim());
    params.put("ExpMonth", cardData[2].trim());
    params.put("CVNum", cardData[3].trim());

    for (Map.Entry<String, String> entry : params.entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      if (value != null) {
        xmlContent = xmlContent.replace("${" + key + "}", value);
      } else {
        throw new IllegalArgumentException("Value for " + key + " is null");
      }
    }

    // Відправка POST запиту
    String url = URL_TOMEE + "/go/service/02";
    HttpResponse response = sendPostRequest(url, xmlContent);
    assertNotNull(response);
    int statusCode = response.getStatusLine().getStatusCode();
    assertEquals(
        statusCode,
        200,
        "Received status code " + statusCode + " from the server, but expected 200");

    String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");

    // Верифікація XML відповіді
    SoftAssert softAssertion = new SoftAssert();
    String formattedResult = verifyXmlResponse(responseContent, softAssertion);
    softAssertion.assertAll();

    // Друк необхідних тегів
    System.out.println("Response:\n" + formattedResult);

    // Перевірка даних в базі даних
    String orderIdFromDb = verifyOrders(approvalCode, rrn);
    System.out.println("OrderID from DB: " + orderIdFromDb);

    // Збереження OrderID для подальшого використання
    orderID = orderIdFromDb;
  }

  private String verifyXmlResponse(String xml, SoftAssert softAssertion) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));

    NodeList tranCodeNodes = doc.getElementsByTagName("TranCode");
    String tranCode =
        tranCodeNodes.getLength() > 0 ? tranCodeNodes.item(0).getTextContent().trim() : "";
    if (tranCode.isEmpty()) {
      softAssertion.fail("TranCode element not found in the response");
    } else {
      softAssertion.assertEquals(tranCode, "000", "TranCode is not 000");
    }

    NodeList approvalCodeNodes = doc.getElementsByTagName("ApprovalCode");
    approvalCode =
        approvalCodeNodes.getLength() > 0 ? approvalCodeNodes.item(0).getTextContent().trim() : "";
    if (approvalCode.isEmpty()) {
      softAssertion.fail("ApprovalCode element not found in the response");
    }

    NodeList rrnNodes = doc.getElementsByTagName("Rrn");
    rrn = rrnNodes.getLength() > 0 ? rrnNodes.item(0).getTextContent().trim() : "";
    if (rrn.isEmpty()) {
      softAssertion.fail("Rrn element not found in the response");
    }

    NodeList commentNodes = doc.getElementsByTagName("Comment");
    String comment =
        commentNodes.getLength() > 0 ? commentNodes.item(0).getTextContent().trim() : "";

    NodeList cvResultNodes = doc.getElementsByTagName("CVResult");
    String cvResult =
        cvResultNodes.getLength() > 0 ? cvResultNodes.item(0).getTextContent().trim() : "";

    NodeList hostCodeNodes = doc.getElementsByTagName("HostCode");
    String hostCode =
        hostCodeNodes.getLength() > 0 ? hostCodeNodes.item(0).getTextContent().trim() : "";

    return formatResult(tranCode, approvalCode, rrn, comment, cvResult, hostCode);
  }

  private String formatResult(
      String tranCode,
      String approvalCode,
      String rrn,
      String comment,
      String cvResult,
      String hostCode) {
    return String.format(
        "<TranCode>%s</TranCode>\n<ApprovalCode>%s</ApprovalCode>\n<Rrn>%s</Rrn>\n<Comment>%s</Comment>\n<CVResult>%s</CVResult>\n<HostCode>%s</HostCode>",
        tranCode, approvalCode, rrn, comment, cvResult, hostCode);
  }

  /* private String verifyDatabase(String approvalCode, String rrn) throws Exception {
      try (Connection connection = getDBConnection()) {
          String sql = "SELECT ORDER_ID FROM TRAN WHERE RRN = ? AND APPROVAL_CODE = ?";
          try (PreparedStatement statement = connection.prepareStatement(sql)) {
              statement.setString(1, rrn);
              statement.setString(2, approvalCode);
              ResultSet resultSet = statement.executeQuery();
              if (resultSet.next()) {
                  return resultSet.getString("ORDER_ID");
              } else {
                  throw new IllegalArgumentException("No entry found with RRN: " + rrn + " and Approval Code: " + approvalCode);
              }
          }
      }
  }*/
  // Метод для генерації динамічного OrderID
  protected String generateOrderID() {
    Random random = new Random();
    int orderId = random.nextInt(1000000) + 1;
    return "24" + orderId;
  }
}
