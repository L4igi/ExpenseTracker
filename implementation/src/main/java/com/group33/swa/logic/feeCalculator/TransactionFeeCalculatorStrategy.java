package com.group33.swa.logic.feeCalculator;

import com.group33.swa.model.transaction.Transaction;

public interface TransactionFeeCalculatorStrategy {
  /**
   * Calculates the Fees for the Transactions and returns the Transaction where the fees where
   * calculated.
   *
   * @param transaction {@link Transaction} Receives the Transaction which should be calculated
   * @return {@link Transaction}
   */
  Transaction executeStrategy(Transaction transaction);
}
