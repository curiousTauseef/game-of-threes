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

    private String msg = "";
    Integer gameNumber = 0;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws Exception {

        if (isFirstPlay) {
            Map<String, String> value = new Gson().fromJson(message.getPayload(), Map.class);
            handleFirstPlayFromAStarterPlayer(session, value);
        } else {

            //Not First Game
            if (sessions.size() > 1) {
                WebSocketSession wantedSession = sessions.stream()
                        .filter(itSession -> itSession != session).findFirst().get();
                gameNumber = gameHelperGenerator
                        .correctTheNumberIfNotDivisibleByThreeOtherwiseReturnIt(gameNumber) / 3;
                formAndPublishTheMessageToClients(wantedSession);
                if (gameNumber==1) {
                    sendWinnerMessage(wantedSession);
                } else {
                    handleTextMessage(wantedSession, null);
                }
            }
        }

    }

    private void handleFirstPlayFromAStarterPlayer(WebSocketSession session, Map<String, String> value) throws Exception {
        gameNumber =
                value.get("number").length() > 0 ?
                        Integer.parseInt(value.get("number")) :
                        gameHelperGenerator.generateNumberDivisibleByThree();

        formAndPublishTheMessageToClients(session);
        isFirstPlay = false;
        handleTextMessage(session, null);
    }

    private void formAndPublishTheMessageToClients(WebSocketSession session) throws Exception {
        msg = "Player " + (Integer.parseInt(session.getId())+1) + " Placed: " + gameNumber;
        //added for history tracking
        messages.add(new TextMessage(msg));
        //publish to add players
        Thread.sleep(1000);
        for (WebSocketSession webSocketSession : sessions) {
            webSocketSession.sendMessage(
                    new TextMessage(msg));
        }
    }


    private void sendWinnerMessage(WebSocketSession session) throws IOException {
        msg = "Woohooooo!!! <br/> Player " + (Integer.parseInt(session.getId())+1) + " Won ";
        //added for history tracking
        messages.add(new TextMessage(msg));
        //publish to add players
        for (WebSocketSession webSocketSession : sessions) {
            webSocketSession.sendMessage(
                    new TextMessage(msg));
        }
    }

    /**
     * invoked after new connection introduced
     *
     * @param session
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
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

}
