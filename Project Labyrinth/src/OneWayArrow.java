import java.awt.Graphics;


public class OneWayArrow  extends Tile{

	public OneWayArrow(int x, int y, int type) {
		super(x, y, type);
		isSolid=false;
		getImage();
	}
	public void render(Graphics g){
		g.drawImage(img,x,y,null);	
	}
	private void getImage(){
		switch(type){
		case 15:	img=Game.game_tileset.get(Tile.ID.OneWayUp.value);
					break;
		case 20:	img=Game.game_tileset.get(Tile.ID.OneWayLeft.value);
					break;
		case 21:	img=Game.game_tileset.get(Tile.ID.OneWayRight.value);
					break;
		case 28:	img=Game.game_tileset.get(Tile.ID.OneWayDown.value);
					break;
		}
	}
	public boolean checkArrow() {
		Character hero=Character.getInstance();
			if(Character.getInstance().dir==Game.Direction.Down && type==Tile.ID.OneWayUp.value){
				if(shape.contains(hero.getX(),hero.getY()+height-1) || shape.contains(hero.getX()+width,hero.getY()+height-1))
					return false;//allow pass
				return true; 
			}
			if(Character.getInstance().dir==Game.Direction.Up && type==Tile.ID.OneWayDown.value){
				if(shape.contains(hero.getX(),hero.getY()) || shape.contains(hero.getX()+width,hero.getY()))
					return false;
				return true;
			}
			if(Character.getInstance().dir==Game.Direction.Left && type==Tile.ID.OneWayRight.value){
				if(shape.contains(hero.getX(),hero.getY()) || shape.contains(hero.getX(),hero.getY()+height))
					return false;
				return true;
			}
			if(Character.getInstance().dir==Game.Direction.Right && type==Tile.ID.OneWayLeft.value){
				if(shape.contains(hero.getX()+width-1,hero.getY()) ||shape.contains(hero.getX()+width-1,hero.getY()+height-1))
					return false;
				return true;
			}
			return false;
	}
}
