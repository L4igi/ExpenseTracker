package com.group33.swa.logic.feeCalculator;

import com.group33.swa.model.transaction.Transaction;

/** Represents the strategy of CryptoTransactions with a specific fee of 0 or 0% */
public class PrivateFeesStrategy implements TransactionFeeCalculatorStrategy {

  /**
   * TransactionFeeCalculatorStrategy Calculates the Fees for the Transactions and returns the
   * Transaction where the fees where calculated.
   *
   * @param transaction {@link Transaction} Receives the Transaction which should be calculated
   * @return {@link Transaction}
   */
  @Override
  public Transaction executeStrategy(Transaction transaction) {
    System.out.println("Using noFees Calculation Method");
    return transaction;
  }
}
