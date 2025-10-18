package tests.nFile;

import org.testng.annotations.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static org.testng.AssertJUnit.assertFalse;
import static tests.BaseTest.transactions;

public class SaveTransactionsInfo {

    @Test
    public void saveDataTestNFile(){
//        assertFalse(tranPaymentFields.isEmpty());
        assertFalse(transactions.isEmpty());
        saveTranInfoAfterTestMap("nfilePayment.txt");
    }

    public static void  saveTranInfoAfterTestMap(String fileName) {
        try {
            try (FileOutputStream fout = new FileOutputStream(fileName)) {
                ObjectOutputStream oos = new ObjectOutputStream(fout);
                oos.writeObject(transactions);
                fout.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
