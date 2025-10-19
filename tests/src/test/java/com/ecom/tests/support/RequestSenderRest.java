package com.ecom.tests.support;

import io.restassured.RestAssured;
import io.restassured.config.SSLConfig;
import org.w3c.dom.Document;

public class RequestSenderRest {

    static {
        // разрешаем любые SSL для тестовых стендов
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.config = RestAssured.config()
                .sslConfig(new SSLConfig().relaxedHTTPSValidation());
    }

    private final String url;

    public RequestSenderRest(String url) {
        this.url = url;
    }

    public static Document sendRequest(String url, Document request){
        RequestSenderRest httpRequestSender = new RequestSenderRest(url);
        String receiveResponse = httpRequestSender.post(DocumentTools.convertXMLDocumentToString(request));
        return DocumentTools.convertStringToXmlDocument(receiveResponse);
    }

    private String post(String xmlBody) {
        return RestAssured.given()
                .relaxedHTTPSValidation()
                .header("Connection", "Keep-Alive")
                .header("Cache-Control", "no-cache")
                .header("User-Agent", "Jakarta Commons-HttpClient/3.1")
                .contentType("application/xml; charset=UTF-8")
                .accept("application/xml, text/html, */*;q=0.8")
                .body(xmlBody)
                .when()
                .post(url)
                .then()
                .extract()
                .asString();
    }
}
