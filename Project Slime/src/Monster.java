import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Monster {
	Rectangle2D.Double mask=new Rectangle2D.Double();
	int movement_speed=3;
	BufferedImage img=null;
	enum direction{Left,Right};
	direction dir;
	int x;
	int y;
	int type;
	Animation animLeft;
	Animation animRight;
	long last_update;
	public Monster(int x, int y,int type){
		this.x=x;
		this.y=y;
		this.type=type;
		dir=direction.Left;
		animLeft=new Animation();
		animRight=new Animation();
		last_update=System.nanoTime();
		setImage();
	}
	public void setImage(){
		try {
			img=ImageIO.read(getClass().getResource("/tileset/slimeLeftWalk1.png"));
			animLeft.addScene(img, 0.5);
			img=ImageIO.read(getClass().getResource("/tileset/slimeLeftWalk2.png"));
			animLeft.addScene(img, 0.5);
			img=ImageIO.read(getClass().getResource("/tileset/slimeRightWalk1.png"));
			animRight.addScene(img, 0.5);
			img=ImageIO.read(getClass().getResource("/tileset/slimeRightWalk2.png"));
			animRight.addScene(img, 0.5);
		} catch (IOException e) {
			e.printStackTrace();
		}
		mask.setRect(x,y ,img.getWidth(), img.getHeight());
	}
	public void setMovement(){
		switch(dir){
		case Right: x+=movement_speed;
					mask.x=x;
					break;
		case Left: 	x-=movement_speed;
					mask.x=x;
					break;
		}
	}
	public void draw(Graphics g){
		animLeft.setImage(System.nanoTime()-last_update);
		//animRight.setImage(System.nanoTime()-last_update);
		switch(dir){
		case Right: g.drawImage(animRight.getImage(), x, y, null);
					break;
		case Left: g.drawImage(animLeft.getImage(), x, y, null);
					break;
		}
		last_update=System.nanoTime();
	}
}
