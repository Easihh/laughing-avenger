import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;

public class Tile implements Comparable<Tile> {
	enum ID{
		Rock(0),ClosedDoor(1),AmmoHeart(2),NoAmmoHeart(3),OpenDoor(4),Character(5),GolRightAwaken(6),SleepMedusa(7),
		MoveableBlock(8),Tree(9),ClosedChest(10),BottomChestOpen(11),GolDownAwaken(12),GolLeftAwaken(13),MedusaAwaken(14),
		OneWayUp(15),TopChest(16),BottomChestEmpty(17),LeftSnakey(18),RightSnakey(19),OneWayLeft(20),OneWayRight(21),
		LeftRightDonMedusa(22),UpDownDonMedusa(23),GolUp(24),GolDown(25),GolLeft(26),GolRight(27),OneWayDown(28),RockWall(29),
		Leeper(31),Water(32),LeftLadder(33),Phantom(34),Skull(35),RightLadder(40),UpDownLadder(41),Sand(42),Grass(43),Alma(48),
		Lava(49);
		int value;
        private ID(int value) {
            this.value = value;
        }
	}
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
		img=Game.game_tileset.get(type);
		setSolidState();
		setDepth();
		int[] xpoints={x,x+width,x+width,x};
		int[] ypoints={y,y,y+height,y+height};
		shape=new Polygon(xpoints, ypoints, 4);
	}
	private void setSolidState() {
		switch(type){
		case 33:
		case 40:
		case 41:isSolid=false;
				break;
		}
		
	}
	private void setDepth() {
		switch(type){
		case 8: depth=2;
				break;
		case 32:depth=-1;
				break;
		}
		
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
							if(collision_tile!=null && collision_tile.getType()==ID.OneWayLeft.value){
								if(checkArrow(ID.OneWayLeft.value))
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
						if(collision_tile!=null && collision_tile.getType()==ID.OneWayLeft.value){
							if(checkArrow(ID.OneWayLeft.value))
								willMove=false;
						}
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
						getCollidingTile(new Rectangle(x+32,y+16,16,16));//bottom part collision
						if(collision_tile!=null && collision_tile.getType()==ID.OneWayLeft.value){
							if(checkArrow(ID.OneWayLeft.value))
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
						if(collision_tile!=null && collision_tile.getType()==ID.OneWayUp.value){
							if(checkArrow(ID.OneWayUp.value))
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
						if(collision_tile!=null && collision_tile.getType()==ID.OneWayUp.value)
							if(checkArrow(ID.OneWayUp.value))
									willMove=false;
						getCollidingTile(new Rectangle(x+16,y+32,16,16));//bottom right part collision
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
						if(collision_tile!=null && collision_tile.getType()==ID.OneWayUp.value)
							if(checkArrow(ID.OneWayUp.value))
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
						if(collision_tile!=null && collision_tile.getType()==Tile.ID.OneWayDown.value){
							if(checkArrow(Tile.ID.OneWayDown.value))
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
						if(collision_tile!=null && collision_tile.getType()==Tile.ID.OneWayDown.value){
							if(checkArrow(Tile.ID.OneWayDown.value))
								willMove=false;
						}
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
						getCollidingTile(new Rectangle(x+16,y-16,16,16));//top right part collision
						if(collision_tile!=null && collision_tile.getType()==Tile.ID.OneWayDown.value){
							if(checkArrow(Tile.ID.OneWayDown.value))
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
						if(collision_tile!=null && collision_tile.getType()==Tile.ID.OneWayRight.value){
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
						if(collision_tile!=null && collision_tile.getType()==Tile.ID.OneWayRight.value){
							if(checkArrow(Tile.ID.OneWayRight.value))
								willMove=false;
						}
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
						getCollidingTile(new Rectangle(x-16,y+16,16,16));//bottom left part collision			
						if(collision_tile!=null && collision_tile.getType()==Tile.ID.OneWayRight.value){
							if(checkArrow(Tile.ID.OneWayRight.value))
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
			if(Level.map_tile.get(i).getType()==Tile.ID.Water.value)
				if(Level.map_tile.get(i).shape.intersects(mask1) && Level.map_tile.get(i).shape.intersects(mask1))
					return true;
		}
		return false;
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
		case 15:	//one-way up arrow
					if(y<=collision_tile.y)
						return true;
					break;
		case 20:	if(x<=collision_tile.x)
						return true;
					break;
		case 21:	if(x>=collision_tile.x)
						return true;
					break;
		case 28:  	if(y>=collision_tile.y)
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
}
