package com.zelda.server;

import java.nio.channels.SelectionKey;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.zelda.server.entity.ServerGameObject;
import com.zelda.server.entity.ServerTile;
import com.zelda.server.message.ServerMessage;

public class GameData {

    private volatile ConcurrentHashMap<String, ServerGameObject> gameEntityMap;
    private volatile Queue<ServerMessage> gameSimulationMessageQueue;
    private ConcurrentHashMap<SelectionKey, ClientConnection> activeConnection;

    public GameData() {
        gameEntityMap = new ConcurrentHashMap<String, ServerGameObject>();
        ServerTile aTile = new ServerTile(100, 100);
        gameEntityMap.put(aTile.getFullIdentifier(), aTile);
        gameSimulationMessageQueue = new ConcurrentLinkedQueue<ServerMessage>();
        activeConnection = new ConcurrentHashMap<SelectionKey, ClientConnection>();
    }

    public ConcurrentHashMap<String, ServerGameObject> getGameEntityMap() {
        return gameEntityMap;
    }

    public Queue<ServerMessage> getGameSimulationMessageQueue() {
        return gameSimulationMessageQueue;
    }

    public ConcurrentHashMap<SelectionKey, ClientConnection> getActiveConnection() {
        return activeConnection;
    }
}
