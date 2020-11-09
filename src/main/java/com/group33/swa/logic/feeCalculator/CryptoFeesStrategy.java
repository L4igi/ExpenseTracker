package com.group33.swa.logic.feeCalculator;

import com.group33.swa.model.transaction.Transaction;

/** Represents the strategy of CryptoTransactions with a specific fee of 0.0015 or 0.15% */
public class CryptoFeesStrategy implements TransactionFeeCalculatorStrategy {
  /**
   * @param transaction {@link Transaction} Receives the Transaction which should be calculated
   * @return {@link Transaction}
   */
  @Override
  public Transaction executeStrategy(Transaction transaction) {
    System.out.println("Using CryptoFeesStrategy Calculation Method");

    float fees = 0.0025f;

    transaction.setAmount(transaction.getAmount() + transaction.getAmount() * fees);

    return transaction;
  }
}
