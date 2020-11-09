package com.group33.swa.logic.messageDecorator;

/** Interface for the behavior definition of a message */
public interface Message {
  String getMessage();

  int getHttpStatus();
}
