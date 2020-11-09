package com.group33.swa.repository.account;

import com.group33.swa.model.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
/** AccountRepository Class */
public interface AccountRepository extends JpaRepository<Account, String> {
  // Account findAccountByAccountName (String accountName);

  /**
   * returns all Accounts owned by User
   *
   * @param UserID unique identifier of user
   * @return {@link List}&lt;{@link Account}&gt;
   */
  List<Account> findAccountsByUserID(int UserID);

  /**
   * returns Accounts owned by User, checks if User owns this Account
   *
   * @param UserID unique identifier of user
   * @param AccountID unique identifier of account
   * @return {@link Account}
   */
  Account getAccountByUserIDAndAccountID(int UserID, int AccountID);

  /**
   * returns a List of all accounts
   *
   * @return {@link List}&lt;{@link Account}&gt;
   */
  List<Account> findAll();

  /**
   * returns true if account exists else false
   *
   * @param accountID unique identifier of account
   * @return boolean
   */
  boolean existsAccountByAccountID(int accountID);
}
