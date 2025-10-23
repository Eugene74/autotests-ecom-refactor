package tests.com.Kafka;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Factory;

public class TestFactory {
  @DataProvider(name = "locales")
  public static Object[][] createLocales() {
    return new Object[][] {
      {"en", "/html/body/div/div/main/div/ul/li[4]"}, // Англійська
      {"uk", "/html/body/div/div/main/div/ul/li[5]"}, // Українська
      {"de", "/html/body/div/div/main/div/ul/li[16]"} // Німецька
    };
  }

  @Factory(dataProvider = "locales")
  public Object[] createInstances(String locale, String languageXPath) {
    return new Object[] {new AVAL_New_front(locale, languageXPath)};
  }
}
