package com.zelda.server.message.processor;

import java.awt.Point;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.zelda.common.Constants.Movement.LEFT;
import static com.zelda.common.Constants.Movement.DOWN;
import static com.zelda.common.Constants.Movement.RIGHT;
import static com.zelda.common.Constants.Movement.UP;

import com.zelda.common.Constants;
import com.zelda.common.GameObject;
import com.zelda.common.Quadtree;
import com.zelda.server.GameData;
import com.zelda.server.ZoneLoader;
import com.zelda.server.entity.Player;
import com.zelda.server.entity.ServerGameObject;
import com.zelda.server.message.ServerHeroAttackMessage;
import com.zelda.server.message.ServerMessage;
import com.zelda.server.message.ServerPositionMessage;

public class ServerMessageProcessor {

    private Queue<ServerMessage> msgQueue;
    private ConcurrentHashMap<String, ServerGameObject> gameEntityMap;
    private Quadtree<GameObject> gameStaticEntityQuadTree;
    private final Logger LOG = LoggerFactory.getLogger(ServerMessageProcessor.class);

    public ServerMessageProcessor() {
        GameData gData = GameData.getInstance();
        msgQueue = gData.getGameSimulationMessageQueue();
        gameEntityMap = gData.getGameEntityMap();
        gameStaticEntityQuadTree = ZoneLoader.getInstance().getZoneByName("Zone1").getStaticObjQtree();
    }

    public void process() {
        while (!msgQueue.isEmpty()) {
            ServerMessage current = msgQueue.poll();
            switch (current.getType()) {
            case Constants.ServerMessageType.MOVEMENT:
                processPositionMessage(current);
                break;
            case Constants.ServerMessageType.HERO_ATTACK:
                processHeroAttackMessage(current);
                break;
            }
        }
    }

    private void processHeroAttackMessage(ServerMessage current) {
        ServerHeroAttackMessage msg = (ServerHeroAttackMessage) current;
        Player player = (Player) gameEntityMap.get(msg.getHeroIdentifier());
        player.triggerPlayerAttack();
    }

    private void processPositionMessage(ServerMessage current) {
        ServerPositionMessage msg = (ServerPositionMessage) current;
        Player player = (Player) gameEntityMap.get(msg.getIdentifier());
        int x = player.getxPosition();
        int y = player.getyPosition();
        boolean collisionFound = false;
        String intentDirection = msg.getDirection();
        Point moveAhead = null;
        switch (intentDirection) {
        case LEFT:
            moveAhead = new Point(-2, 0);
            collisionFound = gameStaticEntityQuadTree.isColliding(player.getMask(), moveAhead);
            break;
        case RIGHT:
            moveAhead = new Point(2, 0);
            collisionFound = gameStaticEntityQuadTree.isColliding(player.getMask(), moveAhead);
            break;
        case UP:
            moveAhead = new Point(0, -2);
            collisionFound = gameStaticEntityQuadTree.isColliding(player.getMask(), moveAhead);
            break;
        case DOWN:
            moveAhead = new Point(0, 2);
            collisionFound = gameStaticEntityQuadTree.isColliding(player.getMask(), moveAhead);
            break;
        }
        if (collisionFound) {
            LOG.debug("" + intentDirection + "ward Collision Detected With:" + player.getFullIdentifier());
        }
        player.setDirection(intentDirection);
        if (!collisionFound && player.getObjState() != Constants.ObjectState.ATTACKING) {
            x += moveAhead.x;
            y += moveAhead.y;
            player.setPosition(x, y);
            //LOG.debug("Position Message Processed.");
        }
    }
}
