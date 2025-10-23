/**
 * @author semyvolos_h
 * @date 8/29/2023 1:40 PM
 */
package tests.paylink.api.stoplist;

import static com.ecom.core.config.EnvData.ENVIRONMENT;
import static com.ecom.tests.support.DocumentTools.getElementFromDocument;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import com.ecom.tests.base.BaseTestStopList;
import com.ecom.tests.support.DataDrivenStoplist;
import com.ecom.tests.support.DocumentTools;
import com.ecom.tests.support.RequestsStoplist;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

public class CreateItem extends BaseTestStopList {
  public static String stopListId;

  @Test(dataProvider = "createItemPan", dataProviderClass = DataDrivenStoplist.class)
  public void createItemPan(
      String merchantId,
      String terminalId,
      String trackingId,
      String type,
      String pan,
      String expDate,
      String createRemark,
      String createDate) {
    Document requestDoc =
        RequestsStoplist.createItemPanDocument(
            merchantId, terminalId, trackingId, type, pan, expDate, createRemark, createDate);

    System.out.println(
        "\n--createItemPan Request--\n\n" + DocumentTools.convertXMLDocumentToString(requestDoc));

    String response =
        given()
            .body(DocumentTools.convertXMLDocumentToString(requestDoc))
            .relaxedHTTPSValidation()
            .when()
            .post(urlCreateItem)
            .asString();

    System.out.println("\n\nResponse:\n" + response);

    Document responseDoc = DocumentTools.convertStringToXmlDocument(response);

    assertEquals(getElementFromDocument(responseDoc, "MerchantID"), merchantId, "merchantId");
    assertEquals(getElementFromDocument(responseDoc, "TerminalID"), terminalId, "terminalId");
    assertEquals(getElementFromDocument(responseDoc, "trackingId"), trackingId, "trackingId");
    if (ENVIRONMENT.equals("release") || ENVIRONMENT.equals("dev")) {
      assertEquals(getElementFromDocument(responseDoc, "stopListId").length(), 6, "stopListId");
    } else {
      assertEquals(getElementFromDocument(responseDoc, "stopListId").length(), 6, "stopListId");
    }
    stopListId = getElementFromDocument(responseDoc, "stopListId");
  }
}
