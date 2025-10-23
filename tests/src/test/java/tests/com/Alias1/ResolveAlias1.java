package tests.com.Alias1;

import static com.ecom.core.config.EnvData.URL_ALIAS_1_RESOLVE;
import static com.ecom.core.config.XMLAliasResource.XML_RESOLVE_ALIAS1_RESOURCE;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import com.ecom.tests.base.BaseTestAlias1;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ResolveAlias1 extends BaseTestAlias1 {

  @Test(dependsOnMethods = "tests.com.Alias1.GetAlias1.testGetAlias1")
  public void testResolveAlias1() throws Exception {
    String xmlContent = getResourceContent(XML_RESOLVE_ALIAS1_RESOURCE);
    String requestId = SharedDataStore.requestId;
    Map<String, String> params = new HashMap<>();
    params.put("MerchantID", MerchantID);
    params.put("TerminalID", TerminalID);
    params.put("RequestID", requestId);
    for (Map.Entry<String, String> entry : params.entrySet()) {
      xmlContent = xmlContent.replace("${" + entry.getKey() + "}", entry.getValue());
    }
    HttpResponse response = sendPostRequest(URL_ALIAS_1_RESOLVE, xmlContent);
    assertNotNull(response);
    int statusCode = response.getStatusLine().getStatusCode();
    System.out.println("Received status code from the server: " + statusCode);
    assertEquals(
        statusCode,
        200,
        "Received status code " + statusCode + " from the server, but expected 200");
    String responseContent = EntityUtils.toString(response.getEntity(), "UTF-8");
    SoftAssert softAssertion = new SoftAssert();
    String maskedResponse = maskRecipientPAN(responseContent);
    System.out.println("Masked Response (XML):");
    printSpecificTagsResolve(maskedResponse); // Друкуємо спеціфічні теги
    verifyXmlResponse(maskedResponse, softAssertion, "ResolveAliasResult");
    softAssertion.assertAll();
  }

  private String maskRecipientPAN(String xml) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    NodeList recipientPANNodes = doc.getElementsByTagName("recipientPrimaryAccountNumber");
    for (int i = 0; i < recipientPANNodes.getLength(); i++) {
      Node node = recipientPANNodes.item(i);
      String originalValue = node.getTextContent().trim();
      String maskedValue = originalValue.replaceAll(".", "*");
      node.setTextContent(maskedValue);
    }
    // Перетворення документа назад в строку
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    DOMSource source = new DOMSource(doc);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    StreamResult result = new StreamResult(outputStream);
    transformer.transform(source, result);
    return new String(outputStream.toByteArray(), StandardCharsets.UTF_8);
  }

  private void printSpecificTagsResolve(String xml) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
    String[] tags = {
      "TerminalId",
      "TrackingId>",
      "country",
      "city",
      "issuerName",
      "cardType",
      "recipientName",
      "postalCode",
      "address1",
      "address2"
    };
    for (String tag : tags) {
      NodeList nodeList = doc.getElementsByTagName(tag);
      if (nodeList.getLength() > 0) {
        String content = nodeList.item(0).getTextContent().trim();
        System.out.println("<" + tag + ">" + content + "</" + tag + ">");
      }
    }
  }
}
