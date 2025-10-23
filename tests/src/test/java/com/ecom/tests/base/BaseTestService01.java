package com.ecom.tests.base;

import com.ecom.utils.ResourceUtils;
import com.ecom.db.JDBCConnection;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import static com.ecom.core.config.CardConfig.cardVISA;
import static com.ecom.core.config.EnvData.URL_TOMEE;
import static com.ecom.core.config.EnvData.merchantID_aval1;
import static com.ecom.core.config.EnvData.terminalID_aval1;

public class BaseTestService01 extends BaseHybridTest {

    protected String baseUrl = URL_TOMEE;
    protected String MerchantID = merchantID_aval1;
    protected String TerminalID = terminalID_aval1;
    protected String CardNum;
    protected String ExpYear;
    protected String ExpMonth;
    protected String CVNum;
    protected static String OrderID; // Змінено на статичну змінну

    @BeforeClass
    public void setUp() throws IOException {
        // Перевірка на наявність всіх необхідних значень
        if (baseUrl == null || MerchantID == null || TerminalID == null) {
            throw new IllegalArgumentException("One of the required properties is null. " +
                    "baseUrl: " + baseUrl + ", MerchantID: " + MerchantID +
                    ", TerminalID: " + TerminalID);
        }
        // Завантаження даних картки з окремого файлу
        String[] cardData = cardVISA; //cardDetails.split(";");
        CardNum = cardData[0].trim();
        ExpMonth = cardData[2].trim(); // Місяць
        ExpYear = cardData[1].trim(); // Рік
        CVNum = cardData[3].trim();

        // Логування завантажених значень
        System.out.println("CardNum: " + CardNum);
        System.out.println("ExpMonth: " + ExpMonth);
        System.out.println("ExpYear: " + ExpYear);
        System.out.println("CVNum: " + CVNum);

        // Перевірка на наявність всіх необхідних значень для картки
        if (CardNum == null || ExpMonth == null || ExpYear == null || CVNum == null) {
            throw new IllegalArgumentException("One of the required card properties is null. " +
                    "CardNum: " + CardNum + ", ExpMonth: " + ExpMonth +
                    ", ExpYear: " + ExpYear + ", CVNum: " + CVNum);
        }

        // Перевірка формату місяця
        if (!ExpMonth.matches("^(0[1-9]|1[0-2])$")) {
            throw new IllegalArgumentException("ExpMonth format is invalid. Expected format is MM.");
        }
    }

    protected String getResourceContent(String filePath) {
        return ResourceUtils.readAsString(filePath);
    }

    // Метод для генерації динамічного OrderID
    protected String generateOrderID() {
        Random random = new Random();
        int orderId = random.nextInt(1000000) + 1;
        return "24" + orderId;
    }

    protected String[] getDatabaseValues() {
        String[] values = new String[3];
        try (Connection connection = JDBCConnection.getDBConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT RRN, APPROVAL_CODE, ORDER_ID FROM TRAN WHERE TRAN_ID = (SELECT MAX(TRAN_ID) FROM TRAN)"
             )) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                values[0] = resultSet.getString("ORDER_ID"); // ORDER_ID
                values[1] = resultSet.getString("APPROVAL_CODE"); // APPROVAL_CODE
                values[2] = resultSet.getString("RRN"); // RRN
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("Не вдалося виконати запит до бази даних: " + e.getMessage());
        }
        return values;
    }
}
