import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;


public class Tile {
	 int x;
	 int y;
	private int width=24;
	private int height=24;
	private int index=0;
	private int maxFrame=0;
	private int nextFrame=0;
	private final long nano=1000000L;
	private long time_since_transform=0;
	private long last_animation_update=0;
	private int TransformedState=0;
	private Image previousState;
	private boolean isMonster=false;
	Image img;
	Animation animation;
	int type;//1=rock 2=moveable green block,3=heartcard,4=closed goal,5=opened chest,6=monster(worm),99=background
	public Polygon shape;
	public Tile(int x, int y,Image image,int type,boolean isMonster) {
		this.x=x;
		this.y=y;
		this.type=type;
		this.isMonster=isMonster;
		img=image;
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
		// TODO Auto-generated method stub
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
				if(isMonster && TransformedState==0)
					g.drawImage(animation.animation[index],x,y,width,height,null);
				else
				g.drawImage(img,x,y,width,height,null);
		}
		//g.drawPolygon(shape);
		updateAnimation();
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

}
