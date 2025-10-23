package com.ecom.tests.base;

import static com.ecom.core.config.EnvData.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

public class BaseTestAlias2
    extends BaseApiTest { // todo потом переделать(или убрать) это промежуточный BaseTestAlias2?
  protected String baseUrl = URL_TOMCAT;
  protected String TerminalID = TERMINAL_ID_AVAL_1;
  protected String MerchantID = MERCHANT_ID_AVAL_1;
  protected String Terminal2ID = TERMINAL_ID_AVAL_4;
  protected String Merchant2ID = MERCHANT_ID_AVAL_4;
  protected static String alias1Id = Long.toString(System.currentTimeMillis());
  protected static String alias2Id = Long.toString(System.currentTimeMillis() + 1).toString();

  // protected String RequestID = properties.getProperty("RequestID");

  // Метод для отримання aliasId з бази даних
  protected String getAliasIdFromDatabase(String trackingId, String query, String resultAliasId)
      throws Exception {
    String aliasId = null;
    try (Connection connection = getDBConnection();
        PreparedStatement statement = connection.prepareStatement(query)) {
      statement.setString(1, trackingId);
      try (ResultSet resultSet = statement.executeQuery()) {
        if (resultSet.next()) {
          aliasId = resultSet.getString(resultAliasId);
        }
      }
    }
    return aliasId;
  }

  protected String generateGUID() {
    return UUID.randomUUID().toString();
  }

  protected String generateUniqueRequestID() {
    return "REQ" + System.currentTimeMillis();
  }
}
