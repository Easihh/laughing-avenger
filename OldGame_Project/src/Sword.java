import java.awt.geom.Rectangle2D;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.*;

public class Sword {
	Rectangle2D mask;
	int x;
	int y;
	int swordDirection;
	public Sword(int direction,int x,int y,int width,int height){
		this.x=x;
		this.y=y;
		swordDirection=direction;
		mask=new Rectangle2D.Double();
		mask.setRect(this.x, this.y, width, height);
	}
	public Rectangle2D getMask(){
		return mask;
	}
	public Image getImage(){
		if(swordDirection==270){
		try{	
			URL imgURL=getClass().getResource("/images/sword_up.gif");
			BufferedImage img=ImageIO.read(imgURL);
			return (Image)img;
		}catch(Exception e){}
		}
		if(swordDirection==90){
			try{	
				URL imgURL=getClass().getResource("/images/sword_down.gif");
				BufferedImage img=ImageIO.read(imgURL);
				return (Image)img;
			}catch(Exception e){}
			}
		if(swordDirection==180){
			try{	
				URL imgURL=getClass().getResource("/images/sword_left.gif");
				BufferedImage img=ImageIO.read(imgURL);
				return (Image)img;
			}catch(Exception e){}
			}
		if(swordDirection==360){
			try{	
				URL imgURL=getClass().getResource("/images/sword_right.gif");
				BufferedImage img=ImageIO.read(imgURL);
				return (Image)img;
			}catch(Exception e){}
			}
		return null;
	}
	public void draw(Graphics g){
		g.drawImage(getImage(), x, y, null);
	}
}
