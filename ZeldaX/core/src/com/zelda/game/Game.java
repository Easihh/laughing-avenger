package com.zelda.game;

import java.awt.Rectangle;
import java.awt.Shape;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.zelda.common.Constants;
import com.zelda.common.GameObject;
import com.zelda.common.Quadtree;
import com.zelda.common.network.HeroIdentiferMessage;
import com.zelda.common.network.Message;
import com.zelda.common.network.ObjectRemovalMessage;
import com.zelda.common.network.PositionMessage;
import com.zelda.network.NetworkController;

import javafx.geometry.Rectangle2D;


public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private long lastUpdate;
    private int frame = 0;

    @SuppressWarnings("unused")
    private NetworkController networkController;

    private static volatile Queue<Message> fromServerMessageQueue;
    private Map<String, ClientGameObject> entityMap;
    private Quadtree<ClientGameObject> entityQTree;
    private List<ClientGameObject> staticTileList;
    private OrthographicCamera camera;

    private String heroIdentifier;
    private OtherPlayer otherPlayer;
    private Hero player;

    private final static int ONE_MINUTE_MILLIS = 1000;
    private Logger LOG = LoggerFactory.getLogger(Game.class);

    @SuppressWarnings("unchecked")
    @Override
    public void create() {
        entityQTree=new Quadtree<ClientGameObject>(0, 0, 512, 512, 1);
        loadGameResources();
        fromServerMessageQueue = new LinkedList<Message>();
        camera=new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.setToOrtho(true);
        networkController = new NetworkController(fromServerMessageQueue);
        entityMap = new HashMap<String, ClientGameObject>();
        lastUpdate = System.currentTimeMillis();
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);
        staticTileList=(List<ClientGameObject>) entityQTree.QuadToList();
        System.out.println("Qtree Size:"+staticTileList.size());
    }

    private void loadGameResources() {
        InputStream is=Game.class.getResourceAsStream("/Zone1.csv");
        Scanner scan = new Scanner(is);
        int zoneStartX=0;
        int zoneStartY=0;
        int insertCount=0;
        while (scan.hasNextLine()) {
            String lineValue = scan.nextLine();
            String[] arr = lineValue.split(",", 0);
            for (int i = 0; i < arr.length; i++) {
                int identifier = Integer.parseInt(arr[i]);
                switch (identifier) {
                case -1:
                    break;
                case 0:
                    Tile tile = new Tile(zoneStartX, zoneStartY);
                    System.out.println("X:"+zoneStartX+" Y:"+zoneStartY);
                    entityQTree.insert(tile);
                    insertCount++;
                    break;
                default:
                    LOG.error("Invalid identifier:" + identifier);
                }
            zoneStartX += Constants.Size.MAX_TILE_WIDTH;
            }
            zoneStartY += Constants.Size.MAX_TILE_HEIGHT;
            zoneStartX = 0;
        }
        System.out.println("Inserted:"+insertCount);
        scan.close();
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
                player = new Hero(128, 160);
                entityMap.put(heroIdentifier, player);
                LOG.debug("Hero Identifier was set to:" + heroIdentifier);
                //System.out.println(entityQTree.insert(player));
                //System.out.println(entityQTree.insert(player));
                //Hero testPlayer=new Hero(512,0);
                //entityQTree.insert(testPlayer);
                //entityQTree.insert(testPlayer);
                
                
                //java.awt.geom.Rectangle2D testMask=new Rectangle(524,0,32,32);
                //System.out.println("IsColliding:"+entityQTree.isColliding(testMask));
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
        for(ClientGameObject obj:staticTileList){
            obj.draw(batch);
        }
        for (ClientGameObject obj : entityMap.values()) {
            obj.update(entityMap.values(),entityQTree);
            obj.draw(batch);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
