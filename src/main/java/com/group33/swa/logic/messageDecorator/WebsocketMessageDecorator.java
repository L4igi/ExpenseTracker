package com.group33.swa.logic.messageDecorator;

/** Decorator wrapper class for Websocketmessages */
public class WebsocketMessageDecorator extends MessageDecorator {

  public WebsocketMessageDecorator(Message decoratorMessage) {
    super(decoratorMessage);
  }

  @Override
  public String getMessage() {
    return getMessageWithHttpStatusFlag(decoratorMessage) + decoratorMessage.getMessage();
  }

  private String getMessageWithHttpStatusFlag(Message decoratorMessage) {
    return "WS-Message: " + decoratorMessage.getHttpStatus() + " | ";
  }
}
