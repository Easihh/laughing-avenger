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
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
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

    private SimpleAnimation walkAnimation;
    private SimpleAnimation attackAnimation;
    private boolean isAttacking = false;
    private int direction;
    private int prevDirection;
    private BitmapFont font = new BitmapFont(true);
    private HeroSword hSword;

    public Hero(int x, int y) {
        updaterQueue = new LinkedList<PositionUpdater>();
        xPosition = x;
        yPosition = y;
        height = HEIGHT;
        width = WIDTH;
        mask = new Rectangle(xPosition, yPosition, width, height);
        setupAnimation();
    }

    private void setupAnimation() {
        List<FileHandle> mfiles = new LinkedList<FileHandle>();
        mfiles.add(Gdx.files.internal("Link_Movement_Up.png"));
        mfiles.add(Gdx.files.internal("Link_Movement_Down.png"));
        mfiles.add(Gdx.files.internal("Link_Movement_Left.png"));
        mfiles.add(Gdx.files.internal("Link_Movement_Right.png"));
        walkAnimation = new SimpleAnimation(mfiles, WIDTH, HEIGHT, 0.25f);
        
        List<FileHandle> afiles=new LinkedList<FileHandle>();
        afiles.add(Gdx.files.internal("Link_AttackUp.png"));
        afiles.add(Gdx.files.internal("Link_AttackDown.png"));
        afiles.add(Gdx.files.internal("Link_AttackLeft.png"));
        afiles.add(Gdx.files.internal("Link_AttackRight.png"));
        attackAnimation = new SimpleAnimation(afiles, WIDTH, HEIGHT, 0.50f);
    }

    @Override
    public void update(Collection<ClientGameObject> ActiveCollection, Quadtree<Tile> quadTree) {
        boolean movementKeyPressed = false;
        // discard server position update for now
        updaterQueue.clear();
        // TODO Some code here to check if Player is too far from server coord
        if (hSword != null) {
            hSword.update(ActiveCollection, quadTree);
            if (hSword.getCurrentDuration() >= hSword.getMaxDuration()) {
                hSword = null;
            }
        }
        updateAnimation();
        movementKeyPressed = checkMovementInput();
        if (!isAttacking && movementKeyPressed) {
            checkMovement(quadTree);
            if (hasChangedDirection()) {
                walkAnimation.resetStateTime(direction);
            }
            walkAnimation.addStateTime(direction, Gdx.graphics.getDeltaTime());
        }

        if (Gdx.input.isKeyPressed(Keys.SPACE) && !isAttacking) {
            isAttacking = true;
            walkAnimation.resetStateTime();
            hSword = new HeroSword(xPosition, yPosition, direction);
        }
        updateMask();
    }
    
    private boolean checkMovementInput() {
        return Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.LEFT)
                        || Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.DOWN);
    }

    private void checkMovement(Quadtree<Tile> quadTree) {
        Point offset;
        prevDirection = direction;
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            direction = Direction.RIGHT;
            offset = new Point(SPEED, 0);
            if (!isColliding(quadTree, offset) && !hasChangedDirection()) {
                xPosition += SPEED;       
                ServerWriter.sendMessage(Command.MOV_RIGHT);
            }
            else if (hasChangedDirection()) {
                ServerWriter.sendMessage(Command.MOV_RIGHT);
            }
        }
        else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            direction = Direction.LEFT;
            offset = new Point(-SPEED, 0);
            if (!isColliding(quadTree, offset) && !hasChangedDirection()) {
                xPosition -= SPEED;
                ServerWriter.sendMessage(Command.MOV_LEFT);
            }
            else if (hasChangedDirection()) {
                ServerWriter.sendMessage(Command.MOV_LEFT);
            }
        }
        else if (Gdx.input.isKeyPressed(Keys.UP)) {
            direction = Direction.UP;
            offset = new Point(0, -SPEED);
            if (!isColliding(quadTree, offset) && !hasChangedDirection()) {
                yPosition -= SPEED;
                ServerWriter.sendMessage(Command.MOV_UP);
            }
            else if (hasChangedDirection()) {
                ServerWriter.sendMessage(Command.MOV_UP);
            }
        }
        else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            direction = Direction.DOWN;
            offset = new Point(0, SPEED);
            if (!isColliding(quadTree, offset) && !hasChangedDirection()) {
                yPosition += SPEED;
                ServerWriter.sendMessage(Command.MOV_DOWN);
            }
            else if (hasChangedDirection()) {
                ServerWriter.sendMessage(Command.MOV_DOWN);
            }
        }
    }

    private void updateAnimation() {
        if (isAttacking) {
            attackAnimation.addStateTime(direction, Gdx.graphics.getDeltaTime());
            if (attackAnimation.getStateTime(direction) >= attackAnimation.getAnimationDuraction(direction)) {
                attackAnimation.resetStateTime();
                isAttacking = false;
            }
        }
    }

    private boolean hasChangedDirection() {
        return prevDirection != direction;
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
        if (hSword != null) {
            hSword.draw(sprBatch);
        }
        if (!isAttacking) {
            sprBatch.draw(walkAnimation.getCurrentFrame(direction), xPosition, yPosition);
            font.draw(sprBatch, "X:" + xPosition + " Y:" + yPosition, 50, 50);
        } else {
            sprBatch.draw(attackAnimation.getCurrentFrame(direction), xPosition, yPosition);
        }
    }

    @Override
    public String getTypeIdenfitier() {
        return HERO;
    }
}
