package tests;


import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.io.ByteArrayInputStream;
import java.util.Base64;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;


public class BaseAPITest extends BaseTest{

    public CloseableHttpClient createAllTrustingClient() throws Exception {
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial((chain, authType) -> true)
                .build();
        return HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
    }

    private String readInputStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }

    protected String getResourceContent(String resourcePath) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return readInputStreamToString(is);
        }
    }

    protected HttpResponse sendPostRequest(String url, String xmlContent) throws Exception {
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(xmlContent));
        post.setHeader("Content-Type", "application/xml");

        CloseableHttpClient client = createAllTrustingClient();
        return client.execute(post);
    }

    protected HttpResponse sendPostRequest(
            String url,
            String json,
            String header,
            String signature
    ) throws Exception {
        // Encode json to Base64
        String payload = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));

        // Build JSON object
        JSONObject requestBody = new JSONObject();
        requestBody.put("header", header);
        requestBody.put("signature", signature);
        requestBody.put("payload", payload);

        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(requestBody.toString()));
        post.setHeader("Content-Type", "application/json");
        CloseableHttpClient client = createAllTrustingClient();
        return client.execute(post);
    }

    protected String getTagValue(String xml, String tagName) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        Document doc = builder.parse(is);
        NodeList nodes = doc.getElementsByTagName(tagName);
        if (nodes.getLength() > 0) {
            return nodes.item(0).getTextContent();
        } else {
            return null;
        }
    }

    /**
     * Masks the middle part of a string, replacing `maskLength` characters with '*'.
     * @param input The input string.
     * @param maskLength Number of characters to mask in the middle.
     * @return Masked string.
     */
    public String maskMiddle(String input, int maskLength) {
        if (input == null || maskLength <= 0 || input.length() <= maskLength) {
            return input;
        }
        int start = (input.length() - maskLength) / 2;
        StringBuilder sb = new StringBuilder();
        sb.append(input.substring(0, start));
        for (int i = 0; i < maskLength; i++) {
            sb.append('*');
        }
        sb.append(input.substring(start + maskLength));
        return sb.toString();
    }

    /**
     * Masks the middle part of an integer, replacing `maskLength` digits with '*'.
     * @param input The input integer.
     * @param maskLength Number of digits to mask in the middle.
     * @return Masked string.
     */
    public String maskMiddle(int input, int maskLength) {
        return maskMiddle(String.valueOf(input), maskLength);
    }

   }