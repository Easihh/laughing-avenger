package drawing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import main.Hero;
import main.Map;

public class GameDrawPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private Hero hero;
	private Map map;
	public static int worldTranslateX,worldTranslateY;
	public GameDrawPanel(){
		setPreferredSize(new Dimension(512,512));
		worldTranslateX=worldTranslateY=0;
		setBackground(Color.CYAN);
		map=Map.getInstance();
		hero=Hero.getInstance();
	}
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		checkBoundary();
		tryTranslate();
		if(worldTranslateX!=0 || worldTranslateY!=0)
			g.translate(worldTranslateX, worldTranslateY);
		map.render(g);
		hero.render(g);
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
