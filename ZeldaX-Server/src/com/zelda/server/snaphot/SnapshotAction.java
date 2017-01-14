package com.zelda.server.snaphot;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.zelda.common.Constants.ObjectState.INACTIVE;
import com.zelda.server.ClientConnection;
import com.zelda.server.GameData;
import com.zelda.server.entity.ServerGameObject;

public class SnapshotAction {

    private List<String> toBeRemoved = new ArrayList<String>();
    private ConcurrentHashMap<SelectionKey, ClientConnection> connections;
    private ConcurrentHashMap<String, ServerGameObject> gameEntityMap;
    private final Logger LOG = LoggerFactory.getLogger(SnapshotAction.class);
    private List<SelectionKey> keyList;

    public SnapshotAction() {
        GameData gData=GameData.getInstance();
        connections = gData.getActiveConnection();
        gameEntityMap = gData.getGameEntityMap();
        keyList = new LinkedList<SelectionKey>(connections.keySet());
    }

    public synchronized void sendUpdateToAllPlayers() {
        byte[] snapshop = buildGameSnaphotMessageToBytes();
        keyList.clear();
        keyList.addAll(connections.keySet());
        sendMessageToPlayer(keyList, snapshop);
        removeInactiveObj();      
    }

    private synchronized void sendMessageToPlayer(List<SelectionKey> keyList, byte[] snapshopt) {
        SocketChannel client = null;
        for (SelectionKey key : keyList) {
            if (key.interestOps() != SelectionKey.OP_ACCEPT && snapshopt != null) {
                key.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                client = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.wrap(snapshopt);
                try {
                    int byteWritten = client.write(buffer);
                    if (byteWritten == 0) {
                        LOG.warn("Warning:zero bytes written to the buffer.Socket Buffer is likely full.");
                        //Should probably disconnect player here.
                    }
                    if (!buffer.hasRemaining()) {
                        key.interestOps(SelectionKey.OP_READ);
                    }
                    if (buffer.hasRemaining()) {
                        LOG.error("Buffer was only partially written to client for player update.");
                        buffer.compact();
                        //should store rest of message here for next send
                    }
                }
                catch (IOException e) {
                    /**Player Disconnected **/
                    LOG.warn("Error while writing snapshot to Player buffer:" + e.getMessage());                                                                                               
                }
            }
        }
    }

    /**
     * Force Send current GameSnapshot to current player. Mostly used on event
     * such as new player connection/zone change.
     **/
    public synchronized void sendSnapshotToCurrentPlayer(SelectionKey playerKey) {
        List<SelectionKey> currentPlayerKey = new LinkedList<SelectionKey>();
        currentPlayerKey.add(playerKey);
        byte[] snapshop = buildForcedGameSnapshotMessageToBytes();
        sendMessageToPlayer(currentPlayerKey, snapshop);
    }

    private byte[] buildForcedGameSnapshotMessageToBytes() {
        byte[] byteArr = null;
        for (String key : gameEntityMap.keySet()) {
            ServerGameObject tmp = gameEntityMap.get(key);
            {
                byte[] tempArr = tmp.convertToBytes();
                byteArr = ArrayUtils.addAll(byteArr, tempArr);
            }
        }
        return byteArr;
    }

    private byte[] buildGameSnaphotMessageToBytes() {
        toBeRemoved.clear();
        byte[] byteArr = null;
        for (String key : gameEntityMap.keySet()) {
            ServerGameObject tmp = gameEntityMap.get(key);
            int objState = tmp.getObjState();
            if (INACTIVE == objState) {
                toBeRemoved.add(key);
            }
            if (!objectHasNotChangedSinceLastUpdate(tmp) || INACTIVE == objState) {
                byte[] tempArr = tmp.convertToBytes();
                byteArr = ArrayUtils.addAll(byteArr, tempArr);
                tmp.updateLastSent();
            }
        }
        return byteArr;
    }

    private boolean objectHasNotChangedSinceLastUpdate(ServerGameObject tmp) {
        return objectPositionHasNotChanged(tmp) && objectDirectionHasNotChanged(tmp) && objectStateHasNotChanged(tmp);
    }

    private boolean objectStateHasNotChanged(ServerGameObject tmp) {
        return tmp.getPrevSentState()==tmp.getObjState();
    }

    private boolean objectDirectionHasNotChanged(ServerGameObject tmp) {
        return tmp.getPrevSentDirection().equals(tmp.getDirection());
    }

    private boolean objectPositionHasNotChanged(ServerGameObject tmp) {
        return tmp.getxPosition() == tmp.getPrevSentXPosition() && tmp.getyPosition() == tmp.getPrevSentYPosition();
    }

    private void removeInactiveObj() {
        for (String key : toBeRemoved) {
            LOG.debug("Removing Entity:" + key + " from the game.");
            gameEntityMap.remove(key);
        }
    }
}
