import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Monster {
	int polygonType;
	Rectangle2D.Double mask=new Rectangle2D.Double();
	int movement_speed=2;
	BufferedImage img=null;
	enum direction{Left,Right};
	direction dir;
	int x;
	int y;
	int type;
	int width;
	int height;
	Animation animLeft;
	Animation animRight;
	long last_update;
	public Monster(int x, int y,int type){
		this.x=x;
		this.y=y;
		this.type=type;
		dir=direction.Right;
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
		width=img.getWidth();
		height=img.getHeight();
		mask.setRect(x,y ,img.getWidth(), img.getHeight());
	}
	public void update(){
		Boolean isColliding=false;
			//if(getCollision(new Point(x-movement_speed,y), 
				//	(new Point((int)(x+mask.width),y)))){
		isColliding=getCollision(new Point(x,y+height+2), 
				(new Point((int)(x+width),y+height+2)));
		if(!isColliding){
			y+=movement_speed;
			mask.y+=movement_speed;
		}
		if(isColliding){
			if(dir==direction.Left){
				x-=movement_speed;
				mask.x=x;
			}
			if(dir==direction.Right){
				x+=movement_speed;
				mask.x=x;
			}
			if(getCollision(new Point(x+width-1,y+height-3), 
					(new Point((int)(x+width-1),y)))){
				if(polygonType==1){
					y+=movement_speed+1;
					mask.y=y;
					x+=movement_speed;
					mask.x=x;
				}
				if(polygonType==3){
					x+=movement_speed;
					mask.x=x;
				}
				System.out.println("type:"+polygonType);
			}
		}
	}
	private boolean getCollision(Point pt1,Point pt2) {
		for(Tile aTile:Level.tile)
			if(aTile.shape.contains(pt1)|| aTile.shape.contains(pt2)){
				polygonType=aTile.type;
				return true;
				}
		return false;
	}
	public void draw(Graphics g){
		animLeft.setImage(System.nanoTime()-last_update);
		switch(dir){
		case Right: if(polygonType==1){
					rotateImage(g,45);
					movement_speed=0;
					}else{
					g.drawImage(animRight.getImage(), x, y, null);
					g.drawRect((int)mask.x, (int)mask.y, (int)mask.width, (int)mask.height);
				}
					break;
		case Left: g.drawImage(animLeft.getImage(), x, y, null);
					g.drawRect((int)mask.x, (int)mask.y, (int)mask.width, (int)mask.height);
					break;
		}
		g.setColor(Color.red);
		last_update=System.nanoTime();
	}
	private void rotateImage(Graphics g, double degree) {
		Graphics2D g2d=(Graphics2D)g;
		// The required drawing location
		//int drawLocationX = x;
		//int drawLocationY = y;

		// Rotation information

		//double rotationRequired = Math.toRadians(degree);
		//double locationX = img.getWidth() / 2;
		//double locationY = img.getHeight() / 2;
		//AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
		//AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

		// Drawing the rotated image at the required drawing locations
		//g2d.drawImage(op.filter(img, null), x, y, null);
		g2d.rotate(Math.toRadians(degree),mask.x,mask.y);
		g2d.drawImage(img, x, y, width, height, null);
		//g2d.setColor(Color.red);
		//g2d.rotate(315,);
		g2d.drawRect((int)mask.x, (int)mask.y, (int)mask.width, (int)mask.height);
		g2d.dispose();
	}
}
