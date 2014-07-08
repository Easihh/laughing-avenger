package com.easih.projectx;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Character {
	private GameView gameView;
	public float x,y;
	public Bitmap myImage;
	public Boolean isMovingRight=false,isMovingLeft=false,isMovingUp=false,isMovingDown=false;
	private static Character myHero;
	public Character(GameView View){
		gameView=View;
		x=512;
		y=176;
		getImage();
		myHero=this;
	}
	static Character getInstance(){
		return myHero;
	}
	private void getImage() {
		myImage=BitmapFactory.decodeResource(gameView.getResources(), R.drawable.character);
	}
}
