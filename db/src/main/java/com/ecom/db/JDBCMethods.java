package com.ecom.db;

import com.ecom.api.type.Attributes;
import com.ecom.db.service.DatabaseServiceFactory;
import com.ecom.db.service.PaymentService;

public final class JDBCMethods {

    private static final PaymentService PAYMENT_SERVICE =
            DatabaseServiceFactory.getDatabaseService().payments();

    private JDBCMethods() {
    }

    public static String getNFileNameByTran(int tranId) {
        return PAYMENT_SERVICE.getNFileNameByTran(tranId);
    }

    public static String getNFileNameByBatch(int batchId) {
        return PAYMENT_SERVICE.getNFileNameByBatch(batchId);
    }

    public static String getAcquirerIDByBank(String bank) {
        return PAYMENT_SERVICE.getAcquirerIDByBank(bank);
    }

    public static int getTranIdByOrder1(String order) {
        return PAYMENT_SERVICE.getTranIdByOrder1(order);
    }

    public static int getTranIdByOrder(String order) {
        return PAYMENT_SERVICE.getTranIdByOrder(order);
    }

    public static int getTranIdByOrderFR(String order) {
        return PAYMENT_SERVICE.getTranIdByOrderFR(order);
    }

    public static String getValueFromMTTranFR(int tranId, String columnName) {
        return PAYMENT_SERVICE.getValueFromMTTranFR(tranId, columnName);
    }

    public static int getTranIdByOrderAndTranType(String order, String tranType) {
        return PAYMENT_SERVICE.getTranIdByOrderAndTranType(order, tranType);
    }

    public static int getTranIdByRrn(String rrn) {
        return PAYMENT_SERVICE.getTranIdByRrn(rrn);
    }

    public static int getTranIdByApprovalCode(String approvalCode) {
        return PAYMENT_SERVICE.getTranIdByApprovalCode(approvalCode);
    }

    public static int getParentTranId(String order) {
        return PAYMENT_SERVICE.getParentTranId(order);
    }

    public static String getMessageISO(Integer tranId) {
        return PAYMENT_SERVICE.getMessageISO(tranId);
    }

    public static String getAuditEntityId(Integer tranId) {
        return PAYMENT_SERVICE.getAuditEntityId(tranId);
    }

    public static String getBatchId(String rrn) {
        return PAYMENT_SERVICE.getBatchId(rrn);
    }

    public static String getBatchIdFromBatch(String filename) {
        return PAYMENT_SERVICE.getBatchIdFromBatch(filename);
    }

    public static String getValueFromTRAN(int tranId, String tagName) {
        return PAYMENT_SERVICE.getValueFromTRAN(tranId, tagName);
    }

    public static void setMerchantFilter(int filterId, String value) {
        PAYMENT_SERVICE.setMerchantFilter(filterId, value);
    }

    public static String getAddendumData(int tranId) {
        return PAYMENT_SERVICE.getAddendumData(tranId);
    }

    public static String getCheckoutData(int tranId) {
        return PAYMENT_SERVICE.getCheckoutData(tranId);
    }

    public static String getTran3dsData(int tranId) {
        return PAYMENT_SERVICE.getTran3dsData(tranId);
    }

    public static Boolean getAddendumData(int tranId, String tag) {
        return PAYMENT_SERVICE.hasAddendumData(tranId, tag);
    }

    public static Boolean getInstallmentData(int tranId) {
        return PAYMENT_SERVICE.hasInstallmentData(tranId);
    }

    public static String getValueFromRecurrent(int tranId) {
        return PAYMENT_SERVICE.getValueFromRecurrent(tranId);
    }

    public static void setMerchantAtt(int merchantId, Attributes attribute, String value) {
        PAYMENT_SERVICE.setMerchantAttribute(merchantId, attribute, value);
    }

    public static void setPropertyURLs(String propertyKey, String value) {
        PAYMENT_SERVICE.setPropertyValue(propertyKey, value);
    }

    public static boolean isStatusFilterCorrect(int filterId, String value) {
        return PAYMENT_SERVICE.isStatusFilterCorrect(filterId, value);
    }

    public static String getMCCMerchant(String merchant) {
        return PAYMENT_SERVICE.getMccForMerchant(merchant);
    }

    public static void setMCCMerchant(String merchant, String mcc) {
        PAYMENT_SERVICE.setMccForMerchant(merchant, mcc);
    }

    public static String getValueFromMTTran(String rrn, String tagName) {
        return PAYMENT_SERVICE.getValueFromMTTran(rrn, tagName);
    }

    public static String getValueFromMTTranFunding(String rrn, String tagName) {
        return PAYMENT_SERVICE.getValueFromMTTranFunding(rrn, tagName);
    }

    public static String getValueFromMTTranPayment(String rrn, String tagName) {
        return PAYMENT_SERVICE.getValueFromMTTranPayment(rrn, tagName);
    }

    public static String getValueFromMTTranReversal(String rrn, String tagName) {
        return PAYMENT_SERVICE.getValueFromMTTranReversal(rrn, tagName);
    }

    public static String getLastRequestId() {
        return PAYMENT_SERVICE.getLastRequestId();
    }

    public static String getValueFromMTTranByAprCode(String approvalCode, String tagName) {
        return PAYMENT_SERVICE.getValueFromMTTranByApprovalCode(approvalCode, tagName);
    }

    public static String getRrnFromMTTran(String receipt, String tranType) {
        return PAYMENT_SERVICE.getRrnFromMTTran(receipt, tranType);
    }

    public static String getRequestIdMT(int merchantId) {
        return PAYMENT_SERVICE.getRequestIdMT(merchantId);
    }

    public static int getTranIdMT(String rrn) {
        return PAYMENT_SERVICE.getTranIdMT(rrn);
    }

    public static String getTranMT3dsData(String rrn) {
        return PAYMENT_SERVICE.getTranMT3dsData(rrn);
    }

    public static void setNewTranTime(int tranId) {
        PAYMENT_SERVICE.setNewTranTime(tranId);
    }

    public static String verifyOrders(String approvalCode, String rrn) {
        return PAYMENT_SERVICE.verifyOrders(approvalCode, rrn);
    }
}
