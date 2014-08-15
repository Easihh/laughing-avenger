package item;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.io.IOException;

import javax.imageio.ImageIO;

import utility.Stopwatch;

import main.Hero;
import main.Main;
import main.Main.GameState;

public class BluePotion extends Item {
	public Stopwatch timer;
	public BluePotion(int x, int y, ID type) {
		super(x, y, type);
		name="Blue Potion";
		inventoryX=x;
		InventoryY=y;
		this.x=x;
		this.y=y;
		mask=new Rectangle(x,y,width,height);
		loadImage();
	}
	private void loadImage() {
		try {img=ImageIO.read(getClass().getResourceAsStream("/map/BlueLifePotion.png"));} 
		catch (IOException e) {e.printStackTrace();}	
	}
	@Override
	public void render(Graphics g) {}

	@Override
	public void use() {
		Main.gameStatus=GameState.Paused;
		timer=new Stopwatch();
		timer.start();
	}

	@Override
	public void update() {
		Hero hero=Hero.getInstance();
		if(timer!=null && timer.elapsedMillis()>500){
			hero.currentHealth++;
			timer.reset();
			if(hero.currentHealth==hero.maxHealth){			
				hero.specialItem=null;
				Main.gameStatus=GameState.Normal;
			}
		}
			
	}

}
