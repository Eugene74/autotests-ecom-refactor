package com.ecom.core.xml;

import com.ecom.utils.ResourceUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/** Common XML utilities for parsing, serialising and validating documents. */
public final class XmlUtils {

  private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY =
      DocumentBuilderFactory.newInstance();

  static {
    DOCUMENT_BUILDER_FACTORY.setNamespaceAware(true);
    DOCUMENT_BUILDER_FACTORY.setIgnoringElementContentWhitespace(true);
  }

  private XmlUtils() {}

  public static Document parse(InputStream inputStream) {
    try {
      return newDocumentBuilder().parse(inputStream);
    } catch (SAXException | IOException | ParserConfigurationException exception) {
      throw new XmlProcessingException("Unable to parse XML document", exception);
    }
  }

  public static Document parse(String xml) {
    try (InputStream stream =
        new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8))) {
      return parse(stream);
    } catch (IOException exception) {
      throw new XmlProcessingException("Unable to read XML payload", exception);
    }
  }

  public static Document parseResource(String resourcePath) {
    try (InputStream stream = ResourceUtils.stream(resourcePath)) {
      return parse(stream);
    } catch (IOException exception) {
      throw new XmlProcessingException(
          "Unable to open XML resource from " + resourcePath, exception);
    }
  }

  public static Document newDocument() {
    try {
      return newDocumentBuilder().newDocument();
    } catch (ParserConfigurationException exception) {
      throw new XmlProcessingException("Unable to construct XML document", exception);
    }
  }

  public static String toString(Document document) {
    try {
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      StreamResult result = new StreamResult(new java.io.StringWriter());
      transformer.transform(new DOMSource(document), result);
      return result.getWriter().toString();
    } catch (TransformerException exception) {
      throw new XmlProcessingException("Unable to serialise XML document", exception);
    }
  }

  public static String toString(Node node) {
    try {
      Transformer transformer = TransformerFactory.newInstance().newTransformer();
      transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
      transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      StreamResult result = new StreamResult(new java.io.StringWriter());
      transformer.transform(new DOMSource(node), result);
      return result.getWriter().toString();
    } catch (TransformerException exception) {
      throw new XmlProcessingException("Unable to serialise XML node", exception);
    }
  }

  public static Schema loadSchema(String schemaPath) {
    SchemaFactory schemaFactory =
        SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    try (InputStream stream = ResourceUtils.stream(schemaPath)) {
      return schemaFactory.newSchema(new StreamSource(stream));
    } catch (SAXException | IOException exception) {
      throw new XmlProcessingException("Unable to load XML schema from " + schemaPath, exception);
    }
  }

  public static void validate(Document document, String schemaPath) {
    validate(document, loadSchema(schemaPath));
  }

  public static void validate(Document document, Schema schema) {
    try {
      Validator validator = schema.newValidator();
      validator.validate(new DOMSource(document));
    } catch (IOException | SAXException exception) {
      throw new XmlProcessingException("XML validation failed", exception);
    }
  }

  private static DocumentBuilder newDocumentBuilder() throws ParserConfigurationException {
    return DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
  }
}
