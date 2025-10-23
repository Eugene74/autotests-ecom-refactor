package com.ecom.tests.support;

import com.ecom.tests.base.BaseApiTest;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


public class MdesVtsRequests extends BaseApiTest {
    public String prepareRequest(
            String xmlTemplatePath,
            String trackingId,
            String tokenId,
            String cardNumber,
            String contentId,
            String merchantId,
            String terminalId,
            String brand
    ) throws IOException {
        String xmlContent = getResourceContent(xmlTemplatePath);

        // Підставляння змінних у XML-запит
        Map<String, String> params = new HashMap<>();
        params.put("merchantId", merchantId);
        params.put("terminalId", terminalId);
        if(trackingId != null) {
            params.put("trackingId", trackingId);
        }
        if(tokenId != null) {
            params.put("tokenId", tokenId);
        }
        if(cardNumber != null) {
            params.put("cardNumber", cardNumber);
        }
        if(contentId != null) {
            params.put("contentId", contentId);
        }
        if(brand != null) {
            params.put("brand", brand);
        }


        for (Map.Entry<String, String> entry : params.entrySet()) {
            xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return xmlContent;
    }

    public String prepareCreateTokenRequest(
            String xmlTemplatePath,
            String trackingId,
            String cardNumber,
            String merchantId,
            String terminalId) throws IOException {
        try {
            return prepareRequest(xmlTemplatePath, trackingId, null, cardNumber, null, merchantId, terminalId, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String prepareTokenActionRequest(
            String xmlTemplatePath,
            String trackingId,
            String tokenID,
            String merchantId,
            String terminalId
    ) throws IOException {
        try {
            return prepareRequest(xmlTemplatePath, trackingId, tokenID, null, null, merchantId, terminalId, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String prepareCardMetadataRequest(
            String xmlTemplatePath,
            String trackingId,
            String tokenID,
            String merchantId,
            String terminalId
    ) throws IOException {
        try {
            return prepareRequest(xmlTemplatePath, trackingId, tokenID, null, null, merchantId, terminalId, null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String prepareCardContentRequest(
            String xmlTemplatePath,
            String trackingId,
            String contentId,
            String merchantId,
            String terminalId,
            String brand
    ) throws IOException {
        try {
            return prepareRequest(xmlTemplatePath, trackingId, null, null, contentId, merchantId, terminalId, brand);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject getPayloadFromJSONResponse(HttpResponse response)  throws Exception {
        String stringResponse = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
        JSONObject responseJson = new JSONObject(stringResponse);
        String payloadBase64 = responseJson.getString("payload");
        byte[] decodedBytes = java.util.Base64.getDecoder().decode(payloadBase64);
        String decodedPayload = new String(decodedBytes);
        return new JSONObject(decodedPayload);
    }


    public String getTokenID(String XMLResponse) throws Exception {
        return getTagValue(XMLResponse, "tokenId");
    }

    public String getTAVV(String XMLResponse) throws Exception {
        return getTagValue(XMLResponse, "TAVV");
    }

    public String getTokenNumber(String XMLResponse) throws Exception {
        return getTagValue(XMLResponse, "TokenNumber");
    }
    public String getTokenExpiryDate(String XMLResponse) throws Exception {
        return getTagValue(XMLResponse, "TokenExpDate");
    }

    public String getExternalTokenID(String XMLResponse) throws Exception {
        return getTagValue(XMLResponse, "externalTokenId");
    }

    public String getTokenStatusMeta(String XMLResponse) throws Exception {
        return getTagValue(XMLResponse, "tokenStatus");
    }

    public String getContentType(String XMLResponse) throws Exception {
        return getTagValue(XMLResponse, "contentType");
    }

    public String getContentID(String XMLResponse) throws Exception {
        return getTagValue(XMLResponse, "contentId");
    }

    public String getFullTrackingID(String XMLResponse) throws Exception {
        return getTagValue(XMLResponse, "FullTrackingId");
    }

    public String getEncodedData(String XMLResponse) throws Exception {
        return getTagValue(XMLResponse, "encodedData");
    }

    public String getCode(String XMLResponse) throws Exception {
        return getTagValue(XMLResponse, "Code");
    }

    public String getTokenStatus (String XMLResponse) throws Exception {
        return getTagValue(XMLResponse, "TokenStatus");
    }
}
