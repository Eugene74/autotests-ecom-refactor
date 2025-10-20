package tests.dashboard.functional.tests.MoneyTransfer;

import com.ecom.tests.support.DashboardRequest;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import tests.BaseRedirect;

import static com.ecom.core.config.CardConfig.cardMCmt;
import static com.ecom.core.config.EnvData.merchant_AVAL;
import static com.ecom.core.config.EnvData.terminal_AVAL;
import static com.ecom.core.config.EnvData.URL_MT_Tran;
import static com.ecom.tests.support.DocumentTools.*;
import static com.ecom.tests.support.MoneyTransferRequests.transferAccountToCard;
import static com.ecom.tests.support.RequestSenderRest.sendRequest;
public class DomesticTransfer extends BaseRedirect {
    private String trackingID;

    @Test
    public void moneyTransfer(){
        Document requestDoc = transferAccountToCard(cardMCmt, merchant_AVAL, terminal_AVAL);
        Document responseDoc = sendRequest(URL_MT_Tran, requestDoc);
        trackingID = getElementFromDocument(responseDoc, "TrackingId");
        System.out.println(trackingID);

        String rrnPayment = getElementFromDocument(responseDoc, "RRN");
        Assert.assertEquals(rrnPayment.length(), 12, "RRN");
    }

    @Test(dependsOnMethods = "moneyTransfer")
    public void findMTTransactionByTrackID()  {
        DashboardRequest request = new DashboardRequest();
        boolean searchByTrack = request.findMTDomTranByID(trackingID);

        Assert.assertEquals(searchByTrack, true, "Search result flag");
    }

    @Test(dependsOnMethods = "moneyTransfer")
    public void findMTTransactionByMerch()  {
        DashboardRequest request = new DashboardRequest();
        boolean searchByMerch = request.findMTDomTranByMID("YVPauto", trackingID);

        Assert.assertEquals(searchByMerch, true, "Search result flag");
    }

    @Test(dependsOnMethods = "moneyTransfer")
    public void findMTTransactionByCard()  {
        DashboardRequest request = new DashboardRequest();
        boolean searchByCard = request.findMTDomTranByCard(cardMCmt[0], trackingID);

        Assert.assertEquals(searchByCard, true, "Search result flag");
    }

    @Test(dependsOnMethods = "moneyTransfer")
    public void findMTTransactionByMultipleFields()  {
        DashboardRequest request = new DashboardRequest();
        boolean searchResults = request.findMTDomTranByMultFields("YVPauto", cardMCmt[0], trackingID);

        Assert.assertEquals(searchResults, true, "Search result flag");
    }

    @Test(dependsOnMethods = "moneyTransfer")
    public void openTransaction()  {
        DashboardRequest request = new DashboardRequest();
        boolean is_opened = request.openDomesticTransaction(trackingID);

        Assert.assertEquals(true, is_opened, "Search result flag");
    }

    @Test (dependsOnMethods = "moneyTransfer")
    public void makeReceipt(){
        DashboardRequest request = new DashboardRequest();
        boolean is_ReceiptMade  = request.makeMTReceipt(trackingID);
        Assert.assertEquals(true, is_ReceiptMade, "Receipt maked flag");
    }
}