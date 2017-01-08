package com.zelda.server.entity;

import com.zelda.common.GameObject;

public abstract class ServerGameObject extends GameObject {

    protected int prevSentXPosition;
    protected int prevSentYPosition;
    protected String prevSentDirection;

    protected String direction = "";

    protected String objState;

    public abstract byte[] convertToBytes();
    
    public String getPrevSentDirection() {
        return prevSentDirection;
    }
        
    public String getDirection() {
        return direction;
    }
    
    public int getPrevSentXPosition() {
        return prevSentXPosition;
    }

    public int getPrevSentYPosition() {
        return prevSentYPosition;
    }

    public void updateLastSent() {
        prevSentXPosition = xPosition;
        prevSentYPosition = yPosition;
        prevSentDirection = direction;
    }

    public String getObjState() {
        return objState;
    }

    public void setObjState(String objState) {
        this.objState = objState;
    }
}
