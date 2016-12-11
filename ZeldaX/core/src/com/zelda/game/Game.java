package com.zelda.game;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import static com.badlogic.gdx.graphics.g2d.Gdx2DPixmap.GDX2D_FORMAT_RGB565;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.zelda.common.Constants;
import com.zelda.common.network.HeroIdentiferMessage;
import com.zelda.common.network.Message;
import com.zelda.common.network.ObjectRemovalMessage;
import com.zelda.common.network.PositionMessage;
import com.zelda.network.NetworkController;

public class Game extends ApplicationAdapter {
    private SpriteBatch batch;
    private Texture img;
    private long lastUpdate;
    private int frame = 0;
    private ShapeRenderer shapeRenderer;
    private NetworkController networkController;
    public static volatile Queue<Message> fromServerMessageQueue;
    //private Map<String,GameObject> nonStaticEntityMap;
    private Map<String,ClientGameObject> entityMap;
    private String heroIdentifier;
    private OtherPlayer otherPlayer;
    private Hero player;
    private Logger LOG=LoggerFactory.getLogger(Game.class);
    private Texture walkSheet;
    private TextureRegion[] walkFrames;
    private TextureRegion currentFrame;
    private Animation walkAnimation;
    float stateTime; 
    private final static int FRAME_COLS=2;
    private final static int FRAME_ROWS=1;
    
	@Override
	public void create () {
        shapeRenderer = new ShapeRenderer();
        fromServerMessageQueue =new LinkedList<Message>();
        networkController = new NetworkController();
        entityMap=new HashMap<String, ClientGameObject>();
        //nonStaticEntityMap=new HashMap<String, GameObject>();
        loadGameResources();
        testAnimation();
	    lastUpdate=System.currentTimeMillis();
		//batch = new SpriteBatch();
		
	}
	
	private void testAnimation(){
	    walkSheet = new Texture(Gdx.files.internal("Link_Movement.png")); // #9
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, 32, 32);              // #10
        walkFrames = new TextureRegion[FRAME_COLS * FRAME_ROWS];
        int index = 0;
        for (int i = 0; i < FRAME_ROWS; i++) {
            for (int j = 0; j < FRAME_COLS; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation(0.5f, walkFrames); 
        stateTime = 0f;
        batch=new SpriteBatch();
	}

    private void loadGameResources() {
        try {
            /** Prod Setup*/
            /*String path = Game.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            int lastIndex=path.lastIndexOf('/');
            path=path.substring(0, lastIndex)+"/resource.jar";
            System.out.println("Path:"+path);*/
            
            String path="C:/Users/Enrico/Desktop/ZeldaGame/resource.jar";        
            ZipFile zipFile=new ZipFile(path);
            ZipEntry ze=zipFile.getEntry("badlogic.jpg");
            InputStream is=zipFile.getInputStream(ze);
            Gdx2DPixmap gdx2Dpix=new Gdx2DPixmap(is,GDX2D_FORMAT_RGB565);
            Pixmap pixMap=new Pixmap(gdx2Dpix);
            img=new Texture(pixMap);
            is.close();
            zipFile.close();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        /*catch (URISyntaxException e) {
            System.out.println(e.getMessage());
        }*/
    }

    @Override
	public void render () {
	    frame++;
	    if(System.currentTimeMillis()-lastUpdate>1000){
	        Gdx.graphics.setTitle("ZeldaX:"+frame);
	        lastUpdate=System.currentTimeMillis();
	        frame=0;
	    }
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stateTime += Gdx.graphics.getDeltaTime();           // #15
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);  // #16
        batch.begin();
        batch.draw(currentFrame, 250, 250);             // #17
        batch.end();
		/*batch.begin();
		batch.draw(img, 20, 20);
		batch.end();*/
		while(!fromServerMessageQueue.isEmpty()){
		    Message message=fromServerMessageQueue.remove();
		    if(message instanceof HeroIdentiferMessage){
		        heroIdentifier=((HeroIdentiferMessage)message).fullIdentifier();
		        player=new Hero(0, 0);
		        entityMap.put(heroIdentifier,player);
		        System.out.println("Hero Identifier was set to:"+heroIdentifier);
		    }
		    if(message instanceof PositionMessage){
		        PositionMessage pMessage=(PositionMessage)message;
		        ClientGameObject objMap=entityMap.get(pMessage.getFullIdentifier());
		        if(objMap==null){//object is new entity
		            if(pMessage.getObjType().equals(Constants.ObjectType.HERO)){
		                otherPlayer=new OtherPlayer(pMessage.getX(), pMessage.getY());
		                entityMap.put(pMessage.getFullIdentifier(),otherPlayer);
		            }
		            if(pMessage.getObjType().equals(Constants.ObjectType.TILE)){
		                Tile tile=new Tile(pMessage.getX(),pMessage.getY());
		                entityMap.put(pMessage.getFullIdentifier(), tile);
		            }
		        }
		        else{		           
		            objMap.loadPosition(new PositionUpdater(pMessage.getX(),pMessage.getY()));
		        }
		    }
		    if(message instanceof ObjectRemovalMessage){
		        ObjectRemovalMessage remObj=(ObjectRemovalMessage)message;
		        entityMap.remove(remObj.getFullIdentifier());
		        System.out.println("Entity:"+remObj.getFullIdentifier()+" was removed from game.");
		    }
		}
	    shapeRenderer.begin(ShapeType.Line);
		for(ClientGameObject obj:entityMap.values()){
		        obj.update(entityMap.values());
		        obj.draw(shapeRenderer);
		}
		shapeRenderer.end();
	}
	
	@Override
	public void dispose () {
		//batch.dispose();
		img.dispose();
	}
}
