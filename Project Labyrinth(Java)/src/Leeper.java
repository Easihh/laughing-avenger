import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
/*Author Enrico Talbot
 * 
 * This class represent the Green Monster that follows the Hero in Game.It's special ability is
 * to Sleep and become a solid block as soon as it is in contact with the Hero.
 */
public class Leeper extends Monster{
	private boolean path_exist=false;
	private int step_to_move;
	private final int step=2,movement=16;
	private final long nano=1000000L;
	private int last_update=0;
	private Movement move,sleep;
	private Node nextMovement;
	public boolean isSleeping;
	
	public Leeper(int x, int y, ID type) {
		super(x, y, type);
		move=new Movement("leeper_movement", 150);
		sleep=new Movement("leeper_sleep",500);//since this Monster has different animation when sleeping
		depth=2;
		isSleeping=false;
		dir=Game.Direction.Left;
		Path=new Stack<Node>();
	}
	@Override
	public void render(Graphics g) {
		if(!isSleeping){
			if(TransformedState==0 && !isMovingAcrossScreen)
				g.drawImage(move.getWalkAnimation(dir).getImage(), x, y, null);
			else{
				if(isMovingAcrossScreen)
					super.updateLocation();
				if(!isOffScreen())
					g.drawImage(img,x,y,width,height,null); 
				}	
		}
		else g.drawImage(sleep.getWalkAnimation(dir).getImage(),x,y,null);
	}
	private void checkIfDrown() {
		if((System.nanoTime()-time_since_water)/nano>duration_in_water && TransformedState==4 && isDrowning){
			Kill_Respawn();
		}
	}
	@Override
	public void transform() {
		if(!isSleeping){
		previousState=img;
		type=Tile.ID.MoveableBlock;
		img=Game.monsterState.get(0);
		time_since_transform=System.nanoTime();	
		TransformedState=1;
		}
	}
	public void update(){
		checkState();
		checkIfDrown();
		if(!isSleeping && TransformedState==0){
			move.getWalkAnimation(dir).setImage();
			if(Labyrinth.GameState==Game.GameState.Normal && type!=Tile.ID.boat && type!=Tile.ID.MoveableBlock)move();
		}
		if(isSleeping) sleep.getWalkAnimation(dir).setImage();
		if(type==Tile.ID.boat && !Labyrinth.hero.isPushing){
			if(boat_movement)boatMovement();
			if(!boat_movement)
				boat_movement=true;
		}
		if(TransformedState!=0){
			if(isCollidingHero())
				reverseMove();
			else stepMove();
		}
		updateMask();
	}
	private boolean isCollidingHero() {

		return(shape.intersects(new Rectangle(Labyrinth.hero.x,Labyrinth.hero.y,width,height)));
	}
	/* Because its possible for the Monster to be shot while its still moving thus
	 * possibly becoming unaligned with the grid,we want the monster to finish its movement
	 * to the next grid even if it has been shot; this way the monster will stay aligned.
	 */
	private void reverseMove() {
		if(step_to_move>0){	
			switch(dir){
			case Left:	step_to_move=x%movement;
						x+=step;
						break;
			case Right:	step_to_move=x%movement;
						x-=step;
						break;
			case Up:	step_to_move=y%movement;
						y+=step;
						break;
			case Down:	step_to_move=y%movement;
						y-=step;
						break;
			default:
				break;
			}
			step_to_move-=step;
		}	
	}
	/* Since we want monster to always be aligned with grid, they can only move 
	 * a certain distance at once but we dont want to move all at once therefore we
	 * have to move slightly every game update until we reach the next grid location.
	 */
	private void stepMove() {
		if(step_to_move>0){	
			switch(dir){
			case Left:	x-=step;
						break;
			case Right:	x+=step;
						break;
			case Up:	y-=step;
						break;
			case Down:	y+=step;
						break;
			default:
				break;
			}
			step_to_move-=step;
		}
	}
	private void Kill_Respawn() {
		Leeper me=copy();
		Level.addRespawn(me);
		Level.toRemove.add(this);
	}
	public Leeper copy(){
		Leeper clone=new Leeper(oldX,oldY,oldtype);
		return clone;
	}
	private boolean isOffScreen(){
		if(x>Level.map_width || x<0 || y<0 || y>Level.map_height){
			Kill_Respawn();
			return true;
		}
		return false;
	}
	/*This is the movement logic for the Monster.The monster will try and find the shortest path via
	 * the A* algorithmn and then decide where it needs to move.It is possible that there are no path
	 * to the Hero at any point in the game therefore the Monster should be moving differently in 
	 *this case.
	 */
	private void move() {
		last_update++;
		if(step_to_move==0  && x%movement==0 && y%movement==0 && 
				last_update>=(movement/step) && TransformedState==0 && !isSleeping){
					last_update=0;
					shortestPath();
					if(path_exist){
						if(Path.size()>1)//since current position was top stack;//
							Path.pop();
						if(!Path.isEmpty())nextMovement=Path.pop();
						step_to_move=movement;
						if(nextMovement.data.x==x){//prepare to move along Y axis
							if(nextMovement.data.y>y){//target is down
								dir=Game.Direction.Down;
							}
							else dir=Game.Direction.Up;
						}
						if(nextMovement.data.y==y){//prepare to move along X axis
							if(nextMovement.data.x>x){//target is on right
								dir=Game.Direction.Right;
							}
							else dir=Game.Direction.Left;
						}
						willSleep();
					}
			}
		stepMove();
		if(!path_exist){
			switch(dir){
			case Left:	if(!checkCollison(new Rectangle(x-step, y,step,half_height),new Rectangle( x-step,y+half_height,step,half_height)))
							x-=step;
						else getnewDirection();
						break;
			case Right:	if(!checkCollison(new Rectangle(x+width, y,step,half_height),new Rectangle( x+width,y+half_height,step,half_height)))
							x+=step;
						else getnewDirection();
						break;
			case Up:	if(!checkCollison(new Rectangle(x, y-step,half_width,step),new Rectangle( x+half_width,y-step,half_width,step)))
							y-=step;
						else getnewDirection();
						break;
			case Down:	if(!checkCollison(new Rectangle(x, y+height,half_width,step),new Rectangle( x+half_width,y+height,half_width,step)))
							y+=step;
						else getnewDirection();
						break;
			default:
				break;
			}
		}
	}
	/* This Monster will turn into sleeping mode as soon as they are in contact with the Hero.
	 */
	private void willSleep() {
		switch(dir){
		case Left:	if(new Rectangle(x-half_width,y-half_height,width,height+half_height).intersects
						(new Rectangle(Labyrinth.hero.x,Labyrinth.hero.y,width,height))){
						x+=step;
						Sound.resetSound();
						Sound.Sleeper.start();
						isSleeping=true;
					}
					break;
		case Right:	if(new Rectangle(x+half_width,y-half_height,width,height+half_height).intersects
						(new Rectangle(Labyrinth.hero.x,Labyrinth.hero.y,width,height))){
						x-=step;
						Sound.resetSound();
						Sound.Sleeper.start();
						isSleeping=true;
					}
					break;
		case Up:	if(new Rectangle(x-half_width,y-half_height,width+half_width,height).intersects
						(new Rectangle(Labyrinth.hero.x,Labyrinth.hero.y,width,height))){
						y+=step;
						Sound.resetSound();
						Sound.Sleeper.start();
						isSleeping=true;
					}	
					break;
		case Down:	if(new Rectangle(x-half_width,y+half_height,width+half_width,height).intersects
					(new Rectangle(Labyrinth.hero.x,Labyrinth.hero.y,width,height))){
						y-=step;
						Sound.resetSound();
						Sound.Sleeper.start();
						isSleeping=true;
					}					
					break;
		default:
			break;
			
		}
	}
	/*This is the A* Algorithmn part that find the shortest path from the monster to the Hero while
	 * also taking collision into consideration and then the Monster will follow this given path.
	 */
	private void shortestPath(){
		Node goal=new Node(getHeroPosition());
		Open=new ArrayList<Node>();
		Closed=new ArrayList<Node>();
		Path=new Stack<Node>();
		path_exist=false;
		Node root=new Node(x,y);
		Node neighbor;
		Open.add(root);	
		while(!Open.isEmpty()){
			Collections.sort(Open);
			Node current=Open.get(0);
			if(Closed.contains(goal)){
				path_exist=true;
				break;
			}
			Open.remove(current);
			Closed.add(current);
				if(!checkCollison(new Rectangle(current.data.x-movement,current.data.y,half_width,half_height),
						new Rectangle(current.data.x-movement,current.data.y+half_height,half_width,half_height))){//left
						neighbor=new Node(current.data.x-movement,current.data.y);
						addNeighbor(neighbor,current);
				}				
				if(!checkCollison(new Rectangle(current.data.x,current.data.y+height,half_width,half_height),
						new Rectangle(current.data.x+movement,current.data.y+height,half_width,half_height))){//down
						neighbor=new Node(current.data.x,current.data.y+movement);
						addNeighbor(neighbor,current);
				}
				if(!checkCollison(new Rectangle(current.data.x,current.data.y-movement,half_width,half_height),
						new Rectangle(current.data.x+movement,current.data.y-movement,half_width,half_height))){//up
						neighbor=new Node(current.data.x,current.data.y-movement);
						addNeighbor(neighbor,current);
				}
				if(!checkCollison(new Rectangle(current.data.x+width,current.data.y,half_width,half_height),
						new Rectangle(current.data.x+width,current.data.y+movement,half_width,half_height))){//right
						neighbor=new Node(current.data.x+movement,current.data.y);
						addNeighbor(neighbor,current);
				}
			}
			buildPath();
		}
	private Point getHeroPosition() {
		//get hero position including future position if he is moving
		Point position=null;
		Character hero=Labyrinth.hero;
		switch(hero.dir){
		case Right:	position=new Point(hero.x+hero.targetX,hero.y);
					break;
		case Left:	position=new Point(hero.x+hero.targetX,hero.y);
					break;
		case Up:	position=new Point(hero.x,hero.y+hero.targetX);
					break;
		case Down:	position=new Point(hero.x,hero.y+hero.targetX);
					break;
		default:	break;
		}
		if(position.x%movement!=0 || position.y%movement!=0)System.out.println("ERROR PATH WONT BE AlIGNED");
		return position;
	}
	public boolean checkCollison(Rectangle mask1,Rectangle mask2) {
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i).shape.intersects(mask1)|| Level.map_tile.get(i).shape.intersects(mask2)){
				if(Level.map_tile.get(i).isSolid)
					return true;
			}
		}
		return false;
	}
}
