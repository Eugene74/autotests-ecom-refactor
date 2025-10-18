package tests.dashboard.invoice;


import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.testng.annotations.Test;
import tests.BaseTest;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.Security;

import static io.restassured.RestAssured.given;

@Test
public class CreateInvoiceJsw extends BaseTest{

    public String privateKeyPath = "src/main/resources/keys/private.pem";

//    @BeforeTest
//    public void init() throws Exception {
//      this.privateKeyPath = getFilePathString("src/main/resources/keys/private.pem");
//      this.privateKeyPath = readFileAsString("src/main/resources/keys/private.pem");
//    }

    @Test
    public void createAndSingMerchantInvoice() throws Exception {

        String urlCreate = "https://ecgec.tad.upc.intranet:8442/dashboard/api/public/merchant-invoices";
        String file = "src/main/resources/template/json/CreateInvoice.json";
        String json = readFileAsString(file);
        System.out.println(json);
        Response response= given().accept(ContentType.JSON).contentType(ContentType.JSON).baseUri(urlCreate).body(json).when().post();
        System.out.println(response.asString());

//
//        JWSObject jwsObject = new JWSObject(
//                new JWSHeader(JWSAlgorithm.RS256),
//                new Payload(json)
//        );
////        jwsObject.sign(new RSASSASigner((RSAPrivateKey) getPrivateKey()));
//        System.out.println(jwsObject.getParsedString());
    }


    public static String readFileAsString(String file)throws Exception
    {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

//    private String getFilePathString(String pathToFile) {
//        return Thread.currentThread().getContextClassLoader().getResource("").getPath() + pathToFile;
//    }
    public PrivateKey getPrivateKey() throws IOException {
        PEMParser reader = new PEMParser(new FileReader(privateKeyPath));
        Security.addProvider(new BouncyCastleProvider());
        PEMKeyPair keyPair = (PEMKeyPair) reader.readObject();
        return new JcaPEMKeyConverter().getKeyPair(keyPair).getPrivate();
    }

}
