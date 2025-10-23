package com.ecom.api.model;

import java.io.Serializable;
import java.util.List;

public class ECommTransaction implements Serializable {

  private TransactionRecord transactionRecord;

  private List<AcquirerData> acquirerDataItems;

  public ECommTransaction(TransactionRecord transactionRecord) {
    this.transactionRecord = transactionRecord;
  }

  public ECommTransaction(
      TransactionRecord transactionRecord, List<AcquirerData> acquirerDataItems) {
    this.transactionRecord = transactionRecord;
    this.acquirerDataItems = acquirerDataItems;
  }
}
