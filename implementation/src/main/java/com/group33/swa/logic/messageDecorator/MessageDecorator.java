package com.group33.swa.logic.messageDecorator;

/** Decorator class for a message */
public class MessageDecorator implements Message {

  protected Message decoratorMessage;

  MessageDecorator(Message decoratorMessage) {
    this.decoratorMessage = decoratorMessage;
  }

  @Override
  public String getMessage() {
    return decoratorMessage.getMessage();
  }

  @Override
  public int getHttpStatus() {
    return decoratorMessage.getHttpStatus();
  }
}
