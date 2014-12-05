import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Collections;
/**
 * 
 * This Class represent the main Hero in the game.This class decides how the Hero
 * should move, Collision detection with different tiles and monster's projectile, abilities etc.
 */

 public class Character {
	private final int width=32,height=32;
	private int movement=2;
	/** The X-axis position of the Character*/
	public  int x;
	/** The Y-axis position of the Character*/
	public 	int y;
	/** The Amount of Ammunition the Character currently possess*/
	public 	int ammo;
	/** Amount of X or Y-axis step in order to reach the next tile position*/
	public 	int targetX;
	private Movement move;
	private Projectile weapon=null;
	private Tile select_Tile;
	/*** Maximum distance the Character is allowed at once*/
	public final int step=16;
	/*** Determines whether the Character is currently shooting*/
	public static boolean isShooting;
	/*** Determines whether the Character is currently surfing on a Monster*/
	public static boolean isOnBoat;
	/*** Determines whether the Character is currently pushing something*/
	public  boolean isPushing;
	/*** Determines whether the Character is currently moving*/
	public  boolean isMoving;
	/*** Determines whether the Character is currently being pushed*/
	public  boolean beingPushed;
	/*** Determines whether the Character can shoot*/
	public  boolean canShot;
	/*** The Character's Death Logic*/
	public Death Death;
	/*** The Direction the Character is facing*/
	public Game.Direction dir;
	/*** The power the Character possess/will be able to activate.*/
	public Power aPower;
	
	public Character(){
		ammo=0;
		move= new Movement("lolo_movement",100);
		aPower=new Power();
		Death=new Death();
		isShooting=isMoving=isPushing=isOnBoat=canShot=beingPushed=false;
		 dir=Game.Direction.Up;
		 targetX=0;
	}
	/*** Method that decides how the the Character,its Weapon and its Animation should be shown on the screen.*/
	public void render(Graphics g){
		g.drawImage(move.getWalkAnimation(dir).getImage(),x,y,width,height,null);
		if(Character.isShooting && weapon!=null)
			weapon.render(g);		
	}
	/**
	 *Main Method of the Character Class that update its state and its animation as well as collision
	 *check and does the Input detection and Movement*/
	public void update(Input input){
		if(Labyrinth.GameState==Game.GameState.Normal){
			if(isWalkingSand())
				movement=1;
			else movement=2;
			if(canShot){
				checkPower();
				if(canShot && input.key[4]==1)
					fireProjectile();
				canShot=false;
			}
			movement(input);
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
		if(noInput(input))
			move.getWalkAnimation(dir).reset();
	}
	private boolean noInput(Input input) {
		return (input.key[0]==0 && input.key[1]==0 && input.key[2]==0 && input.key[3]==0);
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
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i) instanceof Skull)
				if(Level.map_tile.get(i).shape.intersects(x, y, width, height)){
					if(((Skull) Level.map_tile.get(i)).isActive && ((Skull)Level.map_tile.get(i)).TransformedState==0){
						Death.play();
					break;
					}
				}
			if(Level.map_tile.get(i) instanceof Alma)
				if(Level.map_tile.get(i).shape.intersects(x, y, width, height)){
					if(((Alma) Level.map_tile.get(i)).TransformedState==0){
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
	/* We have to determine what the Shoot is colliding with at any time.We have to keep in mind that it's possible for the
	 * "Shot" to be colliding with two different tile at the same time in order to determine what should happen.
	 */
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
			default:	break;			
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
	/* When a character bullet first fully collide with a Monster 
	 * it is transformed into a different state depending on its curernt state
	 */
	private void checkMonsterState(Monster aTile) {
		if(aTile.TransformedState==0)
				aTile.transform();
		else if(aTile.TransformedState==1 || aTile.TransformedState==2){
			if(!aTile.isDrowning)
				aTile.moveAcross_Screen(weapon.dir);
		}
		
	}
	/* Main Collision Method to determine what should be done when player is in contact with most of the game object.
	 * When a player is moving there are invisible collision masks to determine what kind tile the player is touching.
	 * A player should not be able to walk through solid object whether he is partially or fully colliding with said tile.
	 * 
	 * This Method is called every time the player tries to move.This method return whether there is a collision or not.
	 */
	private boolean checkCollision(Rectangle mask1,Rectangle mask2) {
		Tile intersect1=null;
		Tile intersect2=null;
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i).shape.intersects(mask1))
					intersect1=Level.map_tile.get(i);
			if(Level.map_tile.get(i).shape.intersects(mask2))
					intersect2=Level.map_tile.get(i);
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
				default:			break;
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
				//not solid object; look if its One-way Arrow
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
			//not solid object; look if its One-way Arrow
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
		if(Level.heart_amount==2){
			if(Level.room==7){
				aPower.powerActivated_hammer=true;
			Sound.resetSound();
			Sound.PowerEnabled.start();
			}
		}
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
	
	private void movement(Input input) {
		if (input.key[0]==1 && (input.key[3]==1 || input.key[1]==1) && !isMoving){//up and another key is pressed.Up take precedence
				dir=Game.Direction.Up;
				move.walk_up.setImage();
				if(!checkCollision(new Rectangle(x,y-step,step,step),new Rectangle(x+16,y-step,step,step)) && isAligned()){
					if(isOnBoat)
						targetX=2*-step;
					else targetX=-step;
						isMoving=true;
				}
			}
		if (input.key[2]==1 && (input.key[1]==0 || input.key[3]==1) && !isMoving){//down and another key is pressed.Down take precedence.
				dir=Game.Direction.Down;			
				move.walk_down.setImage();
				if(!checkCollision(new Rectangle(x,y+height,step,step),new Rectangle(x+step,y+height,step,step))&& isAligned()){
					if(isOnBoat)
						targetX=2*step;
					else targetX=step;
					isMoving=true;
				}
			}
		if (input.key[0]==1 && !isMoving){//up
			dir=Game.Direction.Up;
			move.walk_up.setImage();
			if(!checkCollision(new Rectangle(x,y-step,step,step),new Rectangle(x+16,y-step,step,step)) && isAligned()){
				if(isOnBoat)
					targetX=2*-step;
				else targetX=-step;
					isMoving=true;
			}
		}
		if (input.key[2]==1 && !isMoving){//down
			dir=Game.Direction.Down;			
			move.walk_down.setImage();
			if(!checkCollision(new Rectangle(x,y+height,step,step),new Rectangle(x+step,y+height,step,step))&& isAligned()){
				if(isOnBoat)
					targetX=2*step;
				else targetX=step;
				isMoving=true;
			}
		}
		if (input.key[1]==1 && !isMoving){//right
					dir=Game.Direction.Right;
					move.walk_right.setImage();
					if(!checkCollision(new Rectangle(x+width,y,step,step),new Rectangle(x+width,y+step,step,step))&& isAligned()){
						if(isOnBoat)
							targetX=2*step;
						else targetX=step;
						isMoving=true;
					}
			}
		if (input.key[3]==1 && !isMoving){//left
			dir=Game.Direction.Left;			
			move.walk_left.setImage();
			if(!checkCollision(new Rectangle(x-step,y,step,step),new Rectangle(x-step,y+step,step,step))&& isAligned()){
				if(isOnBoat)
					targetX=2*-step;
				else targetX=-step;
				isMoving=true;
				}
			}
		checkMovementState();	
	}
	private boolean isAligned(){
		return(x%step==0 && y%step==0);
	}
	/* Method used to determine movement.In order for the Character to always be aligned with the grid,
	 * the player can only move half a grid at once before another movement input is allowed.Since we
	 * want the character movement to be fluid we have to slowly increase its position until it reach half a grid
	 * in step instead of all at once.
	 * 
	 * This Method also does the same thing when a character is moving a block since we want the block to also move in a fluid
	 * manner and not all at once.
	 */
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
	private void fireProjectile() {
		if(!Character.isShooting)
			if(ammo>=1){
				Character.isShooting=true;
				canShot=false;
				createProjectile();
				Sound.ShotSound.stop();
				Sound.ShotSound.setFramePosition(0);
				Sound.ShotSound.start();
				ammo--;
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
		default:	break;
		}
	}
}
