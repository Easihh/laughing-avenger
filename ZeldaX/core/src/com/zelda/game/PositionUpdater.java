package com.zelda.game;

public class PositionUpdater {

    private int serverX;
    private int serverY;

    public PositionUpdater(int x, int y) {
        serverX = x;
        serverY = y;
    }

    public int getserverX() {
        return serverX;
    }

    public int getserverY() {
        return serverY;
    }
}
