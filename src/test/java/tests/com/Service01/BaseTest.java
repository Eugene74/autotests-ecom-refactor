package tests.com.Service01;

import com.ecom.db.JDBCConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.testng.annotations.BeforeClass;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;

public class BaseTest {

    protected String baseUrl;
    protected String MerchantID;
    protected String TerminalID;
    protected String CardNum;
    protected String ExpYear;
    protected String ExpMonth;
    protected String CVNum;
    protected static String OrderID; // Змінено на статичну змінну

    @BeforeClass
    public void setUp() throws IOException {
        // Завантаження пропертів
        Properties properties = new Properties();
        properties.load(new FileReader("properties/env.properties"));

        baseUrl = properties.getProperty("URLtomee");
        MerchantID = properties.getProperty("MerchantID_AVAL1");
        TerminalID = properties.getProperty("TerminalID_AVAL1");

        // Перевірка на наявність всіх необхідних значень
        if (baseUrl == null || MerchantID == null || TerminalID == null) {
            throw new IllegalArgumentException("One of the required properties is null. " +
                    "baseUrl: " + baseUrl + ", MerchantID: " + MerchantID +
                    ", TerminalID: " + TerminalID);
        }

        // Завантаження даних картки з окремого файлу
        Properties cardProperties = new Properties();
        cardProperties.load(new FileReader("properties/cards.properties"));
        String cardDetails = cardProperties.getProperty("cardVISA");
        String[] cardData = cardDetails.split(";");
        CardNum = cardData[0].trim();
        ExpMonth = cardData[2].trim(); // Місяць
        ExpYear = cardData[1].trim(); // Рік
        CVNum = cardData[3].trim();

        // Логування завантажених значень
        System.out.println("CardNum: " + CardNum);
        System.out.println("ExpMonth: " + ExpMonth);
        System.out.println("ExpYear: " + ExpYear);
        System.out.println("CVNum: " + CVNum);

        // Перевірка на наявність всіх необхідних значень для картки
        if (CardNum == null || ExpMonth == null || ExpYear == null || CVNum == null) {
            throw new IllegalArgumentException("One of the required card properties is null. " +
                    "CardNum: " + CardNum + ", ExpMonth: " + ExpMonth +
                    ", ExpYear: " + ExpYear + ", CVNum: " + CVNum);
        }

        // Перевірка формату місяця
        if (!ExpMonth.matches("^(0[1-9]|1[0-2])$")) {
            throw new IllegalArgumentException("ExpMonth format is invalid. Expected format is MM.");
        }
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

    protected String getResourceContent(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }

    protected Connection getDBConnection() throws SQLException {
        return JDBCConnection.getDBConnection();
    }

    // Метод для генерації динамічного OrderID
    protected String generateOrderID() {
        Random random = new Random();
        int orderId = random.nextInt(1000000) + 1;
        return "24" + orderId;
    }
}
