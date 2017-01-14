package com.zelda.game;

import com.zelda.common.network.PositionMessage;

public class PositionUpdater {

    private int serverX;
    private int serverY;
    private int direction;
    private int state;

    public PositionUpdater(int x, int y,int dir) {
        serverX = x;
        serverY = y;
        direction = dir;
    }

    public PositionUpdater(PositionMessage pMessage) {
        int dir = GameUtility.directiontToInt(pMessage.getDirection());
        serverX = pMessage.getX();
        serverY = pMessage.getY();
        direction = dir;
        state = pMessage.getObjState();
    }

    public int getserverX() {
        return serverX;
    }

    public int getserverY() {
        return serverY;
    }
    
    public int getDirection() {
        return direction;
    }
    
    public int getState() {
        return state;
    }
}
