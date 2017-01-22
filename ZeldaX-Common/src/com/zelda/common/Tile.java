package com.zelda.common;

import java.awt.Rectangle;
import com.zelda.common.GameObject;
import com.zelda.common.Constants.Size;
import static com.zelda.common.Constants.ObjectType.TILE;

public class Tile extends GameObject {

    protected final static int HEIGHT = Size.MAX_TILE_HEIGHT * Size.WORLD_SCALE_Y;
    protected final static int WIDTH = Size.MAX_TILE_WIDTH * Size.WORLD_SCALE_X;

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
