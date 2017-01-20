package com.zelda.common;

import java.awt.Rectangle;
import com.zelda.common.GameObject;

import static com.zelda.common.Constants.ObjectType.TILE;

public class Tile extends GameObject {

    protected final static int HEIGHT = 32;
    protected final static int WIDTH = 32;

    public Tile(int x, int y) {
        xPosition = x;
        yPosition = y;
        width = WIDTH;
        height = HEIGHT;
        mask = new Rectangle(xPosition, yPosition, width, height);
    }

    @Override
    public String getTypeIdenfitier() {
        return TILE;
    }
}
