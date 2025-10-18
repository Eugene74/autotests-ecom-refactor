package tests.mdesVts;

import methods.MdesVtsRequests;
import org.apache.http.HttpResponse;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import tests.BaseAPITest;
import tests.BaseTest;

import java.util.HashMap;
import java.util.Map;

import static jdbc.JDBCMethods.setMerchantAtt;
import static org.testng.Assert.*;
import static type.Attributes.*;

public class MdesVtsJsonApi extends BaseAPITest {

    private static final Map<String, String> cardMap = new HashMap<>();
    static {
        cardMap.put("MASTERCARD", cardMdes);
        cardMap.put("VISA", cardVts);
    }


    @BeforeClass
    public void setParam() {
        setMerchantAtt(id_AVAL5, ALLOW_SUPPORT_TOKEN, "true");
        setMerchantAtt(id_AVAL5, ALLOW_TOKEN_MDES, "true");
        setMerchantAtt(id_AVAL5, ALLOW_TOKEN_VTS, "true");
    }

    @DataProvider(name = "cardProvider")
    public Object[][] cardProvider() {
        return new Object[][] {
                { "MASTERCARD" },
                { "VISA" }
        };
    }
    private Map<String, String> tokenIdMap = new HashMap<>();
    private Map<String, String> tavyMap = new HashMap<>();
    private Map<String, String>tokenNumberMap = new HashMap<>();
    private Map<String, String> contentIDMap = new HashMap<>();
    private String header = "eyJhbGciOiJSUzI1NiJ9";
    private String signature = "EXDEhK9kMK0lwTEWH4mm1oJvKm5vVFyXnyDnqEDHDc3mYyXEhLv3Ih6_fdmN-apUPxgV5G" +
            "EpV0YQWTuSyGF3o32dF0n-A4LrZ93z8Dw7gj9ULLd5ffRE42x0tFL6jNNEnVUbj8WB1UeR6mRN4l4aTRaNU123hq6UIqB_jsTxWJU";


    @Test(dataProvider = "cardProvider")
    public void testCreateToken(String cardName) throws Exception {
        String card = cardMap.get(cardName);
        String trackingId = Long.toString(System.currentTimeMillis());
        if (trackingId.length() > 10) {
            trackingId = trackingId.substring(trackingId.length() - 10);
        }
        MdesVtsRequests requests = new MdesVtsRequests();
        String jsonData = requests.prepareCreateTokenRequest(
                JSON_TEMPLATES_CREATE_TOKEN_MDES_VTS,
                trackingId,
                card,
                merchantID_aval5,
                terminalID_aval5
        );
        HttpResponse response = sendPostRequest(URLMdesVtsCreateToken, jsonData, header, signature);
        JSONObject payLoadJson = requests.getPayloadFromJSONResponse(response);
        String tokenID = payLoadJson.getString("UPCToken");
        tokenIdMap.put(card, tokenID);
        assertNotNull(tokenID);
        System.out.println("Token ID: " + tokenID);

        JSONObject extDataToken = payLoadJson.getJSONObject("extDataToken");
        String tavy = extDataToken.getString("TAVV");
        tavyMap.put(card, tavy);
        assertNotNull(tavy);
        System.out.println("TAVY: " + tavy);

        String tokenNumber = extDataToken.getString("TokenNumber");
        tavyMap.put(card, tokenNumber);
        assertNotNull(tokenNumber);
        System.out.println("Token Number: " + maskMiddle(tokenNumber, 6));

        String tokenExpDate = extDataToken.getString("TokenExpDate");
        assertNotNull(tokenExpDate);
        System.out.println("EXP Date: " + tokenExpDate);
    }

    @Test(dataProvider = "cardProvider", dependsOnMethods = "testCreateToken", priority = 1)
    public void testRefreshToken(String cardName) throws Exception {
        String card = cardMap.get(cardName);
        String tokenID = tokenIdMap.get(card);
        String tavy = tavyMap.get(card);
        String trackingId = Long.toString(System.currentTimeMillis());
        if (trackingId.length() > 10) {
            trackingId = trackingId.substring(trackingId.length() - 10);
        }
        MdesVtsRequests requests = new MdesVtsRequests();
        String jsonData = requests.prepareTokenActionRequest(
                BaseTest.JSON_TEMPLATES_REFRESH_TOKEN_MDES_VTS,
                trackingId,
                tokenID,
                merchantID_aval5,
                terminalID_aval5
        );

        HttpResponse response = sendPostRequest(URLMdesVtsRefreshToken, jsonData, header, signature);
        JSONObject payLoadJson = requests.getPayloadFromJSONResponse(response);
        String new_tokenID =  payLoadJson.getString("UPCToken");
        assertEquals(new_tokenID, tokenID, "Actual tokenID" + new_tokenID + " is not equal to expected " + tokenID);

        JSONObject extDataToken = payLoadJson.getJSONObject("extDataToken");
        String new_tavy =    extDataToken.getString("TAVV");
        assertNotNull(new_tavy);
        Assert.assertNotEquals(new_tavy, tavy, "New TAVY" + new_tavy + " is equal to old one");
        System.out.println("New TAVY: " + new_tavy);
        tavyMap.put(card, new_tavy);

        String newTokenNumber =  extDataToken.getString("TokenNumber");
        assertNotNull(newTokenNumber);
        System.out.println("Token Number: " + maskMiddle(newTokenNumber, 6));
        tokenNumberMap.put(card, newTokenNumber);

        String tokenExpDate = extDataToken.getString("TokenExpDate");
        assertNotNull(tokenExpDate);
        System.out.println("EXP date: " + tokenExpDate);
    }

    @Test(dataProvider = "cardProvider", dependsOnMethods = "testCreateToken", priority = 1)
    public void testCardMetaData(String cardName) throws Exception {
        String card = cardMap.get(cardName);
        String tokenID = tokenIdMap.get(card);
        String trackingId = Long.toString(System.currentTimeMillis());
        if (trackingId.length() > 10) {
            trackingId = trackingId.substring(trackingId.length() - 10);
        }
        MdesVtsRequests requests = new MdesVtsRequests();
        String jsonData = requests.prepareCardMetadataRequest(
                JSON_TEMPLATES_CARD_METADATA_MDES_VTS,
                trackingId,
                tokenID,
                merchantID_aval5,
                terminalID_aval5
        );

        HttpResponse response = sendPostRequest(URLMdesVtsCardMetadata, jsonData, header, signature);
        JSONObject payLoadJson = requests.getPayloadFromJSONResponse(response);

        String externalTokenID = payLoadJson.getString("fullTrackingId");
        assertNotNull(externalTokenID);
        System.out.println("External Token ID: " + externalTokenID);

        JSONObject metaData = payLoadJson.getJSONObject("metaData");
        JSONObject firstToken = metaData.getJSONArray("tokens").getJSONObject(0);
        String tokenStatus = firstToken.getString("tokenStatus");
        assertEquals(tokenStatus, "ACTIVE", "Token status is " + tokenStatus + " but expected ACTIVE");
        System.out.println("Token Status: " + tokenStatus);

        JSONObject cardMetaData = metaData.getJSONObject("cardMetaData");
        JSONObject firstContent = cardMetaData.getJSONArray("cardData").getJSONObject(0);
        String contentType = firstContent.getString("contentType");
        if (card == cardMdes) {
            assertEquals(contentType, "BrandLogoAsset", "Content type is " + contentType + " but expected BrandLogoAsset");
        } else if (card == cardVts) {
            assertEquals(contentType, "cardSymbol", "Content type is " + contentType + " but expected cardSymbol");
        }
        System.out.println("Content Type: " + contentType);

        String contentID = firstContent.getString("contentId");
        contentIDMap.put(card, contentID);
        assertNotNull(contentID);
        System.out.println("Content ID: " + contentID);
    }

    @Test(dataProvider = "cardProvider", dependsOnMethods = "testCardMetaData", priority = 1)
    public void testCardContent(String cardName) throws Exception {
        String card = cardMap.get(cardName);
        String contentID = contentIDMap.get(card);
        String trackingId = Long.toString(System.currentTimeMillis());
        String brand = card.equals(cardMdes) ? "MAST" : "VISA";
        if (trackingId.length() > 10) {
            trackingId = trackingId.substring(trackingId.length() - 10);
        }
        MdesVtsRequests requests = new MdesVtsRequests();
        String jsonData = requests.prepareCardContentRequest(
                JSON_TEMPLATES_CARD_CONTENT_MDES_VTS,
                trackingId,
                contentID,
                merchantID_aval5,
                terminalID_aval5,
                brand
        );

        HttpResponse response = sendPostRequest(URLMdesVtsCardContent, jsonData, header, signature);
        JSONObject payLoadJson = requests.getPayloadFromJSONResponse(response);

        String fullTrackingID = payLoadJson.getString("fullTrackingId");
        assertNotNull(fullTrackingID);

        JSONObject cardContent = payLoadJson.getJSONObject("cardContent");
        String new_contentID = cardContent.getString("contentId");
        assertNotNull(new_contentID);
        System.out.println("Content ID: " + new_contentID);

        JSONObject firstContent = cardContent.getJSONArray("content").getJSONObject(0);
        String encodedData = firstContent.getString("encodedData");
        assertNotNull(encodedData);
        assertTrue(encodedData.length() > 100,
                "Encoded data length is " + encodedData.length() + ", expected more than 100");
    }


    @Test(dataProvider = "cardProvider", dependsOnMethods = "testCreateToken", priority = 1)
    public void testTokenStatus(String cardName) throws Exception {
        String card = cardMap.get(cardName);
        String tokenID = tokenIdMap.get(card);
        String trackingId = Long.toString(System.currentTimeMillis());
        if (trackingId.length() > 10) {
            trackingId = trackingId.substring(trackingId.length() - 10);
        }
        MdesVtsRequests requests = new MdesVtsRequests();
        String jsonData = requests.prepareTokenActionRequest(
                JSON_TEMPLATES_TOKEN_STATUS_MDES_VTS,
                trackingId,
                tokenID,
                merchantID_aval5,
                terminalID_aval5
        );

        HttpResponse response =  sendPostRequest(URLMdesVtsTokenStatus, jsonData, header, signature);
        JSONObject payLoadJson = requests.getPayloadFromJSONResponse(response);

        String code = payLoadJson.getString("TranCode");
        assertEquals(code, "000", "Code is " + code + " but expected 000");
        System.out.println("Code: " + code);

        String tokenStatus = payLoadJson.getString("status");
        assertEquals(tokenStatus, "ACTIVE", "Token status is " + tokenStatus + " but expected ACTIVE");
        System.out.println("Token Status: " + tokenStatus);

        String newTokenID = payLoadJson.getString("UPCToken");
        assertEquals(newTokenID, tokenID, "Actual tokenID" + newTokenID + " is not equal to expected " + tokenID);
        System.out.println("Token ID: " + newTokenID);
    }

    @Test(dataProvider = "cardProvider", dependsOnMethods = "testCreateToken", priority = 2)
    public void testTokenSuspend(String cardName) throws Exception {
        String card = cardMap.get(cardName);
        String tokenID = tokenIdMap.get(card);
        String trackingId = Long.toString(System.currentTimeMillis());
        if (trackingId.length() > 10) {
            trackingId = trackingId.substring(trackingId.length() - 10);
        }
        MdesVtsRequests requests = new MdesVtsRequests();
        String jsonData = requests.prepareTokenActionRequest(
                JSON_TEMPLATES_TOKEN_SUSPEND_MDES_VTS,
                trackingId,
                tokenID,
                merchantID_aval5,
                terminalID_aval5
        );

        HttpResponse response =  sendPostRequest(URLMdesVtsTokenSuspend, jsonData, header, signature);
        JSONObject payLoadJson = requests.getPayloadFromJSONResponse(response);

        String code = payLoadJson.getString("TranCode");
        assertEquals(code, "000", "Code is " + code + " but expected 000");
        System.out.println("Code: " + code);

        String tokenStatus =  payLoadJson.getString("status");
        assertEquals(tokenStatus, "SUSPENDED", "Token status is " + tokenStatus + " but expected SUSPENDED");
        System.out.println("Token Status: " + tokenStatus);

        String new_tokenID = payLoadJson.getString("UPCToken");
        assertEquals(new_tokenID, tokenID, "Actual tokenID" + new_tokenID + " is not equal to expected " + tokenID);
        System.out.println("Token ID: " + new_tokenID);
    }

    @Test(dataProvider = "cardProvider", dependsOnMethods = "testTokenSuspend")
    public void testTokenResume(String cardName) throws Exception {
        String card = cardMap.get(cardName);
        String tokenID = tokenIdMap.get(card);
        String trackingId = Long.toString(System.currentTimeMillis());
        if (trackingId.length() > 10) {
            trackingId = trackingId.substring(trackingId.length() - 10);
        }
        MdesVtsRequests requests = new MdesVtsRequests();
        String jsonData = requests.prepareTokenActionRequest(
                JSON_TEMPLATES_TOKEN_RESUME_MDES_VTS,
                trackingId,
                tokenID,
                merchantID_aval5,
                terminalID_aval5
        );

        HttpResponse response =  sendPostRequest(URLMdesVtsTokenResume, jsonData, header, signature);
        JSONObject payLoadJson = requests.getPayloadFromJSONResponse(response);

        String code = payLoadJson.getString("TranCode");
        assertEquals(code, "000", "Code is " + code + " but expected 000");
        System.out.println("Code: " + code);

        String tokenStatus = payLoadJson.getString("status");
        assertEquals(tokenStatus, "ACTIVE", "Token status is " + tokenStatus + " but expected ACTIVE");
        System.out.println("Token Status: " + tokenStatus);

        String new_tokenID = payLoadJson.getString("UPCToken");
        assertEquals(new_tokenID, tokenID, "Actual tokenID" + new_tokenID + " is not equal to expected " + tokenID);
        System.out.println("Token ID: " + new_tokenID);
    }

    @Test(dataProvider = "cardProvider", dependsOnMethods = "testCreateToken", priority = 3)
    public void testTokenDelete(String cardName) throws Exception {
        String card = cardMap.get(cardName);
        String tokenID = tokenIdMap.get(card);
        String trackingId = Long.toString(System.currentTimeMillis());
        if (trackingId.length() > 10) {
            trackingId = trackingId.substring(trackingId.length() - 10);
        }
        MdesVtsRequests requests = new MdesVtsRequests();
        String jsonData = requests.prepareTokenActionRequest(
                JSON_TEMPLATES_TOKEN_DELETE_MDES_VTS,
                trackingId,
                tokenID,
                merchantID_aval5,
                terminalID_aval5
        );

        HttpResponse response =  sendPostRequest(URLMdesVtsTokenDelete, jsonData, header, signature);
        JSONObject payLoadJson = requests.getPayloadFromJSONResponse(response);

        String code = payLoadJson.getString("TranCode");
        assertEquals(code, "000", "Code is " + code + " but expected 000");
        System.out.println("Code: " + code);

        String tokenStatus = payLoadJson.getString("status");
        assertEquals(tokenStatus, "DEACTIVATED", "Token status is " + tokenStatus + " but expected DEACTIVATED");
        System.out.println("Token Status: " + tokenStatus);

        String new_tokenID = payLoadJson.getString("UPCToken");
        assertEquals(new_tokenID, tokenID, "Actual tokenID" + new_tokenID + " is not equal to expected " + tokenID);
        System.out.println("Token ID: " + new_tokenID);
    }
    @AfterClass
    public void setDefaultParam() {
        setMerchantAtt(id_AVAL5, ALLOW_SUPPORT_TOKEN, "true");
        setMerchantAtt(id_AVAL5, ALLOW_TOKEN_MDES, "true");
        setMerchantAtt(id_AVAL5, ALLOW_TOKEN_VTS, "true");
    }
}