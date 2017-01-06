package com.zelda.server.entity;

import java.awt.Rectangle;
import com.zelda.common.GameObject;

import static com.zelda.common.Constants.ObjectType.TILE;

public class Tile extends GameObject {

    private static int HEIGHT = 32;
    private static int WIDTH = 32;

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
