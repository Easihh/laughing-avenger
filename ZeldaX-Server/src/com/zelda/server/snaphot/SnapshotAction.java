package com.zelda.server.snaphot;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zelda.common.Constants;
import com.zelda.server.ClientConnection;
import com.zelda.server.entity.ServerGameObject;

public class SnapshotAction {

    private List<String> toBeRemoved = new ArrayList<String>();
    private ConcurrentHashMap<SelectionKey, ClientConnection> connections;
    private ConcurrentHashMap<String, ServerGameObject> gameEntityMap;
    private boolean forceUpdate = false;
    private final Logger LOG = LoggerFactory.getLogger(SnapshotAction.class);

    public SnapshotAction(ConcurrentHashMap<SelectionKey, ClientConnection> activeConnection,
                    ConcurrentHashMap<String, ServerGameObject> entityMap) {
        connections = activeConnection;
        gameEntityMap = entityMap;
    }

    /**
     * Forcing update removes the requirement that a given object must have
     * moved since the last update sent.
     */
    public synchronized void sendUpdateToPlayers(boolean forceUpdate) {
        this.forceUpdate = forceUpdate;
        SocketChannel client = null;
        byte[] snapshopt = buildGameSnaphotMessageToBytes();
        for (SelectionKey key : connections.keySet()) {
            /*
             * for (byte b : snapshopt) { System.out.format("0x%x ", b); }
             */
            if (key.interestOps() != SelectionKey.OP_ACCEPT && snapshopt != null) {
                key.interestOps(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
                client = (SocketChannel) key.channel();
                ByteBuffer buffer = ByteBuffer.wrap(snapshopt);
                try {
                    int byteWritten = client.write(buffer);
                    if (byteWritten == 0) {
                        LOG.warn("Warning:zero bytes written to the buffer.Buffer is likely full.");
                    }
                    if (!buffer.hasRemaining()) {
                        key.interestOps(SelectionKey.OP_READ);
                    }
                }
                catch (IOException e) {
                    LOG.warn("Error while writing snapshot to Player buffer:" + e.getMessage());// Player
                                                                                                // disconnected

                }
            }
        }
    }

    private byte[] buildGameSnaphotMessageToBytes() {
        toBeRemoved.clear();
        byte[] byteArr = null;
        for (String key : gameEntityMap.keySet()) {
            ServerGameObject tmp = gameEntityMap.get(key);
            if (objectHasNotChangedSinceLastUpdate(tmp) && !forceUpdate) {
                continue;
            }
            if (Constants.ObjectState.INACTIVE.equals(tmp.getObjState())) {
                toBeRemoved.add(key);
            }
            byte[] tempArr = tmp.convertToBytes();
            byteArr = ArrayUtils.addAll(byteArr, tempArr);
            tmp.updateLastSentPosition();
        }
        return byteArr;
    }

    private boolean objectHasNotChangedSinceLastUpdate(ServerGameObject tmp) {
        return tmp.getxPosition() == tmp.getPrevSentXPosition() && tmp.getyPosition() == tmp.getPrevSentYPosition();
    }

    public synchronized void removeInactiveObj() {
        for (String key : toBeRemoved) {
            LOG.debug("Removing Entity:" + key + " from the game.");
            gameEntityMap.remove(key);
        }
    }
}
