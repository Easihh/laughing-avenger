package com.zelda.server.entity;

import com.zelda.common.GameObject;

public abstract class ServerGameObject extends GameObject {

    protected int prevSentXPosition;
    protected int prevSentYPosition;
    protected String prevSentDirection;
    protected int prevSentState;

    protected String direction = "";

    protected int objState;

    public abstract byte[] convertToBytes();
    
    public abstract void update();
    
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
        prevSentState = objState;
    }

    public int getObjState() {
        return objState;
    }

    public void setObjState(int objState) {
        this.objState = objState;
    }
    
    public int getPrevSentState() {
        return prevSentState;
    }
}
