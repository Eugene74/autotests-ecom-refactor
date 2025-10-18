package tests.paylink.admin.pages;
import tests.BaseRedirect;

public class CloseDayPaylink extends BaseRedirect {
    public void closeDay(String idMerch) {
        getDriver().get(URL_ECG);
        BatchPaylink close = new BatchPaylink(getDriver());
        close.login(idMerch);
        close.closed(idMerch);
    }

    public void closeDayForApi(String idMerch) {
        setup();
        getDriver().get(URL_ECG);
        BatchPaylink close = new BatchPaylink(getDriver());
        close.login(idMerch);
        close.closed(idMerch);
        exit();
    }
}