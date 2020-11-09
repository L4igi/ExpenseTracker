package com.group33.swa.webServices.account;

import com.group33.swa.exceptions.AccountException;
import com.group33.swa.model.account.Account;

import java.util.List;
/** AccountServiceImpl Interface */
public interface AccountService {

  /**
   * Saves account to databse returns account if saved, else throws exception
   *
   * @param account represents a account DTO to be stored in the database
   * @return {@link Account}
   * @throws AccountException
   */
  Account createAccount(Account account) throws AccountException;

  /**
   * Receives all accounts for a user with a specific userID
   *
   * @param UserID unique identifier of the user
   * @return {@link List}&lt;{@link Account}&gt;
   */
  List<Account> getAllAccountsbyUserID(int UserID);

  /**
   * returns account by userID and accountID from database
   *
   * @param UserID unique identifier of the user
   * @param AccountID unique identifier of the account
   * @return {@link Account}
   */
  Account getAccount(int UserID, int AccountID);

  /**
   * returns List of all accounts from database
   *
   * @return {@link List}&lt;{@link Account}&gt;
   */
  List<Account> getAllAccounts();

  /**
   * Updates exisiting account in database
   *
   * @param account represents the account with the updated account informations
   * @return {@link Account}
   */
  Account updateAccount(Account account);

  /**
   * returns true if account exists else false
   *
   * @param accountID unique identifier of the account
   * @return {@link Boolean}
   */
  boolean accountExist(int accountID);
}
