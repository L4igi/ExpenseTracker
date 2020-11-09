package com.group33.swa.logic.makeAccount;

import com.group33.swa.exceptions.AccountException;
import com.group33.swa.model.account.Account;
import com.group33.swa.model.account.AccountType;
import com.group33.swa.webServices.account.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** */
@Component
public abstract class MakeAccount {
  @Autowired private AccountService accountService;

  /**
   * Constructor the abstract class
   *
   * @param userID {@link Integer} uses the userID to check if a user
   * @param accountName {@link String} name of the account
   * @param accountType {@link AccountType} Type of a Account, e.g AccountType.PRIVATE
   * @param studentid {@link String} value of the studentid
   * @param overdraftlimit {@link Float} the value the maxium overdrafting balance
   * @param startingamount {@link Float} the value on which the account balance is starting with
   * @return {@link Account}
   */
  protected abstract Account setUpData(
      int userID,
      String accountName,
      AccountType accountType,
      String studentid,
      float overdraftlimit,
      float startingamount);

  /**
   * Method that saves an account in the database
   *
   * @param saveAccount {@link Account} Account-Object that will be saved in the database
   * @return {@link Account}
   * @throws AccountException
   */
  protected Account createAccount(Account saveAccount) throws AccountException {
    /** if account could not be created, throws AccountException */
    saveAccount = accountService.createAccount(saveAccount);
    if (saveAccount == null) {
      throw new AccountException("Account Creation Error");
    }
    return saveAccount;
  }

  protected abstract Account setStartingValue(Account saveAccount, float startingamount);

  /**
   * Uses Template Factory Pattern to create Account of different kinds
   *
   * @param userID {@link Integer} uses the userID to check if a user
   * @param accountName {@link String} name of the account
   * @param accountType {@link AccountType} Type of a Account, e.g AccountType.PRIVATE
   * @param studentid {@link String} value of the studentid
   * @param overdraftlimit {@link Float} the value the maxium overdrafting balance
   * @param startingamount {@link Float} the value on which the account balance is starting with
   * @return {@link Account}
   * @throws AccountException
   */
  public final Account makeAccount(
      int userID,
      String accountName,
      AccountType accountType,
      String studentid,
      float overdraftlimit,
      float startingamount)
      throws AccountException {
    Account account =
        setUpData(userID, accountName, accountType, studentid, overdraftlimit, startingamount);
    account = createAccount(account);
    account = setStartingValue(account, startingamount);
    return account;
  }
}
