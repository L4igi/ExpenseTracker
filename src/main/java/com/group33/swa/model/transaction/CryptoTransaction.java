package com.group33.swa.model.transaction;

import javax.persistence.Entity;
import java.sql.Date;

@Entity
public class CryptoTransaction extends Transaction {
  public CryptoTransaction() {
    super();
  }
  /**
   * creates new Crypto transaction
   *
   * @param fromAccountID defines account from which transaction is outgoing
   * @param toAccountID defines account from which transaction is going to
   * @param fromUserID defines user from which transaction is outgoing
   * @param amount amount of money to be transfered
   * @param transactionType type of transaction
   * @param transactionDate date of transaction
   * @param transactionDescription description of transaction
   * @param transactionCategory category of transaction
   */
  public CryptoTransaction(
      int fromAccountID,
      int toAccountID,
      int fromUserID,
      float amount,
      TransactionType transactionType,
      Date transactionDate,
      String transactionDescription,
      TransactionCategory transactionCategory) {
    super(
        fromAccountID,
        toAccountID,
        fromUserID,
        amount,
        transactionType,
        transactionDate,
        transactionDescription,
        transactionCategory);
  }
}
