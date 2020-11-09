package com.group33.swa.logic.feeCalculator;

import com.group33.swa.model.transaction.Transaction;
import com.group33.swa.model.transaction.TransactionType;

/**
 * TransactionFeeCalculator uses a specific strategy of {@link TransactionFeeCalculatorStrategy} to
 * calculate the fees for a specific {@link TransactionType}
 */
public class TransactionFeeCalculator {
  private TransactionFeeCalculatorStrategy strategy;
  private Transaction transaction;

  /**
   * Constructur which is used to set the defined {@link TransactionFeeCalculatorStrategy}
   *
   * @param strategy {@link Transaction}
   */
  public TransactionFeeCalculator(TransactionFeeCalculatorStrategy strategy) {
    this.strategy = strategy;
  }

  /**
   * Method to calculate the Transaction-Fees
   *
   * @return {@link Transaction}
   */
  public Transaction executeStrategy() {
    return strategy.executeStrategy(transaction);
  }

  public TransactionFeeCalculatorStrategy getStrategy() {
    return strategy;
  }

  public void setStrategy(TransactionFeeCalculatorStrategy strategy) {
    this.strategy = strategy;
  }

  public Transaction getTransaction() {
    return transaction;
  }

  public void setTransaction(Transaction transaction) {
    try {
      if (transaction == null) {
        throw new Exception("Transaction is Null");
      }
      this.transaction = transaction;
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
