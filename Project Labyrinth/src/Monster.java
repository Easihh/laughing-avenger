import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;


public abstract class Monster extends Tile {
	protected final long nano=1000000L;
	protected long time_since_transform;
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
			type=Tile.ID.boat.value;
			isSolid=false;
			Sound.resetSound();
			Sound.Water.start();
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
	
	public void checkState() {
		if((System.nanoTime()-time_since_transform)/nano>7000 && TransformedState==1){
			TransformedState=2;
			img=Game.monsterState.get(1);
		}
		if((System.nanoTime()-time_since_transform)/nano>10000 && TransformedState==2){
			TransformedState=0;
			type=0;
			img=previousState;
		}
		if((System.nanoTime()-time_since_water)/nano>3500 && TransformedState==1 && isDrowning){
			TransformedState=3;
			img=Game.monsterState.get(2);
		}
		if((System.nanoTime()-time_since_water)/nano>5000 && TransformedState==3 && isDrowning){
			TransformedState=4;
			img=Game.monsterState.get(3);
		}
	}
	public void getnewDirection() {
		int direction=0;
		int new_direction=0;
		if(dir==Game.Direction.Down)
			direction=1;
		if(dir==Game.Direction.Left)
			direction=2;
		if(dir==Game.Direction.Right)
			direction=3;
		if(dir==Game.Direction.Up)
			direction=4;
		Random random=new Random();
		do
			new_direction=random.nextInt(5);
		while (new_direction==0 || new_direction==direction);
		if(new_direction==1)
			dir=Game.Direction.Down;
		if(new_direction==2)
			dir=Game.Direction.Left;
		if(new_direction==3)
			dir=Game.Direction.Right;
		if(new_direction==4)
			dir=Game.Direction.Up;
	}
}
