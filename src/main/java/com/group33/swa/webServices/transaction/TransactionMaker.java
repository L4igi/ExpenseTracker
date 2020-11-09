package com.group33.swa.webServices.transaction;

import com.group33.swa.model.transaction.Transaction;

/** Interface for Facade Pattern of the the TransactionMaker */
public interface TransactionMaker {
  Transaction processTransaction(
      int fromAccountID,
      int toAccountID,
      int fromUserID,
      float amount,
      String transactionType,
      String transactionDate,
      String transactionDescription,
      String transactionCategory);
}
