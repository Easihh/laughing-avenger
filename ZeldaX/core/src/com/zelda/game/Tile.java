package com.zelda.game;

import java.awt.Rectangle;
import java.util.Collection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zelda.common.Constants;
import com.zelda.common.Quadtree;

public class Tile extends ClientGameObject {

    private Texture texture;
    private final static int WIDTH = 32;
    private final static int HEIGHT = 32;
    private Sprite spr;

    public Tile(int x, int y) {
        xPosition = x;
        yPosition = y;
        width = WIDTH;
        height = HEIGHT;
        mask = new Rectangle(xPosition, yPosition, width, height);
        texture = new Texture(Gdx.files.internal("Tree.png"));
        spr=new Sprite(texture);
        spr.flip(false, true);
    }

    @Override
    public void loadPosition(PositionUpdater positionUpdater) {
        // TODO Auto-generated method stub
    }

    @Override
    public void update(Collection<ClientGameObject> ActiveCollection,Quadtree<ClientGameObject> quadTree) {
        // TODO Auto-generated method stub
    }

    @Override
    public void draw(SpriteBatch sprBatch) {
        sprBatch.draw(spr, xPosition, yPosition);
    }

    @Override
    public String getTypeIdenfitier() {
        return Constants.ObjectType.TILE;
    }

}
