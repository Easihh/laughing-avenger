package com.zelda.server.message.processor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Queue;

import com.zelda.common.Constants.MessageType;
import com.zelda.common.network.Message;
import com.zelda.common.network.MovementMessage;
import com.zelda.server.ClientConnection;
import com.zelda.server.entity.Player;
import com.zelda.server.message.ServerHeroAttackMessage;
import com.zelda.server.message.ServerMessage;
import com.zelda.server.message.ServerPositionMessage;
import static com.zelda.common.Constants.CLIENT_MOVEMENT_DELAY_TO_SERVER;

public class ClientMessageProcessor {
    
    boolean testedForMovementCheat = false;
    
    public List<ServerMessage> process(ClientConnection clientConnection, Queue<Message> clientMessages) {
        List<ServerMessage> serverMessages = new ArrayList<ServerMessage>();
        while (!clientMessages.isEmpty()) {
            Message current = clientMessages.poll();
            int messageType = current.getType();
            if (MessageType.MOVEMENT == current.getType()) {
                testForMovementInputCheat(current,clientConnection);
                MovementMessage moveMessage = (MovementMessage) current;
                Player player = clientConnection.getHero();
                String identifier = player.getFullIdentifier();
                String direction = moveMessage.getDirection();
                serverMessages.add(new ServerPositionMessage(identifier, direction));
            }
            if (MessageType.HERO_MAIN_ATTACK == current.getType()) {
                Player player = clientConnection.getHero();
                String identifier = player.getFullIdentifier();
                serverMessages.add(new ServerHeroAttackMessage(identifier));
            }
            if (!hasNextMessageofSameType(messageType, current, clientMessages)) {
                clientConnection.getLastMessageByType().put(messageType, current);
            }
        }
        testedForMovementCheat = false;
        return serverMessages;
    }
    
    /**
     * Check if List of Message contains more message of the same type since we
     * want to only add/update the last message time of our map of last message
     * by type.
     */
    private boolean hasNextMessageofSameType(int messageType, Message current, Queue<Message> clientMessages) {
        for (Message msg : clientMessages) {
            if (msg.getType() == messageType && msg != clientMessages) {
                return true;
            }
        }
        return false;
    }

    /**
     * Movement Inputs are sent in bulks(from 1 to about 7 commands) from client
     * to player. It is possible to modify the client and change the rate of
     * commands sent and gain an unfair advantage. Since message are sent in
     * bulk we only want to compare the previous message of the same type from
     * the last bulk to the current bulk message in order to detect if the
     * client sending rate was manipulated.
     * @param clientConnection 
     */
    private void testForMovementInputCheat(Message current, ClientConnection clientConnection) {
        if (testedForMovementCheat) {
            return;
        }
        int messageType = current.getType(); 
        Message lastMessageOfSameType = clientConnection.getLastMessageByType().get(messageType);
        if (lastMessageOfSameType != null) {
            long timeCreated = lastMessageOfSameType.getTimeCreated();
            long now = Calendar.getInstance().getTime().getTime();
            if (now - timeCreated < CLIENT_MOVEMENT_DELAY_TO_SERVER) {
                System.out.println("Player"+clientConnection.getHero().getFullIdentifier()+" is Cheating");
                //should disconnect/ban player here.
            }
            System.out.println("Time Between last messages:" + (now - timeCreated));
            testedForMovementCheat = true;
        }
    }
}
