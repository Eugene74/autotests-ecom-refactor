package com.ecom.db.service;

import com.ecom.api.type.Attributes;

public interface PaymentService {

  String getNFileNameByTran(int tranId);

  String getNFileNameByBatch(int batchId);

  String getAcquirerIDByBank(String bank);

  int getTranIdByOrder1(String order);

  int getTranIdByOrder(String order);

  int getTranIdByOrderFR(String order);

  String getValueFromMTTranFR(int tranId, String columnName);

  int getTranIdByOrderAndTranType(String order, String tranType);

  int getTranIdByRrn(String rrn);

  int getTranIdByApprovalCode(String approvalCode);

  int getParentTranId(String order);

  String getMessageISO(Integer tranId);

  String getAuditEntityId(Integer tranId);

  String getBatchId(String rrn);

  String getBatchIdFromBatch(String filename);

  String getValueFromTRAN(int tranId, String tagName);

  void setMerchantFilter(int filterId, String value);

  String getAddendumData(int tranId);

  String getCheckoutData(int tranId);

  String getTran3dsData(int tranId);

  boolean hasAddendumData(int tranId, String tag);

  boolean hasInstallmentData(int tranId);

  String getValueFromRecurrent(int tranId);

  void setMerchantAttribute(int merchantId, Attributes attribute, String value);

  void setPropertyValue(String propertyKey, String value);

  boolean isStatusFilterCorrect(int filterId, String expectedValue);

  String getMccForMerchant(String merchant);

  void setMccForMerchant(String merchant, String mcc);

  String getValueFromMTTran(String rrn, String tagName);

  String getValueFromMTTranFunding(String rrn, String tagName);

  String getValueFromMTTranPayment(String rrn, String tagName);

  String getValueFromMTTranReversal(String rrn, String tagName);

  String getLastRequestId();

  String getValueFromMTTranByApprovalCode(String approvalCode, String tagName);

  String getRrnFromMTTran(String requestId, String tranType);

  String getRequestIdMT(int merchantId);

  int getTranIdMT(String rrn);

  String getTranMT3dsData(String rrn);

  void setNewTranTime(int tranId);

  String verifyOrders(String approvalCode, String rrn);
}
