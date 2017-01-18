package com.zelda.common.network;

import com.zelda.common.Constants;
import com.zelda.common.network.Message;
import java.nio.ByteBuffer;
import java.util.Calendar;

public class MovementMessage extends Message {
    private String direction = "";
    
    
    public MovementMessage(String dir){
        timeCreated = Calendar.getInstance().getTime().getTime();
        direction = dir;
    }
    
    /**Message format: MessageType(Int)(4Bytes)->Direction(String)(1Bytes)**/
    public MovementMessage(ByteBuffer messageBuffer) {
        timeCreated = Calendar.getInstance().getTime().getTime();
        byte[] valuePart = new byte[Constants.MessageLength.MOVEMENT_DIRECTION];
        for (int i = 0; i < valuePart.length; ++i) {
            valuePart[i] = messageBuffer.get();
        }
        direction = new String(valuePart);
    }

    public String getDirection() {
        return direction;
    }

    public int addToPlayerX(int movementSpeed) {
        if ("L".equals(direction)) {
            return -movementSpeed;
        }
        if ("R".equals(direction)) {
            return movementSpeed;
        }
        return 0;
    }

    public int addToPlayerY(int movementSpeed) {
        if ("D".equals(direction)) {
            return -movementSpeed;
        }
        if ("U".equals(direction)) {
            return movementSpeed;
        }
        return 0;
    }

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getTotalLength());
        buffer.putInt(getType());
        buffer.put(direction.getBytes());
        buffer.flip();
        return buffer.array();
    }

    public String toString() {
        return "Type:" + getType() + " Value:" + direction;
    }

    public int getTotalLength() {
        return Constants.messageTypeLengthMap.get(getType());
    }

    public int getType() {
        return Constants.MessageType.MOVEMENT;
    }
}