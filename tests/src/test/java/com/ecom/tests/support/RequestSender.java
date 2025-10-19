package com.ecom.tests.support;

import org.w3c.dom.Document;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.cert.X509Certificate;
@Deprecated
public class RequestSender {

    /*static {
        javax.net.ssl.HttpsURLConnection.setDefaultHostnameVerifier(
                (hostname, sslSession) -> {
                    if (hostname.equals("ecgec3.tad.upc.intranet")) {
                        return true;
                    }
                    return false;
                });
    }*/

    private static HttpsURLConnection con = null;
    public static String xmlResp = null;

    public static Document sendRequest(String url, Document request){
        RequestSender httpRequestSender = new RequestSender(url);
        String receiveResponse = httpRequestSender.post(DocumentTools.convertXMLDocumentToString(request));
        return DocumentTools.convertStringToXmlDocument(receiveResponse);
    }

    private static void configureTrustAllSSL() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    @Override
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        // Do nothing - trust all clients
                    }
                    @Override
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        // Do nothing - trust all servers
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
    }

    public RequestSender(String url){
        try {
            URL obj = new URL(url);
            configureTrustAllSSL();
            con = (HttpsURLConnection) obj.openConnection();
            
        }
        catch(MalformedURLException ex) { System.out.println(">>> Error on open create"); }
        catch (IOException ex) { System.out.println(">>> Error on open connection"); } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String post(String xml) {
        try {
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Cache-Control", "no-cache");
            con.setRequestProperty("User-Agent", "Jakarta Commons-HttpClient/3.1");
            con.setRequestProperty("Content-Type", "application/xml");
            con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

            con.connect();

            //send request
            OutputStream outputStream = con.getOutputStream();
            byte[] postData = xml.getBytes( StandardCharsets.UTF_8 );
            outputStream.write(postData);

            //receive response
            StringBuilder content;
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            content = new StringBuilder();

            while ((line = in.readLine()) != null) { content.append(line); }
            xmlResp = content.toString();
        }
        catch (ProtocolException ex) { ex.printStackTrace(); }
        catch (IOException ex) { ex.printStackTrace(); }
        catch (Exception e) { e.printStackTrace(); }
        return xmlResp;
    }
}