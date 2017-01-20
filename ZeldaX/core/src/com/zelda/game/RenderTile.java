package com.zelda.game;

import java.util.Map;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zelda.common.Tile;

public class RenderTile extends Tile  {

    private Texture texture;
    private Sprite spr;
    private static final Map<String, Texture> textureMap = GameResources.getInstance().getTextureMap();

    public RenderTile(int x, int y) {
        super(x,y);
        texture = textureMap.get("Tree");
        spr=new Sprite(texture);
        spr.flip(false, true);
    }

    public void draw(SpriteBatch sprBatch) {
        sprBatch.draw(spr, xPosition, yPosition);
    }
}
