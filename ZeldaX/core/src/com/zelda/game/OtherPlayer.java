package com.zelda.game;

import java.awt.Rectangle;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zelda.common.Constants;
import com.zelda.common.Quadtree;

public class OtherPlayer extends ClientGameObject {

    private Queue<PositionUpdater> updaterQueue;
    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;
    private SimpleAnimation walkAnimation;
    private SimpleAnimation attackAnimation;
    private HeroSword hSword;
    private int direction;
    private int speed = 2;
    private int state;

    public OtherPlayer(int x, int y,int dir) {
        updaterQueue = new LinkedList<PositionUpdater>();
        xPosition = x;
        yPosition = y;
        direction = dir;
        mask = new Rectangle(xPosition, yPosition, width, height);
        state = Constants.ObjectState.NORMAL;
        setupMovementAnimation();
    }
    private void setupMovementAnimation() {
        List<FileHandle> files = new LinkedList<FileHandle>();
        files.add(Gdx.files.internal("Link_Movement_Up.png"));
        files.add(Gdx.files.internal("Link_Movement_Down.png"));
        files.add(Gdx.files.internal("Link_Movement_Left.png"));
        files.add(Gdx.files.internal("Link_Movement_Right.png"));
        walkAnimation = new SimpleAnimation(files, WIDTH, HEIGHT, 0.25f);
        
        List<FileHandle> afiles=new LinkedList<FileHandle>();
        afiles.add(Gdx.files.internal("Link_AttackUp.png"));
        afiles.add(Gdx.files.internal("Link_AttackDown.png"));
        afiles.add(Gdx.files.internal("Link_AttackLeft.png"));
        afiles.add(Gdx.files.internal("Link_AttackRight.png"));
        attackAnimation = new SimpleAnimation(afiles, WIDTH, HEIGHT, 0.50f);
    }

    public void update(Collection<ClientGameObject> activeCollection,Quadtree<Tile> quadTree) {
        updateState();
        movement();
    }

    private void updateState() {
        PositionUpdater updater = updaterQueue.peek();
        if (updater == null) {
            return;
        }
        
        int oldState = state;
        if (oldState != updater.getState()) {
            state = updater.getState();
            if(oldState==Constants.ObjectState.ATTACKING){
                hSword = null;
            }
        }
    }
    private void movement() {
        PositionUpdater updater = updaterQueue.peek();
        int oldDirection = direction;
        if (updater == null) {
            return;
        }

        if (updater.getserverX() == xPosition && updater.getserverY() == yPosition
                        && updater.getDirection() == direction && updater.getState() == state) {
            if (state == Constants.ObjectState.ATTACKING) {
                hSword = new HeroSword(xPosition, yPosition, direction);
            }
            updaterQueue.poll();// we are already at server position; discard
                                // update message.
            return;
        }
        if (xPosition != updater.getserverX()) {
            int xDifference = xPosition - updater.getserverX();
            updateXMovement(xDifference);
            direction=updater.getDirection();
            checkWalkAnimationStateTime(oldDirection);
            return;
        }

        if (yPosition != updater.getserverY()) {
            int yDifference = yPosition - updater.getserverY();
            direction=updater.getDirection();
            updateYMovement(yDifference);
            checkWalkAnimationStateTime(oldDirection);
            return;
        }
        if (updater.getserverX() == xPosition && updater.getserverY() == yPosition
                        && updater.getDirection() != direction) {
            direction = updater.getDirection();
            checkWalkAnimationStateTime(oldDirection);
        }
    }
    
    private void checkWalkAnimationStateTime(int oldDirection) {
        if (oldDirection != direction) {
            walkAnimation.resetStateTime(direction);
        }
        walkAnimation.addStateTime(direction, Gdx.graphics.getDeltaTime());
    }
    
    private void updateXMovement(int xDifference) {
        if (xDifference > 0) {
            xPosition -= Math.min(xDifference, speed);

        } else {
            xPosition += Math.min(speed, Math.abs(xDifference));
        }
    }
    /**yDifference is difference between current yPosition and serverY position **/
    private void updateYMovement(int yDifference) {
        if (yDifference > 0) {
            yPosition -= Math.min(yDifference, speed);
            direction = Constants.Direction.UP;

        } else {
            yPosition += Math.min(speed, Math.abs(yDifference));
            direction = Constants.Direction.DOWN;
        }
    }
    
    public void loadPosition(PositionUpdater positionUpdater) {
        updaterQueue.add(positionUpdater);
    }

    @Override
    public void draw(SpriteBatch sprBatch) {
        if (hSword != null) {
            hSword.draw(sprBatch);
            sprBatch.draw(attackAnimation.getCurrentFrame(direction), xPosition, yPosition);
        }
        else sprBatch.draw(walkAnimation.getCurrentFrame(direction), xPosition, yPosition);
    }

    @Override
    public String getTypeIdenfitier() {
        return Constants.ObjectType.HERO;
    }
}
