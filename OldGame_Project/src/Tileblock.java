import java.awt.Graphics;
import java.awt.Image;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

public class Tileblock {
	Rectangle2D mask;
	private boolean isWalkable;
	private int x;
	private int y;
	private Image tileset;
	/*public Tileblock(int xposition, int yposition){
		this.x=xposition;
		this.y=yposition;
		mask=new Rectangle2D.Double(this.x,this.y,32,32);
	}*/
	public Tileblock(int xposition,int yposition, URL Graphic,boolean walkable){
		isWalkable=walkable;
		this.x=xposition;
		this.y=yposition;
		if(!isWalkable)
		mask=new Rectangle2D.Double(this.x,this.y,32,32);
		else mask=new Rectangle2D.Double(this.x,this.y,0,0);
		//System.out.println("MASK:"+x+y);
	try	{	BufferedImage img=ImageIO.read(Graphic);
		tileset=(Image)img;
		}
	catch(Exception e){e.printStackTrace();}
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public Rectangle2D getMask(){
		return mask;
	}
	public void draw(Graphics g){
		g.drawImage(tileset, x,y ,null);
	}
}
