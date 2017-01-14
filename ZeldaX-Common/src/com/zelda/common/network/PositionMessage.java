package com.zelda.common.network;

import com.zelda.common.Constants;
import com.zelda.common.network.Message;
import java.nio.ByteBuffer;
import static com.zelda.common.Constants.MessageLength.OBJ_STR_TYPE_LENGTH;;

public class PositionMessage implements Message {
    private int x;
    private int y;
    private String objType;
    private int id;
    private String direction;
    private int objState;
    
    /**
     * Message format: MessageType(Int)(4Bytes)->objType(String)(2Bytes) ->
     * identifier(Int)(4Bytes) ->xPos(Int)(4Bytes)->yPos(Int)(4Bytes)-> direction(String)(1Bytes)-> State(Int)(4Bytes)
     **/
    public PositionMessage(ByteBuffer messageBuffer) {
        
        byte[] objTypeArr = new byte[OBJ_STR_TYPE_LENGTH];
        byte[] directionArr = new byte[1];

        messageBuffer.get(objTypeArr, 0, OBJ_STR_TYPE_LENGTH);
        objType = new String(objTypeArr);

        id = messageBuffer.getInt();
        x = messageBuffer.getInt();
        y = messageBuffer.getInt();

        messageBuffer.get(directionArr, 0, 1);
        direction = new String(directionArr);
        
        objState=messageBuffer.getInt();
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
        int length = getTotalLength();
        ByteBuffer buffer = ByteBuffer.allocate(length);
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
        return "Type:" + getType() + " ObjectType:" + objType + " Identifier:" + id + " xPosition:" + x + " yPosition:"
                        + y + " direction:" + direction + " state:" + objState;
    }

    public int getTotalLength() {
        return Constants.messageTypeLengthMap.get(getType());
    }

    public int getType() {
        return Constants.MessageType.POSITION;
    }
    
    public int getObjState() {
        return objState;
    }
}