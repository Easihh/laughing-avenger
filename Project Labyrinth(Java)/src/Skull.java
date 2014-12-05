import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import javax.imageio.ImageIO;

public class Skull extends Monster{
	private final long nano=1000000L;
	private Animation Skull;
	private boolean path_exist=false;
	private BufferedImage[] skull_img;
	private final int step=2,movement=16;
	private int step_to_move,update_counter;
	private Node nextMovement;
	public Skull(int x,int y, ID type) {
		super(x, y, type);
		depth=2;
		dir=Game.Direction.Left;
		isActive=false;//Skull is asleep until all hearts are taken
		Skull=new Animation();
		getImage();		
	}
	public void render(Graphics g){
		if(TransformedState==0 && !isMovingAcrossScreen){
			if(isActive){
				Skull.setImage();
				g.drawImage(Skull.getImage(), x,y,width,height,null);
			}
			else g.drawImage(Game.game_tileset.get(Tile.ID.Skull.value),x,y,width,height,null);
		}
		else{	if(isMovingAcrossScreen)
					super.updateLocation();
				if(!isOffScreen())
					g.drawImage(img,x,y,width,height,null);
		}
	}
	private void checkifdrown() {
		if((System.nanoTime()-time_since_water)/nano>duration_in_water && TransformedState==4 && isDrowning){
			Kill_Respawn();
		}
	}
	@Override
	public void transform() {
		previousState=img;
		type=Tile.ID.MoveableBlock;
		img=Game.monsterState.get(0);
		time_since_transform=System.nanoTime();	
		TransformedState=1;	
	}
	/**
	 *Main Method of the Skull Class that update its state and its animation as well as collision
	 *check*/
	public void update(){
		checkState();
		checkifdrown();
		if(isActive && Labyrinth.GameState==Game.GameState.Normal && type!=Tile.ID.boat && type!=Tile.ID.MoveableBlock)
			move();
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
	private boolean isCollidingHero() {

		return(shape.intersects(new Rectangle(Labyrinth.hero.x,Labyrinth.hero.y,width,height)));
	}
	private void Kill_Respawn() {
		Skull me=copy();
		Level.addRespawn(me);
		Level.toRemove.add(this);
	}
	private Skull copy(){
		Skull clone=new Skull(oldX,oldY,oldtype);
		return clone;
	}
	private void getImage(){
		int row=1;
		int col=2;
		BufferedImage img=null;
		skull_img=new BufferedImage[2];
		try {img=ImageIO.read(getClass().getResourceAsStream("/tileset/skull.png"));
			} catch (IOException e) {e.printStackTrace();}
		for(int i=0;i<row;i++){
			 for(int j=0;j<col;j++){
				skull_img[(i*col)+j]=img.getSubimage(j*width, i*height, width, height);
			 }
		 }
		Skull.AddScene(skull_img[0], 500);
		Skull.AddScene(skull_img[1], 500);
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
		update_counter++;
		if(step_to_move==0 && x%movement==0 && y%movement==0 && update_counter>=(movement/step) && TransformedState==0){
			update_counter=0;
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
		default:
			break;
		}
		if(position.x%16!=0 || position.y%16!=0)System.out.println("ERROR PATH WONT BE AlIGNED");
		return position;
	}
	private boolean checkCollison(Rectangle mask1,Rectangle mask2) {
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i).shape.intersects(mask1)|| Level.map_tile.get(i).shape.intersects(mask2)){
				if(Level.map_tile.get(i).isSolid)return true;
			}
		}
		return false;
	}
}

