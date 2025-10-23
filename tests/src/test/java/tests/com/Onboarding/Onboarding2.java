package tests.com.Onboarding;

import com.ecom.tests.base.BaseTestOnboarding;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class Onboarding2 extends BaseTestOnboarding {

    @Test
    public void testSendStaticXML() throws Exception {
        String xmlContent = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
                "<ECommerceConnect xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\" " +
                "xmlns:noNamespaceSchemaLocation=\"https://secure.upc.ua/go/pub/schema/xmlpay-2.4.xsd\" " +
                "xmlns:xenc=\"http://www.w3.org/2001/04/xmlenc#\" " +
                "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                "xsi:noNamespaceSchemaLocation=\"https://secure.upc.ua/go/pub/schema/xmlpay-2.4.xsd\">" +
                "<Message id=\"${__counter(FALSE)}\" version=\"1.0\">" +
                "<XMLOnboardStatusRequest xmlns:xd=\"http://www.w3.org/2000/09/xmldsig#\">" +
                "<MerchantID>1000027</MerchantID>" +
                "<TerminalID>E1000027</TerminalID>" +
                "<RequestID>1ic6h2q5e</RequestID>" +
                "</XMLOnboardStatusRequest>" +
                "</Message>" +
                "</ECommerceConnect>";

        // Логування XML, що відправляється
        System.out.println("Sending XML: " + xmlContent);
        // Додаємо паузу перед відправкою запиту
        Thread.sleep(5000); //todo remove later
        HttpResponse response = sendGetRequestWithEntity(baseUrl + "/go/merchants/status/external", xmlContent);
        assertNotNull(response);
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println("Received status code from the server: " + statusCode);
        // Перевірка статус-коду HTTP
        assertEquals(statusCode, 200, "Received status code " + statusCode + " from the server, but expected 200");
        String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
        // Логування отриманої відповіді
        System.out.println("Received Response: " + responseContent);
    }
}
