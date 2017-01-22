package com.zelda.game;

import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.zelda.common.Constants.Size;

public class SimpleAnimation {

    private Animation[] animation;// index are Up->Down->Left->Right
    private final int WIDTH;
    private final int HEIGHT;
    private int cols;
    private float[] stateTime;

    public SimpleAnimation(List<FileHandle> file, int width, int height, float frameDuration) {
        animation = new Animation[file.size()];// 4 directions
        WIDTH = width;
        HEIGHT = height;
        stateTime = new float[file.size()];
        int fileIndex = 0;
        while (fileIndex < file.size()) {
            FileHandle fileHandle = file.get(fileIndex);
            loadAnimation(fileHandle, fileIndex,frameDuration);
            fileIndex++;
        }
    }

    private void loadAnimation(FileHandle file, int fileIndex, float frameDuration) {
        int ROWS = 1;// Animation always on 1 rows for movement.
        Texture walkSheet = new Texture(file);
        cols = walkSheet.getWidth() / WIDTH;
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, WIDTH, HEIGHT);
        TextureRegion[] walkFrames = new TextureRegion[cols * ROWS];
        int index = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < cols; j++) {
                TextureRegion tr=tmp[i][j];
                tr.flip(false, true);
                walkFrames[index++] = tr;
            }
        }
        animation[fileIndex] = new Animation(frameDuration, walkFrames);
        stateTime[fileIndex] = 0f;
    }
    
    public float getAnimationDuraction(int index) {
        return animation[index].getAnimationDuration();
    }

    public void addStateTime(int index, float amount) {
        stateTime[index] += amount;
    }
    
    public float getStateTime(int index) {
        return stateTime[index];
    }

    public Sprite getCurrentFrame(int direction) {
        TextureRegion text = animation[direction].getKeyFrame(stateTime[direction], true);
        Sprite spr = new Sprite(text);
        spr.setSize(Size.MAX_TILE_WIDTH * Size.WORLD_SCALE_X, Size.MAX_TILE_HEIGHT * Size.WORLD_SCALE_Y);
        return spr;
    }
    
    /**Reset StateTime of Animation for every index except index param**/
    public void resetStateTime(int index) {
        for (int i = 0; i < stateTime.length; i++) {
            if (i != index) {
                stateTime[i] = 0f;
            }
        }
    }

    public void resetStateTime() {
        for (int i = 0; i < stateTime.length; i++) {
            stateTime[i] = 0f;
        }
    }
}
