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
		Lava(49),WaterFlowDown(50),WaterFlowLeft(51),WaterFlowRight(58),WaterFlowUp(59),boat(65),background(99);
		int value;
        private ID(int value) {
            this.value = value;
        }
	}
	private boolean willMove;
	private Tile collision_tile=null;
	protected final int height=32;
	protected final int width=32;
	protected final int half_height=16;
	protected final int half_width=16;
	public int depth=1;
	public int oldX;
	public int oldY;
	public ID oldtype;
	public ID type;
	public boolean isSolid=true;;
	public boolean isMovingAcrossScreen = false;
	public Game.Direction dir;
	protected int x;
	protected int y;
	public Image img;
	public Polygon shape;
	public Game.Direction WaterDir;
	
	public Tile(int x, int y,ID type) {
		this.x=x;
		this.y=y;
		oldX=x;
		oldY=y;
		this.type=type;
		oldtype=type;
		img=Game.game_tileset.get(type.value);
		setSolidState();
		setDepth();
		updateMask();
	}
	private void setSolidState() {
		switch(type){
		case LeftLadder:
		case RightLadder:
		case UpDownLadder:
		case Sand:			isSolid=false;
							break;
		}
		
	}
	private void setDepth() {
		switch(type){
		case MoveableBlock: depth=2;
							break;
		case Water:
		case WaterFlowDown:
		case WaterFlowLeft:
		case WaterFlowRight:
		case WaterFlowUp:	depth=-1;
							break;
		}
		
	}
	public Tile(Image image) {
		img=image;
		type=ID.background;// we have a background
	}
	public void render(Graphics g) {
		g.setColor(Color.BLUE);
		if(type==ID.background){//draw background
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
		Character hero=Character.getInstance();
		switch(direction){
		case "Down":	for(Tile aTile:Level.map_tile){
						for(int i=y;i<=hero.getY();i+=2){
							if(aTile.x==x || Math.abs(aTile.x-hero.getX())<=hero.step)
								if(aTile.y==i && aTile!=this)
									if(aTile.isSolid && aTile.type!=Tile.ID.Tree && !(aTile instanceof Water)){ //bypass tree
										return true;
									}
							}
						}
					break;
		case "Up":	for(Tile aTile:Level.map_tile){
						for(int i=y;i>=hero.getY();i-=2){
							if(aTile.x==x || Math.abs(aTile.x-hero.getX())<=hero.step)
								if(aTile.y==i && aTile!=this)
									if(aTile.isSolid && aTile.type!=Tile.ID.Tree && !(aTile instanceof Water))
										return true;
							}
						}		
					break;
		case "Left":	for(Tile aTile:Level.map_tile){
						for(int i=x;i>=hero.getX();i-=2){
							if(aTile.y==y || Math.abs(aTile.y-hero.getY())<=hero.step)
								if(aTile.x==i && aTile!=this)
									if(aTile.isSolid && aTile.type!=Tile.ID.Tree && !(aTile instanceof Water))
										return true;
							}
						}		
					break;
		case "Right":	for(Tile aTile:Level.map_tile){
						for(int i=x;i<=hero.getX();i+=2){
							if(aTile.y==y || Math.abs(aTile.y-hero.getY())<=hero.step)
								if(aTile.x==i && aTile!=this)
									if(aTile.isSolid && aTile.type!=Tile.ID.Tree && !(aTile instanceof Water))
										return true;
							}
						}		
					break;
		}
		return false;
	}
	public void moveTile(int movement){
		//We are behind the big 32*32 block or Monster in Block form
		willMove=true;
		switch(Character.getInstance().dir){
		case Right:	if(!checkCollision(new Rectangle(x+width,y,half_width,half_height),new Rectangle(x+width,y+half_height,half_width,half_height))){
							checkBelowBlock(ID.OneWayLeft,Character.getInstance().dir);
							}
					else{
						getCollidingTile(new Rectangle(x+width,y,half_width,half_height));//top part collision
						checkPartialCollision(collision_tile,ID.OneWayLeft);
						getCollidingTile(new Rectangle(x+width,y+half_height,half_width,half_height));//bottom part collision
						checkPartialCollision(collision_tile,ID.OneWayLeft);
						if(isFullyCollidingWithWater(new Rectangle(x+width,y+half_height,half_width,half_height),new Rectangle(x+width,y,half_width,half_height))){
							if(this instanceof Monster)
								((Monster)this).moveInWater();
						}
					}
					break;
					
		case Down:	if(!checkCollision(new Rectangle(x,y+height,half_width,half_height),new Rectangle(x+half_width,y+height,half_width,half_height))){
						checkBelowBlock(ID.OneWayUp,Character.getInstance().dir);
					}else{//There is a collision Down.
						getCollidingTile(new Rectangle(x,y+height,half_width,half_height));//bottom left part collision
						checkPartialCollision(collision_tile,ID.OneWayUp);
						getCollidingTile(new Rectangle(x+half_width,y+height,half_width,half_height));//bottom right part collision
						checkPartialCollision(collision_tile,ID.OneWayUp);
						if(isFullyCollidingWithWater(new Rectangle(x+half_width,y+height,half_width,half_height),new Rectangle(x,y+height,half_width,half_height))){
							if(this instanceof Monster)
								((Monster)this).moveInWater();
						}
					}				
					break;
		case Up:
					if(!checkCollision(new Rectangle(x,y-half_height,half_width,half_height),new Rectangle(x+half_width,y-half_height,half_width,half_height))){
						checkBelowBlock(Tile.ID.OneWayDown,Character.getInstance().dir);
					}else{
						getCollidingTile(new Rectangle(x,y-half_height,half_width,half_height));//top left part collision
						checkPartialCollision(collision_tile,Tile.ID.OneWayDown);
						getCollidingTile(new Rectangle(x+half_width,y-half_height,half_width,half_height));//top right part collision
						checkPartialCollision(collision_tile,Tile.ID.OneWayDown);
						if(isFullyCollidingWithWater(new Rectangle(x,y-half_height,half_width,half_height),new Rectangle(x+half_width,y-half_height,half_width,half_height))){
							if(this instanceof Monster)
								((Monster)this).moveInWater();
						}
					}
					break;
		case Left:
					if(!checkCollision(new Rectangle(x-half_width,y,half_width,half_height),new Rectangle(x-half_width,y+half_height,half_width,half_height))){
						checkBelowBlock(Tile.ID.OneWayRight,Character.getInstance().dir);
					}else{
						getCollidingTile(new Rectangle(x-half_width,y,half_width,half_height));//top left part collision
						checkPartialCollision(collision_tile,Tile.ID.OneWayRight);
						getCollidingTile(new Rectangle(x-half_width,y+half_height,half_width,half_height));//bottom left part collision	
						checkPartialCollision(collision_tile,Tile.ID.OneWayRight);
						if(isFullyCollidingWithWater(new Rectangle(x-half_width,y+half_height,half_width,half_height),new Rectangle(x-half_width,y,half_width,half_height))){
							if(this instanceof Monster)
								((Monster)this).moveInWater();
						}
					}
					break;
		}
		if(willMove)moveCondition();
		int[] xpoints={x,x+width,x+width,x};
		int[] ypoints={y,y,y+height,y+height};
		shape=new Polygon(xpoints, ypoints, 4);
	}
	private void moveCondition() {
		if(this instanceof Monster && !((Monster)this).isDrowning)
			move(Character.getInstance().dir);
		if(this instanceof Monster && ((Monster)this).isDrowning){
			move(Character.getInstance().dir);
			Character.isPushing=false;
		}
		if(!(this instanceof Monster))
			move(Character.getInstance().dir);	
	}
	private void move(Game.Direction dir){
		if(dir==Game.Direction.Down || dir==Game.Direction.Right )
			Character.targetX=Character.getInstance().step;
		else Character.targetX=-Character.getInstance().step;
			Character.isMoving=true;
			Character.isPushing=true;
	}
	private void checkPartialCollision(Tile collision_tile, ID value) {
		if(collision_tile!=null && collision_tile.type==value){
			if(checkArrow(value))
				willMove=false;
		}
		if(collision_tile!=null && collision_tile.isSolid)
			willMove=false;
	}
	private void checkBelowBlock(ID value, Game.Direction dir) {
		getCollidingTile(new Rectangle(x,y,32,32));
		if(collision_tile!=null && collision_tile.type==value){
			if(checkArrow(value))
				willMove=false;
		}
	}
	private boolean isFullyCollidingWithWater(Rectangle mask1,
			Rectangle mask2) {
		for(int i=0;i<Level.map_tile.size();i++){
			if(Level.map_tile.get(i) instanceof Water){
				if(Level.map_tile.get(i).shape.intersects(mask1) && Level.map_tile.get(i).shape.intersects(mask2)){
					if(Level.map_tile.get(i).type==Tile.ID.WaterFlowDown)WaterDir=Game.Direction.Down;
					if(Level.map_tile.get(i).type==Tile.ID.WaterFlowUp)WaterDir=Game.Direction.Up;
					if(Level.map_tile.get(i).type==Tile.ID.WaterFlowLeft)WaterDir=Game.Direction.Left;
					if(Level.map_tile.get(i).type==Tile.ID.WaterFlowRight)WaterDir=Game.Direction.Right;
					if(Level.map_tile.get(i).type==Tile.ID.Water)WaterDir=Game.Direction.None;
					return true;
				}
			}
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
	private boolean checkArrow(ID type) {
		switch(type){
		case OneWayUp:		if(y<=collision_tile.y)
								return true;
							break;
		case OneWayLeft:	if(x<=collision_tile.x)
								return true;
							break;
		case OneWayRight:	if(x>=collision_tile.x)
								return true;
							break;
		case OneWayDown:	if(y>=collision_tile.y)
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
