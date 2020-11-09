package com.group33.swa.exceptions;

/** Account Exception Class */
public class AccountException extends Throwable {
  String exception;

  /**
   * @param exception throws Exception if Account could not be saved to Database (if
   *     invalid/incomplete Account was created)
   */
  public AccountException(String exception) {
    this.exception = exception;
  }

  @Override
  public String toString() {
    return "AccountException{" + "exception=" + exception + '}';
  }
}
