package com.zelda.game;

import java.util.List;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class MovementAnimation {

    private Animation[] walkAnimation;// index are Up->Down->Left->Right
    private final int WIDTH;
    private final int HEIGHT;
    private int cols;
    private float[] stateTime;

    public MovementAnimation(List<FileHandle> file, int width, int height) {
        walkAnimation = new Animation[4];// 4 directions
        WIDTH = width;
        HEIGHT = height;
        stateTime = new float[file.size()];
        int fileIndex = 0;
        while (fileIndex < file.size()) {
            FileHandle fileHandle = file.get(fileIndex);
            loadAnimation(fileHandle, fileIndex);
            fileIndex++;
        }
    }

    private void loadAnimation(FileHandle file, int fileIndex) {
        int ROWS = 1;// Animation always on 1 rows for movement.
        Texture walkSheet = new Texture(file);
        cols = walkSheet.getWidth() / WIDTH;
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, WIDTH, HEIGHT);
        TextureRegion[] walkFrames = new TextureRegion[cols * ROWS];
        int index = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < cols; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation[fileIndex] = new Animation(0.25f, walkFrames);
        stateTime[fileIndex] = 0f;
    }

    public void addStateTime(int index, float amount) {
        stateTime[index] += amount;
    }

    public TextureRegion getCurrentFrame(int direction) {
        return walkAnimation[direction].getKeyFrame(stateTime[direction], true);
    }
    
    /**Reset StateTime of Animation for every index except index param**/
    public void resetStateTime(int index) {
        for (int i = 0; i < stateTime.length; i++) {
            if (i != index) {
                stateTime[i] = 0f;
            }
        }
    }
}
