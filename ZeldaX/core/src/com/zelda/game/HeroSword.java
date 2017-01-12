package com.zelda.game;

import java.awt.Rectangle;
import java.util.Collection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zelda.common.Constants;
import com.zelda.common.Quadtree;
import static com.zelda.common.Constants.Direction.UP;
import static com.zelda.common.Constants.Direction.DOWN;
import static com.zelda.common.Constants.Direction.LEFT;
import static com.zelda.common.Constants.Direction.RIGHT;

public class HeroSword extends ClientGameObject{
    
    private Sprite spr;
    private Texture texture;
    private int direction;
    private final float maxDuration = 0.50f;
    private float currentDuration;
    
    public HeroSword(int heroX,int heroY,int dir) {
        direction = dir;
        setSwordPosition(heroX,heroY);
        mask = new Rectangle(xPosition, yPosition, width, height);
        loadSprite();
        currentDuration = 0f;
    }

    private void setSwordPosition(int heroX, int heroY) {
        switch (direction) {
        case UP:
            width = 10;
            height = 16;
            xPosition = heroX + (width / 2);
            yPosition = heroY - height - (height / 2);
            break;
        case DOWN:
            width = 10;
            height = 16;
            xPosition = heroX + width;
            yPosition = heroY + height + (width / 2);
            break;
        case LEFT:
            width = 16;
            height = 10;
            xPosition = heroX - width - (width / 4);
            yPosition = heroY + height;
            break;
        case RIGHT:
            width = 16;
            height = 10;
            xPosition = heroX + Constants.Size.MAX_TILE_WIDTH - (width / 2);
            yPosition = heroY + height;
            break;
        }
    }

    private void loadSprite() {
        switch (direction) {
        case UP:
            texture = new Texture(Gdx.files.internal("WoodSword_Up.png"));
            break;
        case DOWN:
            texture = new Texture(Gdx.files.internal("WoodSword_Down.png"));
            break;
        case LEFT:
            texture = new Texture(Gdx.files.internal("WoodSword_Left.png"));
            break;
        case RIGHT:
            texture = new Texture(Gdx.files.internal("WoodSword_Right.png"));
            break;
        }
        spr = new Sprite(texture);
        spr.flip(false, true);
    }

    @Override
    public void loadPosition(PositionUpdater positionUpdater) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void draw(SpriteBatch sprBatch) {
        sprBatch.draw(spr, xPosition, yPosition);
    }

    @Override
    public void update(Collection<ClientGameObject> activeObjs, Quadtree<Tile> quadTree) {
        updateMask();
        currentDuration += Gdx.graphics.getDeltaTime();
    }

    @Override
    public String getTypeIdenfitier() {
        return "SW";
    }
    
    private void updateMask() {
        mask.setRect(xPosition, yPosition, width, height);
    }
    
    public float getMaxDuration() {
        return maxDuration;
    }

    public float getCurrentDuration() {
        return currentDuration;
    }

}
