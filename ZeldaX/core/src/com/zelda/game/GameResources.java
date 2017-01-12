package com.zelda.game;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class GameResources {
    
    private Map<String, Texture> textureMap = new HashMap<String, Texture>();

    private static GameResources gameResources = null;
    
    private GameResources(){
        loadAllTexture();
    }
    
    private void loadAllTexture() {
        storeTexture("WoodSword_Right");
        storeTexture("WoodSword_Left");
        storeTexture("WoodSword_Down");
        storeTexture("WoodSword_Up");
        storeTexture("Tree");
    }

    private void storeTexture(String fileName) {
        Texture texture = new Texture(Gdx.files.internal("" + fileName + ".png"));
        textureMap.put(fileName, texture);
    }

    public static GameResources getInstance() {
        if (gameResources == null) {
            return new GameResources();
        }
        return gameResources;
    }
    
    public Map<String, Texture> getTextureMap() {
        return textureMap;
    }
}
