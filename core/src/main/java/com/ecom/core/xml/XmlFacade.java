package com.ecom.core.xml;

import java.io.InputStream;
import java.util.function.Consumer;
import javax.xml.validation.Schema;
import org.w3c.dom.Document;

/** High level facade encapsulating template loading and validation workflows. */
public final class XmlFacade {

  public static XmlFacade create() {
    return new XmlFacade();
  }

  private XmlFacade() {}

  public Document load(String resourcePath) {
    return XmlUtils.parseResource(resourcePath);
  }

  public Document load(InputStream inputStream) {
    return XmlUtils.parse(inputStream);
  }

  public Document fromString(String xml) {
    return XmlUtils.parse(xml);
  }

  public Document template(String templatePath, Consumer<Document> customiser) {
    Document document = load(templatePath);
    if (customiser != null) {
      customiser.accept(document);
    }
    return document;
  }

  public Document create(Consumer<Document> builder) {
    Document document = XmlUtils.newDocument();
    if (builder != null) {
      builder.accept(document);
    }
    return document;
  }

  public String asString(Document document) {
    return XmlUtils.toString(document);
  }

  public void validate(Document document, String schemaPath) {
    XmlUtils.validate(document, schemaPath);
  }

  public void validate(Document document, Schema schema) {
    XmlUtils.validate(document, schema);
  }

  public Schema loadSchema(String schemaPath) {
    return XmlUtils.loadSchema(schemaPath);
  }
}
