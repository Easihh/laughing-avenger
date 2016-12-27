package com.zelda.common.network;

import com.zelda.common.Constants;
import com.zelda.common.network.Message;
import java.nio.ByteBuffer;

public class PositionMessage implements Message {
    private int x;
    private int y;
    private String objType;
    private int id;
    private String direction;
    
    /**
     * Message format: MessageType(Int)(4Bytes)->objType(String)(2Bytes) ->
     * identifier(Int)(4Bytes) ->xPos(Int)(4Bytes)->yPos(Int)(4Bytes)-> direction(String)(1Bytes)
     **/
    public PositionMessage(ByteBuffer messageBuffer) {
        
        byte[] objTypeArr = new byte[Constants.MessageLength.OBJ_STR_TYPE_LENGTH];
        byte[] identifierArr = new byte[Constants.MessageLength.OBJ_INT_IDENTIFIER_LENGTH];
        byte[] xPositionArr = new byte[Constants.MessageLength.POSITION_X];
        byte[] yPositionArr = new byte[Constants.MessageLength.POSITION_Y];
        byte[] directionArr = new byte[1];
        
        for (int i = 0; i < objTypeArr.length; i++) {
            objTypeArr[i] = messageBuffer.get();
        }
        objType = new String(objTypeArr);
        for (int i = 0; i < identifierArr.length; i++) {
            identifierArr[i] = messageBuffer.get();
        }
        ByteBuffer buffer = ByteBuffer.wrap(identifierArr);
        id = buffer.getInt();
        for (int i = 0; i < xPositionArr.length; ++i) {
            xPositionArr[i] = messageBuffer.get();
        }
        buffer = ByteBuffer.wrap(xPositionArr);
        x = buffer.getInt();
        
        for (int i = 0; i < yPositionArr.length; ++i) {
            yPositionArr[i] = messageBuffer.get();
        }
        buffer = ByteBuffer.wrap(yPositionArr);
        y = buffer.getInt();
        
        for (int i = 0; i < directionArr.length; ++i) {
            directionArr[i] = messageBuffer.get();
        }       
        direction = new String(directionArr);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
    
    public String getDirection() {
        return direction;
    }

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getTotalLength());
        buffer.putInt(getType());
        buffer.put(objType.getBytes());
        buffer.putInt(id);
        buffer.putInt(x);
        buffer.putInt(y);
        buffer.put(direction.getBytes());
        buffer.flip();
        return buffer.array();
    }

    public String getObjType() {
        return this.objType;
    }

    public String getFullIdentifier() {
        return objType + id;
    }

    public String toString() {
        return "Type:" + getType() + " ObjectType:" + objType + " Identifier:" + id + " xPosition:"
                        + x + " yPosition:" + y +" direction:"+direction;
    }

    public int getTotalLength() {
        return Constants.messageTypeLengthMap.get(getType());
    }

    public int getType() {
        return Constants.MessageType.POSITION;
    }
}