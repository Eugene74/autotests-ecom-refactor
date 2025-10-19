package tests.mdesVts;

import com.ecom.tests.support.MdesVtsRequests;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import tests.BaseAPITest;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


import static com.ecom.db.JDBCMethods.setMerchantAtt;
import static org.testng.Assert.*;
import static com.ecom.api.type.Attributes.*;

public class MdesVtsXMLApi extends BaseAPITest {

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


    @Test(dataProvider = "cardProvider")
    public void testCreateToken(String cardName) throws Exception {
        String card = cardMap.get(cardName);
        String trackingId = Long.toString(System.currentTimeMillis());
        if (trackingId.length() > 10) {
            trackingId = trackingId.substring(trackingId.length() - 10);
        }
        MdesVtsRequests requests = new MdesVtsRequests();
        String xmlData = requests.prepareCreateTokenRequest(
                XML_TEMPLATES_CREATE_TOKEN_MDES_VTS,
                trackingId,
                card,
                merchantID_aval5,
                terminalID_aval5
        );
        HttpResponse response = sendPostRequest(URLMdesVtsCreateToken, xmlData);
        String responseXml = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        String tokenID = requests.getTokenID(responseXml);
        tokenIdMap.put(card, tokenID);
        assertNotNull(tokenID);
        System.out.println("Token ID: " + tokenID);

        String tavy = requests.getTAVV(responseXml);
        tavyMap.put(card, tavy);
        assertNotNull(tavy);
        System.out.println("TAVY: " + tavy);

        String tokenNumber = requests.getTokenNumber(responseXml);
        tavyMap.put(card, tokenNumber);
        assertNotNull(tokenNumber);
        System.out.println("Token Number: " + maskMiddle(tokenNumber, 6));

        String tokenExpDate = requests.getTokenExpiryDate(responseXml);
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
        String xmlData = requests.prepareTokenActionRequest(
                XML_TEMPLATES_REFRESH_TOKEN_MDES_VTS,
                trackingId,
                tokenID,
                merchantID_aval5,
                terminalID_aval5
        );

        HttpResponse response = sendPostRequest(URLMdesVtsRefreshToken, xmlData);
        String responseXml = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        String new_tokenID = requests.getTokenID(responseXml);
        assertEquals(new_tokenID, tokenID, "Actual tokenID" + new_tokenID + " is not equal to expected " + tokenID);

        String new_tavy = requests.getTAVV(responseXml);
        assertNotNull(new_tavy);
        Assert.assertNotEquals(new_tavy, tavy, "New TAVY" + new_tavy + " is equal to old one");
        System.out.println("New TAVY: " + new_tavy);
        tavyMap.put(card, new_tavy);

        String newTokenNumber = requests.getTokenNumber(responseXml);
        assertNotNull(newTokenNumber);
        System.out.println("Token Number: " + maskMiddle(newTokenNumber, 6));
        tokenNumberMap.put(card, newTokenNumber);

        String tokenExpDate = requests.getTokenExpiryDate(responseXml);
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
        String xmlData = requests.prepareCardMetadataRequest(
                XML_TEMPLATES_CARD_METADATA_MDES_VTS,
                trackingId,
                tokenID,
                merchantID_aval5,
                terminalID_aval5
        );

        HttpResponse response = sendPostRequest(URLMdesVtsCardMetadata, xmlData);
        String responseXml = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        String externalTokenID = requests.getExternalTokenID(responseXml);
        assertNotNull(externalTokenID);
        System.out.println("External Token ID: " + externalTokenID);

        String tokenStatus = requests.getTokenStatusMeta(responseXml);
        assertEquals(tokenStatus, "ACTIVE", "Token status is " + tokenStatus + " but expected ACTIVE");
        System.out.println("Token Status: " + tokenStatus);

        String contentType = requests.getContentType(responseXml);
        if (card == cardMdes) {
            assertEquals(contentType, "BrandLogoAsset", "Content type is " + contentType + " but expected BrandLogoAsset");
        } else if (card == cardVts) {
            assertEquals(contentType, "cardSymbol", "Content type is " + contentType + " but expected cardSymbol");
        }
        System.out.println("Content Type: " + contentType);

        String contentID = requests.getContentID(responseXml);
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
        String xmlData = requests.prepareCardContentRequest(
                XML_TEMPLATES_CARD_CONTENT_MDES_VTS,
                trackingId,
                contentID,
                merchantID_aval5,
                terminalID_aval5,
                brand
        );

        HttpResponse response = sendPostRequest(URLMdesVtsCardContent, xmlData);
        String responseXml = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        String fullTrackingID = requests.getFullTrackingID(responseXml);
        assertNotNull(fullTrackingID);

        String new_contentID = requests.getContentID(responseXml);
        assertNotNull(new_contentID);
        System.out.println("Content ID: " + new_contentID);

        String encodedData = requests.getEncodedData(responseXml);
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
        String xmlData = requests.prepareTokenActionRequest(
                XML_TEMPLATES_TOKEN_STATUS_MDES_VTS,
                trackingId,
                tokenID,
                merchantID_aval5,
                terminalID_aval5
        );

        HttpResponse response = sendPostRequest(URLMdesVtsTokenStatus, xmlData);
        String responseXml = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        String code = requests.getCode(responseXml);
        assertEquals(code, "000", "Code is " + code + " but expected 000");
        System.out.println("Code: " + code);

        String tokenStatus = requests.getTokenStatus(responseXml);
        assertEquals(tokenStatus, "ACTIVE", "Token status is " + tokenStatus + " but expected ACTIVE");
        System.out.println("Token Status: " + tokenStatus);

        String newTokenID = requests.getTokenID(responseXml);
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
        String xmlData = requests.prepareTokenActionRequest(
                XML_TEMPLATES_TOKEN_SUSPEND_MDES_VTS,
                trackingId,
                tokenID,
                merchantID_aval5,
                terminalID_aval5
        );

        HttpResponse response = sendPostRequest(URLMdesVtsTokenSuspend, xmlData);
        String responseXml = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        String code = requests.getCode(responseXml);
        assertEquals(code, "000", "Code is " + code + " but expected 000");
        System.out.println("Code: " + code);

        String tokenStatus = requests.getTokenStatus(responseXml);
        assertEquals(tokenStatus, "SUSPENDED", "Token status is " + tokenStatus + " but expected SUSPENDED");
        System.out.println("Token Status: " + tokenStatus);

        String new_tokenID = requests.getTokenID(responseXml);
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
        String xmlData = requests.prepareTokenActionRequest(
                XML_TEMPLATES_TOKEN_RESUME_MDES_VTS,
                trackingId,
                tokenID,
                merchantID_aval5,
                terminalID_aval5
        );

        HttpResponse response = sendPostRequest(URLMdesVtsTokenResume, xmlData);
        String responseXml = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        String code = requests.getCode(responseXml);
        assertEquals(code, "000", "Code is " + code + " but expected 000");
        System.out.println("Code: " + code);

        String tokenStatus = requests.getTokenStatus(responseXml);
        assertEquals(tokenStatus, "ACTIVE", "Token status is " + tokenStatus + " but expected ACTIVE");
        System.out.println("Token Status: " + tokenStatus);

        String new_tokenID = requests.getTokenID(responseXml);
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
        String xmlData = requests.prepareTokenActionRequest(
                XML_TEMPLATES_TOKEN_DELETE_MDES_VTS,
                trackingId,
                tokenID,
                merchantID_aval5,
                terminalID_aval5
        );

        HttpResponse response = sendPostRequest(URLMdesVtsTokenDelete, xmlData);
        String responseXml = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

        String code = requests.getCode(responseXml);
        assertEquals(code, "000", "Code is " + code + " but expected 000");
        System.out.println("Code: " + code);

        String tokenStatus = requests.getTokenStatus(responseXml);
        assertEquals(tokenStatus, "DEACTIVATED", "Token status is " + tokenStatus + " but expected DEACTIVATED");
        System.out.println("Token Status: " + tokenStatus);

        String new_tokenID = requests.getTokenID(responseXml);
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