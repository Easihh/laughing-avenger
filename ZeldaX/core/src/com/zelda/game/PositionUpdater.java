package com.zelda.game;

public class PositionUpdater {

    private int serverX;
    private int serverY;
    private int direction;

    public PositionUpdater(int x, int y,int dir) {
        serverX = x;
        serverY = y;
        direction = dir;
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
}
