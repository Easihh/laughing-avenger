package com.zelda.common.network;

import com.zelda.common.Constants;
import com.zelda.common.network.Message;
import java.nio.ByteBuffer;

public class ObjectRemovalMessage implements Message {
    private String objType;
    private int id;
    
    /**
     * Message format: MessageType(Int)(4Bytes)->objType(String)(2Bytes) ->
     * identifier(Int)(4Bytes)
     **/
    public ObjectRemovalMessage(ByteBuffer messageBuffer) {
        byte[] objTypeArr = new byte[Constants.MessageLength.OBJ_STR_TYPE_LENGTH];
        byte[] identifierArr = new byte[Constants.MessageLength.OBJ_INT_IDENTIFIER_LENGTH];
        
        for (int i = 0; i < objTypeArr.length; ++i) {
            objTypeArr[i] = messageBuffer.get();
        }
        this.objType = new String(objTypeArr);
        
        for (int i = 0; i < identifierArr.length; ++i) {
            identifierArr[i] = messageBuffer.get();
        }
        ByteBuffer buffer = ByteBuffer.wrap(identifierArr);
        this.id = buffer.getInt();
    }

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(getTotalLength());
        buffer.putInt(this.getType());
        buffer.put(this.objType.getBytes());
        buffer.putInt(this.id);
        buffer.flip();
        return buffer.array();
    }

    public String getFullIdentifier() {
        return this.objType + this.id;
    }

    public String toString() {
        return "Type:" + this.getType() + " ObjectType:" + this.objType + " Identifier:" + this.id + " State:"
                        + "REMOVED";
    }

    public int getTotalLength() {
        return Constants.messageTypeLengthMap.get(getType());
    }

    public int getType() {
        return Constants.MessageType.OBJ_REMOVAL;
    }
}