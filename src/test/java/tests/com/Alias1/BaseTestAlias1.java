package tests.com.Alias1;

import com.ecom.db.JDBCConnection;
import tests.com.Endpoint.BasePropertiesTest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.testng.annotations.BeforeClass;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.net.URI;

public class BaseTestAlias1 extends BasePropertiesTest {
    protected static String URLAlias1Create;
    protected static String URLAlias1Update;
    protected static String URLAlias1Get;
    protected static String URLAlias1Resolve;
    protected static String URLAlias1Inguiry;
    protected static String URLAlias1Delete;
    protected static String URLAlias2Create;
    protected static String URLAlias2Get;
    protected static String URLAlias2GetAliasId;
    protected static String URLAlias2GetByExternalId;
    protected static String URLAlias2Inquiry;
    protected static String URLAlias2Resolve;
    protected static String URLAlias2Update;
    protected static String URLAlias2UpdateStatus;
    protected static String URLAlias2CreatePaymentCredential;
    protected static String URLAlias2GetPaymentCredential;
    protected static String URLAlias2GetAllPaymentCredentials;
    protected static String URLAlias2DeletePaymentCredential;
    protected static String URLAlias2Delete;  // Додано URL для запиту Delete
    protected static String alias1Id = Long.toString(System.currentTimeMillis());
    protected static String alias2Id = Long.toString(System.currentTimeMillis()+1).toString();
    protected static final String XML_CREATE_ALIAS1_RESOURCE = "/XMLAlias1/CreateAlias1.xml";
    protected static final String XML_UPDATE_ALIAS1_RESOURCE = "/XMLAlias1/UpdateAlias1.xml";
    protected static final String XML_GET_ALIAS1_RESOURCE = "/XMLAlias1/GetAlias1.xml";
    protected static final String XML_RESOLVE_ALIAS1_RESOURCE = "/XMLAlias1/ResolveAlias1.xml";
    protected static final String XML_INQUIRY_ALIAS1_RESOURCE = "/XMLAlias1/ResolveAlias1.xml";
    protected static final String XML_DELETE_ALIAS1_RESOURCE = "/XMLAlias1/DeleteAlias1.xml";
    protected static final String XML_CREATE_ALIAS2_RESOURCE = "/XMLAlias2/CreateAlias2.xml";
    protected static final String XML_GET_ALIAS2_RESOURCE = "/XMLAlias2/GetAlias2.xml";
    protected static final String XML_GET_ALIAS2ID_BY_VALUE_RESOURCE = "/XMLAlias2/GetAlias2IdByValue.xml";
    protected static final String XML_GET_ALIAS2_BY_EXTERNALID_RESOURCE = "/XMLAlias2/GetAlias2ByExternalId.xml";
    protected static final String XML_INQUIRY_ALIAS2_RESOURCE = "/XMLAlias2/InquiryAlias2.xml";
    protected static final String XML_RESOLVE_ALIAS2_RESOURCE = "/XMLAlias2/ResolveAlias2.xml";
    protected static final String XML_UPDATE_ALIAS2_RESOURCE = "/XMLAlias2/UpdateAlias2.xml";
    protected static final String XML_UPDATE_ALIAS2_STATUS_RESOURCE = "/XMLAlias2/UpdateAlias2Status.xml";
    protected static final String XML_CREATE_PAYMENT_CREDENTIAL_ALIAS2_RESOURCE = "/XMLAlias2/CreatePaymentCredentialAlias2.xml";
    protected static final String XML_GET_PAYMENT_CREDENTIAL_ALIAS2_RESOURCE = "/XMLAlias2/GetPaymentCredentialAlias2.xml";
    protected static final String XML_GET_ALL_PAYMENT_CREDENTIALS_ALIAS2_RESOURCE = "/XMLAlias2/GetAllPaymentCredentialsAlias2.xml";
    protected static final String XML_DELETE_PAYMENT_CREDENTIAL_ALIAS2_RESOURCE = "/XMLAlias2/DeletePaymentCredentialAlias2.xml";
    protected static final String XML_DELETE_ALIAS2_RESOURCE = "/XMLAlias2/DeleteAlias2.xml"; // Додано шлях для запиту DeleteAlias

    // Змінні для конфігурації бази даних
    protected static String DB_URL;
    protected static String DB_USER;
    protected static String DB_PASSWORD;

    @BeforeClass
    @Override
    public void setup() throws IOException {
        super.setup();
        URLAlias1Create = properties.getProperty("URLtomcat") + "/alias/create";
        URLAlias1Update = properties.getProperty("URLtomcat") + "/alias/update";
        URLAlias1Get = properties.getProperty("URLtomcat") + "/alias/get";
        URLAlias1Resolve = properties.getProperty("URLtomcat") + "/alias/resolve";
        URLAlias1Inguiry = properties.getProperty("URLtomcat") + "/alias/inquiry";
        URLAlias1Delete = properties.getProperty("URLtomcat") + "/alias/delete";
        URLAlias2Create = properties.getProperty("URLtomcat") + "/alias/v2/create";
        URLAlias2Get = properties.getProperty("URLtomcat") + "/alias/v2/get";
        URLAlias2GetAliasId = properties.getProperty("URLtomcat") + "/alias/v2/getAliasId";
        URLAlias2GetByExternalId = properties.getProperty("URLtomcat") + "/alias/v2/getByExternalId";
        URLAlias2Inquiry = properties.getProperty("URLtomcat") + "/alias/v2/inquiry";
        URLAlias2Resolve = properties.getProperty("URLtomcat") + "/alias/v2/resolve";
        URLAlias2Update = properties.getProperty("URLtomcat") + "/alias/v2/update";
        URLAlias2UpdateStatus = properties.getProperty("URLtomcat") + "/alias/v2/updateStatus";
        URLAlias2CreatePaymentCredential = properties.getProperty("URLtomcat") + "/alias/v2/createPaymentCredential";
        URLAlias2GetPaymentCredential = properties.getProperty("URLtomcat") + "/alias/v2/getPaymentCredential";
        URLAlias2GetAllPaymentCredentials = properties.getProperty("URLtomcat") + "/alias/v2/getAllPaymentCredentials";
        URLAlias2DeletePaymentCredential = properties.getProperty("URLtomcat") + "/alias/v2/deletePaymentCredential";
        URLAlias2Delete = properties.getProperty("URLtomcat") + "/alias/v2/delete"; // Додано шлях для запиту DeleteAlias

        // Читання конфігурації бази даних
        Properties dbProperties = new Properties();
        try (FileInputStream fis = new FileInputStream("properties/env.properties")) {
            dbProperties.load(fis);
            DB_URL = dbProperties.getProperty("DB_CONNECTION_TP2ST");
            DB_USER = dbProperties.getProperty("DB_USER_OWNER");
            DB_PASSWORD = dbProperties.getProperty("DB_PASSWORD");

        } catch (IOException e) {
            e.printStackTrace();
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


    protected String getResourceContent(String resourcePath) throws IOException {
        try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
            if (is == null) {
                throw new IOException("Resource not found: " + resourcePath);
            }
            return readInputStreamToString(is);
        }
    }

    protected String readInputStreamToString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }

    protected HttpResponse sendGetRequest(String url, String queryParams) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(url + "?" + queryParams);
        return client.execute(post);
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
}