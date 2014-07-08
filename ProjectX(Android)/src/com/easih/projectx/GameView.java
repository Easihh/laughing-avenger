package com.easih.projectx;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

@SuppressLint("UseValueOf")
public class GameView extends SurfaceView implements Runnable,View.OnTouchListener {
	
	Canvas myCanvas;
	Thread t=null;
	GameLogic logic;
	Input input;
	SurfaceHolder holder;
	boolean isRunning=false;
	static int fps=0;
	static private GameView gameView;
	Bitmap picture=BitmapFactory.decodeResource(getResources(), R.drawable.blank);
	Bitmap wall=BitmapFactory.decodeResource(getResources(), R.drawable.rocktile);
	Character hero;
	GameAssets assets;
	public GameView(Context context) {
		super(context);	
		holder=getHolder();
		assets=new GameAssets(context);
		input=new Input(this);
		setOnTouchListener(this);
		gameView=this;
		hero=new Character();
	}
	static GameView getinstance(){
		return gameView;
	}

	@Override
	public void run() {
		Paint paint=new Paint();
		paint.setColor(Color.RED);
		paint.setStyle(Style.STROKE);
		while(isRunning){
			if(!holder.getSurface().isValid())
				continue;
			myCanvas=holder.lockCanvas();
			myCanvas.drawARGB(255,0, 0, 0);//backgorund
			myCanvas.drawText("FPS:"+fps, 25, 25, paint);
			myCanvas.drawText("X:"+hero.x, 25, 50, paint);
			myCanvas.drawText("Y:"+hero.y, 25, 75, paint);
			myCanvas.drawRect(new Rect(256,0,768,352), paint);//PlayScreen
			myCanvas.drawBitmap(wall, 256, 0,null);
			myCanvas.drawBitmap(wall, 288, 0,null);
			myCanvas.drawBitmap(wall, 320, 0,null);
			myCanvas.drawBitmap(wall, 352, 0,null);
			myCanvas.drawBitmap(wall, 384, 0,null);
			myCanvas.drawBitmap(wall, 416, 0,null);
			myCanvas.drawBitmap(wall, 448, 0,null);
			myCanvas.drawBitmap(wall, 480, 0,null);
			myCanvas.drawBitmap(input.leftArrow,input.leftInputX,input.leftInputY,null);
			myCanvas.drawBitmap(input.downArrow,input.downInputX,input.downInputY,null);
			myCanvas.drawBitmap(input.rightArrow,input.rightInputX,input.rightInputY,null);
			myCanvas.drawBitmap(input.upArrow,input.upInputX,input.upInputY,null);
			hero.render(myCanvas);
			holder.unlockCanvasAndPost(myCanvas);
		}
	}
	public void pause(){
		isRunning=false;
		logic.isRunning=false;
		while(true){
			try{t.join();
				logic.join();
			}
			catch (InterruptedException e){e.printStackTrace();}
			break;
		}
		t=null;
		logic=null;
	}
	public void resume(){
		isRunning=true;
		t=new Thread(this);
		t.start();
		logic=new GameLogic();
		logic.start();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN:
			input.getPressedButton(event);
			break;
		case MotionEvent.ACTION_UP:
			input.ReleaseButton(event);
			break;
		case MotionEvent.ACTION_MOVE:
			input.DragRelease(event);
		}
		return true;
	}
}
