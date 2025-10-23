package tests.nFile;


import com.ecom.api.type.FieldsNFile;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import static com.ecom.tests.support.ParserNFile.checkNFile;
import static com.ecom.tests.support.ParserNFile.readTransInfoFromNFile;
import static com.ecom.tests.support.ParserNFile.readTransInfoFromSerialized;

public class CheckNFileByTransactionsPaylink {
    ArrayList<Map<FieldsNFile, String>> listOfTransactions = readTransInfoFromSerialized("nfilePayment.txt");
    SoftAssert softAssertion= new SoftAssert();

    @Test
    public void checkNFilePayRefundTransactions() throws IOException {
        for (Map rrn : listOfTransactions) {
            System.out.println(rrn);
        }
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(saveTetResult()));
        softAssertion.assertEquals(checkNFile(listOfTransactions, bw), 0,"Test fail");
        bw.flush();
        softAssertion.assertAll();
    }

    @Test
    public void test() {
        ArrayList<Map<FieldsNFile, String>> listOfTransactions = readTransInfoFromNFile();

        listOfTransactions.forEach(tran -> {
            System.out.println(">>> tran: " + tran);
        });
    }

    public static FileOutputStream saveTetResult(){
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream("TestNFile_Result.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return fout;
    }
}