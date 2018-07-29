package com.takeaway.gameofthrees;

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
public class SocketHandler extends SocketHandlerBase {

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {
        if (isFirstPlay) {
            Map value = new Gson().fromJson(message.getPayload(), Map.class);
            handleFirstPlayFromAStarterPlayer(session, value);
        } else {
            if (sessions.size() > 1) {
                handleTurnsFromPlayersAfterFirstPlay(session);
            }
        }

    }

    /**
     * invoked after new connection introduced
     *
     * @param session
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //limit number of players to 2
        if (sessions.size() < 2) {
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
            if (sessions.size() == 2 && isFirstGameAndOneSession) {
                WebSocketSession wantedSession = sessions.stream()
                        .filter(itSession -> itSession != session).findFirst().get();
                handleTextMessage(wantedSession, null);
                isFirstGameAndOneSession = false;
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        if (sessions.size() == 0) {
            messages.clear();
            isFirstPlay = true;
        }
    }

}
