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
import com.zelda.server.entity.Player;
import com.zelda.server.entity.ServerGameObject;
import com.zelda.server.message.ServerMessage;
import com.zelda.server.message.ServerPositionMessage;

public class ServerMessageProcessor {

    private Queue<ServerMessage> msgQueue;
    private ConcurrentHashMap<String, ServerGameObject> gameEntityMap;
    private final Logger LOG = LoggerFactory.getLogger(ServerMessageProcessor.class);

    public ServerMessageProcessor(Queue<ServerMessage> messageQueue,
                    ConcurrentHashMap<String, ServerGameObject> entityMap) {
        msgQueue = messageQueue;
        gameEntityMap = entityMap;
    }

    public void process() {
        while (!msgQueue.isEmpty()) {
            ServerMessage current = msgQueue.poll();
            if (current.getType() == 1) {
                ServerPositionMessage actual = (ServerPositionMessage) current;
                Player player = (Player) gameEntityMap.get(actual.getIdentifier());
                int x = player.getxPosition();
                int y = player.getyPosition();
                for (GameObject obj : gameEntityMap.values()) {
                    if (obj != player && !(obj instanceof Player)) {
                        switch (actual.getDirection()) {
                        case LEFT:
                            if (player.intersect(player.getMask(), obj.getMask(), new Point(-2, 0))) {
                                LOG.debug("Leftward Collision Detected With:" + obj.getFullIdentifier());
                            } else
                                x -= 2;
                            break;
                        case RIGHT:
                            if (player.intersect(player.getMask(), obj.getMask(), new Point(2, 0))) {
                                LOG.debug("Rightward Collision Detected With:" + obj.getFullIdentifier());
                            } else
                                x += 2;
                            break;
                        case UP:
                            if (player.intersect(player.getMask(), obj.getMask(), new Point(0, 2))) {
                                LOG.debug("Upward Collision Detected With:" + obj.getFullIdentifier());
                            } else
                                y += 2;
                            break;
                        case DOWN:
                            if (player.intersect(player.getMask(), obj.getMask(), new Point(0, -2))) {
                                LOG.debug("Downward Collision Detected With:" + obj.getFullIdentifier());
                            } else
                                y -= 2;
                            break;
                        }
                        player.setPosition(x, y);
                        LOG.debug("Position Message Processed.");
                        break;
                    }
                }
            }
        }
    }
}
