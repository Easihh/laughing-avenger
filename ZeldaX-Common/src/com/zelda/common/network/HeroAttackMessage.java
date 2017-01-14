package com.zelda.common.network;

import com.zelda.common.Constants;
import com.zelda.common.network.Message;
import java.nio.ByteBuffer;

public class HeroAttackMessage implements Message {
    private int heroId;
    private String heroIdentifier;
    
    public HeroAttackMessage(){}

    public HeroAttackMessage(String identifier, int id) {
        heroIdentifier = identifier;
        heroId = id;
    }

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getTotalLength());
        buffer.putInt(this.getType());
        buffer.put(this.heroIdentifier.getBytes());
        buffer.putInt(this.heroId);
        buffer.flip();
        return buffer.array();
    }

    public String getFullIdentifier() {
        return heroIdentifier + heroId;
    }

    public String toString() {
        return "Type:" + this.getType();
    }

    public int getTotalLength() {
        return Constants.messageTypeLengthMap.get(getType());
    }

    public int getType() {
        return Constants.MessageType.HERO_MAIN_ATTACK;
    }
}