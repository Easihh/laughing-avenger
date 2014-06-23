import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
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
	public static int x;
	public static int y;
	public static Tile select_Tile;
	
	public final static int step=16;
	public static ArrayList<Game.button> keypressed;
	public static boolean isShooting;
	public static boolean isPushing;
	public static boolean isMoving;
	public static  BufferedImage[] projectile_img;
	public static Game.button lastKey;
	public static Game.Direction dir;
	public static int ammo;
	public static int targetX;
	public static boolean powerActivated_hammer;
	public static boolean powerActivated_ladder;
	public static boolean powerActivated_arrow;
	public static boolean beingPushed=false;
	public static Projectile weapon=null;
	public static Death Death;
	public static Point lastPosition;
	public Character(int x, int y){
		Character.x=x;
		Character.y=y;
		Character.ammo=0;
		Death=new Death();
		isShooting=false;
		isMoving=false;
		isPushing=false;
		powerActivated_hammer=true;
		powerActivated_ladder=true;
		powerActivated_arrow=true;
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
			CollisionWithMonster();
		}
		if(Labyrinth.GameState==Game.GameState.Paused){
			MonsterBulletCollision();
			CollisionWithBullet();
		}
		if(Character.isShooting && weapon!=null)
			bullet_Collision();
	}
	private void CollisionWithMonster() {
		for(Tile aTile:Level.map_tile){
			if(aTile instanceof Skull)
				if(aTile.shape.intersects(x, y, width, height)){
					if(((Skull) aTile).isActive){
					Sound.Death.start();
					Labyrinth.GameState=Game.GameState.Death;
					}
				}
		}
		
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
										if(aTile.getType()!=6 && aTile.getType()!=95)//if its a tree the shot traverse it
											if(aMonster instanceof Gol)
												aMonster.canShoot=true;
										break;
							case Right:	if(aTile.shape.contains(aMonster.projectile.x+width,aMonster.projectile.y) || 
											aTile.shape.contains(aMonster.projectile.x+width,aMonster.projectile.y+height-1))
										if(aTile.getType()!=6 && aTile.getType()!=95)//if its a tree the shot traverse it
											if(aMonster instanceof Gol)
												aMonster.canShoot=true;
									break;
							case Left: 	if(aTile.shape.contains(aMonster.projectile.x-movement,aMonster.projectile.y) || 
											aTile.shape.contains(aMonster.projectile.x-movement,aMonster.projectile.y+height-1))
										if(aTile.getType()!=6 && aTile.getType()!=95)//if its a tree the shot traverse it
											if(aMonster instanceof Gol)
												aMonster.canShoot=true;
										break;
							case Up: 	if(aTile.shape.contains(aMonster.projectile.x+width-1,aMonster.projectile.y-1) || 
											aTile.shape.contains(aMonster.projectile.x,aMonster.projectile.y-1))
										if(aTile.getType()!=6 && aTile.getType()!=95){//if its a tree the shot traverse it
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
					if(aTile.isSolid && (!(aTile instanceof Water)))
						Character.isShooting=false;
					if(aTile instanceof Monster)
						checkMonsterState((Monster)aTile);
				}
			}
			if(weapon.dir==Game.Direction.Left){
				if(aTile.shape.contains(weapon.x-movement,weapon.y) && aTile.shape.contains(weapon.x-movement,weapon.y+height-1)){
					if(aTile.isSolid && (!(aTile instanceof Water)))
						Character.isShooting=false;
					if(aTile instanceof Monster)
						checkMonsterState((Monster)aTile);
				}
			}
			if(weapon.dir==Game.Direction.Up){
				if(aTile.shape.contains(weapon.x+width-1,weapon.y-1) && aTile.shape.contains(weapon.x,weapon.y-1)){
					if(aTile.isSolid && (!(aTile instanceof Water)))
						Character.isShooting=false;
					if(aTile instanceof Monster)
						checkMonsterState((Monster)aTile);
				}
			}
			if(weapon.dir==Game.Direction.Down){
				if(aTile.shape.contains(weapon.x+width-1,weapon.y+height) && aTile.shape.contains(weapon.x,weapon.y+height)){
					if(aTile.isSolid && (!(aTile instanceof Water)))
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
						 if(!(aTile instanceof Water) && !(theTile instanceof Water)){
							 if(aTile.isSolid || theTile.isSolid)
								 Character.isShooting=false;
						 }
				}
			}
				if(weapon.dir==Game.Direction.Left && aTile.shape.contains(weapon.x-movement,weapon.y)){//check top part collision
					for(Tile theTile:Level.map_tile){
					 if(theTile.shape.contains(weapon.x-movement,weapon.y+height-1))//check bottom part collision
						 if(!(aTile instanceof Water) && !(theTile instanceof Water)){
							 if(aTile.isSolid || theTile.isSolid)
								 Character.isShooting=false;
						 }
				}
			}
				if(weapon.dir==Game.Direction.Up && aTile.shape.contains(weapon.x,weapon.y-1)){//check left part collision
					for(Tile theTile:Level.map_tile){
					 if(theTile.shape.contains(weapon.x+width-1,weapon.y-1))//check right part collision
						 if(!(aTile instanceof Water) && !(theTile instanceof Water)){
							 if(aTile.isSolid || theTile.isSolid)
								 Character.isShooting=false;
						 }
				}
			}
				if(weapon.dir==Game.Direction.Down && aTile.shape.contains(weapon.x,weapon.y+height)){//check left part collision
					for(Tile theTile:Level.map_tile){
					 if(theTile.shape.contains(weapon.x+width-1,weapon.y+height))//check right part collision
						 if(!(aTile instanceof Water) && !(theTile instanceof Water)){
							 if(aTile.isSolid || theTile.isSolid)
								 Character.isShooting=false;
						 }
				}
			}
		}
	}
}
	private void checkMonsterState(Monster aTile) {
		if(aTile.TransformedState==0)
				aTile.transform();
		else if(aTile.TransformedState==1 || aTile.TransformedState==2){
				aTile.moveAcross_Screen(weapon.dir);
		}
		
	}
	private boolean checkCollision(Rectangle mask1,Rectangle mask2) {
		Tile intersect1=null;
		Tile intersect2=null;
		for(Tile aTile:Level.map_tile){
			if(aTile.shape.intersects(mask1))
					intersect1=aTile;
			if(aTile.shape.intersects(mask2))
					intersect2=aTile;
		}
		if(Level.goal.shape.intersects(mask1) && Level.goal.shape.intersects(mask2))
			if(Level.goal.getType()==5)
				Level.takeGoal();
		//Full Collision on same tile
		if(intersect1!=null && intersect2!=null){
			if(intersect1==intersect2){
				switch(intersect1.getType()){
				case 1:		return true;//rock
				case 2:		select_Tile=intersect1;
							intersect1.moveTile(step);
							return true;
				case 3:		takeHeart(intersect1);
							break;
				case 6:		return true;//tree
				case 11:	//One-way Arrow
				case 12:	//left one-way Arrow
				case 13:	//right one-way Arrow
				case 14:	//down one-way Arrow
							OneWayArrow OneArrow=(OneWayArrow)intersect1;
							return(OneArrow.checkArrow());
				case 15:	return true;//Skull
				case 19:	//worm
				case 20:	return true;
				case 30: 	return true;//rock wall
				case 100:	if(Character.y<2*step)
								Level.nextLevel(); //end door
							return false;
				case 94:  //heart give no ammo
							takeHeart(intersect1);
							break;
				case 95:	return true;//water
				case 96: 	return true;//door closed
				case 99:  	return false; //the background tile
				}
				
			}
			else if(intersect1.isSolid || intersect2.isSolid)
				return true; //both collision are different but could be same type of tile.	
			
		}
		//One of the 2 collision mask is empty
		else if(intersect1==null && intersect2!=null){
				if(intersect2.isSolid){
					if(intersect2.getType()!=3 && intersect2.getType()!=94) //hearth considered solid  for enemy projectile 
						return true;
				}
				//not not solid object; look if its One-way Arrow
				if(intersect2 instanceof OneWayArrow){
					OneWayArrow anArrow=(OneWayArrow)intersect2;
					return (anArrow.checkArrow());
				}
		}
		else if(intersect2==null && intersect1!=null){
			if(intersect1.isSolid){
				if(intersect1.getType()!=3 && intersect1.getType()!=94) //hearth considered solid  for enemy projectile 
					return true;
			}
			//not not solid object; look if its One-way Arrow
			if(intersect1 instanceof OneWayArrow){
				OneWayArrow anArrow=(OneWayArrow)intersect1;
				return (anArrow.checkArrow());
			}
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
		if(aTile.getType()==3)
			Character.ammo+=2;
	}
	private void movement() {
		checkMovementState();
		if(!keypressed.isEmpty()&& !isMoving){//if a button direction is held down
			if(keypressed.size()==1)
				lastKey=keypressed.get(0);//get last pressed button
			else
				lastKey=keypressed.get(keypressed.size()-1);//get last pressed button
			if(lastKey==Game.button.W && !beingPushed){
				dir=Game.Direction.Up;
				walk_up.setImage();
				if(!checkCollision(new Rectangle(x,y-step,16,16),new Rectangle(x+16,y-step,16,16))){
						targetX=-step;
						isMoving=true;
				}
			}
			if(lastKey==Game.button.S && !beingPushed){
				dir=Game.Direction.Down;			
				walk_down.setImage();
				if(!checkCollision(new Rectangle(x,y+height,16,16),new Rectangle(x+step,y+height,16,16))){
					targetX=step;
					isMoving=true;
				}
			}
			if(lastKey==Game.button.D){
				if(checkKeypressed()){
					switch(dir){
					case Down:	if(!checkCollision(new Rectangle(x,y+height,16,16),new Rectangle(x+step,y+height,16,16))){
									walk_down.setImage();
									targetX=step;
									isMoving=true;
								}
								break;
					case Up:	if(!checkCollision(new Rectangle(x,y-step,16,16),new Rectangle(x+16,y-step,16,16))){
									walk_up.setImage();
									targetX=-step;
									isMoving=true;
								}
								break;
					}
				}else{
					dir=Game.Direction.Right;
					walk_right.setImage();
					if(!checkCollision(new Rectangle(x+width,y,16,16),new Rectangle(x+width,y+step,16,16))){
						targetX=step;
						isMoving=true;
					}
				}
			}
			if(lastKey==Game.button.A){
				if(checkKeypressed()){
					switch(dir){
					case Down:	if(!checkCollision(new Rectangle(x,y+height,16,16),new Rectangle(x+step,y+height,16,16))){
									walk_down.setImage();
									targetX=step;
									isMoving=true;
								}
								break;
					case Up:	if(!checkCollision(new Rectangle(x,y-step,16,16),new Rectangle(x+16,y-step,16,16))){
									walk_up.setImage();
									targetX=-step;
									isMoving=true;
								}
								break;
					}
				}else{	dir=Game.Direction.Left;			
						walk_left.setImage();
						if(!checkCollision(new Rectangle(x-step,y,16,16),new Rectangle(x-step,y+step,16,16))){
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
	public static boolean checkPower() {
		Power aPower=new Power();
		if(powerActivated_ladder)aPower.useLadder();
		if(powerActivated_hammer)aPower.useHammer();
		if(powerActivated_arrow)aPower.useArrow();
		return false;
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
