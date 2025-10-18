package methods;

import org.w3c.dom.Document;
import tests.BaseTest;
import tests.paylink.admin.pages.CloseDayPaylink;

import static methods.DocumentTools.*;

public class PaylinkRequests extends BaseTest{

    public static void paylinkCloseDay(Integer idMerchant)  {
        CloseDayPaylink end = new CloseDayPaylink();
        end.closeDay(idMerchant.toString());
    }

    public static void paylinkCloseDayForApi(Integer idMerchant)  {
        CloseDayPaylink end = new CloseDayPaylink();
        end.closeDayForApi(idMerchant.toString());
    }

    public static Document payment(String[] card, String description, String merch, String term){
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTest.XML_TEMPLATES_PAY_REQ_PATH);
        fillRequestData(newRequestDoc, card, merch, term, description);
        return newRequestDoc;
    }

    public static Document paymentToken(String[] token, String description, String merch, String term){
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTest.XML_TEMPLATES_TOKEN_REQ_PATH);
        fillTokenRequestData(newRequestDoc, token, merch, term, description);
        return newRequestDoc;
    }


    public static Document paymentBorica(String[] card, String description, String merch, String term, String amount){
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTest.XML_TEMPLATES_PAY_REQ_PATH);
        fillRequestDataBorica(newRequestDoc, card, merch, term, description, amount);
        return newRequestDoc;
    }

    public static Document paymentAmount(String[] card, String amount, String description, String merch, String term){
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTest.XML_TEMPLATES_PAY_REQ_PATH);
        fillRequestDataAmount(newRequestDoc, card, merch, term, description, amount);
        return newRequestDoc;
    }

    public static Document paymentPares(String[] card, String description, String merch, String term){
        Document newRequestDoc = payment(card, description, merch, term);
        fillParesData(newRequestDoc, card, "PayData");
        return newRequestDoc;
    }

    public static Document getInstPlansRequest(String[] card, String amount, String merch, String term){
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTest.XML_TEMPLATES_GET_INSTALLMENT_PATH);
        fillGetInstPlanRequest(newRequestDoc, card, amount, merch, term);
        return newRequestDoc;
    }
    
    public static Document paymentLocalInstallment(String[] card, String description, String merch, String term, String subsequentAmount,String instPlanParamId, String numberOfPay, String feeMonth, String interestRate, String checkValue){//, String [] insData){
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTest.XML_TEMPLATES_LOCAL_INSTALLMENT_PATH);
        assert newRequestDoc != null;
        fillRequestData(newRequestDoc, card, merch, term, description);
        newRequestDoc.getElementsByTagName("TotalAmount").item(0).setTextContent(subsequentAmount);

//      fillParesData(newRequestDoc, card, "PayData");
        fillLocalInstChoice(newRequestDoc, instPlanParamId, numberOfPay, feeMonth, interestRate, checkValue);
        return newRequestDoc;
    }

    public static Document paymentEmv(String[] card, String description, String merch, String term){
        Document newRequestDoc = payment(card, description, merch, term);
        fillEMVData(newRequestDoc, card, "PayData");
        return newRequestDoc;
    }

    public static Document paymentEmvBorica(String[] card, String description, String merch, String term, String amount){
        Document newRequestDoc = paymentBorica(card, description, merch, term, amount);
        fillEMVData(newRequestDoc, card, "PayData");
        return newRequestDoc;
    }

    public static Document paymentCheckout(String[] card, String description, String merch, String term){
        Document newRequestDoc = payment(card, description, merch, term);
        fillCheckoutData(newRequestDoc);
        return newRequestDoc;
    }

    public static Document paymentAddendum(String[] card, String description, String merch, String term, String paysys){
        Document newRequestDoc = paymentPares(card, description, merch, term);
        fillAddendumData(newRequestDoc, paysys);
        return newRequestDoc;
    }

    public static Document paymentMasterpass(String[] card, String description, String merch, String term){
        Document newRequestDoc = payment(card, description, merch, term);
        addTags(newRequestDoc, "PayData", "Walletid", "999");
        return newRequestDoc;
    }

    public static Document paymentRecurent(String[] card, String description, String merch, String term, String value){
        Document newRequestDoc = payment(card, description, merch, term);
        addTags(newRequestDoc, "PayData", "Recurrent", value);
        return newRequestDoc;
    }

    public static Document paymentPreAuth(String[] card, String description, String merch, String term){
        Document newRequestDoc = payment(card, description, merch, term);
        newRequestDoc.renameNode(newRequestDoc.getElementsByTagName("Authorization").item(0), "", "Preauthorization");
        return newRequestDoc;
    }

    public static Document paymentPreAuthPares(String[] card, String description, String merch, String term){
        Document newRequestDoc = paymentPares(card, description, merch, term);
        newRequestDoc.renameNode(newRequestDoc.getElementsByTagName("Authorization").item(0), "", "Preauthorization");
        return newRequestDoc;
    }

    public static Document paymentPostAuth(Document requestPay, Document responsePay, String description, double delta){
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTest.XML_TEMPLATES_REV_REQ_PATH);
        fillPostRequest(newRequestDoc, requestPay, responsePay, description, delta);
        return newRequestDoc;
    }

    public static Document paymentInstallChoice(Document requestPay, Document responsePay){
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTest.XML_TEMPLATES_INSTALLMENT_CHOOSE_PATH);
        assert newRequestDoc != null;
        fillInvoice(newRequestDoc, requestPay);
        fillFromResponseData(newRequestDoc,responsePay);
        return newRequestDoc;
    }

    public static Document reversal(Document requestPay, Document responsePay, double delta){
        Document requestRevDoc = DocumentTools.readXMLFile(BaseTest.XML_TEMPLATES_REV_REQ_PATH);
        fillRequestRefund(requestRevDoc, requestPay, responsePay, delta);
        return requestRevDoc;
    }

    public static Document settlementRefund(String[] card, String description, String merch, String term){
        Document newRequestDoc = payment(card, description, merch, term);
        DocumentTools.fillSettlement(newRequestDoc);
        return newRequestDoc;
    }

    public static Document enrollRequest(String[] card, String description, String merch, String term){
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTest.XML_TEMPLATES_MPI_ENROLL_REQ_PATH);
        fillRequestMPIData(newRequestDoc, card, merch, term, description);
        return newRequestDoc;
    }

    public static Document mpiAuthRequest(String paRes, String merch, String term){
        Document newRequestDoc = DocumentTools.readXMLFile(BaseTest.XML_TEMPLATES_MPI_AUTH_REQ_PATH);
        fillMerchant(newRequestDoc, merch, term );
        newRequestDoc.getElementsByTagName("PaRes").item(0).setTextContent(paRes);
        return newRequestDoc;
    }
}
