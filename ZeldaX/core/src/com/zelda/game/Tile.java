package com.zelda.game;

import java.awt.Rectangle;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zelda.common.Constants;
import com.zelda.common.GameObject;

public class Tile extends GameObject {

    private Texture texture;
    private final static int WIDTH = 32;
    private final static int HEIGHT = 32;
    private Sprite spr;
    private static final Map<String, Texture> textureMap = GameResources.getInstance().getTextureMap();

    public Tile(int x, int y) {
        xPosition = x;
        yPosition = y;
        width = WIDTH;
        height = HEIGHT;
        mask = new Rectangle(xPosition, yPosition, width, height);
        texture = textureMap.get("Tree");
        spr=new Sprite(texture);
        spr.flip(false, true);
    }

    public void draw(SpriteBatch sprBatch) {
        sprBatch.draw(spr, xPosition, yPosition);
    }

    @Override
    public String getTypeIdenfitier() {
        return Constants.ObjectType.TILE;
    }

}
