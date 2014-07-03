import java.awt.Graphics;
public class OneWayArrow  extends Tile{

	public OneWayArrow(int x, int y, ID type) {
		super(x, y, type);
		isSolid=false;
		getImage();
	}
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
		}
	}
	public boolean checkArrow() {
		Character hero=Character.getInstance();
			if(Character.getInstance().dir==Game.Direction.Down && type==Tile.ID.OneWayUp){
				if(shape.contains(hero.getX(),hero.getY()+height-1) || shape.contains(hero.getX()+width,hero.getY()+height-1))
					return false;//allow pass
				return true; 
			}
			if(Character.getInstance().dir==Game.Direction.Up && type==Tile.ID.OneWayDown){
				if(shape.contains(hero.getX(),hero.getY()) || shape.contains(hero.getX()+width,hero.getY()))
					return false;
				return true;
			}
			if(Character.getInstance().dir==Game.Direction.Left && type==Tile.ID.OneWayRight){
				if(shape.contains(hero.getX(),hero.getY()) || shape.contains(hero.getX(),hero.getY()+height))
					return false;
				return true;
			}
			if(Character.getInstance().dir==Game.Direction.Right && type==Tile.ID.OneWayLeft){
				if(shape.contains(hero.getX()+width-1,hero.getY()) ||shape.contains(hero.getX()+width-1,hero.getY()+height-1))
					return false;
				return true;
			}
			return false;
	}
}
