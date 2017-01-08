package com.zelda.game;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import static com.zelda.common.Constants.ObjectType.HERO;

import com.zelda.common.Quadtree;
import com.zelda.common.Constants.Command;
import com.zelda.common.Constants.Direction;
import com.zelda.network.ServerWriter;

public class Hero extends ClientGameObject {

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;
    private static final int SPEED = 2;
    private Queue<PositionUpdater> updaterQueue;

    private MovementAnimation walkAnimation;
    private int direction;
    private int prevDirection;

    public Hero(int x, int y) {
        updaterQueue = new LinkedList<PositionUpdater>();
        xPosition = x;
        yPosition = y;
        height = HEIGHT;
        width = WIDTH;
        mask = (new Rectangle(xPosition, yPosition, width, height));
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

    @Override
    public void update(Collection<ClientGameObject> ActiveCollection,Quadtree<Tile> quadTree) {
        boolean movementKeyPressed = false;
        Point offset;
        // discard server position update for now
        updaterQueue.clear();
        // TODO Some code here to check if Player is too far from server coord

        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            prevDirection = direction;
            direction = Direction.RIGHT;
            movementKeyPressed = true;
            offset = new Point(SPEED, 0);
            if (!isColliding(quadTree, offset) || prevDirection != direction) {
                xPosition += SPEED;       
                ServerWriter.sendMessage(Command.MOV_RIGHT);
            }
        }
        else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            prevDirection = direction;
            direction = Direction.LEFT;
            movementKeyPressed = true;
            offset = new Point(-SPEED, 0);
            if (!isColliding(quadTree, offset) || prevDirection != direction) {
                xPosition -= SPEED;
                ServerWriter.sendMessage(Command.MOV_LEFT);
            }
        }
        else if (Gdx.input.isKeyPressed(Keys.UP)) {
            prevDirection = direction;
            direction = Direction.UP;
            movementKeyPressed = true;
            offset = new Point(0, -SPEED);
            if (!isColliding(quadTree, offset) || prevDirection != direction) {
                yPosition -= SPEED;
                ServerWriter.sendMessage(Command.MOV_UP);
            }
        }
        else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            prevDirection = direction;
            direction = Direction.DOWN;
            movementKeyPressed = true;
            offset = new Point(0, SPEED);
            if (!isColliding(quadTree, offset) || prevDirection != direction) {
                yPosition += SPEED;
                ServerWriter.sendMessage(Command.MOV_DOWN);
            }
        }

        if (movementKeyPressed) {
            walkAnimation.resetStateTime(direction);
            walkAnimation.addStateTime(direction, Gdx.graphics.getDeltaTime());
        }
        updateMask();
    }

    private boolean isColliding(Quadtree<Tile> quadTree, Point offset) {
        if (quadTree.isColliding(mask, offset)) {
            return true;
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
    public void draw(SpriteBatch sprBatch) {
        sprBatch.draw(walkAnimation.getCurrentFrame(direction), xPosition, yPosition);
    }

    @Override
    public String getTypeIdenfitier() {
        return HERO;
    }
}
