package com.ecom.tests.base;

import static com.ecom.core.config.CardConfig.cardVISA;
import static com.ecom.core.config.EnvData.MERCHANT_ID_AVAL_1;
import static com.ecom.core.config.EnvData.TERMINAL_ID_AVAL_1;
import static com.ecom.core.config.EnvData.URL_TOMEE;

import java.io.IOException;
import java.util.Random;
import org.testng.annotations.BeforeClass;

public class CrossBaseTestXML extends BaseApiTest {
  protected String baseUrl = URL_TOMEE;
  protected String MerchantID = MERCHANT_ID_AVAL_1;
  protected String TerminalID = TERMINAL_ID_AVAL_1;
  protected String CardNum;
  protected String ExpYear;
  protected String ExpMonth;
  protected String CVNum;
  protected static String OrderID; // Змінено на статичну змінну

  @BeforeClass
  public void setUp() throws IOException {
    // Перевірка на наявність всіх необхідних значень
    if (baseUrl == null || MerchantID == null || TerminalID == null) {
      throw new IllegalArgumentException(
          "One of the required properties is null. "
              + "baseUrl: "
              + baseUrl
              + ", MerchantID: "
              + MerchantID
              + ", TerminalID: "
              + TerminalID);
    }
    // Завантаження даних картки з окремого файлу
    String[] cardData = cardVISA; // cardDetails.split(";");
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
      throw new IllegalArgumentException(
          "One of the required card properties is null. "
              + "CardNum: "
              + CardNum
              + ", ExpMonth: "
              + ExpMonth
              + ", ExpYear: "
              + ExpYear
              + ", CVNum: "
              + CVNum);
    }

    // Перевірка формату місяця
    if (!ExpMonth.matches("^(0[1-9]|1[0-2])$")) {
      throw new IllegalArgumentException("ExpMonth format is invalid. Expected format is MM.");
    }
  }

  // Метод для генерації динамічного OrderID
  protected String generateOrderID() {
    Random random = new Random();
    int orderId = random.nextInt(1000000) + 1;
    return "24" + orderId;
  }
}
