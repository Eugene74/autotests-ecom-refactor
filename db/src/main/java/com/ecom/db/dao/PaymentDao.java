package com.ecom.db.dao;

import com.ecom.api.type.Attributes;
import com.ecom.db.exception.DatabaseAccessException;
import com.ecom.db.spi.ConnectionProvider;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

/** DAO covering payment-related tables such as TRAN, MT_TRAN and supporting entities. */
public class PaymentDao extends BaseDao {

  public PaymentDao(ConnectionProvider connectionProvider) {
    super(connectionProvider);
  }

  public String getNFileNameByTran(int tranId) {
    Integer batchId =
        query(
            "SELECT BATCH_ID FROM TRAN WHERE TRAN_ID = ?",
            statement -> statement.setInt(1, tranId),
            resultSet -> resultSet.next() ? resultSet.getInt("BATCH_ID") : null);
    return batchId == null ? "" : getNFileNameByBatch(batchId);
  }

  public String getNFileNameByBatch(int batchId) {
    return query(
        "SELECT FILENAME FROM BATCH WHERE BATCH_ID = ?",
        statement -> statement.setInt(1, batchId),
        resultSet -> resultSet.next() ? resultSet.getString("FILENAME") : "");
  }

  public String getAcquirerIDByBank(String bank) {
    Integer acquirerId =
        query(
            "SELECT ACQUIRER_ID FROM BANK WHERE BANKNAME = ?",
            statement -> statement.setString(1, bank),
            resultSet -> resultSet.next() ? resultSet.getInt("ACQUIRER_ID") : 0);
    return Integer.toString(acquirerId);
  }

  public int getTranIdByOrder1(String order) {
    String cleaned =
        order.replace("Order ID", "").trim().replace("\n", "").replace("\r", "");
    return query(
        "SELECT MAX(TRAN_ID) AS TRAN_ID FROM TRAN WHERE ORDER_ID = ?",
        statement -> statement.setString(1, cleaned),
        resultSet -> resultSet.next() ? resultSet.getInt("TRAN_ID") : 0);
  }

  public int getTranIdByOrder(String order) {
    String normalized = order;
    if (order.startsWith("Order № ")) {
      normalized = order.substring(8).trim();
    }
    String finalOrder = normalized;
    return query(
        "SELECT MAX(TRAN_ID) AS TRAN_ID FROM TRAN WHERE ORDER_ID = ?",
        statement -> statement.setString(1, finalOrder),
        resultSet -> resultSet.next() ? resultSet.getInt("TRAN_ID") : 0);
  }

  public int getTranIdByOrderFR(String order) {
    return query(
        "SELECT MT_TRAN_ID FROM MT_TRAN WHERE TRAN_TYPE = 'A' ORDER BY TRAN_TIME DESC FETCH FIRST 1 ROWS ONLY",
        null,
        resultSet -> resultSet.next() ? resultSet.getInt("MT_TRAN_ID") : 0);
  }

  public String getValueFromMTTranFR(int tranId, String columnName) {
    return query(
        "SELECT * FROM MT_TRAN WHERE MT_TRAN_ID = ?",
        statement -> statement.setInt(1, tranId),
        resultSet -> resultSet.next() ? Objects.toString(resultSet.getString(columnName), "") : "");
  }

  public int getTranIdByOrderAndTranType(String order, String tranType) {
    return query(
        "SELECT MAX(TRAN_ID) AS TRAN_ID FROM TRAN WHERE ORDER_ID = ? AND OP_TYPE = ?",
        statement -> {
          statement.setString(1, order);
          statement.setString(2, tranType);
        },
        resultSet -> resultSet.next() ? resultSet.getInt("TRAN_ID") : 0);
  }

  public int getTranIdByRrn(String rrn) {
    return query(
        "SELECT MAX(TRAN_ID) AS TRAN_ID FROM TRAN WHERE RRN = ?",
        statement -> statement.setString(1, rrn),
        resultSet -> resultSet.next() ? resultSet.getInt("TRAN_ID") : 0);
  }

  public int getTranIdByApprovalCode(String approvalCode) {
    return query(
        "SELECT MAX(MT_TRAN_ID) AS MT_TRAN_ID FROM MT_TRAN WHERE APPROVAL_CODE = ?",
        statement -> statement.setString(1, approvalCode),
        resultSet -> resultSet.next() ? resultSet.getInt("MT_TRAN_ID") : 0);
  }

  public int getParentTranId(String order) {
    return query(
        "SELECT MIN(TRAN_ID) AS TRAN_ID FROM TRAN WHERE ORDER_ID = ?",
        statement -> statement.setString(1, order),
        resultSet -> resultSet.next() ? resultSet.getInt("TRAN_ID") : 0);
  }

  public String getMessageISO(Integer tranId) {
    String auditEntityId = getAuditEntityId(tranId);
    if (auditEntityId == null) {
      return null;
    }
    return query(
        "SELECT MESSAGE FROM AUDIT_LOG WHERE AUDIT_ENTITY_ID = ?",
        statement -> statement.setLong(1, Long.parseLong(auditEntityId)),
        resultSet -> resultSet.next() ? getClobString(resultSet.getClob("MESSAGE")) : null);
  }

  public String getAuditEntityId(Integer tranId) {
    return query(
        "SELECT AUDIT_ENTITY_ID FROM TRAN WHERE TRAN_ID = ?",
        statement -> statement.setInt(1, tranId),
        resultSet -> resultSet.next() ? resultSet.getString("AUDIT_ENTITY_ID") : null);
  }

  private String getClobString(Clob clob) {
    if (clob == null) {
      return null;
    }
    StringBuilder builder = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(clob.getCharacterStream())) {
      String line;
      while ((line = reader.readLine()) != null) {
        builder.append(line);
      }
    } catch (IOException | SQLException exception) {
      throw new DatabaseAccessException("Unable to read CLOB", exception);
    }
    return builder.toString();
  }

  public String getBatchId(String rrn) {
    return query(
        "SELECT BATCH_ID FROM TRAN WHERE RRN = ?",
        statement -> statement.setString(1, rrn),
        resultSet -> resultSet.next() ? resultSet.getString("BATCH_ID") : null);
  }

  public String getBatchIdFromBatch(String filename) {
    return query(
        "SELECT BATCH_ID FROM BATCH WHERE FILENAME = ?",
        statement -> statement.setString(1, filename),
        resultSet -> resultSet.next() ? resultSet.getString("BATCH_ID") : null);
  }

  public String getValueFromTRAN(int tranId, String tagName) {
    return query(
        "SELECT * FROM TRAN WHERE TRAN_ID = ?",
        statement -> statement.setInt(1, tranId),
        resultSet ->
            resultSet.next()
                ? switch (tagName) {
                  case "CVResult" -> resultSet.getString("CVC_RESULT");
                  case "MTI" -> resultSet.getString("MTI");
                  case "ECI" -> resultSet.getString("ECI");
                  case "PAResStatus" -> resultSet.getString("PARES_STATUS");
                  case "PA_ECI" -> resultSet.getString("PA_ECI");
                  case "POS_CODE" -> resultSet.getString("POS_CONDITION_CODE");
                  case "PSTranID" -> resultSet.getString("PS_TRAN_ID");
                  case "ApprovalCode" -> resultSet.getString("APPROVAL_CODE");
                  case "Rrn" -> resultSet.getString("RRN");
                  case "TranCode" -> resultSet.getString("TRAN_CODE_ID");
                  case "OP_TYPE" -> resultSet.getString("OP_TYPE");
                  case "RevFlag" -> resultSet.getString("REVERSAL_FLAG");
                  case "Batch" -> resultSet.getString("BATCH_ID");
                  case "OrderID" -> resultSet.getString("ORDER_ID");
                  case "TotalAmount" -> resultSet.getString("TRAN_AMOUNT");
                  case "STAN" -> resultSet.getString("STAN");
                  case "Checkout" -> resultSet.getString("CHECKOUT");
                  case "AddendumData" -> resultSet.getString("ADDENDUM_DATA");
                  case "Version3DS" -> resultSet.getString("VERSION_3DS");
                  case "RECURRENT" -> resultSet.getString("RECURRENT");
                  case "Installment" -> resultSet.getString("INSTALLMENT");
                  case "FEE" -> resultSet.getString("FEE");
                  default -> null;
                }
                : null);
  }

  public void setMerchantFilter(int filterId, String value) {
    update(
        "UPDATE FILTER SET STATUS = ? WHERE FILTER_ID = ?",
        statement -> {
          statement.setString(1, value);
          statement.setInt(2, filterId);
        });
  }

  public String getAddendumData(int tranId) {
    return query(
        "SELECT D_CLASS FROM TRAN_ADDENDUM_DATA WHERE TRAN_ID = ?",
        statement -> statement.setInt(1, tranId),
        resultSet -> {
          StringBuilder builder = new StringBuilder();
          while (resultSet.next()) {
            if (builder.length() > 0) {
              builder.append("; ");
            }
            builder.append(resultSet.getString("D_CLASS"));
          }
          return builder.toString();
        });
  }

  public String getCheckoutData(int tranId) {
    return query(
        "SELECT CALL_ID FROM TRAN_CHECKOUT WHERE TRAN_ID = ?",
        statement -> statement.setInt(1, tranId),
        resultSet -> {
          StringBuilder builder = new StringBuilder();
          while (resultSet.next()) {
            if (builder.length() > 0) {
              builder.append("; ");
            }
            builder.append(resultSet.getString("CALL_ID"));
          }
          return builder.toString();
        });
  }

  public String getTran3dsData(int tranId) {
    return query(
        "SELECT DS_TRAN_ID FROM TRAN_3DS_V2_DATA WHERE TRAN_ID = ?",
        statement -> statement.setInt(1, tranId),
        resultSet -> {
          StringBuilder builder = new StringBuilder();
          while (resultSet.next()) {
            if (builder.length() > 0) {
              builder.append("; ");
            }
            builder.append(resultSet.getString("DS_TRAN_ID"));
          }
          return builder.toString();
        });
  }

  public boolean hasAddendumData(int tranId, String tag) {
    return query(
        "SELECT D_VALUE FROM TRAN_ADDENDUM_DATA WHERE TRAN_ID = ? AND D_CLASS = ?",
        statement -> {
          statement.setInt(1, tranId);
          statement.setString(2, tag);
        },
        ResultSet::next);
  }

  public boolean hasInstallmentData(int tranId) {
    return query(
        "SELECT FLD_104 FROM TRAN_INSTALLMENT WHERE TRAN_ID = ?",
        statement -> statement.setInt(1, tranId),
        resultSet -> resultSet.next() && resultSet.getString("FLD_104") != null);
  }

  public String getValueFromRecurrent(int tranId) {
    return query(
        "SELECT INCOMING_TID FROM TRAN_RECURRENT WHERE TRAN_ID = ?",
        statement -> statement.setInt(1, tranId),
        resultSet -> resultSet.next() ? resultSet.getString("INCOMING_TID") : null);
  }

  public void setMerchantAttribute(int merchantId, Attributes attribute, String value) {
    update(
        "UPDATE MERCHANT_ATTR SET ATTR_VALUE = ? WHERE MERCHANT_ID = ? AND ATTR_NAME = ?",
        statement -> {
          statement.setString(1, value);
          statement.setInt(2, merchantId);
          statement.setString(3, attribute.toString());
        });
  }

  public void setPropertyValue(String propertyKey, String value) {
    update(
        "UPDATE PROPERTY_VALUE SET PROPERTY_VALUE = ? WHERE PROPERTY_KEY = ?",
        statement -> {
          statement.setString(1, value);
          statement.setString(2, propertyKey);
        });
  }

  public boolean isStatusFilterCorrect(int filterId, String expectedValue) {
    String value =
        query(
            "SELECT STATUS FROM FILTER WHERE FILTER_ID = ?",
            statement -> statement.setInt(1, filterId),
            resultSet -> resultSet.next() ? resultSet.getString("STATUS") : null);
    return Objects.equals(value, expectedValue);
  }

  public String getMccForMerchant(String merchant) {
    return query(
        "SELECT MCC FROM MERCHANT WHERE MERCHANT_NAME = ?",
        statement -> statement.setString(1, merchant),
        resultSet -> resultSet.next() ? resultSet.getString("MCC") : null);
  }

  public void setMccForMerchant(String merchant, String mcc) {
    update(
        "UPDATE MERCHANT SET MCC = ? WHERE MERCHANT_NAME = ?",
        statement -> {
          statement.setString(1, mcc);
          statement.setString(2, merchant);
        });
  }

  public String getValueFromMTTran(String rrn, String tagName) {
    return query(
        "SELECT * FROM MT_TRAN WHERE RRN = ?",
        statement -> statement.setString(1, rrn),
        resultSet ->
            resultSet.next()
                ? switch (tagName) {
                  case "CVResult" -> resultSet.getString("CVC_RERULT");
                  case "ECI" -> resultSet.getString("ECI");
                  case "PAResStatus" -> resultSet.getString("PA_STATUS");
                  case "PA_ECI" -> resultSet.getString("PA_ECI");
                  case "Version3DS" -> resultSet.getString("VERSION_3DS");
                  case "POS_CONDITION_CODE", "POS_CODE" ->
                      resultSet.getString("POS_CONDITION_CODE");
                  case "RRN" -> resultSet.getString("RRN");
                  case "TranCode" -> resultSet.getString("TRAN_CODE_ID");
                  case "Code" -> resultSet.getString("ACTION_CODE");
                  case "MCC" -> resultSet.getString("CL_MCC");
                  case "Reversed" -> resultSet.getString("REVERSED");
                  case "TranAmount" -> resultSet.getString("TRAN_AMOUNT");
                  case "MERCHANT_ID" -> resultSet.getString("MERCHANT_ID");
                  default -> null;
                }
                : null);
  }

  public String getValueFromMTTranFunding(String rrn, String tagName) {
    return query(
        "SELECT * FROM MT_TRAN WHERE RRN = ? AND TRAN_TYPE IN ('F', 'P')",
        statement -> statement.setString(1, rrn),
        resultSet ->
            resultSet.next()
                ? switch (tagName) {
                  case "TranType" -> resultSet.getString("TRAN_TYPE");
                  case "ActionCode" -> resultSet.getString("ACTION_CODE");
                  case "TranAmount" -> resultSet.getString("TRAN_AMOUNT");
                  case "CardNumber" -> resultSet.getString("CARD_NUMBER");
                  case "ApprovalCode" -> resultSet.getString("APPROVAL_CODE");
                  case "RRN" -> resultSet.getString("RRN");
                  default -> null;
                }
                : null);
  }

  public String getValueFromMTTranPayment(String rrn, String tagName) {
    return query(
        "SELECT * FROM MT_TRAN WHERE RRN = ? AND TRAN_TYPE = 'P'",
        statement -> statement.setString(1, rrn),
        resultSet ->
            resultSet.next()
                ? switch (tagName) {
                  case "TranType" -> resultSet.getString("TRAN_TYPE");
                  case "ActionCode" -> resultSet.getString("ACTION_CODE");
                  case "TranAmount" -> resultSet.getString("TRAN_AMOUNT");
                  default -> null;
                }
                : null);
  }

  public String getValueFromMTTranReversal(String rrn, String tagName) {
    return query(
        "SELECT * FROM MT_TRAN WHERE RRN = ? AND TRAN_TYPE = 'R'",
        statement -> statement.setString(1, rrn),
        resultSet ->
            resultSet.next()
                ? switch (tagName) {
                  case "TranType" -> resultSet.getString("TRAN_TYPE");
                  case "ActionCode" -> resultSet.getString("ACTION_CODE");
                  case "TranAmount" -> resultSet.getString("TRAN_AMOUNT");
                  default -> null;
                }
                : null);
  }

  public String getLastRequestId() {
    return query(
        "SELECT MT_REQUESTS_ID FROM MT_TRAN ORDER BY TRAN_TIME DESC FETCH FIRST 1 ROWS ONLY",
        null,
        resultSet -> resultSet.next() ? resultSet.getString("MT_REQUESTS_ID") : null);
  }

  public String getValueFromMTTranByApprovalCode(String approvalCode, String tagName) {
    return query(
        "SELECT * FROM MT_TRAN WHERE APPROVAL_CODE = ?",
        statement -> statement.setString(1, approvalCode),
        resultSet ->
            resultSet.next()
                ? switch (tagName) {
                  case "CVResult" -> resultSet.getString("CVC_RERULT");
                  case "ECI" -> resultSet.getString("ECI");
                  case "PAResStatus" -> resultSet.getString("PA_STATUS");
                  case "PA_ECI" -> resultSet.getString("PA_ECI");
                  case "Version3DS" -> resultSet.getString("VERSION_3DS");
                  case "POS_CONDITION_CODE", "POS_CODE" ->
                      resultSet.getString("POS_CONDITION_CODE");
                  case "RRN" -> resultSet.getString("RRN");
                  case "TranCode" -> resultSet.getString("TRAN_CODE_ID");
                  case "Code" -> resultSet.getString("ACTION_CODE");
                  case "MCC" -> resultSet.getString("CL_MCC");
                  case "Reversed" -> resultSet.getString("REVERSED");
                  default -> null;
                }
                : null);
  }

  public String getRrnFromMTTran(String requestId, String tranType) {
    return query(
        "SELECT RRN FROM MT_TRAN WHERE MT_REQUESTS_ID = ? AND TRAN_TYPE = ?",
        statement -> {
          statement.setString(1, requestId);
          statement.setString(2, tranType);
        },
        resultSet -> resultSet.next() ? resultSet.getString("RRN") : null);
  }

  public String getRequestIdMT(int merchantId) {
    return query(
        "SELECT MAX(MT_REQUESTS_ID) FROM MT_REQUESTS WHERE MERCHANT_ID = ?",
        statement -> statement.setInt(1, merchantId),
        resultSet -> resultSet.next() ? resultSet.getString(1) : null);
  }

  public int getTranIdMT(String rrn) {
    return query(
        "SELECT MT_TRAN_ID FROM MT_TRAN WHERE RRN = ?",
        statement -> statement.setString(1, rrn),
        resultSet -> resultSet.next() ? resultSet.getInt("MT_TRAN_ID") : 0);
  }

  public String getTranMT3dsData(String rrn) {
    return query(
        "SELECT DS_TRAN_ID FROM MT_TRAN_3DS_DATA WHERE RRN = ?",
        statement -> statement.setString(1, rrn),
        resultSet -> {
          StringBuilder builder = new StringBuilder();
          while (resultSet.next()) {
            if (builder.length() > 0) {
              builder.append("; ");
            }
            builder.append(resultSet.getString("DS_TRAN_ID"));
          }
          return builder.toString();
        });
  }

  public void setNewTranTime(int tranId) {
    update(
        "UPDATE TRAN SET TRAN_TIME = ? WHERE TRAN_ID = ?",
        statement -> {
          statement.setTimestamp(1, Timestamp.from(Instant.now()));
          statement.setInt(2, tranId);
        });
  }

  public String verifyOrders(String approvalCode, String rrn) {
    return query(
        "SELECT ORDER_ID FROM TRAN WHERE RRN = ? AND APPROVAL_CODE = ?",
        statement -> {
          statement.setString(1, rrn);
          statement.setString(2, approvalCode);
        },
        resultSet -> {
          if (resultSet.next()) {
            return resultSet.getString("ORDER_ID");
          }
          throw new DatabaseAccessException(
              "No entry found with RRN=" + rrn + " and approval=" + approvalCode);
        });
  }

