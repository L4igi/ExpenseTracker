package com.group33.swa.webController.websocket;

import com.group33.swa.logic.messageDecorator.Message;
import com.group33.swa.logic.observer.Observable;
import com.group33.swa.logic.observer.Observer;
import java.io.IOException;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/** WebSocketHandler which is used to Handles Massages and Client-Socket-Sessions */
public class WebSocketHandler extends TextWebSocketHandler implements Observer {

  private WebSocketSession clientSession;

  /**
   * Method which receives Messages from a Client over a Websocket.
   *
   * @param session {@link WebSocketSession}
   * @param message {@link TextMessage}
   * @throws IOException
   */
  @Override
  public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
    System.out.println(session);
    System.out.println(message.toString());

    if (message.getPayload().equals("hi")) {
      TextMessage send_message = new TextMessage("Hello back");
      session.sendMessage(send_message);
    }
  }

  /**
   * Method which is beeing called when a new connection of a client on the Websocekt was
   * established.
   *
   * @param session {@link WebSocketSession}
   */
  @Override
  public void afterConnectionEstablished(WebSocketSession session) {

    System.out.println(
        "New Client connection established! ClientID: "
            + session.getId()
            + " IP: "
            + session.getRemoteAddress().getHostName());
    this.clientSession = session;
  }

  /**
   * This Method which is beeing called by a Observable-Instance
   *
   * @param o
   * @param arg
   */
  @Override
  public void update(Observable o, Object arg) {

    System.out.println(this);

    System.out.println("ClientSession == null: " + (clientSession == null));
    System.out.println(clientSession);
    if (clientSession != null && clientSession.isOpen()) {
      try {
        if (arg instanceof Message) {
          clientSession.sendMessage(new TextMessage(((Message) arg).getMessage()));
        }
      } catch (IOException e) {
        System.out.println(e);
      }

    } else {
      System.out.println("No clientsession available. Therefore no message will be send! ");
      System.out.println(o);
      System.out.println(arg);
    }
  }
}
