package tests.com.Onbording;

import tests.com.Endpoint.BasePropertiesTest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.testng.annotations.BeforeClass;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class BaseTestOnboarding extends BasePropertiesTest {
    protected static String baseUrl;
    protected static final String XML_ONBOARD_MERCHANT_RESOURCE = "/XMLOnbording/ExternalOnboarding.xml";
    protected static final String XML_GET_STATUS_ONBOARD = "/XMLOnbording/GetStatusOnbording.xml";

    @BeforeClass
    @Override
    public void setup() throws IOException {
        super.setup();
        baseUrl = properties.getProperty("URLtomee");
    }


    protected HttpResponse sendGetRequest(String url) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url);
        get.setHeader("Content-Type", "application/xml");
        return client.execute(get);
    }

    protected String getResourceContent(String resourcePath) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return readInputStreamToString(is);
        }
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
}
