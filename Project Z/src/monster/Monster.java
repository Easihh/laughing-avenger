package monster;

import item.Arrow;
import item.Heart;
import item.Item;
import item.MagicalRod;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.Hero;
import main.Hero.Direction;
import main.Map;
import main.Projectile;
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
	public Projectile projectile;
	protected static int id=0;
	public double damage;
	public final int invincible_duration=500,width=32,height=32,step=16,pushbackSpeed=4;
	public enum ID{ RedOctorok(1),Fire(2),WoodSword(3),Merchant(4);
		public int value;
		private ID(int value){
			this.value=value;
		}
	}
	public Direction dir,pushbackdir;
	public Monster(int x,int y,Monster.ID type){
		super(x,y,type);
		dir=Direction.None;
		id++;
		myID=id;
		this.x=x;
		this.y=y;
		this.type=type;
		//isSolid=setSolidState();
		img=Ressource.monster_type.get(type.value);
		loadDeathEffect();
		pushbackdir=Direction.None;
	}
	private void loadDeathEffect() {
		try {killEffect=ImageIO.read(getClass().getResourceAsStream("/map/kill_effect.png"));
		} catch (IOException e) {e.printStackTrace();}		
	}
	/*private boolean setSolidState() {
		switch(type){
		case RedOctorok:
		case WoodSword:
			return false;
		}
		return true;
	}*/
	public abstract void update();
	public abstract void render(Graphics g);
	public void checkHeroSwordCollision() {
		Hero hero=Hero.getInstance();
		if((hero.attack.mySword!=null && invincible_timer==null && hero.attack.mySword.mask.intersects(mask)) ||
				hero.attack.theSword!=null && invincible_timer==null && hero.attack.theSword.mask.intersects(mask)){
			monsterHit();
			if(hitpoint>0)
				pushback(96);
			checkIfDead();
		}
		if(hero.specialItem!=null && hero.specialItem.type==Item.ID.Arrow && ((Arrow)hero.specialItem).myArrow!=null)
			if(((Arrow)hero.specialItem).myArrow.mask.intersects(mask)){
				monsterHit();
				checkIfDead();
				((Arrow)hero.specialItem).myArrow=null;
		}
		if(hero.specialItem!=null && hero.specialItem.type==Item.ID.MagicalRod && ((MagicalRod)hero.specialItem).projectile!=null)
			if(((MagicalRod)hero.specialItem).projectile.mask.intersects(mask)){
				monsterHit();
				checkIfDead();
				if(hitpoint>0)
					pushback(64);
				((MagicalRod)hero.specialItem).projectile=null;
				//hero.isAttacking=false;
		}
	}
	private void monsterHit() {
		invincible_timer=new Stopwatch();
		invincible_timer.start();
		Sound.enemyHit.setFramePosition(0);
		Sound.enemyHit.start();
		hitpoint--;	
	}
	private void checkIfDead() {
		if(hitpoint==0){
			Sound.enemyKill.setFramePosition(0);
			Sound.enemyKill.start();
			death=new Stopwatch();
			death.start();
		}
	}
	public void checkHeroCollision(){
		Hero hero=Hero.getInstance();
		int pushDistance=64;
		if(mask.intersects(new Rectangle(hero.x,hero.y,32,32)) && hero.invincible_timer==null)
			hero.getHurt(pushDistance,damage);
		if(projectile!=null && projectile.mask.intersects(new Rectangle(hero.x,hero.y,32,32)) && hero.invincible_timer==null){
			if((projectile.dir==Direction.Down && hero.dir!=Direction.Up) ||
					(projectile.dir==Direction.Left && hero.dir!=Direction.Right) ||
					(projectile.dir==Direction.Right && hero.dir!=Direction.Left) ||
					(projectile.dir==Direction.Up && hero.dir!=Direction.Down))
				hero.getHurt(pushDistance,damage);
			else{
					Sound.shield.setFramePosition(0);
					Sound.shield.start();
				}
			projectile=null;
		}
			
			
	}
	private void pushback(int distance) {
		pushbackdir=Hero.getInstance().dir;
		stepToPush=distance;
	}
	public void destroy(int destroyID) {
		mask=new Rectangle(x,y,0,0);
		for(int i=0;i<Map.getInstance().worldWidth;i++)
			for(int j=0;j<Map.getInstance().worldHeight;j++)
				if(Map.allObject[i][j] instanceof Monster && ((Monster)Map.allObject[i][j]).myID==destroyID){					
					Map.allObject[i][j]=getLoot(Map.allObject[i][j]);
				}
	}
	private Item getLoot(Tile aTile) {
		Point finalPosition=getFinalPosition(aTile);
		if(finalPosition.x%16!=0)System.out.println("Unaligned X");
		if(finalPosition.y%16!=0)System.out.println("Unaligned Y");
		return new Heart(finalPosition.x, finalPosition.y, Item.ID.HeartContainer);
	}
	
	/** Because Monster could be moving/not aligned when killed we want to spawn the Item aligned with Tile.
	 * @param aTile **/
	private Point getFinalPosition(Tile aTile) {
		switch(((Monster)aTile).dir){
		case Left: 	return new Point(x-((Monster)aTile).stepToMove,y);
		case Right:	return new Point(x+((Monster)aTile).stepToMove,y);
		case Up:	return new Point(x,y-((Monster)aTile).stepToMove);
		case Down:	return new Point(x,y+((Monster)aTile).stepToMove);
		}
		return null;
	}
	public void updateMask() {
		mask=new Rectangle(x,y,width,height);	
	}
}
