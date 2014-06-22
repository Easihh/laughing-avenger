import java.util.Collections;


public class Power {
	Tile colliding_tile1=null;
	Tile colliding_tile2=null;
	Tile toDelete=null;
	Tile toAdd=null;
	public Power(){
		Sound.HammerPowerUsed.setFramePosition(0);
		Sound.ArrowBridgePowerUsed.setFramePosition(0);
	}
	public	void useLadder() {	
		switch(Character.dir){
		case Up:	colliding_tile1=getCollidingTile(Character.x,Character.y-1);
					colliding_tile2=getCollidingTile(Character.x+32-1,Character.y-1);
					if(colliding_tile1 instanceof Water && colliding_tile2 instanceof Water)
						if(colliding_tile1==colliding_tile2)
							for(Tile aTile:Level.map_tile){
								if(aTile==colliding_tile1){
									toDelete=aTile;
									toAdd=new Tile(Character.x,Character.y-32,91);
								}
							}
					if(toDelete!=null){
						Level.map_tile.add(toAdd);
						Level.map_tile.remove(toDelete);
						Collections.sort(Level.map_tile);
						Sound.ArrowBridgePowerUsed.start();
						//Character.powerActivated_ladder=false;
						deletePower(2);
					}
					break;
		case Down:	colliding_tile1=getCollidingTile(Character.x+32-1,Character.y+32);
					colliding_tile2=getCollidingTile(Character.x,Character.y+32);
					if(colliding_tile1 instanceof Water && colliding_tile2 instanceof Water)
						if(colliding_tile1==colliding_tile2)
							for(Tile aTile:Level.map_tile){
								if(aTile==colliding_tile1){
									toDelete=aTile;
									toAdd=new Tile(Character.x,Character.y+32,91);
								}
							}
					if(toDelete!=null){
						Level.map_tile.add(toAdd);
						Level.map_tile.remove(toDelete);
						Collections.sort(Level.map_tile);
						Sound.ArrowBridgePowerUsed.start();
						//Character.powerActivated_ladder=false;
						deletePower(2);
					}
					break;
		case Left:	colliding_tile1=getCollidingTile(Character.x-1,Character.y);
					colliding_tile2=getCollidingTile(Character.x-1,Character.y+32-1);
					if(colliding_tile1 instanceof Water && colliding_tile2 instanceof Water)
						if(colliding_tile1==colliding_tile2)
							for(Tile aTile:Level.map_tile){
								if(aTile==colliding_tile1){
									toDelete=aTile;
									toAdd=new Tile(Character.x-32,Character.y,93);
								}
							}
					if(toDelete!=null){
						Level.map_tile.add(toAdd);
						Level.map_tile.remove(toDelete);
						Collections.sort(Level.map_tile);
						Sound.ArrowBridgePowerUsed.start();
						//Character.powerActivated_ladder=false;
						deletePower(2);
					}
					break;
		case Right:	colliding_tile1=getCollidingTile(Character.x+32,Character.y);
					colliding_tile2=getCollidingTile(Character.x+32,Character.y+32-1);
					if(colliding_tile1 instanceof Water && colliding_tile2 instanceof Water)
						if(colliding_tile1==colliding_tile2)
							for(Tile aTile:Level.map_tile){
								if(aTile==colliding_tile1){
									toDelete=aTile;
									toAdd=new Tile(Character.x+32,Character.y,92);
								}
							}
					if(toDelete!=null){
						Level.map_tile.add(toAdd);
						Level.map_tile.remove(toDelete);
						Collections.sort(Level.map_tile);
						Sound.ArrowBridgePowerUsed.start();
						//Character.powerActivated_ladder=false;
						deletePower(2);
					}
					break;
			}
	}
	public void useHammer() {
		switch(Character.dir){
		case Up:	colliding_tile1=getCollidingTile(Character.x,Character.y-1);
					colliding_tile2=getCollidingTile(Character.x+32-1,Character.y-1);
					if(colliding_tile1!=null && colliding_tile2!=null)
						if(colliding_tile1.getType()==1  && colliding_tile2.getType()==1)//rock
							if(colliding_tile1==colliding_tile2)
								for(Tile aTile:Level.map_tile){
									if(aTile==colliding_tile1){
										toDelete=aTile;
									}
								}
					if(toDelete!=null){;
						Level.map_tile.remove(toDelete);
						Collections.sort(Level.map_tile);
						Sound.HammerPowerUsed.start();
						Character.powerActivated_hammer=false;
						deletePower(1);
					}
					break;
		case Down:	colliding_tile1=getCollidingTile(Character.x+32-1,Character.y+32);
					colliding_tile2=getCollidingTile(Character.x,Character.y+32);
					if(colliding_tile1!=null && colliding_tile2!=null)
						if(colliding_tile1.getType()==1  && colliding_tile2.getType()==1)
							if(colliding_tile1==colliding_tile2)
								for(Tile aTile:Level.map_tile){
									if(aTile==colliding_tile1){
										toDelete=aTile;
									}
								}
					if(toDelete!=null){
						Level.map_tile.remove(toDelete);
						Collections.sort(Level.map_tile);
						Sound.HammerPowerUsed.start();
						Character.powerActivated_hammer=false;
						deletePower(1);
					}
					break;
		case Left:	colliding_tile1=getCollidingTile(Character.x-1,Character.y);
					colliding_tile2=getCollidingTile(Character.x-1,Character.y+32-1);
					if(colliding_tile1!=null && colliding_tile2!=null)
						if(colliding_tile1.getType()==1  && colliding_tile2.getType()==1)
							if(colliding_tile1==colliding_tile2)
								for(Tile aTile:Level.map_tile){
									if(aTile==colliding_tile1){
										toDelete=aTile;
									}
								}
					if(toDelete!=null){
						Level.map_tile.remove(toDelete);
						Collections.sort(Level.map_tile);
						Sound.HammerPowerUsed.start();
						Character.powerActivated_hammer=false;
						deletePower(1);
					}
					break;
		case Right:	colliding_tile1=getCollidingTile(Character.x+32,Character.y);
					colliding_tile2=getCollidingTile(Character.x+32,Character.y+32-1);
					if(colliding_tile1!=null && colliding_tile2!=null)
						if(colliding_tile1.getType()==1  && colliding_tile2.getType()==1)
							if(colliding_tile1==colliding_tile2)
								for(Tile aTile:Level.map_tile){
									if(aTile==colliding_tile1){
										toDelete=aTile;
									}
								}
					if(toDelete!=null){
						Level.map_tile.remove(toDelete);
						Collections.sort(Level.map_tile);
						Character.powerActivated_hammer=false;
						Sound.HammerPowerUsed.start();
						deletePower(1);
					}
					break;
			}
	}
	public void useArrow() {	
	}
	private Tile getCollidingTile(int x1,int y1) {
		for(Tile aTile:Level.map_tile){
			if(aTile.shape.contains(x1,y1)){
					return aTile;
				}	
			}
		return null;
	}
	private void deletePower(int type){
		for(int i=0;i<3;i++){//power array
			if(Level.Power[i]==type)
				Level.Power[i]=0;
		}
	}
}
