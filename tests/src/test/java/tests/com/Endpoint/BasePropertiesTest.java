package tests.com.Endpoint;

import com.ecom.core.config.PropertiesManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.testng.annotations.BeforeClass;

import javax.net.ssl.SSLContext;
import java.util.Properties;

public class BasePropertiesTest {
    protected Properties properties;
    protected String baseUrl;
    protected String TerminalID;
    //protected String Terminal2ID;
    protected String MerchantID;
   // protected String Merchant2ID;
    protected String RequestID;

    protected String Terminal2ID;  // Додано оголошення змінних Terminal2ID та Merchant2ID
    protected String Merchant2ID;  // Додано
    @BeforeClass
    public void setup() {
        properties = new Properties();
        properties.putAll(PropertiesManager.getInstance().getEnvProperties());

        // Отримуємо значення з properties
        baseUrl = properties.getProperty("URLtomcat");
        TerminalID = properties.getProperty("TerminalID_AVAL1");
        MerchantID = properties.getProperty("MerchantID_AVAL1");
        Terminal2ID = properties.getProperty("TerminalID_AVAL4");
        Merchant2ID = properties.getProperty("MerchantID_AVAL4");
        RequestID = properties.getProperty("RequestID");

        // Зберігаємо значення merchantId з properties
    }

    public CloseableHttpClient createAllTrustingClient() throws Exception {
        SSLContext sslContext = SSLContextBuilder.create()
                .loadTrustMaterial((chain, authType) -> true)
                .build();
        return HttpClients.custom()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();
    }

    protected HttpResponse sendPostRequest(String url, String xmlContent) throws Exception {
        HttpPost post = new HttpPost(url);
        post.setEntity(new StringEntity(xmlContent));
        post.setHeader("Content-Type", "application/xml");

        CloseableHttpClient client = createAllTrustingClient();
        return client.execute(post);
    }
}


