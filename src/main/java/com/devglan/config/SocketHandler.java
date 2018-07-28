package com.devglan.config;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws IOException {

        Map<String, String> value = new Gson().fromJson(message.getPayload(), Map.class);
        //TODO
        if (isFirstPlay) {
            Integer startOfGameNumber =
                    value.get("number").length() > 0 ?
                            Integer.parseInt(value.get("number")) :
                            gameHelperGenerator.generateNumberDivisibleByThree();

            msg = "Player " + session.getId() + " Placed: " + startOfGameNumber;
            messages.add(new TextMessage(msg));
            for (WebSocketSession webSocketSession : sessions) {
                webSocketSession.sendMessage(
                        new TextMessage(msg));
            }
            isFirstPlay = false;
        } else {

        }


//        messages.add(new TextMessage(value.get("number") + " !" + "  " +
//                session.getId()));
//        for (WebSocketSession webSocketSession : sessions) {
//            webSocketSession.sendMessage(
//                    new TextMessage(value.get("number") + " !" + "  " +
//                            session.getId())
//
//            );
  //      }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //the messages will be broadcasted to all users.
        sessions.add(session);
        if (messages.size() > 0) {
            for (TextMessage message : messages) {
                session.sendMessage(message);
            }

        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        if(sessions.size()==0)
            messages.clear();
    }

}
