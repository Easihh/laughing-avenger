import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
/* Author Enrico Talbot
 * 
 * This is the Base Class for All Monsters where each type of Monster derive from this class.
 * This class contains all the elements shared by all Monster or Actions that are present in all Monster.
 * Only part of the A* Path algorithm is implemented in this class because the other part such as 
 * bypassing certain obstacles depends on the type of Monster.
 */
public abstract class Monster extends Tile {
	protected final long nano=1000000L;
	protected boolean boat_movement=false;
	protected final int duration_in_water=7000;
	protected int TransformedState=0;
	protected Projectile projectile;
	protected boolean canShoot=false;
	public boolean isActive,isDrowning=false;
	public Image previousState;
	protected long time_since_water,time_since_transform;
	public Stack<Node> Path;
	public ArrayList<Node> Open,Closed;
	public Monster(int x, int y, ID type) {
		super(x, y, type);
	}
	public abstract void transform();
	public abstract void render(Graphics g);
	
	/* This Method decides how  far the Monster will be thrown in the water and its change of state*/
	public void moveInWater() {	
		if(!isDrowning){
			switch(Labyrinth.hero.dir){
			case Left:	Labyrinth.hero.targetX=-2*Labyrinth.hero.step;
						break;
			case Right:	Labyrinth.hero.targetX=2*Labyrinth.hero.step;
						break;
			case Up:	Labyrinth.hero.targetX=-2*Labyrinth.hero.step;
						break;
			case Down:	Labyrinth.hero.targetX=2*Labyrinth.hero.step;
						break;
			default:
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
		Labyrinth.hero.isMoving=true;
		Labyrinth.hero.isPushing=true;
		isDrowning=true;
	}
	/* This decides how the boat should be moving on Water.Only a Type of Water with a direction Flow 
	 * can cause the boat to move.The boat will keep moving in the same direction as the Water Flow
	 * until the next tile in the same direction makes it unable to move on and in this case it will 
	 * try to go in the next clock-wise direction ie Right->Down->Left->Up->Right until it is destroyed.
	 */
	public void boatMovement() {
		Tile findWaterFlow;
		if(WaterDir==Game.Direction.Right){
			if(checkWaterCollision(new Rectangle(x,y,1,32),Tile.ID.WaterFlowRight)){
				if(Character_is_on_boat())
					Labyrinth.hero.x=(Labyrinth.hero.x+1);
				x+=1;
			}
			else{
				findWaterFlow=getWaterFlow(new Rectangle(x,y,1,32));
				if(findWaterFlow!=null && findWaterFlow.type==Tile.ID.WaterFlowDown)
					WaterDir=Game.Direction.Down;
			}
		}
		else if(WaterDir==Game.Direction.Down){
				if(checkWaterCollision(new Rectangle(x,y,32,1),ID.WaterFlowDown)){
					if(Character_is_on_boat())
						Labyrinth.hero.y=(Labyrinth.hero.y+1);
					y+=1;
				}
				else{
					findWaterFlow=getWaterFlow(new Rectangle(x,y,32,1));
					if(findWaterFlow!=null && findWaterFlow.type==Tile.ID.WaterFlowLeft)
						WaterDir=Game.Direction.Left;
					}
		}
		else if(WaterDir==Game.Direction.Left){
				if(checkWaterCollision(new Rectangle(x,y,32,1),Tile.ID.WaterFlowLeft)){
					if(Character_is_on_boat())
						Labyrinth.hero.x=(Labyrinth.hero.x-1);
					x-=1;
				}
				else{
					findWaterFlow=getWaterFlow(new Rectangle(x,y,32,1));
					if(findWaterFlow!=null && findWaterFlow.type==Tile.ID.WaterFlowUp)
						WaterDir=Game.Direction.Up;
					}
		}
		else if(WaterDir==Game.Direction.Up){
						if(checkWaterCollision(new Rectangle(x,y-1,32,1),Tile.ID.WaterFlowUp)){
							if(Character_is_on_boat())
								Labyrinth.hero.y=(Labyrinth.hero.y-1);
							y-=1;
						}
						else{
							findWaterFlow=getWaterFlow(new Rectangle(x,y-1,32,1));
							if(findWaterFlow!=null && findWaterFlow.type==Tile.ID.WaterFlowRight)
								WaterDir=Game.Direction.Right;
						}
		}
	}
	private boolean Character_is_on_boat() {
		if(Labyrinth.hero.x==x && Labyrinth.hero.y==y)
			return true;
		return false;
	}
	/* Determine which WaterFlow type we are in contact with in order to decide where the boat 
	 * should be heading.
	 */
	private Tile getWaterFlow(Rectangle mask) {
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i) instanceof Water)
				if(Level.map_tile.get(i).shape.intersects(mask))
					return Level.map_tile.get(i);
		}
		return null;
	}
	private boolean checkWaterCollision(Rectangle mask, ID Water) {
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i).type==Water)
				if(Level.map_tile.get(i).shape.intersects(mask))
					return true;
		}
		return false;
	}
	/* A* Algorithmn addNeighbor part shared by all Monster.*/
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
	/* Monster that are shot once and not thrown into Water, are slowly changed back to previous state 
	 * after a certain amount of time.Monster thrown into Water are destroyed after a period of Time.
	 */
	public void checkState() {
		if((System.nanoTime()-time_since_transform)/nano>7000 && TransformedState==1 && !isDrowning){
			TransformedState=2;
			img=Game.monsterState.get(1);
		}
		if((System.nanoTime()-time_since_transform)/nano>10000 && TransformedState==2 && !isDrowning){
			TransformedState=0;
			img=previousState;
			type=oldtype;
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
	/* Because Monster try and move toward the main Character via A* Algorithm, it is possible 
	 * there are no path; In such a case, when the Monster hit an obstacle; it should pick a new 
	 * direction to move.
	 */
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
