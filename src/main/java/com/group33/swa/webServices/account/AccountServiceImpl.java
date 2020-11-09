package com.group33.swa.webServices.account;

import com.group33.swa.exceptions.AccountException;
import com.group33.swa.model.account.Account;
import com.group33.swa.repository.account.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
/** AccountServiceImpl Class */
@Service
public class AccountServiceImpl implements AccountService {

  @Autowired private AccountRepository accountRepository;

  /**
   * Saves account to databse returns account if saved, else throws exception
   *
   * @param account represents a account DTO to be stored in the database
   * @return {@link Account}
   * @throws AccountException
   */
  @Override
  public Account createAccount(Account account) throws AccountException {
    try {
      Account returnAccount = accountRepository.save(account);
      return returnAccount;
    } catch (Exception e) {
      System.out.println("Account creat exception");
      return null;
    }
  }

  /**
   * Receives all accounts for a user with a specific userID
   *
   * @param UserID unique identifier of the user
   * @return {@link List}&lt;{@link Account}&gt;
   */
  @Override
  public List<Account> getAllAccountsbyUserID(int UserID) {
    return accountRepository.findAccountsByUserID(UserID);
  }

  /**
   * returns account by userID and accountID from database
   *
   * @param UserID unique identifier of the user
   * @param AccountID unique identifier of the account
   * @return {@link Account}
   */
  @Override
  public Account getAccount(int UserID, int AccountID) {
    return accountRepository.getAccountByUserIDAndAccountID(UserID, AccountID);
  }

  /**
   * returns List of all accounts from database
   *
   * @return {@link List}&lt;{@link Account}&gt;
   */
  @Override
  public List<Account> getAllAccounts() {
    return accountRepository.findAll();
  }

  /**
   * Updates exisiting account in database
   *
   * @param account represents the account with the updated account informations
   * @return {@link Account}
   */
  @Override
  public Account updateAccount(Account account) {
    return accountRepository.save(account);
  }

  /**
   * returns true if account exists else false
   *
   * @param accountID unique identifier of the account
   * @return boolean
   */
  @Override
  public boolean accountExist(int accountID) {
    System.out.println("in returnBool");
    return accountRepository.existsAccountByAccountID(accountID);
  }
}
