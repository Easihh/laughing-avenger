import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;
import javax.imageio.ImageIO;

public class Alma extends Monster{
	private Movement move;
	private Animation roll;
	private boolean path_exist=false;
	private int step_to_move;
	private Node nextMovement;
	private int last_update=0;
	public Alma(int x, int y, int type) {
		super(x, y, type);
		depth=2;
		dir=Game.Direction.Down;
		move=new Movement("alma_movement", 150);
		roll=new Animation();
		setupImage();
	}

	private void setupImage() {
		BufferedImage img=null;
		BufferedImage[] images=new BufferedImage[3];
		try {
			//roll
			img=ImageIO.read(getClass().getResourceAsStream("/tileset/alma_roll.png"));
			for(int i=0;i<1;i++){//all animation on same row
				for(int j=0;j<2;j++){
					images[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
				}
			 }
			roll.AddScene(images[0], 150);
			roll.AddScene(images[1], 150);
			} catch (IOException e) {e.printStackTrace();}
	}

	@Override
	public void transform() {
		previousState=img;
		type=Tile.ID.MoveableBlock.value;
		img=Game.monsterState.get(0);
		time_since_transform=System.nanoTime();	
		TransformedState=1;	
	}
	public void update(){
		getAnimation().setImage();
		if(Labyrinth.GameState==Game.GameState.Normal && type!=Tile.ID.boat.value && type!=Tile.ID.MoveableBlock.value)move();
		if(type==Tile.ID.boat.value && !Character.isPushing){
			if(boat_movement)boatMovement();
			if(!boat_movement)
				boat_movement=true;
		}
		if(TransformedState!=0)
			stepMove();
		updateMask();
	}
	private void stepMove() {
		if(step_to_move>0){	
			switch(dir){
			case Left:	x-=2;
						break;
			case Right:	x+=2;
						break;
			case Up:	y-=2;
						break;
			case Down:	y+=2;
						break;
			}
			step_to_move-=2;
		}
	}
	@Override
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
		if((System.nanoTime()-time_since_water)/nano>8000 && TransformedState==4 && isDrowning){
			Kill_Respawn();
		}	
	}

	private Animation getAnimation() {
		switch(dir){
		case Left: 	if(!Object_inBetween("Left"))
						return roll;
					return move.walk_left;
		case Right: if(!Object_inBetween("Right"))
						return roll;
					return move.walk_right;
		case Up: 	return move.walk_up;
		case Down: 	return move.walk_down;
		}
		return null;
	}
	private void Kill_Respawn() {
		Alma me=copy();
		Level.addRespawn(me);
		Level.toRemove.add(this);
	}
	public Alma copy(){
		Alma clone=new Alma(oldX,oldY,oldtype);
		return clone;
	}
	private boolean isOffScreen(){
		if(x>Level.map_width || x<0 || y<0 || y>Level.map_height){
			Kill_Respawn();
			return true;
		}
		return false;
	}
	private void move() {
		last_update++;
		if(step_to_move==0 && Character.getInstance().getX()%16==0 && Character.getInstance().getY()%16==0 && 
				x%16==0 && y%16==0 && last_update>=8 && TransformedState==0){
					last_update=0;
					shortestPath();
					if(path_exist){
						if(Path.size()>1)//since current position was top stack;//
							Path.pop();
						if(!Path.isEmpty())nextMovement=Path.pop();
						step_to_move=16;
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
		if(step_to_move>0){	
			switch(dir){
			case Left:	x-=2;
						break;
			case Right:	x+=2;
						break;
			case Up:	y-=2;
						break;
			case Down:	y+=2;
						break;
			}
			step_to_move-=2;
			}
		if(!path_exist){
			switch(dir){
			case Left:	if(!checkCollison(new Rectangle(x-2, y,2,16),new Rectangle( x-2,y+16,2,16)))
							x-=2;
						else getnewDirection();
						break;
			case Right:	if(!checkCollison(new Rectangle(x+32, y,2,16),new Rectangle( x+32,y+16,2,16)))
							x+=2;
						else getnewDirection();
						break;
			case Up:	if(!checkCollison(new Rectangle(x, y-2,16,2),new Rectangle( x+16,y-2,16,2)))
							y-=2;
						else getnewDirection();
						break;
			case Down:	if(!checkCollison(new Rectangle(x, y+32,16,2),new Rectangle( x+16,y+32,16,2)))
							y+=2;
						else getnewDirection();
						break;
			}
		}
		updateMask();
	}
	private void shortestPath(){
		Node goal=new Node(Character.getInstance().getX(),Character.getInstance().getY());
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
				if(!checkCollison(new Rectangle(current.data.x-16,current.data.y,16,16),
						new Rectangle(current.data.x-16,current.data.y+16,16,16))){//left
						neighbor=new Node(current.data.x-16,current.data.y);
						addNeighbor(neighbor,current);
				}				
				if(!checkCollison(new Rectangle(current.data.x,current.data.y+32,16,16),
						new Rectangle(current.data.x+16,current.data.y+32,16,16))){//down
						neighbor=new Node(current.data.x,current.data.y+16);
						addNeighbor(neighbor,current);
				}
				if(!checkCollison(new Rectangle(current.data.x,current.data.y-16,16,16),
						new Rectangle(current.data.x+16,current.data.y-16,16,16))){//up
						neighbor=new Node(current.data.x,current.data.y-16);
						addNeighbor(neighbor,current);
				}
				if(!checkCollison(new Rectangle(current.data.x+32,current.data.y,16,16),
						new Rectangle(current.data.x+32,current.data.y+16,16,16))){//right
						neighbor=new Node(current.data.x+16,current.data.y);
						addNeighbor(neighbor,current);
				}
			}
			buildPath();
		}
	public boolean checkCollison(Rectangle mask1,Rectangle mask2) {
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i).shape.intersects(mask1)|| Level.map_tile.get(i).shape.intersects(mask2)){
				if(Level.map_tile.get(i).isSolid)return true;
				if(Level.map_tile.get(i).type==Tile.ID.Grass.value)// cant  walk on grass
					return true;				
			}
		}
		return false;
	}
}
