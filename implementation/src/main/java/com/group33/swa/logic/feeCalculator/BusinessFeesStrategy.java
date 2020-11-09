package com.group33.swa.logic.feeCalculator;

import com.group33.swa.model.transaction.Transaction;

/** Represents the strategy of BusinessTransactions with a specific fee 0.015 or 1.5%; */
public class BusinessFeesStrategy implements TransactionFeeCalculatorStrategy {
  /**
   * @param transaction {@link Transaction} Receives the Transaction which should be calculated
   * @return {@link Transaction}
   */
  @Override
  public Transaction executeStrategy(Transaction transaction) {
    System.out.println("Using BusinessFeesStrategy Calculation Method");
    float fees = 0.015f;

    transaction.setAmount(transaction.getAmount() + transaction.getAmount() * fees);

    return transaction;
  }
}
