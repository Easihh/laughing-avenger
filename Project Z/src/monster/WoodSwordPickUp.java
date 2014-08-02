package monster;

import java.awt.Graphics;

import main.Hero;
import main.Item;
import utility.Ressource;
import utility.Sound;
import utility.Stopwatch;

public class WoodSwordPickUp extends Item{
	public Stopwatch pickUpItemTimer;
	public WoodSwordPickUp(int x, int y, Item.ID type) {
		super(x,y,type);
		this.x=x;
		this.y=y;
		this.type=type;
		img=Ressource.monster_type.get(3);
	}
	public void render(Graphics g) {
		g.drawImage(img,x,y,null);
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		if(x==Hero.getInstance().x && y==Hero.getInstance().y && pickUpItemTimer==null){
			Hero.getInstance().obtainItem=Ressource.obtainItem;
			pickUpItemTimer=new Stopwatch();
			pickUpItemTimer.start();
			Sound.newItem.setFramePosition(0);
			Sound.newInventItem.setFramePosition(0);
			Sound.newItem.start();
			Sound.newInventItem.start();
			Hero.getInstance().mainWeapon=1;
		}
		if(pickUpItemTimer!=null)
			y=Hero.getInstance().y-height;
		if(pickUpItemTimer!=null && pickUpItemTimer.elapsedMillis()>1000){
			removeItemFromShop();
			removeMerchant();
			Hero.getInstance().obtainItem=null;
		}
	}
	@Override
	public void use() {}
}
