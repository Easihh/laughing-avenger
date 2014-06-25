import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;


public abstract class Monster extends Tile {
	protected int TransformedState=0;
	protected Projectile projectile;
	protected boolean canShoot=false;
	public boolean isActive;
	public boolean isDrowning=false;
	public Image previousState;
	protected long time_since_water;
	public Monster(int x, int y, int type) {
		super(x, y, type);
	}
	public abstract void transform();
	public abstract void render(Graphics g);
	public void moveInWater() {
		
		if(!isDrowning){
			switch(Character.dir){
			case Left:	Character.targetX=-2*Character.step;
						break;
			case Right:	Character.targetX=2*Character.step;
						break;
			case Up:	Character.targetX=-2*Character.step;
						break;
			case Down:	Character.targetX=2*Character.step;
						break;
			}
			time_since_water=System.nanoTime();
		}
		Character.isMoving=true;
		Character.isPushing=true;
		isDrowning=true;
	}
	public boolean checkCollision(Rectangle mask1,Rectangle mask2) {
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i).shape.intersects(mask1)|| Level.map_tile.get(i).shape.intersects(mask2)){
				if(Level.map_tile.get(i).isSolid)return true;
			}
		}
		return false;
	}
}
