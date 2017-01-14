package com.zelda.server.message;

import static com.zelda.common.Constants.ServerMessageType.HERO_ATTACK;;

public class ServerHeroAttackMessage implements ServerMessage {

    private String heroIdentifier;

    public ServerHeroAttackMessage(String identifier) {
        heroIdentifier = identifier;
    }
    
    public String getHeroIdentifier() {
        return heroIdentifier;
    }
    
    @Override
    public int getType() {
        return HERO_ATTACK;
    }
}
