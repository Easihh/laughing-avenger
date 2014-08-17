package monster;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import main.Hero.Direction;
import main.Map;
import main.Movement;
import main.Projectile;


public class RedOctorok extends Monster{
	private Movement Octorok,OctorokHit1,OctorokHit2;
	private BufferedImage projectile_img;
	public RedOctorok(int x, int y, ID type) {
		super(x,y,type);
		Octorok=new Movement("RedOctorok", 250);
		OctorokHit1=new Movement("RedOctorok_Hit1",250);
		OctorokHit2=new Movement("RedOctorok_Hit2",250);
		dir=Direction.Down;
		hitpoint=2;
		moveSpeed=1;
		damage=2;
		loadProjectileImage();
	}

	private void loadProjectileImage() {
		try {projectile_img=ImageIO.read(getClass().getResourceAsStream("/map/rockProjectile.png"));} 
		catch (IOException e) {e.printStackTrace();}	
	}

	@Override
	public void render(Graphics g) {
		if(projectile !=null)projectile.render(g);
		if(death==null && invincible_timer==null)
			g.drawImage(Octorok.getWalkAnimation(dir).getImage(),x,y,null);
		if(death==null && invincible_timer!=null && invincible_timer.elapsedMillis()<250)
			g.drawImage(OctorokHit1.getWalkAnimation(dir).getImage(),x,y,null);
		if(death==null && invincible_timer!=null && invincible_timer.elapsedMillis()>250)
			g.drawImage(OctorokHit2.getWalkAnimation(dir).getImage(),x,y,null);
		if(death!=null)
			g.drawImage(killEffect,x,y,null);
	}
	@Override
	public void update() {
		if(death!=null && death.elapsedMillis()>200){
			destroy(myID);
			death=null;
		}
		if(death==null){
			updateMask();
			checkHeroSwordCollision();
			checkHeroCollision();
			if(projectile!=null)projectile.update();
			if(projectile!=null && projectile.outOfBound())projectile=null;
			if(stepToMove==0){
				if(x%step!=0 || y%step!=0){
					System.out.println("x:"+x%step);
					System.out.println("y:"+y%step);
					System.out.println("ERROR UNALIGNED");
				}
				if(!collisionTile() && !outOfBound(step,dir)){
					tryShoot();
					rdymove();
				}
				else getNewDirection();
			}
			if(stepToMove>0 && pushbackdir==Direction.None)
				move();
			if(stepToMove>0 && pushbackdir!=Direction.None)
				pushbackMove();
			if(invincible_timer!=null && invincible_timer.elapsedMillis()>invincible_duration)
				invincible_timer=null;
		}
	}

	private void tryShoot() {
		Random rng=new Random();
		int value=rng.nextInt(100);
		if(value<5)
			createProjectile();
	}

	private void createProjectile() {
		switch(dir){
		case Left:	projectile=new Projectile(new Rectangle(x-32,y,32,32), projectile_img, dir);
					break;
		case Right:	projectile=new Projectile(new Rectangle(x+32,y,32,32), projectile_img, dir);
					break;
		case Up:	projectile=new Projectile(new Rectangle(x,y-32,32,32), projectile_img, dir);
					break;
		case Down:	projectile=new Projectile(new Rectangle(x,y+32,32,32), projectile_img, dir);
					break;
		}
	}

	private void pushbackMove() {
		switch(pushbackdir){
		case Down:	if(stepToPush%step==0){
						if(!checkCollision(new Rectangle(x,y+height,width,step)) && !outOfBound(step, pushbackdir)){
							stepToPush-=pushbackSpeed;
							y+=pushbackSpeed;}
						else pushbackdir=Direction.None; 
					}
					else{
							y+=pushbackSpeed;
							stepToPush-=pushbackSpeed;
						}
					Octorok.walk_down.setImage();
					break;
		case Up:	if(stepToPush%step==0){
						if(!checkCollision(new Rectangle(x,y-step,width,step)) && !outOfBound(step, pushbackdir)){
							stepToPush-=pushbackSpeed;
							y-=pushbackSpeed;}
						else pushbackdir=Direction.None; 
					}
					else{
							y-=pushbackSpeed;
							stepToPush-=pushbackSpeed;
						}
					Octorok.walk_up.setImage();
					break;
		case Left: 	if(stepToPush%step==0){
						if(!checkCollision(new Rectangle(x-step,y,step,height)) && !outOfBound(step, pushbackdir)){
							stepToPush-=pushbackSpeed;
							x-=pushbackSpeed;}
						else pushbackdir=Direction.None; 
					}
					else{
							x-=pushbackSpeed;
							stepToPush-=pushbackSpeed;
						}
					Octorok.walk_left.setImage();
					break;
		case Right: 	if(stepToPush%step==0){
							if(!checkCollision(new Rectangle(x+width,y,step,height)) && !outOfBound(step, pushbackdir)){
								stepToPush-=pushbackSpeed;
								x+=pushbackSpeed;}
							else pushbackdir=Direction.None; 
						}
						else{
								x+=pushbackSpeed;
								stepToPush-=pushbackSpeed;
							}
					Octorok.walk_right.setImage();
					break;
		}
		if(stepToPush<=0)pushbackdir=Direction.None;
	}

	private boolean outOfBound(int nextStep,Direction dir) {
		Map map=Map.getInstance();
		switch(dir){
		case Down: 	return(y+nextStep>=(map.worldY*map.roomHeight)+map.roomHeight);
		case Up: 	return(y-nextStep<=(map.worldY*map.roomHeight));
		case Right: return(x+nextStep>=(map.worldX*map.roomWidth)+map.roomWidth);
		case Left: 	return(x-nextStep<=(map.worldX*map.roomWidth));
		}
		return false;
	}

	private boolean collisionTile() {
		if(dir==Direction.Down)
			return(checkCollision(new Rectangle(x,y+height,width,step)));
		if(dir==Direction.Left)
			return(checkCollision(new Rectangle(x-step,y,step,height)));
		if(dir==Direction.Right)
			return(checkCollision(new Rectangle(x+width,y,step,height)));
		if(dir==Direction.Up)
			return(checkCollision(new Rectangle(x,y-step,width,step)));
		return false;
	}

	private boolean checkCollision(Rectangle nextMask) {
		Map map=Map.getInstance();
		for(int i=map.worldX*map.tilePerRow;i<map.worldX*map.tilePerRow+map.tilePerRow;i++){
			for(int j=map.worldY*map.tilePerCol;j<map.worldY*map.tilePerCol+map.tilePerCol;j++){
				if(Map.allObject[i][j]!=null){
					if(Map.allObject[i][j].mask.intersects(nextMask) && Map.allObject[i][j]!=this) 
						if(Map.allObject[i][j].isSolid)
							return true;					
				}
			}
		}
		return false;
	}

	private void move() {
		switch(dir){
		case Down:	y+=moveSpeed;
					Octorok.walk_down.setImage();
					break;
		case Up:	y-=moveSpeed;
					Octorok.walk_up.setImage();
					break;
		case Left: 	x-=moveSpeed;
					Octorok.walk_left.setImage();
					break;
		case Right: x+=moveSpeed;
					Octorok.walk_right.setImage();
					break;
		}
		stepToMove-=moveSpeed;
	}

	private void rdymove() {
		Random random=new Random();
		int result=random.nextInt(6);
		if(result>4)getNewDirection();
		else stepToMove=step;
	}

	private void getNewDirection() {
		int mydir=0,result;
		if(dir==Direction.Up)mydir=0;
		if(dir==Direction.Right)mydir=1;
		if(dir==Direction.Down)mydir=2;
		if(dir==Direction.Left)mydir=3;
		Random myRandom=new Random();
		do
			result=myRandom.nextInt(4);
		while(result==mydir);
		if(result==0)dir=Direction.Up;
		if(result==1)dir=Direction.Right;
		if(result==2)dir=Direction.Down;
		if(result==3)dir=Direction.Left;
	}

}
