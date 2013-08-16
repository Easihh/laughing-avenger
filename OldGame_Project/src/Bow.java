import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.awt.Image;
import javax.imageio.ImageIO;
public class Bow extends Item{
	private int x;
	private int y;//
	private boolean ownership=true;
	Rectangle2D mask=new Rectangle2D.Double();
	Bow(){
		super();
		x=150;
		y=79;
		mask.setRect(x, y, 32, 32);
	}
	public boolean getOwnership(){
		return(ownership);
	}
	public void acquired(){
		ownership=true;
	}
	public void draw(Graphics g){
		URL imgURL=getClass().getResource("/images/bow.gif");
		try {
			BufferedImage im=ImageIO.read(imgURL);
			g.drawImage((Image)im, x, y, null);
			}
		catch(Exception e){}
	}
	public Rectangle2D getMask(){
		return mask;
	}
}
