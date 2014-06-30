import java.awt.Rectangle;

public class Power {
	private Tile colliding_tile1=null;
	private Tile colliding_tile2=null;
	public boolean powerActivated_hammer=false;
	public boolean powerActivated_ladder=false;
	public boolean powerActivated_arrow=false;
	Character hero;
	public Power(){}
	public	void useLadder() {
		hero=Character.getInstance();
		Sound.ArrowBridgePowerUsed.setFramePosition(0);
		switch(Character.getInstance().dir){
		case Up:	colliding_tile1=getCollidingTile(new Rectangle(hero.getX(),hero.getY()-hero.step,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(hero.getX()+hero.step,hero.getY()-hero.step,16,16));
					checkWaterCollision(Tile.ID.UpDownLadder.value);
					break;
		case Down:	colliding_tile1=getCollidingTile(new Rectangle(hero.getX(),hero.getY()+32,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(hero.getX()+hero.step,hero.getY()+32,16,16));
					checkWaterCollision(Tile.ID.UpDownLadder.value);
					break;
		case Left:	colliding_tile1=getCollidingTile(new Rectangle(hero.getX()-hero.step,hero.getY(),16,16));
					colliding_tile2=getCollidingTile(new Rectangle(hero.getX()-hero.step,hero.getY()+hero.step,16,16));
					checkWaterCollision(Tile.ID.LeftLadder.value);
					break;
		case Right:	colliding_tile1=getCollidingTile(new Rectangle(hero.getX()+32,hero.getY(),16,16));
					colliding_tile2=getCollidingTile(new Rectangle(hero.getX()+32,hero.getY()+hero.step,16,16));
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
						deletePower(2);
						disablePower(2);					
					}
				}
			}
	
	public void useHammer() {
		hero=Character.getInstance();
		Sound.HammerPowerUsed.setFramePosition(0);
		switch(Character.getInstance().dir){
		case Up:	colliding_tile1=getCollidingTile(new Rectangle(hero.getX(),hero.getY()-hero.step,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(hero.getX()+hero.step,hero.getY()-hero.step,16,16));
					checkHammerCollision();
					break;
		case Down:	colliding_tile1=getCollidingTile(new Rectangle(hero.getX(),hero.getY()+32,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(hero.getX()+hero.step,hero.getY()+32,16,16));
					checkHammerCollision();
					break;
		case Left:	colliding_tile1=getCollidingTile(new Rectangle(hero.getX()-hero.step,hero.getY(),16,16));
					colliding_tile2=getCollidingTile(new Rectangle(hero.getX()-hero.step,hero.getY()+hero.step,16,16));
					checkHammerCollision();
					break;
		case Right:	colliding_tile1=getCollidingTile(new Rectangle(hero.getX()+32,hero.getY(),16,16));
					colliding_tile2=getCollidingTile(new Rectangle(hero.getX()+32,hero.getY()+hero.step,16,16));
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
							deletePower(1);
							disablePower(1);
						}
					}
				}
	
	public void useArrow() {
		hero=Character.getInstance();
		Sound.ArrowBridgePowerUsed.setFramePosition(0);
		switch(Character.getInstance().dir){
		case Up:	colliding_tile1=getCollidingTile(new Rectangle(hero.getX(),hero.getY()-hero.step,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(hero.getX()+hero.step,hero.getY()-hero.step,16,16));
					checkArrowCollision();
					break;
		case Down:	colliding_tile1=getCollidingTile(new Rectangle(hero.getX(),hero.getY()+32,16,16));
					colliding_tile2=getCollidingTile(new Rectangle(hero.getX()+hero.step,hero.getY()+32,16,16));
					checkArrowCollision();
					break;
		case Left:	colliding_tile1=getCollidingTile(new Rectangle(hero.getX()-hero.step,hero.getY(),16,16));
					colliding_tile2=getCollidingTile(new Rectangle(hero.getX()-hero.step,hero.getY()+hero.step,16,16));
					checkArrowCollision();
					break;
		case Right:	colliding_tile1=getCollidingTile(new Rectangle(hero.getX()+32,hero.getY(),16,16));
					colliding_tile2=getCollidingTile(new Rectangle(hero.getX()+32,hero.getY()+hero.step,16,16));
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
						deletePower(3);
						disablePower(3);
					}
				}
	}
	private int getNewArrow(Tile aTile) {
		if(aTile.type==Tile.ID.OneWayUp.value)return Tile.ID.OneWayRight.value;
		if(aTile.type==Tile.ID.OneWayLeft.value)return Tile.ID.OneWayUp.value;
		if(aTile.type==Tile.ID.OneWayRight.value)return Tile.ID.OneWayDown.value;
		if(aTile.type==Tile.ID.OneWayDown.value)return Tile.ID.OneWayLeft.value;
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
	private void disablePower(int type){
		boolean powerExist=false;
		for(int i=0;i<3;i++){//power array
			if(Level.Power[i]==type){
				powerExist=true;
				break;
			}
		}
		if(!powerExist && type==1)powerActivated_hammer=false;
		if(!powerExist && type==2)powerActivated_ladder=false;
		if(!powerExist && type==3)powerActivated_arrow=false;
	}
	private void deletePower(int type){
		for(int i=0;i<3;i++){//power array
			if(Level.Power[i]==type){
				Level.Power[i]=0;
				Character.getInstance().canShot=false;
				break;
			}
		}
	}
}
