package tests.nFile;


import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import tests.BaseTest;
import com.ecom.api.type.FieldsNFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import static methods.ParserNFile.*;

public class CheckNFileByTransactionsPaylink extends BaseTest {
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