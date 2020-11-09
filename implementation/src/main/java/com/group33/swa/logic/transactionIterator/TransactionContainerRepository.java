package com.group33.swa.logic.transactionIterator;

import com.group33.swa.logic.iterator.Container;
import com.group33.swa.logic.iterator.Iterator;
import com.group33.swa.webServices.account.AccountService;
import com.group33.swa.webServices.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Implements Iterator Pattern Container */
@Component
public class TransactionContainerRepository implements Container {
  @Autowired private AccountService accountService;
  @Autowired private TransactionService transactionService;

  int accountID;

  public TransactionContainerRepository() {}

  @Override
  public Iterator getIterator() {
    return new TransactionIterator(transactionService.getAllTransactionsByAccountID(accountID));
  }

  public void setAccountID(int accountID) {
    this.accountID = accountID;
  }

  public int getAccountID() {
    return accountID;
  }
}
