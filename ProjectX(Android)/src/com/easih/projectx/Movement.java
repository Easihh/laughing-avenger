package com.easih.projectx;

import android.graphics.Bitmap;

public class Movement {
	private final int width=32,height=32;
	
	public Animation walk_down;
	public Animation walk_up;
	public Animation walk_left;
	public Animation walk_right;
	public Movement(Bitmap spriteSheet,int delay){
		walk_down=new Animation();
		walk_up=new Animation();
		walk_left=new Animation();
		walk_right=new Animation();
		buildAnimation(spriteSheet,delay);
	}
	private void buildAnimation(Bitmap spriteSheet, int delay) {
		int cols=spriteSheet.getWidth()/width;
		for(int i=0;i<(cols);i++){
			walk_up.AddScene(Bitmap.createBitmap(spriteSheet, width*i, 0, width, height), delay);
			walk_down.AddScene(Bitmap.createBitmap(spriteSheet, width*i, 32, width, height), delay);
			walk_left.AddScene(Bitmap.createBitmap(spriteSheet, width*i, 64, width, height), delay);
			walk_right.AddScene(Bitmap.createBitmap(spriteSheet, width*i, 96, width, height), delay);
		}
	}
}
