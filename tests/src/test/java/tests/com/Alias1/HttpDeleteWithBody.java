package tests.com.Alias1;

import java.net.URI;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

@Deprecated
public class HttpDeleteWithBody
    extends HttpEntityEnclosingRequestBase { // todo в фреймворке этот метод чато встречается -
  // убрать в утилиту или сервис, рефакторить весь
  // фреймворк
  public static final String METHOD_NAME =
      "DELETE"; // todo только разные  METHOD_NAME везде, можно просто передавать параметром

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
