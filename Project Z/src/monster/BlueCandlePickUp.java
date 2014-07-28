package monster;

import java.awt.Graphics;

import main.BlueCandle;
import main.Hero;

import utility.Ressource;
import utility.Sound;
import utility.Stopwatch;

public class BlueCandlePickUp extends Monster{
	public Stopwatch pickUpItemTimer;
	public BlueCandlePickUp(int x, int y, ID type) {
		super(x,y,type);
		this.type=type;
		img=Ressource.monster_type.get(type.value);
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
			Hero.getInstance().inventory_items[0][0]=new BlueCandle();
			Hero.getInstance().inventory_items[0][0].hasOwnership=true;
		}
		if(pickUpItemTimer!=null)
			y=Hero.getInstance().y-height;
		if(pickUpItemTimer!=null && pickUpItemTimer.elapsedMillis()>1000){
			destroy(myID);
			Hero.getInstance().obtainItem=null;
		}
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(img,x,y,null);
	}
	
}
