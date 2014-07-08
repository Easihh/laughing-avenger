package com.easih.projectx;

public class GameLogic extends Thread{
	Boolean isRunning=false;
	private Long elapsed;
	private int fps=0;
	private final int maxFps=60;
	public GameLogic(){
		isRunning=true;
	}
	@Override	
	public void run() {	
		elapsed=System.currentTimeMillis();
		long timer=0;
		while(isRunning){
			fps++;
			while(System.currentTimeMillis()-timer<(1000/maxFps))
				try {Thread.sleep(1);
				} catch (InterruptedException e1) {e1.printStackTrace();}
			timer=System.currentTimeMillis();
			movement();
			if((System.currentTimeMillis()-elapsed)>=1000){
				GameView.fps=fps;
				fps=0;
				elapsed=System.currentTimeMillis();
			}
		}
	}
	private void movement() {
		Character hero=Character.getInstance();
		if(hero.isMovingLeft)hero.x-=1;
		if(hero.isMovingRight)hero.x+=1;
		if(hero.isMovingDown)hero.y+=1;
		if(hero.isMovingUp)hero.y-=1;
	}
}