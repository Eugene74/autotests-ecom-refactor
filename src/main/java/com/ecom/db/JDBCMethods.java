package com.ecom.db;

import com.ecom.type.Attributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.*;

public class JDBCMethods extends JDBCConnection {

    public  static String getNFileNameByTran(int tran_id){
        int batch_id = 0;
        String select = "SELECT BATCH_ID FROM TRAN WHERE TRAN_ID = ? ";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setInt(1, tran_id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                batch_id = rs.getInt("BATCH_ID");//getInt("TRAN_ID");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return getNFileNameByBatch(batch_id);
    }

    public  static String getNFileNameByBatch(int batch_id){
        String filename = "";
        String select = "SELECT FILENAME FROM BATCH WHERE BATCH_ID = ? ";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setInt(1, batch_id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                filename = rs.getString("FILENAME");//getInt("TRAN_ID");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return filename;
    }

    public  static String getAcquirerIDByBank(String bank){
        int acquirer_id = 0;
        String select = "SELECT ACQUIRER_ID FROM BANK WHERE BANKNAME = ? ";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setString(1, bank);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                acquirer_id = rs.getInt("ACQUIRER_ID"); //getInt("TRAN_ID");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Integer.toString(acquirer_id);
    }

    public static int getTranIdByOrder1(String order) {
        int tranID = 0;
        String select = "SELECT MAX(TRAN_ID) as TRAN_ID FROM TRAN WHERE ORDER_ID = ?";

        // Очистка значения order от лишних символов
        order = order.replace("Order ID", "").trim().replace("\n", "").replace("\r", "");
        System.out.println("Executing query for ORDER_ID: [" + order + "]");

        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setString(1, order);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                tranID = rs.getInt("TRAN_ID");
            } else {
                System.out.println("No TRAN_ID found for ORDER_ID: [" + order + "]");
            }
        } catch (Exception e) {
            System.out.println("Error while executing query: " + e.getMessage());
        }
        return tranID;
    }


    public static int getTranIdByOrder(String order) {
        int tranID = 0;
        String select = "SELECT MAX(TRAN_ID) as TRAN_ID FROM TRAN WHERE ORDER_ID = ?";

        // Видалення префікса "Order №" з order
        if (order.startsWith("Order № ")) {
            order = order.substring(8).trim(); // Видаляємо перші 8 символів
        }

        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setString(1, order);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                tranID = rs.getInt("TRAN_ID");
            }
        } catch (Exception e) {
            System.out.println("Error while getting TranId: " + e.getMessage());
        }

        return tranID;
    }


    public static int getTranIdByOrderFR(String order) {
        String select = "SELECT MT_TRAN_ID, TRAN_TYPE, RRN, ACTION_CODE, APPROVAL_CODE, CL_TYPE " +
                "FROM MT_TRAN " +
                "WHERE TRAN_TYPE = 'A' " +
                "ORDER BY TRAN_TIME DESC FETCH FIRST 1 ROWS ONLY";
        int refundIdFromDB = 0;
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(select)) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                refundIdFromDB = rs.getInt("MT_TRAN_ID");
                String tranType = rs.getString("TRAN_TYPE");
                String rrn = rs.getString("RRN");
                String actionCode = rs.getString("ACTION_CODE");
                String approvalCode = rs.getString("APPROVAL_CODE");
                String clType = rs.getString("CL_TYPE");

                // Логирование для проверки значений
                System.out.println("TRAN_TYPE: " + tranType);
                System.out.println("RRN: " + rrn);
                System.out.println("ACTION_CODE: " + actionCode);
                System.out.println("APPROVAL_CODE: " + approvalCode);
                System.out.println("CL_TYPE: " + clType);
            }
        } catch (Exception e) {
            System.out.println("Error while executing SQL query: " + e.getMessage());
        }
        return refundIdFromDB;
    }

    public static String getValueFromMTTranFR(int tranId, String columnName) {
        String value = "";
        String query = "SELECT * FROM MT_TRAN WHERE MT_TRAN_ID = ?";
        try (Connection dbConnection = getDBConnection();
             PreparedStatement statement = dbConnection.prepareStatement(query)) {
            statement.setInt(1, tranId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                value = rs.getString(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value != null ? value : "";
    }


    public static int getTranIdByOrderAndTranType(String order, String tranTpe) {
        int tranID = 0;
        String select = "SELECT MAX(TRAN_ID) as TRAN_ID  FROM TRAN WHERE ORDER_ID = ? AND OP_TYPE = ?";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setString(1, order);
            statement.setString(2, tranTpe);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                tranID = rs.getInt("TRAN_ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tranID;
    }






    public static int getTranIdByRrn(String rrn) {
        int tranID = 0;
        String select = "SELECT MAX(TRAN_ID) as TRAN_ID  FROM TRAN WHERE RRN = ? ";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setString(1, rrn);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                tranID = rs.getInt("TRAN_ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tranID;
    }

    public static int getTranIdByApprovalCode(String rrn) {
        int tranID = 0;
        String select = "SELECT MAX(MT_TRAN_ID) as MT_TRAN_ID  FROM MT_TRAN WHERE APPROVAL_CODE = ? ";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setString(1, rrn);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                tranID = rs.getInt("TRAN_ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return tranID;
    }


    public static int getParentTranId(String order) {
        int parentTranId = 0;
        String select = "SELECT MIN(TRAN_ID) as TRAN_ID FROM TRAN WHERE ORDER_ID = ?";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setString(1, order);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                parentTranId = rs.getInt("TRAN_ID");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return parentTranId;
    }

    public static String getMessageISO(Integer tranId) {
        String message = null;
        String select = "SELECT * FROM AUDIT_LOG WHERE AUDIT_ENTITY_ID = ?";
        try (Connection dbConnection = getDBConnection();
             PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setLong(1, Long.valueOf(getAuditEntityId(tranId)));
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                message = getClobString(rs.getClob("MESSAGE"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return message;
    }

    public static String getAuditEntityId(Integer tranId) {
        String entityId = null;
        String select = "SELECT AUDIT_ENTITY_ID FROM TRAN WHERE TRAN_ID = ?";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setInt(1, tranId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                entityId = String.valueOf(rs.getLong(1));
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return entityId;
    }

    public static String getClobString(Clob clob) {
        BufferedReader stringReader = null;
        try {
            stringReader = new BufferedReader(clob.getCharacterStream());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String singleLine = null;
        StringBuffer strBuff = new StringBuffer();
        try {
            while ((singleLine = stringReader.readLine()) != null) {
                strBuff.append(singleLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strBuff.toString();
    }

    public static String getBatchId(String rrn) {
        String batchId = null;
        String select = "SELECT * FROM TRAN WHERE RRN = ?";
        try (Connection dbConnection = getDBConnection();
             PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setString(1, rrn);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                batchId = rs.getString("BATCH_ID");
            }
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        return batchId;
    }

    public static String getBatchIdFromBatch(String file) {
        String batchId = null;
        String select = "SELECT * FROM BATCH WHERE FILENAME = ?";
        try (Connection dbConnection = getDBConnection();
             PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setString(1, file);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                batchId = rs.getString("BATCH_ID");
            }
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        return batchId;
    }


    public static String getValueFromTRAN(int tranId, String tagName) {
        String value = "";
        String select = "SELECT * FROM TRAN WHERE TRAN_ID = ?";
        try (
                Connection dbConnection = getDBConnection();
                PreparedStatement statement = dbConnection.prepareStatement(select)
        ) {
            statement.setInt(1, tranId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                switch (tagName) {
                    case "CVResult":
                        value = rs.getString("CVC_RESULT");
                        break;
                    case "MTI":
                        value = rs.getString("MTI");
                        break;
                    case "ECI":
                        value = rs.getString("ECI");
                        break;
                    case "PAResStatus":
                        value = rs.getString("PARES_STATUS");
                        break;
                    case "PA_ECI":
                        value = rs.getString("PA_ECI");
                        break;
                    case "POS_CODE":
                        value = rs.getString("POS_CONDITION_CODE");
                        break;
                    case "PSTranID":
                        value = rs.getString("PS_TRAN_ID");
                        break;
                    case "ApprovalCode":
                        value = rs.getString("APPROVAL_CODE");
                        break;
                    case "Rrn":
                        value = rs.getString("RRN");
                        break;
                    case "TranCode":
                        value = rs.getString("TRAN_CODE_ID");
                        break;
                    case "OP_TYPE":
                        value = rs.getString("OP_TYPE");
                        break;
                    case "RevFlag":
                        value = rs.getString("REVERSAL_FLAG");
                        break;
                    case "Batch":
                        value = rs.getString("BATCH_ID");
                        break;
                    case "OrderID":
                        value = rs.getString("ORDER_ID");
                        break;
                    case "TotalAmount":
                        value = rs.getString("TRAN_AMOUNT");
                        break;
                    case "STAN":
                        value = rs.getString("STAN");
                        break;
                    case "Checkout":
                        value = rs.getString("CHECKOUT");
                        break;
                    case "AddendumData":
                        value = rs.getString("ADDENDUM_DATA");
                        break;
                    case "Version3DS":
                        value = rs.getString("VERSION_3DS");
                        break;
                    case "RECURRENT":
                        value = rs.getString("RECURRENT");
                        break;
                    case "Installment":
                        value = rs.getString("INSTALLMENT");
                        break;
                    case "FEE":
                        value = rs.getString("FEE");
                        break;
                    default:
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println(">>> ERROR: value is null");
        }
        return value;
    }


    public static void setMerchantFilter(int filtID, String value) {
        String query = "UPDATE FILTER SET STATUS = ? WHERE FILTER_ID=?";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(query)) {
            dbConnection.setAutoCommit(false);
            statement.setString(1, value);
            statement.setInt(2, filtID);
            statement.executeUpdate();
            dbConnection.commit();
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }


    public static String getAddendumData(int tranId) throws SQLException {
        String value = "";
        String select = "SELECT * FROM TRAN_ADDENDUM_DATA WHERE TRAN_ID =  ? ";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setInt(1, tranId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                value = value + rs.getString("D_Class") + "; ";
            }
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        return value;
    }

    public static String getCheckoutData(int tranId) {
        String value = "";
        String select = "SELECT * FROM TRAN_CHECKOUT WHERE TRAN_ID =  ?";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setInt(1, tranId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                value = value + rs.getString("CALL_ID") + "; ";
            }
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        return value;
    }

    public static String getTran3dsData(int tranId) {
        String value = "";
        String select = "SELECT * FROM TRAN_3DS_V2_DATA WHERE TRAN_ID =  ?";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setInt(1, tranId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                value = value + rs.getString("DS_TRAN_ID") + "; ";
            }
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        return value;
    }

    public static Boolean getAddendumData(int tranId, String tag) {
        Boolean value = false;
        String select = "SELECT * FROM TRAN_ADDENDUM_DATA WHERE TRAN_ID =  ? AND D_CLASS = ?";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setInt(1, tranId);
            statement.setString(2, tag);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                if (rs.getString("D_VALUE") != null)
                    value = true;
            }
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        return value;
    }


    public static Boolean getInstallmentData(int tranId) {
        Boolean field104 = false;
        String select = "SELECT * FROM TRAN_INSTALLMENT WHERE TRAN_ID =  ?";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setInt(1, tranId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                if (rs.getString("FLD_104") != null)
                    field104 = true;
            }
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        return field104;
    }

    public static String getValueFromRecurrent(int tranId) {
        String incomingTID = null;
        String select = "SELECT INCOMING_TID FROM TRAN_RECURRENT WHERE TRAN_ID =  ?";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setInt(1, tranId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                incomingTID = String.valueOf(rs.getString(1));
            }
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        return incomingTID;
    }

    public static void setMerchantAtt(int merchID, Attributes attribute, String value) {
        String query = "UPDATE MERCHANT_ATTR SET ATTR_VALUE = ? WHERE MERCHANT_ID=? AND ATTR_NAME=?";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(query)) {
            dbConnection.setAutoCommit(false);
            statement.setString(1, value);
            statement.setInt(2, merchID);
            statement.setString(3, attribute.toString());
            statement.executeUpdate();
            dbConnection.commit();
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    public static void setPropertyURLs(String propertyKey, String value) {
        String query = "UPDATE PROPERTY_VALUE SET PROPERTY_VALUE = ? WHERE PROPERTY_KEY=?";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(query)) {
            dbConnection.setAutoCommit(false);
            statement.setString(1, value);
            statement.setString(2, propertyKey);
            statement.executeUpdate();
            dbConnection.commit();
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    public static boolean isStatusFilterCorrect(int filtID, String value) {
        boolean isStatusCorrect = false;
        String query = "SELECT STATUS FROM FILTER WHERE FILTER_ID=?";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(query)) {
            dbConnection.setAutoCommit(false);
            statement.setInt(1, filtID);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                if (rs.getString("STATUS").equals(value))
                    isStatusCorrect = true;
            }
            dbConnection.commit();
        } catch (Exception ex) {
            ex.getStackTrace();
        }
        return isStatusCorrect;
    }


    public static String getMCCMerchant(String merchant) {
        String mcc = null;
        String select = "SELECT MCC FROM MERCHANT WHERE MERCHANT_CODE = ?";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setString(1, merchant);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                mcc = rs.getString("MCC");
            }
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }
        return mcc;
    }

    public static void setMCCMerchant(String merchant, String mcc) {
        String query = "UPDATE MERCHANT SET MCC = ? WHERE MERCHANT_CODE = ?";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(query)) {
            dbConnection.setAutoCommit(false);
            statement.setString(2, mcc);
            statement.setString(1, merchant);
            statement.executeUpdate();
            dbConnection.commit();
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    //******************************************************************************************************************
    //FOR MT
    //******************************************************************************************************************
    public static String getValueFromMTTran(String rrn, String tagName) {
        String value = null;
        String select = "SELECT * FROM MT_TRAN WHERE RRN =  ?";
        try (Connection dbConnection = getDBConnection();
             PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setString(1, rrn);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                switch (tagName) {
                    case "CVResult":
                        value = rs.getString("CVC_RERULT");
                        break;
                    case "ECI":
                        value = rs.getString("ECI");
                        break;
                    case "PAResStatus":
                        value = rs.getString("PA_STATUS");
                        break;
                    case "PA_ECI":
                        value = rs.getString("PA_ECI");
                        break;
                    case "Version3DS":
                        value = rs.getString("VERSION_3DS");
                        break;
                    case "POS_CONDITION_CODE":
                        value = rs.getString("POS_CONDITION_CODE");
                        break;
                    case "ApprovalCode":
                        value = rs.getString("APPROVAL_CODE");
                        break;
                    case "Code":
                        value = rs.getString("ACTION_CODE");
                        break;
                    case "MCC":
                        value = rs.getString("CL_MCC");
                        break;
                    case "Reversed":
                        value = rs.getString("REVERSED");
                        break;
                    default:
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return value;
    }

    public static String getValueFromMTTranFunding(String rrn, String tagName) {
        return getValueFromMTTranWithType(rrn, "F", tagName);
    }

    public static String getValueFromMTTranPayment(String rrn, String tagName) {
        return getValueFromMTTranWithType(rrn, "P", tagName);
    }

    public static String getValueFromMTTranReversal(String rrn, String tagName) {
        return getValueFromMTTranWithType(rrn, "R", tagName);
    }

    // Внутрішній універсальний метод
    private static String getValueFromMTTranWithType(String rrn, String tranType, String tagName) {
        String value = null;
        String select = "SELECT * FROM MT_TRAN WHERE RRN = ? AND TRAN_TYPE = ?";
        try (Connection dbConnection = getDBConnection();
             PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setString(1, rrn);
            statement.setString(2, tranType);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                switch (tagName) {
                    case "CVResult":
                        value = rs.getString("CVC_RERULT");
                        break;
                    case "ECI":
                        value = rs.getString("ECI");
                        break;
                    case "PAResStatus":
                        value = rs.getString("PA_STATUS");
                        break;
                    case "PA_ECI":
                        value = rs.getString("PA_ECI");
                        break;
                    case "Version3DS":
                        value = rs.getString("VERSION_3DS");
                        break;
                    case "POS_CONDITION_CODE":
                        value = rs.getString("POS_CONDITION_CODE");
                        break;
                    case "ApprovalCode":
                        value = rs.getString("APPROVAL_CODE");
                        break;
                    case "Code":
                        value = rs.getString("ACTION_CODE");
                        break;
                    case "MCC":
                        value = rs.getString("CL_MCC");
                        break;
                    case "Reversed":
                        value = rs.getString("REVERSED");
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getLastRequestId() {
        String requestId = null;
        String select = "SELECT MT_REQUESTS_ID FROM MT_TRAN ORDER BY TRAN_TIME DESC FETCH FIRST 1 ROWS ONLY";
        try (Connection dbConnection = getDBConnection();
             PreparedStatement statement = dbConnection.prepareStatement(select)) {
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                requestId = rs.getString("MT_REQUESTS_ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requestId;
    }


    public static String getValueFromMTTranByAprCode(String apprCode, String tagName) {
        String value = null;
        String select = "SELECT * FROM MT_TRAN WHERE APPROVAL_CODE =  ?";
        try (Connection dbConnection = getDBConnection();
             PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setString(1, apprCode);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                switch (tagName) {
                    case "CVResult":
                        value = rs.getString("CVC_RERULT");
                        break;
                    case "ECI":
                        value = rs.getString("ECI");
                        break;
                    case "PAResStatus":
                        value = rs.getString("PA_STATUS");
                        break;
                    case "PA_ECI":
                        value = rs.getString("PA_ECI");
                        break;
                    case "Version3DS":
                        value = rs.getString("VERSION_3DS");
                        break;
                    case "POS_CONDITION_CODE":
                        value = rs.getString("POS_CONDITION_CODE");
                        break;
                    case "RRN":
                        value = rs.getString("RRN");
                        break;
                    case "TranCode":
                        value = rs.getString("TRAN_CODE_ID");
                        break;
                    case "Code":
                        value = rs.getString("ACTION_CODE");
                        break;
                    case "MCC":
                        value = rs.getString("CL_MCC");
                        break;
                    case "Reversed":
                        value = rs.getString("REVERSED");
                        break;
                    default:
                        break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return value;
    }


    public static String getRrnFromMTTran(String receipt, String tran_type) {
        String rrn = null;
        String select = "SELECT * FROM MT_TRAN WHERE MT_REQUESTS_ID =  ? AND TRAN_TYPE = ?";

        try (Connection dbConnection = getDBConnection();
             PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setString(1, receipt);
            statement.setString(2, tran_type);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                rrn = rs.getString("RRN");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return rrn;
    }

    public static String getRequestIdMT(int merchantId) {
        String requestId = "";
        String select = "SELECT MAX(MT_REQUESTS_ID) FROM MT_REQUESTS WHERE MERCHANT_ID =  ?";

        try (Connection dbConnection = getDBConnection();
             PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setInt(1, merchantId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                requestId = rs.getString(1);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return requestId;
    }

    public static int getTranIdMT(String rrn) {
        int value = 0;
        String select = "SELECT * FROM MT_TRAN WHERE RRN =  ?";

        try (Connection dbConnection = getDBConnection();
             PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setString(1, rrn);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                value = rs.getInt("MT_TRAN_ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return value;
    }

    public static String getTranMT3dsData(String rrn) {
        int tranId = getTranIdMT(rrn);
        String value = "";
        String select = "SELECT * FROM MT_TRAN_3DS_V2_DATA WHERE MT_TRAN_ID =  ?";
        try (Connection dbConnection = getDBConnection(); PreparedStatement statement = dbConnection.prepareStatement(select)) {
            statement.setInt(1, tranId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                value = rs.getString("DS_TRAN_ID") + "; ";
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return value;
    }

    // pld-1562 - set new time for a tran (-1 day)
    public static void setNewTranTime(int tranId) {
        String query = "UPDATE TRAN T SET T.TRAN_TIME = T.TRAN_TIME + INTERVAL '-1' DAY WHERE TRAN_ID = ?";
        try (Connection dbConnection = getDBConnection();
             PreparedStatement statement = dbConnection.prepareStatement(query)) {
            dbConnection.setAutoCommit(false);
            statement.setInt(1, tranId);
            statement.executeUpdate();
            dbConnection.commit();
        } catch (Exception ex) {
            ex.getStackTrace();
        }
    }

    public static String verifyOrders(String approvalCode, String rrn) throws Exception {
        String query = "SELECT ORDER_ID FROM TRAN WHERE RRN = ? AND APPROVAL_CODE = ?";
        try (Connection dbConnection = getDBConnection();
             PreparedStatement statement = dbConnection.prepareStatement(query)) {
                statement.setString(1, rrn);
                statement.setString(2, approvalCode);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    return resultSet.getString("ORDER_ID");
                } else {
                    throw new IllegalArgumentException("No entry found with RRN: " + rrn + " and Approval Code: " + approvalCode);
                }
            }
        }
}