import java.awt.Rectangle;
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
		toDelete=null;
		toAdd=null;
		switch(Character.dir){
		case Up:	colliding_tile1=getCollidingTile(new Rectangle(Character.x,Character.y-Character.step,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x+Character.step,Character.y-Character.step,16,16));
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
		case Down:	colliding_tile1=getCollidingTile(new Rectangle(Character.x,Character.y+32,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x+Character.step,Character.y+32,16,16));
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
		case Left:	colliding_tile1=getCollidingTile(new Rectangle(Character.x-Character.step,Character.y,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x-Character.step,Character.y+Character.step,16,16));
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
		case Right:	colliding_tile1=getCollidingTile(new Rectangle(Character.x+32,Character.y,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x+32,Character.y+Character.step,16,16));
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
		toDelete=null;
		toAdd=null;
		switch(Character.dir){
		case Up:	colliding_tile1=getCollidingTile(new Rectangle(Character.x,Character.y-Character.step,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x+Character.step,Character.y-Character.step,16,16));
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
						//Character.powerActivated_hammer=false;
						deletePower(1);
					}
					break;
		case Down:	colliding_tile1=getCollidingTile(new Rectangle(Character.x,Character.y+32,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x+Character.step,Character.y+32,16,16));
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
						//Character.powerActivated_hammer=false;
						deletePower(1);
					}
					break;
		case Left:	colliding_tile1=getCollidingTile(new Rectangle(Character.x-Character.step,Character.y,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x-Character.step,Character.y+Character.step,16,16));
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
						//Character.powerActivated_hammer=false;
						deletePower(1);
					}
					break;
		case Right:	colliding_tile1=getCollidingTile(new Rectangle(Character.x+32,Character.y,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x+32,Character.y+Character.step,16,16));
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
						//Character.powerActivated_hammer=false;
						Sound.HammerPowerUsed.start();
						deletePower(1);
					}
					break;
			}
	}
	public void useArrow() {
		toDelete=null;
		toAdd=null;
		switch(Character.dir){
		case Up:	colliding_tile1=getCollidingTile(new Rectangle(Character.x,Character.y-Character.step,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x+Character.step,Character.y-Character.step,16,16));
					if(colliding_tile1 instanceof OneWayArrow && colliding_tile2 instanceof OneWayArrow)
						if(colliding_tile1==colliding_tile2)
							for(Tile aTile:Level.map_tile){
								if(aTile==colliding_tile1){
									toDelete=aTile;
									int type=getNewArrow(aTile);
									toAdd=new OneWayArrow(aTile.x,aTile.y,type);
								}
							}
					if(toDelete!=null){
						Level.map_tile.add(toAdd);
						Level.map_tile.remove(toDelete);
						Collections.sort(Level.map_tile);
						Sound.ArrowBridgePowerUsed.start();
						//Character.powerActivated_arrowr=false;
						deletePower(3);
					}
					break;
		case Down:	colliding_tile1=getCollidingTile(new Rectangle(Character.x,Character.y+32,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x+Character.step,Character.y+32,16,16));
					if(colliding_tile1 instanceof OneWayArrow && colliding_tile2 instanceof OneWayArrow)
						if(colliding_tile1==colliding_tile2)
							for(Tile aTile:Level.map_tile){
								if(aTile==colliding_tile1){
									toDelete=aTile;
									int type=getNewArrow(aTile);
									toAdd=new OneWayArrow(aTile.x,aTile.y,type);
								}
							}
					if(toDelete!=null){
						Level.map_tile.add(toAdd);
						Level.map_tile.remove(toDelete);
						Collections.sort(Level.map_tile);
						Sound.ArrowBridgePowerUsed.start();
						//Character.powerActivated_ladder=false;
						deletePower(3);
					}
					break;
		case Left:	colliding_tile1=getCollidingTile(new Rectangle(Character.x-Character.step,Character.y,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x-Character.step,Character.y+Character.step,16,16));
					if(colliding_tile1 instanceof OneWayArrow && colliding_tile2 instanceof OneWayArrow)
						if(colliding_tile1==colliding_tile2)
							for(Tile aTile:Level.map_tile){
								if(aTile==colliding_tile1){
									int type=getNewArrow(aTile);
									toDelete=aTile;
									toAdd=new OneWayArrow(aTile.x,aTile.y,type);
								}
							}
					if(toDelete!=null){
						Level.map_tile.add(toAdd);
						Level.map_tile.remove(toDelete);
						Collections.sort(Level.map_tile);
						Sound.ArrowBridgePowerUsed.start();
						//Character.powerActivated_ladder=false;
						deletePower(3);
					}
					break;
		case Right:	colliding_tile1=getCollidingTile(new Rectangle(Character.x+32,Character.y,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x+32,Character.y+Character.step,16,16));
					if(colliding_tile1 instanceof OneWayArrow && colliding_tile2 instanceof OneWayArrow)
						if(colliding_tile1==colliding_tile2)
							for(Tile aTile:Level.map_tile){
								if(aTile==colliding_tile1){
									int type=getNewArrow(aTile);
									toDelete=aTile;
									toAdd=new OneWayArrow(aTile.x,aTile.y,type);
								}
							}
					if(toDelete!=null){
						Level.map_tile.add(toAdd);
						Level.map_tile.remove(toDelete);
						Collections.sort(Level.map_tile);
						Sound.ArrowBridgePowerUsed.start();
						//Character.powerActivated_ladder=false;
						deletePower(3);
					}
					break;
			}
	}
	private int getNewArrow(Tile aTile) {
		if(aTile.getType()==11)return 13;//up arrow -> right arrow
		if(aTile.getType()==12)return 11;//Left arrow -> up arrow
		if(aTile.getType()==13)return 14;//right arrow-> down arrow
		if(aTile.getType()==14)return 12;//down arrow-> left arrow
		return 0;
	}
	private Tile getCollidingTile(Rectangle mask) {
		for(Tile aTile:Level.map_tile){
			if(aTile.shape.intersects(mask)){
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
