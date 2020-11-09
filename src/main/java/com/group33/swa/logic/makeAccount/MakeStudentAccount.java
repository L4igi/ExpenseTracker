package com.group33.swa.logic.makeAccount;

import com.group33.swa.model.account.Account;
import com.group33.swa.model.account.AccountType;
import com.group33.swa.model.account.StudentAccount;
import com.group33.swa.webServices.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** */
@Component
public class MakeStudentAccount extends MakeAccount {

  @Override
  public Account setUpData(
      int userID,
      String accountName,
      AccountType accountType,
      String studentid,
      float overdraftlimit,
      float startingamount) {
    Account account =
        new StudentAccount(userID, accountName, accountType, studentid, overdraftlimit);
    return account;
  }

  @Autowired private AccountService accountService;

  @Override
  public Account setStartingValue(Account account, float startingamount) {
    return account;
  }
}
