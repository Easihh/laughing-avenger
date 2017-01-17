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

    public List<ServerMessage> process(ClientConnection clientConnection, Queue<Message> clientMessages) {
        List<ServerMessage> serverMessages = new ArrayList<ServerMessage>();
        while (!clientMessages.isEmpty()) {
            Message current = clientMessages.poll();
            int messageType = current.getType();
            Message lastMessageOfSameType = clientConnection.getLastMessageByType().get(messageType);
            if (lastMessageOfSameType != null) {
                long timeCreated = lastMessageOfSameType.getTimeCreated();
                long now = Calendar.getInstance().getTime().getTime();
                if (now - timeCreated < CLIENT_MOVEMENT_DELAY_TO_SERVER) {
                    System.out.println("Player is Cheating");
                    //should disconnect/ban player here.
                }
                System.out.println("Time Between last messages:" + (now - timeCreated));
            }
            clientConnection.getLastMessageByType().put(messageType, current);
            if (MessageType.MOVEMENT == current.getType()) {
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
        }
        return serverMessages;
    }
}
