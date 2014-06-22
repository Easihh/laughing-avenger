import java.awt.Graphics;
import java.awt.Image;


public abstract class Monster extends Tile {
	protected int TransformedState=0;
	protected Projectile projectile;
	protected boolean canShoot=false;
	public boolean isActive;
	public Image previousState;
	public Monster(int x, int y, int type) {
		super(x, y, type);
	}
	public abstract void transform();
	public abstract void render(Graphics g);
}
