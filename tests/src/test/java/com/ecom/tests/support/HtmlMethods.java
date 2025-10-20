package com.ecom.tests.support;

import com.ecom.ui.util.Waiters;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import tests.BaseTest;

import java.io.File;
import java.io.IOException;

import static com.ecom.core.config.EnvData.MERCHANT_ID_LOOK;
import static com.ecom.core.config.EnvData.TERMINAL_ID_LOOK;
import static com.ecom.core.xml.TemplateCatalog.*;

public class HtmlMethods extends BaseTest{
    public static Document readHtmlFile(String path){
        String htmlString = "";
        try {
            htmlString = Jsoup.parse(new File(path), "UTF-8").outerHtml();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Jsoup.parse(htmlString);
    }

    public static void fillAuthorizationForm( String delay, String url, String merch, String term){
        Document doc = readHtmlFile(AUTH_PATH);
        doc.getElementsByTag("form").attr("action", url);
        doc.getElementsByAttributeValue("name","MerchantID").attr("value", merch);
        doc.getElementsByAttributeValue("name","TerminalID").attr("value", term);
        System.out.println("Delay " + delay);
        doc.getElementsByAttributeValue("name","delay").attr("value", delay);

        File f = new File(AUTH_PATH);
        try {
                FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");
                Waiters.sleep(3000);
                String file_string = FileUtils.readFileToString(f, "UTF-8");
            System.out.println(file_string);
        } catch (IOException e) {
            System.out.println("Cannot save!");
        }
    }

    public static void fillAuthorizationFormInstallment( String delay, String url, String merch, String term){
        Document doc = readHtmlFile(AUTH_PATH_INSTALLMENT);
        doc.getElementsByTag("form").attr("action", url);
        doc.getElementsByAttributeValue("name","MerchantID").attr("value", merch);
        doc.getElementsByAttributeValue("name","TerminalID").attr("value", term);
        doc.getElementsByAttributeValue("name","delay").attr("value", delay);

        File f = new File(AUTH_PATH_INSTALLMENT);
        try {
            if(f.canWrite()){
            FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");
            }
        } catch (IOException e) { System.out.println("Cannot save!"); }
        Waiters.sleep(3000);

    }

    public static void fillVerificationForm(String url){
        Document doc = readHtmlFile(VERIFY_PATH);
        doc.getElementsByTag("form").attr("action", url);
        doc.getElementsByAttributeValue("name","MerchantID").attr("value", MERCHANT_ID_LOOK);
        doc.getElementsByAttributeValue("name","TerminalID").attr("value", TERMINAL_ID_LOOK);


        File f = new File(VERIFY_PATH);
        try {
            FileUtils.writeStringToFile(f, doc.outerHtml(), "UTF-8");
            String file_string = FileUtils.readFileToString(f, "UTF-8");
            System.out.println(file_string);
        } catch (IOException e) {
            System.out.println("Cannot save!");
        }
    }

}
