package item;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;
import javax.imageio.ImageIO;

import utility.Ressource;
import utility.Sound;
import utility.Stopwatch;
import main.Fire;
import main.Hero;
import main.Hero.Direction;
import main.Map;
import main.Shop;
import main.Tile;
import monster.Monster;

public class BlueCandle extends Item {
	public Fire fire;
	public Stopwatch timer,pickUpItemTimer;
	private int stepToMove;
	private BlueCandle myCandle;
	public BlueCandle(int x,int y,Item.ID type){
		super(x, y, type);
		name="Blue Candle";
		cost=60;
		this.x=x;
		this.y=y;
		mask=new Rectangle(x,y,width,height);
		loadImage();
	}
	
	private void loadImage() {
		try {img=ImageIO.read(getClass().getResourceAsStream("/map/BlueCandle.png"));
		} 
		catch (IOException e) {e.printStackTrace();}	
	}
	public void use(){
		Sound.candle.setFramePosition(0);
		Sound.candle.flush();
		Sound.candle.start();
		Hero hero=Hero.getInstance();
		stepToMove=64;
		timer=new Stopwatch();
		timer.start();
		if(hero.dir==Direction.Down)
			fire=new Fire(hero.x, hero.y+height, Monster.ID.Fire);
		if(hero.dir==Direction.Up)
			fire=new Fire(hero.x, hero.y-height, Monster.ID.Fire);
		if(hero.dir==Direction.Left)
			fire=new Fire(hero.x-width, hero.y, Monster.ID.Fire);
		if(hero.dir==Direction.Right)
			fire=new Fire(hero.x+width, hero.y, Monster.ID.Fire);
		fire.dir=hero.dir;
	}
	public void update(){
		if(fire!=null) updateFireMovement();
		else checkCollisionWithHero();
		if(fire!=null && timer!=null && timer.elapsedMillis()>3000){
			timer=null;
			fire=null;
		}
	}
	private void checkCollisionWithHero() {
		if(x==Hero.getInstance().x && y==Hero.getInstance().y && pickUpItemTimer==null){
			if(Hero.getInstance().rupee_amount>=cost){
				Hero.getInstance().rupee_amount-=cost;
				Hero.getInstance().obtainItem=Ressource.obtainItem;
				pickUpItemTimer=new Stopwatch();
				pickUpItemTimer.start();
				Sound.newItem.setFramePosition(0);
				Sound.newInventItem.setFramePosition(0);
				Sound.newItem.start();
				Sound.newInventItem.start();
				myCandle=new BlueCandle(241,98,Item.ID.BlueCandle);
				Hero.getInstance().inventory_items[0][0]=myCandle;
				myCandle.hasOwnership=true;
				//Hero.getInstance().inventory_items[0][0].hasOwnership=true;
			}
		}
		if(pickUpItemTimer!=null){
			y=Hero.getInstance().y-height;
			if(Hero.getInstance().isInsideShop!=Shop.ID.None.value)
				destroyOtherItems();
		}
		if(pickUpItemTimer!=null && pickUpItemTimer.elapsedMillis()>1000){
			removeItemFromShop();
			removeMerchant();
			Hero.getInstance().obtainItem=null;
			myCandle.hasBeenPickedUp=true;
		}
	}

	private void destroyOtherItems() {
		Map map=Map.getInstance();
		Tile[][] theRoom=Map.allShop.get(Hero.getInstance().isInsideShop-1).theRoom;
		for(int i=0;i<map.tilePerRow;i++){
			for(int j=0;j<map.tilePerCol;j++){
				if(theRoom[i][j]!=null && theRoom[i][j] instanceof Item && theRoom[i][j]!=this)	
					theRoom[i][j]=null;
			}
		}
	}

	private void updateFireMovement() {
		if(fire.dir==Direction.Down && stepToMove!=0){
			fire.y++;
			stepToMove--;
		}
		if(fire.dir==Direction.Up && stepToMove!=0){
			fire.y--;
			stepToMove--;
		}
		if(fire.dir==Direction.Left && stepToMove!=0){
			fire.x--;
			stepToMove--;
		}
		if(fire.dir==Direction.Right && stepToMove!=0){
			fire.x++;
			stepToMove--;
		}
	}

	public void render(Graphics g){
		if(fire!=null){
			fire.fireAnimation.setImage();
			g.drawImage(fire.fireAnimation.getImage(),fire.x,fire.y,null);
		}
		if(fire==null && hasBeenPickedUp==false){
			g.drawImage(img,x,y,null);
			g.setColor(Color.WHITE);
			if(pickUpItemTimer==null)
				g.drawString(""+cost, x+8, y+64);
		}
	}
}
