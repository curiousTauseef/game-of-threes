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
public class SocketHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    private List<TextMessage> messages = new CopyOnWriteArrayList<>();

    private GameHelperGenerator gameHelperGenerator = new GameHelperGenerator();
    private Boolean isFirstPlay = true;
    private Boolean isFirstGameAndOneSession = false;

    private String msg = "";
    Integer gameNumber = 0;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {
        if (isFirstPlay) {
            Map<String, String> value = new Gson().fromJson(message.getPayload(), Map.class);
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

    /**
     * Invoked upon disconnect
     *
     * @param session
     * @param status
     * @throws Exception
     */
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
        if (sessions.size() == 0) {
            messages.clear();
            isFirstPlay = true;
        }
    }

    private void handleFirstPlayFromAStarterPlayer(WebSocketSession session, Map<String, String> value) throws Exception {
        if (isFirstPlay && sessions.size() == 1) {
            isFirstGameAndOneSession = true;
        }

        gameNumber =
                value.get("number").length() > 0 ?
                        Integer.parseInt(value.get("number")) :
                        gameHelperGenerator.generateNumberDivisibleByThree();

        formAndPublishTheMessageToClients(session);
        isFirstPlay = false;
        handleTextMessage(session, null);
    }

    private void handleTurnsFromPlayersAfterFirstPlay(WebSocketSession session) throws Exception {
        WebSocketSession wantedSession = sessions.stream()
                .filter(itSession -> itSession != session).findFirst().get();
        gameNumber = gameHelperGenerator
                .correctTheNumberIfNotDivisibleByThreeOtherwiseReturnIt(gameNumber) / 3;
        formAndPublishTheMessageToClients(wantedSession);
        if (gameNumber == 1) {
            sendWinnerMessage(wantedSession);
        } else {
            handleTextMessage(wantedSession, null);
        }
    }

    private void formAndPublishTheMessageToClients(WebSocketSession session) throws Exception {
        msg = "Player " + (Integer.parseInt(session.getId()) + 1) + " Placed: " + gameNumber;
        //added for history tracking
        messages.add(new TextMessage(msg));
        //publish to add players
          Thread.sleep(500);
        for (WebSocketSession webSocketSession : sessions) {
            webSocketSession.sendMessage(
                    new TextMessage(msg));
        }
    }


    private void sendWinnerMessage(WebSocketSession session) throws IOException {
        msg = "Woohooooo!!! <br/> Player " + (Integer.parseInt(session.getId()) + 1) + " Won ";
        //added for history tracking
        messages.add(new TextMessage(msg));
        //publish to add players
        for (WebSocketSession webSocketSession : sessions) {
            webSocketSession.sendMessage(
                    new TextMessage(msg));
        }

        sessions.clear();
        messages.clear();

    }

}
