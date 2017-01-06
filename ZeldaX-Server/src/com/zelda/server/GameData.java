package com.zelda.server;

import java.nio.channels.SelectionKey;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.zelda.server.entity.ServerGameObject;
import com.zelda.server.message.ServerMessage;

public class GameData {

    private static volatile ConcurrentHashMap<String, ServerGameObject> gameEntityMap;
    private static volatile Queue<ServerMessage> gameSimulationMessageQueue;
    private static volatile ConcurrentHashMap<SelectionKey, ClientConnection> activeConnection;
    private static GameData gData = null; 

    private GameData() {
        gameEntityMap = new ConcurrentHashMap<String, ServerGameObject>();
        ZoneLoader.getInstance();//Load All Map on Start up.
        gameSimulationMessageQueue = new ConcurrentLinkedQueue<ServerMessage>();
        activeConnection = new ConcurrentHashMap<SelectionKey, ClientConnection>();
    }
    
    public static GameData getInstance() {
        if (gData == null) {
            gData = new GameData();
        }
        return gData;
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
