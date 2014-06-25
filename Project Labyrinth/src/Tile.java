import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;

public class Tile implements Comparable<Tile> {
	protected final int height=32;
	protected final int width=32;
	public int depth=1;
	public int oldX;
	public int oldY;
	public int oldtype;
	public int type;
	public boolean isSolid=true;;
	public boolean isMovingAcrossScreen = false;
	public Game.Direction dir;
	protected int x;
	protected int y;
	public Image img;
	public Polygon shape;
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
	public Tile(Image image) {
		img=image;
		type=99;// we have a background
	}
	public int getType(){
		return type;
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
	public boolean checkCollision(Rectangle mask1,Rectangle mask2) {
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i).shape.intersects(mask1)|| Level.map_tile.get(i).shape.intersects(mask2)){
				if(Level.map_tile.get(i).isSolid)return true;
				if(Level.map_tile.get(i) instanceof OneWayArrow)return true;
			}
		}
		return false;
	}
	@Override
	public int compareTo(Tile anotherTile) {
		if(depth<anotherTile.depth)return -1;
		if(depth==anotherTile.depth)return 0;
		return 1;
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
	public boolean Object_inBetween(String direction) {
		switch(direction){
		case "Down":	for(Tile aTile:Level.map_tile){
						for(int i=y;i<=Character.y;i+=2){
							if(aTile.x==x || Math.abs(aTile.x-Character.x)<=Character.step)
								if(aTile.y==i && aTile!=this)
									if(aTile.isSolid && aTile.getType()!=6){ //bypass tree
										return true;
									}
							}
						}
					break;
		case "Up":	for(Tile aTile:Level.map_tile){
						for(int i=y;i>=Character.y;i-=2){
							if(aTile.x==x || Math.abs(aTile.x-Character.x)<=Character.step)
								if(aTile.y==i && aTile!=this)
									if(aTile.isSolid && aTile.getType()!=6)
										return true;
							}
						}		
					break;
		case "Left":	for(Tile aTile:Level.map_tile){
						for(int i=x;i>=Character.x;i-=2){
							if(aTile.y==y || Math.abs(aTile.y-Character.y)<=Character.step)
								if(aTile.x==i && aTile!=this)
									if(aTile.isSolid && aTile.getType()!=6)
										return true;
							}
						}		
					break;
		case "Right":	for(Tile aTile:Level.map_tile){
						for(int i=x;i<=Character.x;i+=2){
							if(aTile.y==y || Math.abs(aTile.y-Character.y)<=Character.step)
								if(aTile.x==i && aTile!=this)
									if(aTile.isSolid && aTile.getType()!=6)
										return true;
							}
						}		
					break;
		}
		return false;
	}
	public void moveTile(int movement){
		//We are behind the big 32*32 block or Monster in Block form
		switch(Character.dir){
		case Right:	if(!checkCollision(new Rectangle(x+32,y,16,16),new Rectangle(x+32,y+16,16,16))){
							//check for one-way arrow Below the block
							boolean willMove=true;
							getCollidingTile(new Rectangle(x,y,32,32));
							if(collision_tile!=null && collision_tile.getType()==12){
								if(checkArrow(12))
									willMove=false;
							}
							if(willMove){
								if(this instanceof Monster){
									if(!((Monster)this).isDrowning){
										Character.targetX=Character.step;
										Character.isMoving=true;
										Character.isPushing=true;
															}
								}
								else{
										Character.targetX=Character.step;
										Character.isMoving=true;
										Character.isPushing=true;
									}
								}
							}
					else{//Collision right
						boolean willMove=true;
						getCollidingTile(new Rectangle(x+32,y,16,16));//top part collision
						if(collision_tile!=null && collision_tile.getType()==12){
							if(checkArrow(12))
								willMove=false;
						}
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
						getCollidingTile(new Rectangle(x+32,y+16,16,16));//bottom part collision
						if(collision_tile!=null && collision_tile.getType()==12){
							if(checkArrow(12))
								willMove=false;
						}
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
						if(isFullyCollidingWithWater(new Rectangle(x+32,y+16,16,16),new Rectangle(x+32,y,16,16))){
							if(this instanceof Monster)
								((Monster)this).moveInWater();
						}
						if(willMove){
							Character.targetX=Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
					}
					break;
					
		case Down:	if(!checkCollision(new Rectangle(x,y+32,16,16),new Rectangle(x+16,y+32,16,16))){
						boolean willMove=true;
						getCollidingTile(new Rectangle(x,y,32,32));
						if(collision_tile!=null && collision_tile.getType()==11){
							if(checkArrow(11))
								willMove=false;
							}
						if(willMove){
							if(this instanceof Monster){
								if(!((Monster)this).isDrowning){
									Character.targetX=Character.step;
									Character.isMoving=true;
									Character.isPushing=true;
														}
							}
							else{
									Character.targetX=Character.step;
									Character.isMoving=true;
									Character.isPushing=true;
								}
							}
					}else{//There is a collision Down.
						boolean willMove=true;
						getCollidingTile(new Rectangle(x,y+32,16,16));//bottom left part collision
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
						if(collision_tile!=null && collision_tile.getType()==11)
							if(checkArrow(11))
									willMove=false;
						getCollidingTile(new Rectangle(x+16,y+32,16,16));//bottom right part collision
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
						if(collision_tile!=null && collision_tile.getType()==11)
							if(checkArrow(11))
									willMove=false;
						if(isFullyCollidingWithWater(new Rectangle(x+16,y+32,16,16),new Rectangle(x,y+32,16,16))){
							if(this instanceof Monster)
								((Monster)this).moveInWater();
						}
						if(willMove){
							Character.targetX=Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
					}
						
					break;
		case Up:
					if(!checkCollision(new Rectangle(x,y-16,16,16),new Rectangle(x+16,y-16,16,16))){
						boolean willMove=true;
						getCollidingTile(new Rectangle(x,y,32,32));
						if(collision_tile!=null && collision_tile.getType()==14){
							if(checkArrow(14))
								willMove=false;
						}
						if(willMove){
							if(this instanceof Monster){
								if(!((Monster)this).isDrowning){
									Character.targetX=-Character.step;
									Character.isMoving=true;
									Character.isPushing=true;
														}
							}
							else{
									Character.targetX=-Character.step;
									Character.isMoving=true;
									Character.isPushing=true;
								}
							}
						}else{
						boolean willMove=true;
						getCollidingTile(new Rectangle(x,y-16,16,16));//top left part collision
						if(collision_tile!=null && collision_tile.getType()==14){
							if(checkArrow(14))
								willMove=false;
						}
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
						getCollidingTile(new Rectangle(x+16,y-16,16,16));//top right part collision
						if(collision_tile!=null && collision_tile.getType()==14){
							if(checkArrow(14))
								willMove=false;
						}
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
						if(isFullyCollidingWithWater(new Rectangle(x,y-16,16,16),new Rectangle(x+16,y-16,16,16))){
							if(this instanceof Monster)
								((Monster)this).moveInWater();
						}
						if(willMove){
							Character.targetX=-Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
					}
					break;
		case Left:
					if(!checkCollision(new Rectangle(x-16,y,16,16),new Rectangle(x-16,y+16,16,16))){
						boolean willMove=true;
						getCollidingTile(new Rectangle(x,y,32,32));
						if(collision_tile!=null && collision_tile.getType()==13){
							if(checkArrow(13))
								willMove=false;
						}
						if(willMove){
							if(this instanceof Monster){
								if(!((Monster)this).isDrowning){
									Character.targetX=-Character.step;
									Character.isMoving=true;
									Character.isPushing=true;
														}
							}
							else{
									Character.targetX=-Character.step;
									Character.isMoving=true;
									Character.isPushing=true;
								}
							}
					}else{
						boolean willMove=true;
						getCollidingTile(new Rectangle(x-16,y,16,16));//top left part collision
						if(collision_tile!=null && collision_tile.getType()==13){
							if(checkArrow(13))
								willMove=false;
						}
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
						getCollidingTile(new Rectangle(x-16,y+16,16,16));//bottom left part collision			
						if(collision_tile!=null && collision_tile.getType()==13){
							if(checkArrow(13))
								willMove=false;
						}
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
						if(isFullyCollidingWithWater(new Rectangle(x-16,y+16,16,16),new Rectangle(x-16,y,16,16))){
							if(this instanceof Monster)
								((Monster)this).moveInWater();
						}
						if(willMove){
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
	private boolean isFullyCollidingWithWater(Rectangle mask1,
			Rectangle mask2) {
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i).getType()==95)
				if(Level.map_tile.get(i).shape.intersects(mask1) && Level.map_tile.get(i).shape.intersects(mask1))
					return true;
		}
		return false;
	}
	public void setType(int i) {
		type=i;
	}
	public void update(){}
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
	public void updateMask(){
		int[] xpoints={x,x+width,x+width,x};
		int[] ypoints={y,y,y+height,y+height};
		shape=new Polygon(xpoints, ypoints, 4);
	}
	private boolean checkArrow(int type) {
		switch(type){
		case 11:	//one-way up arrow
					if(y<=collision_tile.y)
						return true;
					break;
		case 12:	if(x<=collision_tile.x)
						return true;
					break;
		case 13:	if(x>=collision_tile.x)
						return true;
					break;
		case 14:  	if(y>=collision_tile.y)
						return true;
					break;
		}
		return false;
	}
	private boolean getCollidingTile(Rectangle mask) {
		Tile thisTile=this;
		for(Tile aTile:Level.map_tile){
			if(aTile.shape.intersects(mask)){
				if(aTile!=thisTile){
					collision_tile=aTile;
					return true;
				}
			}
		}
		collision_tile=null;
		return false;
	}
	private void getImage() {
		switch(type){
		case 1: 	img=Level.game_tileset[0];//rock
					break;	
		case 2: 	img=Level.game_tileset[8];//moveable block
					depth=2;
					break;
		case 3: 	img=Level.game_tileset[2];//heart give ammo
					break;
		case 4: 	img=Level.game_tileset[10];//chest closed
					break;
		case 5: 	img=Level.game_tileset[11];//chest open bottom
					break;
		case 6: 	img=Level.game_tileset[9];//tree
					break;
		case 30: 	img=Level.game_tileset[29];//rockwall
					break;
		case 89: 	img=Level.game_tileset[42];//Sand
					isSolid=false;
					break;
		case 90: 	img=Level.game_tileset[43];//Grass
					isSolid=false;
					break;			
		case 91: 	img=Level.game_tileset[41];//up-down ladder
					depth=-1;
					isSolid=false;
					break;
		case 92: 	img=Level.game_tileset[40];//right ladder
					depth=-1;
					isSolid=false;
					break;
		case 93: 	img=Level.game_tileset[33];//left ladder
					depth=-1;
					isSolid=false;
					break;
		case 94: 	img=Level.game_tileset[2];//heart give no ammo
					break;
		case 96: 	img=Level.game_tileset[1];//door closed
					break;
		case 97: 	img=Level.game_tileset[17];//chest bottom empty
					break;
		case 98: 	img=Level.game_tileset[16];//chest open top
					break;			
		}
	}
}
