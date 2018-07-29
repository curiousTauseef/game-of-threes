package com.takeaway.gameofthrees;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.takeaway.gameofthrees.StaticDataProvider.*;

/**
 * Parent class for SocketHandler to include supportive methods shared between operational work flow
 */
public abstract class SocketHandlerBase extends AbstractWebSocketHandler {

    protected List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();
    //to carry on history of messages during the current game
    protected List<TextMessage> messages = new CopyOnWriteArrayList<>();
    protected GameHelperGenerator gameHelperGenerator = new GameHelperGenerator();

    protected String msg = "";
    protected Integer gameNumber = 0;
    protected Boolean isFirstPlay = true;
    //Use this Boolean for the purpose when we have just one player connected and should wait for the other
    protected Boolean isFirstGameAndOneSession = false;

    /**
     * Handle the move from the first player even if it's user entry or random generated number
     *
     * @param session
     * @param value
     * @throws Exception
     */
    protected void handleFirstPlayFromAStarterPlayer(WebSocketSession session, Map<String, String> value) throws Exception {
        if (isFirstPlay && sessions.size() == 1) {
            isFirstGameAndOneSession = true;
        }

        gameNumber =
                value.get(INPUT_LABEL).length() > 0 ?
                        Integer.parseInt(value.get(INPUT_LABEL)) :
                        gameHelperGenerator.generateNumberDivisibleByThree();

        formAndPublishTheMessageToClients(session);
        isFirstPlay = false;
        handleTextMessage(session, null);
    }

    /**
     * Operates on the game after first play
     *
     * @param session
     * @throws Exception
     */
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

    /**
     * Publish message to all connected players
     *
     * @param session
     * @throws Exception
     */
    protected void formAndPublishTheMessageToClients(WebSocketSession session) throws Exception {
        Thread.sleep(THREAD_WAITING_TIME);
        msg = PLAYER_PLAY_TURN.replace("[1]", String.valueOf(Integer.parseInt(session.getId()) + 1))
                .replace("[2]", String.valueOf(gameNumber));
        publishMessageToClients();

    }

    /**
     * Inform all players that we have a winner and empty message array to make it ready for next game
     *
     * @param session
     * @throws IOException
     */
    protected void sendWinnerMessage(WebSocketSession session) throws IOException {
        msg = WINNER_MESSAGE.replace("[]", String.valueOf((Integer.parseInt(session.getId()) + 1)));
        //added for history tracking
        publishMessageToClients();
        //sessions.clear();
        messages.clear();
        isFirstPlay = true;

    }

    /**
     * internally supportive method to spread the messages among all clients
     *
     * @throws IOException
     */
    private void publishMessageToClients() throws IOException {
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
