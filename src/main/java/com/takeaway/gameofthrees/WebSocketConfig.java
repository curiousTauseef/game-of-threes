package com.takeaway.gameofthrees;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import static com.takeaway.gameofthrees.StaticDataProvider.*;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {


    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new SocketHandler(), WEBSOCKET_URI);
    }

}
