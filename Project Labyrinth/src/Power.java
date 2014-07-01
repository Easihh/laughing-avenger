import java.awt.Rectangle;

public class Power {
	private Tile colliding_tile1=null;
	private Tile colliding_tile2=null;
	private final int height=32;
	private final int width=32;
	private final int half_width=16;
	private final int half_height=16;
	public boolean powerActivated_hammer=false;
	public boolean powerActivated_ladder=false;
	public boolean powerActivated_arrow=false;
	Character hero;
	public Power(){}
	public	void useLadder() {
		hero=Character.getInstance();
		Sound.ArrowBridgePowerUsed.setFramePosition(0);
		findCollidingTile();
		switch(Character.getInstance().dir){
		case Up:	checkWaterCollision(Tile.ID.UpDownLadder);
					break;
		case Down:	checkWaterCollision(Tile.ID.UpDownLadder);
					break;
		case Left:	checkWaterCollision(Tile.ID.LeftLadder);
					break;
		case Right:	checkWaterCollision(Tile.ID.RightLadder);
					break;
			}
	}
	private void findCollidingTile() {
		switch(Character.getInstance().dir){
		case Up:	colliding_tile1=getCollidingTile(new Rectangle(hero.getX(),hero.getY()-hero.step,half_width,half_height));
					colliding_tile2=getCollidingTile(new Rectangle(hero.getX()+hero.step,hero.getY()-hero.step,half_width,half_height));
					break;
		case Down:	colliding_tile1=getCollidingTile(new Rectangle(hero.getX(),hero.getY()+height,half_width,half_height));
					colliding_tile2=getCollidingTile(new Rectangle(hero.getX()+hero.step,hero.getY()+height,half_width,half_height));
					break;
		case Left:	colliding_tile1=getCollidingTile(new Rectangle(hero.getX()-hero.step,hero.getY(),half_width,half_height));
					colliding_tile2=getCollidingTile(new Rectangle(hero.getX()-hero.step,hero.getY()+hero.step,half_width,half_height));
					break;
		case Right:	colliding_tile1=getCollidingTile(new Rectangle(hero.getX()+width,hero.getY(),half_width,half_height));
					colliding_tile2=getCollidingTile(new Rectangle(hero.getX()+width,hero.getY()+hero.step,half_width,half_height));
					break;
			}
	}
	private void checkWaterCollision(Tile.ID type) {
		if(colliding_tile1 instanceof Water && colliding_tile2 instanceof Water)
			if(colliding_tile1==colliding_tile2)
				for(Tile aTile:Level.map_tile){
					if(aTile==colliding_tile1){
						Level.toDelete.add(aTile);
						Level.toAdd.add(new Tile(aTile.x,aTile.y,type));
						Sound.ArrowBridgePowerUsed.start();
						deletePower(Game.SpecialPower.Ladder);
						disablePower(Game.SpecialPower.Ladder);				
					}
				}
			}
	
	public void useHammer() {
		hero=Character.getInstance();
		Sound.HammerPowerUsed.setFramePosition(0);
		findCollidingTile();
		checkHammerCollision();
	}
	private void checkHammerCollision() {
		if(colliding_tile1!=null && colliding_tile2!=null)
			if(colliding_tile1.type==Tile.ID.Rock  && colliding_tile2.type==Tile.ID.Rock )//rock
				if(colliding_tile1==colliding_tile2)
					for(Tile aTile:Level.map_tile){
						if(aTile==colliding_tile1){
							Level.toDelete.add(aTile);
							Sound.HammerPowerUsed.start();
							deletePower(Game.SpecialPower.Hammer);
							disablePower(Game.SpecialPower.Hammer);
						}
					}
				}
	
	public void useArrow() {
		hero=Character.getInstance();
		Sound.ArrowBridgePowerUsed.setFramePosition(0);
		findCollidingTile();
		checkArrowCollision();
	}
	private void checkArrowCollision() {
		if(colliding_tile1 instanceof OneWayArrow && colliding_tile2 instanceof OneWayArrow)
			if(colliding_tile1==colliding_tile2)
				for(Tile aTile:Level.map_tile){
					if(aTile==colliding_tile1){
						Level.toDelete.add(aTile);
						Tile.ID type=getNewArrow(aTile);
						Level.toAdd.add((new OneWayArrow(aTile.x,aTile.y,type)));
						Sound.ArrowBridgePowerUsed.start();
						deletePower(Game.SpecialPower.ArrowChange);
						disablePower(Game.SpecialPower.ArrowChange);
					}
				}
	}
	private Tile.ID getNewArrow(Tile aTile) {
		if(aTile.type==Tile.ID.OneWayUp)return Tile.ID.OneWayRight;
		if(aTile.type==Tile.ID.OneWayLeft)return Tile.ID.OneWayUp;
		if(aTile.type==Tile.ID.OneWayRight)return Tile.ID.OneWayDown;
		if(aTile.type==Tile.ID.OneWayDown)return Tile.ID.OneWayLeft;
		return null;
	}
	private Tile getCollidingTile(Rectangle mask) {
		for(Tile aTile:Level.map_tile){
			if(aTile.shape.intersects(mask)){
					return aTile;
				}	
			}
		return null;
	}
	private void disablePower(Game.SpecialPower type){
		boolean powerExist=false;
		for(int i=0;i<3;i++){//max power allowed
			if(Level.Power[i]==type){
				powerExist=true;
				break;
			}
		}
		if(!powerExist && type==Game.SpecialPower.Hammer)powerActivated_hammer=false;
		if(!powerExist && type==Game.SpecialPower.Ladder)powerActivated_ladder=false;
		if(!powerExist && type==Game.SpecialPower.ArrowChange)powerActivated_arrow=false;
	}
	private void deletePower(Game.SpecialPower type){
		for(int i=0;i<3;i++){//max power allowed
			if(Level.Power[i]==type){
				Level.Power[i]=Game.SpecialPower.None;
				Character.getInstance().canShot=false;
				break;
			}
		}
	}
}
