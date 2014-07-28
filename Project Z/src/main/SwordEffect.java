package main;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SwordEffect {
	Image topLeftParticle;
	Image topRightParticle;
	Image bottomLeftParticle;
	Image bottomRightParticle;
	int topLeftX,topRightX,bottomRightX,bottomLeftX;
	int topLeftY,topRightY,bottomRightY,bottomLeftY;
	int startX,startY;
	final int effect_speed=2;
	public SwordEffect(Point startPoint){
		loadEffect();
		startX=startPoint.x;
		startY=startPoint.y;
		topLeftX=bottomLeftX=startX;
		topRightX=bottomRightX=startX+16;
		topLeftY=topRightY=startY;
		bottomLeftY=bottomRightY=startY+16;
	}

	private void loadEffect() {
	try {
		topRightParticle=ImageIO.read(getClass().getResourceAsStream("/Map/sword_effect_topright.png"));
		topLeftParticle=ImageIO.read(getClass().getResourceAsStream("/Map/sword_effect_topleft.png"));
		bottomRightParticle=ImageIO.read(getClass().getResourceAsStream("/Map/sword_effect_bottomright.png"));
		bottomLeftParticle=ImageIO.read(getClass().getResourceAsStream("/Map/sword_effect_bottomleft.png"));
	} catch (IOException e) {e.printStackTrace();}
	}
	public void update(){
		topLeftX-=effect_speed;
		topLeftY-=effect_speed;
		topRightX+=effect_speed;
		topRightY-=effect_speed;
		bottomLeftX-=effect_speed;
		bottomLeftY+=effect_speed;
		bottomRightX+=effect_speed;
		bottomRightY+=effect_speed;
	}
	public void render(Graphics g){
		g.drawImage(topLeftParticle,topLeftX,topLeftY,null);
		g.drawImage(topRightParticle,topRightX,topRightY,null);
		g.drawImage(bottomLeftParticle,bottomLeftX,bottomLeftY,null);
		g.drawImage(bottomRightParticle,bottomRightX,bottomRightY,null);
	}
}
