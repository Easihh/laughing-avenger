package com.zelda.common.network;

import com.zelda.common.Constants;
import com.zelda.common.network.Message;
import java.nio.ByteBuffer;

public class MovementMessage implements Message {
    private String direction = "";
    
    /**Message format: MessageType(Int)(4Bytes)->Direction(String)(1Bytes)**/
    public MovementMessage(ByteBuffer messageBuffer) {
        byte[] valuePart = new byte[Constants.MessageLength.MOVEMENT_DIRECTION];
        for (int i = 0; i < valuePart.length; ++i) {
            valuePart[i] = messageBuffer.get();
        }
        this.direction = new String(valuePart);
    }

    public String getDirection() {
        return this.direction;
    }

    public int addToPlayerX(int movementSpeed) {
        if ("L".equals(this.direction)) {
            return -movementSpeed;
        }
        if ("R".equals(this.direction)) {
            return movementSpeed;
        }
        return 0;
    }

    public int addToPlayerY(int movementSpeed) {
        if ("D".equals(this.direction)) {
            return -movementSpeed;
        }
        if ("U".equals(this.direction)) {
            return movementSpeed;
        }
        return 0;
    }

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getTotalLength());
        buffer.putInt(this.getType());
        buffer.put(this.direction.getBytes());
        buffer.flip();
        return buffer.array();
    }

    public String toString() {
        return "Type:" + this.getType() + " Value:" + this.direction;
    }

    public int getTotalLength() {
        return Constants.messageTypeLengthMap.get(getType());
    }

    public int getType() {
        return Constants.MessageType.MOVEMENT;
    }
}