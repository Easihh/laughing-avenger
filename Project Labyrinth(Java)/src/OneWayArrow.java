import java.awt.Graphics;
public class OneWayArrow  extends Tile{

	public OneWayArrow(int x, int y, ID type) {
		super(x, y, type);
		isSolid=false;
		getImage();
	}
	/*** Render the Arrow Tile on screen */
	public void render(Graphics g){
		g.drawImage(img,x,y,null);	
	}
	private void getImage(){
		switch(type){
		case OneWayUp:		img=Game.game_tileset.get(Tile.ID.OneWayUp.value);
							break;
		case OneWayLeft:	img=Game.game_tileset.get(Tile.ID.OneWayLeft.value);
							break;
		case OneWayRight:	img=Game.game_tileset.get(Tile.ID.OneWayRight.value);
							break;
		case OneWayDown:	img=Game.game_tileset.get(Tile.ID.OneWayDown.value);
							break;
		default:
			break;
		}
	}
	/** Decides the Hero collision with One-Way Arrow.The hero may not be able to pass
	 * through a One-Way arrow if he is moving in a direction at the opposite of where the 
	 * One-Way arrow is pointing.i.e The hero cannot pass through a one way arrow by moving
	 * up if the One-Way Arrow is pointing down unless the Hero was fully colliding with a
	 * One-Way Arrow in a different direction.
	 */
	public boolean checkArrow() {
		Character hero=Labyrinth.hero;
			if(Labyrinth.hero.dir==Game.Direction.Down && type==Tile.ID.OneWayUp){
				if(shape.contains(hero.x,hero.y+height-1) || shape.contains(hero.x+width,hero.y+height-1))
					return false;//allow pass
				return true; 
			}
			if(Labyrinth.hero.dir==Game.Direction.Up && type==Tile.ID.OneWayDown){
				if(shape.contains(hero.x,hero.y) || shape.contains(hero.x+width,hero.y))
					return false;
				return true;
			}
			if(Labyrinth.hero.dir==Game.Direction.Left && type==Tile.ID.OneWayRight){
				if(shape.contains(hero.x,hero.y) || shape.contains(hero.x,hero.y+height))
					return false;
				return true;
			}
			if(Labyrinth.hero.dir==Game.Direction.Right && type==Tile.ID.OneWayLeft){
				if(shape.contains(hero.x+width-1,hero.y) ||shape.contains(hero.x+width-1,hero.y+height-1))
					return false;
				return true;
			}
			return false;
	}
}
