import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public abstract class Item {
	abstract void draw(Graphics g);
	abstract Rectangle2D getMask();
	abstract boolean getOwnership();
	abstract void acquired();
}
