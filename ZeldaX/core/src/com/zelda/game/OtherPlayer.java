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

public class OtherPlayer extends ClientGameObject {

    private Queue<PositionUpdater> updaterQueue;
    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;
    private MovementAnimation walkAnimation;
    private int direction;
    private int speed = 2;

    public OtherPlayer(int x, int y) {
        updaterQueue = new LinkedList<PositionUpdater>();
        xPosition = x;
        yPosition = y;
        mask = new Rectangle(xPosition, yPosition, width, height);
        setupMovementAnimation();
    }
    private void setupMovementAnimation() {
        List<FileHandle> files = new LinkedList<FileHandle>();
        files.add(Gdx.files.internal("Link_Movement_Up.png"));
        files.add(Gdx.files.internal("Link_Movement_Down.png"));
        files.add(Gdx.files.internal("Link_Movement_Left.png"));
        files.add(Gdx.files.internal("Link_Movement_Right.png"));
        walkAnimation = new MovementAnimation(files, WIDTH, HEIGHT);
    }

    public void update(Collection<ClientGameObject> collection) {
        movement();
    }

    private void movement() {
        PositionUpdater current = updaterQueue.peek();
        int oldDirection = direction;
        if (current == null) {
            return;
        }

        if (current.getserverX() == xPosition && current.getserverY() == yPosition) {
            updaterQueue.poll();// we are already at server position; discard
                                // update message.
            return;
        }
        if (xPosition != current.getserverX()) {
            int xDifference = xPosition - current.getserverX();
            direction = getNewXMovementDirection(xDifference);
            checkWalkAnimationStateTime(oldDirection);
            return;
        }

        if (yPosition != current.getserverY()) {
            int yDifference = yPosition - current.getserverY();
            direction = getNewYMovementDirection(yDifference);
            checkWalkAnimationStateTime(oldDirection);
            return;
        }
    }
    
    private void checkWalkAnimationStateTime(int oldDirection) {
        if (oldDirection != direction) {
            walkAnimation.resetStateTime(direction);
        }
        walkAnimation.addStateTime(direction, Gdx.graphics.getDeltaTime());
    }
    
    private int getNewXMovementDirection(int xDifference) {
        int direction = -1;
        if (xDifference > 0) {
            xPosition -= Math.min(xDifference, speed);
            direction = Constants.Direction.LEFT;

        } else {
            xPosition += Math.min(speed, Math.abs(xDifference));
            direction = Constants.Direction.RIGHT;
        }
        return direction;
    }
    
    private int getNewYMovementDirection(int yDifference) {
        int direction = -1;
        if (yDifference > 0) {
            yPosition -= Math.min(yDifference, speed);
            direction = Constants.Direction.DOWN;

        } else {
            yPosition += Math.min(speed, Math.abs(yDifference));
            direction = Constants.Direction.UP;
        }
        return direction;
    }
    
    public void loadPosition(PositionUpdater positionUpdater) {
        updaterQueue.add(positionUpdater);
    }

    @Override
    public void draw(SpriteBatch sprBatch) {
        sprBatch.draw(walkAnimation.getCurrentFrame(direction), xPosition, yPosition);
    }

    @Override
    public String getTypeIdenfitier() {
        return Constants.ObjectType.HERO;
    }
}
