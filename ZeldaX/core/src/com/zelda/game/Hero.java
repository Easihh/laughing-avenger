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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.zelda.common.Constants;
import com.zelda.network.ServerWriter;

public class Hero extends ClientGameObject {

    private static final int WIDTH = 32;
    private static final int HEIGHT = 32;
    private static final int SPEED = 2;
    private Queue<PositionUpdater> updaterQueue;

    private MovementAnimation walkAnimation;
    private int direction;

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
    public void update(Collection<ClientGameObject> collection) {
        boolean movementKeyPressed = false;
        Point offset;
        // discard server position update for now
        updaterQueue.clear();
        // TODO Some code here to check if Player is too far from server coord

        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            direction = Constants.Direction.RIGHT;
            movementKeyPressed = true;
            offset = new Point(SPEED, 0);
            if (!isColliding(collection, offset)) {
                xPosition += SPEED;
                ServerWriter.sendMessage("0001R");
            }
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            direction = Constants.Direction.LEFT;
            movementKeyPressed = true;
            offset = new Point(-SPEED, 0);
            if (!isColliding(collection, offset)) {
                xPosition -= SPEED;
                ServerWriter.sendMessage("0001L");
            }
        }
        if (Gdx.input.isKeyPressed(Keys.UP)) {
            direction = Constants.Direction.UP;
            movementKeyPressed = true;
            yPosition += SPEED;
            ServerWriter.sendMessage("0001U");
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            direction = Constants.Direction.DOWN;
            movementKeyPressed = true;
            yPosition -= SPEED;
            ServerWriter.sendMessage("0001D");
        }

        if (movementKeyPressed) {
            walkAnimation.addStateTime(direction, Gdx.graphics.getDeltaTime());
            // walkAnimation.getCurcurrentFrame =
            // walkAnimation.getKeyFrame(stateTime, true);
        }
        updateMask();
    }

    private boolean isColliding(Collection<ClientGameObject> collection, Point offset) {
        for (ClientGameObject obj : collection) {
            if (intersect(mask, obj.getMask(), offset) && !mask.equals(obj.getMask())
                            && !(obj instanceof OtherPlayer)) {
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
    public void draw(SpriteBatch sprBatch) {
        sprBatch.draw(walkAnimation.getCurrentFrame(direction), xPosition, yPosition);
    }

    @Override
    public String getTypeIdenfitier() {
        return Constants.ObjectType.HERO;
    }
}
