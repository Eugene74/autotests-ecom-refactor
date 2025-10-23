package tests.context;

import com.ecom.api.model.ECommTransaction;
import com.ecom.api.type.FieldsNFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class TransactionContext {
  private TransactionContext() {}

  public static final List<String> ECOM_TRAN_PAYMENT_LIST = new ArrayList<>();
  public static final List<Map<FieldsNFile, String>> ECOM_TRAN_PAYMENT_FIELDS = new ArrayList<>();
  public static final List<Map<FieldsNFile, String>> ECOM_TRAN_REVERS_FIELDS = new ArrayList<>();
  public static final List<ECommTransaction> ECOMM_TRANSACTION_LIST = new ArrayList<>();

  public static void clearAll() {
    ECOM_TRAN_PAYMENT_LIST.clear();
    ECOM_TRAN_PAYMENT_FIELDS.clear();
    ECOM_TRAN_REVERS_FIELDS.clear();
    ECOMM_TRANSACTION_LIST.clear();
  }
}
