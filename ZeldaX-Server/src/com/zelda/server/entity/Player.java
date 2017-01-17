package com.zelda.server.entity;

import java.awt.Rectangle;
import java.nio.ByteBuffer;

import com.zelda.common.Constants;
import com.zelda.common.network.ObjectRemovalMessage;

import static com.zelda.common.Constants.ObjectState.NORMAL;
import static com.zelda.common.Constants.ObjectState.INACTIVE;
import static com.zelda.common.Constants.ObjectState.ATTACKING;
import static com.zelda.common.Constants.ObjectType.HERO;
import static com.zelda.common.Constants.MessageType.POSITION;
import static com.zelda.common.Constants.SERVER_LOOP_DELAY;

public class Player extends ServerGameObject {

    private static int heroId = 0;
    private static int WIDTH = 32;
    private static int HEIGHT = 32;
    private final int MAX_SWORD_ATTACK_ANIMATION_DURATION = 600; //actually 500(same as client) but add 100 due to server loop.
    private int CURRENT_SWORD_ATTACK_ANIMATION_DURATION = 0;
    
    public Player() {
        xPosition = 128;
        yPosition = 160;
        prevSentXPosition = Integer.MIN_VALUE;
        prevSentYPosition = Integer.MIN_VALUE;
        direction = Constants.Movement.UP;
        heroId++;
        idIdentifier = heroId;
        width = WIDTH;
        height = HEIGHT;
        objState = NORMAL;
        mask = new Rectangle(xPosition, yPosition, width, height);
    }

    @Override
    public byte[] convertToBytes() {
        if (INACTIVE == objState) {
            return new ObjectRemovalMessage(HERO, idIdentifier).getBytes();
        }
        return convertToBytesActive();
    }

    private byte[] convertToBytesActive() {
        ByteBuffer buffer = ByteBuffer.allocate(Constants.MessageLength.POSITION_LENGTH);
        buffer.putInt(POSITION);
        buffer.put(HERO.getBytes());
        buffer.putInt(idIdentifier);
        buffer.putInt(xPosition);
        buffer.putInt(yPosition);
        buffer.put(direction.getBytes());
        buffer.putInt(objState);
        buffer.flip();
        return buffer.array();
    }

    @Override
    public String getTypeIdenfitier() {
        return HERO;
    }
    
    public String getDirection() {
        return direction;
    }
    
    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void triggerPlayerAttack() {
        objState = ATTACKING;
    }

    @Override
    public void update() {
        if (objState == ATTACKING) {
            CURRENT_SWORD_ATTACK_ANIMATION_DURATION += SERVER_LOOP_DELAY;
            if (CURRENT_SWORD_ATTACK_ANIMATION_DURATION >= MAX_SWORD_ATTACK_ANIMATION_DURATION) {
                CURRENT_SWORD_ATTACK_ANIMATION_DURATION = 0;
                objState = NORMAL;
            }
        }
    }
}
