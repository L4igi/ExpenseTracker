package com.group33.swa.model.account;

import javax.persistence.Entity;

/** Student Account Class */
@Entity
public class StudentAccount extends GiroAccount {

  String student_number;

  public StudentAccount() {
    super();
  };

  /**
   * has student_number as defining feature
   *
   * @param userID unique identifies user
   * @param accountName unique per user
   * @param accountType defines type of account
   * @param overdraftlimit sets overdraftlimit to certain amount
   * @param student_number sets studentnumber as unique identifier
   */
  public StudentAccount(
      int userID,
      String accountName,
      AccountType accountType,
      float overdraftlimit,
      String student_number) {
    super(userID, accountName, accountType, overdraftlimit);
    this.student_number = student_number;
  };

  /**
   * has student_number as defining feature, overdraftlimit if not set, set to 0
   *
   * @param userID unique identifies user
   * @param accountName unique per user
   * @param accountType defines type of account
   * @param student_number sets studentnumber as unique identifier
   */
  public StudentAccount(
      int userID, String accountName, AccountType accountType, String student_number) {
    super(userID, accountName, accountType);
    this.student_number = student_number;
  };

  public StudentAccount(
      int userID,
      String accountName,
      AccountType accountType,
      String student_number,
      float overdraftlimit) {
    super(userID, accountName, accountType, overdraftlimit);
    this.student_number = student_number;
  };

  public String getStudent_number() {
    return student_number;
  }

  public void setStudent_number(String student_number) {
    this.student_number = student_number;
  }

  @Override
  public String toString() {
    return "StudentAccount{"
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
        + ", overdraftlimit="
        + overdraftlimit
        + "student_number="
        + student_number
        + '}';
  }
}
