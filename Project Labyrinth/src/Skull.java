import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;


public class Skull extends Monster{
	private BufferedImage[] skull_img;
	private final long nano=1000000L;
	private long time_since_transform;
	private int step_to_move;
	Animation Skull;
	public Skull(int x,int y, int type) {
		super(x, y, type);
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
		if(step_to_move==0)
			shortestPath();
		else{
			switch(dir){
				case Left:  x-=2;
							break;
				case Right: x+=2;
							break;
				case Up: 	y-=2;			
							break;
				case Down: 	y+=2;
							break;
			}
			step_to_move-=2;
		}
		updateMask();
	}
	public void shortestPath(){
		int targetX=x-Character.x;
		int targetY=y-Character.y;
		if(Math.abs(targetX)> Math.abs(targetY)){//Character is closer on the X axis
			if(targetX>0){// Character is to the left of Skull
				if(!checkCollison(x-1, y, x-1, y+height-1)){
					step_to_move=16;
					dir=Game.Direction.Left;
				}
			}
			else if(targetX<0){//Character is to the right of Skull			
				if(!checkCollison(x+width, y+height-1, x+width, y)){
					step_to_move=16;
					dir=Game.Direction.Right;
				}
			}
		}
		else{
			if(targetY>0){// Character is  up from Skull
				if(!checkCollison(x+width-1, y-1, x, y-1)){
					dir=Game.Direction.Up;
					step_to_move=16;
				}
			}
			else if(targetY<0){//Character is down from Skull
				if(!checkCollison(x+width-1, y+height, x, y+height)){
					dir=Game.Direction.Down;
					step_to_move=16;
				}
			}
		}
	}
}

