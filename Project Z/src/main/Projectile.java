package main;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;

import main.Hero.Direction;

public class Projectile {
	public int x,y;
	private int width;
	private int height;
	private final int speed=5;
	public Direction dir;
	private BufferedImage img;
	public Rectangle mask;
	public Projectile(Rectangle infoRect, BufferedImage img,Direction dir){
		x=infoRect.x;
		y=infoRect.y;
		this.img=img;
		this.dir=dir;
		width=infoRect.width;
		height=infoRect.height;
		mask=new Rectangle(x,y,width,height);
	}
	public void update(){
		switch(dir){
		case Down:	y+=speed;
					break;
		case Up:	y-=speed;
					break;
		case Left:	x-=speed;
					break;
		case Right: x+=speed;
					break;
		}
		mask=new Rectangle(x,y,width,height);
		//System.out.println("X:"+x);
		//System.out.println("Y:"+y);
	}
	public void render(Graphics g){
		g.drawImage(img, x, y,null);		
	}
	public BufferedImage getTilt(double degree,BufferedImage img) {
        double sin = Math.abs(Math.sin(degree)), cos = Math.abs(Math.cos(degree));
        int w = img.getWidth(), h = img.getHeight();
        int neww = (int)Math.floor(w*cos+h*sin), newh = (int)Math.floor(h*cos+w*sin);
        GraphicsEnvironment ge=GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();
        BufferedImage result = gc.createCompatibleImage(neww, newh, Transparency.TRANSLUCENT);
        Graphics2D g = result.createGraphics();
        g.translate((neww-w)/2, (newh-h)/2);
        g.rotate(degree,w/2,h/2);
        g.drawRenderedImage(img, null);
        g.dispose();
        return result;
	}
	public boolean outOfBound() {
		Map map=Map.getInstance();
		if(dir==Direction.Right && x>(map.worldX*map.roomWidth)+map.roomWidth)
			return true;
		if(dir==Direction.Left && x<(map.worldX*map.roomWidth))
			return true;
		if(dir==Direction.Up && y<(map.worldY*map.roomHeight))
			return true;
		if(dir==Direction.Down && y>(map.worldY*map.roomHeight)+map.roomHeight)
			return true;
		return false;
	}
}
