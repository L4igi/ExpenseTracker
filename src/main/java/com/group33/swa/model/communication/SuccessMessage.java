package com.group33.swa.model.communication;

import com.group33.swa.logic.messageDecorator.Message;

/** Class for the reprensation of an successmessage */
public class SuccessMessage implements Message {

  private String message;
  private final int HTTPSTATUS = 200;

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public int getHttpStatus() {
    return HTTPSTATUS;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
