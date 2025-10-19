package com.ecom.db.service;

import com.ecom.db.dao.PaymentDao;
import com.ecom.db.spi.ConnectionProvider;
import java.sql.Connection;
import java.sql.SQLException;

abstract class BaseDatabaseService implements DatabaseService {

  private final ConnectionProvider connectionProvider;
  private final PaymentService paymentService;

  protected BaseDatabaseService(ConnectionProvider connectionProvider) {
    this.connectionProvider = connectionProvider;
    this.paymentService = new DefaultPaymentService(new PaymentDao(connectionProvider));
  }

  @Override
  public PaymentService payments() {
    return paymentService;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return connectionProvider.getConnection();
  }
}
