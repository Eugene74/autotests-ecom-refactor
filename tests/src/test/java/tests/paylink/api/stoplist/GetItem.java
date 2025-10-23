/**
 * @author semyvolos_h
 * @date 9/4/2023 5:47 PM
 */
package tests.paylink.api.stoplist;

import com.ecom.core.config.ConfigStoplist;
import com.ecom.tests.base.BaseTestStopList;
import com.ecom.tests.support.DataDrivenStoplist;
import com.ecom.tests.support.DocumentTools;
import com.ecom.tests.support.RequestsStoplist;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import static io.restassured.RestAssured.given;
import static com.ecom.tests.support.DocumentTools.getElementFromDocument;
import static org.testng.Assert.assertEquals;

public class GetItem extends BaseTestStopList {
    @Test(dataProvider="getItemPan", dataProviderClass = DataDrivenStoplist.class)
    public void getItemPan(String merchantId, String terminalId, String trackingId, String stopListId) {
        Document requestDoc = RequestsStoplist.getItemPanDocument(merchantId, terminalId, trackingId, stopListId);

        System.out.println("\n--getItem Request--\n\n" + DocumentTools.convertXMLDocumentToString(requestDoc));

        String response = given()
                .body(DocumentTools.convertXMLDocumentToString(requestDoc))
                .relaxedHTTPSValidation()
                .when()
                .post(urlGetItem).asString();

        System.out.println("\n\nResponse:\n" + response);

        Document responseDoc = DocumentTools.convertStringToXmlDocument(response);

        assertEquals(getElementFromDocument(responseDoc, "MerchantID"), merchantId, "merchantId");
        assertEquals(getElementFromDocument(responseDoc, "TerminalID"), terminalId, "terminalId");
        assertEquals(getElementFromDocument(responseDoc, "trackingId"), trackingId, "trackingId");
        assertEquals(getElementFromDocument(responseDoc, "type"), ConfigStoplist.getProperty("stoplist.create.item.type"), "type");
        assertEquals(getElementFromDocument(responseDoc, "value").length(), 16, "value");
        assertEquals(getElementFromDocument(responseDoc, "expDate"), ConfigStoplist.getProperty("stoplist.create.item.expDate"), "expDate");
        assertEquals(getElementFromDocument(responseDoc, "remark"), ConfigStoplist.getProperty("stoplist.create.item.createRemark"), "remark");
        assertEquals(getElementFromDocument(responseDoc, "created"), ConfigStoplist.getProperty("stoplist.create.item.createDate"), "created");
    }
}