package com.group33.swa.model.account;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
/** Account Class */
@Entity
public abstract class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  int accountID;

  @NotNull int userID;
  AccountType accountType;

  @Column(unique = true)
  String accountName;

  float balance;

  public Account() {}

  /**
   * on creating of new Account sets starting balance to 0
   *
   * @param userID unique identifies user
   * @param accountName unique per user
   * @param accountType defines type of account
   */
  public Account(int userID, String accountName, AccountType accountType) {
    this.userID = userID;
    this.accountType = accountType;
    this.accountName = accountName;
    this.balance = 0.0f;
  }

  public int getAccountID() {
    return accountID;
  }

  public void setAccountID(int accountID) {
    this.accountID = accountID;
  }

  public int getUserID() {
    return userID;
  }

  public void setUserID(int userID) {
    this.userID = userID;
  }

  public AccountType getAccountType() {
    return accountType;
  }

  public void setAccountType(AccountType accountType) {
    this.accountType = accountType;
  }

  public String getAccountName() {
    return accountName;
  }

  public void setAccountName(String accountName) {
    this.accountName = accountName;
  }

  public float getBalance() {
    return balance;
  }

  public void setBalance(float amount) {
    this.balance = amount;
  }

  public abstract float getOverdraftlimit();

  abstract void setOverdraftlimit(float overdraftlimit);

  @Override
  public String toString() {
    return "Account{"
        + "accountID="
        + accountID
        + ", userID="
        + userID
        + ", accountType="
        + accountType
        + ", accountName='"
        + accountName
        + ", amount"
        + balance
        + '\''
        + '}';
  }
}
