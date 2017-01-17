package com.zelda.common.network;

import com.zelda.common.Constants;
import com.zelda.common.network.Message;
import java.nio.ByteBuffer;
import java.util.Calendar;

public class HeroAttackMessage extends Message {
    
    public HeroAttackMessage() {
        timeCreated = Calendar.getInstance().getTime().getTime();
    }

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getTotalLength());
        buffer.putInt(this.getType());
        buffer.flip();
        return buffer.array();
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