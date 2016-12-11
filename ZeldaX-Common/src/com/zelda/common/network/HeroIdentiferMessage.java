package com.zelda.common.network;

import com.zelda.common.Constants;
import com.zelda.common.network.Message;
import java.nio.ByteBuffer;

public class HeroIdentiferMessage implements Message {
    private int idValue;
    private String objectType;

    public HeroIdentiferMessage(String objType, int id) {
        this.objectType = objType;
        this.idValue = id;
    }
    
    /**
     * Message format: MessageType(Int)(4Bytes)->objType(String)(2Bytes) ->
     * identifier(Int)(4Bytes)
     **/
    public HeroIdentiferMessage(ByteBuffer messageBuffer) {
        byte[] typePart = new byte[Constants.MessageLength.OBJ_STR_TYPE_LENGTH];
        byte[] identifierPart = new byte[Constants.MessageLength.OBJ_INT_IDENTIFIER_LENGTH];
        for (int i = 0; i < typePart.length; ++i) {
            typePart[i] = messageBuffer.get();
        }
        this.objectType = new String(typePart);
        for (int i = 0; i < identifierPart.length; ++i) {
            identifierPart[i] = messageBuffer.get();
        }
        ByteBuffer buffer = ByteBuffer.wrap(identifierPart);
        this.idValue = buffer.getInt();
    }

    public int getIdValue() {
        return this.idValue;
    }

    public String getObjectType() {
        return this.objectType;
    }

    public String fullIdentifier() {
        return this.objectType + this.idValue;
    }
    
    @Override
    public int getTotalLength() {
        return Constants.messageTypeLengthMap.get(getType());
    }
    
    @Override
    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getTotalLength());
        buffer.putInt(this.getType());
        buffer.put(this.objectType.getBytes());
        buffer.putInt(this.idValue);
        buffer.flip();
        return buffer.array();
    }

    public String toString() {
        return "Type:" + this.getType() + " Value:" + this.objectType + this.idValue;
    }
    
    @Override
    public int getType() {
        return Constants.MessageType.HERO_ID;
    }
}