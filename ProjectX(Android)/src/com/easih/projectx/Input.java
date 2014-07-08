package com.easih.projectx;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.view.MotionEvent;

public class Input {
	public Bitmap leftArrow,rightArrow,upArrow,downArrow;
	public GameView gameView;
	private Character hero;
	Rect leftButton,downButton,rightButton,upButton;
	public final int inputWidth=64,inputHeight=64;
	public final int leftInputX=50,leftInputY=300,downInputX=114,downInputY=300;
	public final int rightInputX=178,rightInputY=300,upInputX=114,upInputY=236;
	public Input(GameView View){
		this.gameView=View;
		leftArrow=BitmapFactory.decodeResource(View.getResources(), R.drawable.arrow_left);
		rightArrow=BitmapFactory.decodeResource(View.getResources(), R.drawable.arrow_right);
		downArrow=BitmapFactory.decodeResource(View.getResources(), R.drawable.arrow_down);
		upArrow=BitmapFactory.decodeResource(View.getResources(), R.drawable.arrow_up);
		buildInputMask();
	}
	private void buildInputMask() {
		leftButton=new Rect(leftInputX,leftInputY,leftInputX+inputWidth,leftInputY+inputHeight);
		downButton=new Rect(downInputX,downInputY,downInputX+inputWidth,downInputY+inputHeight);
		rightButton=new Rect(rightInputX,rightInputY,rightInputX+inputWidth,rightInputY+inputHeight);
		upButton=new Rect(upInputX,upInputY,upInputX+inputWidth,upInputY+inputHeight);
		
	}
	public void DragRelease(MotionEvent event) {
		hero=Character.getInstance();
		Rect click=new Rect((int)event.getX(),(int)event.getY(),(int)event.getX()+1,(int)event.getY()+1);
		if(!Rect.intersects(leftButton, click))hero.isMovingLeft=false;
		if(!Rect.intersects(rightButton, click))hero.isMovingRight=false;
		if(!Rect.intersects(downButton, click))hero.isMovingDown=false;
		if(!Rect.intersects(upButton, click))hero.isMovingUp=false;	
	}
	public void getPressedButton(MotionEvent event) {
		hero=Character.getInstance();
		Rect click=new Rect((int)event.getX(),(int)event.getY(),(int)event.getX()+1,(int)event.getY()+1);
		if(Rect.intersects(leftButton, click))hero.isMovingLeft=true;
		if(Rect.intersects(rightButton, click))hero.isMovingRight=true;
		if(Rect.intersects(downButton, click))hero.isMovingDown=true;
		if(Rect.intersects(upButton, click))hero.isMovingUp=true;
	}
	public void ReleaseButton(MotionEvent event) {
		hero=Character.getInstance();
		Rect click=new Rect((int)event.getX(),(int)event.getY(),(int)event.getX()+1,(int)event.getY()+1);
		if(Rect.intersects(leftButton, click))hero.isMovingLeft=false;
		if(Rect.intersects(rightButton, click))hero.isMovingRight=false;
		if(Rect.intersects(downButton, click))hero.isMovingDown=false;
		if(Rect.intersects(upButton, click))hero.isMovingUp=false;
	}
}
