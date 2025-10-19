package tests.dashboard.invoice;

import com.ecom.core.util.ResourceUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

public class GetStatusInvoice {
    @Test
    public void createAndSingMerchantInvoice() throws Exception {

        String urlStatus = "https://ecgec.tad.upc.intranet:8442/dashboard/api/public/merchant-invoices/status";

        String file = "template/json/JsonBody";
//JSONObject jsonObject
//        jsonObject.put("header", "eyJhbGciOiJSUzI1NiJ9");
//        jsonObject.put("payload", "eyJzdWIiOiIxMjM0NTY3ODkwIiwib3JkZXJJZCI6Ik15T3JkZXJUZXN0MTkxMDIwMjAxIiwibWVyY2hhbnRJZCI6IjE4MDAwMDciLCJ0ZXJtaW5hbElkIjoiRTE4MDAwMDcifQ");
//        jsonObject.put("signature", "lpCNY7dCtbxtyrh0W5XBMfOIyS0rpTedGhwYAqnmLBpm8ttkHfhy0NJmyBM9S9Nm7JiEACe0zItGpQ8mzAYIYlP-A1DYNGu0SknVx-oNEcfZ2auUj_rjnBj1qtty7wRQZMwVf2YYxgupB0X3ZmgNHgvIq24_CnXEB8Zufzz7eTmjXS3uzd1wjMgUgYn8wJQq4bI98iDKp8JDNTni9nnier4KLhuwTghkW3WJlYSm97q6sC42zpJbMSbtNysI20nQSEb_-eMy0ZNPSlkgwROeKb6KlzmGfjZmzWQTcSZjwC1HjwpeTi-0ZDfyYRnkr1O4QFBcCevrDOxm_c5siDGTrQ");



//
//        String getInvoiceStatus = jsonObject.toString();
//        System.out.println(getInvoiceStatus);

        String json = readFileAsString(file);
        System.out.println(json);
        Response response= given().accept(ContentType.JSON).contentType(ContentType.JSON).baseUri(urlStatus).body(json).when().post();
        System.out.println(response.asString());

    }

    public static String readFileAsString(String file) {
        return ResourceUtils.readAsString(file);
    }


    @Test
    public void payByToken() throws Exception {

        String payload = "{\n" +
                "\"MerchantID\":\"1800007\",\n" +
                "\"TerminalID\":\"E1800007\",\n" +
                "\"OrderID\":\"40324t\",\n" +
                "\"UPCToken\":\"3341EACA58D1DB9F7A3B2E49190FA0AC\",\n" +
                "\"TotalAmount\":\"5000\",\n" +
                "\"Currency\":\"980\",\n" +
                "\"PurchaseTime\":\"200519220844\",\n" +
                "\"PurchaseDesc\":\"token\",\n" +
                "\"recurrent\":false\n" +
                "\"cvv\":950\n" +
                "}";

        String urlStatus = "https://ecgec.tad.upc.intranet:8443/go/payByToken";

        String file = "template/json/JsonBody";

        String json = readFileAsString(file);
        System.out.println(json);
        Response response= given().accept(ContentType.JSON).contentType(ContentType.JSON).baseUri(urlStatus).body(json).when().post();
        System.out.println(response.asString());

    }

}
