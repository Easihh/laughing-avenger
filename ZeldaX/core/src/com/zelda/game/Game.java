package com.zelda.game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zelda.common.Constants;
import com.zelda.common.network.HeroIdentiferMessage;
import com.zelda.common.network.Message;
import com.zelda.common.network.ObjectRemovalMessage;
import com.zelda.common.network.PositionMessage;
import com.zelda.network.NetworkController;

public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private long lastUpdate;
    private int frame = 0;

    @SuppressWarnings("unused")
    private NetworkController networkController;

    private static volatile Queue<Message> fromServerMessageQueue;
    private Map<String, ClientGameObject> entityMap;

    private String heroIdentifier;
    private OtherPlayer otherPlayer;
    private Hero player;

    private final static int ONE_MINUTE_MILLIS = 1000;
    private Logger LOG = LoggerFactory.getLogger(Game.class);

    @Override
    public void create() {
        fromServerMessageQueue = new LinkedList<Message>();
        networkController = new NetworkController(fromServerMessageQueue);
        entityMap = new HashMap<String, ClientGameObject>();
        loadGameResources();
        lastUpdate = System.currentTimeMillis();
        batch = new SpriteBatch();

    }

    private void loadGameResources() {
        /*
         * InputStream is=Game.class.getResourceAsStream("/Tree.png");
         * Gdx2DPixmap gdx2Dpix=new Gdx2DPixmap(is,GDX2D_FORMAT_RGB565); Pixmap
         * pixMap=new Pixmap(gdx2Dpix); img=new Texture(pixMap);
         */
    }

    @Override
    public void render() {
        frame++;
        if (System.currentTimeMillis() - lastUpdate > ONE_MINUTE_MILLIS) {
            Gdx.graphics.setTitle("ZeldaX:" + frame);
            lastUpdate = System.currentTimeMillis();
            frame = 0;
        }
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        while (!fromServerMessageQueue.isEmpty()) {
            Message message = fromServerMessageQueue.remove();
            if (message instanceof HeroIdentiferMessage) {
                heroIdentifier = ((HeroIdentiferMessage) message).fullIdentifier();
                player = new Hero(0, 0);
                entityMap.put(heroIdentifier, player);
                LOG.debug("Hero Identifier was set to:" + heroIdentifier);
            }
            if (message instanceof PositionMessage) {
                PositionMessage pMessage = (PositionMessage) message;
                ClientGameObject objMap = entityMap.get(pMessage.getFullIdentifier());
                int direction=GameUtility.directiontToInt(pMessage.getDirection());
                if (objMap == null) {// object is a new entity
                    if (pMessage.getObjType().equals(Constants.ObjectType.HERO)) {
                        otherPlayer = new OtherPlayer(pMessage.getX(), pMessage.getY(),direction);
                        entityMap.put(pMessage.getFullIdentifier(), otherPlayer);
                    }
                    if (pMessage.getObjType().equals(Constants.ObjectType.TILE)) {
                        Tile tile = new Tile(pMessage.getX(), pMessage.getY());
                        entityMap.put(pMessage.getFullIdentifier(), tile);
                    }
                } else {
                    objMap.loadPosition(new PositionUpdater(pMessage.getX(), pMessage.getY(),direction));
                }
            }
            if (message instanceof ObjectRemovalMessage) {
                ObjectRemovalMessage remObj = (ObjectRemovalMessage) message;
                entityMap.remove(remObj.getFullIdentifier());
                LOG.debug("Entity:" + remObj.getFullIdentifier() + " was removed from game.");
            }
        }
        batch.begin();
        for (ClientGameObject obj : entityMap.values()) {
            obj.update(entityMap.values());
            obj.draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
