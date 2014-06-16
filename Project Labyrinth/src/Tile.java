import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Polygon;

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
	public boolean checkCollison(int x1,int y1,int x2,int y2) {
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
	@Override
	public int compareTo(Tile anotherTile) {
		if(depth<anotherTile.depth)return -1;
		if(depth==anotherTile.depth)return 0;
		return 1;
	}
	private boolean getCollidingTile(int x1,int y1) {
		Tile thisTile=this;
		for(Tile aTile:Level.map_tile){
			if(aTile.shape.contains(x1,y1)){
				if(aTile!=thisTile){
					collision_tile=aTile;
					return true;
				}
			}
		}
		collision_tile=null;
		return false;
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
	public void moveTile(int movement){
		switch(Character.dir){
		case Right:	if(!checkCollison(x+width,y,x+width,y+height-1) && Character.dir==Game.Direction.Right){
						boolean willMove=true;
						getCollidingTile(x,y);
						if(collision_tile!=null && collision_tile.shape.contains(x,y) 
								&& collision_tile.getType()==12){
							if(checkArrow(12))//one way arrow left
								willMove=false;
						}
						getCollidingTile(x,y+height-1);
						if(collision_tile!=null && collision_tile.shape.contains(x,y+height) 
								&& collision_tile.getType()==12){
							if(checkArrow(12))
								willMove=false;
						}
						if(willMove){
							Character.targetX=Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
					}
					else{//Collision right
						boolean willMove=true;
						getCollidingTile(x+width,y+height-1);//top part collision
						if(collision_tile!=null && collision_tile.getType()==12){
							if(checkArrow(12))
								willMove=false;
						}
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
						getCollidingTile(x+width,y);//bottom part collision
						if(collision_tile!=null && collision_tile.getType()==12){
							if(checkArrow(12))
								willMove=false;
						}
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
						if(willMove){
							Character.targetX=Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
					}
					break;
					
		case Down:	if(!checkCollison(x,y+height,x+width-1,y+height)&& Character.dir==Game.Direction.Down){
						// no collision in front of the Block but its possible the block is  already colliding with
						//something
						boolean willMove=true;
						getCollidingTile(x,y);
						if(collision_tile!=null && collision_tile.shape.contains(x,y) 
								 && collision_tile.getType()==11){
							if(checkArrow(11))//one-way up arrow
								willMove=false;
						}
						getCollidingTile(x+width-1,y);
						if(collision_tile!=null && collision_tile.shape.contains(x+width-1,y) 
								 && collision_tile.getType()==11){
							if(checkArrow(11))
								willMove=false;
						}						
						if(willMove){
							Character.targetX=Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
					}else{//There is a collision Down.
						boolean willMove=true;
						getCollidingTile(x,y+height);//top part collision
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
						if(collision_tile!=null && collision_tile.getType()==11)
							if(checkArrow(11))
									willMove=false;
						getCollidingTile(x+width-1,y+height);//bottom part collision
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
						if(collision_tile!=null && collision_tile.getType()==11)
							if(checkArrow(11))
									willMove=false;
						if(willMove){
							Character.targetX=Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
					}
						
					break;
		case Up:
					if(!checkCollison(x,y-1,x+width-1,y-1)&& Character.dir==Game.Direction.Up){
						boolean willMove=true;
						getCollidingTile(x,y);
						if(collision_tile!=null && collision_tile.shape.contains(x,y) 
								 && collision_tile.getType()==14){
							if(checkArrow(14))//one-way down arrow
								willMove=false;
						}	
						getCollidingTile(x+width-1,y);
						if(collision_tile!=null && collision_tile.shape.contains(x+width-1,y) 
								 && collision_tile.getType()==14){
							if(checkArrow(14))//one-way down arrow
								willMove=false;
						}	
						if(willMove){
							Character.targetX=-Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
						
					}else{
						boolean willMove=true;
						getCollidingTile(x,y-1);//top part collision
						if(collision_tile!=null && collision_tile.getType()==14){
							if(checkArrow(14))
								willMove=false;
						}
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
						getCollidingTile(x+width-1,y-1);//bottom part collision
						if(collision_tile!=null && collision_tile.getType()==14){
							if(checkArrow(14))
								willMove=false;
						}
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
						if(willMove){
							Character.targetX=-Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
					}
					break;
		case Left:
					if(!checkCollison(x-1,y,x-1,y+height-1)&& Character.dir==Game.Direction.Left){
						boolean willMove=true;
						getCollidingTile(x,y);
						if(collision_tile!=null && collision_tile.shape.contains(x,y) 
								 && collision_tile.getType()==13){
							if(checkArrow(13))//one-way down arrow
								willMove=false;
						}
						getCollidingTile(x,y+height-1);
						if(collision_tile!=null && collision_tile.shape.contains(x,y+height-1) 
								 && collision_tile.getType()==13){
							if(checkArrow(13))//one-way down arrow
								willMove=false;
						}
						if(willMove){
							Character.targetX=-Character.step;
							Character.isMoving=true;
							Character.isPushing=true;
						}
					}else{
						boolean willMove=true;
						getCollidingTile(x-1,y);//top part collision
						if(collision_tile!=null && collision_tile.getType()==13){
							if(checkArrow(13))
								willMove=false;
						}
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
						getCollidingTile(x-1,y+height-1);//bottom part collision
						if(collision_tile!=null && collision_tile.getType()==13){
							if(checkArrow(13))
								willMove=false;
						}
						if(collision_tile!=null && collision_tile.isSolid)
							willMove=false;
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
}
