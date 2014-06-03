import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;



public class Character {
	public static int x;
	public static int y;
	private static int ammo;
	public static boolean isShooting;
	public static boolean isPushing;
	private int width=24;
	private int height=24;
	private int rows=1;// character animation row
	private int cols=5;// character animation colum
	private final int movement=2;
	final static int step=12;
	static boolean isMoving;
	private BufferedImage img=null;
	private Animation walk_down=null;
	private Animation walk_up=null;
	private Animation walk_left=null;
	private Animation walk_right=null;
	static long last_animation_update=0;
	static public Game.button lastKey;
	static public Game.button lastPressed;
	static ArrayList<Game.button> keypressed=new ArrayList<Game.button>();
	static public Game.Direction dir;
	static Projectile weapon=null;
	static private BufferedImage[] projectile_img;
	static private Tile select_Tile;
	static int targetX=0;
	public Character(int x, int y){
		Character.x=x;
		Character.y=y;
		Character.ammo=0;
		isShooting=false;
		isMoving=false;
		isPushing=false;
		 dir=Game.Direction.Down;
		 lastKey=Game.button.None;
		 walk_down=new Animation(cols,100);
		 walk_up=new Animation(cols,100);
		 walk_left=new Animation(cols,100);
		 walk_right=new Animation(cols,100);
		 try {
			img=ImageIO.read(getClass().getResource("/tileset/Lolo_down.png"));
			getImagefromSpriteSheet(img,walk_down);
			img=ImageIO.read(getClass().getResource("/tileset/Lolo_up.png"));
			getImagefromSpriteSheet(img,walk_up);
			img=ImageIO.read(getClass().getResource("/tileset/Lolo_left.png"));
			getImagefromSpriteSheet(img,walk_left);
			img=ImageIO.read(getClass().getResource("/tileset/Lolo_right.png"));
			getImagefromSpriteSheet(img,walk_right);
			img=ImageIO.read(getClass().getResource("/tileset/shoot_sheet.png"));
			getProjectileSheet(img);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void getProjectileSheet(BufferedImage spriteSheet) {
		int sheet_row=1;
		int sheet_cols=2;
		projectile_img=new BufferedImage[sheet_row*sheet_cols];
		for(int i=0;i<sheet_row;i++){
			 for(int j=0;j<sheet_cols;j++){
				projectile_img[(i*sheet_cols)+j]=spriteSheet.getSubimage(j*width, i*height, width, height);
			 }
		}			
	}
	private void getImagefromSpriteSheet(BufferedImage spriteSheet,
			Animation Animation) {
		BufferedImage[] animation_array=new BufferedImage[cols]; 
		 for(int i=0;i<rows;i++){
			 for(int j=0;j<cols;j++){
				animation_array[(i*cols)+j]=spriteSheet.getSubimage(j*width, i*height, width, height);
			 }
		 }
		 Animation.animation=animation_array;
	}
	public void render(Graphics g){
		g.setColor(Color.red);
		switch(dir){
	
		case Down:	if(keypressed.size()==0)
						walk_down.index=0;
					g.drawImage(walk_down.animation[walk_down.index],x,y,width,height,null);
					break;
		case Up:	if(keypressed.size()==0)
						walk_up.index=0;
					g.drawImage(walk_up.animation[walk_up.index],x,y,width,height,null);
					break;
		case Right:	if(keypressed.size()==0)
						walk_right.index=0;
					g.drawImage(walk_right.animation[walk_right.index],x,y,width,height,null);
					break;
		case Left:	if(keypressed.size()==0)
						walk_left.index=0;
					g.drawImage(walk_left.animation[walk_left.index],x,y,width,height,null);
					break;
		default: 	g.drawRect(x, y, width, height);
					break;
		}
		if(Character.isShooting)
			weapon.render(g);		
	}
	public void update(){
		movement();
		if(Character.isShooting && weapon!=null)
			bullet_Collision();
	}
	private void bullet_Collision() {
		for(Tile aTile:Level.map_tile){
			if(weapon.dir==Game.Direction.Right){
				if(aTile.shape.contains(weapon.x+width,weapon.y) && aTile.shape.contains(weapon.x+width,weapon.y+height-1))
					Character.isShooting=false;
			}
			if(weapon.dir==Game.Direction.Left){
				if(aTile.shape.contains(weapon.x-movement,weapon.y) && aTile.shape.contains(weapon.x-movement,weapon.y+height-1))
					Character.isShooting=false;
			}
			if(weapon.dir==Game.Direction.Up){
				if(aTile.shape.contains(weapon.x+width-1,weapon.y-1) && aTile.shape.contains(weapon.x,weapon.y-1))
					Character.isShooting=false;
			}
			if(weapon.dir==Game.Direction.Down){
				if(aTile.shape.contains(weapon.x+width-1,weapon.y+height) && aTile.shape.contains(weapon.x,weapon.y+height))
					Character.isShooting=false;
			}
		}
		//if still no full collision check if bullet is colliding with two tile instead of 1
		if(Character.isShooting){
			for(Tile aTile:Level.map_tile){
				if(weapon.dir==Game.Direction.Right && aTile.shape.contains(weapon.x+width,weapon.y)){//check top part collision
					for(Tile theTile:Level.map_tile){
					 if(theTile.shape.contains(weapon.x+width,weapon.y+height-1))//check bottom part collision
						Character.isShooting=false;
				}
			}
				if(weapon.dir==Game.Direction.Left && aTile.shape.contains(weapon.x-movement,weapon.y)){//check top part collision
					for(Tile theTile:Level.map_tile){
					 if(theTile.shape.contains(weapon.x-movement,weapon.y+height-1))//check bottom part collision
						Character.isShooting=false;
				}
			}
				if(weapon.dir==Game.Direction.Up && aTile.shape.contains(weapon.x,weapon.y-1)){//check left part collision
					for(Tile theTile:Level.map_tile){
					 if(theTile.shape.contains(weapon.x+width-1,weapon.y-1))//check right part collision
						Character.isShooting=false;
				}
			}
				if(weapon.dir==Game.Direction.Down && aTile.shape.contains(weapon.x,weapon.y+height)){//check left part collision
					for(Tile theTile:Level.map_tile){
					 if(theTile.shape.contains(weapon.x+width-1,weapon.y+height))//check right part collision
						Character.isShooting=false;
				}
			}
		}
	}
}
	private boolean checkCollision(Point pt1,Point pt2) {
		if(Level.goal.shape.contains(pt1)|| Level.goal.shape.contains(pt2))
			if(Level.goal.getType()==5)
				Level.takeGoal();
		for(Tile aTile:Level.map_tile){
			if(aTile.shape.contains(pt1) || aTile.shape.contains(pt2)){
				if(aTile.isWalkable()){
					if(Character.y<2*step)
						Level.nextLevel(); //end door
					return false;
				}
				//we have a collision
				switch(aTile.getType()){
				
				case 2: if(aTile.shape.contains(pt1) && aTile.shape.contains(pt2)){
							select_Tile=aTile;
							aTile.moveTile(step);
						}
						break;
				case 3: takeHeart(aTile);
						break;
				}
				return true;
			}
		}
		return false;
	}
	private void takeHeart(Tile aTile) {
		Level.map_tile.remove(aTile);
		Level.heart_amount-=1;
		if(Level.heart_amount==0)
			Level.openChest();
		Character.ammo++;
	}
	private void movement() {
		checkMovementState();
		if(!keypressed.isEmpty()&& !isMoving){//if a button direction is held down
			if(keypressed.size()==1)
				lastKey=keypressed.get(0);//get last pressed button
			else
				lastKey=keypressed.get(keypressed.size()-1);//get last pressed button
			if(lastKey==Game.button.W){
				dir=Game.Direction.Up;
				walk_up.increaseIndex(last_animation_update);
				if(!checkCollision(new Point(x+width-1,y-1),new Point((int)(x),y-1))){
						targetX=-step;
						isMoving=true;
				}
			}
			if(lastKey==Game.button.S){
				dir=Game.Direction.Down;			
				walk_down.increaseIndex(last_animation_update);
				if(!checkCollision(new Point(x+width-1,y+height),new Point((int)(x),y+height))){
					targetX=step;
					isMoving=true;
				}
			}
			if(lastKey==Game.button.D){
				if(checkKeypressed()){
					switch(dir){
					case Down:	if(!checkCollision(new Point(x+width-1,y+height),new Point((int)(x),y+height))){
									walk_down.increaseIndex(last_animation_update);
									targetX=step;
									isMoving=true;
								}
								break;
					case Up:	if(!checkCollision(new Point(x+width-1,y-1),new Point((int)(x),y-1))){
									walk_up.increaseIndex(last_animation_update);
									targetX=-step;
									isMoving=true;
								}
								break;
					}
				}else{
					dir=Game.Direction.Right;
					walk_right.increaseIndex(last_animation_update);
					if(!checkCollision(new Point(x+width,y+height-1),new Point((int)(x+width),y))){
						targetX=step;
						isMoving=true;
					}
				}
			}
			if(lastKey==Game.button.A){
				if(checkKeypressed()){
					switch(dir){
					case Down:	if(!checkCollision(new Point(x+width-1,y+height),new Point((int)(x),y+height))){
									walk_down.increaseIndex(last_animation_update);
									targetX=step;
									isMoving=true;
								}
								break;
					case Up:	if(!checkCollision(new Point(x+width-1,y-1),new Point((int)(x),y-1))){
									walk_up.increaseIndex(last_animation_update);
									targetX=-step;
									isMoving=true;
								}
								break;
					}
				}else{	dir=Game.Direction.Left;			
						walk_left.increaseIndex(last_animation_update);
						if(!checkCollision(new Point(x-movement,y),new Point((int)(x-movement),y+height-1))){
								targetX=-step;
								isMoving=true;
				}
			}
		}
	}
}
	private void checkMovementState() {
		if(targetX<0){
			if(dir==Game.Direction.Left){
				targetX+=movement;
				Character.x-=movement;
				walk_left.increaseIndex(last_animation_update);
				if(isPushing){
					select_Tile.x-=movement;
					select_Tile.updateMask();
				}
			}else{//up
				targetX+=movement;
				Character.y-=movement;
				walk_up.increaseIndex(last_animation_update);
				if(isPushing){
					select_Tile.y-=movement;
					select_Tile.updateMask();
				}
			}
		}
		if(targetX>0){
			if(dir==Game.Direction.Right){
				targetX-=movement;
				Character.x+=movement;
				walk_right.increaseIndex(last_animation_update);
				if(isPushing){
					select_Tile.x+=movement;
					select_Tile.updateMask();
				}
			}else{//down
				targetX-=movement;
				Character.y+=movement;
				walk_down.increaseIndex(last_animation_update);
				if(isPushing){
					select_Tile.y+=movement;
					select_Tile.updateMask();
				}
			}
		}
		if(targetX==0){
			isMoving=false;
			isPushing=false;
		}
	}
	static boolean checkKeypressed() {
		//check if up and down arrow arrow are held down
		//we dont want to move to right and left if thats true;
		for(Game.button abutton:keypressed){
			if(abutton==Game.button.S || abutton==Game.button.W)
				return true;
		}
		return false;
	}
	public static boolean checkIfpressed(Game.button a) {
		for(Game.button abutton:keypressed){
			if(abutton==a)
				return true;
		}
		return false;
	}
	public static void releaseButton(int keycode) {
		switch(keycode){
		case KeyEvent.VK_A:	removeKey(Game.button.A);
							break;
		case KeyEvent.VK_S:	removeKey(Game.button.S);
							break;
		case KeyEvent.VK_W:	removeKey(Game.button.W);
							break;
		case KeyEvent.VK_D:	removeKey(Game.button.D);
							break;
		}
	}
	private static void removeKey(Game.button d) {
		Game.button toRemove=Game.button.None;
		for(Game.button abutton:keypressed){
			if(abutton==d)
				toRemove=abutton;
		}
		keypressed.remove(toRemove);
	}
	public static void fireProjectile() {
		if(!Character.isShooting)
			if(ammo>=1){
				Character.isShooting=true;
				createProjectile();
			}	
	}
	private static void createProjectile() {
		switch(Character.dir){
		
		case Right:	Character.weapon=new Projectile(x+targetX,y,projectile_img[0],Character.dir);
					break;
		case Left:	Character.weapon=new Projectile(x+targetX,y,projectile_img[0],Character.dir);
					break;
		case Up:	Character.weapon=new Projectile(x,y+targetX,projectile_img[1],Character.dir);
					break;
		case Down:	Character.weapon=new Projectile(x,y+targetX,projectile_img[1],Character.dir);
					break;
		}
		
	}
}
