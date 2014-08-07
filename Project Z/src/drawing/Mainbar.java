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
import main.Map;

public class Mainbar extends JPanel{
	private static final long serialVersionUID = 1L;
	private Hero hero;
	private BufferedImage itemslot,keyicon,rupeesicon,bombicon,lifefullicon,lifeemptyicon,lifehalficon;
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
			lifefullicon=ImageIO.read(getClass().getResourceAsStream("/Map/Life_Full.png"));
			lifeemptyicon=ImageIO.read(getClass().getResourceAsStream("/Map/Life_Empty.png"));
			lifehalficon=ImageIO.read(getClass().getResourceAsStream("/Map/Life_Half.png"));
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
		x.drawString("- L I F E -",408,24);
		drawLife(x);
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
		drawMinimap(x);
	}
	private void drawMinimap(Graphics2D x) {
		Map map=Map.getInstance();
		x.setColor(Color.GRAY);
		x.fillRect(32, 16, 128, 64);
		for(int i=0;i<map.worldWidth/map.tilePerRow;i++)
			for(int j=0;j<map.worldHeight/map.tilePerCol;j++){
				x.setColor(new Color(185,122,87));
				x.drawRect(32+(8*j), 16+(4*i), 8, 4);
				if(map.worldX==j && map.worldY==i){
					x.setColor(Color.green);
					x.fillRect(32+(8*j), 16+(4*i), 8, 4);
				}
			}
	}
	private void drawLife(Graphics2D x){
		int maxHeartPerRow=8,currentHeartInRow=0;
		int startX=368,startY=52,currentX=368,currentY=52;
			int fullHeartAmt=hero.currentHealth/2;
			int emptyHeartAmt=(hero.maxHealth-hero.currentHealth)/2;
			int halfheartAmt=hero.currentHealth%2;
			//draw Full Hearts
			for(int i=0;i<fullHeartAmt;i++){
				//if(i<maxHeartPerRow)
					x.drawImage(lifefullicon, currentX, currentY, null);
					
				//else x.drawImage(lifefullicon, currentX, startY-16, null);
				currentX+=16;
				currentHeartInRow++;
				if(currentHeartInRow==maxHeartPerRow){
					currentHeartInRow=0;
					currentX=startX;
					currentY=startY-16;
				}
			}
			//Draw Partial hearts
			for(int i=0;i<halfheartAmt;i++){
					x.drawImage(lifehalficon, currentX, currentY, null);
					currentHeartInRow++;
					currentX+=16;
					if(currentHeartInRow==maxHeartPerRow){
						currentHeartInRow=0;
						currentX=startX;
						currentY=startY-16;
					}
			}
			//Draw Empty hearts
			for(int i=0;i<emptyHeartAmt;i++){
					x.drawImage(lifeemptyicon, currentX, currentY, null);
					currentHeartInRow++;
					currentX+=16;
					if(currentHeartInRow==maxHeartPerRow){
						currentHeartInRow=0;
						currentX=startX;
						currentY=startY-16;
					}
			}
	}
	private void drawWeaponSlots(Graphics g) {
		Graphics2D x=(Graphics2D) g;
		if(hero.mainWeapon>0)
			x.drawImage(Ressource.weaponType.elementAt(hero.mainWeapon-1), 256, 32,null);
		if(hero.specialItem!=null)
			x.drawImage(hero.specialItem.img,306,32,null);
	}
}
