package com.group33.swa.model.transaction;

import javax.persistence.Entity;
/** Private Transaction Class */
@Entity
public class PrivateTransaction extends Transaction {

  public PrivateTransaction() {
    super();
  }

  /**
   * creates new private transaction
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
  public PrivateTransaction(
      int fromAccountID,
      int toAccountID,
      int fromUserID,
      float amount,
      TransactionType transactionType,
      java.sql.Date transactionDate,
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
