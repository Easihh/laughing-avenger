package com.zelda.game;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.zelda.common.Constants;

public class OtherPlayer extends ClientGameObject{

    private Queue<PositionUpdater> updaterQueue;
    private int speed=1;
    
    public OtherPlayer(int x,int y){
        updaterQueue=new LinkedList<PositionUpdater>();
        xPosition=x;
        yPosition=y;
        mask=new Rectangle(xPosition,yPosition,width,height);
    }
        
    public void update(Collection<ClientGameObject> collection){
        movement();
    }
    
    private void movement() {
        PositionUpdater current=updaterQueue.peek();
        if(current==null){
            return;
        }
        
        if(current.getserverX()==xPosition  && current.getserverY()==yPosition){
            updaterQueue.poll();//we are already at server position; discard update message.
            return ;
        }
        if(xPosition!=current.getserverX()){
            int xDifference=xPosition-current.getserverX();
            if(xDifference>0){
                xPosition-=Math.min(xDifference, speed);
                return ;
            }
            xPosition+=Math.min(speed, Math.abs(xDifference));
            return ;
        }
        
        if(yPosition!=current.getserverY()){
            int yDifference=yPosition-current.getserverY();
            if(yDifference>0){
                yPosition-=Math.min(yDifference, speed);
                return ;
            }
            yPosition+=Math.min(speed, Math.abs(yDifference));
            return ;
        }
    }

    public void loadPosition(PositionUpdater positionUpdater) {
        updaterQueue.add(positionUpdater);
    }

    @Override
    public void draw(ShapeRenderer shapeRenderer) {
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.rect(xPosition, yPosition, 32, 32);
    }

    @Override
    public String getTypeIdenfitier() {
         return Constants.ObjectType.HERO;
    }
}
