package monster;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Attack;
import main.Hero;
import main.Hero.Direction;
import main.Map;
import main.Tile;

import utility.Ressource;
import utility.Sound;
import utility.Stopwatch;

public abstract class Monster extends Tile{
	public ID type;
	public int hitpoint,myID,stepToMove,stepToPush,moveSpeed;
	public BufferedImage img,killEffect;
	public Rectangle mask;
	public Stopwatch invincible_timer,death;
	protected static int id=0;
	public final int invincible_duration=500,width=32,height=32,step=16,pushbackSpeed=4;
	public enum ID{ BlueCandle(0),RedOctorok(1),Fire(2),WoodSword(3);
		public int value;
		private ID(int value){
			this.value=value;
		}
	}
	//public enum Direction{None,Left,Right,Up,Down};
	public Direction dir,pushbackdir;
	public Monster(int x,int y,Monster.ID type){
		super(x,y,type);
		dir=Direction.None;
		id++;
		myID=id;
		this.x=x;
		this.y=y;
		this.type=type;
		isSolid=setSolidState();
		img=Ressource.monster_type.get(type.value);
		loadDeathEffect();
		pushbackdir=Direction.None;
	}
	private void loadDeathEffect() {
		try {killEffect=ImageIO.read(getClass().getResourceAsStream("/map/kill_effect.png"));
		} catch (IOException e) {e.printStackTrace();}		
	}
	private boolean setSolidState() {
		switch(type){
		case BlueCandle:
		case RedOctorok:
		case WoodSword:
			return false;
		}
		return true;
	}
	public abstract void update();
	public abstract void render(Graphics g);
	public void checkHeroSwordCollision() {
		Hero hero=Hero.getInstance();
		if((hero.attack.mySword!=null && invincible_timer==null && hero.attack.mySword.mask.intersects(mask)) ||
				Attack.theSword!=null && invincible_timer==null && Attack.theSword.mask.intersects(mask)){	
			invincible_timer=new Stopwatch();
			invincible_timer.start();
			Sound.enemyHit.setFramePosition(0);
			Sound.enemyHit.start();
			hitpoint--;
			if(hitpoint>0)
				pushback();
			if(hitpoint==0){
				Sound.enemyKill.setFramePosition(0);
				Sound.enemyKill.start();
				death=new Stopwatch();
				death.start();
			}
		}
	}
	private void pushback() {
		pushbackdir=Hero.getInstance().dir;
		stepToPush+=96;
		//System.out.println(dir);
		//System.out.println("StepTomove:"+stepToMove);
	}
	public void destroy(int destroyID) {
		mask=new Rectangle(x,y,0,0);
		for(int i=0;i<Map.getInstance().worldWidth;i++)
			for(int j=0;j<Map.getInstance().worldHeight;j++)
				if(Map.allObject[i][j] instanceof Monster && ((Monster)Map.allObject[i][j]).myID==destroyID)
					Map.allObject[i][j]=null;
	}
	public void updateMask() {
		mask=new Rectangle(x,y,width,height);	
	}
}
