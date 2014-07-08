package com.easih.projectx;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class GameAssets {
	private static GameAssets asset;
	Context context;
	public Bitmap heroSheet;
	public GameAssets(Context context){
		this.context=context;
		loadImages();
		asset=this;
	}
	static GameAssets getInstance(){
		return asset;
	}
	private void loadImages() {
		heroSheet=BitmapFactory.decodeResource(context.getResources(), R.drawable.lolo_movement);
	}
	
}
