import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.Rectangle;
/* Author Enrico Talbot
 * 
 * This is the Top Level Class where most of the Game object are derived from.*/

public class Tile implements Comparable<Tile> {
	/*Since most of the game object are actually a single tile in a grid, we give them ID to be able to 
	 * refer to them by name instead of value.
	 */
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
	protected final int height=32,width=32,half_height=16,half_width=16;
	public int depth=1,oldX,oldY;//depth is needed to draw object in order as 2 tile can occupy same space.
	public ID type,oldtype;// to keep track of what kind of Tile they are/were before changing to another type and revert back
	public boolean isSolid=true,isMovingAcrossScreen=false;//Monster shot twice within few second flies across the screen.
	public Game.Direction dir;
	protected int x,y;
	public Image img;
	public Polygon shape;//used to determine collision
	public Game.Direction WaterDir;//Used for Water Tile that have a wind direction.
	
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
		case Grass:
		case Sand:			isSolid=false;
							break;
		default:			break;
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
		default:			break;
		}
		
	}
	public Tile(Image image) {
		img=image;
		type=ID.background;// we have a background
	}
	public void render(Graphics g) {
		g.setColor(Color.BLUE);
		if(type==ID.background){//draw map filled with the background image
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
	/* Used to sort the Tile by their Depth before painting them by depth order.*/
	public int compareTo(Tile anotherTile) {
		if(depth<anotherTile.depth)return -1;
		if(depth==anotherTile.depth)return 0;
		return 1;
	}
	/* Monster that are shot twice must flies across the screen and not collide with anything*/
	public void moveAcross_Screen(Game.Direction direction){
		shape.reset();//tile can now pass through everything
		isMovingAcrossScreen = true;
		dir=direction;
		Sound.ShotSound.stop();
		Sound.MonsterDestroyed.stop();
		Sound.MonsterDestroyed.setFramePosition(0);
		Sound.MonsterDestroyed.start();
	}
	/* Search if there is an object between the direction of the tile and the Main Character and whether
	 * that object should act as a block or not in the path from the tile to the Character.The step of each
	 * Search should be the smallest step the main Character can move. 
	 */
	public boolean Object_inBetween(String direction) {
		Character hero=Labyrinth.hero;
		switch(direction){
		case "Down":	for(int i=0;i<Level.map_tile.size();i++){
						for(int j=y;j<=hero.y;j+=2){
							if(Level.map_tile.get(i).x==x || Math.abs(Level.map_tile.get(i).x-hero.x)<=hero.step)
								if(Level.map_tile.get(i).y==j && Level.map_tile.get(i)!=this)
									if(Level.map_tile.get(i).isSolid && Level.map_tile.get(i).type!=Tile.ID.Tree && !(Level.map_tile.get(i) instanceof Water)){ //bypass tree
										return true;
									}
							}
						}
					break;
		case "Up":	for(int i=0;i<Level.map_tile.size();i++){
						for(int j=y;j>=hero.y;j-=2){
							if(Level.map_tile.get(i).x==x || Math.abs(Level.map_tile.get(i).x-hero.x)<=hero.step)
								if(Level.map_tile.get(i).y==j && Level.map_tile.get(i)!=this)
									if(Level.map_tile.get(i).isSolid && Level.map_tile.get(i).type!=Tile.ID.Tree && !(Level.map_tile.get(i) instanceof Water))
										return true;
							}
						}		
					break;
		case "Left":	for(int i=0;i<Level.map_tile.size();i++){
						for(int j=x;j>=hero.x;j-=2){
							if(Level.map_tile.get(i).y==y || Math.abs(Level.map_tile.get(i).y-hero.y)<=hero.step)
								if(Level.map_tile.get(i).x==j && Level.map_tile.get(i)!=this)
									if(Level.map_tile.get(i).isSolid && Level.map_tile.get(i).type!=Tile.ID.Tree && !(Level.map_tile.get(i) instanceof Water))
										return true;
							}
						}		
					break;
		case "Right":	for(int i=0;i<Level.map_tile.size();i++){
						for(int j=x;j<=hero.x;j+=2){
							if(Level.map_tile.get(i).y==y || Math.abs(Level.map_tile.get(i).y-hero.y)<=hero.step)
								if(Level.map_tile.get(i).x==j && Level.map_tile.get(i)!=this)
									if(Level.map_tile.get(i).isSolid && Level.map_tile.get(i).type!=Tile.ID.Tree && !(Level.map_tile.get(i) instanceof Water))
										return true;
							}
						}		
					break;
		}
		return false;
	}
	/* This Method decides how things work when moving object such as Green Block or  Monster that 
	 * have been shot once.A Green Block should only be allowed to be moved if the Character is Fully 
	 * Colliding on either of the Four Side of the Block i.e partially colliding character with the 
	 * block will not be allowed to push it.
	 * 
	 * A Block or Monster can be moved into the Water only if they will be fully colliding with a 
	 * Water tile once pushed and only if they are not currently colliding with Water.
	 * 
	 * A Block or Monster may not be moved in the same direction as a One-Way Arrow object is pointing.
	 */
	public void moveTile(int movement){
		willMove=true;
		switch(Labyrinth.hero.dir){
		case Right:	if(!checkCollision(new Rectangle(x+width,y,half_width,half_height),new Rectangle(x+width,y+half_height,half_width,half_height))){
							checkBelowBlock(ID.OneWayLeft,Labyrinth.hero.dir);
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
						checkBelowBlock(ID.OneWayUp,Labyrinth.hero.dir);
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
						checkBelowBlock(Tile.ID.OneWayDown,Labyrinth.hero.dir);
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
						checkBelowBlock(Tile.ID.OneWayRight,Labyrinth.hero.dir);
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
		default:	break;
		}
		if(willMove)moveCondition();
		// Update The Collision Mask coordinates since the tile may have moved. 
		updateMask();
	}
	private void moveCondition() {
		if(this instanceof Monster && !((Monster)this).isDrowning)
			move(Labyrinth.hero.dir);
		if(this instanceof Monster && ((Monster)this).isDrowning){
			move(Labyrinth.hero.dir);
			Labyrinth.hero.isPushing=false;
		}
		if(!(this instanceof Monster))
			move(Labyrinth.hero.dir);	
	}
	private void move(Game.Direction dir){
		if(dir==Game.Direction.Down || dir==Game.Direction.Right )
			Labyrinth.hero.targetX=Labyrinth.hero.step;
		else Labyrinth.hero.targetX=-Labyrinth.hero.step;
		Labyrinth.hero.isMoving=true;
		Labyrinth.hero.isPushing=true;
	}
	private void checkPartialCollision(Tile collision_tile, ID value) {
		if(collision_tile!=null && collision_tile.type==value){
			if(checkArrow(value))
				willMove=false;
		}
		if(collision_tile!=null && collision_tile.isSolid)
			willMove=false;
	}
	/*This checks if the Block is currently  Completely Over a One-Way Arrow Tile and decides 
	whether it is allowed to move in that direction.*/
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
	
	/* this Method update the new location of the Monster when its flying across the screen at the update
	 * location speed.
	 */
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
		default:	break;
		}
	}
	/*In order to determine collision we use a Square Mask at a size equal to the tile size and we must
	update its coordinates should the tile moves.*/
	public void updateMask(){
		int[] xpoints={x,x+width,x+width,x};
		int[] ypoints={y,y,y+height,y+height};
		shape=new Polygon(xpoints, ypoints, 4);
	}
	/*Decides how a block should be allowed to move when facing different One-Way Arrow object*/
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
		default:			break;
		}
		return false;
	}
	private boolean getCollidingTile(Rectangle mask) {
		Tile thisTile=this;
		for(int i=0;i<Level.map_tile.size();i++){
			Tile aTile=Level.map_tile.get(i);
			if(aTile.shape.intersects(mask) && aTile!=thisTile){
					collision_tile=aTile;
					return true;
				}
			}
		collision_tile=null;
		return false;
	}
}
