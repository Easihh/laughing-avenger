package drawing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

import utility.Ressource;

import main.Hero;

public class Mainbar extends JPanel{
	private static final long serialVersionUID = 1L;
	private Hero hero;
	private BufferedImage itemslot,keyicon,rupeesicon,bombicon;
	public Mainbar(){
		setPreferredSize(new Dimension(512,96));
		hero=Hero.getInstance();
		loadImage();
	}
	private void loadImage() {
		try {itemslot=ImageIO.read(getClass().getResourceAsStream("/Map/Itemslot.png"));
			keyicon=ImageIO.read(getClass().getResourceAsStream("/Map/keys_icon.png"));
			bombicon=ImageIO.read(getClass().getResourceAsStream("/Map/bomb_icon.png"));
			rupeesicon=ImageIO.read(getClass().getResourceAsStream("/Map/rupees_icon.png"));
			} catch (IOException e) {e.printStackTrace();}
	}
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D x=(Graphics2D) g;
		x.setColor(Color.black);
		x.fillRect(0, 0, 512, 96);
		x.setColor(Color.red);
		x.drawImage(itemslot, 240, 18,null);
		x.drawImage(itemslot, 300, 18,null);
		x.drawString("- L I F E -",400,32);
		x.drawString("X:"+hero.x,100,48);
		x.drawString("Y:"+hero.y,100,72);
		drawWeaponSlots(x);
		x.setColor(Color.white);
		x.drawImage(rupeesicon,186,18,null);
		x.drawImage(keyicon,186,38,null);
		x.drawImage(bombicon,186,58,null);
		x.drawString("X"+hero.key_amount,208,52);
		x.drawString("X"+hero.bomb_amount,208,72);
		x.drawString("X"+hero.rupee_amount,208,32);
	}
	private void drawWeaponSlots(Graphics g) {
		Graphics2D x=(Graphics2D) g;
		if(hero.mainWeapon>0)
			x.drawImage(Ressource.weaponType.elementAt(hero.mainWeapon-1), 256, 32,null);
		if(hero.specialItem!=null)
			x.drawImage(hero.specialItem.img,306,32,null);
	}
}
