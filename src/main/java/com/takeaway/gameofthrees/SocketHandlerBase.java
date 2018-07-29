package com.takeaway.gameofthrees;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.takeaway.gameofthrees.StaticDataProvider.*;

public abstract class SocketHandlerBase extends TextWebSocketHandler {

    protected String msg = "";
    protected Integer gameNumber = 0;
    protected List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    protected List<TextMessage> messages = new CopyOnWriteArrayList<>();

    protected GameHelperGenerator gameHelperGenerator = new GameHelperGenerator();

    protected Boolean isFirstPlay = true;
    protected Boolean isFirstGameAndOneSession = false;

    protected void handleFirstPlayFromAStarterPlayer(WebSocketSession session, Map<String, String> value) throws Exception {
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

    protected void handleTurnsFromPlayersAfterFirstPlay(WebSocketSession session) throws Exception {
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

    protected void formAndPublishTheMessageToClients(WebSocketSession session) throws Exception {
        Thread.sleep(500);
        msg = PLAYER_PLAY_TURN.replace("[1]", String.valueOf(Integer.parseInt(session.getId()) + 1))
                .replace("[2]", String.valueOf(gameNumber));
        publishMessageToClients();

    }


    protected void sendWinnerMessage(WebSocketSession session) throws IOException {
        msg = WINNER_MESSAGE.replace("[]", String.valueOf((Integer.parseInt(session.getId()) + 1)));
        //added for history tracking
        publishMessageToClients();
        //sessions.clear();
        messages.clear();
        isFirstPlay = true;

    }


    protected void publishMessageToClients() throws IOException {
        //added for history tracking
        messages.add(new TextMessage(msg));
        //publish to add players
        sessions.forEach(webSocketSession -> {
            try {
                webSocketSession.sendMessage(
                        new TextMessage(msg));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
