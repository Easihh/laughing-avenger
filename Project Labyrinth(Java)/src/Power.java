import java.awt.Rectangle;
/* Author Enrico Talbot
 * 
 * Class used for Main Character Special Power.Only Certain Level allow the usage of Power.
 * The type of Power allowed depends on the Map up to 3 power(same allowed) and obtained after
 * taking a certain number of heart cards on the Level(# depend on Level).Power can only be used
 * Once.
 * 
 * There are 3 powers that can used in the Game: Hammer, Ladder and One-Way Arrow changer.
 */
public class Power {
	private Tile colliding_tile1=null,colliding_tile2=null;
	private final int height=32,width=32,half_width=16,half_height=16;
	/*** Whether the hammer power is activated*/
	public boolean powerActivated_hammer=false;
	/*** Whether the ladder power is activated*/
	public boolean powerActivated_ladder=false;
	/*** Whether the arrow changer power is activated*/
	public boolean powerActivated_arrow=false;
	Character hero;
	public Power(){}
	/*** Ladder can only be used if the next Full tile infront of the Character is Water Type.*/
	public	void useLadder() {
		Sound.ArrowBridgePowerUsed.setFramePosition(0);
		findCollidingTile();
		switch(Labyrinth.hero.dir){
		case Up:	checkWaterCollision(Tile.ID.UpDownLadder);
					break;
		case Down:	checkWaterCollision(Tile.ID.UpDownLadder);
					break;
		case Left:	checkWaterCollision(Tile.ID.LeftLadder);
					break;
		case Right:	checkWaterCollision(Tile.ID.RightLadder);
					break;
		default:	break;
		}
	}
	/* Method used to determine what is the type of Tile infront of the Character and if its a 
	 * partial or full tile.
	 */
	private void findCollidingTile() {
		hero=Labyrinth.hero;
		switch(hero.dir){
		case Up:	colliding_tile1=getCollidingTile(new Rectangle(hero.x,hero.y-hero.step,half_width,half_height));
					colliding_tile2=getCollidingTile(new Rectangle(hero.x+hero.step,hero.y-hero.step,half_width,half_height));
					break;
		case Down:	colliding_tile1=getCollidingTile(new Rectangle(hero.x,hero.y+height,half_width,half_height));
					colliding_tile2=getCollidingTile(new Rectangle(hero.x+hero.step,hero.y+height,half_width,half_height));
					break;
		case Left:	colliding_tile1=getCollidingTile(new Rectangle(hero.x-hero.step,hero.y,half_width,half_height));
					colliding_tile2=getCollidingTile(new Rectangle(hero.x-hero.step,hero.y+hero.step,half_width,half_height));
					break;
		case Right:	colliding_tile1=getCollidingTile(new Rectangle(hero.x+width,hero.y,half_width,half_height));
					colliding_tile2=getCollidingTile(new Rectangle(hero.x+width,hero.y+hero.step,half_width,half_height));
					break;
		default:
			break;
			}
	}
	/* Method used to check if the Water tile infront of you is part of the same tile or not since
	 * we do not want to allow player to use ladder power in-between two water tile.
	 */
	private void checkWaterCollision(Tile.ID type) {
		if(colliding_tile1 instanceof Water && colliding_tile2 instanceof Water)
			if(colliding_tile1==colliding_tile2)
				for(int i=0;i<Level.map_tile.size();i++){
					Tile aTile=Level.map_tile.get(i);
					if(aTile==colliding_tile1){
						Level.toDelete.add(aTile);
						Level.toAdd.add(new Tile(aTile.x,aTile.y,type));
						Sound.ArrowBridgePowerUsed.start();
						deletePower(Game.SpecialPower.Ladder);
						disablePower(Game.SpecialPower.Ladder);				
					}
				}
			}
	/*** Ladder can only be used if the next Full tile infront of the Character is Rock Type.*/
	public void useHammer() {;
		Sound.HammerPowerUsed.setFramePosition(0);
		findCollidingTile();
		checkHammerCollision();
	}
	/* Method used to check if the Rock tile infront of you is part of the same tile or not since
	 * we do not want to allow player to use Hammer power in-between two rock tile.
	 */
	private void checkHammerCollision() {
		if(colliding_tile1!=null && colliding_tile2!=null)
			if(colliding_tile1.type==Tile.ID.Rock  && colliding_tile2.type==Tile.ID.Rock )//rock
				for(int i=0;i<Level.map_tile.size();i++){
					Tile aTile=Level.map_tile.get(i);
					if(aTile==colliding_tile1){
							Level.toDelete.add(aTile);
							Sound.HammerPowerUsed.start();
							deletePower(Game.SpecialPower.Hammer);
							disablePower(Game.SpecialPower.Hammer);
						}
					}
				}
	/*** Ladder can only be used if the next Full tile infront of the Character is Arrow Type.*/
	public void useArrow() {
		Sound.ArrowBridgePowerUsed.setFramePosition(0);
		findCollidingTile();
		checkArrowCollision();
	}
	/* Method used to check if the One-Way Arrow tile infront of you is part of the same tile or not 
	 * since we do not want to allow player to use Arrow power in-between two One-Way Arrow tile.
	 */
	private void checkArrowCollision() {
		if(colliding_tile1 instanceof OneWayArrow && colliding_tile2 instanceof OneWayArrow)
			if(colliding_tile1==colliding_tile2)
				for(int i=0;i<Level.map_tile.size();i++){
					Tile aTile=Level.map_tile.get(i);
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
	/* The Usage of the Arrow Power Changes One-Way Tile direction into its next 90 degrees direction 
	 * i.e. Up->Right->Down->Left->Up
	 */
	private Tile.ID getNewArrow(Tile aTile) {
		if(aTile.type==Tile.ID.OneWayUp)return Tile.ID.OneWayRight;
		if(aTile.type==Tile.ID.OneWayLeft)return Tile.ID.OneWayUp;
		if(aTile.type==Tile.ID.OneWayRight)return Tile.ID.OneWayDown;
		if(aTile.type==Tile.ID.OneWayDown)return Tile.ID.OneWayLeft;
		return null;
	}
	private Tile getCollidingTile(Rectangle mask) {
		for(int i=0;i<Level.map_tile.size();i++){
			Tile aTile=Level.map_tile.get(i);
			if(aTile.shape.intersects(mask))
				return aTile;
			}
		return null;
	}
	// Since its possible to have multiple of the same power, we only want to disable a given power
	//when there are no more of it in any of the 3 power slot.
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
				Labyrinth.hero.canShot=false;
				break;
			}
		}
	}
}
