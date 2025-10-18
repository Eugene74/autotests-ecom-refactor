package tests.dashboard.functional.tests.MoneyTransfer;

import com.ecom.tests.support.DashboardRequest;
import org.testng.Assert;
import org.testng.annotations.Test;
import tests.BaseRedirect;

public class ExchangeRate extends BaseRedirect {

    public static final String currencyFrom = "AFN";
    public static final String currencyTo = "ALL";
    public static final String rate = "56";
    public static final String bank = "AVAL";

    @Test
    public static void createExchangeRate(){
        DashboardRequest request = new DashboardRequest();
        boolean createFlag = request.createExchangeRate(currencyFrom, currencyTo, rate, bank);

        Assert.assertEquals(createFlag, true, "creation flag");
    }

    @Test(dependsOnMethods = "createExchangeRate")
    public static void deleteExchangeRate(){
        DashboardRequest request = new DashboardRequest();
        boolean createFlag = request.deleteExchangeRate(currencyFrom, currencyTo, rate, bank);

        Assert.assertEquals(createFlag, true, "creation flag");
    }
}
