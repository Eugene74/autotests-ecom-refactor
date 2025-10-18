package com.ecom.tests.base;

import com.codeborne.selenide.WebDriverRunner;
import com.ecom.core.config.PropertiesManager;
import com.ecom.core.driver.OptionsManager;
import com.ecom.core.driver.SelenideDriverManager;
import com.ecom.core.xml.TemplateCatalog;
import com.ecom.model.ECommTransaction;
import com.ecom.type.FieldsNFile;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Central base implementation that provides configuration and driver lifecycle helpers.
 */
public abstract class BaseTestSupport {

  protected static final PropertiesManager PROPERTIES = PropertiesManager.getInstance();

  /** Paylink XML templates. */
  protected static final String XML_TEMPLATES_PAY_REQ_PATH = TemplateCatalog.PAY_REQUEST;
  protected static final String XML_TEMPLATES_TOKEN_REQ_PATH = TemplateCatalog.TOKEN_REQUEST;
  protected static final String XML_TEMPLATES_REV_REQ_PATH = TemplateCatalog.REVERSAL_REQUEST;
  protected static final String XML_TEMPLATES_INSTALLMENT_CHOOSE_PATH =
      TemplateCatalog.INSTALLMENT_CHOOSE;
  protected static final String XML_TEMPLATES_GET_INSTALLMENT_PATH =
      TemplateCatalog.GET_INSTALLMENT_PLANS;
  protected static final String XML_TEMPLATES_LOCAL_INSTALLMENT_PATH =
      TemplateCatalog.LOCAL_INSTALLMENT;
  protected static final String XML_TEMPLATES_MPI_ENROLL_REQ_PATH = TemplateCatalog.MPI_ENROLLMENT;
  protected static final String XML_TEMPLATES_MPI_AUTH_REQ_PATH = TemplateCatalog.MPI_AUTHORIZATION;

  /** MDES/VTS XML templates. */
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
      TemplateCatalog.MDES_VTS_TOKEN_DELETE;

  /** MDES/VTS JSON templates. */
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
      TemplateCatalog.JSON_MDES_VTS_TOKEN_DELETE;

  protected static final String AUTH_PATH = TemplateCatalog.HTML_AUTHORIZATION;
  protected static final String AUTH_PATH_INSTALLMENT = TemplateCatalog.HTML_AUTHORIZATION_INSTALLMENT;
  protected static final String VERIFY_PATH = TemplateCatalog.HTML_ACCOUNT_VERIFICATION;

  public static final List<String> tranPayment = new ArrayList<>();
  public static final List<Map<FieldsNFile, String>> tranPaymentFields = new ArrayList<>();
  public static final List<Map<FieldsNFile, String>> tranReversFields = new ArrayList<>();
  public static final List<ECommTransaction> transactions = new ArrayList<>();

  protected final String environment = PROPERTIES.getEnvironment();
  protected final boolean isHeadless = PROPERTIES.isHeadless();

  protected final String login = PROPERTIES.getEnv("login");
  protected final String password = PROPERTIES.getEnv("password");
  protected final String urlmaildev =
      String.format(PROPERTIES.getEnv("base.urlmaildev"), environment);
  protected final String pipelineName =
      PROPERTIES.getEnv("URLtomee").split("-tomee")[0].replace("https://", "").toLowerCase();

  protected final String URL_ECG = PROPERTIES.getEnv("URLtomee") + "/paylink/extranet/index.jsp";
  protected final String URL_MERCH = PROPERTIES.getEnv("URLtomee") + "/go/merchant/do?action=trans";
  protected final String URL = PROPERTIES.getEnv("URLtomee") + "/go/service/02";
  protected final String URLjsonToken = PROPERTIES.getEnv("URLtomee") + "/go/payByToken";
  protected final String URL_Inst = PROPERTIES.getEnv("URLtomee") + "/go/service/installments";

  protected final String URLmt = PROPERTIES.getEnv("URLtomcat");
  protected final String URLmtTran = PROPERTIES.getEnv("URLtomcat") + "/mt/tran";
  protected final String URLp2p = PROPERTIES.getEnv("URLtomcat") + "/mt/p2p/init/";
  protected final String URLdasboard = PROPERTIES.getEnv("URLtomcat") + "/dashboard/login";
  protected final String URLfr = PROPERTIES.getEnv("URLtomcat") + "/mt/public/fast-refund/doFastRefund";
  protected final String URLInvocing = PROPERTIES.getEnv("URLtomee") + "/go";
  protected final String TermURL = PROPERTIES.getEnv("URLtomee") + "/go/enter";
  protected final String ActiveServerURL = PROPERTIES.getEnv("URLtomee") + "/paylink/astest";
  protected final String CallbackURl = PROPERTIES.getEnv("URLtomee") + "/go/3ds";
  protected final String URLredirect = PROPERTIES.getEnv("URLtomee") + "/go/pay";
  protected final String URLredirectPay = PROPERTIES.getEnv("URLtomee") + "/go/pay";
  protected final String URLredirectCapture = PROPERTIES.getEnv("URLtomee") + "/go/capture";

  protected final String URLMdesVtsCreateToken = PROPERTIES.getEnv("URLtomee") + "/go/token/create";
  protected final String URLMdesVtsRefreshToken = PROPERTIES.getEnv("URLtomee") + "/go/token/refresh";
  protected final String URLMdesVtsCardMetadata = PROPERTIES.getEnv("URLtomee") + "/go/card/metadata";
  protected final String URLMdesVtsCardContent = PROPERTIES.getEnv("URLtomee") + "/go/card/content";
  protected final String URLMdesVtsTokenStatus = PROPERTIES.getEnv("URLtomee") + "/go/token/status";
  protected final String URLMdesVtsTokenSuspend = PROPERTIES.getEnv("URLtomee") + "/go/token/suspend";
  protected final String URLMdesVtsTokenResume = PROPERTIES.getEnv("URLtomee") + "/go/token/resume";
  protected final String URLMdesVtsTokenDelete = PROPERTIES.getEnv("URLtomee") + "/go/token/delete";

  protected final Integer id_AVAL = Integer.valueOf(PROPERTIES.getEnv("idAVAL." + environment));
  protected final String merchant_AVAL = PROPERTIES.getEnv("MerchantID_AVAL");
  protected final String terminal_AVAL = PROPERTIES.getEnv("TerminalID_AVAL");

  protected final Integer id_AVAL1 = Integer.valueOf(PROPERTIES.getEnv("idAVAL1." + environment));
  protected final String merchantID_aval1 = PROPERTIES.getEnv("MerchantID_AVAL1");
  protected final String terminalID_aval1 = PROPERTIES.getEnv("TerminalID_AVAL1");

  protected final Integer id_AVAL2 = Integer.valueOf(PROPERTIES.getEnv("idAVAL2." + environment));
  protected final String merchantID_aval2 = PROPERTIES.getEnv("MerchantID_AVAL2");
  protected final String terminalID_aval2 = PROPERTIES.getEnv("TerminalID_AVAL2");

  protected final Integer id_AVAL3 = Integer.valueOf(PROPERTIES.getEnv("idAVAL3." + environment));
  protected final String merchantID_aval3 = PROPERTIES.getEnv("MerchantID_AVAL3");
  protected final String terminalID_aval3 = PROPERTIES.getEnv("TerminalID_AVAL3");

  protected final Integer id_AVAL4 = Integer.valueOf(PROPERTIES.getEnv("idAVAL4." + environment));
  protected final String merchantID_aval4 = PROPERTIES.getEnv("MerchantID_AVAL4");
  protected final String terminalID_aval4 = PROPERTIES.getEnv("TerminalID_AVAL4");

  protected final Integer id_AVAL5 = Integer.valueOf(PROPERTIES.getEnv("idAVAL5." + environment));
  protected final String merchantID_aval5 = PROPERTIES.getEnv("MerchantID_AVAL5");
  protected final String terminalID_aval5 = PROPERTIES.getEnv("TerminalID_AVAL5");

  protected final Integer idFacilAVAL = Integer.valueOf(PROPERTIES.getEnv("idFacilAVAL." + environment));
  protected final String merchantIDFacil_AVAL = PROPERTIES.getEnv("MerchantIDFacil_AVAL");
  protected final String terminalIDFacil_AVAL = PROPERTIES.getEnv("TerminalIDFacil_AVAL");

  protected final String merchantIDLook = PROPERTIES.getEnv("MerchantID_AVAL");
  protected final String terminalIDLook = PROPERTIES.getEnv("TerminalID_AVAL");

  protected final Integer motoFiltr = Integer.valueOf(PROPERTIES.getEnv("MOTO"));
  protected final Integer voiceFiltr = Integer.valueOf(PROPERTIES.getEnv("VOICE"));

  protected final String[] cardVISA = PROPERTIES.getCardArray("cardVISA");
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
  protected final String cardVts = PROPERTIES.getCard("cardVts");

  protected WebDriver driver;

  protected void setup() {
    ensureChromeDriverBinary();
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

  protected void ensureChromeDriverBinary() {
    boolean driverAvailable = false;
    try {
      Process process = new ProcessBuilder("chromedriver", "--version").start();
      driverAvailable = process.waitFor() == 0;
    } catch (IOException exception) {
      driverAvailable = false;
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
    }
    if (!driverAvailable) {
      String driverPath = System.getProperty("user.dir") + "/driver/chromedriver.exe";
      System.setProperty("webdriver.chrome.driver", driverPath);
    }
  }
}
