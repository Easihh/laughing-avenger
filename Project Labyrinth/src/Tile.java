import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Tile {
	private final int height=32;
	private final int width=32;
	private final long nano=1000000L;
	private boolean isMonster=false;
	private int index=0;
	private int maxFrame=0;
	private int nextFrame=0;
	private int oldX;
	private int oldY;
	public int oldtype;
	private int type;//1=rock 2=moveable green block,3=heartcard,4=closed goal,5=opened chest,6=tree,99=background
	private long last_animation_update=0;
	private long time_since_transform=0;
	public Image old_img;
	public Image previousState;
	
	
	public Projectile myProjectile;
	public boolean canShoot=false;
	public Animation animation;
	public boolean isMovingAcrossScreen = false;
	public Game.Direction dir;
	public Game.Direction old_dir;
	public int TransformedState=0;
	public int x;
	public int y;
	public Image img;
	public Polygon shape;
	public BufferedImage projectile_img;
	
	public Tile(int x, int y,Image image,int type,boolean isMonster) {
		this.x=x;
		this.y=y;
		oldX=x;
		oldY=y;
		this.type=type;
		oldtype=type;
		this.isMonster=isMonster;
		img=image;
		old_img=image;
		if(type==11 || type==22)
			canShoot=true;
		int[] xpoints={x,x+width,x+width,x};
		int[] ypoints={y,y,y+height,y+height};
		shape=new Polygon(xpoints, ypoints, 4);
	}
	public Tile(Image image) {
		img=image;
		type=99;// we have a background
	}
	public void moveTile(int movement){
		if(!checkCollison(x+width,y,x+width,y+height-1) && Character.dir==Game.Direction.Right){
						Character.targetX=Character.step;
						Character.isMoving=true;
						Character.isPushing=true;
					}
		//colliding with something check if one-way arrow and move accordingly
		//else{
			//	checkOneWayArrowCollision(aTile, tile)
		//}
		if(!checkCollison(x,y+height,x+width-1,y+height)&& Character.dir==Game.Direction.Down){
						Character.targetX=Character.step;
						Character.isMoving=true;
						Character.isPushing=true;
						
					}
		if(!checkCollison(x,y-1,x+width-1,y-1)&& Character.dir==Game.Direction.Up){
						Character.targetX=-Character.step;
						Character.isMoving=true;
						Character.isPushing=true;
					}
		if(!checkCollison(x-1,y,x-1,y+height-1)&& Character.dir==Game.Direction.Left){
						Character.targetX=-Character.step;
						Character.isMoving=true;
						Character.isPushing=true;
					}
		int[] xpoints={x,x+width,x+width,x};
		int[] ypoints={y,y,y+height,y+height};
		shape=new Polygon(xpoints, ypoints, 4);
	}
	private boolean checkCollison(int x1,int y1,int x2,int y2) {
		for(Tile aTile:Level.map_tile){
			if(aTile.shape.contains(x1,y1)|| aTile.shape.contains(x2,y2)){
				if(aTile.getType()==12)
					return(checkOneWayArrowCollision(aTile, this));
				if(aTile.getType()==2)
					return false;
				return true;
			}
		}
		return false;
	}
	private boolean checkOneWayArrowCollision(Tile aTile, Tile tile) {
		if(Character.dir==Game.Direction.Down && aTile.dir==Game.Direction.Up){
			if(aTile.shape.contains(tile.x,tile.y+height-1) || aTile.shape.contains(tile.x+width,tile.y+height-1))
				return false;//allow pass
			return true; 
		}
		if(Character.dir==Game.Direction.Up && aTile.dir==Game.Direction.Down){
			if(aTile.shape.contains(tile.x,tile.y) || aTile.shape.contains(tile.x+width,tile.y))
				return false;
			return true;
		}
		if(Character.dir==Game.Direction.Left && aTile.dir==Game.Direction.Right){
			System.out.println("tile:"+aTile.getType());
			if(aTile.shape.contains(tile.x,tile.y) || aTile.shape.contains(tile.x,tile.y+height))
				return false;
			return true;
		}
		if(Character.dir==Game.Direction.Right && aTile.dir==Game.Direction.Left){
			if(aTile.shape.contains(tile.x+width-1,tile.y) || aTile.shape.contains(tile.x+width-1,tile.y+height-1))
				return false;
			return true;
		}
		return false;
	}
	public boolean isWalkable(){
		return(type==99 || type==4 ||type==100);
	}
	public int getType(){
		return type;
	}
	public void updateMask(){
		int[] xpoints={x,x+width,x+width,x};
		int[] ypoints={y,y,y+height,y+height};
		shape=new Polygon(xpoints, ypoints, 4);
	}
	public void render(Graphics g) {
		g.setColor(Color.BLUE);
		if(type==99){//draw background
			for(int y=0;y<Level.map_height;y+=height){
				for(int x=0;x<Level.map_width;x+=width){
					g.drawImage(img,x,y,width,height,null);
				}
			}
		}
		else{
				checkState();
				if(isMonster && TransformedState==0 && !isMovingAcrossScreen){
					if(maxFrame>0){
					g.drawImage(animation.animation[index],x,y,width,height,null);
					if(type==22)
						move();
					}
					else
						//g.drawPolygon(shape);
						g.drawImage(img,x,y,width,height,null);
						fireProjectile(g);
				}
				else{			
					if(isMovingAcrossScreen)
						updateLocation();
					if(!isOffScreen())
				g.drawImage(img,x,y,width,height,null);
						//g.drawPolygon(shape);
				}
		}
		updateAnimation();
	}
	private void move() {
		switch(dir){
		case Left: 	if(checkCollison(x-1, y, x-1, y+height-1)){
						dir=Game.Direction.Right;
					}else
					x-=2;
					break;
		case Right: if(checkCollison(x+width, y+height-1, x+width, y)){
						dir=Game.Direction.Left;
					}else
					x+=2;
					break;
		case Up: if(checkCollison(x+width-1, y-1, x, y-1)){
						dir=Game.Direction.Down;
					}else
					y-=2;
					break;
		case Down: if(checkCollison(x+width-1, y+height, x, y+height)){
					dir=Game.Direction.Up;
					}else
					y+=2;
					break;
		}
		updateMask();
	}
	private void fireProjectile(Graphics g) {
		if(canShoot){
			if(type!=11 && type!=22){//not a medusa or donMedusa
				if(LineofSight()){
					myProjectile=new Projectile(x,y,projectile_img,dir);
					Sound.DragonSound.stop();
					Sound.DragonSound.setFramePosition(0);
					Sound.DragonSound.start();
					canShoot=false;
					myProjectile.projectile_speed=3;
				}
			}
			else
				try {
						MultiDirectionSight();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		if(myProjectile!=null && !canShoot){
			myProjectile.render(g);
		}
		
	}
	private void MultiDirectionSight() throws IOException {
		boolean shoot=false;
		boolean inRange=false;
		Game.Direction dir=null;
		/*Case Down*/
		if((Character.x+Character.step==x|| Character.x-Character.step==x) && y<Character.y && type==11){
			img=Level.game_tileset[14];
			inRange=true;
		}
		if(Character.x==x  && y<Character.y){
				//hero found in line of sight
			// Check if there is an object inbetween
			if(type==11)img=Level.game_tileset[14];//change to awaken medusa;
			inRange=true;
			dir=Game.Direction.Down;
			if(!Object_inBetween(dir)){
				shoot=true;
				if(type==11)projectile_img=ImageIO.read(getClass().getResource("/tileset/medusa_shot_down.png"));
				if(type==22)
					projectile_img=ImageIO.read(getClass().getResource("/tileset/DonMedusaShot_Down.png"));	
			}
		}
		/*Case Up*/
		if((Character.x+Character.step==x|| Character.x-Character.step==x) && y>Character.y && type==11){
			img=Level.game_tileset[14];
			inRange=true;
		}
		if( x==Character.x && y>Character.y){
			dir=Game.Direction.Up;
			inRange=true;
			if(type==11)img=Level.game_tileset[14];//change to awaken medusa;
			if(!Object_inBetween(dir)){
				shoot=true;
				if(type==11)projectile_img=ImageIO.read(getClass().getResource("/tileset/medusa_shot_up.png"));
				if(type==22)
					projectile_img=ImageIO.read(getClass().getResource("/tileset/DonMedusaShot_Up.png"));	
			}
		}
		/*Case Left*/
		if((Character.y+Character.step==y|| Character.y-Character.step==y) && x>Character.x && type==11){
			img=Level.game_tileset[14];
			inRange=true;
		}
		if((y==Character.y) && x>Character.x){
			dir=Game.Direction.Left;
			inRange=true;
			if(type==11)img=Level.game_tileset[14];//change to awaken medusa;
			if(!Object_inBetween(dir)){
				shoot=true;
				if(type==11)projectile_img=ImageIO.read(getClass().getResource("/tileset/medusa_shot_left.png"));
				if(type==22)
					projectile_img=ImageIO.read(getClass().getResource("/tileset/DonMedusaShot_Left.png"));	
			}
		}
		/*Case Right*/
		if((Character.y+Character.step==y|| Character.y-Character.step==y) && x<Character.x && type==11){
			img=Level.game_tileset[14];
			inRange=true;
		}
		if(y==Character.y && x<Character.x){
			dir=Game.Direction.Right;
			inRange=true;
			if(type==11)img=Level.game_tileset[14];//change to awaken medusa;
			if(!Object_inBetween(dir)){
				shoot=true;
				if(type==11)
				projectile_img=ImageIO.read(getClass().getResource("/tileset/medusa_shot_right.png"));
				if(type==22)
				projectile_img=ImageIO.read(getClass().getResource("/tileset/DonMedusaShot_Right.png"));	
			}
		}
		if(shoot){
			myProjectile=new Projectile(x,y,projectile_img,dir);
			Sound.MedusaSound.start();
			canShoot=false;
			myProjectile.projectile_speed=6;
			//dir=null;
		}
		if(!inRange){
			img=Level.game_tileset[7];
		}
	}
	private boolean Object_inBetween(Game.Direction dir) {
		switch(dir){
		case Down:	for(Tile aTile:Level.map_tile){
						for(int i=y;i<=Character.y;i+=2){
							if(aTile.x==x || aTile.x+Character.step==x || aTile.x-Character.step==x)
								if(aTile.y==i && aTile!=this)
									if(aTile.getType()!=6)//projectile bypass tree
										return true;
							}
						}
					break;
		case Up:	for(Tile aTile:Level.map_tile){
						for(int i=y;i>=Character.y;i-=2){
							if(aTile.x==x || aTile.x+Character.step==x || aTile.x-Character.step==x)
								if(aTile.y==i && aTile!=this)
									if(aTile.getType()!=6)//projectile bypass tree
										return true;
							}
						}		
					break;
		case Left:	for(Tile aTile:Level.map_tile){
						for(int i=x;i>=Character.x;i-=2){
							if(aTile.y==y || aTile.y+Character.step==y || aTile.y-Character.step==y)
								if(aTile.x==i && aTile!=this)
									if(aTile.getType()!=6)//projectile bypass tree
										return true;
							}
						}		
					break;
		case Right:	for(Tile aTile:Level.map_tile){
						for(int i=x;i<=Character.x;i+=2){
							if(aTile.y==y || aTile.y+Character.step==y || aTile.y-Character.step==y)
								if(aTile.x==i && aTile!=this)
									if(aTile.getType()!=6)//projectile bypass tree
										return true;
							}
						}		
					break;
		}
		return false;
	}
	private boolean LineofSight() {
		switch(dir){
		
		case Down: if((Character.x+Character.step==x || Character.x==x || Character.x-Character.step==x) && y<Character.y)
						return true;
					break;
		case Up:	if((x-Character.step==Character.x || x==Character.x|| x+Character.step==Character.x) && y>Character.y)
						return true;
					break;
		case Left:	if((Character.y-Character.step==y || Character.y+Character.step==y ||y==Character.y) && x>Character.x)
						return true;
					break;
		case Right:	if((Character.y-Character.step==y || Character.y+Character.step==y || y==Character.y) && x<Character.x)
						return true;
					break;
		}
		return false;
	}
	private void updateLocation() {
		switch(dir){
		case Left:	x-=12;
					break;
		case Right:	x+=12;
					break;
		case Down:	y+=12;
					break;
		case Up:	y-=12;
					break;
		}
	}
	public boolean isOffScreen(){
		if(x>Level.map_width || x<0 || y<0 || y>Level.map_height){
			Tile aTile=this;
			aTile.x=oldX;
			aTile.y=oldY;
			aTile.type=oldtype;
			aTile.img=old_img;	
			aTile.isMovingAcrossScreen=false;
			aTile.TransformedState=0;
			aTile.dir=old_dir;
			Level.addRespawn(aTile);
			aTile.updateMask();
			Level.toRemove.add(aTile);//remove the tile if it goes offscreen
			return true;
		}
		return false;
	}
	public void updateAnimation(){
		if(isMonster && TransformedState==0)
			if((System.nanoTime()-last_animation_update)/nano>nextFrame){
				last_animation_update=System.nanoTime();
				index++;
				if(index==maxFrame)
					index=0;
			}
	}
	/* Check the state of the Tile and revert it back to its original image 10second after being shot*/
	private void checkState() {
		if((System.nanoTime()-time_since_transform)/nano>7000 && TransformedState==1){
			TransformedState=2;
			img=Level.monsterState[1];
		}
		if((System.nanoTime()-time_since_transform)/nano>10000 && TransformedState==2){
			TransformedState=0;
			type=1;
			img=previousState;
			old_img=img;
		}
			
		
	}
	public boolean isMonster() {
		return isMonster;
	}
	public void transform() {
		previousState=img;
		type=2;
		img=Level.monsterState[0];
		time_since_transform=System.nanoTime();	
		TransformedState=1;
	}
	public void setAnimation(BufferedImage anim_sheet,int maxFrame,int next){
		nextFrame=next;
		animation=new Animation(maxFrame,nextFrame);
		this.maxFrame=maxFrame;
		 for(int i=0;i<1;i++){//all animation on same row
			 for(int j=0;j<maxFrame;j++){
				animation.animation[(i*maxFrame)+j]=anim_sheet.getSubimage(j*width, i*height, width, height);
			 }
		 }
	}
	public void moveAcross_Screen(Game.Direction direction){
		shape.reset();//tile can now pass through everything
		isMovingAcrossScreen = true;
		old_dir=dir;
		dir=direction;
		Sound.ShotSound.stop();
		Sound.MonsterDestroyed.stop();
		Sound.MonsterDestroyed.setFramePosition(0);
		Sound.MonsterDestroyed.start();
	}
	public void setType(int i) {
		type=i;	
	}

}
