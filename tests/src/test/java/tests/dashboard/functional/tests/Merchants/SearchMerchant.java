package tests.dashboard.functional.tests.Merchants;

import com.ecom.tests.base.BaseUiTest;
import com.ecom.tests.support.DashboardRequest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SearchMerchant extends BaseUiTest {
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
