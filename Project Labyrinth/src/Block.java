import java.awt.Point;
import java.awt.Polygon;


public class Block extends Tile{

	public Block(int x, int y, int type) {
		super(x, y, type);
		img=Level.game_tileset[8];
		depth=2;
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
						if(collision_tile!=null && !OneWayArrow()){//allow passage
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
						if( collision_tile!=null && !OneWayArrow()){//allow passage
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
						if(collision_tile!=null && !OneWayArrow()){//allow passage
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
						if(collision_tile!=null && !OneWayArrow()){//allow passage
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
	private boolean OneWayArrow() {
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
}
