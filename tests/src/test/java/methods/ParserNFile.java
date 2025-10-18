package methods;

import com.ecom.core.util.ResourceUtils;
import com.ecom.api.type.FieldsNFile;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

import static com.ecom.api.type.FieldsNFile.*;

public class ParserNFile {
    private static final int startPoint = 112;
    private static final String PATH = "nFiles/new.txt";

    public static ArrayList<Map<FieldsNFile, String>> readTransInfoFromSerialized(String fileName) {
        FileInputStream fin;
        ArrayList<Map<FieldsNFile, String>> listOfMapTransactions = new ArrayList<>();
        try {
            fin = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fin);
            listOfMapTransactions = (ArrayList<Map<FieldsNFile, String>>) ois.readObject();
            fin.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return listOfMapTransactions;
    }

    public static ArrayList<Map<FieldsNFile, String>> readTransInfoFromNFile() {
        ArrayList<Map<FieldsNFile, String>> listOfMapTransactions = new ArrayList<>();

//        FileInputStream fileInputStream;
//        try {
//            fileInputStream = new FileInputStream(fileName);
//
//            fileInputStream.
//
//            fileInputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(ResourceUtils.stream(PATH), StandardCharsets.UTF_8))) {
            reader.lines().forEach(line -> {
                System.out.println(line);
                System.out.println("-------------------------------------------------------------------------------------");
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        return listOfMapTransactions;
    }

    public static int getIndexStart(String rrn, String tranType) throws IOException {
        String data = ResourceUtils.readAsString(PATH);
        FieldsNFile field = FieldsNFile.valueOf(Tran_type.toString());
        int start = data.indexOf(rrn) + (field.getStart()-startPoint);
        int end = start + field.getLength();
        return (data.substring(start, end).equals(tranType))? data.indexOf(rrn) : data.lastIndexOf(rrn);
    }

    public static String getValueFromNFileByFieldName(String rrn, String f, String tranType) throws IOException {
        String data = ResourceUtils.readAsString(PATH);
        FieldsNFile field = FieldsNFile.valueOf(f);
        int start = getIndexStart(rrn, tranType) + (field.getStart()-startPoint);
        int end = start + field.getLength();
        return data.substring(start, end);
    }

    public static String getTagFromField(String tag, String field){
        if (field.contains(tag)){return tag;}
        else {return "";}
    }

    public static String readFild122(Map pay, String rrn, String fildName, Object key, String tranType) throws IOException {
        String fieldValue = getValueFromNFileByFieldName(rrn, fildName, tranType);
        String readFild122 = key + StringUtils.repeat(" ", (20 - fildName.length())) + ":  expected = " + pay.get(key)
                + StringUtils.repeat(" ", (32 - pay.get(key).toString().length())) + ":  actual = "
                + getTagFromField("3DSV", fieldValue)+"+"+getTagFromField("SAAV", fieldValue)+"+"+getTagFromField("CAVV", fieldValue)+": "+pay.get(key).toString();
        //getTagFromField("3DSV", fieldValue);
        System.out.println(readFild122);
        return readFild122;
    }

    public static String readFild126(Map pay, String rrn, String fieldName, Object key, String tranType) throws IOException {
        String fieldValue = getValueFromNFileByFieldName(rrn, fieldName, tranType );
        String readFild126 = key
                + StringUtils.repeat(" ", (20 - fieldName.length())) + ":  expected = "
                + pay.get(key)
                + StringUtils.repeat(" ", (32 - pay.get(key).toString().length())) + ":  actual = "
                + getTagFromField("15POSE001R", fieldValue);
        System.out.println(readFild126);
        return readFild126;
    }

    public static String readResult(Map pay, String rrn, String fieldName, Object key, Boolean result, String tranType) throws IOException {
        String readResult = key
                + StringUtils.repeat(" ", (20 - fieldName.length())) + "|  expected = "
                + pay.get(key)
                + StringUtils.repeat(" ", (32 - pay.get(key).toString().length())) + "|  actual = "
                + getValueFromNFileByFieldName(rrn, fieldName, tranType )
                + StringUtils.repeat(" ", (32 - getValueFromNFileByFieldName(rrn, fieldName,tranType ).length())) + "| result: "
                + (result? "PASS":"<<FAIL>>");
        System.out.println(readResult);
        return readResult;
    }

    public static String readResultCutSpaces(Map pay, String rrn, String fieldName, Object key, Boolean result, String tranType) throws IOException {
        String readResult = key
                + StringUtils.repeat(" ", (20 - fieldName.length())) + "|  expected = "
                + pay.get(key).toString().trim()
                + StringUtils.repeat(" ", (32 - pay.get(key).toString().trim().length())) + "|  actual = "
                + getValueFromNFileByFieldName(rrn, fieldName, tranType).trim()
                + StringUtils.repeat(" ", (32 - getValueFromNFileByFieldName(rrn, fieldName,tranType ).trim().length())) + "| result: "
                + (result? "PASS":"<<FAIL>>");
        System.out.println(readResult);
        return readResult;
    }

    public static int checkNFile(ArrayList<Map<FieldsNFile, String>> transactions, BufferedWriter bw) throws IOException {
        int i = 0;
        int countFailTest = 0;
        Boolean result;

        for (Map<FieldsNFile, String> pay : transactions) {

            bw.write("-------------------------------------------------------------");
            bw.newLine();
            bw.write(++i + ". Test Name: " + pay.get(TestName));
            bw.newLine();
            bw.write("RRN: " + pay.get(Ref_number));
            bw.newLine();

            System.out.println("-------------------------------------------------------------");
            String rrn = pay.get(Ref_number);
            String tranType = pay.get(Tran_type);
            System.out.println(i + ". Test Name: " + pay.get(TestName));
            System.out.println("RRN: " + rrn);

            for (FieldsNFile fieldsNFile : pay.keySet()) {
                String fieldName = fieldsNFile.toString();

                if (fieldName.equals(TestName.toString()) || fieldName.equals(Ref_number.toString())) {
                    continue;
                }
                if (fieldName.equals(Mtid.toString()) && Mtid.toString().equals("16"))
//                        fieldName.equals(FLD_122.toString()) ||
//                        fieldName.equals(FLD_122_addendum.toString()) ||
//                        fieldName.equals(FLD_122_addendumMC06.toString()) ||
//                        fieldName.equals(FLD_122_MC06.toString()) ||
//                        fieldName.equals(FLD_122_MC05.toString()) ||
//                        fieldName.equals(FLD_122_addendum3DS.toString()))
                {
                    Assert.assertTrue(getValueFromNFileByFieldName(rrn, fieldName, tranType ).contains("3DSV"));
                    Assert.assertTrue(getValueFromNFileByFieldName(rrn, fieldName, tranType).contains("SAAV"));
                    Assert.assertTrue(getValueFromNFileByFieldName(rrn, fieldName, tranType).contains(getValueFromNFileByFieldName(rrn, fieldName, tranType)));
                    bw.write(readFild122(pay, rrn, fieldName, fieldsNFile, tranType));
                    bw.newLine();
                } else if (fieldName.equals(Mtid.toString()) && Mtid.toString().equals("15"))
//                        fieldName.equals(FLD_126_reccurent.toString()))
                {
                    Assert.assertTrue(getValueFromNFileByFieldName(rrn, fieldName, tranType).contains("POSE001R"));
                    bw.write(readFild126(pay, rrn, fieldName, fieldsNFile, tranType));
                    bw.newLine();
                } else {
                    result = pay.get(fieldsNFile).equals(getValueFromNFileByFieldName(rrn, fieldName, tranType)) ? true : false;
                    if (!result){ ++countFailTest; }
                    if (fieldName.equals(FLD_095.toString())){
                        bw.write(readResultCutSpaces(pay, rrn, fieldName, fieldsNFile, result, tranType));
                    } else { bw.write(readResult(pay, rrn, fieldName, fieldsNFile, result, tranType)); }
                    bw.newLine();
                }
            }

            bw.write("-------------------------------------------------------------");
            bw.newLine();
            System.out.println("-------------------------------------------------------------");
        }
        return countFailTest;
    }
}
