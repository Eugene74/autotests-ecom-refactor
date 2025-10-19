package com.ecom.core.xml;

/** Runtime exception thrown when XML parsing or validation fails. */
public class XmlProcessingException extends RuntimeException {

  public XmlProcessingException(String message, Throwable cause) {
    super(message, cause);
  }

  public XmlProcessingException(String message) {
    super(message);
  }
}
