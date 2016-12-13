package com.zelda.game;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.zelda.common.Constants;

public class OtherPlayer extends ClientGameObject{

    private Queue<PositionUpdater> updaterQueue;
    private int speed=2;
    
    private Texture walkSheet;
    private TextureRegion[] walkFrames;
    private TextureRegion currentFrame;
    private Animation walkAnimation;
    private float stateTime; 
    private final static int FRAME_COLS=2;
    private final static int FRAME_ROWS=1;
    
    public OtherPlayer(int x,int y){
        updaterQueue=new LinkedList<PositionUpdater>();
        xPosition=x;
        yPosition=y;
        mask=new Rectangle(xPosition,yPosition,width,height);
        
        walkSheet = new Texture(Gdx.files.internal("Link_Movement.png")); // #9
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, 32, 32);              // #10
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation(0.5f, walkFrames); 
        stateTime = 0f;
        currentFrame = walkAnimation.getKeyFrame(stateTime, true); 
    }
        
    public void update(Collection<ClientGameObject> collection){
        movement();    
    }
    
    private void movement() {
        boolean isMoving=true;
        PositionUpdater current=updaterQueue.peek();
        if(current==null){
            return;
        }
        
        if(current.getserverX()==xPosition  && current.getserverY()==yPosition){
            updaterQueue.poll();//we are already at server position; discard update message.
            return ;
        }
        stateTime += Gdx.graphics.getDeltaTime();           // #15
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);  // #16
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
    public void draw(ShapeRenderer shapeRenderer,SpriteBatch sprBatch) {
        sprBatch.draw(currentFrame, xPosition, yPosition);             // #17
        //shapeRenderer.setColor(Color.BLUE);
        //shapeRenderer.rect(xPosition, yPosition, 32, 32);
    }

    @Override
    public String getTypeIdenfitier() {
         return Constants.ObjectType.HERO;
    }
}
