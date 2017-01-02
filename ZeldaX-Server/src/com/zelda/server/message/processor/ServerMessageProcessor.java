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
import com.zelda.common.GameObject;
import com.zelda.server.GameData;
import com.zelda.server.entity.Player;
import com.zelda.server.entity.ServerGameObject;
import com.zelda.server.message.ServerMessage;
import com.zelda.server.message.ServerPositionMessage;

public class ServerMessageProcessor {

    private Queue<ServerMessage> msgQueue;
    private ConcurrentHashMap<String, ServerGameObject> gameEntityMap;
    private final Logger LOG = LoggerFactory.getLogger(ServerMessageProcessor.class);

    public ServerMessageProcessor() {
        GameData gData = GameData.getInstance();
        msgQueue = gData.getGameSimulationMessageQueue();
        gameEntityMap = gData.getGameEntityMap();
    }

    public void process() {
        while (!msgQueue.isEmpty()) {
            ServerMessage current = msgQueue.poll();
            if (current.getType() == 1) {
                ServerPositionMessage actual = (ServerPositionMessage) current;
                Player player = (Player) gameEntityMap.get(actual.getIdentifier());
                int x = player.getxPosition();
                int y = player.getyPosition();
                boolean collisionFound = false;
                String intentDirection = actual.getDirection();
                Point moveAhead = null;
                for (GameObject obj : gameEntityMap.values()) {
                    switch (intentDirection) {
                    case LEFT:
                        moveAhead = new Point(-2, 0);
                        collisionFound = willCollide(player, obj, moveAhead, LEFT);
                        break;
                    case RIGHT:
                        moveAhead = new Point(2, 0);
                        collisionFound = willCollide(player, obj, moveAhead, RIGHT);
                        break;
                    case UP:
                        moveAhead = new Point(0, -2);
                        collisionFound = willCollide(player, obj, moveAhead, UP);
                        break;
                    case DOWN:
                        moveAhead = new Point(0, 2);
                        collisionFound = willCollide(player, obj, moveAhead, DOWN);
                        break;
                    }
                    if (collisionFound) {
                        break;
                    }
                }
                if (!collisionFound) {
                    x += moveAhead.x;
                    y += moveAhead.y;
                    player.setPosition(x, y);
                    player.setDirection(intentDirection);
                    LOG.debug("Position Message Processed.");
                }
            }
        }
    }

    private boolean willCollide(Player player, GameObject obj, Point point, String direction) {
        if (player.intersect(player.getMask(), obj.getMask(), point) && !isPlayerCollison(player, obj)) {
            LOG.debug("" + direction + "ward Collision Detected With:" + obj.getFullIdentifier());
            return true;
        }
        return false;
    }

    /**Do not Allow collision with yourself and other hero. **/
    private boolean isPlayerCollison(Player player, GameObject obj) {
        return obj == player || (obj instanceof Player);
    }
}
