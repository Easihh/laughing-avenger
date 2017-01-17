package com.zelda.common.network;

import com.zelda.common.Constants;
import com.zelda.common.network.Message;
import static com.zelda.common.Constants.MessageLength.OBJ_STR_TYPE_LENGTH;
import java.nio.ByteBuffer;

public class ObjectRemovalMessage extends Message {
    private String objType;
    private int id;
    
    public ObjectRemovalMessage(String type, int identifer) {
        objType = type;
        id = identifer;
    }
    
    /**
     * Message format: MessageType(Int)(4Bytes)->objType(String)(2Bytes) ->
     * identifier(Int)(4Bytes)
     **/
    public ObjectRemovalMessage(ByteBuffer messageBuffer) {
        byte[] objTypeArr = new byte[OBJ_STR_TYPE_LENGTH];
        
        messageBuffer.get(objTypeArr,0,OBJ_STR_TYPE_LENGTH);
        objType = new String(objTypeArr);
        id = messageBuffer.getInt();
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