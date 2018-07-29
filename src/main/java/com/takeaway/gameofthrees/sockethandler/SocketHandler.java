package com.takeaway.gameofthrees.sockethandler;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;

/**
 * Socket Handler that handles the main operations in the websocket between clients
 */
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
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        if (sessions.size() == 0) {
            messages.clear();
            isFirstPlay = true;
        }
    }

}
