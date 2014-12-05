import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import javax.imageio.ImageIO;

/** 
 * Class that represent the Monster Alma which is first shown on Level 8.This monster ability is
 * that it will follow the Hero until it touch him making the hero die.This monster will also
 * switch to a roll form when moving on the right or left if there are no object collision.
 * Unlike other monsters who follow the Hero to his death, this Monster cannot go on Grass tile.
 */
public class Alma extends Monster{
	private Animation roll;
	private boolean path_exist=false;
	private int last_update=0;
	private final int step=2,movement=16,roll_animation_duration=150;
	private int step_to_move;
	private Movement move;
	private Node nextMovement;
	
	public Alma(int x, int y, ID type) {
		super(x, y, type);
		depth=2;
		dir=Game.Direction.Down;
		move=new Movement("alma_movement", 150);
		roll=new Animation();
		setupImage();
	}

	private void setupImage() {
		int rows=1;
		int cols=2;
		BufferedImage img=null;
		BufferedImage[] images=new BufferedImage[cols*rows];
		try {img=ImageIO.read(getClass().getResourceAsStream("/tileset/alma_roll.png"));
			for(int i=0;i<rows;i++){
				for(int j=0;j<cols;j++){
					images[(i*cols)+j]=img.getSubimage(j*width, i*height, width, height);
				}
			 }
			roll.AddScene(images[0], roll_animation_duration);
			roll.AddScene(images[1], roll_animation_duration);
			} catch (IOException e) {e.printStackTrace();}
	}
	public void transform() {
		previousState=img;
		type=Tile.ID.MoveableBlock;
		img=Game.monsterState.get(0);
		time_since_transform=System.nanoTime();	
		TransformedState=1;
	}
	/**
	 *Main Method of the Alma Class that update its state and its animation as well as collision
	 *check*/
	public void update(){
		getAnimation().setImage();
		if(Labyrinth.GameState==Game.GameState.Normal && type!=Tile.ID.boat && type!=Tile.ID.MoveableBlock)move();
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
	/* Because its possible for the Monster to be shot while it's still moving thus
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
	/* Decides which animation is played based on the Direction and 
	 * whether there are objects in its path.
	 */
	private Animation getAnimation(){
		switch(dir){
		case Left: 	if(!Object_inBetween("Left"))
						return roll;
					return move.walk_left;
		case Right: if(!Object_inBetween("Right"))
						return roll;
					return move.walk_right;
		case Up: 	return move.walk_up;
		case Down: 	return move.walk_down;
		default:
			break;
		}
		return null;
	}
	/*Since we want monster to always be aligned with grid, they can only move 
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
	public void render(Graphics g) {
		checkState();
		checkIfDrown();
			if(TransformedState==0 && !isMovingAcrossScreen)
				g.drawImage(getAnimation().getImage(), x, y, null);
			else{
				if(isMovingAcrossScreen)
					super.updateLocation();
				if(!isOffScreen())
					g.drawImage(img,x,y,width,height,null); 
				}	
		}
	
	private void checkIfDrown() {
		if((System.nanoTime()-time_since_water)/nano>duration_in_water && TransformedState==4 && isDrowning){
			Kill_Respawn();
		}	
	}
	private void Kill_Respawn() {
		Alma me=copy();
		Level.addRespawn(me);
		Level.toRemove.add(this);
	}
	/**
	 * Returns a copy of the current Alma object.
	 */
	public Alma copy(){
		Alma clone=new Alma(oldX,oldY,oldtype);
		return clone;
	}
	/* Return whether the Character has gone outside the screen boundaries*/
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
		if(step_to_move==0 &&
				x%movement==0 && y%movement==0 && last_update>=(movement/step) && TransformedState==0){
					last_update=0;
					shortestPath();
					if(path_exist){
						if(Path.size()>1)
							Path.pop();//since current position was top stack;//
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
	/*This is the A* Algorithmn part that find the shortest path(the least heuristic value from Monster to Player) from the 
	 * monster to the Hero while also taking collision into consideration and then the Monster will follow this given path.
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
		default:
			break;
		}
		if(position.x%16!=0 || position.y%16!=0)System.out.println("ERROR PATH WONT BE AlIGNED");
		return position;
	}
	/**
	 * Collision method for this Monster.The Monster should not traverse any solid tile or Grass tile.
	 * Returns whether there exist a collision at next position based on current direction.
	 */
	public boolean checkCollison(Rectangle mask1,Rectangle mask2) {
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i).shape.intersects(mask1)|| Level.map_tile.get(i).shape.intersects(mask2)){
				if(Level.map_tile.get(i).isSolid)return true;
				if(Level.map_tile.get(i).type==Tile.ID.Grass)//this Monster cant  walk on grass
					return true;				
			}
		}
		return false;
	}
}
