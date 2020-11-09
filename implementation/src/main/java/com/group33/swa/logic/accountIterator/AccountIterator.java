package com.group33.swa.logic.accountIterator;

import com.group33.swa.logic.iterator.Iterator;
import com.group33.swa.model.account.Account;

import java.util.List;

/** */
public class AccountIterator implements Iterator {

  private List<Account> accList;
  private int index;

  public AccountIterator(List<Account> accList) {
    this.accList = accList;
    this.index = 0;
  }

  @Override
  public boolean hasNext() {
    if (index < accList.size()) return true;
    return false;
  }

  @Override
  public Account next() {
    if (hasNext()) {
      return accList.get(index++);
    }
    return null;
  }
}
