import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public class Selector extends Rectangle2D.Double{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Selector(){
		super();
		height=32;
		width=32;
		x=150;
		y=79;
	}
	public int  getXInt(){
		return (int)x;
	}
	public int getYInt(){
		return (int)y;
	}
	public void setSelectX(int pos){
		x=pos;
	}
	public void setSelectY(int pos){
		y=pos;
	}
	public void draw(Graphics g){
	    g.setColor(Color.white);
	    g.drawLine(getXInt(), getYInt(), getXInt(), getYInt()+32);
	    g.drawLine(getXInt(), getYInt(), getXInt()+8, getYInt());
	    g.drawLine(getXInt(), getYInt()+32, getXInt()+8, getYInt()+32);
	    g.drawLine(getXInt()+32, getYInt(), getXInt()+32, getYInt()+32);
	    g.drawLine(getXInt()+32, getYInt(), getXInt()+24, getYInt());
	    g.drawLine(getXInt()+32, getYInt()+32, getXInt()+24, getYInt()+32);
	}
}
