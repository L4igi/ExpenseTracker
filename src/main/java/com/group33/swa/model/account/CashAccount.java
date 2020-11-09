package com.group33.swa.model.account;

import javax.persistence.Entity;

/** Cash Account Class */
@Entity
public class CashAccount extends Account {
  float startAmount;
  float overdraftlimit;

  public CashAccount() {
    super();
  }

  /**
   * has starting amount and overdraft limit as defining features, overdraftlimit always set to 0
   *
   * @param userID unique identifies user
   * @param accountName unique per user
   * @param accountType defines type of account
   * @param startAmount defines starting amount of cash account
   */
  public CashAccount(int userID, String accountName, AccountType accountType, float startAmount) {
    super(userID, accountName, accountType);
    this.startAmount = startAmount;
    this.overdraftlimit = 0;
  }

  /**
   * has starting amount and overdraft limit as defining features, overdraftlimit always set to 0 if
   * starting amount not set, sets to 0
   *
   * @param userID unique identifies user
   * @param accountName unique per user
   * @param accountType defines type of account
   */
  public CashAccount(int userID, String accountName, AccountType accountType) {
    super(userID, accountName, accountType);
    this.startAmount = 0;
    this.overdraftlimit = 0;
  }

  public float getStartAmount() {
    return startAmount;
  }

  public void setStartAmount(float startAmount) {
    this.startAmount = startAmount;
  }

  @Override
  public float getOverdraftlimit() {
    return overdraftlimit;
  }

  @Override
  public void setOverdraftlimit(float overdraftlimit) {
    this.overdraftlimit = this.balance;
  }

  @Override
  public String toString() {
    return "CashAccount{"
        + "accountID="
        + accountID
        + ", userID="
        + userID
        + ", accountType="
        + accountType
        + ", accountName='"
        + accountName
        + '\''
        + ", balance="
        + balance
        + ", startAmount="
        + startAmount
        + ", overdraftlimit="
        + overdraftlimit
        + '}';
  }
}
