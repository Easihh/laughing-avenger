package com.zelda.common.network;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import com.zelda.common.Constants;

public class ByteMessageReader {
    private static int currentType = -1;
    private static int typeLength = -1;
    private static byte[] typeArr = new byte[Constants.MessageType.LENGTH];
    private static int typeArrIndex = 0;
    
    public List<Message> decodeMessage(ByteBuffer messageBuffer) {
        ArrayList<Message> messageList = new ArrayList<Message>();
        messageBuffer.flip();
        
        /**
         * Traverse buffer while keeping in mind it is possible we have partial
         * message or partial byte of actual values so we keep track of values
         * until we know we have a complete message to parse.
         */
        while (messageBuffer.remaining() > 0) {
            if (currentType == -1) {
                while (typeArrIndex < typeArr.length && messageBuffer.hasRemaining()) {
                    ByteMessageReader.typeArr[ByteMessageReader.typeArrIndex] = messageBuffer.get();
                    typeArrIndex++;
                }
                /**Possible Partial Message so we can't determine type yet **/ 
                if (typeArrIndex < typeArr.length) {
                    return messageList;
                }
                ByteBuffer buffer = ByteBuffer.wrap(typeArr);
                currentType = buffer.getInt();
                /**
                 * Invalid message type should disconnect player/show error
                 * instead of invaliding whole buffer as it should not be
                 * possible to receive such message
                 */
                if (!Constants.messageTypeLengthMap.containsKey(currentType)) {
                    System.out.println("Invalid message of type:" + currentType);
                    this.reset();
                    return messageList;
                }
            }
            if (typeLength != -1)
                continue;
            Message currentValue = this.getValueByMessageType(messageBuffer);
            messageList.add(currentValue);
            System.out.println("Message was:" + currentValue.toString());
            this.reset();
        }
        return messageList;
    }

    private void reset() {
        currentType = -1;
        typeArrIndex = 0;
    }

    private Message getValueByMessageType(ByteBuffer messageBuffer) {
        Message msg = null;
        switch (currentType) {
        case Constants.MessageType.MOVEMENT: {
            msg = new MovementMessage(messageBuffer);
            break;
        }
        case Constants.MessageType.POSITION: {
            msg = new PositionMessage(messageBuffer);
            break;
        }
        case Constants.MessageType.OBJ_REMOVAL: {
            msg = new ObjectRemovalMessage(messageBuffer);
            break;
        }
        case Constants.MessageType.HERO_ID: {
            msg = new HeroIdentiferMessage(messageBuffer);
        }
        }
        return msg;
    }
}
