/**
 * @author semyvolos_h
 * @date 9/5/2023 12:12 PM
 */
package tests.paylink.api.stoplist;

import methods.DataDrivenStoplist;
import methods.DocumentTools;
import methods.RequestsStoplist;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import static io.restassured.RestAssured.given;
import static methods.DocumentTools.getElementFromDocument;
import static org.testng.Assert.assertEquals;

public class UpdateItem extends BaseTestStoplist{
    @Test(dataProvider="updateItemPan", dataProviderClass = DataDrivenStoplist.class)
    public void updateItemPan(String merchantId, String terminalId, String trackingId, String stopListId, String expDate, String updateRemark){
        Document requestDoc = RequestsStoplist.updateItemPanDocument(merchantId, terminalId, trackingId, stopListId, expDate, updateRemark);

        System.out.println("\n--updateItemPan Request--\n\n" + DocumentTools.convertXMLDocumentToString(requestDoc));

        String response = given()
                .body(DocumentTools.convertXMLDocumentToString(requestDoc))
                .relaxedHTTPSValidation()
                .when()
                .post(urlUpdateItem).asString();

        System.out.println("\n\nResponse:\n" + response);

        Document responseDoc = DocumentTools.convertStringToXmlDocument(response);

        assertEquals(getElementFromDocument(responseDoc, "MerchantID"), merchantId, "merchantId");
        assertEquals(getElementFromDocument(responseDoc, "TerminalID"), terminalId, "terminalId");
        assertEquals(getElementFromDocument(responseDoc, "trackingId"), trackingId, "trackingId");
        assertEquals(getElementFromDocument(responseDoc, "updated"), "true", "updated");
    }
}