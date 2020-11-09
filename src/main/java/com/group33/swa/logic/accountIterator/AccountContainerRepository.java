package com.group33.swa.logic.accountIterator;

import com.group33.swa.logic.iterator.Container;
import com.group33.swa.logic.iterator.Iterator;
import com.group33.swa.webServices.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** Class for the reprensation of a accountContainer */
@Component
public class AccountContainerRepository implements Container {
  @Autowired private AccountService accountService;
  private int userID;

  public AccountContainerRepository() {}

  @Override
  public Iterator getIterator() {
    return new AccountIterator(accountService.getAllAccountsbyUserID(userID));
  }

  public void setUserID(int userID) {
    this.userID = userID;
  }

  public int getUserID() {
    return userID;
  }
}
