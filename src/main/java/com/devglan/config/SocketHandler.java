package com.devglan.config;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class SocketHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private List<TextMessage> messages = new CopyOnWriteArrayList<>();

    private GameHelperGenerator gameHelperGenerator = new GameHelperGenerator();
    private Boolean isFirstPlay = true;

    private String msg = "";
    Integer gameNumber =0;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws IOException {

        Map<String, String> value = new Gson().fromJson(message.getPayload(), Map.class);

        if (isFirstPlay) {
             gameNumber =
                    value.get("number").length() > 0 ?
                            Integer.parseInt(value.get("number")) :
                            gameHelperGenerator.generateNumberDivisibleByThree();

            msg = "Player " + session.getId() + " Placed: " + gameNumber;
            //added for history tracking
            messages.add(new TextMessage(msg));
            //publish to add players
            for (WebSocketSession webSocketSession : sessions) {
                webSocketSession.sendMessage(
                        new TextMessage(msg));
            }
            isFirstPlay = false;
        } else {

        }

    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        //the messages will be broadcasted to all users when they are connected
        sessions.add(session);

        if (messages.size() > 0) {
            messages.forEach(message -> {
                try {
                    session.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        if (sessions.size() == 0)
            messages.clear();
    }

}
