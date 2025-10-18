package tests.paylink.tokens.json;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseTest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import static io.restassured.RestAssured.given;
import static jdbc.JDBCMethods.*;
import static jdbc.JDBCMethods.setMerchantAtt;
import static methods.DocumentTools.*;
import static methods.DocumentTools.verifiedDataFromDB;
import static methods.PaylinkRequests.payment;
import static methods.RequestSender.sendRequest;
import static org.testng.Assert.assertEquals;
import static type.Attributes.ALLOW_SUPPORT_TOKEN;

public class TokenJson extends BaseTest {
    private String tokenId;
    private int tranId;

    @BeforeClass
    public void setParam(){
        setMerchantAtt(id_AVAL, ALLOW_SUPPORT_TOKEN,"true");
        System.out.println("ALLOW_SUPPORT_TOKEN: TRUE");
    }

    @Test
    public void Payment(){
        Document requestDoc = payment(cardVISA, "payment", merchant_AVAL, terminal_AVAL);
        Document responseDoc = sendRequest(URL, requestDoc);
        System.out.println("--PAYMENT--\nRequest\n" + printRequest(requestDoc));
        System.out.println("Response\n" + printResponse(responseDoc));
        tokenId = getElementFromDocument(responseDoc, "tokenId");

        tranId = getTranIdByOrder(getElementFromDocument(requestDoc, "OrderID"));
        String approvalCode = getElementFromDocument(responseDoc, "ApprovalCode");
        String rrn = getElementFromDocument(responseDoc, "Rrn");

        assertEquals(getElementFromDocument(responseDoc, "TranCode"), "000", "TranCode");
        assertEquals(getElementFromDocument(responseDoc, "CVResult"), "M", "CVResult");
        assertEquals(getElementFromDocument(responseDoc, "HostCode"), "000", "HostCode");
        assertEquals(approvalCode.length(), 6, "ApprovalCode");
        assertEquals(rrn.length(), 12, "Rrn");
        assertEquals(getValueFromTRAN(tranId, "ECI"), "07", "ECI");

        System.out.println("Verified:");
        String[] verifiedResponse = {"TranCode", "CVResult", "HostCode", "Rrn", "ApprovalCode"};
        String[] verifiedResponseFromDB = {"ECI"};
        try {
            verifiedDataFromResponse(responseDoc, verifiedResponse);
            verifiedDataFromDB(tranId, verifiedResponseFromDB);
        } catch (Exception e) {
            System.out.println("TEST FAILED");
        }
    }

    @Test (dependsOnMethods = "Payment")
    public void TokenPayment(){
        String headerBase64 = "eyJhbGciOiJSUzI1NiJ9";
        String signatureBase64 = "aFeC_5bxnRrAEc_Ni41yPKtgwXMwXMRm-3LS-1N1N66VsXZzXHXi7JdOOypWJ-IZELH13Q60wLQF-xxFMxa1exo8ACx4dxFGmCZE7i1znNZIgzffxcHtPvqqaG3T4QW51kUD_nKiaGo8Uw2NkCVgV3i1sLmFMVaC1SEB9KBw5A3SiwfleRoKMigxcfdpZt-_6YnJ8Um00ZJ2gBhRKpkoEIYZZSH9UYlBBeI9uLYzrGiDRhwDfXs-87H5ni-pq1h2ZYuXrUuRewqmSTlKiFb-9U2hz1EmdaR9NEzwOXcKbG60wvILEDp3xC-hVhl8ojkC0UnnlYJ1jRPtLPmmLVNUPw";

        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyMMddHHmmss");

        String payload = "{\n" +
                "\"MerchantID\":\"" + merchant_AVAL + "\",\n" +
                "\"TerminalID\":\"" + terminal_AVAL + "\",\n" +
                "\"OrderID\":\"" + df.format(date) + "\",\n" +
                "\"UPCToken\":\"" + tokenId + "\",\n" +
                "\"TotalAmount\":\"777\",\n" +
                "\"Currency\":\"980\",\n" +
                "\"PurchaseTime\":\"" + df.format(date) + "\",\n" +
                "\"PurchaseDesc\":\"token json\"" +
                //"\"Recurrent\":\"301076377260037\""+
                "\n}";

        System.out.println("-- PAYMENT --\n\n" +
                "Payload REQUEST\n" + payload + "\n");

        String payloadBase64 = Base64.getEncoder().encodeToString(payload.getBytes());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("header", headerBase64);
        jsonObject.put("payload", payloadBase64);
        jsonObject.put("signature", signatureBase64);

        Response response = given().accept(ContentType.JSON).contentType(ContentType.JSON).baseUri(URLjsonToken).body(jsonObject).when().post();

        String payloadRes = response.jsonPath().getString("payload");
        String headerRes = response.jsonPath().getString("header");
        String signatureRes = response.jsonPath().getString("signature");

        byte[] payloadBase64DecodedBytes = Base64.getDecoder().decode(payloadRes);
        String payloadDecoded = new String(payloadBase64DecodedBytes);
        System.out.println("Payload RESPONSE\n" + payloadDecoded + "\n");

        JSONAssert.assertEquals("{\"TranCode\":\"000\"}", payloadDecoded, false);
    }

    @AfterClass
    public void defaultParam(){
        setMerchantAtt(id_AVAL, ALLOW_SUPPORT_TOKEN, "false");
        System.out.println("ALLOW_SUPPORT_TOKEN: FALSE");
    }
}