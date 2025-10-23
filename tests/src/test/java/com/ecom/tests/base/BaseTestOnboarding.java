package com.ecom.tests.base;

import java.io.IOException;
import java.net.URI;
import java.util.Random;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class BaseTestOnboarding
    extends BaseApiTest { // todo потом переделать(или убрать) это промежуточный BaseTestOnboarding?
  protected static String baseUrl;
  protected static final String XML_ONBOARD_MERCHANT_RESOURCE =
      "/XMLOnbording/ExternalOnboarding.xml";
  protected static final String XML_GET_STATUS_ONBOARD = "/XMLOnbording/GetStatusOnbording.xml";

  /*@BeforeClass
  @Override
  public void setup() {
      super.setup();
      baseUrl = properties.getProperty("URLtomee");
  }    */

  protected class HttpGetWithEntity extends HttpEntityEnclosingRequestBase {
    public static final String METHOD_NAME = "GET";

    @Override
    public String getMethod() {
      return METHOD_NAME;
    }

    public HttpGetWithEntity(final String uri) {
      super();
      setURI(URI.create(uri));
    }

    public HttpGetWithEntity(final URI uri) {
      super();
      setURI(uri);
    }

    public HttpGetWithEntity() {
      super();
    }
  }

  protected HttpResponse sendGetRequestWithEntity(String url, String xmlContent)
      throws IOException {
    CloseableHttpClient client = HttpClients.createDefault();
    HttpGetWithEntity get = new HttpGetWithEntity(url);
    get.setHeader("Content-Type", "application/xml");
    get.setHeader("Accept", "application/xml");

    // Додаємо XML в тіло запиту
    StringEntity entity = new StringEntity(xmlContent, "UTF-8");
    get.setEntity(entity);

    return client.execute(get);
  }

  protected String generateRandomDigits(int length) {
    Random random = new Random();
    StringBuilder digits = new StringBuilder(length);
    for (int i = 0; i < length; i++) {
      digits.append(random.nextInt(10));
    }
    return digits.toString();
  }
}
