import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import javax.imageio.ImageIO;


public class Character {
	private final int width=32;
	private final int height=32;
	private final int rows=1;// character animation row
	private final int cols=5;// character animation colum
	private final int movement=2;
	private Animation walk_down=null;
	private Animation walk_up=null;
	private Animation walk_left=null;
	private Animation walk_right=null;
	private BufferedImage img=null;
	private static  BufferedImage[] projectile_img;
	private static int ammo;
	public static int x;
	public static int y;
	public static Tile select_Tile;
	
	public final static int step=16;
	public static ArrayList<Game.button> keypressed;
	public static boolean isShooting;
	public static boolean isPushing;
	public static boolean isMoving;
	public static Game.button lastKey;
	public static Game.Direction dir;
	public static int targetX;
	//public static long last_animation_update=0;
	public static Projectile weapon=null;
	public static Death Death;
	public Character(int x, int y){
		Character.x=x;
		Character.y=y;
		Character.ammo=0;
		Death=new Death();
		isShooting=false;
		isMoving=false;
		isPushing=false;
		 dir=Game.Direction.Down;
		 lastKey=Game.button.None;
		 keypressed=new ArrayList<Game.button>();
		 walk_down=new Animation();
		 walk_up=new Animation();
		 walk_left=new Animation();
		 walk_right=new Animation();
		 targetX=0;
		 try {
			img=ImageIO.read(getClass().getResource("/tileset/Lolo_down.png"));
			getImagefromSpriteSheet(img,walk_down,"down");
			img=ImageIO.read(getClass().getResource("/tileset/Lolo_up.png"));
			getImagefromSpriteSheet(img,walk_up,"up");
			img=ImageIO.read(getClass().getResource("/tileset/Lolo_left.png"));
			getImagefromSpriteSheet(img,walk_left,"left");
			img=ImageIO.read(getClass().getResource("/tileset/Lolo_right.png"));
			getImagefromSpriteSheet(img,walk_right,"right");
			img=ImageIO.read(getClass().getResource("/tileset/shoot_sheet.png"));
			getProjectileSheet(img);
			img=ImageIO.read(getClass().getResource("/tileset/monster_state.png"));
			getMonsterStatesheet(img);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private void getMonsterStatesheet(BufferedImage StateSheet) {
		int sheet_row=1;
		int sheet_cols=2;
		Level.monsterState=new BufferedImage[sheet_row*sheet_cols];
		for(int i=0;i<sheet_row;i++){
			 for(int j=0;j<sheet_cols;j++){
				Level.monsterState[(i*sheet_cols)+j]=StateSheet.getSubimage(j*width, i*height, width, height);
			 }
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
			Animation Animation,String direction) {
		BufferedImage[] img=new BufferedImage[cols]; 
		 for(int i=0;i<rows;i++){
			 for(int j=0;j<cols;j++){
				img[(i*cols)+j]=spriteSheet.getSubimage(j*width, i*height, width, height);			
			 }
		 }
		 switch(direction){ 
		 case "left":	for(int i=0;i<cols;i++)
			 				walk_left.AddScene(img[i],100);
			 			break;
		 case "right":	for(int i=0;i<cols;i++)
							walk_right.AddScene(img[i],100);
			 			break;
		 case "down":	for(int i=0;i<cols;i++)
							walk_down.AddScene(img[i],100);
			 			break;
		 case "up":		for(int i=0;i<cols;i++)
							walk_up.AddScene(img[i],100);
			 			break;
		 }
	}
	public void render(Graphics g){
		g.setColor(Color.red);
		switch(dir){
	
		case Down:	if(keypressed.size()==0)					
						walk_down.reset();
					g.drawImage(walk_down.getImage(),x,y,width,height,null);
					break;
		case Up:	if(keypressed.size()==0)
						walk_up.reset();
					g.drawImage(walk_up.getImage(),x,y,width,height,null);
					break;
		case Right:	if(keypressed.size()==0)
						walk_right.reset();
					g.drawImage(walk_right.getImage(),x,y,width,height,null);
					break;
		case Left:	if(keypressed.size()==0)
						walk_left.reset();
					g.drawImage(walk_left.getImage(),x,y,width,height,null);
					break;
		default: 	g.drawRect(x, y, width, height);
					break;
		}
		if(Character.isShooting)
			weapon.render(g);		
	}
	public void update(){
		if(Labyrinth.GameState==Game.GameState.Normal){
			movement();
			MonsterBulletCollision();
			CollisionWithBullet();
		}
		if(Labyrinth.GameState==Game.GameState.Paused){
			MonsterBulletCollision();
			CollisionWithBullet();
		}
		if(Character.isShooting && weapon!=null)
			bullet_Collision();
	}
	private void CollisionWithBullet(){
		for(Tile aTile:Level.map_tile){
			if(aTile instanceof Monster){
				Monster aMonster=(Monster)aTile;
				Projectile projectile=aMonster.projectile;
				if(projectile!=null)
					switch(projectile.dir){
						case Right:	if(projectile.shape.contains(x+width-1,y) || 
								projectile.shape.contains(x+width-1,y+height)){
								projectile.projectile_speed=0;
								Sound.Death.start();
								Labyrinth.GameState=Game.GameState.Death;
								}
								break;
						case Left:	if(projectile.shape.contains(x,y) || 
								projectile.shape.contains(x,y+height-1)){
								projectile.projectile_speed=0;
								Sound.Death.start();
								Labyrinth.GameState=Game.GameState.Death;
								}
								break;
						case Down:	if(projectile.shape.contains(x+width,y+height-1) || 
								projectile.shape.contains(x,y+height-1)){
								projectile.projectile_speed=0;
								Sound.Death.start();
								Labyrinth.GameState=Game.GameState.Death;
								}
								break;
						case Up:	if(projectile.shape.contains(x+width-1,y) || 
								projectile.shape.contains(x,y)){
								projectile.projectile_speed=0;
								Sound.Death.start();
								Labyrinth.GameState=Game.GameState.Death;
								}
								break;
				}
			}
		}
	}
	private void MonsterBulletCollision() {
		/** Search if there is a  tile touching the bullet 
		 *  Destroy the bullet if it touch anything solid like a rock but not tree.
		 * */
		
		for(Tile theTile:Level.map_tile){
			if(theTile instanceof Monster){
				Monster aMonster=(Monster)theTile;
				if(aMonster.projectile!=null)
					for(Tile aTile:Level.map_tile){
							switch(aMonster.projectile.dir){
							case Down:	if(aTile.shape.contains(aMonster.projectile.x+width-1,aMonster.projectile.y+height) || 
											aTile.shape.contains(aMonster.projectile.x,aMonster.projectile.y+height))
										if(aTile.getType()!=6)//if its a tree the shot traverse it
											if(aMonster instanceof Gol)
												aMonster.canShoot=true;
										break;
							case Right:	if(aTile.shape.contains(aMonster.projectile.x+width,aMonster.projectile.y) || 
											aTile.shape.contains(aMonster.projectile.x+width,aMonster.projectile.y+height-1))
										if(aTile.getType()!=6)//if its a tree the shot traverse it
											if(aMonster instanceof Gol)
												aMonster.canShoot=true;
									break;
							case Left: 	if(aTile.shape.contains(aMonster.projectile.x-movement,aMonster.projectile.y) || 
											aTile.shape.contains(aMonster.projectile.x-movement,aMonster.projectile.y+height-1))
										if(aTile.getType()!=6)//if its a tree the shot traverse it
											if(aMonster instanceof Gol)
												aMonster.canShoot=true;
										break;
							case Up: 	if(aTile.shape.contains(aMonster.projectile.x+width-1,aMonster.projectile.y-1) || 
											aTile.shape.contains(aMonster.projectile.x,aMonster.projectile.y-1))
										if(aTile.getType()!=6){//if its a tree the shot traverse it
											if(aMonster instanceof Gol)
												aMonster.canShoot=true;
									}
								break;
					}
				}
			}
		}
	}
	private void bullet_Collision() {
		for(Tile aTile:Level.map_tile){
			if(weapon.dir==Game.Direction.Right){
				if(aTile.shape.contains(weapon.x+width,weapon.y) && aTile.shape.contains(weapon.x+width,weapon.y+height-1)){
					Character.isShooting=false;
					if(aTile instanceof Monster)
						checkMonsterState((Monster)aTile);
				}
			}
			if(weapon.dir==Game.Direction.Left){
				if(aTile.shape.contains(weapon.x-movement,weapon.y) && aTile.shape.contains(weapon.x-movement,weapon.y+height-1)){
					Character.isShooting=false;
					if(aTile instanceof Monster)
						checkMonsterState((Monster)aTile);
				}
			}
			if(weapon.dir==Game.Direction.Up){
				if(aTile.shape.contains(weapon.x+width-1,weapon.y-1) && aTile.shape.contains(weapon.x,weapon.y-1)){
					Character.isShooting=false;
					if(aTile instanceof Monster)
						checkMonsterState((Monster)aTile);
				}
			}
			if(weapon.dir==Game.Direction.Down){
				if(aTile.shape.contains(weapon.x+width-1,weapon.y+height) && aTile.shape.contains(weapon.x,weapon.y+height)){
					Character.isShooting=false;
					if(aTile instanceof Monster)
						checkMonsterState((Monster)aTile);
			}
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
	public void checkMonsterState(Monster aTile) {
		if(aTile.TransformedState==0)
				aTile.transform();
		else if(aTile.TransformedState==1 || aTile.TransformedState==2){
				aTile.moveAcross_Screen(weapon.dir);
		}
		
	}
	private boolean checkCollision(Point pt1,Point pt2) {
		if(Level.goal.shape.contains(pt1)|| Level.goal.shape.contains(pt2))
			if(Level.goal.getType()==5)
				Level.takeGoal();
		for(Tile aTile:Level.map_tile){
			if(aTile.shape.contains(pt1) || aTile.shape.contains(pt2)){
				if(aTile.shape.contains(pt1)&& aTile.shape.contains(pt2) && aTile.getType()==100){
					if(Character.y<2*step)
						Level.nextLevel(); //end door
					return false;
				}
				//we have a collision
				switch(aTile.getType()){
				
				case 2: if(aTile.shape.contains(pt1) && aTile.shape.contains(pt2)){
							select_Tile=aTile;
							Block Tile=(Block)aTile;
							Tile.moveTile(step);
						}
						break;
				case 3: 	takeHeart(aTile);
							break;
				case 11:	
				case 12:
				case 13:
				case 14:if(!searchBlock(pt1,pt2)){ //no block infront
							if(!OneWayArrow(aTile))
								return false;
						}
						else 	if(select_Tile.shape.contains(pt1) && select_Tile.shape.contains(pt2)){
									Block Tile=(Block)select_Tile;
									Tile.moveTile(step);
						}
						break;
				}
				return true;
			}
		}
		return false;
	}
	private boolean searchBlock(Point pt1, Point pt2) {
		for(Tile aTile:Level.map_tile){
			if(aTile.shape.contains(pt1) || aTile.shape.contains(pt2)){
				if(aTile.getType()==2){
					select_Tile=aTile;
					return true;
				}
			}
		}
		return false;
	}
	private boolean OneWayArrow(Tile aTile) {
		if(Character.dir==Game.Direction.Down && aTile.getType()==11){
			if(aTile.shape.contains(x,y+height-1) || aTile.shape.contains(x+width,y+height-1))
				return false;//allow pass
			return true; 
		}
		if(Character.dir==Game.Direction.Up && aTile.getType()==14){
			if(aTile.shape.contains(x,y) || aTile.shape.contains(x+width,y))
				return false;
			return true;
		}
		if(Character.dir==Game.Direction.Left && aTile.getType()==13){
			if(aTile.shape.contains(x,y) || aTile.shape.contains(x,y+height))
				return false;
			return true;
		}
		if(Character.dir==Game.Direction.Right && aTile.getType()==12){
			if(aTile.shape.contains(x+width-1,y) || aTile.shape.contains(x+width-1,y+height-1))
				return false;
			return true;
		}
		return false;
	}
	private void takeHeart(Tile aTile) {
		Level.map_tile.remove(aTile);
		Collections.sort(Level.map_tile);
		Level.heart_amount-=1;
		Sound.HeartSound.stop();
		Sound.HeartSound.setFramePosition(0);
		Sound.HeartSound.start();
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
				walk_up.setImage();
				if(!checkCollision(new Point(x+width-1,y-1),new Point((int)(x),y-1))){
						targetX=-step;
						isMoving=true;
				}
			}
			if(lastKey==Game.button.S){
				dir=Game.Direction.Down;			
				walk_down.setImage();
				if(!checkCollision(new Point(x+width-1,y+height),new Point((int)(x),y+height))){
					targetX=step;
					isMoving=true;
				}
			}
			if(lastKey==Game.button.D){
				if(checkKeypressed()){
					switch(dir){
					case Down:	if(!checkCollision(new Point(x+width-1,y+height),new Point((int)(x),y+height))){
									walk_down.setImage();
									targetX=step;
									isMoving=true;
								}
								break;
					case Up:	if(!checkCollision(new Point(x+width-1,y-1),new Point((int)(x),y-1))){
									walk_up.setImage();
									targetX=-step;
									isMoving=true;
								}
								break;
					}
				}else{
					dir=Game.Direction.Right;
					walk_right.setImage();
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
									walk_down.setImage();
									targetX=step;
									isMoving=true;
								}
								break;
					case Up:	if(!checkCollision(new Point(x+width-1,y-1),new Point((int)(x),y-1))){
									walk_up.setImage();
									targetX=-step;
									isMoving=true;
								}
								break;
					}
				}else{	dir=Game.Direction.Left;			
						walk_left.setImage();
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
				walk_left.setImage();
				if(isPushing){
					select_Tile.x-=movement;
					select_Tile.updateMask();
				}
			}else{//up
				targetX+=movement;
				Character.y-=movement;
				walk_up.setImage();
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
				walk_right.setImage();
				if(isPushing){
					select_Tile.x+=movement;
					select_Tile.updateMask();
				}
			}else{//down
				targetX-=movement;
				Character.y+=movement;
				walk_down.setImage();
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
				Sound.ShotSound.stop();
				Sound.ShotSound.setFramePosition(0);
				Sound.ShotSound.start();
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
