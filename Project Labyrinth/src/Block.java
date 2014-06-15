import java.awt.Polygon;


public class Block extends Tile{

	public Block(int x, int y, int type) {
		super(x, y, type);
		img=Level.game_tileset[8];
		depth=2;
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
}
