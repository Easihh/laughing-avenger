import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;


public class Tile {
	private final int height=24;
	private final int width=24;
	private final long nano=1000000L;
	private boolean isMonster=false;
	private int index=0;
	private int maxFrame=0;
	private int nextFrame=0;
	private int oldX;
	private int oldY;
	private int oldtype;
	private int type;//1=rock 2=moveable green block,3=heartcard,4=closed goal,5=opened chest,6=tree,99=background
	private long last_animation_update=0;
	private long time_since_transform=0;
	private Image old_img;
	private Image previousState;
	public Projectile myProjectile;
	public boolean canShoot=false;
	
	public Animation animation;
	public boolean isMovingAcrossScreen = false;
	public Game.Direction dir;
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
				return true;
			}
		}
		return false;
	}
	public boolean isWalkable(){
		return(type==99 || type==4);
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
					if(maxFrame>0)
					g.drawImage(animation.animation[index],x,y,width,height,null);
					else
						g.drawImage(img,x,y,width,height,null);
					if(projectile_img!=null)//this monster can shoot
						fireProjectile(g);
				}
				else{
					if(isMovingAcrossScreen)
						updateLocation();
					if(!isOffScreen())
				g.drawImage(img,x,y,width,height,null);
				}
		}
		updateAnimation();
	}
	private void fireProjectile(Graphics g) {
		if(canShoot){
			if(LineofSight()){
				myProjectile=new Projectile(x,y,projectile_img,dir);
				canShoot=false;
				myProjectile.projectile_speed=3;
			}
		}
		if(myProjectile!=null && !canShoot){
			//System.out.println("X"+myProjectile.x);
			//System.out.println("Y"+myProjectile.y);
			myProjectile.render(g);
		}
		
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
		case Left:	x-=8;
					break;
		case Right:	x+=8;
					break;
		case Down:	y+=8;
					break;
		case Up:	y-=8;
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
	public void moveAcross_Screen(Game.Direction dir){
		type=4;//tile can now pass through everything
		isMovingAcrossScreen = true;
		this.dir=dir;
	}
	public void setType(int i) {
		type=i;	
	}

}
