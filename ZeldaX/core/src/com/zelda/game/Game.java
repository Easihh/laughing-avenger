package com.zelda.game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.zelda.common.Constants;
import com.zelda.common.GameObject;
import com.zelda.common.Quadtree;
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
    private Quadtree<GameObject> staticEntityQTree;
    private Quadtree<GameObject> nextZoneStaticEntityQTree;
    private List<RenderTile> staticTileList;
    private List<RenderTile> nextZoneStaticTileList;
    private OrthographicCamera camera;
    private OrthographicCamera uiCamera;
    private ZoneLoader zLoader;
    private Zone currentZone;
    private Zone nextZone;
    private String heroIdentifier;
    private OtherPlayer otherPlayer;
    private Hero player;
    private static float SCREEN_WIDTH;
    private static float SCREEN_HEIGHT;
    private final static int ONE_MINUTE_MILLIS = 1000;
    private ExtendViewport  viewPort;
    private ExtendViewport  uiViewPort;
    private BitmapFont font;
    private Logger LOG = LoggerFactory.getLogger(Game.class);

    @SuppressWarnings({ "unchecked", "unused" })
    @Override
    public void create() {
        font = new BitmapFont(true);  
        SCREEN_WIDTH = Gdx.graphics.getWidth();
        SCREEN_HEIGHT = Gdx.graphics.getHeight();
        GameResources gameRes = GameResources.getInstance();//pre-load all texture once at start.
        loadMap();
        staticEntityQTree = currentZone.getStaticObjQtree();
        fromServerMessageQueue = new LinkedList<Message>();
        networkController = new NetworkController(fromServerMessageQueue);
        entityMap = new HashMap<String, ClientGameObject>();
        lastUpdate = System.currentTimeMillis();
        batch = new SpriteBatch();
        camera = new OrthographicCamera();
        uiCamera = new OrthographicCamera();
        camera.setToOrtho(true);
        uiCamera.setToOrtho(true);
        viewPort = new ExtendViewport(10 * SCREEN_WIDTH, 10 * SCREEN_HEIGHT, camera);
        uiViewPort = new ExtendViewport(SCREEN_WIDTH, SCREEN_HEIGHT, uiCamera);
        camera.position.set(SCREEN_WIDTH / 2, SCREEN_HEIGHT / 2, 0);
        batch.setProjectionMatrix(camera.combined);
        staticTileList = (List<RenderTile>) staticEntityQTree.QuadToList();
    }

    @SuppressWarnings("unchecked")
    private void loadMap() {
        zLoader = ZoneLoader.getInstance();
        currentZone = zLoader.getZoneByName("Zone1");
        nextZone = zLoader.getZoneByName("Zone2");
        nextZoneStaticEntityQTree = nextZone.getStaticObjQtree();
        nextZoneStaticTileList = (List<RenderTile>) nextZoneStaticEntityQTree.QuadToList();
    }
    
    @Override
    public void resize(int width, int height) {
        viewPort.update(width, height, true);
        uiViewPort.update(width, height, true);
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
                player = new Hero(camera, uiCamera);
                entityMap.put(heroIdentifier, player);
                LOG.debug("Hero Identifier was set to:" + heroIdentifier);
            }
            if (message instanceof PositionMessage) {
                PositionMessage pMessage = (PositionMessage) message;
                ClientGameObject objMap = entityMap.get(pMessage.getFullIdentifier());
                int direction = GameUtility.directiontToInt(pMessage.getDirection());
                if (objMap == null) {// object is a new entity
                    if (pMessage.getObjType().equals(Constants.ObjectType.HERO)) {
                        otherPlayer = new OtherPlayer(pMessage.getX(), pMessage.getY(),direction);
                        entityMap.put(pMessage.getFullIdentifier(), otherPlayer);
                    }
                } else {
                    objMap.loadPosition(new PositionUpdater(pMessage));
                }
            }
            if (message instanceof ObjectRemovalMessage) {
                ObjectRemovalMessage remObj = (ObjectRemovalMessage) message;
                entityMap.remove(remObj.getFullIdentifier());
                LOG.debug("Entity:" + remObj.getFullIdentifier() + " was removed from game.");
            }
        }
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
                     
        for (ClientGameObject obj : entityMap.values()) {
            obj.update(entityMap.values(),staticEntityQTree);
            obj.draw(batch);
        }
        for(RenderTile obj:staticTileList){
            obj.draw(batch);
        }
        
        //render next closest Zone
        for (RenderTile obj : nextZoneStaticTileList) {
            obj.draw(batch);
        }
        batch.end();
        batch.setProjectionMatrix(uiCamera.combined);
        batch.begin();
        font.draw(batch, "X:" + player.getxPosition() + " Y:" + player.getyPosition(), (player.getxPosition()/10) -16, (player.getyPosition()/10) -16);
        batch.end();
        

        camera.update();
        uiCamera.update();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
