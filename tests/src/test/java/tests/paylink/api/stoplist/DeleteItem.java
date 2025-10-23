/**
 * @author semyvolos_h
 * @date 9/4/2023 4:13 PM
 */
package tests.paylink.api.stoplist;

import com.ecom.tests.base.BaseTestStopList;
import com.ecom.tests.support.DataDrivenStoplist;
import com.ecom.tests.support.DocumentTools;
import com.ecom.tests.support.RequestsStoplist;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import static io.restassured.RestAssured.given;
import static com.ecom.tests.support.DocumentTools.getElementFromDocument;
import static org.testng.Assert.assertEquals;

public class DeleteItem extends BaseTestStopList {
    @Test(dataProvider="deleteItemPan", dataProviderClass = DataDrivenStoplist.class)
    public void deleteItemPan(String merchantId, String terminalId, String trackingId, String stopListId) {
        Document requestDoc = RequestsStoplist.deleteItemPanDocument(merchantId, terminalId, trackingId, stopListId);

        System.out.println("\n--deleteItem Request--\n\n" + DocumentTools.convertXMLDocumentToString(requestDoc));

        String response = given()
                .body(DocumentTools.convertXMLDocumentToString(requestDoc))
                .relaxedHTTPSValidation()
                .when()
                .post(urlDeleteItem).asString();

        System.out.println("\n\nResponse:\n" + response);

        Document responseDoc = DocumentTools.convertStringToXmlDocument(response);

        assertEquals(getElementFromDocument(responseDoc, "MerchantID"), merchantId, "merchantId");
        assertEquals(getElementFromDocument(responseDoc, "TerminalID"), terminalId, "terminalId");
        assertEquals(getElementFromDocument(responseDoc, "trackingId"), trackingId, "trackingId");
    }
}
