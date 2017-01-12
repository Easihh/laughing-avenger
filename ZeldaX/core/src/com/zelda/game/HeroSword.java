package com.zelda.game;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zelda.common.GameObject;
import com.zelda.common.Quadtree;
import static com.zelda.common.Constants.Direction.UP;
import static com.zelda.common.Constants.Direction.DOWN;
import static com.zelda.common.Constants.Direction.LEFT;
import static com.zelda.common.Constants.Direction.RIGHT;
import static com.zelda.common.Constants.Size.MAX_TILE_HEIGHT;
import static com.zelda.common.Constants.Size.MAX_TILE_WIDTH;
import static com.zelda.common.Constants.ObjectType.NORMAL_SWORD;

public class HeroSword extends GameObject{
    
    private Sprite spr;
    private Texture texture;
    private int direction;
    private final float maxDuration = 0.50f;
    private float currentDuration;
    private static final Map<String, Texture> textureMap = GameResources.getInstance().getTextureMap();
    
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
            height = (MAX_TILE_HEIGHT / 2);
            xPosition = heroX + (width / 2);
            yPosition = heroY - height - (height / 2);
            break;
        case DOWN:
            width = 10;
            height = (MAX_TILE_HEIGHT / 2);
            xPosition = heroX + width;
            yPosition = heroY + height + (width / 2);
            break;
        case LEFT:
            width = (MAX_TILE_WIDTH / 2);
            height = 10;
            xPosition = heroX - width - (width / 4);
            yPosition = heroY + height;
            break;
        case RIGHT:
            width = (MAX_TILE_WIDTH / 2);
            height = 10;
            xPosition = heroX + MAX_TILE_WIDTH - (width / 2);
            yPosition = heroY + height;
            break;
        }
    }

    private void loadSprite() {
        switch (direction) {
        case UP:
            texture = textureMap.get("WoodSword_Up");
            break;
        case DOWN:
            texture = textureMap.get("WoodSword_Down");
            break;
        case LEFT:
            texture = textureMap.get("WoodSword_Left");
            break;
        case RIGHT:
            texture = textureMap.get("WoodSword_Right");
            break;
        }
        spr = new Sprite(texture);
        spr.flip(false, true);
    }

    public void draw(SpriteBatch sprBatch) {
        sprBatch.draw(spr, xPosition, yPosition);
    }

    public void update(Collection<ClientGameObject> activeObjs, Quadtree<Tile> quadTree) {
        currentDuration += Gdx.graphics.getDeltaTime();
    }

    @Override
    public String getTypeIdenfitier() {
        return NORMAL_SWORD;
    }
        
    public float getMaxDuration() {
        return maxDuration;
    }

    public float getCurrentDuration() {
        return currentDuration;
    }

}
