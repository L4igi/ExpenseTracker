package com.group33.swa.model.account;

import javax.persistence.Entity;

/** Giro Account Class has overdraftlimit as defining feature, if not set, set to 0 */
@Entity
public class GiroAccount extends Account {
  float overdraftlimit;

  public GiroAccount() {
    super();
  };

  /**
   * has overdraftlimit as defining feature, if not set, set to 0
   *
   * @param userID unique identifies user
   * @param accountName unique per user
   * @param accountType defines type of account
   * @param overdraftlimit sets overdraftlimit to certain amount
   */
  public GiroAccount(
      int userID, String accountName, AccountType accountType, float overdraftlimit) {
    super(userID, accountName, accountType);
    this.overdraftlimit = overdraftlimit;
  };

  /**
   * if overdraftlimit not set, set to 0
   *
   * @param userID unique identifies user
   * @param accountName unique per user
   * @param accountType defines type of account
   */
  public GiroAccount(int userID, String accountName, AccountType accountType) {
    super(userID, accountName, accountType);
    this.overdraftlimit = 0.0f;
  };

  public float getOverdraftlimit() {
    return overdraftlimit;
  }

  public void setOverdraftlimit(float overdraftlimit) {
    this.overdraftlimit = overdraftlimit;
  }

  @Override
  public String toString() {
    return "GiroAccount{"
        + "overdraftlimit="
        + overdraftlimit
        + ", accountID="
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
        + '}';
  }
}
