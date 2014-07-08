package com.easih.projectx;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Character {
	public float x,y;
	public Bitmap myImage;
	public Boolean isMovingRight=false,isMovingLeft=false,isMovingUp=false,isMovingDown=false;
	private static Character myHero;
	private int direction;
	private final int width=32,height=32,step=16,move=2;
	private int target;
	private Movement movement;
	public Character(){
		x=512;
		y=176;
		direction=0;
		movement=new Movement(GameAssets.getInstance().heroSheet,100);
		myHero=this;
	}
	static Character getInstance(){
		return myHero;
	}
	public void render(Canvas myCanvas) {
		myCanvas.drawBitmap(getImage(),x,y,null);		
	}
	public void update(){
		checkIfStanding();
		if(target==0)tryMove();
		if(target>0)Move();
	}
	private void checkIfStanding() {
		if(!isMovingUp && direction==0)movement.walk_up.reset();
		if(!isMovingRight && direction==1)movement.walk_right.reset();
		if(!isMovingDown && direction==2)movement.walk_down.reset();
		if(!isMovingLeft && direction==3)movement.walk_left.reset();
	}
	private void Move() {
		switch(direction){
		case 0:	y-=move;
				break;
		case 1:	x+=move;
				break;
		case 2:	y+=move;
				break;
		case 3:	x-=move;
				break;
		}
		target-=move;
	}
	private void tryMove() {
		if(isMovingUp){
			direction=0;
			movement.walk_up.setImage();
			target=step;
		}
		else if(isMovingRight){
			direction=1;
			movement.walk_right.setImage();
			target=step;
		}
		else if(isMovingDown){
			direction=2;
			movement.walk_down.setImage();
			target=step;
		}
		else if(isMovingLeft){
			direction=3;
			movement.walk_left.setImage();
			target=step;
		}	
	}
	private Bitmap getImage() {
		switch(direction){
		case 0:	return movement.walk_up.getImage();
		case 1:	return movement.walk_right.getImage();
		case 2:	return movement.walk_down.getImage();
		case 3:	return movement.walk_left.getImage();
		}
		return null;
	}
}
