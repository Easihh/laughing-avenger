import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import javax.imageio.ImageIO;


public class Leeper extends Monster{
	public boolean isSleeping;
	long time_since_transform;
	long nano=1000000L;
	private int step_to_move;
	private boolean path_exist=false;
	Stack<Node> Path;
	BufferedImage[] spriteSheet;
	Animation walk_left;
	Animation walk_right;
	Animation walk_up;
	Animation walk_down;
	Animation sleep_left;
	Animation sleep_right;
	Animation sleep_up;
	Animation sleep_down;
	Node nextMovement;
	public Leeper(int x, int y, int type) {
		super(x, y, type);
		isSleeping=false;
		dir=Game.Direction.Left;
		Path=new Stack<Node>();
		walk_left=new Animation();
		walk_right=new Animation();
		walk_up=new Animation();
		walk_down=new Animation();
		sleep_right=new Animation();
		sleep_left=new Animation();
		sleep_up=new Animation();
		sleep_down=new Animation();
		getImage();
	}
	public void getImage(){
		BufferedImage img=null;
		spriteSheet = new BufferedImage[2];
		try {//left movement
			img=ImageIO.read(getClass().getResource("/tileset/leeper_left.png"));
			for(int i=0;i<1;i++){//all animation on same row
				 for(int j=0;j<2;j++){
					spriteSheet[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
				 }
			 }
			walk_left.AddScene(spriteSheet[0], 150);
			walk_left.AddScene(spriteSheet[1], 150);
			//left Sleep
			img=ImageIO.read(getClass().getResource("/tileset/leeper_sleep_left.png"));
			for(int i=0;i<1;i++){//all animation on same row
				 for(int j=0;j<2;j++){
					spriteSheet[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
				 }
			 }
			sleep_left.AddScene(spriteSheet[0], 500);
			sleep_left.AddScene(spriteSheet[1], 500);
			///Right movement
			img=ImageIO.read(getClass().getResource("/tileset/leeper_right.png"));
			for(int i=0;i<1;i++){//all animation on same row
				 for(int j=0;j<2;j++){
					spriteSheet[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
				 }
			 }
			walk_right.AddScene(spriteSheet[0], 150);
			walk_right.AddScene(spriteSheet[1], 150);
			//right Sleep
			img=ImageIO.read(getClass().getResource("/tileset/leeper_sleep_right.png"));
			for(int i=0;i<1;i++){//all animation on same row
				 for(int j=0;j<2;j++){
					spriteSheet[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
				 }
			 }
			sleep_right.AddScene(spriteSheet[0], 500);
			sleep_right.AddScene(spriteSheet[1], 500);
			//Up movement
			img=ImageIO.read(getClass().getResource("/tileset/leeper_up.png"));
			for(int i=0;i<1;i++){//all animation on same row
				 for(int j=0;j<2;j++){
					spriteSheet[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
				 }
			 }
			walk_up.AddScene(spriteSheet[0], 150);
			walk_up.AddScene(spriteSheet[1], 150);
			sleep_up.AddScene(spriteSheet[0], 500);
			sleep_up.AddScene(spriteSheet[1], 500);
			//Down movement
			img=ImageIO.read(getClass().getResource("/tileset/leeper_down.png"));
			for(int i=0;i<1;i++){//all animation on same row
				 for(int j=0;j<2;j++){
					spriteSheet[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
				 }
			 }
			walk_down.AddScene(spriteSheet[0], 150);
			walk_down.AddScene(spriteSheet[1], 150);
			//Down sleep
			img=ImageIO.read(getClass().getResource("/tileset/leeper_sleep_down.png"));
			for(int i=0;i<1;i++){//all animation on same row
				 for(int j=0;j<2;j++){
					spriteSheet[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
				 }
			 }
			sleep_down.AddScene(spriteSheet[0], 500);
			sleep_down.AddScene(spriteSheet[1], 500);
			} catch (IOException e) {e.printStackTrace();}
	}
	@Override
	public void transform() {
		if(!isSleeping){
		previousState=img;
		type=2;
		img=Level.monsterState[0];
		time_since_transform=System.nanoTime();	
		TransformedState=1;
		}
	}
	@Override
	public void render(Graphics g) {
		checkState();
		if(!isSleeping){
			if(TransformedState==0 && !isMovingAcrossScreen)
				g.drawImage(getAnimation().getImage(), x, y, null);
			else{
				if(isMovingAcrossScreen)
					super.updateLocation();
				if(!isOffScreen())
					g.drawImage(img,x,y,width,height,null); 
				}	
		}
		else //g.drawPolygon(shape); 
			g.drawImage(getSleepAnimation().getImage(),x,y,null);
	}
	public void update(){
		if(!isSleeping){
			getAnimation().setImage();
			if(TransformedState==0)move();
		}
		else getSleepAnimation().setImage();
	}
	private Animation getSleepAnimation() {
		switch(dir){
		case Left: return sleep_left;
		case Right: return sleep_right;
		case Up: return sleep_up;
		case Down: return sleep_down;
		}
		return null;
	}
	private Animation getAnimation() {
		switch(dir){
		case Left: return walk_left;
		case Right: return walk_right;
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
			type=1;
			img=previousState;
		}	
	}
	public boolean isOffScreen(){
		if(x>Level.map_width || x<0 || y<0 || y>Level.map_height){
			Leeper aTile=this;
			aTile.x=oldX;
			aTile.y=oldY;
			aTile.type=oldtype;
			aTile.img=previousState;	
			aTile.isMovingAcrossScreen=false;
			aTile.isActive=isActive;
			aTile.TransformedState=0;
			Level.addRespawn(aTile);
			aTile.updateMask();
			Level.toRemove.add(aTile);//remove the tile if it goes offscreen
			return true;
		}
		return false;
	}
	public void move() {
		if(step_to_move==0 && Character.x%16==0 && Character.y%16==0){
					getShortestPath();
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
				case Left:	if(shape.intersects(Character.x+2, Character.y, 32, 32)){
								System.out.println(step_to_move);
								Sound.Sleeper.setFramePosition(0);
								Sound.Sleeper.start();
								isSleeping=true;
								if(step_to_move!=16)
									x+=step_to_move;
							}else
							x-=2;
							break;
				case Right:	if(shape.intersects(Character.x-2, Character.y, 32, 32)){
					System.out.println(step_to_move);
								Sound.Sleeper.setFramePosition(0);
								Sound.Sleeper.start();
								isSleeping=true;
								if(step_to_move!=16)
									x-=step_to_move;
							}else
							x+=2;
							break;
				case Up:	if(shape.intersects(Character.x, Character.y+2, 32, 32)){
								System.out.println(step_to_move);
								Sound.Sleeper.setFramePosition(0);
								Sound.Sleeper.start();
								isSleeping=true;
							}else
							y-=2;
							break;
				case Down:	if(shape.intersects(Character.x, Character.y-2, 32, 32)){
								System.out.println("target:"+Character.targetX);
								System.out.println(step_to_move);
								Sound.Sleeper.setFramePosition(0);
								Sound.Sleeper.start();
								isSleeping=true;
							}else
							y+=2;
							break;
				}
				step_to_move-=2;
			}
		updateMask();
	}
	public void getShortestPath(){
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
				if(!checkCollison(current.data.x-1,current.data.y,current.data.x-1,current.data.y+32-1)){//left
					neighbor=new Node(current.data.x-16,current.data.y);
					if(!Closed.contains(neighbor))
						if(!Open.contains(neighbor) || (current.Gscore+1<neighbor.Gscore)){
							neighbor.parent=current;
							neighbor.Gscore=current.Gscore+1;
							neighbor.updateScore(x, y);
							if(!Open.contains(neighbor))
									Open.add(neighbor);
								}
							}				
					if(!checkCollison(current.data.x+32-1,current.data.y+32,current.data.x,current.data.y+32)){//down
						neighbor=new Node(current.data.x,current.data.y+16);
						if(!Closed.contains(neighbor))
							if(!Open.contains(neighbor) || (current.Gscore+1<neighbor.Gscore)){
								neighbor.parent=current;
								neighbor.Gscore=current.Gscore+1;
								neighbor.updateScore(x, y);
								if(!Open.contains(neighbor))
									Open.add(neighbor);
							}
					}
					if(!checkCollison(current.data.x+32-1,current.data.y-1,current.data.x,current.data.y-1)){//up
						neighbor=new Node(current.data.x,current.data.y-16);
						if(!Closed.contains(neighbor))
							if(!Open.contains(neighbor) || (current.Gscore+1<neighbor.Gscore)){
								neighbor.parent=current;
								neighbor.Gscore=current.Gscore+1;
								neighbor.updateScore(x, y);
								if(!Open.contains(neighbor))
									Open.add(neighbor);
							}
					}
					if(!checkCollison(current.data.x+32,current.data.y+32-1,current.data.x+32,current.data.y)){//right
						neighbor=new Node(current.data.x+16,current.data.y);
						if(!Closed.contains(neighbor))
							if(!Open.contains(neighbor) || (current.Gscore+1<neighbor.Gscore)){
								neighbor.parent=current;
								neighbor.Gscore=current.Gscore+1;
								neighbor.updateScore(x, y);
								if(!Open.contains(neighbor))
									Open.add(neighbor);
							}
					}
			}
			Node test=Closed.get(Closed.size()-1);
			while(test!=null){
				Path.add(test);
				test=test.parent;
			}
		}
}
