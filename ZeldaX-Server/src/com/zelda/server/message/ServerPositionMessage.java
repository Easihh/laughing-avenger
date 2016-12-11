package com.zelda.server.message;

import static com.zelda.common.Constants.ServerMessageType.MOVEMENT;

public class ServerPositionMessage implements ServerMessage {

    private String identifier;
    private String direction;

    public ServerPositionMessage(String id, String dir) {
        identifier = id;
        direction = dir;
    }

    public String getDirection() {
        return direction;
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public int getType() {
        return MOVEMENT;
    }
}
