package com.group33.swa.logic.transactionIterator;

import com.group33.swa.logic.iterator.Iterator;
import com.group33.swa.model.transaction.Transaction;

import java.util.List;

/** Implements Iterator Pattern functionaltities to iterate Container */
public class TransactionIterator implements Iterator {

  private List<Transaction> transactionList;
  private int index;

  public TransactionIterator(List<Transaction> accList) {
    this.transactionList = accList;
    this.index = 0;
  }

  @Override
  public boolean hasNext() {
    if (index < transactionList.size()) return true;
    return false;
  }

  @Override
  public Transaction next() {
    if (hasNext()) {
      return transactionList.get(index++);
    }
    return null;
  }
}
