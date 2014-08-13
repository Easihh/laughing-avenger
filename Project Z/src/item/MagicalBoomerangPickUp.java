package item;

import java.awt.Graphics;

import main.Hero;

import utility.Ressource;
import utility.Sound;

public class MagicalBoomerangPickUp extends Item{

	public MagicalBoomerangPickUp(int x, int y, ID type) {
		super(x, y, type);
		img=Ressource.monster_type.get(9);
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(img,x,y,null);
	}

	@Override
	public void use() {	}

	@Override
	public void update() {
		if(x==Hero.getInstance().x && y==Hero.getInstance().y){
			Sound.newInventItem.setFramePosition(0);
			Sound.newInventItem.start();
			Hero.getInstance().inventory_items[0][4]=new MagicalBoomerang(369,98,Item.ID.MagicalBoomerang);;
			Hero.getInstance().inventory_items[0][4].hasOwnership=true;
			removeItemFromShop();
		}
	}

}
