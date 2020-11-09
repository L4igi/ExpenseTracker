package com.group33.swa.model.account;
/** Account Enum Class */
public enum AccountType {
  DEFAULT("DEFAULT"),
  GIRO("Giro"),
  BUSINESS("Business"),
  STUDENT("Student"),
  CASH("Cash");
  private String accString;

  AccountType(String accString) {
    this.accString = accString;
  }
}
