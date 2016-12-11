package com.zelda.game;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.zelda.common.Constants;
import com.zelda.network.ServerWriter;

public class Hero extends ClientGameObject{
    
    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;
    private static final int SPEED=1;
    private Queue<PositionUpdater> updaterQueue;
    
    public Hero(int x,int y){
        updaterQueue = new LinkedList<PositionUpdater>();
        xPosition = x;
        yPosition = y;
        height = HEIGHT;
        width = WIDTH;
        mask=(new Rectangle(xPosition, yPosition, width, height));
    }
    
    @Override    
    public void update(Collection<ClientGameObject> collection){
        Point offset;
        //discard server position update for now
        updaterQueue.clear();
        //Some code here to check if Player is too far from server coord
        
        if(Gdx.input.isKeyPressed(Keys.RIGHT)){
            offset=new Point(1,0);
            if(!isColliding(collection,offset)){
                xPosition+=SPEED;
                ServerWriter.sendMessage("0001R");
            }
        }
        if(Gdx.input.isKeyPressed(Keys.LEFT)){
            offset=new Point(-1,0);
            if(!isColliding(collection,offset)){
                xPosition-=SPEED;
                ServerWriter.sendMessage("0001L");
            }
        }
        if(Gdx.input.isKeyPressed(Keys.UP)){
            yPosition+=SPEED;
            ServerWriter.sendMessage("0001U");
        }
        if(Gdx.input.isKeyPressed(Keys.DOWN)){
            yPosition-=SPEED;
            ServerWriter.sendMessage("0001D");
        }
        updateMask();
    }

    private boolean isColliding(Collection<ClientGameObject> collection, Point offset) {
        for(ClientGameObject obj:collection){
            if(intersect(mask, obj.getMask(), offset) && !mask.equals(obj.getMask())){
                return true;
            }
        }
        return false;
    }

    private void updateMask() {
        mask.setRect(xPosition, yPosition, width, height);
    }
    
    @Override  
    public void loadPosition(PositionUpdater positionUpdater) {
        updaterQueue.add(positionUpdater);
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.MAGENTA);
        shapeRenderer.rect(xPosition, yPosition, width, height);
    }

    @Override
    public String getTypeIdenfitier() {
         return Constants.ObjectType.HERO;
    }
}
