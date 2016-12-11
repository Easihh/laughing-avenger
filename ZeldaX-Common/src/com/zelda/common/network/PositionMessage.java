package com.zelda.common.network;

import com.zelda.common.Constants;
import com.zelda.common.network.Message;
import java.nio.ByteBuffer;

public class PositionMessage implements Message {
    private int x;
    private int y;
    private String objType;
    private int id;
    
    /**
     * Message format: MessageType(Int)(4Bytes)->objType(String)(2Bytes) ->
     * identifier(Int)(4Bytes) ->xPos(Int)(4Bytes)->yPos(Int)(4Bytes)
     **/
    public PositionMessage(ByteBuffer messageBuffer) {
        
        byte[] objTypeArr = new byte[Constants.MessageLength.OBJ_STR_TYPE_LENGTH];
        byte[] identifierArr = new byte[Constants.MessageLength.OBJ_INT_IDENTIFIER_LENGTH];
        byte[] xPositionArr = new byte[Constants.MessageLength.POSITION_X];
        byte[] yPositionArr = new byte[Constants.MessageLength.POSITION_Y];
        
        for (int i = 0; i < objTypeArr.length; i++) {
            objTypeArr[i] = messageBuffer.get();
        }
        this.objType = new String(objTypeArr);
        for (int i = 0; i < identifierArr.length; i++) {
            identifierArr[i] = messageBuffer.get();
        }
        ByteBuffer buffer = ByteBuffer.wrap(identifierArr);
        this.id = buffer.getInt();
        for (int i = 0; i < xPositionArr.length; ++i) {
            xPositionArr[i] = messageBuffer.get();
        }
        buffer = ByteBuffer.wrap(xPositionArr);
        this.x = buffer.getInt();
        for (int i = 0; i < yPositionArr.length; ++i) {
            yPositionArr[i] = messageBuffer.get();
        }
        buffer = ByteBuffer.wrap(yPositionArr);
        this.y = buffer.getInt();
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getTotalLength());
        buffer.putInt(this.getType());
        buffer.put(this.objType.getBytes());
        buffer.putInt(this.id);
        buffer.putInt(this.x);
        buffer.putInt(this.y);
        buffer.flip();
        return buffer.array();
    }

    public String getObjType() {
        return this.objType;
    }

    public String getFullIdentifier() {
        return this.objType + this.id;
    }

    public String toString() {
        return "Type:" + this.getType() + " ObjectType:" + this.objType + " Identifier:" + this.id + " xPosition:"
                        + this.x + " yPosition:" + this.y;
    }

    public int getTotalLength() {
        return Constants.messageTypeLengthMap.get(getType());
    }

    public int getType() {
        return Constants.MessageType.POSITION;
    }
}