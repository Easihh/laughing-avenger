import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Stack;


public abstract class Monster extends Tile {
	protected int TransformedState=0;
	protected Projectile projectile;
	protected boolean canShoot=false;
	public boolean isActive;
	public boolean isDrowning=false;
	public Image previousState;
	protected long time_since_water;
	public Stack<Node> Path;
	public ArrayList<Node> Open;
	public ArrayList<Node> Closed;
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
	
	public void addNeighbor(Node neighbor, Node current) {
		if(!Closed.contains(neighbor)){
			if(!Open.contains(neighbor)){
				Open.add(neighbor);
				neighbor.parent=current;
				neighbor.Gscore=current.Gscore+1;
				neighbor.updateScore(x, y);
			}
			if(Open.contains(neighbor) && (current.Gscore+1<neighbor.Gscore)){
				neighbor.parent=current;
				neighbor.Gscore=current.Gscore+1;
				neighbor.updateScore(x, y);
			}
		}
	}
	public void buildPath(){
		Node temp=Closed.get(Closed.size()-1);
		while(temp!=null){
			Path.add(temp);
			temp=temp.parent;
		}
	}
}
