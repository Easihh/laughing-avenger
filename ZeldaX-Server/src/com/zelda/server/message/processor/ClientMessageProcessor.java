package com.zelda.server.message.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.zelda.common.Constants;
import com.zelda.common.network.HeroAttackMessage;
import com.zelda.common.network.Message;
import com.zelda.common.network.MovementMessage;
import com.zelda.server.ClientConnection;
import com.zelda.server.entity.Player;
import com.zelda.server.message.ServerHeroAttackMessage;
import com.zelda.server.message.ServerMessage;
import com.zelda.server.message.ServerPositionMessage;

public class ClientMessageProcessor {

    public List<ServerMessage> process(ClientConnection clientConnection, Queue<Message> clientMessages) {
        List<ServerMessage> serverMessages = new ArrayList<ServerMessage>();
        while (!clientMessages.isEmpty()) {
            Message current = clientMessages.poll();
            if (current instanceof MovementMessage) {
                MovementMessage moveMessage = (MovementMessage) current;
                Player player = clientConnection.getHero();
                String identifier = player.getFullIdentifier();
                String direction = moveMessage.getDirection();
                serverMessages.add(new ServerPositionMessage(identifier, direction));
            }
            if (current instanceof HeroAttackMessage) {
                Player player = clientConnection.getHero();
                String identifier = player.getFullIdentifier();
                serverMessages.add(new ServerHeroAttackMessage(identifier));
            }
        }
        return serverMessages;
    }
}
