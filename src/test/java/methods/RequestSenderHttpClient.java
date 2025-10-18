package methods;

import org.w3c.dom.Document;

import javax.net.ssl.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.cert.X509Certificate;
import java.time.Duration;
import static java.nio.charset.StandardCharsets.UTF_8;

public class RequestSenderHttpClient {

    private final HttpClient client;
    private final String url;

    public RequestSenderHttpClient(String url) {
        this.url = url;
        this.client = createTrustAllHttpClient();
    }

    public static Document sendRequest(String url, Document request){
        RequestSenderHttpClient httpRequestSender = new RequestSenderHttpClient(url);
        String receiveResponse = httpRequestSender.post(DocumentTools.convertXMLDocumentToString(request));
        return DocumentTools.convertStringToXmlDocument(receiveResponse);
    }

    private String post(String xmlBody) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    //.header("Connection", "Keep-Alive")
                    .header("Cache-Control", "no-cache")
                    .header("User-Agent", "Jakarta Commons-HttpClient/3.1")
                    .header("Content-Type", "application/xml")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                    .POST(HttpRequest.BodyPublishers.ofString(xmlBody, UTF_8))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(UTF_8));
            return response.body();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при выполнении POST запроса: " + url, e);
        }
    }

    private HttpClient createTrustAllHttpClient() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            // Do nothing - trust all clients
                        }
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            // Do nothing - trust all clients
                        }
                    }
            };
            SSLContext sslContext = SSLContext.getInstance("TLS"); //новое безопаное симейство протоколов
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            return HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .connectTimeout(Duration.ofSeconds(10))
                    .followRedirects(HttpClient.Redirect.NORMAL)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Не удалось создать SSLContext", e);
        }
    }
}
