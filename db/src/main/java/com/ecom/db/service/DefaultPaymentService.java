package com.ecom.db.service;

import com.ecom.api.type.Attributes;
import com.ecom.db.dao.PaymentDao;

public class DefaultPaymentService implements PaymentService {

  private final PaymentDao paymentDao;

  public DefaultPaymentService(PaymentDao paymentDao) {
    this.paymentDao = paymentDao;
  }

  @Override
  public String getNFileNameByTran(int tranId) {
    return paymentDao.getNFileNameByTran(tranId);
  }

  @Override
  public String getNFileNameByBatch(int batchId) {
    return paymentDao.getNFileNameByBatch(batchId);
  }

  @Override
  public String getAcquirerIDByBank(String bank) {
    return paymentDao.getAcquirerIDByBank(bank);
  }

  @Override
  public int getTranIdByOrder1(String order) {
    return paymentDao.getTranIdByOrder1(order);
  }

  @Override
  public int getTranIdByOrder(String order) {
    return paymentDao.getTranIdByOrder(order);
  }

  @Override
  public int getTranIdByOrderFR(String order) {
    return paymentDao.getTranIdByOrderFR(order);
  }

  @Override
  public String getValueFromMTTranFR(int tranId, String columnName) {
    return paymentDao.getValueFromMTTranFR(tranId, columnName);
  }

  @Override
  public int getTranIdByOrderAndTranType(String order, String tranType) {
    return paymentDao.getTranIdByOrderAndTranType(order, tranType);
  }

  @Override
  public int getTranIdByRrn(String rrn) {
    return paymentDao.getTranIdByRrn(rrn);
  }

  @Override
  public int getTranIdByApprovalCode(String approvalCode) {
    return paymentDao.getTranIdByApprovalCode(approvalCode);
  }

  @Override
  public int getParentTranId(String order) {
    return paymentDao.getParentTranId(order);
  }

  @Override
  public String getMessageISO(Integer tranId) {
    return paymentDao.getMessageISO(tranId);
  }

  @Override
  public String getAuditEntityId(Integer tranId) {
    return paymentDao.getAuditEntityId(tranId);
  }

  @Override
  public String getBatchId(String rrn) {
    return paymentDao.getBatchId(rrn);
  }

  @Override
  public String getBatchIdFromBatch(String filename) {
    return paymentDao.getBatchIdFromBatch(filename);
  }

  @Override
  public String getValueFromTRAN(int tranId, String tagName) {
    return paymentDao.getValueFromTRAN(tranId, tagName);
  }

  @Override
  public void setMerchantFilter(int filterId, String value) {
    paymentDao.setMerchantFilter(filterId, value);
  }

  @Override
  public String getAddendumData(int tranId) {
    return paymentDao.getAddendumData(tranId);
  }

  @Override
  public String getCheckoutData(int tranId) {
    return paymentDao.getCheckoutData(tranId);
  }

  @Override
  public String getTran3dsData(int tranId) {
    return paymentDao.getTran3dsData(tranId);
  }

  @Override
  public boolean hasAddendumData(int tranId, String tag) {
    return paymentDao.hasAddendumData(tranId, tag);
  }

  @Override
  public boolean hasInstallmentData(int tranId) {
    return paymentDao.hasInstallmentData(tranId);
  }

  @Override
  public String getValueFromRecurrent(int tranId) {
    return paymentDao.getValueFromRecurrent(tranId);
  }

  @Override
  public void setMerchantAttribute(int merchantId, Attributes attribute, String value) {
    paymentDao.setMerchantAttribute(merchantId, attribute, value);
  }

  @Override
  public void setPropertyValue(String propertyKey, String value) {
    paymentDao.setPropertyValue(propertyKey, value);
  }

  @Override
  public boolean isStatusFilterCorrect(int filterId, String expectedValue) {
    return paymentDao.isStatusFilterCorrect(filterId, expectedValue);
  }

  @Override
  public String getMccForMerchant(String merchant) {
    return paymentDao.getMccForMerchant(merchant);
  }

  @Override
  public void setMccForMerchant(String merchant, String mcc) {
    paymentDao.setMccForMerchant(merchant, mcc);
  }

  @Override
  public String getValueFromMTTran(String rrn, String tagName) {
    return paymentDao.getValueFromMTTran(rrn, tagName);
  }

  @Override
  public String getValueFromMTTranFunding(String rrn, String tagName) {
    return paymentDao.getValueFromMTTranFunding(rrn, tagName);
  }

  @Override
  public String getValueFromMTTranPayment(String rrn, String tagName) {
    return paymentDao.getValueFromMTTranPayment(rrn, tagName);
  }

  @Override
  public String getValueFromMTTranReversal(String rrn, String tagName) {
    return paymentDao.getValueFromMTTranReversal(rrn, tagName);
  }

  @Override
  public String getLastRequestId() {
    return paymentDao.getLastRequestId();
  }

  @Override
  public String getValueFromMTTranByApprovalCode(String approvalCode, String tagName) {
    return paymentDao.getValueFromMTTranByApprovalCode(approvalCode, tagName);
  }

  @Override
  public String getRrnFromMTTran(String requestId, String tranType) {
    return paymentDao.getRrnFromMTTran(requestId, tranType);
  }

  @Override
  public String getRequestIdMT(int merchantId) {
    return paymentDao.getRequestIdMT(merchantId);
  }

  @Override
  public int getTranIdMT(String rrn) {
    return paymentDao.getTranIdMT(rrn);
  }

  @Override
  public String getTranMT3dsData(String rrn) {
    return paymentDao.getTranMT3dsData(rrn);
  }

  @Override
  public void setNewTranTime(int tranId) {
    paymentDao.setNewTranTime(tranId);
  }

  @Override
  public String verifyOrders(String approvalCode, String rrn) {
    return paymentDao.verifyOrders(approvalCode, rrn);
  }
}
