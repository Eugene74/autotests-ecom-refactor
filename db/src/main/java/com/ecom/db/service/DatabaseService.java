package com.ecom.db.service;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseService {

  PaymentService payments();

  Connection getConnection() throws SQLException;
}
