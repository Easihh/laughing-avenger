import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.Image;
abstract class Monster {
	private static int id;
	private int myID;
	protected int x;
	protected int y;
	protected int hp;
	protected int direction;
	private Rectangle2D mask;
	Monster(int x,int y){
		direction=90;
		myID=id;
		id++;
		hp=3;
		this.x=x;
		this.y=y;
		mask=new Rectangle2D.Double();
		mask.setRect((double)x, (double)y, 32, 32);
	}
	public Rectangle2D getMask(){
		return mask;
	}
	public int getID(){
		return myID;
	}
	abstract public void setDamage();
	public int getHP(){
		return hp;
	}
	abstract Image getImage();
	abstract public void draw(Graphics g);
	abstract public void AI();
}
