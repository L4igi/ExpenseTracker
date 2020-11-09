package com.group33.swa.model.transaction;
/**
 * Transaction Enum Class not yet implemented to full extend if transaction between accounts of one
 * and the same user should be transactiontype self if external transaction, business or private
 * transaction with different conditions
 */
public enum TransactionType {
  PRIVATE("PRIVATE"),
  BUSINESS("BUSINESS"),
  CRYPTO("CRYPTO");

  private String transactionString;

  TransactionType(String transactionString) {
    this.transactionString = transactionString;
  }
}
