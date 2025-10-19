package com.ecom.db.exception;

/** Unchecked exception representing failures during database interaction. */
public class DatabaseAccessException extends RuntimeException {

  public DatabaseAccessException(String message, Throwable cause) {
    super(message, cause);
  }

  public DatabaseAccessException(String message) {
    super(message);
  }
}
