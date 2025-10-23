package com.ecom.tests.base;

import com.ecom.db.JDBCConnection;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;
import javax.net.ssl.SSLContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/** Common HTTP helpers used by API tests. */
public abstract class BaseApiTest extends BaseTest { // todo переделать то GotClass

  protected CloseableHttpClient createAllTrustingClient() throws Exception {
    SSLContext sslContext =
        SSLContextBuilder.create().loadTrustMaterial((chain, authType) -> true).build();
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
    return result.toString(StandardCharsets.UTF_8);
  }

  protected String getResourceContent(String resourcePath) throws IOException {
    try (InputStream stream = getClass().getResourceAsStream(resourcePath)) {
      if (stream == null) {
        throw new IOException("Resource not found: " + resourcePath);
      }
      return readInputStreamToString(stream);
    }
  }

  protected HttpResponse sendPostRequest(String url, String xmlContent) throws Exception {
    HttpPost post = new HttpPost(url);
    post.setEntity(new StringEntity(xmlContent));
    post.setHeader("Content-Type", "application/xml");
    return createAllTrustingClient().execute(post);
  }

  protected HttpResponse sendPostRequest(String url, String json, String header, String signature)
      throws Exception {
    String payload = Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));

    JSONObject requestBody = new JSONObject();
    requestBody.put("header", header);
    requestBody.put("signature", signature);
    requestBody.put("payload", payload);

    HttpPost post = new HttpPost(url);
    post.setEntity(new StringEntity(requestBody.toString()));
    post.setHeader("Content-Type", "application/json");
    return createAllTrustingClient().execute(post);
  }

  protected String getTagValue(String xml, String tagName) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    try (InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
      Document document = builder.parse(stream);
      NodeList nodes = document.getElementsByTagName(tagName);
      return nodes.getLength() > 0 ? nodes.item(0).getTextContent() : null;
    }
  }

  public String maskMiddle(String input, int maskLength) {
    if (input == null || maskLength <= 0 || input.length() <= maskLength) {
      return input;
    }
    int start = (input.length() - maskLength) / 2;
    StringBuilder builder = new StringBuilder();
    builder.append(input, 0, start);
    for (int index = 0; index < maskLength; index++) {
      builder.append('*');
    }
    builder.append(input.substring(start + maskLength));
    return builder.toString();
  }

  public String maskMiddle(int input, int maskLength) {
    return maskMiddle(String.valueOf(input), maskLength);
  }

  protected HttpResponse sendGetRequest(String url, String queryParams) throws IOException {
    CloseableHttpClient client = HttpClients.createDefault();
    HttpPost post = new HttpPost(url + "?" + queryParams);
    return client.execute(post);
  }

  protected HttpResponse sendGetRequest(String url) throws IOException {
    CloseableHttpClient client = HttpClients.createDefault();
    HttpGet get = new HttpGet(url);
    get.setHeader("Content-Type", "application/xml");
    return client.execute(get);
  }

  protected HttpResponse sendDeleteRequest(String url, String xmlContent) throws IOException {
    CloseableHttpClient client = HttpClients.createDefault();
    HttpDeleteWithBody delete = new HttpDeleteWithBody(url);
    delete.setHeader("Content-Type", "application/xml");
    delete.setEntity(new StringEntity(xmlContent));
    return client.execute(delete);
  }

  public class HttpDeleteWithBody extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "DELETE";

    public HttpDeleteWithBody() {
      super();
    }

    public HttpDeleteWithBody(final URI uri) {
      super();
      setURI(uri);
    }

    public HttpDeleteWithBody(final String uri) {
      super();
      setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
      return METHOD_NAME;
    }
  }

  protected Connection getDBConnection() throws SQLException {
    return JDBCConnection.getDBConnection();
  }

  protected HttpResponse sendPutRequest(String url, String xmlContent) throws Exception {
    CloseableHttpClient client = createAllTrustingClient();
    HttpPut put = new HttpPut(url);
    put.setHeader("Content-Type", "application/xml");
    put.setEntity(new StringEntity(xmlContent));
    return client.execute(put);
  }
}
