/**
 * @author semyvolos_h
 * @date 9/5/2023 12:09 PM
 */
package tests.paylink.api.stoplist;

import static com.ecom.tests.support.DocumentTools.getElementFromDocument;
import static io.restassured.RestAssured.given;
import static org.testng.Assert.assertEquals;

import com.ecom.tests.base.BaseTestStopList;
import com.ecom.tests.support.DataDrivenStoplist;
import com.ecom.tests.support.DocumentTools;
import com.ecom.tests.support.RequestsStoplist;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

public class FindItem extends BaseTestStopList {
  @Test(dataProvider = "findItemPan", dataProviderClass = DataDrivenStoplist.class)
  public void findItemPan(
      String merchantId, String terminalId, String trackingId, String type, String pan) {
    Document requestDoc =
        RequestsStoplist.findItemPanDocument(merchantId, terminalId, trackingId, type, pan);

    System.out.println(
        "\n--findItemPan Request--\n\n" + DocumentTools.convertXMLDocumentToString(requestDoc));

    String response =
        given()
            .body(DocumentTools.convertXMLDocumentToString(requestDoc))
            .relaxedHTTPSValidation()
            .when()
            .post(urlFindItem)
            .asString();

    System.out.println("\n\nResponse:\n" + response);

    Document responseDoc = DocumentTools.convertStringToXmlDocument(response);

    assertEquals(getElementFromDocument(responseDoc, "MerchantID"), merchantId, "merchantId");
    assertEquals(getElementFromDocument(responseDoc, "TerminalID"), terminalId, "terminalId");
    assertEquals(getElementFromDocument(responseDoc, "trackingId"), trackingId, "trackingId");
    assertEquals(getElementFromDocument(responseDoc, "found"), "true", "stopListId");
  }
}
