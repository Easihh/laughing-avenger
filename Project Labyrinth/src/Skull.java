import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Stack;

import javax.imageio.ImageIO;

public class Skull extends Monster{
	private BufferedImage[] skull_img;
	private final long nano=1000000L;
	private long time_since_transform;
	private int step_to_move;
	private boolean path_exist=false;
	Stack<Node> Path;
	Animation Skull;
	Node nextMovement;
	public Skull(int x,int y, int type) {
		super(x, y, type);
		Path=new Stack<Node>();
		dir=Game.Direction.Left;
		isActive=false;
		Skull=new Animation();
		getImage();
	}
	public void getImage(){
		BufferedImage img=null;
		skull_img=new BufferedImage[2];
		try {img=ImageIO.read(getClass().getResource("/tileset/skull.png"));
			} catch (IOException e) {e.printStackTrace();}
		for(int i=0;i<1;i++){//all animation on same row
			 for(int j=0;j<2;j++){
				skull_img[(i*2)+j]=img.getSubimage(j*width, i*height, width, height);
			 }
		 }
		Skull.AddScene(skull_img[0], 500);
		Skull.AddScene(skull_img[1], 500);
	}
	public void update(){
		if(isActive)
			move();
	}
	public void render(Graphics g){
		checkState();
		if(TransformedState==0 && !isMovingAcrossScreen){
			if(isActive){
				Skull.setImage();
				g.drawImage(Skull.getImage(), x,y,width,height,null);
			}
			else g.drawImage(Level.game_tileset[35],x,y,width,height,null);
		}
		else{	if(isMovingAcrossScreen)
					super.updateLocation();
				if(!isOffScreen())
					g.drawImage(img,x,y,width,height,null);
		}
	}
	@Override
	public void transform() {
		previousState=img;
		type=2;
		img=Level.monsterState[0];
		time_since_transform=System.nanoTime();	
		TransformedState=1;		
	}
	public boolean isOffScreen(){
		if(x>Level.map_width || x<0 || y<0 || y>Level.map_height){
			Skull aTile=this;
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

