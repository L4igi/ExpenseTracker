package com.group33.swa.webController.websocket.config;

import com.group33.swa.webController.websocket.WebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/** Configuration for the Websocket-Interface */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

  /**
   * Registeres a WebSocketHandler on a specific Interface e.g URL/notifcation
   *
   * @param registry {@link WebSocketHandlerRegistry}
   */
  @Override
  public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    registry.addHandler(webSocketHandler(), "/notification").setAllowedOrigins("*");
  }

  /**
   * Returns a new WebSocketHandler-Instance as a Bean
   *
   * @return {@link WebSocketHandler}
   */
  @Bean
  public WebSocketHandler webSocketHandler() {
    return new com.group33.swa.webController.websocket.WebSocketHandler();
  }
}
