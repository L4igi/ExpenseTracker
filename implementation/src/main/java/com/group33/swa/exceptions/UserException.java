package com.group33.swa.exceptions;
/** User Exception Class */
public class UserException extends Exception {
  String exception;

  /**
   * @param exception throws Exception if User could not be saved to Database (if invalid/incomplete
   *     User was created)
   */
  public UserException(String exception) {
    this.exception = exception;
  }

  @Override
  public String toString() {
    return "UserException{" + "exception=" + exception + '}';
  }
}
