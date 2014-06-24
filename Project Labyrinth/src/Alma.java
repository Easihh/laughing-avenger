import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;
import javax.imageio.ImageIO;

public class Alma extends Monster{
	private final long nano=1000000L;
	private long time_since_transform;
	private Animation walk_down;
	private Animation walk_up;
	private Animation walk_left;
	private Animation walk_right;
	private Animation roll;
	private boolean path_exist=false;
	private int step_to_move;
	private Node nextMovement;
	private Stack<Node> Path;
	private int last_update=0;
	public Alma(int x, int y, int type) {
		super(x, y, type);
		depth=2;
		dir=Game.Direction.Down;
		walk_down=new Animation();
		walk_up=new Animation();
		walk_left=new Animation();
		walk_right=new Animation();
		roll=new Animation();
		setupImage();
	}

	private void setupImage() {
		BufferedImage img=null;
		BufferedImage[] images=new BufferedImage[3];
		try {
			img=ImageIO.read(getClass().getResource("/tileset/alma_down.png"));
			//walk down
			for(int i=0;i<1;i++){//all animation on same row
				 for(int j=0;j<3;j++){
					images[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
				 }
			}
			walk_down.AddScene(images[0], 150);
			walk_down.AddScene(images[1], 150);
			walk_down.AddScene(images[2], 150);
			//walk up
			img=ImageIO.read(getClass().getResource("/tileset/alma_up.png"));
			for(int i=0;i<1;i++){//all animation on same row
				for(int j=0;j<3;j++){
					images[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
				}
			 }
			walk_up.AddScene(images[0], 150);
			walk_up.AddScene(images[1], 150);
			walk_up.AddScene(images[2], 150);
			//walk left
			img=ImageIO.read(getClass().getResource("/tileset/alma_left.png"));
			for(int i=0;i<1;i++){//all animation on same row
				for(int j=0;j<3;j++){
					images[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
				}
			 }
			walk_left.AddScene(images[0], 150);
			walk_left.AddScene(images[1], 150);
			walk_left.AddScene(images[2], 150);	
			//walk right
			img=ImageIO.read(getClass().getResource("/tileset/alma_right.png"));
			for(int i=0;i<1;i++){//all animation on same row
				for(int j=0;j<3;j++){
					images[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
				}
			 }
			walk_right.AddScene(images[0], 150);
			walk_right.AddScene(images[1], 150);
			walk_right.AddScene(images[2], 150);
			//roll
			img=ImageIO.read(getClass().getResource("/tileset/alma_roll.png"));
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
		type=2;
		img=Level.monsterState[0];
		time_since_transform=System.nanoTime();	
		TransformedState=1;	
	}
	public void update(){
		getAnimation().setImage();
		if(TransformedState==0 && Labyrinth.GameState==Game.GameState.Normal)move();
	}
	@Override
	public void render(Graphics g) {
		checkState();
			if(TransformedState==0 && !isMovingAcrossScreen)
				g.drawImage(getAnimation().getImage(), x, y, null);
			else{
				if(isMovingAcrossScreen)
					super.updateLocation();
				if(!isOffScreen())
					g.drawImage(img,x,y,width,height,null); 
				}	
		}
	private Animation getAnimation() {
		switch(dir){
		case Left: if(!Object_inBetween("Left"))
						return roll;
					return walk_left;
		case Right: if(!Object_inBetween("Right"))
						return roll;
					return walk_right;
		case Up: return walk_up;
		case Down: return walk_down;
		}
		return null;
	}
	private void checkState() {
		if((System.nanoTime()-time_since_transform)/nano>7000 && TransformedState==1){
			TransformedState=2;
			img=Level.monsterState[1];
		}
		if((System.nanoTime()-time_since_transform)/nano>10000 && TransformedState==2){
			TransformedState=0;
			type=0;
			img=previousState;
		}	
	}
	private boolean isOffScreen(){
		if(x>Level.map_width || x<0 || y<0 || y>Level.map_height){
			Alma aTile=this;
			aTile.x=oldX;
			aTile.y=oldY;
			aTile.type=oldtype;
			aTile.img=previousState;	
			aTile.isMovingAcrossScreen=false;
			aTile.TransformedState=0;
			aTile.path_exist=false;
			aTile.last_update=0;
			aTile.step_to_move=0;
			aTile.dir=Game.Direction.Left;
			Level.addRespawn(aTile);
			aTile.updateMask();
			Level.toRemove.add(aTile);//remove the tile if it goes offscreen
			return true;
		}
		return false;
	}
	private void move() {
		last_update++;
		if(step_to_move==0 && Character.x%16==0 && Character.y%16==0 && x%16==0 && y%16==0 && last_update>=8){
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
		Node goal=new Node(Character.x,Character.y);
		ArrayList<Node> Open=new ArrayList<Node>();
		ArrayList<Node> Closed=new ArrayList<Node>();
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
					if(!checkCollison(new Rectangle(current.data.x,current.data.y+32,16,16),
							new Rectangle(current.data.x+16,current.data.y+32,16,16))){//down
						neighbor=new Node(current.data.x,current.data.y+16);
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
					if(!checkCollison(new Rectangle(current.data.x,current.data.y-16,16,16),
							new Rectangle(current.data.x+16,current.data.y-16,16,16))){//up
						neighbor=new Node(current.data.x,current.data.y-16);
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
					if(!checkCollison(new Rectangle(current.data.x+32,current.data.y,16,16),
							new Rectangle(current.data.x+32,current.data.y+16,16,16))){//right
						neighbor=new Node(current.data.x+16,current.data.y);
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
			}
			Node test=Closed.get(Closed.size()-1);
			while(test!=null){
				Path.add(test);
				test=test.parent;
			}
		}
	private void getnewDirection() {
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
	public boolean checkCollison(Rectangle mask1,Rectangle mask2) {
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i).shape.intersects(mask1)|| Level.map_tile.get(i).shape.intersects(mask2)){
				if(Level.map_tile.get(i).isSolid)return true;
				if(Level.map_tile.get(i).getType()==90)// cant walk on grass
					return true;
			}
		}
		return false;
	}
}