package com.ecom.tests.base;

import com.codeborne.selenide.WebDriverRunner;
import com.ecom.api.model.ECommTransaction;
import com.ecom.api.type.FieldsNFile;
import com.ecom.ui.driver.OptionsManager;
import com.ecom.ui.driver.SelenideDriverManager;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.ecom.core.config.EnvData.isHeadless;

/**
 * Central base implementation that provides configuration and driver lifecycle helpers.
 */
public abstract class BaseTestSupport {

  //protected static final PropertiesManager PROPERTIES = PropertiesManager.getInstance();

  /** Paylink XML templates. */
  /*protected static final String XML_TEMPLATES_PAY_REQ_PATH = TemplateCatalog.PAY_REQUEST;
  protected static final String XML_TEMPLATES_TOKEN_REQ_PATH = TemplateCatalog.TOKEN_REQUEST;
  protected static final String XML_TEMPLATES_REV_REQ_PATH = TemplateCatalog.REVERSAL_REQUEST;
  protected static final String XML_TEMPLATES_INSTALLMENT_CHOOSE_PATH =
      TemplateCatalog.INSTALLMENT_CHOOSE;
  protected static final String XML_TEMPLATES_GET_INSTALLMENT_PATH =
      TemplateCatalog.GET_INSTALLMENT_PLANS;
  protected static final String XML_TEMPLATES_LOCAL_INSTALLMENT_PATH =
      TemplateCatalog.LOCAL_INSTALLMENT;
  protected static final String XML_TEMPLATES_MPI_ENROLL_REQ_PATH = TemplateCatalog.MPI_ENROLLMENT;
  protected static final String XML_TEMPLATES_MPI_AUTH_REQ_PATH = TemplateCatalog.MPI_AUTHORIZATION;*/

/*  *//** MDES/VTS XML templates. *//*
  protected static final String XML_TEMPLATES_CREATE_TOKEN_MDES_VTS =
      TemplateCatalog.MDES_VTS_CREATE_TOKEN;
  protected static final String XML_TEMPLATES_REFRESH_TOKEN_MDES_VTS =
      TemplateCatalog.MDES_VTS_REFRESH_TOKEN;
  protected static final String XML_TEMPLATES_CARD_METADATA_MDES_VTS =
      TemplateCatalog.MDES_VTS_CARD_METADATA;
  protected static final String XML_TEMPLATES_CARD_CONTENT_MDES_VTS =
      TemplateCatalog.MDES_VTS_CARD_CONTENT;
  protected static final String XML_TEMPLATES_TOKEN_STATUS_MDES_VTS =
      TemplateCatalog.MDES_VTS_TOKEN_STATUS;
  protected static final String XML_TEMPLATES_TOKEN_SUSPEND_MDES_VTS =
      TemplateCatalog.MDES_VTS_TOKEN_SUSPEND;
  protected static final String XML_TEMPLATES_TOKEN_RESUME_MDES_VTS =
      TemplateCatalog.MDES_VTS_TOKEN_RESUME;
  protected static final String XML_TEMPLATES_TOKEN_DELETE_MDES_VTS =
      TemplateCatalog.MDES_VTS_TOKEN_DELETE;*/

 /* *//** MDES/VTS JSON templates. *//*
  protected static final String JSON_TEMPLATES_CREATE_TOKEN_MDES_VTS =
      TemplateCatalog.JSON_MDES_VTS_CREATE_TOKEN;
  protected static final String JSON_TEMPLATES_REFRESH_TOKEN_MDES_VTS =
      TemplateCatalog.JSON_MDES_VTS_REFRESH_TOKEN;
  protected static final String JSON_TEMPLATES_CARD_METADATA_MDES_VTS =
      TemplateCatalog.JSON_MDES_VTS_CARD_METADATA;
  protected static final String JSON_TEMPLATES_CARD_CONTENT_MDES_VTS =
      TemplateCatalog.JSON_MDES_VTS_CARD_CONTENT;
  protected static final String JSON_TEMPLATES_TOKEN_STATUS_MDES_VTS =
      TemplateCatalog.JSON_MDES_VTS_TOKEN_STATUS;
  protected static final String JSON_TEMPLATES_TOKEN_SUSPEND_MDES_VTS =
      TemplateCatalog.JSON_MDES_VTS_TOKEN_SUSPEND;
  protected static final String JSON_TEMPLATES_TOKEN_RESUME_MDES_VTS =
      TemplateCatalog.JSON_MDES_VTS_TOKEN_RESUME;
  protected static final String JSON_TEMPLATES_TOKEN_DELETE_MDES_VTS =
      TemplateCatalog.JSON_MDES_VTS_TOKEN_DELETE;*/

  /*protected static final String AUTH_PATH = TemplateCatalog.HTML_AUTHORIZATION;
  protected static final String AUTH_PATH_INSTALLMENT = TemplateCatalog.HTML_AUTHORIZATION_INSTALLMENT;
  protected static final String VERIFY_PATH = TemplateCatalog.HTML_ACCOUNT_VERIFICATION;*/

  public static final List<String> tranPayment = new ArrayList<>();
  public static final List<Map<FieldsNFile, String>> tranPaymentFields = new ArrayList<>();
  public static final List<Map<FieldsNFile, String>> tranReversFields = new ArrayList<>();
  public static final List<ECommTransaction> transactions = new ArrayList<>();

  //protected final boolean isHeadless = PROPERTIES.isHeadless();

  /*protected final Integer motoFiltr = Integer.valueOf(PROPERTIES.getEnv("MOTO"));
  protected final Integer voiceFiltr = Integer.valueOf(PROPERTIES.getEnv("VOICE"));*/

  /*protected final String[] cardVISA = PROPERTIES.getCardArray("cardVISA");
  protected final String[] cardVISAmt = PROPERTIES.getCardArray("cardVISAmt");
  protected final String[] cardMC = PROPERTIES.getCardArray("cardMC");
  protected final String[] cardMC06 = PROPERTIES.getCardArray("cardMC06");
  protected final String[] cardMC07 = PROPERTIES.getCardArray("cardMC07");
  protected final String[] cardMC05 = PROPERTIES.getCardArray("cardMC05");
  protected final String[] cardMCa = PROPERTIES.getCardArray("cardMCa");
  protected final String[] cardMCInst = PROPERTIES.getCardArray("cardMCInst");
  protected final String[] cardMCmt = PROPERTIES.getCardArray("cardMCmt");
  protected final String[] cardMCmta = PROPERTIES.getCardArray("cardMCmta");
  protected final String[] cardVisaRed = PROPERTIES.getCardArray("cardVisaRed");
  protected final String cardVISArec = PROPERTIES.getCard("cardVISArec");
  protected final String cardMCfr = PROPERTIES.getCard("cardMCfr");
  protected final String cardMCardfr = PROPERTIES.getCard("cardMCardfr");
  protected final String cardVISAfr = PROPERTIES.getCard("cardVISAfr");
  protected final String[] cardVISAmtRev = PROPERTIES.getCardArray("cardVISAmtRev");
  protected final String cardMdes = PROPERTIES.getCard("cardMdes");
  protected final String cardVts = PROPERTIES.getCard("cardVts");*/

  protected WebDriver driver;

  protected void setup() {
    SelenideDriverManager.ensureChromeDriverBinary();
    SelenideDriverManager.configure();
    ChromeDriver chromeDriver = new ChromeDriver(OptionsManager.chrome(isHeadless));
    if (isHeadless) {
      chromeDriver.manage().window().setSize(new Dimension(3840, 2160));
    } else {
      chromeDriver.manage().window().maximize();
    }
    chromeDriver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
    WebDriverRunner.setWebDriver(chromeDriver);
    driver = chromeDriver;
  }

  protected void exit() {
    if (driver != null) {
      driver.quit();
      driver = null;
      WebDriverRunner.closeWebDriver();
    }
  }

  public WebDriver getDriver() {
    return driver == null ? WebDriverRunner.getWebDriver() : driver;
  }

}
