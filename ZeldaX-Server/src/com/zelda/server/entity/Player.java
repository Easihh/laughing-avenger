package com.zelda.server.entity;

import java.awt.Rectangle;
import java.nio.ByteBuffer;

import com.zelda.common.Constants;

import static com.zelda.common.Constants.ObjectState.ACTIVE;
import static com.zelda.common.Constants.ObjectType.HERO;
import static com.zelda.common.Constants.MessageType.OBJ_REMOVAL;
import static com.zelda.common.Constants.MessageType.POSITION;

public class Player extends ServerGameObject {

    private static int heroId = 0;
    private static int WIDTH = 32;
    private static int HEIGHT = 32;
    private String direction = "";

    public Player() {
        xPosition = 0;
        yPosition = 0;
        prevSentXPosition = Integer.MIN_VALUE;
        prevSentYPosition = Integer.MIN_VALUE;
        direction = Constants.Movement.UP;
        heroId++;
        idIdentifier = heroId;
        width = WIDTH;
        height = HEIGHT;
        objState = ACTIVE;
        mask = new Rectangle(xPosition, yPosition, width, height);
    }

    @Override
    public byte[] convertToBytes() {
        if (ACTIVE.equals(objState)) {
            return convertToBytesActive();
        }
        return convertToBytesInactive();
    }

    private byte[] convertToBytesInactive() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        buffer.putInt(OBJ_REMOVAL);
        buffer.put(HERO.getBytes());
        buffer.putInt(idIdentifier);
        buffer.flip();
        return buffer.array();
    }

    private byte[] convertToBytesActive() {
        ByteBuffer buffer = ByteBuffer.allocate(Constants.MessageLength.POSITION_LENGTH);
        buffer.putInt(POSITION);
        buffer.put(HERO.getBytes());
        buffer.putInt(idIdentifier);
        buffer.putInt(xPosition);
        buffer.putInt(yPosition);
        buffer.put(direction.getBytes());
        buffer.flip();
        return buffer.array();
    }

    @Override
    public String getTypeIdenfitier() {
        return HERO;
    }
}
