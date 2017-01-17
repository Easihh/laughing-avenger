package com.zelda.game;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import static com.zelda.common.Constants.ObjectType.HERO;
import static com.zelda.common.Constants.Command.MAIN_SWD_ATTACK;

import com.zelda.common.Quadtree;
import com.zelda.common.network.HeroAttackMessage;
import com.zelda.common.network.Message;
import com.zelda.common.network.MovementMessage;
import com.zelda.common.Constants;
import com.zelda.common.Constants.Command;
import com.zelda.common.Constants.Direction;
import com.zelda.common.Constants.Movement;
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
    private float currentMovementDelayTime;
    private final float MAX_MOVEMENT_DELAY_TO_SERVER = 0.10f;
    private List<Message> movementCommandList;
    private static Logger LOG = LoggerFactory.getLogger(Hero.class);

    public Hero(int x, int y) {
        updaterQueue = new LinkedList<PositionUpdater>();
        xPosition = x;
        yPosition = y;
        height = HEIGHT;
        width = WIDTH;
        mask = new Rectangle(xPosition, yPosition, width, height);
        setupAnimation();
        currentMovementDelayTime = 0.0f;
        movementCommandList = new LinkedList<Message>();
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
        currentMovementDelayTime += Gdx.graphics.getDeltaTime();
        boolean movementKeyPressed = false;
        // discard server position update for now
        updaterQueue.clear();
        // TODO Maybe Some code here to check if Player is too far from server coord
        
        if (hSword != null) {
            hSword.update(ActiveCollection, quadTree);
            if (hSword.getCurrentDuration() >= hSword.getMaxDuration()) {
                hSword = null;
            }
        }
        updateAnimation();
        movementKeyPressed = checkMovementInput();
        if (!isAttacking && movementKeyPressed && !Gdx.input.isKeyPressed(Keys.SPACE)) {
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
            ServerWriter.sendMessage(new HeroAttackMessage());
        }
        updateMask();
        checkBulkMovementCommand();
    }
    
    private void checkBulkMovementCommand() {
        if (currentMovementDelayTime >= MAX_MOVEMENT_DELAY_TO_SERVER) {
            if (!movementCommandList.isEmpty()) {
                LOG.info("Sending To Server:");
                ServerWriter.sendBulkMessage(movementCommandList);
                currentMovementDelayTime = 0.0f;
                movementCommandList.clear();
            }
        }
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
                movementCommandList.add(new MovementMessage(Movement.RIGHT));
            }
            else if (hasChangedDirection()) {
                movementCommandList.add(new MovementMessage(Movement.RIGHT));
            }
        }
        else if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            direction = Direction.LEFT;
            offset = new Point(-SPEED, 0);
            if (!isColliding(quadTree, offset) && !hasChangedDirection()) {
                xPosition -= SPEED;
                movementCommandList.add(new MovementMessage(Movement.LEFT));
            }
            else if (hasChangedDirection()) {
                movementCommandList.add(new MovementMessage(Movement.LEFT));
            }
        }
        else if (Gdx.input.isKeyPressed(Keys.UP)) {
            direction = Direction.UP;
            offset = new Point(0, -SPEED);
            if (!isColliding(quadTree, offset) && !hasChangedDirection()) {
                yPosition -= SPEED;
                movementCommandList.add(new MovementMessage(Movement.UP));
            }
            else if (hasChangedDirection()) {
                movementCommandList.add(new MovementMessage(Movement.UP));
            }
        }
        else if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            direction = Direction.DOWN;
            offset = new Point(0, SPEED);
            if (!isColliding(quadTree, offset) && !hasChangedDirection()) {
                yPosition += SPEED;
                movementCommandList.add(new MovementMessage(Movement.DOWN));
            }
            else if (hasChangedDirection()) {
                movementCommandList.add(new MovementMessage(Movement.DOWN));
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

    @Override
    public void loadPosition(PositionUpdater positionUpdater) {
        updaterQueue.add(positionUpdater);
    }

    @Override
    public void draw(SpriteBatch sprBatch) {
        if (hSword != null) {
            hSword.draw(sprBatch);
            sprBatch.draw(attackAnimation.getCurrentFrame(direction), xPosition, yPosition);
        }
        if (!isAttacking) {
            sprBatch.draw(walkAnimation.getCurrentFrame(direction), xPosition, yPosition);
            font.draw(sprBatch, "X:" + xPosition + " Y:" + yPosition, 50, 50);
        }
    }

    @Override
    public String getTypeIdenfitier() {
        return HERO;
    }
}
