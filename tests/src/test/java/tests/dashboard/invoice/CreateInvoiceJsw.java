package tests.dashboard.invoice;

import static io.restassured.RestAssured.given;

import com.ecom.utils.ResourceUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.testng.annotations.Test;

@Test
public class CreateInvoiceJsw {

  public String privateKeyPath = "keys/private.pem";

  //    @BeforeTest
  //    public void init() throws Exception {
  //      this.privateKeyPath = getFilePathString("src/main/resources/keys/private.pem");
  //      this.privateKeyPath = readFileAsString("src/main/resources/keys/private.pem");
  //    }

  @Test
  public void createAndSingMerchantInvoice() throws Exception {

    String urlCreate = "https://ecgec.tad.upc.intranet:8442/dashboard/api/public/merchant-invoices";
    String file = "template/json/CreateInvoice.json";
    String json = readFileAsString(file);
    System.out.println(json);
    Response response =
        given()
            .accept(ContentType.JSON)
            .contentType(ContentType.JSON)
            .baseUri(urlCreate)
            .body(json)
            .when()
            .post();
    System.out.println(response.asString());

    //
    //        JWSObject jwsObject = new JWSObject(
    //                new JWSHeader(JWSAlgorithm.RS256),
    //                new Payload(json)
    //        );
    ////        jwsObject.sign(new RSASSASigner((RSAPrivateKey) getPrivateKey()));
    //        System.out.println(jwsObject.getParsedString());
  }

  public static String readFileAsString(String file) {
    return ResourceUtils.readAsString(file);
  }

  //    private String getFilePathString(String pathToFile) {
  //        return Thread.currentThread().getContextClassLoader().getResource("").getPath() +
  // pathToFile;
  //    }
  public PrivateKey getPrivateKey() throws IOException {
    Security.addProvider(new BouncyCastleProvider());
    try (Reader reader =
            new InputStreamReader(ResourceUtils.stream(privateKeyPath), StandardCharsets.UTF_8);
        PEMParser parser = new PEMParser(reader)) {
      PEMKeyPair keyPair = (PEMKeyPair) parser.readObject();
      return new JcaPEMKeyConverter().getKeyPair(keyPair).getPrivate();
    }
  }
}
