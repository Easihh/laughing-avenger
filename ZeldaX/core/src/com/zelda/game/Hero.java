package com.zelda.game;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
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

    private Texture walkSheet;
    private TextureRegion[] walkFrames;
    private TextureRegion currentFrame;
    private Animation walkAnimation;
    private float stateTime;
    private final static int FRAME_COLS = 2;
    private final static int FRAME_ROWS = 1;

    public Hero(int x, int y) {
        updaterQueue = new LinkedList<PositionUpdater>();
        xPosition = x;
        yPosition = y;
        height = HEIGHT;
        width = WIDTH;
        mask = (new Rectangle(xPosition, yPosition, width, height));

        walkSheet = new Texture(Gdx.files.internal("Link_Movement.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, 32, 32);
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation(0.25f, walkFrames);
        stateTime = 0f;
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);
    }

    @Override
    public void update(Collection<ClientGameObject> collection) {
        boolean movementKeyPressed = false;
        Point offset;
        // discard server position update for now
        updaterQueue.clear();
        // TODO Some code here to check if Player is too far from server coord

        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            movementKeyPressed = true;
            offset = new Point(SPEED, 0);
            if (!isColliding(collection, offset)) {
                xPosition += SPEED;
                ServerWriter.sendMessage("0001R");
            }
        }
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            movementKeyPressed = true;
            offset = new Point(-SPEED, 0);
            if (!isColliding(collection, offset)) {
                xPosition -= SPEED;
                ServerWriter.sendMessage("0001L");
            }
        }
        if (Gdx.input.isKeyPressed(Keys.UP)) {
            movementKeyPressed = true;
            yPosition += SPEED;
            ServerWriter.sendMessage("0001U");
        }
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            movementKeyPressed = true;
            yPosition -= SPEED;
            ServerWriter.sendMessage("0001D");
        }

        if (movementKeyPressed) {
            stateTime += Gdx.graphics.getDeltaTime();
            currentFrame = walkAnimation.getKeyFrame(stateTime, true);
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
        sprBatch.draw(currentFrame, xPosition, yPosition);
    }

    @Override
    public String getTypeIdenfitier() {
        return Constants.ObjectType.HERO;
    }
}
