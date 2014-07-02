import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public abstract class Monster extends Tile {
	protected final long nano=1000000L;
	protected boolean boat_movement=false;
	protected final int duration_in_water=7000;
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
	public Monster(int x, int y, ID type) {
		super(x, y, type);
	}
	public abstract void transform();
	public abstract void render(Graphics g);
	
	public void moveInWater() {	
		if(!isDrowning){
			switch(Character.getInstance().dir){
			case Left:	Character.targetX=-2*Character.getInstance().step;
						break;
			case Right:	Character.targetX=2*Character.getInstance().step;
						break;
			case Up:	Character.targetX=-2*Character.getInstance().step;
						break;
			case Down:	Character.targetX=2*Character.getInstance().step;
						break;
			}
			time_since_water=System.nanoTime();
			img=Game.monsterState.get(0);
			time_since_transform=0;
			TransformedState=1;
			type=Tile.ID.boat;
			isSolid=false;
			Sound.resetSound();
			Sound.Water.start();
		}
		Character.isMoving=true;
		Character.isPushing=true;
		isDrowning=true;
	}
	public void boatMovement() {
		Tile findWaterFlow;
		if(WaterDir==Game.Direction.Right){
			if(checkWaterCollision(new Rectangle(x+width,y,1,32))){
				if(Character_is_on_boat())
					Character.getInstance().setX(Character.getInstance().getX()+1);
				x+=1;
			}
			else{
				findWaterFlow=getWaterFlow(new Rectangle(x,y+32+1,32,1));
				if(findWaterFlow.type==Tile.ID.WaterFlowDown)
					WaterDir=Game.Direction.Down;						
				}
		}
		else if(WaterDir==Game.Direction.Down){
				if(checkWaterCollision(new Rectangle(x,y+32,32,1))){
					if(Character_is_on_boat())
						Character.getInstance().setY(Character.getInstance().getY()+1);
					y+=1;
				}
				else{
					findWaterFlow=getWaterFlow(new Rectangle(x-1,y,1,32));
					if(findWaterFlow.type==Tile.ID.WaterFlowLeft)
						WaterDir=Game.Direction.Left;
					}
		}
		else if(WaterDir==Game.Direction.Left){
				if(checkWaterCollision(new Rectangle(x-1,y,1,32))){
					if(Character_is_on_boat())
						Character.getInstance().setX(Character.getInstance().getX()-1);
					x-=1;
				}
				else{
					findWaterFlow=getWaterFlow(new Rectangle(x,y-1,32,1));
					if(findWaterFlow.type==Tile.ID.WaterFlowUp)
						WaterDir=Game.Direction.Up;
					}
		}
		else if(WaterDir==Game.Direction.Up){
						if(checkWaterCollision(new Rectangle(x,y-1,32,1))){
							if(Character_is_on_boat())
								Character.getInstance().setY(Character.getInstance().getY()-1);
							y-=1;
						}
						else{
							findWaterFlow=getWaterFlow(new Rectangle(x+32+1,y,32,32));
							if(findWaterFlow.type==Tile.ID.WaterFlowRight)
								WaterDir=Game.Direction.Right;
							}
		}
	}
	private boolean Character_is_on_boat() {
		if(Character.getInstance().getX()==x && Character.getInstance().getY()==y)
			return true;
		return false;
	}
	private Tile getWaterFlow(Rectangle mask) {
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i) instanceof Water)
				if(Level.map_tile.get(i).shape.intersects(mask))
					return Level.map_tile.get(i);
		}
		return null;
	}
	private boolean checkWaterCollision(Rectangle mask) {
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i) instanceof Water)
				if(Level.map_tile.get(i).shape.intersects(mask))
					return true;
		}
		return false;
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
		if((System.nanoTime()-time_since_transform)/nano>7000 && TransformedState==1 && !isDrowning){
			TransformedState=2;
			img=Game.monsterState.get(1);
		}
		if((System.nanoTime()-time_since_transform)/nano>10000 && TransformedState==2 && !isDrowning){
			TransformedState=0;
			img=previousState;
		}
		if((System.nanoTime()-time_since_water)/nano>4000 && TransformedState==1 && isDrowning){
			TransformedState=3;
			img=Game.monsterState.get(2);
		}
		if((System.nanoTime()-time_since_water)/nano>6000 && TransformedState==3 && isDrowning){
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
