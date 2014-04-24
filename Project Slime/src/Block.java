import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Block extends Rectangle2D.Double{
	//Polygon triangle;
	public int type;
	//public int tileType=0;
	private BufferedImage img=null;
	public Block(int x,int y, int type){
	 this.x=x;
	 this.y=y;
	 this.width=24;
	 this.height=24;
	 this.type=type;
	 setImage();
	}
	public void setImage(){
		String imgString=null;
		switch(type){
		case 1: imgString="snowLeft.png";
				break;
		case 2: imgString="snowMid.png";
				break; 
		case 3: imgString="snowRight.png";
				break;
		case 4: 
				break;
		case 5: imgString="snowHillLeft2.png";
				break;
		case 6: 
				break;
		case 7: imgString="snowHillRight2.png";
				break;
		case 8: imgString="snowCenter.png";
				break;
		case 9: imgString="snowCliffLeft.png";
				break;
		case 10: imgString="snowCliffRight.png";
				break;
		case 11: imgString="buttonRed.png";
				break;
		default:imgString="default.png";
				break;
		}
		try {img= ImageIO.read(getClass().getResource("/tileset/"+imgString));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void draw(Graphics g){
		g.setColor(Color.black);
		g.drawImage(img, (int)(x), (int)(y), null);
	}
}
