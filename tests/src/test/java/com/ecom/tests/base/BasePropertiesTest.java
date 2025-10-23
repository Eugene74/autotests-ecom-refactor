package com.ecom.tests.base;

@Deprecated
public class BasePropertiesTest {
   /* protected Properties properties;
    protected String baseUrl;
    protected String TerminalID;
    //protected String Terminal2ID;
    protected String MerchantID;
   // protected String Merchant2ID;
    protected String RequestID;

    protected String Terminal2ID;  // Додано оголошення змінних Terminal2ID та Merchant2ID
    protected String Merchant2ID;  // Додано

    @BeforeClass
    public void setup() {*/
        //properties = new Properties();
        //properties.putAll(PropertiesManager.getInstance().getEnvProperties());

        // Отримуємо значення з properties

        // Зберігаємо значення merchantId з properties
  //  }
/*
    public CloseableHttpClient createAllTrustingClient() throws Exception {
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial((chain, authType) -> true)
                .build();
        return HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
    }*/

   /* protected HttpResponse sendPostRequest(String url, String xmlContent) throws Exception {
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(xmlContent));
        post.setHeader("Content-Type", "application/xml");

        CloseableHttpClient client = createAllTrustingClient();
        return client.execute(post);
    }*/
}


