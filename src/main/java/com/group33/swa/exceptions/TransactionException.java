package com.group33.swa.exceptions;
/** Transaction Exception Class */
public class TransactionException extends Throwable {
  String exception;

  /**
   * @param exception throws Exception if Transaction could not be saved to Database (if
   *     invalid/incomplete Transaction was created)
   */
  public TransactionException(String exception) {
    this.exception = exception;
  }

  @Override
  public String toString() {
    return "TransactionException{" + "exception=" + exception + '}';
  }
}
