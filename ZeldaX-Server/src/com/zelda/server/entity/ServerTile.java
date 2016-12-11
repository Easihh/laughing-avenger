package com.zelda.server.entity;

import java.awt.Rectangle;
import java.nio.ByteBuffer;

import static com.zelda.common.Constants.MessageType.POSITION;
import static com.zelda.common.Constants.ObjectType.TILE;

public class ServerTile extends ServerGameObject {

    private static int tileId = 0;
    private static int HEIGHT = 32;
    private static int WIDTH = 32;

    public ServerTile(int x, int y) {
        tileId++;
        idIdentifier = tileId;
        xPosition = x;
        yPosition = y;
        prevSentXPosition = xPosition;
        prevSentYPosition = yPosition;
        width = WIDTH;
        height = HEIGHT;
        mask = new Rectangle(xPosition, yPosition, width, height);
    }

    @Override
    public byte[] convertToBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(18);
        buffer.putInt(POSITION);
        buffer.put(TILE.getBytes());
        buffer.putInt(idIdentifier);
        buffer.putInt(xPosition);
        buffer.putInt(yPosition);
        buffer.flip();
        return buffer.array();
    }

    @Override
    public String getTypeIdenfitier() {
        return TILE;
    }
}
