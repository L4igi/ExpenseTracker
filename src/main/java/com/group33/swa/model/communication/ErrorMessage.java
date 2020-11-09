package com.group33.swa.model.communication;

import com.group33.swa.logic.messageDecorator.Message;

/** Class for the reprensation of an errormessage */
public class ErrorMessage implements Message {

  private String message;
  private final int HTTPSTATUS = 409;

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }

  @Override
  public int getHttpStatus() {
    return HTTPSTATUS;
  }
}
