package com.zelda.server.entity;

import com.zelda.common.GameObject;

public abstract class ServerGameObject extends GameObject {

    protected int prevSentXPosition;
    protected int prevSentYPosition;
    protected String objState;

    public abstract byte[] convertToBytes();

    public int getPrevSentXPosition() {
        return prevSentXPosition;
    }

    public int getPrevSentYPosition() {
        return prevSentYPosition;
    }

    public void updateLastSentPosition() {
        prevSentXPosition = xPosition;
        prevSentYPosition = yPosition;
    }

    public String getObjState() {
        return objState;
    }

    public void setObjState(String objState) {
        this.objState = objState;
    }
}
