package tests.dashboard.functional.tests.Merchants;

import methods.DashboardRequest;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.BaseRedirect;
import tests.dashboard.functional.pages.MainPage;

public class SearchMerchant extends BaseRedirect  {
    @Test
    public static void searchMerchByID() {
        DashboardRequest request = new DashboardRequest();
        boolean searchCheck = request.searchMerchantByID();

        Assert.assertEquals(true, searchCheck, "Search result flag");
    }

    @Test
    public static void searchMerchByName() {
        DashboardRequest request = new DashboardRequest();
        boolean searchCheck = request.searchMerchantByName("YVPauto");
        Assert.assertEquals(true, searchCheck, "Search result flag");

    }

    @Test
    public static void searchByMultipleFields() {
        DashboardRequest request = new DashboardRequest();
        boolean searchCheck = request.searchMerchantByMultFields();

        Assert.assertEquals(true, searchCheck, "Search result flag");
    }

    @Test
    public static void openMerchant() {
        DashboardRequest request = new DashboardRequest();
        boolean searchCheck = request.openFindedMerchant("YVPauto");

        Assert.assertEquals(true, searchCheck, "Search result flag");
    }
}