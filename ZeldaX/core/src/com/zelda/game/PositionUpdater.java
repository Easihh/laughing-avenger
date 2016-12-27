package com.zelda.game;

public class PositionUpdater {

    private int serverX;
    private int serverY;
    private String direction;

    public PositionUpdater(int x, int y,String dir) {
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
}
