package drawing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import utility.Ressource;

import main.Animation;
import main.Hero;
import main.Map;
import main.Shop;

public class GameDrawPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Hero hero;
	private Map map;
	private Animation rupee;
	public static int worldTranslateX,worldTranslateY;
	public GameDrawPanel(){
		setPreferredSize(new Dimension(512,512));
		worldTranslateX=worldTranslateY=0;
		setBackground(Color.CYAN);
		map=Map.getInstance();
		hero=Hero.getInstance();
		setRupeeAnimation();
	}
	private void setRupeeAnimation() {
		rupee=new Animation();
		rupee.AddScene(Ressource.rupee.elementAt(0), 200);
		rupee.AddScene(Ressource.rupee.elementAt(1), 200);
	}
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D x=(Graphics2D) g;
		checkBoundary();
		tryTranslate();
		if(worldTranslateX!=0 || worldTranslateY!=0)
			x.translate(worldTranslateX, worldTranslateY);
		map.render(x);
		if(hero.isInsideShop==Shop.ID.CandleShop.value){
			rupee.setImage();
			x.setColor(Color.white);
			x.drawString("X",map.worldX*map.roomWidth+160,map.worldY*map.roomHeight+340);
			x.drawImage(rupee.getImage(),map.worldX*map.roomWidth+128,map.worldY*map.roomHeight+320,null);
		}
		hero.render(x);
	}
	private void checkBoundary() {
		switch(hero.isOutsideRoomBoundary()){
		case 1: 
				hero.translateX=map.roomWidth;
				hero.translateY=0;
				break;
		case 2:	
				hero.translateX=0;
				hero.translateY=-map.roomHeight;
				break;
		case 3:	
				hero.translateX=-map.roomWidth;
				hero.translateY=0;
				break;
		case 4:	
				hero.translateX=0;
				hero.translateY=map.roomHeight;
				break;
		}
	}
	
	private void tryTranslate() {
		int scrollSpeed=8;
		if(hero.translateX!=0){
			//Must translate left or right
			if(hero.translateX<0){
				worldTranslateX-=scrollSpeed;
				hero.translateX+=scrollSpeed;
			}
			if(hero.translateX>0){
				worldTranslateX+=scrollSpeed;
				hero.translateX-=scrollSpeed;
			}
		}
		else if(hero.translateY!=0){
			//Must translate up or down
			if(hero.translateY<0){
				worldTranslateY-=scrollSpeed;
				hero.translateY+=scrollSpeed;
			}
			if(hero.translateY>0){
				worldTranslateY+=scrollSpeed;
				hero.translateY-=scrollSpeed;
			}
		}		
	}
}
