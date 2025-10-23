package tests.nFile;

import static org.testng.AssertJUnit.assertFalse;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.testng.annotations.Test;
import tests.context.TransactionContext;

public class SaveTransactionsInfo {

  @Test
  public void saveDataTestNFile() {
    //        assertFalse(TransactionContext.tranPaymentFields.isEmpty());
    assertFalse(TransactionContext.ECOMM_TRANSACTION_LIST.isEmpty());
    saveTranInfoAfterTestMap("nfilePayment.txt");
  }

  public static void saveTranInfoAfterTestMap(String fileName) {
    try {
      try (FileOutputStream fout = new FileOutputStream(fileName)) {
        ObjectOutputStream oos = new ObjectOutputStream(fout);
        oos.writeObject(TransactionContext.ECOMM_TRANSACTION_LIST);
        fout.close();
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
