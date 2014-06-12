import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

public class Tile implements Comparable<Tile> {
	protected final int height=32;
	protected final int width=32;
	protected int maxFrame=0;
	private int nextFrame=0;
	public int depth=1;
	public int oldX;
	public int oldY;
	public int oldtype;
	public int type;//1=rock 2=moveable green block,3=heartcard,4=closed goal,5=opened chest,6=tree,99=background
	public boolean canShoot=false;
	public Animation animation;
	public boolean isMovingAcrossScreen = false;
	public Game.Direction dir;
	protected int x;
	protected int y;
	public Image img;
	public Polygon shape;
	public BufferedImage projectile_img;
	public Tile collision_tile=null;
	public Tile(int x, int y,int type) {
		this.x=x;
		this.y=y;
		oldX=x;
		oldY=y;
		this.type=type;
		oldtype=type;
		getImage();
		int[] xpoints={x,x+width,x+width,x};
		int[] ypoints={y,y,y+height,y+height};
		shape=new Polygon(xpoints, ypoints, 4);
	}
	private void getImage() {
		switch(type){
		case 96: 	img=Level.game_tileset[1];//door closed
					break;
		case 97: 	img=Level.game_tileset[17];//chest bottom empty
					break;
		case 98: 	img=Level.game_tileset[16];//chest open top
					break;
		case 1: 	img=Level.game_tileset[0];//rock
					break;
		case 2: 	img=Level.game_tileset[8];//moveable block
					depth=2;
					break;	
		case 3: 	img=Level.game_tileset[2];//heart
					break;
		case 4: 	img=Level.game_tileset[10];//chest closed
					break;
		case 5: 	img=Level.game_tileset[11];//chest open bottom
					break;
		case 6: 	img=Level.game_tileset[9];//tree
					break;
		case 30: 	img=Level.game_tileset[29];//rockwall
					break;
		}
		
	}
	public Tile(Image image) {
		img=image;
		type=99;// we have a background
	}
	public void moveTile(int movement, Point pt1, Point pt2){
		switch(Character.dir){
		
		case Right:	if(!checkCollison(x+width,y,x+width,y+height-1) && Character.dir==Game.Direction.Right){
								searchOneWayArrow(x,y,x,y,x,y);
								if(collision_tile==null){
									Character.targetX=Character.step;
									Character.isMoving=true;
									Character.isPushing=true;
								}
								if(collision_tile!=null && collision_tile.getType()!=12){
									Character.targetX=Character.step;
									Character.isMoving=true;
									Character.isPushing=true;
								}
						}
					else{
						searchOneWayArrow(x+width,y+height-1,x+width,y,x,y);
						if(collision_tile!=null && !OneWayArrow(pt1,pt2)){//allow passage
							Character.targetX=Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
					}
					break;
					
		case Down:	if(!checkCollison(x,y+height,x+width-1,y+height)&& Character.dir==Game.Direction.Down){
						searchOneWayArrow(x,y,x,y,x,y);
						if(collision_tile==null){
							Character.targetX=Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
						if(collision_tile!=null && collision_tile.getType()!=11){
							Character.targetX=Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
					}
					else{
						searchOneWayArrow(x,y+height,x+width-1,y+height,x,y);
						if( collision_tile!=null && !OneWayArrow(pt1,pt2)){//allow passage
							Character.targetX=Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
					}
					break;
		case Up:
					if(!checkCollison(x,y-1,x+width-1,y-1)&& Character.dir==Game.Direction.Up){
						searchOneWayArrow(x,y,x,y,x,y);
						if(collision_tile==null){
							Character.targetX=-Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
						if(collision_tile!=null && collision_tile.getType()!=14){
							Character.targetX=-Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
					}
					else{
						searchOneWayArrow(x,y-1,x+width-1,y-1,x,y+height-1);
						if(collision_tile!=null && !OneWayArrow(pt1,pt2)){//allow passage
							Character.targetX=-Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
					}
					break;
		case Left:
					if(!checkCollison(x-1,y,x-1,y+height-1)&& Character.dir==Game.Direction.Left){
						searchOneWayArrow(x,y,x,y,x,y);
						if(collision_tile==null){
							Character.targetX=-Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
						if(collision_tile!=null && collision_tile.getType()!=13){
							Character.targetX=-Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
					}
					else{
						searchOneWayArrow(x-1,y,x-1,y+height-1,x+width-1,y);
						if(collision_tile!=null && !OneWayArrow(pt1,pt2)){//allow passage
							Character.targetX=-Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
					}
					break;
		}
		int[] xpoints={x,x+width,x+width,x};
		int[] ypoints={y,y,y+height,y+height};
		shape=new Polygon(xpoints, ypoints, 4);
	}
	private boolean searchOneWayArrow(int x1,int y1,int x2,int y2,int x3,int y3) {
		for(Tile aTile:Level.map_tile){
			if(aTile.shape.contains(x1,y1)|| aTile.shape.contains(x2,y2) || 
					aTile.shape.contains(x3,y3)){
				if(aTile instanceof OneWayArrow){
					collision_tile=aTile;
					return true;
				}
			}
		}
		collision_tile=null;
		return false;
	}
	private boolean checkCollison(int x1,int y1,int x2,int y2) {
		for(Tile aTile:Level.map_tile){
			if(aTile.shape.contains(x1,y1)|| aTile.shape.contains(x2,y2)){
				return true;
			}
		}
		return false;
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
		else
			g.drawImage(img,x,y,width,height,null);
	}
	public void move() {
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
	protected boolean Object_inBetween(String direction) {
		switch(direction){
		case "Down":	for(Tile aTile:Level.map_tile){
						for(int i=y;i<=Character.y;i+=2){
							if(aTile.x==x || aTile.x+Character.step==x || aTile.x-Character.step==x)
								if(aTile.y==i && aTile!=this)
									if(aTile.getType()!=6)//projectile bypass tree
										return true;
							}
						}
					break;
		case "Up":	for(Tile aTile:Level.map_tile){
						for(int i=y;i>=Character.y;i-=2){
							if(aTile.x==x || aTile.x+Character.step==x || aTile.x-Character.step==x)
								if(aTile.y==i && aTile!=this)
									if(aTile.getType()!=6)//projectile bypass tree
										return true;
							}
						}		
					break;
		case "Left":	for(Tile aTile:Level.map_tile){
						for(int i=x;i>=Character.x;i-=2){
							if(aTile.y==y || aTile.y+Character.step==y || aTile.y-Character.step==y)
								if(aTile.x==i && aTile!=this)
									if(aTile.getType()!=6)//projectile bypass tree
										return true;
							}
						}		
					break;
		case "Right":	for(Tile aTile:Level.map_tile){
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
	public void updateLocation() {
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
	/* Check the state of the Tile and revert it back to its original image 10second after being shot*/
	public void setAnimation(BufferedImage anim_sheet,int maxFrame){
		//nextFrame=next;
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
		dir=direction;
		Sound.ShotSound.stop();
		Sound.MonsterDestroyed.stop();
		Sound.MonsterDestroyed.setFramePosition(0);
		Sound.MonsterDestroyed.start();
	}
	public void setType(int i) {
		type=i;	
	}
	private boolean OneWayArrow(Point pt1, Point pt2) {
		if(Character.dir==Game.Direction.Down){
			if(collision_tile.getType()==11){
				if(collision_tile.shape.contains(x,y+height-1) || collision_tile.shape.contains(x+width,y+height-1))
					return false;// pass through
				return true; 	
			}
			return false;
		}
		if(Character.dir==Game.Direction.Up){
			if(collision_tile.getType()==14){
				if(collision_tile.shape.contains(x,y) || collision_tile.shape.contains(x+width,y))
					return false;
				return true;		
			}
			return false;
		}
		if(Character.dir==Game.Direction.Left){
			if(collision_tile.getType()==13){
				if(collision_tile.shape.contains(x,y) || collision_tile.shape.contains(x,y+height))
					return false;
				return true;
				}
			return false;
		}
		if(Character.dir==Game.Direction.Right){
			if(collision_tile.getType()==12){
				if(collision_tile.shape.contains(x+width-1,y) || collision_tile.shape.contains(x+width-1,y+height-1))
					return false;
				return true;
			}
			return false;
		}
		return false;
	}
	@Override
	public int compareTo(Tile anotherTile) {
		if(depth<anotherTile.depth)return -1;
		if(depth==anotherTile.depth)return 0;
		return 1;
	}
}
