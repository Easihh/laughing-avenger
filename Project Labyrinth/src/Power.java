import java.awt.Rectangle;

public class Power {
	private Tile colliding_tile1=null;
	private Tile colliding_tile2=null;
	public boolean powerActivated_hammer=false;
	public boolean powerActivated_ladder=false;
	public boolean powerActivated_arrow=false;
	public Power(){}
	public	void useLadder() {
		Sound.ArrowBridgePowerUsed.setFramePosition(0);
		switch(Character.dir){
		case Up:	colliding_tile1=getCollidingTile(new Rectangle(Character.x,Character.y-Character.step,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x+Character.step,Character.y-Character.step,16,16));
					checkWaterCollision(Tile.ID.UpDownLadder.value);
					break;
		case Down:	colliding_tile1=getCollidingTile(new Rectangle(Character.x,Character.y+32,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x+Character.step,Character.y+32,16,16));
					checkWaterCollision(Tile.ID.UpDownLadder.value);
					break;
		case Left:	colliding_tile1=getCollidingTile(new Rectangle(Character.x-Character.step,Character.y,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x-Character.step,Character.y+Character.step,16,16));
					checkWaterCollision(Tile.ID.LeftLadder.value);
					break;
		case Right:	colliding_tile1=getCollidingTile(new Rectangle(Character.x+32,Character.y,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x+32,Character.y+Character.step,16,16));
					checkWaterCollision(Tile.ID.RightLadder.value);
					break;
			}
	}
	private void checkWaterCollision(int type) {
		if(colliding_tile1 instanceof Water && colliding_tile2 instanceof Water)
			if(colliding_tile1==colliding_tile2)
				for(Tile aTile:Level.map_tile){
					if(aTile==colliding_tile1){
						Level.toDelete.add(aTile);
						Level.toAdd.add(new Tile(aTile.x,aTile.y,type));
						Sound.ArrowBridgePowerUsed.start();
						//powerActivated_ladder=false;
						deletePower(2);
					}
				}
			}
	
	public void useHammer() {
		Sound.HammerPowerUsed.setFramePosition(0);
		switch(Character.dir){
		case Up:	colliding_tile1=getCollidingTile(new Rectangle(Character.x,Character.y-Character.step,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x+Character.step,Character.y-Character.step,16,16));
					checkHammerCollision();
					break;
		case Down:	colliding_tile1=getCollidingTile(new Rectangle(Character.x,Character.y+32,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x+Character.step,Character.y+32,16,16));
					checkHammerCollision();
					break;
		case Left:	colliding_tile1=getCollidingTile(new Rectangle(Character.x-Character.step,Character.y,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x-Character.step,Character.y+Character.step,16,16));
					checkHammerCollision();
					break;
		case Right:	colliding_tile1=getCollidingTile(new Rectangle(Character.x+32,Character.y,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x+32,Character.y+Character.step,16,16));
					checkHammerCollision();
					break;
			}
	}
	private void checkHammerCollision() {
		if(colliding_tile1!=null && colliding_tile2!=null)
			if(colliding_tile1.type==Tile.ID.Rock.value  && colliding_tile2.type==Tile.ID.Rock.value )//rock
				if(colliding_tile1==colliding_tile2)
					for(Tile aTile:Level.map_tile){
						if(aTile==colliding_tile1){
							Level.toDelete.add(aTile);
							Sound.HammerPowerUsed.start();
							//powerActivated_hammer=false;
							deletePower(1);
						}
					}
				}
	
	public void useArrow() {
		Sound.ArrowBridgePowerUsed.setFramePosition(0);
		switch(Character.dir){
		case Up:	colliding_tile1=getCollidingTile(new Rectangle(Character.x,Character.y-Character.step,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x+Character.step,Character.y-Character.step,16,16));
					checkArrowCollision();
					break;
		case Down:	colliding_tile1=getCollidingTile(new Rectangle(Character.x,Character.y+32,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x+Character.step,Character.y+32,16,16));
					checkArrowCollision();
					break;
		case Left:	colliding_tile1=getCollidingTile(new Rectangle(Character.x-Character.step,Character.y,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x-Character.step,Character.y+Character.step,16,16));
					checkArrowCollision();
					break;
		case Right:	colliding_tile1=getCollidingTile(new Rectangle(Character.x+32,Character.y,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(Character.x+32,Character.y+Character.step,16,16));
					checkArrowCollision();
					break;
			}
	}
	private void checkArrowCollision() {
		if(colliding_tile1 instanceof OneWayArrow && colliding_tile2 instanceof OneWayArrow)
			if(colliding_tile1==colliding_tile2)
				for(Tile aTile:Level.map_tile){
					if(aTile==colliding_tile1){
						Level.toDelete.add(aTile);
						int type=getNewArrow(aTile);
						Level.toAdd.add((new OneWayArrow(aTile.x,aTile.y,type)));
						Sound.ArrowBridgePowerUsed.start();
						//powerActivated_arrow=false;
						deletePower(3);
					}
				}
	}
	private int getNewArrow(Tile aTile) {
		if(aTile.getType()==Tile.ID.OneWayUp.value)return Tile.ID.OneWayRight.value;
		if(aTile.getType()==Tile.ID.OneWayLeft.value)return Tile.ID.OneWayUp.value;
		if(aTile.getType()==Tile.ID.OneWayRight.value)return Tile.ID.OneWayDown.value;
		if(aTile.getType()==Tile.ID.OneWayDown.value)return Tile.ID.OneWayLeft.value;
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
