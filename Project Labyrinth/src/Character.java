import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;

public class Character {
	private static Character instance=null; 
	private final int width=32;
	private final int height=32;
	private Game.button lastKey;
	private int movement=2;
	private int x;
	private int y;
	private Movement move;
	private Projectile weapon=null;
	private Tile select_Tile;
	
	public final int step=16;
	public ArrayList<Game.button> keypressed;
	public static boolean isShooting;
	public static boolean isPushing;
	public static boolean isMoving;
	public static boolean beingPushed;
	public static boolean isOnBoat;
	public boolean canShot;
	public Death Death;
	public Game.Direction dir;
	public int ammo;
	public static int targetX;
	public Power aPower;
	
	public Character(){
		ammo=0;
		move= new Movement("lolo_movement",100);
		aPower=new Power();
		Death=new Death();
		isShooting=false;
		isMoving=false;
		isPushing=false;
		isOnBoat=false;
		canShot=false;
		 dir=Game.Direction.Up;
		 lastKey=Game.button.None;
		 beingPushed=false;
		 keypressed=new ArrayList<Game.button>();
		 targetX=0;
	}
	  public static Character getInstance() {
		    if(instance == null) {
		        instance = new Character();}
		     return instance;}
	  public static void destroyInstance() {
		  		instance =null;}
	  
	  public int getX(){
		  return x;}
	  public int getY(){
		  return y;}
	  public void setX(int x){
		  this.x=x;}
	  public void setY(int y){
		  this.y=y;}
	  
	public void render(Graphics g){
		if(keypressed.size()==0)					
			move.getWalkAnimation(dir).reset();
		g.drawImage(move.getWalkAnimation(dir).getImage(),x,y,width,height,null);
		if(Character.isShooting && weapon!=null)
			weapon.render(g);		
	}
	
	public void update(){
		if(Labyrinth.GameState==Game.GameState.Normal){
			if(isWalkingSand())
				movement=1;
			else movement=2;
			if(canShot){
				checkPower();
				if(canShot)
					fireProjectile();
				canShot=false;
			}
			movement();
			CollisionWithWater();
			MonsterBulletCollision();
			CollisionWithBullet();
			CollisionWithMonster();
		}
		if(Labyrinth.GameState==Game.GameState.Paused){
			MonsterBulletCollision();
			CollisionWithBullet();
		}
		if(Character.isShooting && weapon!=null){
			bullet_state();
			weapon.update();
		}
	}
	private void CollisionWithWater() {
		boolean isCollidingWater=false;
		boolean death=false;
		isOnBoat=false;
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i) instanceof Water)
				if(Level.map_tile.get(i).shape.intersects(x, y, width, height)){
					isCollidingWater=true;
					death=true;
					}
				}
		if(isCollidingWater){
			for(int i=0;i<Level.map_tile.size();i++){
				if(Level.map_tile.get(i).type==Tile.ID.boat){
					if(Level.map_tile.get(i).shape.intersects(x, y, width, height)){
						death=false;
						isOnBoat=true;
					}
					if(Level.map_tile.get(i).x==x && Level.map_tile.get(i).y==y){
						death=false;
						isOnBoat=true;
					}
			}	}
		}
		if(death){
			Death.play();
		}
	}
	private boolean isWalkingSand() {
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i).type==Tile.ID.Sand)
				if(Level.map_tile.get(i).shape.intersects(x, y, width, height)){
					return true;
					}
				}
		return false;
	}
	private void CollisionWithMonster() {
		for(Tile aTile:Level.map_tile){
			if(aTile instanceof Skull)
				if(aTile.shape.intersects(x, y, width, height)){
					if(((Skull) aTile).isActive && ((Skull)aTile).TransformedState==0){
						Death.play();
					break;
					}
				}
			if(aTile instanceof Alma)
				if(aTile.shape.intersects(x, y, width, height)){
					if(((Alma) aTile).TransformedState==0){
						Death.play();
						break;
					}
				}
		}
		
	}
	private void CollisionWithBullet(){
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i) instanceof Monster){
				Monster aMonster=(Monster)Level.map_tile.get(i);
				Projectile projectile=aMonster.projectile;
				if(projectile!=null)
					if(new Rectangle(projectile.x,projectile.y,width,height).intersects(new Rectangle(x,y,width,height))){
						projectile.projectile_speed=0;
						Death.play();
					}
				}
			}
		}
	private void MonsterBulletCollision() {
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i) instanceof Monster && ((Monster)Level.map_tile.get(i)).projectile!=null){
				Monster aMonster=(Monster)Level.map_tile.get(i);
				searchifBulletCollide(aMonster);
				}
			}
	}
	private void searchifBulletCollide(Monster aMonster) {
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i)!=aMonster && aMonster.projectile!=null){//dont collide with yourself.
				if(Level.map_tile.get(i).shape.intersects(new Rectangle(aMonster.projectile.x,aMonster.projectile.y,width,height))){
					if(Level.map_tile.get(i).type!=Tile.ID.Water && Level.map_tile.get(i).type!=Tile.ID.Tree)
						if(aMonster instanceof Gol){
							aMonster.canShoot=true;
							aMonster.projectile=null;
						}
				}	}
			}
		}
	private void bullet_state() {
		Tile colliding_tile1;
		Tile colliding_tile2;
			switch(weapon.dir){
			case Left:	colliding_tile1=getCollidingTile(weapon.lefttop);
						colliding_tile2=getCollidingTile(weapon.leftbottom);
						bulletCollision(colliding_tile1,colliding_tile2);
						break;
			case Right:	colliding_tile1=getCollidingTile(weapon.righttop);
						colliding_tile2=getCollidingTile(weapon.rightbottom);
						bulletCollision(colliding_tile1,colliding_tile2);
						break;
			case Up:	colliding_tile1=getCollidingTile(weapon.topleft);
						colliding_tile2=getCollidingTile(weapon.topright);
						bulletCollision(colliding_tile1,colliding_tile2);
						break;
			case Down:	colliding_tile1=getCollidingTile(weapon.bottomleft);
						colliding_tile2=getCollidingTile(weapon.bottomright);
						bulletCollision(colliding_tile1,colliding_tile2);			
						}
			}
	private void bulletCollision(Tile colliding_tile1, Tile colliding_tile2) {
		if(colliding_tile1!=null && colliding_tile2!=null && colliding_tile1==colliding_tile2){//full collision
			if(colliding_tile1 instanceof Monster){
				checkMonsterState((Monster)colliding_tile1);
				isShooting=false;}
			else if(colliding_tile1.isSolid && (!(colliding_tile1 instanceof Water)))
				isShooting=false;}
		//check partial collision
		else if(colliding_tile1!=null && colliding_tile2!=null){
			if(!(colliding_tile1 instanceof Water) && !(colliding_tile2 instanceof Water)){
				if(colliding_tile1.isSolid || colliding_tile2.isSolid)
					isShooting=false;
			}
		}
		else if(colliding_tile1!=null && colliding_tile2==null){
				if(colliding_tile1.isSolid && (!(colliding_tile1 instanceof Water)))
					isShooting=false;}
		else if(colliding_tile2!=null && colliding_tile1==null){
			if(colliding_tile2.isSolid && (!(colliding_tile2 instanceof Water)))
				isShooting=false;}
		}
	private Tile getCollidingTile(Rectangle mask) {
		for (int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i).shape.intersects(mask))
				return Level.map_tile.get(i);}
		return null;
	}
	
	private void checkMonsterState(Monster aTile) {
		if(aTile.TransformedState==0)
				aTile.transform();
		else if(aTile.TransformedState==1 || aTile.TransformedState==2){
			if(!aTile.isDrowning)
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
			if(Level.goal.type==Tile.ID.BottomChestOpen)
				Level.takeGoal();
		//Full Collision on same tile
		if(intersect1!=null && intersect2!=null){
			if(intersect1 instanceof Monster && (((Monster)intersect1).type!=Tile.ID.MoveableBlock) && 
					((Monster)intersect1).type!=Tile.ID.boat)	return true;
			if(intersect1==intersect2){
				switch(intersect1.type){
				case Rock:			return true;
				case ClosedDoor:	return true;
				case AmmoHeart:		if(Math.abs(intersect1.x-x)<=step && Math.abs(intersect1.y-y)<=step)
										takeHeart(intersect1);
									break;
				case NoAmmoHeart:  	if(Math.abs(intersect1.x-x)<=step && Math.abs(intersect1.y-y)<=step)
										takeHeart(intersect1);
									break;	
				case OpenDoor:		if(y<2*step){
										Level.nextLevel();
										return true;}
									return false;
				case MoveableBlock:	select_Tile=intersect1;
									intersect1.moveTile(step);
									return true;
				case Tree: 			return true;
				case OneWayUp:	
				case OneWayLeft:	
				case OneWayRight:	
				case OneWayDown:	OneWayArrow OneArrow=(OneWayArrow)intersect1;
									return(OneArrow.checkArrow());
				case RockWall:		return true;
				case Water:			return true;
				case WaterFlowDown:	return true;
				case WaterFlowLeft:	return true;
				case WaterFlowRight:return true;
				case WaterFlowUp:	return true;
				case Lava:			return true;
				case boat:			if(intersect1.x%step==0 && intersect1.y%step==0){
										x=intersect1.x;
										y=intersect1.y;
									}
							return true;
				}
				
			}
			else if(intersect1.isSolid || intersect2.isSolid)
				return true; //both collision are different but could be same type of tile.	
			
		}
		//One of the 2 collision mask is empty
		else if(intersect1==null && intersect2!=null){
				if(intersect2.isSolid){
					if(intersect2.type!=Tile.ID.AmmoHeart && intersect2.type!=Tile.ID.NoAmmoHeart) //hearth considered solid  for enemy projectile 
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
				if(intersect1.type!=Tile.ID.AmmoHeart && intersect1.type!=Tile.ID.NoAmmoHeart) //hearth considered solid  for enemy projectile 
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
		if(Level.heart_amount==0){
			Level.openChest();
			if(Level.room==6){
				aPower.powerActivated_ladder=true;
			Sound.resetSound();
			Sound.PowerEnabled.start();
			}
		}
		if(aTile.type==Tile.ID.AmmoHeart)
			ammo+=2;
	}
	
	private void movement() {
		if(!keypressed.isEmpty()&& !isMoving){//if a button direction is held down
			if(keypressed.size()==1)
				lastKey=keypressed.get(0);//get last pressed button
			else
				lastKey=keypressed.get(keypressed.size()-1);//get last pressed button
			if(lastKey==Game.button.W){
				dir=Game.Direction.Up;
				move.walk_up.setImage();
				if(!checkCollision(new Rectangle(x,y-step,step,step),new Rectangle(x+16,y-step,step,step)) && isAligned()){
					if(isOnBoat)
						targetX=2*-step;
					else targetX=-step;
						isMoving=true;
				}
			}
			if(lastKey==Game.button.S){
				dir=Game.Direction.Down;			
				move.walk_down.setImage();
				if(!checkCollision(new Rectangle(x,y+height,step,step),new Rectangle(x+step,y+height,step,step))&& isAligned()){
					if(isOnBoat)
						targetX=2*step;
					else targetX=step;
					isMoving=true;
				}
			}
			if(lastKey==Game.button.D){
				if(checkKeypressed()){
					switch(dir){
					case Down:	if(!checkCollision(new Rectangle(x,y+height,16,16),new Rectangle(x+step,y+height,step,step))&& isAligned()){
									move.walk_down.setImage();
									if(isOnBoat)
										targetX=2*step;
									else targetX=step;
									isMoving=true;
								}
								break;
					case Up:	if(!checkCollision(new Rectangle(x,y-step,step,step),new Rectangle(x+16,y-step,step,step))&& isAligned()){
									move.walk_up.setImage();
									if(isOnBoat)
										targetX=2*-step;
									else targetX=-step;
									isMoving=true;
								}
								break;
					}
				}else{
					dir=Game.Direction.Right;
					move.walk_right.setImage();
					if(!checkCollision(new Rectangle(x+width,y,step,step),new Rectangle(x+width,y+step,step,step))&& isAligned()){
						if(isOnBoat)
							targetX=2*step;
						else targetX=step;
						isMoving=true;
					}
				}
			}
			if(lastKey==Game.button.A){
				if(checkKeypressed()){
					switch(dir){
					case Down:	if(!checkCollision(new Rectangle(x,y+height,16,16),new Rectangle(x+step,y+height,step,step))&& isAligned()){
									move.walk_down.setImage();
									if(isOnBoat)
										targetX=2*step;
									else targetX=step;
									isMoving=true;
								}
								break;
					case Up:	if(!checkCollision(new Rectangle(x,y-step,16,16),new Rectangle(x+step,y-step,step,step))&& isAligned()){
									move.walk_up.setImage();
									if(isOnBoat)
										targetX=2*-step;
									else targetX=-step;
									isMoving=true;
								}
								break;
					}
				}else{	dir=Game.Direction.Left;			
						move.walk_left.setImage();
						if(!checkCollision(new Rectangle(x-step,y,step,step),new Rectangle(x-step,y+step,step,step))&& isAligned()){
							if(isOnBoat)
								targetX=2*-step;
							else targetX=-step;
								isMoving=true;
				}
			}
		}
	}
	checkMovementState();	
}
	private boolean isAligned(){
		return(x%step==0 && y%step==0);
	}
	private void checkMovementState() {
		if(targetX<0){
			if(dir==Game.Direction.Left){
				beingPushed=false;
				targetX+=movement;
				x-=movement;
				move.walk_left.setImage();
				if(isPushing){
					select_Tile.x-=movement;
					select_Tile.updateMask();
				}
			}else{//up
				targetX+=movement;
				y-=movement;
				move.walk_up.setImage();
				if(isPushing){
					select_Tile.y-=movement;
					select_Tile.updateMask();
				}
			}
		}
		if(targetX>0){
			if(dir==Game.Direction.Right){
				beingPushed=false;
				targetX-=movement;
				x+=movement;
				move.walk_right.setImage();
				if(isPushing){
					select_Tile.x+=movement;
					select_Tile.updateMask();
				}
			}else{//down
				targetX-=movement;
				y+=movement;
				move.walk_down.setImage();
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
	public boolean checkKeypressed() {
		//check if up and down arrow arrow are held down
		//we dont want to move to right and left if thats true;
		for(Game.button abutton:keypressed){
			if(abutton==Game.button.S || abutton==Game.button.W)
				return true;
		}
		return false;
	}
	public boolean checkIfpressed(Game.button a) {
		for(Game.button abutton:keypressed){
			if(abutton==a)
				return true;
		}
		return false;
	}
	public void releaseButton(int keycode) {
		switch(keycode){
		case KeyEvent.VK_A:		removeKey(Game.button.A);
								break;
		case KeyEvent.VK_S:		removeKey(Game.button.S);
								break;
		case KeyEvent.VK_W:		removeKey(Game.button.W);
								break;
		case KeyEvent.VK_D:		removeKey(Game.button.D);
								break;				
		}
	}
	private void removeKey(Game.button d) {
		Game.button toRemove=Game.button.None;
		for(Game.button abutton:keypressed){
			if(abutton==d)
				toRemove=abutton;
		}
		keypressed.remove(toRemove);
	}
	private void fireProjectile() {
		if(!Character.isShooting)
			if(ammo>=1){
				Character.isShooting=true;
				canShot=false;
				createProjectile();
				Sound.ShotSound.stop();
				Sound.ShotSound.setFramePosition(0);
				Sound.ShotSound.start();
				//ammo--;
			}	
	}
	private void checkPower() {
		if(aPower.powerActivated_ladder)aPower.useLadder();
		if(aPower.powerActivated_hammer)aPower.useHammer();
		if(aPower.powerActivated_arrow)aPower.useArrow();
	}
	private void createProjectile() {
		switch(dir){
		
		case Right:	weapon=new Projectile(x+targetX,y,Game.projectile_img.get(0),dir);
					break;
		case Left:	weapon=new Projectile(x+targetX,y,Game.projectile_img.get(0),dir);
					break;
		case Up:	weapon=new Projectile(x,y+targetX,Game.projectile_img.get(1),dir);
					break;
		case Down:	weapon=new Projectile(x,y+targetX,Game.projectile_img.get(1),dir);
					break;
		}
	}
}
