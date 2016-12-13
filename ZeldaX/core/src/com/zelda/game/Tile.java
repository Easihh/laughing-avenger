package com.zelda.game;

import java.awt.Rectangle;
import java.util.Collection;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.zelda.common.Constants;

public class Tile extends ClientGameObject{
    
    private Texture texture;
    
    public Tile(int x,int y){
        xPosition=x;
        yPosition=y;
        width=32;
        height=32;
        mask=new Rectangle(xPosition,yPosition,width,height);
        texture = new Texture(Gdx.files.internal("Tree.png")); // #9
        
    }

    @Override
    public void loadPosition(PositionUpdater positionUpdater) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void update(Collection<ClientGameObject> collection) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer,SpriteBatch sprBatch) {
        sprBatch.draw(texture, xPosition, yPosition);
        //shapeRenderer.setColor(Color.FOREST);
        //shapeRenderer.rect(xPosition, yPosition, width, height);
    }

    @Override
    public String getTypeIdenfitier() {
        return Constants.ObjectType.TILE;
    }

}
