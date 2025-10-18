package tests;

import model.ECommTransaction;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.Dimension;
import type.FieldsNFile;
import methods.WebDriverHolder;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;


public class BaseTest {

    /** Paylink */
    protected static final String XML_TEMPLATES_PAY_REQ_PATH = "./src/main/resources/template/xml/paylink/PayRequest.xml";
    protected static final String XML_TEMPLATES_TOKEN_REQ_PATH = "./src/main/resources/template/xml/paylink/TokenRequest.xml";
    protected static final String XML_TEMPLATES_REV_REQ_PATH = "./src/main/resources/template/xml/paylink/ReversalRequest.xml";
    protected static final String XML_TEMPLATES_INSTALLMENT_CHOOSE_PATH = "./src/main/resources/template/xml/paylink/Installment.xml";
    protected static final String XML_TEMPLATES_GET_INSTALLMENT_PATH = "./src/main/resources/template/xml/paylink/GetInstallmentPlans.xml";
    protected static final String XML_TEMPLATES_LOCAL_INSTALLMENT_PATH = "./src/main/resources/template/xml/paylink/LocalInstallment.xml";
    protected static final String XML_TEMPLATES_MPI_ENROLL_REQ_PATH = "./src/main/resources/template/xml/mpi/enrollmentRequest.xml";
    protected static final String XML_TEMPLATES_MPI_AUTH_REQ_PATH = "./src/main/resources/template/xml/mpi/authorizationRequest.xml";
    protected static final String XML_TEMPLATES_CREATE_TOKEN_MDES_VTS = "/template/xml/api/mdesvts/create_token.xml";
    protected static final String XML_TEMPLATES_REFRESH_TOKEN_MDES_VTS = "/template/xml/api/mdesvts/refresh_token.xml";
    protected static final String XML_TEMPLATES_CARD_METADATA_MDES_VTS = "/template/xml/api/mdesvts/card_metadata.xml";
    protected static final String XML_TEMPLATES_CARD_CONTENT_MDES_VTS = "/template/xml/api/mdesvts/card_content.xml";
    protected static final String XML_TEMPLATES_TOKEN_STATUS_MDES_VTS = "/template/xml/api/mdesvts/token_status.xml";
    protected static final String XML_TEMPLATES_TOKEN_SUSPEND_MDES_VTS = "/template/xml/api/mdesvts/token_suspend.xml";
    protected static final String XML_TEMPLATES_TOKEN_RESUME_MDES_VTS = "/template/xml/api/mdesvts/token_resume.xml";
    protected static final String XML_TEMPLATES_TOKEN_DELETE_MDES_VTS = "/template/xml/api/mdesvts/token_delete.xml";
    protected static final String JSON_TEMPLATES_CREATE_TOKEN_MDES_VTS = "/template/json/mdesvts/create_token.json";
    protected static final String JSON_TEMPLATES_REFRESH_TOKEN_MDES_VTS = "/template/json/mdesvts/refresh_token.json";
    protected static final String JSON_TEMPLATES_CARD_METADATA_MDES_VTS = "/template/json/mdesvts/card_metadata.json";
    protected static final String JSON_TEMPLATES_CARD_CONTENT_MDES_VTS = "/template/json/mdesvts/card_content.json";
    protected static final String JSON_TEMPLATES_TOKEN_STATUS_MDES_VTS = "/template/json/mdesvts/token_status.json";
    protected static final String JSON_TEMPLATES_TOKEN_SUSPEND_MDES_VTS = "/template/json/mdesvts/token_suspend.json";
    protected static final String JSON_TEMPLATES_TOKEN_RESUME_MDES_VTS = "/template/json/mdesvts/token_resume.json";
    protected static final String JSON_TEMPLATES_TOKEN_DELETE_MDES_VTS = "/template/json/mdesvts/token_delete.json";

    protected static final String AUTH_PATH = "./src/main/resources/template/html/Authorization.html";
    protected static final String AUTH_PATH_INSTALLMENT = "./src/main/resources/template/html/AuthorizationInstallment.html";
    protected static final String VERIFY_PATH = "./src/main/resources/template/html/AccountVerification.html";

    public static ArrayList<String> tranPayment = new ArrayList<>();
    public static List<Map<FieldsNFile, String>> tranPaymentFields = new ArrayList<>();
    public static List<Map<FieldsNFile, String>> tranReversFields = new ArrayList<>();
    public static List<ECommTransaction> transactions = new ArrayList<>();


    public static Properties cardsProperties = new Properties();
    protected static Properties envProperties = new Properties();

    static {
        FileInputStream fisCards;
        FileInputStream fisEnv;
        try {
            fisCards = new FileInputStream("properties/cards.properties");
            fisEnv = new FileInputStream("properties/env.properties");
            cardsProperties.load(fisCards);
            envProperties.load(fisEnv);
        } catch (IOException e) {
            System.err.println("ERROR. No properties file!");
        }
    }

    public static String environment = System.getProperty("env", envProperties.getProperty("default_env"));
    boolean isHeadless = Boolean.parseBoolean(envProperties.getProperty("is_headless", "false"));

    protected String login = envProperties.getProperty("login");
    protected String password = envProperties.getProperty("password");
    protected String urlmaildev =  String.format(
            envProperties.getProperty("base.urlmaildev"),
            System.getProperty("env", envProperties.getProperty("default_env")));
    protected String pipelineName = envProperties.getProperty("URLtomee")
            .split("-tomee")[0]
            .replace("https://", "")
            .toLowerCase();

    protected static String URL_ECG = envProperties.getProperty("URLtomee")+"/paylink/extranet/index.jsp";

    protected static String URL_MERCH = envProperties.getProperty("URLtomee")+"/go/merchant/do?action=trans";

    protected static String URL = envProperties.getProperty("URLtomee")+"/go/service/02";
    protected static String URLjsonToken = envProperties.getProperty("URLtomee")+"/go/payByToken";
    protected static String URL_Inst = envProperties.getProperty("URLtomee")+"/go/service/installments";

    protected static String URLmt = envProperties.getProperty("URLtomcat");
    protected static String URLmtTran = envProperties.getProperty("URLtomcat") + "/mt/tran";
    protected static String URLp2p = envProperties.getProperty("URLtomcat")+ "/mt/p2p/init/";
    protected static String URLdasboard = envProperties.getProperty("URLtomcat")+"/dashboard/login";
    protected static String URLfr = envProperties.getProperty("URLtomcat") + "/mt/public/fast-refund/doFastRefund";
//1111111111
    protected static String URLInvocing = envProperties.getProperty("URLtomee")+"/go";
    protected static String TermURL = envProperties.getProperty("URLtomee")+"/go/enter";
    protected static String ActiveServerURL = envProperties.getProperty("URLtomee") + "/paylink/astest";
    protected static String CallbackURl = envProperties.getProperty("URLtomee") + "/go/3ds";
    protected static String URLredirect = envProperties.getProperty("URLtomee")+"/go/pay";
    protected static String URLredirectPay = envProperties.getProperty("URLtomee")+"/go/pay";
    protected static String URLredirectCapture = envProperties.getProperty("URLtomee")+"/go/capture";

    protected static String URLMdesVtsCreateToken = envProperties.getProperty("URLtomee")+"/go/token/create";
    protected static String URLMdesVtsRefreshToken = envProperties.getProperty("URLtomee")+"/go/token/refresh";
    protected static String URLMdesVtsCardMetadata = envProperties.getProperty("URLtomee")+"/go/card/metadata";
    protected static String URLMdesVtsCardContent = envProperties.getProperty("URLtomee")+"/go/card/content";
    protected static String URLMdesVtsTokenStatus = envProperties.getProperty("URLtomee")+"/go/token/status";
    protected static String URLMdesVtsTokenSuspend = envProperties.getProperty("URLtomee")+"/go/token/suspend";
    protected static String URLMdesVtsTokenResume = envProperties.getProperty("URLtomee")+"/go/token/resume";
    protected static String URLMdesVtsTokenDelete = envProperties.getProperty("URLtomee")+"/go/token/delete";

    // merchant aval
    protected static Integer id_AVAL = Integer.valueOf(envProperties.getProperty("idAVAL."+environment));
    protected static String merchant_AVAL = envProperties.getProperty("MerchantID_AVAL");
    protected static String terminal_AVAL = envProperties.getProperty("TerminalID_AVAL");

    // AutoYVP
    protected static Integer id_AVAL1 = Integer.valueOf(envProperties.getProperty("idAVAL1."+environment));
    protected static String merchantID_aval1 = envProperties.getProperty("MerchantID_AVAL1");
    protected static String terminalID_aval1 = envProperties.getProperty("TerminalID_AVAL1");

    // Auto_YVP1
    protected static Integer id_AVAL2 = Integer.valueOf(envProperties.getProperty("idAVAL2."+environment));
    protected static String merchantID_aval2 = envProperties.getProperty("MerchantID_AVAL2");
    protected static String terminalID_aval2 = envProperties.getProperty("TerminalID_AVAL2");

    // Auto_YVP2
    protected static Integer id_AVAL3 = Integer.valueOf(envProperties.getProperty("idAVAL3."+environment));
    protected static String merchantID_aval3 = envProperties.getProperty("MerchantID_AVAL3");
    protected static String terminalID_aval3 = envProperties.getProperty("TerminalID_AVAL3");

    // Auto_YVP3
    protected static Integer id_AVAL4 = Integer.valueOf(envProperties.getProperty("idAVAL4."+environment));
    protected static String merchantID_aval4 = envProperties.getProperty("MerchantID_AVAL4");
    protected static String terminalID_aval4 = envProperties.getProperty("TerminalID_AVAL4");

    // Auto_YVP4
    protected static Integer id_AVAL5 = Integer.valueOf(envProperties.getProperty("idAVAL5."+environment));
    protected static String merchantID_aval5 = envProperties.getProperty("MerchantID_AVAL5");
    protected static String terminalID_aval5 = envProperties.getProperty("TerminalID_AVAL5");

    // merchant FacilitatorMC
    protected static Integer idFacilAVAL = Integer.valueOf(envProperties.getProperty("idFacilAVAL."+environment));
    protected String merchantIDFacil_AVAL = envProperties.getProperty("MerchantIDFacil_AVAL");
    protected String terminalIDFacil_AVAL = envProperties.getProperty("TerminalIDFacil_AVAL");

    // merchant LookUp
    protected static String merchantIDLook = envProperties.getProperty("MerchantID_AVAL");
    protected static String terminalIDLook = envProperties.getProperty("TerminalID_AVAL");


    protected Integer motoFiltr = Integer.valueOf(envProperties.getProperty("MOTO"));
    protected Integer voiceFiltr = Integer.valueOf(envProperties.getProperty("VOICE"));


    // cards
    protected String[] cardVISA = cardsProperties.getProperty("cardVISA").split(";");
    protected String[] cardVISAmt = cardsProperties.getProperty("cardVISAmt").split(";");

    protected String[] cardMC = cardsProperties.getProperty("cardMC").split(";");
    protected String[] cardMC06 = cardsProperties.getProperty("cardMC06").split(";");

    protected String[] cardMC07 = cardsProperties.getProperty("cardMC07").split(";");
    protected String[] cardMC05 = cardsProperties.getProperty("cardMC05").split(";");
    protected String[] cardMCa = cardsProperties.getProperty("cardMCa").split(";");
    protected String[] cardMCInst = cardsProperties.getProperty("cardMCInst").split(";");

    protected String[] cardMCmt = cardsProperties.getProperty("cardMCmt").split(";");
    protected String[] cardMCmta = cardsProperties.getProperty("cardMCmta").split(";");
    protected String[] cardVisaRed = cardsProperties.getProperty("cardVisaRed").split(";");
    protected String cardVISArec = cardsProperties.getProperty("cardVISArec");
    protected String cardMCfr = cardsProperties.getProperty("cardMCfr");
    protected String cardMCardfr = cardsProperties.getProperty("cardMCardfr");
    protected String cardVISAfr = cardsProperties.getProperty("cardVISAfr");
    protected String[] cardVISAmtRev = cardsProperties.getProperty("cardVISAmtRev").split(";");

    protected static String cardMdes = cardsProperties.getProperty("cardMdes");
    protected static String cardVts = cardsProperties.getProperty("cardVts");




    public static WebDriver driver;
    protected void setup() {
        boolean isDriverInPath = false;
        try {
            Process process = new ProcessBuilder("chromedriver", "--version").start();
            int exitCode = process.waitFor();
            isDriverInPath = (exitCode == 0);
        } catch (Exception e) {
            isDriverInPath = false;
        }

        // Set path only if not found in PATH
        if (!isDriverInPath) {
            System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
        }

        ChromeOptions options = new ChromeOptions();
        if (isHeadless){
            options.addArguments("--headless");
            options.addArguments("--window-size=3840,2160"); // Maximum 4K resolution
        }
        // Bypass Cloudflare checks
        options.addArguments("--disable-blink-features=AutomationControlled");

        options.addArguments("--ignore-certificate-errors");
        options.addArguments("--allow-insecure-localhost");
        options.addArguments("--disable-web-security");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        WebDriverHolder.setDriver(driver);
        if (isHeadless) {
            driver.manage().window().setSize(new Dimension(3840, 2160));
        } else {
            driver.manage().window().maximize();
        }
        driver.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
    }

    protected void exit() {

        driver.quit();
    }
    public WebDriver getDriver() {

        return driver;
    }
}